package net.jeqo.gizmo.Managers;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.Utils.ColourUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static net.jeqo.gizmo.data.Placeholders.screenTitle;
import static net.jeqo.gizmo.data.Placeholders.screenTitleFirstJoin;

public class ScreeningManager {

    private final Gizmo plugin;

    public HashMap<String, ItemStack[]> saveInv = new HashMap<>();
    public HashMap<UUID, Boolean> playersScreenActive = new HashMap<>();

    private final ColourUtils colourUtils = new ColourUtils();

    public ScreeningManager(Gizmo plugin) {
        this.plugin = plugin;
    }

    // Welcome screen
    public void welcomeScreen(Player player) {
        // Store the player's ID and set the screen to active
        playersScreenActive.put(player.getUniqueId(), true);

        // Store and clear the player's inventory
        saveInv.put(player.getPlayer().getName(), player.getPlayer().getInventory().getContents());

        player.getPlayer().getInventory().clear();

        // Begin the screen sequence
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            // check if screens.yml enable-welcome-screen = true
            if (!plugin.configManager.getScreens().getBoolean("enable-welcome-screen")) return;

            InventoryView screen = player.getPlayer().openInventory(plugin.getServer().createInventory(null, 54, screenTitle()));

            if (plugin.configManager.getScreens().get("Items") != null) {
                for (String key : plugin.configManager.getScreens().getConfigurationSection("Items").getKeys(false)) {

                    int slot = plugin.configManager.getScreens().getInt("Items." + key + ".slot");
                    ItemStack item = new ItemStack(Material.matchMaterial(plugin.configManager.getScreens().getString("Items." + key + ".material")));
                    ItemMeta meta = item.getItemMeta();

                    if (plugin.configManager.getScreens().get("Items." + key + ".lore") != null) {
                        List<String> lore = plugin.configManager.getScreens().getStringList("Items." + key + ".lore");

                        for (int i = 0; i < lore.size(); i++) {
                            lore.set(i, colourUtils.oldFormat(lore.get(i)));
                        }

                        meta.setLore(lore);
                    }

                    if (plugin.configManager.getScreens().getBoolean("Items." + key + ".hide-flags")) {
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                    }

                    meta.setCustomModelData(plugin.configManager.getScreens().getInt("Items." + key + ".custom-model-data"));
                    meta.setDisplayName(colourUtils.oldFormat(plugin.configManager.getScreens().getString("Items." + key + ".name")));
                    item.setItemMeta(meta);

                    screen.setItem(slot, item);
                }
            }
        }, plugin.configManager.getConfig().getInt("delay"));
    }

    // Welcome screen first join
    public void welcomeScreenInitial(Player player) {
        // Store the player's ID and set the screen to active
        plugin.screeningManager.playersScreenActive.put(player.getUniqueId(), true);
        // Store and clear the player's inventory
        plugin.screeningManager.saveInv.put(player.getPlayer().getName(), player.getPlayer().getInventory().getContents());
        player.getPlayer().getInventory().clear();

        // Begin the screen sequence
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            // check if screens.yml enable-first-join-welcome-screen = true
            if (!plugin.configManager.getScreens().getBoolean("enable-first-join-welcome-screen")) return;

            InventoryView screen = player.getPlayer().openInventory(plugin.getServer().createInventory(null, 54, screenTitleFirstJoin()));

            if (plugin.configManager.getScreens().get("First-Join-Items") != null) {
                for (String key : plugin.configManager.getScreens().getConfigurationSection("First-Join-Items").getKeys(false)) {

                    int slot = plugin.configManager.getScreens().getInt("First-Join-Items" + key + ".slot");
                    ItemStack item = new ItemStack(Material.matchMaterial(plugin.configManager.getScreens().getString("First-Join-Items." + key + ".material")));
                    ItemMeta meta = item.getItemMeta();

                    if (plugin.configManager.getScreens().get("First-Join-Items." + key + ".lore") != null) {
                        List<String> lore = plugin.configManager.getScreens().getStringList("First-Join-Items." + key + ".lore");
                        for (int i = 0; i < lore.size(); i++) {
                            lore.set(i, colourUtils.oldFormat(lore.get(i)));
                        }

                        meta.setLore(lore);
                    }

                    if (plugin.configManager.getScreens().getBoolean("First-Join-Items." + key + ".hide-flags")) {
                        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                        meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                    }

                    meta.setCustomModelData(plugin.configManager.getScreens().getInt("First-Join-Items." + key + ".custom-model-data"));
                    meta.setDisplayName(colourUtils.oldFormat(plugin.configManager.getScreens().getString("First-Join-Items." + key + ".name")));
                    item.setItemMeta(meta);

                    screen.setItem(slot, item);
                }
            }
        }, plugin.configManager.getConfig().getInt("delay"));
    }
}
