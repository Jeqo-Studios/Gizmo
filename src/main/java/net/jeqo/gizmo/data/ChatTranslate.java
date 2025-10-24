package net.jeqo.gizmo.data;

import net.jeqo.gizmo.logger.Logger;
import net.jeqo.gizmo.logger.LoggingLevel;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ChatTranslate is a utility class that provides methods to translate chat messages
 * with support for hex color codes and PlaceholderAPI placeholders.
 */
public class ChatTranslate {

    /**
     * Translates a message for a CommandSender, replacing hex color codes and placeholders.
     * @param sender the CommandSender to apply placeholders for, if applicable
     * @param message the message to translate, which may contain hex color codes and PlaceholderAPI placeholders
     * @return the translated message with color codes and placeholders applied
     */
    public static String translate(CommandSender sender, String message) {
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

        try {
            if (sender instanceof Player) {
                return PlaceholderAPI.setPlaceholders((Player) sender, ChatColor.translateAlternateColorCodes('&', message));
            } else {
                return ChatColor.translateAlternateColorCodes('&', message);
            }
        } catch (NoClassDefFoundError e) {
            Logger.log(LoggingLevel.WARNING, "PlaceholderAPI is not currently installed! Please install PlaceholderAPI to use its features.");
            return ChatColor.translateAlternateColorCodes('&', message);
        }
    }
}
