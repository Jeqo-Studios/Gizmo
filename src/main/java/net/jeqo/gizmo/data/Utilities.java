package net.jeqo.gizmo.data;

import net.jeqo.gizmo.Gizmo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Utilities {

    public static String getMinecraftVersion()
    {
        Matcher matcher = Pattern.compile("(\\(MC: )([\\d\\.]+)(\\))").matcher(Bukkit.getVersion());
        if (matcher.find()) {
            return matcher.group(2);
        }
        return null;
    }

    Gizmo plugin = Gizmo.getPlugin(Gizmo.class);
    public static String hex(String message) {
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
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void log(@NotNull String text) {
        Bukkit.getLogger().log(Level.INFO, text);
    }

    public static void warn(@NotNull String text) {
        Bukkit.getLogger().log(Level.WARNING, text);
    }
}
