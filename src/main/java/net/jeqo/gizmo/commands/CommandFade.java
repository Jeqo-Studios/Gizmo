package net.jeqo.gizmo.commands;

import net.jeqo.gizmo.commands.manager.Command;
import net.jeqo.gizmo.commands.manager.enums.CommandPermission;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static net.jeqo.gizmo.utils.Placeholders.gizmoPrefix;
import static net.jeqo.gizmo.utils.Utilities.*;

public class CommandFade extends Command {

    public CommandFade(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("fade");
        this.setCommandDescription("Displays a fullscreen fade with a solid color.");
        this.setCommandSyntax("/gizmo fade <in> <stay> <out> [player]");
        this.setRequiredPermission(CommandPermission.FADE);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length == 5) {
            Player target = Bukkit.getPlayer(args[4]);
            if (target == null) {
                player.sendMessage(chatTranslate(gizmoPrefix() + "&7That player is not online!"));
                return true;
            }

            if (args[1].matches("[0-9]+") && args[2].matches("[0-9]+") && args[3].matches("[0-9]+")) {
                target.sendTitle(pullScreensConfig("Unicodes.background"), "", Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            } else {
                player.sendMessage(chatTranslate(gizmoPrefix() + "&7Only numbers can be used for time values!"));
            }
        } else if (args.length == 4) {
            if (args[1].matches("[0-9]+") && args[2].matches("[0-9]+") && args[3].matches("[0-9]+")) {
                player.sendTitle(pullScreensConfig("Unicodes.background"), "", Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
            } else {
                player.sendMessage(chatTranslate(gizmoPrefix() + "&7Only numbers can be used for time values!"));
            }

        } else {
            player.sendMessage(chatTranslate(gizmoPrefix() + "&7Usage: /gizmo fade <in> <stay> <out> [player]"));
        }
        return false;
    }
}
