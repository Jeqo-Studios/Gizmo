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

    private final Gizmo plugin;

    public PlayerScreening(Gizmo plugin) {
        this.plugin = plugin;
    }

    // Resource pack status event
    @EventHandler
    public boolean onPackAccept(PlayerResourcePackStatusEvent event) {
        Player player = event.getPlayer();

        switch (event.getStatus()) {
            case ACCEPTED:
                // Display the background unicode during the delay
                if (!plugin.configManager.getConfig().getBoolean("delay-background")) return true;
                player.sendTitle(pullConfig("background-color") + pullScreensConfig("Unicodes.background"), "", 0, 999999, 0);
                break;
            case SUCCESSFULLY_LOADED:
                // Play a configured sound when the pack is loaded
                if (plugin.configManager.getConfig().getBoolean("sound-on-pack-load.enable")) {
                    player.playSound(player.getLocation(), Sound.valueOf(plugin.getConfig().getString("sound-on-pack-load.sound")),
                            Float.parseFloat(plugin.getConfig().getString("sound-on-pack-load.volume")),
                            Float.parseFloat(plugin.getConfig().getString("sound-on-pack-load.pitch")));
                }

                // Display first time welcome screen
                if (!player.hasPlayedBefore()) {
                    if (plugin.configManager.getScreens().getBoolean("first-join-welcome-screen")) {
                        welcomeScreenInitial(player);
                        return false;
                    }
                }

                // Display the screen once per restart
                if (plugin.configManager.getScreens().getBoolean("once-per-restart")) {
                    // Check if the player has already seen the screen this server session
                    if (plugin.playerManager.playerTracker.get(player.getUniqueId()) == null) {
                        plugin.playerManager.playerTracker.put(player.getUniqueId(), String.valueOf(1));
                        plugin.screeningManager.welcomeScreen(player);
                    }
                } else if (!plugin.configManager.getScreens().getBoolean("once-per-restart")) {
                    plugin.screeningManager.welcomeScreen(player);
                }

                break;
            case DECLINED:
            case FAILED_DOWNLOAD:
                // Debug mode check; if enabled it will still send the player the welcome screen
                if (plugin.configManager.getConfig().getBoolean("debug-mode")) {
                    player.sendMessage(Utilities.chatTranslate(gizmoPrefix() + "#acb5bfNo server resource pack detected and/or debug mode is enabled."));
                    player.sendMessage(Utilities.chatTranslate(gizmoPrefix() + "#acb5bfSending welcome screen..."));
                    plugin.screeningManager.welcomeScreen(player);
                } else {
                    player.removePotionEffect(PotionEffectType.BLINDNESS);

                    //TODO fix this
                    if (Objects.equals(pullMessagesConfig("no-pack-loaded"), "[]")) {
                        return false;
                    } else {
                        for (String msg : plugin.configManager.getLang().getStringList("no-pack-loaded")) {
                            player.sendMessage(Utilities.chatTranslate(msg));
                        }
                    }
                }

                break;
        }
        return false;
    }

    // Welcome screen first join
    public void welcomeScreenInitial(Player e) {

        // Store the player's ID and set the screen to active
        plugin.screeningManager.playersScreenActive.put(e.getUniqueId(), true);
        // Store and clear the player's inventory
        plugin.screeningManager.saveInv.put(e.getPlayer().getName(), e.getPlayer().getInventory().getContents());
        e.getPlayer().getInventory().clear();

        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            public void run() {

                // Begin the screen sequence
                // check if screens.yml enable-first-join-welcome-screen = true
                if (Objects.equals(pullScreensConfig("enable-first-join-welcome-screen"), "true")) {

                        InventoryView screen = e.getPlayer().openInventory(plugin.getServer().createInventory(null, 54, screenTitleFirstJoin()));

                        if (pullScreensConfig("First-Join-Items") != null) {
                            for (String key : Objects.requireNonNull(plugin.configManager.getScreens().getConfigurationSection("First-Join-Items")).getKeys(false)) {
                                ConfigurationSection keySection = Objects.requireNonNull(plugin.configManager.getScreens().getConfigurationSection("First-Join-Items")).getConfigurationSection(key);
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

                                screen.setItem(slot, item);
                            }
                        }
                } else {
                    return;
                }
            }
        }, plugin.configManager.getConfig().getInt("delay"));
    }
}
