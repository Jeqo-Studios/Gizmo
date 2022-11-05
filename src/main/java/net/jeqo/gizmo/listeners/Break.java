package net.jeqo.gizmo.listeners;

import net.jeqo.gizmo.Gizmo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class Break implements Listener {

    Gizmo plugin = Gizmo.getPlugin(Gizmo.class);

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {

        if (plugin.getConfig().getString("hide-quit-messages") == String.valueOf(true)) {
            e.setQuitMessage("");
        }

    }

}
