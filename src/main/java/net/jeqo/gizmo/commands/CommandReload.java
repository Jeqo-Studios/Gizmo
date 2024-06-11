package net.jeqo.gizmo.commands;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.commands.manager.Command;
import net.jeqo.gizmo.commands.manager.enums.CommandPermission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

import static net.jeqo.gizmo.utils.Placeholders.gizmoPrefix;
import static net.jeqo.gizmo.utils.Utilities.chatTranslate;
import static net.jeqo.gizmo.utils.Utilities.pullMessagesConfig;

public class CommandReload extends Command {

    public CommandReload(JavaPlugin plugin) {
        super(plugin);
        this.addCommandAlias("reload");
        this.addCommandAlias("rl");
        this.setCommandDescription("Reloads the Gizmo configuration.");
        this.setCommandSyntax("/gizmo reload");
        this.setRequiredPermission(CommandPermission.RELOAD);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        Gizmo.getInstance().reloadConfig();
        if (Objects.equals(pullMessagesConfig("config-reloaded"), "[]")) {
        } else {
            player.sendMessage(chatTranslate(gizmoPrefix() + pullMessagesConfig("config-reloaded")));
        }

        return false;
    }
}
