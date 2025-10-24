package net.jeqo.gizmo.command.commands;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.command.CommandPermission;
import net.jeqo.gizmo.command.GizmoCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ReloadCommand extends GizmoCommand {

    @Override
    public String name() {
        return "reload";
    }

    @Override
    public String syntax() {
        return "reload";
    }

    @Override
    public ArrayList<String> aliases() {
        ArrayList<String> aliases = new ArrayList<>();
        aliases.add("rl");
        return aliases;
    }

    @Override
    public String description() {
        return "Reloads the Gizmo configs (not recommended).";
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public int minArgs() {
        return 0;
    }

    @Override
    public int maxArgs() {
        return 0;
    }

    @Override
    public ArrayList<CommandPermission> requiredCommandPermissions() {
        ArrayList<CommandPermission> permissions = new ArrayList<>();

        permissions.add(CommandPermission.RELOAD);

        return permissions;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        player.sendMessage(ChatColor.GREEN + "Reloading Gizmo configs...");
        Gizmo.getInstance().getConfigConfigurationCore().reload();
        Gizmo.getInstance().getMessagesConfigurationCore().reload();
        Gizmo.getInstance().getScreensConfigurationCore().reload();

        player.sendMessage(ChatColor.GREEN + "Reloaded Gizmo configs.");
    }
}
