package net.jeqo.gizmo.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.data.Utilities;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Objects;

public class Prime implements Listener {

    Gizmo plugin = Gizmo.getPlugin(Gizmo.class);
    String shift12 = plugin.getConfig().getString("Unicodes.shift-12");
    String shift36 = plugin.getConfig().getString("Unicodes.shift-36");
    String shift256 = plugin.getConfig().getString("Unicodes.shift-256");
    String shift501 = plugin.getConfig().getString("Unicodes.shift-501");

    public static HashMap<String, ItemStack[]> saveInv = new HashMap<>();

    public static Boolean screening;

    @EventHandler
    public void onPackAccept(PlayerResourcePackStatusEvent e) {
        Player p = e.getPlayer();

        if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {

            screening = true;

            if (Objects.equals(plugin.getConfig().getString("Sound-on-Pack-Load.enable"), "true")) {
                p.playSound(p.getLocation(), Sound.valueOf(plugin.getConfig().getString("Sound-on-Pack-Load.sound")), Float.parseFloat(Objects.requireNonNull(plugin.getConfig().getString("Sound-on-Pack-Load.volume"))), Float.parseFloat(Objects.requireNonNull(plugin.getConfig().getString("Sound-on-Pack-Load.pitch"))));
            }


            if (p.getGameMode().equals(GameMode.SURVIVAL) || p.getGameMode().equals(GameMode.ADVENTURE)) {
                p.teleport(getLocation(p));
            }


            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {

                    saveInv.put(p.getName(), p.getInventory().getContents());
                    p.getInventory().clear();

                    if (Objects.equals(plugin.getConfig().getString("enable-welcome-screen"), "true")) {
                        e.getPlayer().setGameMode(GameMode.SPECTATOR);
                        if (Objects.equals(plugin.getConfig().getString("enable-background"), "true")) {
                            e.getPlayer().openInventory(plugin.getServer().createInventory(null, 54, ChatColor.WHITE + shift256 + shift256 + plugin.getConfig().getString("Unicodes.background") + shift501 + shift36 + plugin.getConfig().getString("Unicodes.welcome-screen")));
                        } else if (Objects.equals(plugin.getConfig().getString("enable-background"), "false")) {
                            e.getPlayer().openInventory(plugin.getServer().createInventory(null, 54, ChatColor.WHITE + shift36 + shift12 + plugin.getConfig().getString("Unicodes.welcome-screen")));
                        }
                    } else {
                        if (Objects.equals(plugin.getConfig().getString("enable-fade"), "true")) {
                            if (Objects.equals(plugin.getConfig().getString("fade-mode"), "A")) {
                                p.sendTitle((String) plugin.getConfig().get("Unicodes.background"), "", 0, 5, plugin.getConfig().getInt("fade-time"));
                            } else if (Objects.equals(plugin.getConfig().getString("fade-mode"), "B")) {
                                e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, plugin.getConfig().getInt("fade-time"), 1, false, false));
                            }
                        }
                    }

                    }
            }, 5);
        } else if (e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD || e.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED) {
            p.setGameMode(Protect.joinGm);
            p.removePotionEffect(PotionEffectType.BLINDNESS);
            p.sendMessage(PlaceholderAPI.setPlaceholders(p.getPlayer(), Utilities.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.no-pack-loaded"))));
        }
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){

        if (Objects.equals(plugin.getConfig().getString("hide-join-messages"), String.valueOf(true))) {
            e.setJoinMessage("");
        }

    }

    @EventHandler
    public void restoreInv(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        if(e.getView().getTitle().equals(ChatColor.WHITE + shift256 + shift256 + plugin.getConfig().getString("Unicodes.background") + shift501 + shift36 + plugin.getConfig().getString("Unicodes.welcome-screen")) || e.getView().getTitle().equals(ChatColor.WHITE +  shift36 + shift12 + plugin.getConfig().getString("Unicodes.welcome-screen"))) {
            p.getInventory().setContents((ItemStack[]) saveInv.get(p.getName()));
        }
    }


    public Location getLocation(Player player){
        Location location = player.getLocation().clone();

        while (!location.getBlock().getRelative(BlockFace.DOWN).getType().isSolid() && location.getY() != 0){
            location.add(0, -1, 0);
        }

        if (location.getY() == 0){
            return player.getLocation();
        }

        location.add(0, 2.25, 0);
        return location;
    }
}
