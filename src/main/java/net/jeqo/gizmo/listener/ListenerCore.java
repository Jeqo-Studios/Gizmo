package net.jeqo.gizmo.listener;

import net.jeqo.gizmo.Gizmo;
import org.bukkit.Bukkit;

/**
 * Handles all core functionality in regard to the listeners
 */
public class ListenerCore {

    /**
     * Initializes all listeners for the Gizmo plugin and registers them with the Bukkit plugin manager.
     */
    public void initialize() {
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(), Gizmo.getInstance());
        Bukkit.getPluginManager().registerEvents(new ItemClickListener(), Gizmo.getInstance());
        Bukkit.getPluginManager().registerEvents(new ItemPickupListener(), Gizmo.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerDamageListener(), Gizmo.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerItemDamageListener(), Gizmo.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), Gizmo.getInstance());
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), Gizmo.getInstance());
        Bukkit.getPluginManager().registerEvents(new ResourcePackDeclineListener(), Gizmo.getInstance());
        Bukkit.getPluginManager().registerEvents(new RestoreInventoryListener(), Gizmo.getInstance());
        Bukkit.getPluginManager().registerEvents(new ScreenAdvanceListener(), Gizmo.getInstance());
        Bukkit.getPluginManager().registerEvents(new ScreenDisplayHandler(), Gizmo.getInstance());
        Bukkit.getPluginManager().registerEvents(new SlotClickListener(), Gizmo.getInstance());
    }
}
