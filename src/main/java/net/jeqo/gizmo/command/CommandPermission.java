package net.jeqo.gizmo.command;

import lombok.Getter;

/**
 * Represents a command permission that is associated with a command
 * <p>
 * The permission is formatted as "gizmo.[command_name]" for simplicity.
 */
@Getter
public enum CommandPermission {
    FADE("gizmo.fade"),
    RELOAD("gizmo.reload"),
    SHOW("gizmo.show");

    private final String permission;

    CommandPermission(String permission) {
        this.permission = permission;
    }
}
