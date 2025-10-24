package net.jeqo.gizmo.command.commands;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.command.CommandPermission;
import net.jeqo.gizmo.command.GizmoCommand;
import net.jeqo.gizmo.data.ChatTranslate;
import net.jeqo.gizmo.listener.ScreenDisplayHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static net.jeqo.gizmo.data.Placeholders.gizmoPrefix;

public class ShowCommand extends GizmoCommand {

    @Override
    public String name() {
        return "show";
    }

    @Override
    public String syntax() {
        return "show [player]";
    }

    @Override
    public String description() {
        return "Force displays the welcome screen.";
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
        return 1;
    }

    @Override
    public ArrayList<CommandPermission> requiredCommandPermissions() {
        ArrayList<CommandPermission> permissions = new ArrayList<>();

        permissions.add(CommandPermission.SHOW);

        return permissions;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if (args.length < 1) {
            ScreenDisplayHandler.showWelcomeScreen(player);
        } else {
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(ChatColor.RED + "The target player is not online.");
                return;
            }

            String rawMessage = Gizmo.getInstance().getMessagesConfigurationCore().getString("show-screen-others");
            rawMessage = rawMessage.replace("%player_name%", target.getName());
            player.sendMessage(ChatTranslate.translate(player, gizmoPrefix() + rawMessage));
            ScreenDisplayHandler.showWelcomeScreen(target);
        }
    }
}