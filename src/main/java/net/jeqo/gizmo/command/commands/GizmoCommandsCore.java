package net.jeqo.gizmo.command.commands;

import net.jeqo.gizmo.Gizmo;

/**
 * Handles all Gizmo related subcommands
 */
public class GizmoCommandsCore {

    public void initialize() {
        Gizmo.getInstance().getCommandCore().stageCommand(new GizmoSubcommand());
    }
}
