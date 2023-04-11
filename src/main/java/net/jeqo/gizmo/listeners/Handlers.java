package net.jeqo.gizmo.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.data.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class Handlers implements Listener {

    Gizmo plugin = Gizmo.getPlugin(Gizmo.class);

    public static Location joinLoc;


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (!Objects.equals(plugin.getConfig().getString("enable-welcome-screen"), "false")) {
            if (Objects.equals(plugin.getConfig().getString("blindness-during-prompt"), "true")) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999, 1, false, false));
            }
            if (Objects.equals(plugin.getConfig().getString("player-invulnerable-during-load"), "true")) {

            }
        }

    }



    @EventHandler
    public void onPackLoad(PlayerResourcePackStatusEvent e) {
        Player p = e.getPlayer();

        if (Objects.equals(plugin.getConfig().getString("kick-on-decline"), "true")) {
            if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                {
                    disableEffects(p);
                }

            } else if (e.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED || e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                disableEffects(p);
                p.kickPlayer(Utils.hex(Objects.requireNonNull(plugin.getConfig().getString("messages.kick-on-decline")).replace(",", "\n").replace("[", "").replace("]", "")));
            }


        } else if (Objects.equals(plugin.getConfig().getString("kick-on-decline"), "false")) {
            if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                disableEffects(p);
            } else if (e.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED || e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                disableEffects(p);
                if (plugin.papiLoaded()) {
                    for (String msg : plugin.getConfig().getStringList("messages.no-pack-loaded")) {
                        p.sendMessage(PlaceholderAPI.setPlaceholders(p, Utils.hex(msg)));
                    }
                } else {
                    for (String msg : plugin.getConfig().getStringList("messages.no-pack-loaded")) {
                        p.sendMessage(Utils.hex(msg));
                    }
                }
            }
        }
    }





    private void disableEffects(Player p) {
        p.removePotionEffect(PotionEffectType.BLINDNESS);
        p.removePotionEffect(PotionEffectType.INVISIBILITY);
    }





    // All handlers for the player while they are being screened
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {

        Entity entity = e.getEntity();

        if (Objects.equals(plugin.getConfig().getString("player-invulnerable-during-load"), "true")) {
            if (entity instanceof Player) {
                Player p = (Player) e.getEntity();
                if (Initiate.screeningPlayers.get(p.getUniqueId()) != null) {
                    if (Initiate.screeningPlayers.get(p.getUniqueId())) {
                        e.setCancelled(true);
                    }
                }
            }
        }

    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByBlockEvent e) {

        Entity entity = e.getEntity();

        if (Objects.equals(plugin.getConfig().getString("player-invulnerable-during-load"), "true")) {
            if (entity instanceof Player) {
                Player p = (Player) e.getEntity();
                if (Initiate.screeningPlayers.get(p.getUniqueId()) != null) {
                    if (Initiate.screeningPlayers.get(p.getUniqueId())) {
                        e.setCancelled(true);
                    }
                }
            }
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        Player p = e.getPlayer();

        if (Objects.equals(plugin.getConfig().getString("player-invulnerable-during-load"), "true")) {
            if (Initiate.screeningPlayers.get(p.getUniqueId()) != null) {
                if (Initiate.screeningPlayers.get(p.getUniqueId())) {
                    e.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent e) {

            Entity entity = e.getEntity();

            if (Objects.equals(plugin.getConfig().getString("player-invulnerable-during-load"), "true")) {
                if (entity instanceof Player) {
                    Player p = (Player) e.getEntity();
                    if (Initiate.screeningPlayers.get(p.getUniqueId()) != null) {
                        if (Initiate.screeningPlayers.get(p.getUniqueId())) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
    }

    @EventHandler
    public void onSlotClick(InventoryClickEvent e) {

        if (Objects.equals(plugin.getConfig().getString("player-invulnerable-during-load"), "true")) {
                Player p = (Player) e.getWhoClicked();
                if (Initiate.screeningPlayers.get(p.getUniqueId()) != null) {
                    if (Initiate.screeningPlayers.get(p.getUniqueId())) {
                        e.setCancelled(true);
                    }
                }
        }
    }
}

    // End of handlers for the player while they are being screened

