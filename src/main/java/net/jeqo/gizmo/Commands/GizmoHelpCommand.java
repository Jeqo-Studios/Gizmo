package net.jeqo.gizmo.Commands;

import net.jeqo.gizmo.Managers.Commands.SubCommands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GizmoHelpCommand implements SubCommands {

    @Override
    public void onCommand(Player player, String[] args) {

    }

    @Override
    public String name() {
        return "help";
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
