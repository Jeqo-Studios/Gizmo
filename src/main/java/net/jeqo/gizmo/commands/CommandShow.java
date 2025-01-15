package net.jeqo.gizmo.commands;

import net.jeqo.gizmo.commands.manager.Command;
import net.jeqo.gizmo.commands.manager.enums.CommandPermission;
import net.jeqo.gizmo.listeners.PlayerScreening;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static net.jeqo.gizmo.utils.Placeholders.gizmoPrefix;
import static net.jeqo.gizmo.utils.Utilities.chatTranslate;
import static net.jeqo.gizmo.utils.Utilities.pullMessagesConfig;

public class CommandShow extends Command {

    public CommandShow(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("show");
        this.setCommandDescription("Shows the Gizmo welcome screen.");
        this.setCommandSyntax("/gizmo show [player]");
        this.setRequiredPermission(CommandPermission.HELP);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player p = (Player) sender;
        if (args.length > 1) {
            Player t = Bukkit.getPlayer(args [1]);
                if (t != null) {
                    PlayerScreening.welcomeScreen(t);
                    if (Objects.equals(pullMessagesConfig("show-screen-others"), "[]")) {
                    } else {
                        p.sendMessage(chatTranslate(gizmoPrefix() + pullMessagesConfig("show-screen-others")));
                    }
                } else {
                    if (Objects.equals(pullMessagesConfig("player-not-found"), "[]")) {
                    } else {
                        p.sendMessage(chatTranslate(gizmoPrefix() + pullMessagesConfig("player-not-found")));
                    }
                }
        } else {
            PlayerScreening.welcomeScreen(p);
        }

        return false;
    }
}
