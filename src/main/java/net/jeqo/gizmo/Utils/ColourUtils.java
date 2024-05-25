package net.jeqo.gizmo.Utils;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColourUtils {

    private final Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");

    public String oldFormat(String message) {
        Matcher match = pattern.matcher(message);

        while (match.find()) {
            String colour = message.substring(match.start(), match.end());
            message = message.replace(colour, ChatColor.of(colour) + "");
            match = pattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
