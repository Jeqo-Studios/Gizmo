package net.jeqo.gizmo.listeners;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.data.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static net.jeqo.gizmo.data.Placeholders.*;
import static net.jeqo.gizmo.data.Utilities.*;

public class PlayerScreening implements Listener {


    static Gizmo plugin = Gizmo.getPlugin(Gizmo.class);
    public static HashMap<String, ItemStack[]> saveInv = new HashMap<>();
    public static HashMap<UUID, Boolean> playersScreenActive = new HashMap<>();



    // Resource pack status event
    @EventHandler
    public boolean onPackAccept(PlayerResourcePackStatusEvent e) {
        Player p = e.getPlayer();
        switch (e.getStatus()) {
            case ACCEPTED:
                // Display the background unicode during the delay
                if (plugin.getConfig().getString("delay-background").equals("true")) {
                    p.sendTitle(pullConfig("background-color") + (String) pullScreensConfig("Unicodes.background"), "", 0, 999999, 0);
                }
                break;
            case SUCCESSFULLY_LOADED:
                // Play a configured sound when the pack is loaded
                if (Objects.equals(plugin.getConfig().getString("sound-on-pack-load.enable"), "true")) {
                    p.playSound(p.getLocation(), Sound.valueOf(plugin.getConfig().getString("sound-on-pack-load.sound")), Float.parseFloat(Objects.requireNonNull(plugin.getConfig().getString("sound-on-pack-load.volume"))), Float.parseFloat(Objects.requireNonNull(plugin.getConfig().getString("sound-on-pack-load.pitch"))));
                }
                // Display first time welcome screen
                if (!p.hasPlayedBefore()) {
                    if (pullScreensConfig("first-join-welcome-screen").equalsIgnoreCase("true")) {
                        welcomeScreenInitial(p);
                        return false;
                    }
                }
                // Display the screen once per restart
                if (pullScreensConfig("once-per-restart").equals("true")) {
                    // Check if the player has already seen the screen this server session
                    if (Gizmo.playerTracker.get(p.getUniqueId()) == null) {
                        Gizmo.playerTracker.put(p.getUniqueId(), String.valueOf(1));
                        welcomeScreen(p);
                    }
                } else if (pullScreensConfig("once-per-restart").equals("false")) {
                    welcomeScreen(p);
                }
                break;
            case DECLINED:
                break;
            case FAILED_DOWNLOAD:
                // Debug mode check; if enabled it will still send the player the welcome screen
                if (plugin.getConfig().getString("debug-mode").equalsIgnoreCase("true")) {
                    p.sendMessage(Utilities.chatTranslate(gizmoPrefix() + "#acb5bfNo server resource pack detected."));
                    p.sendMessage(Utilities.chatTranslate(gizmoPrefix() + "#acb5bfDebug mode is enabled."));
                    welcomeScreen(p);
                } else {
                    p.removePotionEffect(PotionEffectType.BLINDNESS);
                    if (Objects.equals(pullMessagesConfig("no-pack-loaded"), "[]")) {
                        return false;
                    } else {
                        for (String msg : plugin.getMessagesConfig().getStringList("no-pack-loaded")) {
                            p.sendMessage(Utilities.chatTranslate(msg));
                        }
                    }
                }
                break;
        }
        return false;
    }




