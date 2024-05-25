package net.jeqo.gizmo.Commands;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.Managers.Commands.SubCommands;
import net.jeqo.gizmo.Utils.ColourUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GizmoShowCommand implements SubCommands {

    private final Gizmo plugin;

    private final ColourUtils colourUtils = new ColourUtils();

    public GizmoShowCommand(Gizmo plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (!player.hasPermission("gizmo.show")) {
            player.sendMessage(colourUtils.oldFormat(plugin.configManager.getLang().getString("no-permission")));
            return;
        }

        if (args.length == 0) {
            player.sendMessage(colourUtils.oldFormat("&7Usage: /gizmo show [player]"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(colourUtils.oldFormat(plugin.configManager.getLang().getString("prefix") + "&7That player is not online!"));
            return;
        }

        plugin.screeningManager.welcomeScreen(player);
        player.sendMessage(colourUtils.oldFormat(plugin.configManager.getLang().getString("prefix") + plugin.configManager.getLang().getString("show-screen-others")));
    }

    @Override
    public String name() {
        return "show";
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
