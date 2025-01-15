package net.jeqo.gizmo.listeners;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.logger.Logger;
import net.jeqo.gizmo.utils.Configurations;
import net.jeqo.gizmo.utils.MessageTranslations;
import net.jeqo.gizmo.utils.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import static net.jeqo.gizmo.utils.Placeholders.screenTitle;

public class ClickableItems implements Listener {

    @EventHandler
    public void onCommandItemClick(InventoryClickEvent event) {

        if (event.getView().getTitle().equals(screenTitle())) {

            MessageTranslations messageTranslations = new MessageTranslations(Gizmo.getInstance());
            Player player = (Player) event.getWhoClicked();
            int rawSlot = event.getRawSlot();

            for (String key : Configurations.getScreensConfig().getConfigurationSection("Items").getKeys(false)) {
                if (Configurations.getScreensConfig().getInt("Items." + key + ".slot") == rawSlot) {
                    if (Configurations.getScreensConfig().getString("Items." + key + ".commands") != null) {
                        if (Configurations.getScreensConfig().getString("Items." + key + ".close-on-click").equals("true")) {
                            player.closeInventory();
                        }
                        for (String command : Configurations.getScreensConfig().getStringList("Items." + key + ".commands")) {
                            if (command.contains("[console]")) {
                                command = command.replace("[console] ", "");
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                            } else if (command.contains("[message]")) {
                                command = command.replace("[message] ", "");
                                player.sendMessage(messageTranslations.getSerializedString(command.replace("%player%", player.getName())));
                            } else if (command.contains("[player]")) {
                                command = command.replace("[player] ", "");
                                player.performCommand(command);
                            } else {
                                Logger.logToPlayer(player, Placeholders.gizmoPrefix() + "An error occurred. Please review the console for more information.");
                                Logger.logWarning("\"" + key + "\"" + " (screens.yml) has a command with an invalid format.");
                            }
                        }

                    }
                }
            }
        }
    }
}
