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

    private final Gizmo plugin;

    public ClickableItems(Gizmo plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommandItemClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(screenTitle())) return;

        Player p = (Player) event.getWhoClicked();

        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) return;

        int rawSlot = event.getRawSlot();

        for (String key : plugin.configManager.getScreens().getConfigurationSection("Items").getKeys(false)) {
            if (plugin.configManager.getScreens().getInt("Items." + key + ".slot") == rawSlot) {
                if (plugin.configManager.getScreens().getString("Items." + key + ".commands") != null) {
                    if (plugin.configManager.getScreens().getString("Items." + key + ".close-on-click").equals("true")) {
                        p.closeInventory();
                    }

                    for (String command : plugin.configManager.getScreens().getStringList("Items." + key + ".commands")) {
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
