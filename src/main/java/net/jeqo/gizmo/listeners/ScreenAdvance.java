package net.jeqo.gizmo.listeners;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.logger.Logger;
import net.jeqo.gizmo.utils.Configurations;
import net.jeqo.gizmo.utils.Placeholders;
import net.jeqo.gizmo.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

import static net.jeqo.gizmo.utils.Placeholders.screenTitle;
import static net.jeqo.gizmo.utils.Utilities.pullConfig;
import static net.jeqo.gizmo.utils.Utilities.pullScreensConfig;

public class ScreenAdvance implements Listener {
    Gizmo plugin = Gizmo.getPlugin(Gizmo.class);

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (event.getView().getTitle().equals(screenTitle())) {

            if (Objects.equals(plugin.getConfig().getString("enable-fade"), "true")) {
                player.sendTitle(Utilities.chatTranslate(pullConfig("background-color") + pullScreensConfig("Unicodes.background")), "", 0, 5, plugin.getConfig().getInt("fade-time"));
            } else {
                player.sendTitle("", "", 0, 0, 0);
            }


            try {
                if (Objects.equals(plugin.getConfig().getString("sound-on-advance.enable"), "true")) {
                    player.playSound(player.getLocation(), Sound.valueOf(plugin.getConfig().getString("sound-on-advance.sound")), Float.parseFloat(Objects.requireNonNull(plugin.getConfig().getString("sound-on-advance.volume"))), Float.parseFloat(Objects.requireNonNull(plugin.getConfig().getString("sound-on-advance.pitch"))));
                }
            } catch (NullPointerException ex) {
                Logger.logWarning("sound-on-advance is not configured correctly.");
            }


            PlayerScreening.playersScreenActive.remove(player.getUniqueId());
            player.removePotionEffect(PotionEffectType.BLINDNESS);


            for (String command : plugin.getConfig().getStringList("commands-on-advance")) {
                if (command.contains("[console]")) {
                    command = command.replace("[console] ", "");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                } else if (command.contains("[message]")) {
                    command = command.replace("[message] ", "");
                    player.sendMessage(Utilities.chatTranslate(command.replace("%player%", player.getName())));
                } else if (command.contains("[player]")) {
                    command = command.replace("[player] ", "");
                    player.performCommand(command);
                } else {
                    player.sendMessage(Placeholders.gizmoPrefix() + "An error occurred. Please review the console for more information.");
                    Logger.logWarning("Commands-on-advance (config.yml) has a command with an invalid format.");
                }
            }


            if (!player.hasPlayedBefore()) {
                if (Objects.requireNonNull(Configurations.getScreensConfig().getString("first-join-welcome-screen")).equalsIgnoreCase("true")) {
                    welcomeMessageFirstJoin(player);
                }
            } else {
                welcomeMessage(player);
            }
        }
    }

    private void welcomeMessage(Player player) {
        String welcomeMessage = (Configurations.getMessagesConfig().getString("welcome-message"));
        if (!welcomeMessage.equals("[]")) {
            welcomeMessage = welcomeMessage.replace(", ", "\n").replace("[", "").replace("]", "");
            player.sendMessage(Utilities.chatTranslate(welcomeMessage));
        }
    }

    private void welcomeMessageFirstJoin(Player player) {
        String welcomeMessage = (Configurations.getMessagesConfig().getString("first-join-welcome-message"));
        if (welcomeMessage.equals("[]")) {
            welcomeMessage = welcomeMessage.replace(", ", "\n").replace("[", "").replace("]", "");
            player.sendMessage(Utilities.chatTranslate(welcomeMessage));
        }
    }
}