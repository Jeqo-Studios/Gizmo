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
}
