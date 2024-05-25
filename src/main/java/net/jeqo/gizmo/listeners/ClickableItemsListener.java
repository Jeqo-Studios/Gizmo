package net.jeqo.gizmo.listeners;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.Utils.ColourUtils;
import net.jeqo.gizmo.data.Placeholders;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class ClickableItemsListener implements Listener {

    private final Gizmo plugin;

    private final ColourUtils colourUtils = new ColourUtils();

    public ClickableItemsListener(Gizmo plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onCommandItemClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(plugin.configManager.screenTitle())) return;

        Player player = (Player) event.getWhoClicked();

        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) return;

        int rawSlot = event.getRawSlot();

        for (String key : plugin.configManager.getScreens().getConfigurationSection("Items").getKeys(false)) {
            if (plugin.configManager.getScreens().getInt("Items." + key + ".slot") == rawSlot) {
                if (plugin.configManager.getScreens().getString("Items." + key + ".commands") != null) {
                    if (plugin.configManager.getScreens().getString("Items." + key + ".close-on-click").equals("true")) {
                        player.closeInventory();
                    }

                    for (String command : plugin.configManager.getScreens().getStringList("Items." + key + ".commands")) {
                        if (command.contains("[console]")) {
                            command = command.replace("[console] ", "");
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
                        } else if (command.contains("[message]")) {
                            command = command.replace("[message] ", "");
                            player.sendMessage(colourUtils.oldFormat(command.replace("%player%", player.getName())));
                        } else if (command.contains("[player]")) {
                            command = command.replace("[player] ", "");
                            player.performCommand(command);
                        } else {
                            player.sendMessage(Placeholders.gizmoPrefix() + "An error occurred. Please review the console for more information.");
                            plugin.getLogger().warning("\"" + key + "\"" + " (screens.yml) has a command with an invalid format.");
                        }
                    }
                }
            }
        }
    }
}
