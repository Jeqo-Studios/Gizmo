package net.jeqo.gizmo.Managers;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.data.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static net.jeqo.gizmo.data.Placeholders.screenTitle;

public class ScreeningManager {

    private final Gizmo plugin;

    public HashMap<String, ItemStack[]> saveInv = new HashMap<>();
    public HashMap<UUID, Boolean> playersScreenActive = new HashMap<>();

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
                            lore.set(i, Utilities.chatTranslate(lore.get(i)));
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
                    meta.setDisplayName(Utilities.chatTranslate(plugin.configManager.getScreens().getString("Items." + key + ".name")));
                    item.setItemMeta(meta);

                    screen.setItem(slot, item);
                }
            }
        }, plugin.configManager.getConfig().getInt("delay"));
    }
}
