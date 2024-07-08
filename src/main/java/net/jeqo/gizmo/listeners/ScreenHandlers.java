package net.jeqo.gizmo.listeners;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.data.Utilities;
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import java.util.Objects;

import static net.jeqo.gizmo.data.Placeholders.screenTitle;
import static net.jeqo.gizmo.data.Placeholders.screenTitleFirstJoin;
import static net.jeqo.gizmo.listeners.PlayerScreening.saveInv;

public class ScreenHandlers implements Listener {

    Gizmo plugin = Gizmo.getPlugin(Gizmo.class);



    // Player join handlers
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        // Check and give blindness effect
        if (Objects.equals(plugin.getConfig().getString("blindness-during-prompt"), "true")) {
            e.getPlayer().addPotionEffect(PotionEffectType.BLINDNESS.createEffect(999999, 1));
        }
    }


    // Resource pack status handler
    @EventHandler
    public void onPackLoad(PlayerResourcePackStatusEvent e) {
        Player p = e.getPlayer();

        if (Objects.equals(plugin.getConfig().getString("kick-on-decline"), "true")) {
            if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                disableEffects(p);
            } else if (e.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED || e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                disableEffects(p);
                p.kickPlayer(Utilities.chatTranslate(Objects.requireNonNull(plugin.getConfig().getString("messages.kick-on-decline")).replace(",", "\n").replace("[", "").replace("]", "")));
            }
        } else if (Objects.equals(plugin.getConfig().getString("kick-on-decline"), "false")) {
            if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                disableEffects(p);
            } else if (e.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED || e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                disableEffects(p);
                for (String msg : plugin.getConfig().getStringList("messages.no-pack-loaded")) {
                    p.sendMessage(Utilities.chatTranslate(msg));
                }
            }
        }
    }



    // Restore player inventory event
    @EventHandler
    public void restoreInv(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (e.getView().getTitle().equals(screenTitle()) || e.getView().getTitle().equals(screenTitleFirstJoin())) {
            p.getInventory().setContents((ItemStack[]) saveInv.get(p.getName()));
        }
    }





    // Disable all potion effects
    private void disableEffects(Player p) {

        if (Objects.equals(plugin.getConfig().getString("clear-effects-on-load"), "false")){
            p.removePotionEffect(PotionEffectType.BLINDNESS);
            return;
        }


        for (PotionEffectType effect : PotionEffectType.values()) {
            if (p.hasPotionEffect(effect)) {
                p.removePotionEffect(effect);
            }
        }
    }





    // Disabled events while screen is active
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (PlayerScreening.playersScreenActive.get(p.getUniqueId()) != null) {
            if (PlayerScreening.playersScreenActive.get(p.getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            Player p = (Player) e.getEntity();
            if (PlayerScreening.playersScreenActive.get(p.getUniqueId()) != null) {
                if (PlayerScreening.playersScreenActive.get(p.getUniqueId())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onSlotClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (PlayerScreening.playersScreenActive.get(p.getUniqueId()) != null) {
            if (PlayerScreening.playersScreenActive.get(p.getUniqueId())) {
                e.setCancelled(true);
            }
        }
    }





    // Toggleable damage events
    @EventHandler
    public void onPlayerDamage(EntityDamageByBlockEvent e) {
        Entity entity = e.getEntity();
        if (Objects.equals(plugin.getConfig().getString("player-invulnerable-during-load"), "true")) {
            if (entity instanceof Player) {
                Player p = (Player) e.getEntity();
                if (PlayerScreening.playersScreenActive.get(p.getUniqueId()) != null) {
                    if (PlayerScreening.playersScreenActive.get(p.getUniqueId())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        Entity entity = e.getEntity();
        if (Objects.equals(plugin.getConfig().getString("player-invulnerable-during-load"), "true")) {
            if (entity instanceof Player) {
                Player p = (Player) e.getEntity();
                if (PlayerScreening.playersScreenActive.get(p.getUniqueId()) != null) {
                    if (PlayerScreening.playersScreenActive.get(p.getUniqueId())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerItemDamage(PlayerItemDamageEvent e) {
        if (Objects.equals(plugin.getConfig().getString("player-invulnerable-during-load"), "true")) {
            Player p = (Player) e.getPlayer();
            if (PlayerScreening.playersScreenActive.get(p.getUniqueId()) != null) {
                if (PlayerScreening.playersScreenActive.get(p.getUniqueId())) {
                    e.setCancelled(true);
                }
            }
        }
    }



}
