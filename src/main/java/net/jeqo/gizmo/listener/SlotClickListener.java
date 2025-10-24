package net.jeqo.gizmo.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class SlotClickListener implements Listener {

    @EventHandler
    public void onSlotClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (ScreenDisplayHandler.playersScreenActive.get(p.getUniqueId()) != null) {
            if (ScreenDisplayHandler.playersScreenActive.get(p.getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }
}
