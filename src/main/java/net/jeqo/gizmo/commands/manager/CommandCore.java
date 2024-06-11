package net.jeqo.gizmo.commands.manager;

import lombok.Getter;
import net.jeqo.gizmo.commands.CommandFade;
import net.jeqo.gizmo.commands.CommandHelp;
import net.jeqo.gizmo.commands.CommandReload;
import net.jeqo.gizmo.commands.CommandShow;
import net.jeqo.gizmo.commands.manager.enums.CommandAccess;
import net.jeqo.gizmo.utils.MessageTranslations;
import net.jeqo.gizmo.utils.Placeholders;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static net.jeqo.gizmo.commands.manager.utils.ErrorHandling.usage;

@Getter
public class CommandCore implements CommandExecutor {
    private final ArrayList<Command> commands;
    private final JavaPlugin plugin;
    private final MessageTranslations messageTranslations;

    public CommandCore(JavaPlugin providedPlugin) {
        this.plugin = providedPlugin;
        this.commands = new ArrayList<>();
        this.messageTranslations = new MessageTranslations(this.getPlugin());

        // Add any commands you want registered here
        addCommand(new CommandHelp(this.getPlugin()));
        addCommand(new CommandReload(this.getPlugin()));
        addCommand(new CommandShow(this.getPlugin()));
        addCommand(new CommandFade(this.getPlugin()));

        // Register all commands staged
        registerCommands();

        Objects.requireNonNull(this.getPlugin().getCommand("gizmo")).setTabCompleter(new CommandTabCompleter());
    }

    /**
     * Registers all commands in the commands list
     */
    public void registerCommands() {
        Objects.requireNonNull(this.getPlugin().getCommand("gizmo")).setExecutor(this);
    }

    /**
     * Gets a commands description by its alias
     * @param commandAlias The alias of the command
     * @return The description of the command
     */
    public String getCommandDescription(String commandAlias) {
        for (Command command : this.getCommands()) {
            if (command.getCommandAliases().contains(commandAlias)) {
                return command.getCommandDescription();
            }
        }
        return null;
    }

    /**
     * Adds a command to the commands list
     * @param command The command to add
     */
    public void addCommand(Command command) {
        this.getCommands().add(command);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player player)) {
            Component consoleMessage = Component.text("This command can only be executed by a player.").color(NamedTextColor.RED);
            sender.sendMessage(consoleMessage);
            return true;
        }

        if (args.length == 0) {
            usage(player);
            return true;
        }

        // Define what a subcommand really is
        String subcommand = args[0].toLowerCase();
        String[] subcommandArgs = Arrays.copyOfRange(args, 1, args.length);

        for (Command currentCommand : getCommands()) {
            if (currentCommand.getCommandAliases().contains(subcommand)) {
                if (!meetsRequirements(currentCommand, sender)) {
                    Component noPermissionMessage = this.getMessageTranslations().getSerializedString(Placeholders.gizmoPrefix(), this.getMessageTranslations().getMessage("no-permission"));
                    sender.sendMessage(noPermissionMessage);
                    return false;
                }

                if (currentCommand.getRequiredAccess() == CommandAccess.DISABLED) {
                    Component commandDisabledMessage = Component.text("This command is currently disabled.").color(NamedTextColor.RED);
                    sender.sendMessage(commandDisabledMessage);
                    return false;
                }

                try {
                    currentCommand.execute(sender, subcommandArgs);
                } catch (Exception ignored) {
                }
                return true;
            }
        }

        usage(sender);
        return false;
    }

    /**
     * Checks if the player sending the command meets the requirements to execute the command
     * @param command The command to check
     * @param sender The sender of the command
     * @return Whether the user meets the requirements
     */
    public boolean meetsRequirements(Command command, CommandSender sender) {
        return command.hasRequirement(sender, command.getRequiredPermission());
    }
}