    // Welcome screen
    public static void welcomeScreen(Player e) {

        // Store the player's ID and set the screen to active
        playersScreenActive.put(e.getUniqueId(), true);
        // Store and clear the player's inventory
        saveInv.put(e.getPlayer().getName(), e.getPlayer().getInventory().getContents());
        e.getPlayer().getInventory().clear();


        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {

                // Begin the screen sequence
                // check if screens.yml enable-welcome-screen = true
                if (pullScreensConfig("enable-welcome-screen").equalsIgnoreCase("true")) {

                        InventoryView screen = e.getPlayer().openInventory(plugin.getServer().createInventory(null, 54, screenTitle()));

                        if (pullScreensConfig("Items") != null) {
                            for (String key : Objects.requireNonNull(plugin.getScreensConfig().getConfigurationSection("Items")).getKeys(false)) {
                                ConfigurationSection keySection = Objects.requireNonNull(plugin.getScreensConfig().getConfigurationSection("Items")).getConfigurationSection(key);
                                assert keySection != null;
                                int slot = keySection.getInt("slot");
                                ItemStack item = new ItemStack(Objects.requireNonNull(Material.matchMaterial(Objects.requireNonNull(keySection.getString("material")))));
                                ItemMeta meta = item.getItemMeta();
                                if (pullScreensConfig("Items." + key + ".lore") != null) {
                                    List<String> lore = keySection.getStringList("lore");
                                    for (int i = 0; i < lore.size(); i++) {
                                        lore.set(i, Utilities.chatTranslate(lore.get(i)));
                                    }
                                    assert meta != null;
                                    meta.setLore(lore);
                                }
                                assert meta != null;
                                if (keySection.getString("hide-flags") == String.valueOf(true)) {
                                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                                    meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                    meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                                    meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                                    meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                                }
                                meta.setCustomModelData(keySection.getInt("custom-model-data"));
                                meta.setDisplayName(Utilities.chatTranslate(keySection.getString("name")));
                                item.setItemMeta(meta);
                                assert screen != null;
                                screen.setItem(slot, item);
                            }
                        }

                } else {
                    return;
                }
            }
        }, plugin.getConfig().getInt("delay"));
    }




    // Welcome screen first join
    public void welcomeScreenInitial(Player e) {

        // Store the player's ID and set the screen to active
        playersScreenActive.put(e.getUniqueId(), true);
        // Store and clear the player's inventory
        saveInv.put(e.getPlayer().getName(), e.getPlayer().getInventory().getContents());
        e.getPlayer().getInventory().clear();

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {

                // Begin the screen sequence
                // check if screens.yml enable-first-join-welcome-screen = true
                if (Objects.equals(pullScreensConfig("enable-first-join-welcome-screen"), "true")) {

                        InventoryView screen = e.getPlayer().openInventory(plugin.getServer().createInventory(null, 54, screenTitleFirstJoin()));

                        if (pullScreensConfig("First-Join-Items") != null) {
                            for (String key : Objects.requireNonNull(plugin.getScreensConfig().getConfigurationSection("First-Join-Items")).getKeys(false)) {
                                ConfigurationSection keySection = Objects.requireNonNull(plugin.getScreensConfig().getConfigurationSection("First-Join-Items")).getConfigurationSection(key);
                                assert keySection != null;
                                int slot = keySection.getInt("slot");
                                ItemStack item = new ItemStack(Objects.requireNonNull(Material.matchMaterial(Objects.requireNonNull(keySection.getString("material")))));
                                ItemMeta meta = item.getItemMeta();
                                if (pullScreensConfig("First-Join-Items." + key + ".lore") != null) {
                                    List<String> lore = keySection.getStringList("lore");
                                    for (int i = 0; i < lore.size(); i++) {
                                        lore.set(i, Utilities.chatTranslate(lore.get(i)));
                                    }
                                    assert meta != null;
                                    meta.setLore(lore);
                                }
                                assert meta != null;
                                if (Objects.equals(keySection.getString("hide-flags"), String.valueOf(true))) {
                                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                                    meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                    meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                                    meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
                                    meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                                }
                                meta.setCustomModelData(keySection.getInt("custom-model-data"));
                                meta.setDisplayName(Utilities.chatTranslate(keySection.getString("name")));
                                item.setItemMeta(meta);
                                assert screen != null;
                                screen.setItem(slot, item);
                            }
                        }
                } else {
                    return;
                }
            }
        }, plugin.getConfig().getInt("delay"));
    }
}
