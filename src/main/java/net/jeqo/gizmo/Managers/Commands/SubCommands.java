package net.jeqo.gizmo.Managers.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public interface SubCommands {
    void onCommand(Player player, String[] args);
    String name();
    List<String> tabComplete(CommandSender sender, String[] args);
}
