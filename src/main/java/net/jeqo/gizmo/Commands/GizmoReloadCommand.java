package net.jeqo.gizmo.Commands;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.Managers.Commands.SubCommands;
import net.jeqo.gizmo.Utils.ColourUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

import static net.jeqo.gizmo.data.Placeholders.gizmoPrefix;

public class GizmoReloadCommand implements SubCommands {

    private final Gizmo plugin;

    private final ColourUtils colourUtils = new ColourUtils();

    public GizmoReloadCommand(Gizmo plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onCommand(Player player, String[] args) {
        if (!player.hasPermission("gizmo.reload")) {
            player.sendMessage(colourUtils.oldFormat(plugin.configManager.getLang().getString("no-permission")));
            return;
        }

        plugin.configManager.load();
        player.sendMessage(colourUtils.oldFormat(gizmoPrefix() + plugin.configManager.getLang().getString("config-reloaded")));
    }

    @Override
    public String name() {
        return "reload";
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
