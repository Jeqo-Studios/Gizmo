package net.jeqo.gizmo.listener;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

public class ItemPickupListener implements Listener {

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            Player p = (Player) e.getEntity();
            if (ScreenDisplayHandler.playersScreenActive.get(p.getUniqueId()) != null) {
                if (ScreenDisplayHandler.playersScreenActive.get(p.getUniqueId())) {
                    e.setCancelled(true);
                }
            }
        }
    }
}
