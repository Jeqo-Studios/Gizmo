package net.jeqo.gizmo.command;

import net.jeqo.gizmo.command.commands.GizmoCommandsCore;
import net.jeqo.gizmo.configuration.PluginConfiguration;
import net.jeqo.gizmo.logger.Logger;
import net.jeqo.gizmo.logger.LoggingLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A class with utilities to manage the core functionality regarding
 * commands registered and used on a server.
 */
@Getter
@Setter
public class CommandCore implements CommandExecutor, TabCompleter {
    private ArrayList<GizmoCommand> commands;
    private JavaPlugin plugin;

    /**
     * Created a new instance of the command core to access
     * the command utilities
     * @param plugin The plugin instance to register the commands to
     */
    public CommandCore(JavaPlugin plugin) {
        if (plugin == null) {
            Logger.log(LoggingLevel.ERROR, "The " + PluginConfiguration.NAME + " plugin cannot be null. Please provide a valid instance.");
        }

        this.commands = new ArrayList<>();
        this.plugin = plugin;
    }

    /**
     * Initializes the command core by staging all commands.
     * This is done here to reduce the size of Gizmo.
     */
    public void initialize() {
        GizmoCommandsCore gizmoCommandsCore = new GizmoCommandsCore();

        gizmoCommandsCore.initialize();
    }

    /**
     * Stages a command to be registered to the plugin
     * @param command The command to stage
     */
    public void stageCommand(GizmoCommand command) {
        this.commands.add(command);
    }

    /**
     * Registers all staged commands to the plugin
     */
    public void registerCommands() {
        for (GizmoCommand command : this.getCommands()) {
            List<String> aliases = new ArrayList<>();
            if (command.aliases() != null) {
                aliases.addAll(command.aliases());
            }
            if (!aliases.contains(command.name())) {
                aliases.add(command.name());
            }

            for (String alias : aliases) {
                if (this.getPlugin().getCommand(alias) == null) {
                    Logger.log(LoggingLevel.ERROR, "The command alias '" + alias + "' is not registered in the plugin.yml file. Please add it to the file.");
                } else {
                    // Cast and set usage to be nothing so it can appear in help commands and in the proper plugin.yml
                    PluginCommand registeredCommand = (PluginCommand) Objects.requireNonNull(this.getPlugin().getCommand(alias)).setUsage("");
                    Objects.requireNonNull(registeredCommand).setExecutor(this);
                    Objects.requireNonNull(registeredCommand).setTabCompleter(this);
                }
            }
        }
    }

    /**
     * Executes the command, returning whether the command was successful
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return Whether the command was handled successfully
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        for (GizmoCommand gizmoCommand : this.getCommands()) {
            List<String> aliases = gizmoCommand.aliases() == null ? List.of() : gizmoCommand.aliases();

            // Check if the command matches either the name or one of its aliases
            if (command.getName().equalsIgnoreCase(gizmoCommand.name()) || aliases.contains(command.getName())) {
                // Check validity of the command execution
                if (!this.isValidExecution(gizmoCommand, sender, args.length)) return false;

                // If there are sub command available, skip treating as regular command
                if (!gizmoCommand.subcommands().isEmpty()) {
                    boolean subcommandHandled = false;

                    for (GizmoCommand subcommand : gizmoCommand.subcommands()) {
                        if (subcommand.name().equalsIgnoreCase(args[0]) || subcommand.aliases().contains(args[0])) {
                            subcommandHandled = true;
                            // Check validity of the command execution
                            if (!this.isValidExecution(subcommand, sender, (args.length - 1))) return false;

                            // Execute subcommand with the prefix command itself stripped
                            try {
                                String[] strippedArguments = Arrays.copyOfRange(args, 1, args.length);
                                subcommand.execute(sender, strippedArguments);
                            } catch (Exception e) {
                                sender.sendMessage(ChatColor.RED + "An error occurred while executing the subcommand '/" + subcommand.name() + "'. Please check the syntax and try again.");
                                Logger.log(LoggingLevel.ERROR, e.getMessage());
                            }

                            break;
                        }
                    }

                    // No subcommand matched, handle the main command as a retry-like attempt
                    if (!subcommandHandled) {
                        try {
                            gizmoCommand.execute(sender, args);
                        } catch (Exception e) {
                            sender.sendMessage(ChatColor.RED + "An error occurred while executing the command '/" + gizmoCommand.name() + "'. Please check the syntax and try again.");
                        }
                    }
                    return true;
                }

                // Execute the command and call the event that should be triggered when the command is run
                try {
                    gizmoCommand.execute(sender, args);
                } catch (Exception e) {
                    // In the event of incorrect usage, send an error message to the sender and log the error
                    String errorMessage = "An error occurred while executing the command '"
                            + ChatColor.RED + gizmoCommand.name()
                            + ChatColor.RESET + "'. Incorrect usage, the correct syntax is '"
                            + ChatColor.RED + "/" + gizmoCommand.syntax()
                            + ChatColor.RESET + "'. Please try again.";

                    sender.sendMessage(errorMessage);
                    Logger.log(LoggingLevel.ERROR, "An error occurred while executing the command '" + gizmoCommand.name() + "'. Incorrect usage, the correct syntax is '" + gizmoCommand.syntax() + "'. Please try again.");
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the command execution is valid
     * @param command The command that is being checked
     * @param sender The command sender
     * @param argsLength The number of arguments provided
     * @return A boolean representing if the execution can be carries on
     */
    public boolean isValidExecution(GizmoCommand command, CommandSender sender, int argsLength) {
        if (!this.hasPermission(sender, command)) return false;
        if (!this.isPlayer(sender) && command.isPlayerOnly()) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
            return false;
        }

