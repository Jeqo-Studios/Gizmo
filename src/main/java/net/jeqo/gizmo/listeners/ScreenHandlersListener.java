package net.jeqo.gizmo.listeners;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.Utils.ColourUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.potion.PotionEffectType;

public class ScreenHandlersListener implements Listener {

    private final Gizmo plugin;

    private final ColourUtils colourUtils = new ColourUtils();

    public ScreenHandlersListener(Gizmo plugin) {
        this.plugin = plugin;
    }

    // Player join handlers
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Check and give blindness effect
        if (!plugin.configManager.getConfig().getBoolean("blindness-during-prompt")) return;
        event.getPlayer().addPotionEffect(PotionEffectType.BLINDNESS.createEffect(999999, 1));
    }

    // Resource pack status handler
    @EventHandler
    public void onPackLoad(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();

        if (plugin.configManager.getConfig().getBoolean("kick-on-decline")) {
            if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                disableEffects(player);
            } else if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED || event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                disableEffects(player);
                player.kickPlayer(colourUtils.oldFormat(plugin.configManager.getConfig().getString("kick-on-decline")).replace(",", "\n").replace("[", "").replace("]", ""));
            }
        } else if (!plugin.configManager.getConfig().getBoolean("kick-on-decline")) {
            if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                disableEffects(player);
            } else if (event.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED || event.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                disableEffects(player);
                for (String msg : plugin.configManager.getConfig().getStringList("no-pack-loaded")) {
                    player.sendMessage(colourUtils.oldFormat(msg));
                }
            }
        }
    }

    // Restore player inventory event
    @EventHandler
    public void restoreInv(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (!event.getView().getTitle().equals(plugin.configManager.screenTitle()) || !event.getView().getTitle().equals(plugin.configManager.screenTitleFirstJoin())) return;

        player.getInventory().setContents(plugin.screeningManager.saveInv.get(player.getName()));
    }

    // Disable all potion effects
    private void disableEffects(Player player) {
        for (PotionEffectType effect : PotionEffectType.values()) {
            if (!player.hasPotionEffect(effect)) continue;
            player.removePotionEffect(effect);
        }
    }

    // Disabled events while screen is active
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (plugin.screeningManager.playersScreenActive.get(player.getUniqueId()) == null) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        Entity entity = event.getEntity();

        if (!(entity instanceof Player player)) return;
        if (plugin.screeningManager.playersScreenActive.get(player.getUniqueId()) == null) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onSlotClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (plugin.screeningManager.playersScreenActive.get(player.getUniqueId()) == null) return;

        event.setCancelled(true);
    }

    // Toggleable damage events
    @EventHandler
    public void onPlayerDamage(EntityDamageByBlockEvent event) {
        Entity entity = event.getEntity();

        if (!plugin.configManager.getConfig().getBoolean("player-invulnerable-during-load")) return;

        if (!(entity instanceof Player player)) return;
        if (plugin.screeningManager.playersScreenActive.get(player.getUniqueId()) == null) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();

        if (!plugin.configManager.getConfig().getBoolean("player-invulnerable-during-load")) return;

        if (!(entity instanceof Player player)) return;
        if (plugin.screeningManager.playersScreenActive.get(player.getUniqueId()) == null) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent event) {
        if (!plugin.configManager.getConfig().getBoolean("player-invulnerable-during-load")) return;

        Player player = event.getPlayer();
        if (plugin.screeningManager.playersScreenActive.get(player.getUniqueId()) == null) return;

        event.setCancelled(true);
    }
}
