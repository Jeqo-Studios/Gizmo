package net.jeqo.gizmo.Commands;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.Managers.Commands.SubCommands;
import net.jeqo.gizmo.Utils.ColourUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GizmoFadeCommand implements SubCommands {

    private final Gizmo plugin;

    private final ColourUtils colourUtils = new ColourUtils();

    public GizmoFadeCommand(Gizmo plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (!player.hasPermission("gizmo.fade")) {
            player.sendMessage(colourUtils.oldFormat(plugin.configManager.getLang().getString("no-permission")));
            return;
        }

        if (args.length == 0) {
            player.sendMessage(colourUtils.oldFormat("&7Usage: /gizmo fade [player] <in> <stay> <out>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) {
            player.sendMessage(colourUtils.oldFormat(plugin.configManager.getLang().getString("prefix") + "&7That player is not online!"));
            return;
        }

        if (!args[1].matches("[0-9]+") && !args[2].matches("[0-9]+") && !args[3].matches("[0-9]+")) {
            player.sendMessage(colourUtils.oldFormat(plugin.configManager.getLang().getString("prefix") + "&7Only numbers can be used for time values!"));
            return;
        }

        player.sendTitle(plugin.configManager.getConfig().getString("Unicodes.background"), "", Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
    }

    @Override
    public String name() {
        return "fade";
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
