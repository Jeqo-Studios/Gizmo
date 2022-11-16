package net.jeqo.gizmo.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.data.Commands;
import net.jeqo.gizmo.data.Utilities;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;


public class Advance implements Listener {

    Gizmo plugin = Gizmo.getPlugin(Gizmo.class);
    String shift48 = plugin.getConfig().getString("Unicodes.shift-48");
    String shift1013 = plugin.getConfig().getString("Unicodes.shift-1013");
    String shift1536 = plugin.getConfig().getString("Unicodes.shift-1536");


    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        if (e.getView().getTitle().equals(ChatColor.WHITE + shift1013 + plugin.getConfig().getString("Unicodes.background") + shift1536 + plugin.getConfig().getString("Unicodes.welcome-screen")) || e.getView().getTitle().equals(ChatColor.WHITE + shift48 + plugin.getConfig().getString("Unicodes.welcome-screen"))) {

            if (Objects.equals(plugin.getConfig().getString("enable-fade"), "true")) {
                if (Objects.equals(plugin.getConfig().getString("fade-mode"), "A")) {
                    p.sendTitle((String) plugin.getConfig().get("Unicodes.background"), "", 0, 5, plugin.getConfig().getInt("fade-time"));
                } else if (Objects.equals(plugin.getConfig().getString("fade-mode"), "B")) {
                    e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, plugin.getConfig().getInt("fade-time"), 1, false, false));
                }
            }

            try {
                if (Objects.equals(plugin.getConfig().getString("Sound-on-Advance.enable"), "true")) {
                    p.playSound(p.getLocation(), Sound.valueOf(plugin.getConfig().getString("minecraft" + "Sound-on-Advance.sound")), Float.parseFloat(Objects.requireNonNull(plugin.getConfig().getString("Sound-on-Advance.volume"))), Float.parseFloat(Objects.requireNonNull(plugin.getConfig().getString("Sound-on-Advance.pitch"))));
                }
            } catch (NullPointerException ex) {
                Utilities.warn("Sound-on-Advance is not configured correctly.");
            }

            Prime.screening = false;

            if (Protect.joinGm != null) {
                p.setGameMode(Protect.joinGm);
            } else {
                p.setGameMode(Commands.showGm);
            }


            if (!p.hasPlayedBefore()) {
                if (plugin.getConfig().getString("first-join-welcome-screen").equalsIgnoreCase("true")) {
                    welcomeMessageInitial(p);
                    return;
                }
            } else {
                welcomeMessage(p);
            }



            if (Objects.equals(plugin.getConfig().getString("Command-on-Advance.enable"), "true")) {
                p.performCommand(Objects.requireNonNull(plugin.getConfig().getString("Command-on-Advance.command")));
            }
        }
    }



    private void welcomeMessage(Player p) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            String welcomeMessage = (plugin.getConfig().getString("welcome-message"));
            assert welcomeMessage != null;
            if (welcomeMessage.equals("[]")) {
            } else {
                welcomeMessage = PlaceholderAPI.setPlaceholders((OfflinePlayer) p, welcomeMessage.replace(", ", "\n").replace("[", "").replace("]", ""));
                p.sendMessage(Utilities.hex(welcomeMessage));
            }
        } else {
            String welcomeMessage = (plugin.getConfig().getString("welcome-message"));
            assert welcomeMessage != null;
            if (welcomeMessage.equals("[]")) {
            } else {
                p.sendMessage(Utilities.hex(welcomeMessage.replace(", ", "\n").replace("[", "").replace("]", "")));
            }
        }
    }



    private void welcomeMessageInitial(Player p) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            String welcomeMessage = (plugin.getConfig().getString("first-join-welcome-message"));
            assert welcomeMessage != null;
            if (welcomeMessage.equals("[]")) {
            } else {
                welcomeMessage = PlaceholderAPI.setPlaceholders((OfflinePlayer) p, welcomeMessage.replace(", ", "\n").replace("[", "").replace("]", ""));
                p.sendMessage(Utilities.hex(welcomeMessage));
            }
        } else {
            String welcomeMessage = (plugin.getConfig().getString("first-join-welcome-message"));
            assert welcomeMessage != null;
            if (welcomeMessage.equals("[]")) {
            } else {
                p.sendMessage(Utilities.hex(welcomeMessage.replace(", ", "\n").replace("[", "").replace("]", "")));
            }
        }
    }


    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        Player p = (Player) e.getPlayer();

        if ((Objects.equals(plugin.getConfig().getString("Auto-Close.enable"), "true"))) {
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    if (Prime.screening.equals(true)) {
                        p.closeInventory();
                    }
                }
            }, Long.parseLong(Objects.requireNonNull(plugin.getConfig().getString("Auto-Close.time"))));
        }
    }
}