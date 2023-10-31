package net.jeqo.gizmo.listeners;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.data.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import java.util.Objects;

import static net.jeqo.gizmo.data.Placeholders.screenTitle;
import static net.jeqo.gizmo.data.Utilities.pullConfig;
import static net.jeqo.gizmo.data.Utilities.pullScreensConfig;


public class ScreenAdvance implements Listener {
    Gizmo plugin = Gizmo.getPlugin(Gizmo.class);


    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        if (e.getView().getTitle().equals(screenTitle())) {

            if (Objects.equals(plugin.getConfig().getString("enable-fade"), "true")) {
                p.sendTitle(Utilities.chatTranslate(pullConfig("background-color") + pullScreensConfig("Unicodes.background")), "", 0, 5, plugin.getConfig().getInt("fade-time"));
            } else {
                p.sendTitle("", "", 0, 0, 0);
            }


            try {
                if (Objects.equals(plugin.getConfig().getString("sound-on-advance.enable"), "true")) {
                    p.playSound(p.getLocation(), Sound.valueOf(plugin.getConfig().getString("sound-on-advance.sound")), Float.parseFloat(Objects.requireNonNull(plugin.getConfig().getString("sound-on-advance.volume"))), Float.parseFloat(Objects.requireNonNull(plugin.getConfig().getString("sound-on-advance.pitch"))));
                }
            } catch (NullPointerException ex) {
                Utilities.warn("sound-on-advance is not configured correctly.");
            }


            PlayerScreening.playersScreenActive.remove(p.getUniqueId());


            if (pullConfig("player-command-on-advance.enable").equals("true")) {
                for (String command : plugin.getConfig().getStringList("player-command-on-advance.commands")) {
                    p.performCommand(command.replace("%player%", p.getName()));
                }
            }

            if (pullConfig("console-command-on-advance.enable").equals("true")) {
                for (String command : plugin.getConfig().getStringList("console-command-on-advance.commands")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", p.getName()));
                }
            }


            if (!p.hasPlayedBefore()) {
                if (plugin.getScreensConfig().getString("first-join-welcome-screen").equalsIgnoreCase("true")) {
                    welcomeMessageFirstJoin(p);
                }
            } else {
                welcomeMessage(p);
            }
        }
    }



    private void welcomeMessage(Player p) {
        String welcomeMessage = (plugin.getMessagesConfig().getString("welcome-message"));
        assert welcomeMessage != null;
        if (welcomeMessage.equals("[]")) {
        } else {
            welcomeMessage = welcomeMessage.replace(", ", "\n").replace("[", "").replace("]", "");
            p.sendMessage(Utilities.chatTranslate(welcomeMessage));
        }
    }



    private void welcomeMessageFirstJoin(Player p) {
        String welcomeMessage = (plugin.getMessagesConfig().getString("first-join-welcome-message"));
        assert welcomeMessage != null;
        if (welcomeMessage.equals("[]")) {
            return;
        } else {
            welcomeMessage = welcomeMessage.replace(", ", "\n").replace("[", "").replace("]", "");
            p.sendMessage(Utilities.chatTranslate(welcomeMessage));
        }
    }
}