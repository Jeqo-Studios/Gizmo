package net.jeqo.gizmo.listeners;

import net.jeqo.gizmo.Gizmo;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

import java.util.Objects;

public class DamageListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByBlockEvent event) {
        Entity entity = event.getEntity();
        if (Objects.equals(Gizmo.getInstance().getConfig().getString("player-invulnerable-during-load"), "true")) {
            if (entity instanceof Player) {
                Player player = (Player) event.getEntity();
                if (PlayerScreening.playersScreenActive.get(player.getUniqueId()) != null) {
                    if (PlayerScreening.playersScreenActive.get(player.getUniqueId())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (Objects.equals(Gizmo.getInstance().getConfig().getString("player-invulnerable-during-load"), "true")) {
            if (entity instanceof Player) {
                Player player = (Player) event.getEntity();
                if (PlayerScreening.playersScreenActive.get(player.getUniqueId()) != null) {
                    if (PlayerScreening.playersScreenActive.get(player.getUniqueId())) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        if (Objects.equals(Gizmo.getInstance().getConfig().getString("player-invulnerable-during-load"), "true")) {
            Player player = event.getPlayer();
            if (PlayerScreening.playersScreenActive.get(player.getUniqueId()) != null) {
                if (PlayerScreening.playersScreenActive.get(player.getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