        if ((argsLength) < command.minArgs() || (argsLength) > command.maxArgs()) {
            sender.sendMessage(ChatColor.RED + "Incorrect usage. The correct syntax is '/" + command.syntax() + "'. Please try again.");
            return false;
        }
        return true;
    }

    /**
     * Checks if the sender has the required permissions to execute the command
     * @param sender The sender of the command
     * @param command The command to check the permissions for
     * @return Whether the sender has the required permissions to execute the command
     */
    public boolean hasPermission(CommandSender sender, GizmoCommand command) {
        if (!(sender instanceof Player playerSender)) {
            return true;
        }
        // Check if the sender has the required permissions to execute the command
        for (CommandPermission permission : command.requiredCommandPermissions()) {
            if (!sender.hasPermission(permission.getPermission())) {
                sender.sendMessage(ChatColor.RED + "The command '/" + command.name() + "' requires the permission '" + permission.getPermission() + "'.");
                return false;
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        for (GizmoCommand gizmoCommand : this.getCommands()) {
            List<String> aliases = gizmoCommand.aliases() == null ? List.of() : gizmoCommand.aliases();
            if (command.getName().equalsIgnoreCase(gizmoCommand.name()) || aliases.contains(command.getName())) {

                // If this command has subcommands
                if (!gizmoCommand.subcommands().isEmpty()) {
                    if (args.length == 1) {
                        String currentInput = args[0].toLowerCase();
                        List<String> subCompletions = new ArrayList<>();
                        for (GizmoCommand sub : gizmoCommand.subcommands()) {
                            if (sub.name().toLowerCase().startsWith(currentInput)) {
                                subCompletions.add(sub.name());
                            }
                            for (String subAlias : sub.aliases()) {
                                if (subAlias.toLowerCase().startsWith(currentInput)) {
                                    subCompletions.add(subAlias);
                                }
                            }
                        }
                        return subCompletions;
                    } else {
                        // Delegate to the matching subcommand's tabCompleteOptions()
                        String subName = args[0];
                        for (GizmoCommand sub : gizmoCommand.subcommands()) {
                            if (sub.name().equalsIgnoreCase(subName) || sub.aliases().contains(subName)) {
                                int argIndex = args.length - 2; // args[1] is index 0 for subcommand args
                                String currentInput = args[args.length - 1].toLowerCase();
                                List<String> completions = new ArrayList<>();
                                for (var entry : sub.tabCompleteOptions().entrySet()) {
                                    if (entry.getValue() == argIndex && entry.getKey().toLowerCase().startsWith(currentInput)) {
                                        completions.add(entry.getKey());
                                    }
                                }
                                return completions;
                            }
                        }
                    }
                }

                // No subcommands - use this command's tabCompleteOptions
                int argIndex = args.length - 1;
                String currentInput = args[argIndex].toLowerCase();
                HashMap<String, Integer> options = gizmoCommand.tabCompleteOptions();
                List<String> completions = new ArrayList<>();
                for (var entry : options.entrySet()) {
                    if (entry.getValue() == argIndex && entry.getKey().toLowerCase().startsWith(currentInput)) {
                        completions.add(entry.getKey());
                    }
                }
                return completions;
            }
        }
        return List.of();
    }


    /**
     * Checks if the sender is a player
     * @param sender The sender of the command
     * @return Whether the sender is a player
     */
    public boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }
}
