package net.jeqo.gizmo.listeners;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.data.Placeholders;
import net.jeqo.gizmo.data.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

import static net.jeqo.gizmo.data.Placeholders.screenTitle;

public class ClickableItems implements Listener {
    static Gizmo plugin = Gizmo.getPlugin(Gizmo.class);

    @EventHandler
    public void onCommandItemClick(InventoryClickEvent e) {

        if (e.getView().getTitle().equals(screenTitle())) {

            Player p = (Player) e.getWhoClicked();

            ItemStack clickedItem = e.getCurrentItem();
            int rawSlot = e.getRawSlot();

            for (String key : plugin.getScreensConfig().getConfigurationSection("Items").getKeys(false)) {
                if (plugin.getScreensConfig().getInt("Items." + key + ".slot") == rawSlot) {
                    if (plugin.getScreensConfig().getString("Items." + key + ".commands") != null) {
                        if (plugin.getScreensConfig().getString("Items." + key + ".close-on-click").equals("true")) {
                            p.closeInventory();
                        }
                        for (String command : plugin.getScreensConfig().getStringList("Items." + key + ".commands")) {
                            if (command.contains("[console]")) {
                                command = command.replace("[console] ", "");
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", p.getName()));
                            } else if (command.contains("[message]")) {
                                command = command.replace("[message] ", "");
                                p.sendMessage(Utilities.chatTranslate(command.replace("%player%", p.getName())));
                            } else if (command.contains("[player]")) {
                                command = command.replace("[player] ", "");
                                p.performCommand(command);
                            } else {
                                p.sendMessage(Placeholders.gizmoPrefix() + "An error occurred. Please review the console for more information.");
                                Utilities.warn("\"" + key + "\"" + " (screens.yml) has a command with an invalid format.");
                            }
                        }

                    }
                }
            }
        }
    }
}
