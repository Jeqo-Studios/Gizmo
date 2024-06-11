package net.jeqo.gizmo.commands;

import net.jeqo.gizmo.commands.manager.Command;
import net.jeqo.gizmo.commands.manager.enums.CommandPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import static net.jeqo.gizmo.commands.manager.utils.ErrorHandling.usage;

public class CommandHelp extends Command {

    public CommandHelp(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("help");
        this.setCommandDescription("Gets help for commands for Gizmo");
        this.setCommandSyntax("/gizmo help");
        this.setRequiredPermission(CommandPermission.HELP);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        usage(sender);

        return false;
    }
}