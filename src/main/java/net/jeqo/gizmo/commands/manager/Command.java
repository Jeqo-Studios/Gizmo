package net.jeqo.gizmo.commands.manager;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.gizmo.commands.manager.enums.CommandAccess;
import net.jeqo.gizmo.commands.manager.enums.CommandPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

@Getter
public abstract class Command {

    protected JavaPlugin plugin;

    @Setter
    @Getter
    private CommandPermission requiredPermission;
    @Setter @Getter
    private CommandAccess requiredAccess = CommandAccess.ENABLED;
    @Setter @Getter
    private String commandSyntax;
    @Setter @Getter
    public String commandDescription;
    private final ArrayList<String> commandAliases = new ArrayList<>();

    public Command(JavaPlugin providedPlugin) {
        this.plugin = providedPlugin;
    }

    /**
     * Adds alias commands to the current command
     * Note: All aliases added here must be added to the plugin.yml file
     * @param alias The alias to add to the command
     */
    public void addCommandAlias(String alias) {
        this.getCommandAliases().add(alias);
    }

    /**
     * What's executed on the command run
     * @param sender The sender of the command
     * @param args The arguments of the command
     * @return Whether the command was executed successfully
     * @throws Exception If an error occurs during command execution
     */
    public abstract boolean execute(CommandSender sender, String[] args) throws Exception;

    /**
     * Checks if the command meets the requirements to be executed
     * @param sender The sender of the command
     * @param permission The permission required to execute the command
     * @return Whether the command meets the requirements
     */
    public boolean hasRequirement(CommandSender sender, CommandPermission permission) {
        switch (permission) {
            case FADE -> {
                if (sender instanceof Player) {
                    if (!sender.hasPermission("gizmo.fade")) {
                        return false;
                    }
                }
            }
            case HELP -> {
                if (sender instanceof Player) {
                    if (!sender.hasPermission("gizmo.help")) {
                        return false;
                    }
                }
            }
            case RELOAD -> {
                if (sender instanceof Player) {
                    if (!sender.hasPermission("gizmo.reload")) {
                        return false;
                    }
                }
            }
            case SHOW -> {
                if (sender instanceof Player) {
                    if (!sender.hasPermission("gizmo.show")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}