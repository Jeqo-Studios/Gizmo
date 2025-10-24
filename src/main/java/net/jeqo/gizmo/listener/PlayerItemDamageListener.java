package net.jeqo.gizmo.listener;

import net.jeqo.gizmo.Gizmo;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;

import java.util.Objects;

public class PlayerItemDamageListener implements Listener {

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent e) {
        if (Objects.equals(Gizmo.getInstance().getConfig().getString("player-invulnerable-during-load"), "true")) {
            Player p = e.getPlayer();
            if (ScreenDisplayHandler.playersScreenActive.get(p.getUniqueId()) != null) {
                if (ScreenDisplayHandler.playersScreenActive.get(p.getUniqueId())) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
