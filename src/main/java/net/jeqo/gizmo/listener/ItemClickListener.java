package net.jeqo.gizmo.listener;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.configuration.ConfigurationCore;
import net.jeqo.gizmo.data.Placeholders;
import net.jeqo.gizmo.data.ChatTranslate;
import net.jeqo.gizmo.logger.Logger;
import net.jeqo.gizmo.logger.LoggingLevel;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Set;

import static net.jeqo.gizmo.data.Placeholders.screenTitle;

public class ItemClickListener implements Listener {

    @EventHandler
    public void onCommandItemClick(InventoryClickEvent e) {
        String sectionName;
        if (e.getView().getTitle().equals(screenTitle())) {
            sectionName = "Items";
        } else if (e.getView().getTitle().equals(Placeholders.screenTitleFirstJoin())) {
            sectionName = "First-Join-Items";
        } else {
            return;
        }

        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        int rawSlot = e.getRawSlot();

        if (clickedItem == null || clickedItem.getType().isAir()) return;

        ConfigurationCore config = Gizmo.getInstance().getScreensConfigurationCore();
        Set<String> itemKeys = config.getSectionKeys(sectionName);
        if (itemKeys == null) return;

        for (String key : itemKeys) {
            String basePath = "Items." + key;

            // Slot check (optional, but more precise)
            if (config.contains(basePath + ".slot") && config.getInteger(basePath + ".slot") != rawSlot) continue;

            // Build expected ItemStack from config
            Material material = Material.matchMaterial(config.getString(basePath + ".material"));
            if (material == null) continue;

            ItemStack expectedItem = new ItemStack(material);
            ItemMeta meta = expectedItem.getItemMeta();
            if (meta == null) continue;

            if (config.contains(basePath + ".name")) {
                meta.setDisplayName(ChatTranslate.translate(null, config.getString(basePath + ".name")));
            }

            if (config.contains(basePath + ".lore")) {
                List<String> lore = config.getStringList(basePath + ".lore").stream()
                        .map(s -> ChatTranslate.translate(null, s))
                        .toList();
                meta.setLore(lore);
            }

            if (config.contains(basePath + ".custom-model-data")) {
                meta.setCustomModelData(config.getInteger(basePath + ".custom-model-data"));
            }

            if (config.getBoolean(basePath + ".hide-flags")) {
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS,
                        ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_UNBREAKABLE);
            }

            expectedItem.setItemMeta(meta);

            if (!clickedItem.isSimilar(expectedItem)) continue;

            if (config.getBoolean(basePath + ".close-on-click")) {
                p.closeInventory();
            }

            List<String> commands = config.getStringList(basePath + ".commands");
            for (String command : commands) {
                command = command.replace("%player%", p.getName());

                if (command.startsWith("[console] ")) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("[console] ", ""));
                } else if (command.startsWith("[message] ")) {
                    p.sendMessage(ChatTranslate.translate(null, command.replace("[message] ", "")));
                } else if (command.startsWith("[player] ")) {
                    p.performCommand(command.replace("[player] ", ""));
                } else {
                    p.sendMessage(Placeholders.gizmoPrefix() + "An error occurred. Please review the console for more information.");
                    Logger.log(LoggingLevel.ERROR, "\"" + key + "\" (screens.yml) has a command with an invalid format.");
                }
            }

            break;
        }
    }
}
