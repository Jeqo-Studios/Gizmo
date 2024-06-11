package net.jeqo.gizmo.data;

import net.jeqo.gizmo.Gizmo;
import org.bukkit.ChatColor;

import static net.jeqo.gizmo.data.Utilities.pullConfig;
import static net.jeqo.gizmo.data.Utilities.pullScreensConfig;

public class Placeholders {
    static Gizmo plugin = Gizmo.getPlugin(Gizmo.class);

    public static String gizmoPrefix() {
        return "#ee0000[Gizmo] ";
    }
    public static String gizmoPrefixNoDec() {
        return "#ee0000Gizmo";
    }

    public static String shift48() {
        return plugin.getScreensConfig().getString("Unicodes.shift-48");
    }
    public static String shift1013() {
        return plugin.getScreensConfig().getString("Unicodes.shift-1013");
    }
    public static String shift1536() {
        return plugin.getScreensConfig().getString("Unicodes.shift-1536");
    }

    public static String screenTitle() {
        return pullConfig("background-color") + shift1013() + pullScreensConfig("Unicodes.background") + shift1536() + ChatColor.WHITE + pullScreensConfig("Unicodes.welcome-screen");
    }
    public static String screenTitleFirstJoin() {
        return pullConfig("background-color") + shift1013() + pullScreensConfig("Unicodes.first-join-background") + shift1536() + ChatColor.WHITE + pullScreensConfig("Unicodes.first-join-welcome-screen");
    }
}
