package net.jeqo.gizmo.utils;

import net.jeqo.gizmo.Gizmo;
import org.bukkit.ChatColor;

import static net.jeqo.gizmo.utils.Utilities.pullConfig;
import static net.jeqo.gizmo.utils.Utilities.pullScreensConfig;

public class Placeholders {
    public static String gizmoPrefix() {
        return "#ee0000[Gizmo] ";
    }
    public static String gizmoPrefixNoDec() {
        return "#ee0000Gizmo";
    }

    public static String shift48() {
        return Configurations.getScreensConfig().getString("Unicodes.shift-48");
    }
    public static String shift1013() {
        return Configurations.getScreensConfig().getString("Unicodes.shift-1013");
    }
    public static String shift1536() {
        return Configurations.getScreensConfig().getString("Unicodes.shift-1536");
    }

    public static String screenTitle() {
        return pullConfig("background-color") + shift1013() + pullScreensConfig("Unicodes.background") + shift1536() + ChatColor.WHITE + pullScreensConfig("Unicodes.welcome-screen");
    }
    public static String screenTitleFirstJoin() {
        return pullConfig("background-color") + shift1013() + pullScreensConfig("Unicodes.first-join-background") + shift1536() + ChatColor.WHITE + pullScreensConfig("Unicodes.first-join-welcome-screen");
    }
}
