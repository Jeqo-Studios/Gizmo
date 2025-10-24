package net.jeqo.gizmo.listener;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.data.ChatTranslate;
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

import java.util.*;

import static net.jeqo.gizmo.data.Placeholders.*;

public class ScreenDisplayHandler implements Listener {

    private static final Gizmo plugin = Gizmo.getPlugin(Gizmo.class);
    public static final Map<UUID, ItemStack[]> saveInv = new HashMap<>();
    public static final Map<UUID, Boolean> playersScreenActive = new HashMap<>();

    @EventHandler
    public void onPackStatus(PlayerResourcePackStatusEvent e) {
        Player player = e.getPlayer();

        if (plugin.getConfig().getBoolean("sound-on-pack-load.enable")) {
            playConfiguredSound(player);
        }

        switch (e.getStatus()) {
            case ACCEPTED -> {
                if (plugin.getConfig().getBoolean("delay-background")) {
                    player.sendTitle(
                            Gizmo.getInstance().getConfigConfigurationCore().getString("background-color")
                                    + Gizmo.getInstance().getScreensConfigurationCore().getString("Unicodes.background"),
                            "",
                            0, 999999, 0
                    );
                }
            }
            case SUCCESSFULLY_LOADED -> handlePackLoaded(player);
            case DECLINED, FAILED_DOWNLOAD -> handlePackDeclined(player);
        }
    }

    private void handlePackLoaded(Player player) {
        if (!player.hasPlayedBefore() &&
                plugin.getScreensConfigurationCore().getBoolean("enable-first-join-welcome-screen") &&
                Gizmo.getInstance().playerTracker.get(player.getUniqueId()) == null) {

            Gizmo.getInstance().playerTracker.put(player.getUniqueId(), "1");
            showInitialWelcomeScreen(player);
            return;
        }

        boolean oncePerRestart = plugin.getScreensConfigurationCore().getBoolean("once-per-restart");
        if (!oncePerRestart || Gizmo.getInstance().playerTracker.get(player.getUniqueId()) == null) {
            Gizmo.getInstance().playerTracker.put(player.getUniqueId(), "1");
            showWelcomeScreen(player);
        }
    }

    private void handlePackDeclined(Player player) {
        if (plugin.getConfig().getBoolean("debug-mode")) {
            player.sendMessage(ChatTranslate.translate(null, gizmoPrefix() + "#acb5bfNo server resource pack detected and/or debug mode is enabled."));
            player.sendMessage(ChatTranslate.translate(null, gizmoPrefix() + "#acb5bfSending welcome screen..."));
            showWelcomeScreen(player);
        } else {
            player.removePotionEffect(PotionEffectType.BLINDNESS);
            List<String> messages = plugin.getMessagesConfigurationCore().getStringList("no-pack-loaded");
            for (String msg : messages) {
                player.sendMessage(ChatTranslate.translate(null, msg));
            }
        }
    }

    private void playConfiguredSound(Player player) {
        String sound = plugin.getConfig().getString("sound-on-pack-load.sound");
        float volume = (float) plugin.getConfig().getDouble("sound-on-pack-load.volume");
        float pitch = (float) plugin.getConfig().getDouble("sound-on-pack-load.pitch");
        try {
            player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
        } catch (IllegalArgumentException ignored) {}
    }

    public static void showWelcomeScreen(Player player) {
        playersScreenActive.put(player.getUniqueId(), true);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            saveInventory(player);
            InventoryView screen = player.openInventory(plugin.getServer().createInventory(null, 54, screenTitle()));

            ConfigurationSection itemsSection = plugin.getScreensConfigurationCore().getSection("Items");
            if (itemsSection != null) {
                populateScreenItems(screen, itemsSection);
            }
        }, plugin.getConfig().getInt("delay"));
    }

    public static void showInitialWelcomeScreen(Player player) {
        playersScreenActive.put(player.getUniqueId(), true);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            saveInventory(player);
            InventoryView screen = player.openInventory(plugin.getServer().createInventory(null, 54, screenTitleFirstJoin()));

            ConfigurationSection firstJoinItems = plugin.getScreensConfigurationCore().getSection("First-Join-Items");
            if (firstJoinItems != null) {
                populateScreenItems(screen, firstJoinItems);
            }
        }, plugin.getConfig().getInt("delay"));
    }

    private static void saveInventory(Player player) {
        saveInv.put(player.getUniqueId(), player.getInventory().getContents());
        player.getInventory().clear();
    }

    private static void populateScreenItems(InventoryView inventory, ConfigurationSection section) {
        for (String key : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(key);
            if (itemSection == null) continue;

            Material material = Material.matchMaterial(itemSection.getString("material"));
            if (material == null) continue;

            int slot = itemSection.getInt("slot");
            ItemStack item = new ItemStack(material);
            ItemMeta meta = item.getItemMeta();
            if (meta == null) continue;

            List<String> lore = itemSection.getStringList("lore");
            if (!lore.isEmpty()) {
                lore.replaceAll(loreLine -> ChatTranslate.translate(null, loreLine));
                meta.setLore(lore);
            }

            if (itemSection.getBoolean("hide-flags")) {
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS,
                        ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_UNBREAKABLE);
            }

            if (itemSection.contains("custom-model-data")) {
                meta.setCustomModelData(itemSection.getInt("custom-model-data"));
            }

            if (itemSection.contains("name")) {
                meta.setDisplayName(ChatTranslate.translate(null, itemSection.getString("name")));
            }

            item.setItemMeta(meta);

            if (slot >= 0) {
                inventory.setItem(slot, item);
            }
        }
    }
}
