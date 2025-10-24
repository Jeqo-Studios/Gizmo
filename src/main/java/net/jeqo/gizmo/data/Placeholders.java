package net.jeqo.gizmo.data;

import net.jeqo.gizmo.Gizmo;
import org.bukkit.ChatColor;

public class Placeholders {

    public static String gizmoPrefix() {
        return "#ee0000[Gizmo] ";
    }
    public static String gizmoPrefixNoDec() {
        return "#ee0000Gizmo";
    }


    public static String shift48() {
        return Gizmo.getInstance().getScreensConfigurationCore().getString("Unicodes.shift-48");
    }
    public static String shift1013() {
        return Gizmo.getInstance().getScreensConfigurationCore().getString("Unicodes.shift-1013");
    }
    public static String shift1536() {
        return Gizmo.getInstance().getScreensConfigurationCore().getString("Unicodes.shift-1536");
    }

    public static String screenTitle() {
        return Gizmo.getInstance().getConfigConfigurationCore().getString("background-color") +
                shift1013() + Gizmo.getInstance().getScreensConfigurationCore().getString("Unicodes.background") +
                shift1536() + ChatColor.WHITE + Gizmo.getInstance().getScreensConfigurationCore().getString("Unicodes.welcome-screen");
    }
    public static String screenTitleFirstJoin() {
        return Gizmo.getInstance().getConfigConfigurationCore().getString("background-color") +
                shift1013() + Gizmo.getInstance().getScreensConfigurationCore().getString("Unicodes.first-join-background") +
                shift1536() + ChatColor.WHITE + Gizmo.getInstance().getScreensConfigurationCore().getString("Unicodes.first-join-welcome-screen");
    }
}
