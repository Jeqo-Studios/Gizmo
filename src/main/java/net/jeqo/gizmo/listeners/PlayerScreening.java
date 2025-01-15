package net.jeqo.gizmo.listeners;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.utils.MessageTranslations;
import net.jeqo.gizmo.utils.Utilities;
import net.jeqo.gizmo.utils.Configurations;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
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

import static net.jeqo.gizmo.utils.Placeholders.*;
import static net.jeqo.gizmo.utils.Utilities.*;

public class PlayerScreening implements Listener {
    public static HashMap<String, ItemStack[]> saveInv = new HashMap<>();
    public static HashMap<UUID, Boolean> playersScreenActive = new HashMap<>();
    MessageTranslations messageTranslations = new MessageTranslations(Gizmo.getInstance());

    // Resource pack status event
    @EventHandler
    public boolean onPackAccept(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();
        switch (event.getStatus()) {
            case ACCEPTED:
                // Display the background unicode during the delay
                if (Gizmo.getInstance().getConfig().getString("delay-background").equals("true")) {
                    Title title = Title.title(messageTranslations.getSerializedString(pullConfig("background-color") + pullScreensConfig("Unicodes.background")), Component.text(""));
                    player.sendTitle(pullConfig("background-color") + pullScreensConfig("Unicodes.background"), "", 0, 999999, 0);
                }
                break;
            case SUCCESSFULLY_LOADED:
                // Play a configured sound when the pack is loaded
                if (Objects.equals(Gizmo.getInstance().getConfig().getString("sound-on-pack-load.enable"), "true")) {
                    player.playSound(player.getLocation(), Sound.valueOf(Gizmo.getInstance().getConfig().getString("sound-on-pack-load.sound")), Float.parseFloat(Objects.requireNonNull(Gizmo.getInstance().getConfig().getString("sound-on-pack-load.volume"))), Float.parseFloat(Objects.requireNonNull(Gizmo.getInstance().getConfig().getString("sound-on-pack-load.pitch"))));
                }
                // Display first time welcome screen
                if (!player.hasPlayedBefore()) {
                    if (pullScreensConfig("first-join-welcome-screen").equalsIgnoreCase("true")) {
                        welcomeScreenInitial(player);
                        return false;
                    }
                }
                // Display the screen once per restart
                if (pullScreensConfig("once-per-restart").equals("true")) {
                    // Check if the player has already seen the screen this server session
                    if (Gizmo.playerTracker.get(player.getUniqueId()) == null) {
                        Gizmo.getPlayerTracker().put(player.getUniqueId(), String.valueOf(1));
                        welcomeScreen(player);
                    }
                } else if (pullScreensConfig("once-per-restart").equals("false")) {
                    welcomeScreen(player);
                }
                break;
            case DECLINED:
            case FAILED_DOWNLOAD:
                // Debug mode check; if enabled it will still send the player the welcome screen
                if (Gizmo.getInstance().getConfig().getString("debug-mode").equalsIgnoreCase("true")) {
                    player.sendMessage(Utilities.chatTranslate(gizmoPrefix() + "#acb5bfNo server resource pack detected and/or debug mode is enabled."));
                    player.sendMessage(Utilities.chatTranslate(gizmoPrefix() + "#acb5bfSending welcome screen..."));
                    welcomeScreen(player);
                } else {
                    player.removePotionEffect(PotionEffectType.BLINDNESS);
                    if (Objects.equals(pullMessagesConfig("no-pack-loaded"), "[]")) {
                        return false;
                    } else {
                        for (String msg : Configurations.getMessagesConfig().getStringList("no-pack-loaded")) {
                            player.sendMessage(Utilities.chatTranslate(msg));
                        }
                    }
                }
                break;
        }
        return false;
    }

    public static void welcomeScreen(Player e) {

        // Store the player's ID and set the screen to active
        playersScreenActive.put(e.getUniqueId(), true);
        // Store and clear the player's inventory
        saveInv.put(e.getPlayer().getName(), e.getPlayer().getInventory().getContents());
        e.getPlayer().getInventory().clear();


        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Gizmo.getInstance(), new Runnable() {
            public void run() {

                // Begin the screen sequence
                // check if screens.yml enable-welcome-screen = true
                if (pullScreensConfig("enable-welcome-screen").equalsIgnoreCase("true")) {

                    InventoryView screen = e.getPlayer().openInventory(Gizmo.getInstance().getServer().createInventory(null, 54, screenTitle()));

                    if (pullScreensConfig("Items") != null) {
                        for (String key : Objects.requireNonNull(Configurations.getScreensConfig().getConfigurationSection("Items")).getKeys(false)) {
                            ConfigurationSection keySection = Objects.requireNonNull(Configurations.getScreensConfig().getConfigurationSection("Items")).getConfigurationSection(key);
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
        }, Gizmo.getInstance().getConfig().getInt("delay"));
    }

    // Welcome screen first join
    public void welcomeScreenInitial(Player e) {

        // Store the player's ID and set the screen to active
        playersScreenActive.put(e.getUniqueId(), true);
        // Store and clear the player's inventory
        saveInv.put(e.getPlayer().getName(), e.getPlayer().getInventory().getContents());
        e.getPlayer().getInventory().clear();

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Gizmo.getInstance(), () -> {

            // Begin the screen sequence
            // check if screens.yml enable-first-join-welcome-screen = true
            if (Objects.equals(pullScreensConfig("enable-first-join-welcome-screen"), "true")) {

                    InventoryView screen = e.getPlayer().openInventory(Gizmo.getInstance().getServer().createInventory(null, 54, screenTitleFirstJoin()));

                    if (pullScreensConfig("First-Join-Items") != null) {
                        for (String key : Objects.requireNonNull(Configurations.getScreensConfig().getConfigurationSection("First-Join-Items")).getKeys(false)) {
                            ConfigurationSection keySection = Objects.requireNonNull(Configurations.getScreensConfig().getConfigurationSection("First-Join-Items")).getConfigurationSection(key);
                            assert keySection != null;
                            int slot = keySection.getInt("slot");
                            ItemStack item = new ItemStack(Objects.requireNonNull(Material.matchMaterial(Objects.requireNonNull(keySection.getString("material")))));
                            ItemMeta meta = item.getItemMeta();
                            if (pullScreensConfig("First-Join-Items." + key + ".lore") != null) {
                                List<String> lore = keySection.getStringList("lore");
                                for (int i = 0; i < lore.size(); i++) {
                                    lore.set(i, Utilities.chatTranslate(lore.get(i)));
                                }
                                meta.setLore(lore);
                            }
                            if (Objects.equals(keySection.getString("hide-flags"), String.valueOf(true))) {
                                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                                meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                                meta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
                                meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
                            }
                            meta.setCustomModelData(keySection.getInt("custom-model-data"));
                            meta.setDisplayName(Utilities.chatTranslate(keySection.getString("name")));
                            item.setItemMeta(meta);
                            assert screen != null;
                            screen.setItem(slot, item);
                        }
                    }
            }
        }, Gizmo.getInstance().getConfig().getInt("delay"));
    }
}
