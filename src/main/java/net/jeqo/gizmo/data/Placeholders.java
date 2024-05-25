package net.jeqo.gizmo.data;

import net.jeqo.gizmo.Gizmo;

public class Placeholders {

    static Gizmo plugin = Gizmo.getPlugin(Gizmo.class);

    public static String gizmoPrefix() {
        return "#ee0000[Gizmo] ";
    }
    public static String gizmoPrefixNoDec() {
        return "#ee0000Gizmo";
    }
}
