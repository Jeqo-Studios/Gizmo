package net.jeqo.gizmo.data;

import me.clip.placeholderapi.PlaceholderAPI;
import net.jeqo.gizmo.Gizmo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {
    static Gizmo plugin = Gizmo.getPlugin(Gizmo.class);

    public static String chatTranslate(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder("");
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return PlaceholderAPI.setPlaceholders(null, ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void log(@NotNull String text) {
        Bukkit.getLogger().log(Level.INFO, "[Gizmo] " + text);
    }
    public static void warn(@NotNull String text) {
        Bukkit.getLogger().log(Level.WARNING, "[Gizmo] " + text);
    }

    public static String pullConfig(String id) {
        return Utilities.chatTranslate(plugin.getConfig().getString(id, ""));
    }
    public static String pullScreensConfig(String id) {
        return Utilities.chatTranslate(plugin.getScreensConfig().getString(id, ""));
    }
    public static String pullMessagesConfig(String id) {
        return Utilities.chatTranslate(plugin.getMessagesConfig().getString(id, ""));
    }
}
