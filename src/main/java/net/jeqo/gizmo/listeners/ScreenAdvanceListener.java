package net.jeqo.gizmo.listeners;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.data.Placeholders;
import net.jeqo.gizmo.data.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static net.jeqo.gizmo.data.Placeholders.screenTitle;
import static net.jeqo.gizmo.data.Utilities.pullConfig;
import static net.jeqo.gizmo.data.Utilities.pullScreensConfig;


public class ScreenAdvanceListener implements Listener {

    private final Gizmo plugin;

    private final Set<UUID> processingPlayers = new HashSet<>();

    public ScreenAdvanceListener(Gizmo plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (!event.getView().getTitle().equals(screenTitle())) return;
        if (!plugin.screeningManager.playersScreenActive.get(player.getUniqueId())) return;

        if (plugin.configManager.getConfig().getBoolean("enable-fade")) {
            player.sendTitle(Utilities.chatTranslate(pullConfig("background-color") + pullScreensConfig("Unicodes.background")), "", 0, 5, plugin.getConfig().getInt("fade-time"));
        }

        if (processingPlayers.contains(player.getUniqueId())) return;
        processingPlayers.add(player.getUniqueId());

        try {
            if (plugin.configManager.getConfig().getBoolean("sound-on-advance.enable")) {
                player.playSound(player.getLocation(), Sound.valueOf(plugin.getConfig().getString("sound-on-advance.sound")),
                        Float.parseFloat(plugin.getConfig().getString("sound-on-advance.volume")),
                        Float.parseFloat(plugin.getConfig().getString("sound-on-advance.pitch")));
            }
        } catch (NullPointerException ex) {
            Utilities.warn("sound-on-advance is not configured correctly.");
        }

        try {
            plugin.configManager.getConfig().getStringList("commands-on-advance").forEach(command -> {
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
                    Utilities.warn("Commands-on-advance (config.yml) has a command with an invalid format.");
                }
            });
        } finally {
            processingPlayers.remove(player.getUniqueId());
            plugin.screeningManager.playersScreenActive.remove(player.getUniqueId());
        }

        if (!player.hasPlayedBefore()) {
            if (plugin.configManager.getScreens().getBoolean("first-join-welcome-screen")) {
                welcomeMessageFirstJoin(player);
            }
        } else {
            welcomeMessage(player);
        }
    }

    private void welcomeMessage(Player player) {
        String welcomeMessage = (plugin.configManager.getLang().getString("welcome-message"));

        if (welcomeMessage.equals("[]")) return;

        welcomeMessage = welcomeMessage.replace(", ", "\n").replace("[", "").replace("]", "");
        player.sendMessage(Utilities.chatTranslate(welcomeMessage));
    }

    private void welcomeMessageFirstJoin(Player player) {
        String welcomeMessage = (plugin.configManager.getLang().getString("first-join-welcome-message"));

        if (welcomeMessage.equals("[]")) return;

        welcomeMessage = welcomeMessage.replace(", ", "\n").replace("[", "").replace("]", "");
        player.sendMessage(Utilities.chatTranslate(welcomeMessage));
    }
}