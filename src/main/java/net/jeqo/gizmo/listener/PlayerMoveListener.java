package net.jeqo.gizmo.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (ScreenDisplayHandler.playersScreenActive.get(p.getUniqueId()) != null) {
            if (ScreenDisplayHandler.playersScreenActive.get(p.getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }
}
