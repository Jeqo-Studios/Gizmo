package net.jeqo.gizmo.Commands;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.Managers.Commands.SubCommands;
import net.jeqo.gizmo.Utils.ColourUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GizmoHelpCommand implements SubCommands {

    private final Gizmo plugin;

    private final ColourUtils colourUtils = new ColourUtils();

    public GizmoHelpCommand(Gizmo plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (!player.hasPermission("gizmo.help")) {
            player.sendMessage(colourUtils.oldFormat(plugin.configManager.getLang().getString("no-permission")));
            return;
        }

        player.sendMessage(colourUtils.oldFormat("#ee0000Usages:"));
        player.sendMessage(colourUtils.oldFormat("&f/gizmo fade <player> <in> <stay> <out> &7- Displays a fade."));
        player.sendMessage(colourUtils.oldFormat("&f/gizmo show <player> &7- Force displays the welcome screen."));
        player.sendMessage(colourUtils.oldFormat("&f/gizmo reload &7- Reloads the Gizmo configs (not recommended)."));
    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
