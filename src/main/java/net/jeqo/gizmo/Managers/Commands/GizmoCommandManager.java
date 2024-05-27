package net.jeqo.gizmo.Managers.Commands;

import net.jeqo.gizmo.Commands.GizmoFadeCommand;
import net.jeqo.gizmo.Commands.GizmoHelpCommand;
import net.jeqo.gizmo.Commands.GizmoReloadCommand;
import net.jeqo.gizmo.Commands.GizmoShowCommand;
import net.jeqo.gizmo.Gizmo;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GizmoCommandManager implements CommandExecutor, TabCompleter {

    private final ArrayList<SubCommands> commands = new ArrayList<>();

    public GizmoCommandManager(Gizmo plugin) {
        plugin.getCommand("gizmo").setExecutor(this);
        plugin.getCommand("gizmo").setTabCompleter(this);

        commands.add(new GizmoShowCommand(plugin));
        commands.add(new GizmoFadeCommand(plugin));

        commands.add(new GizmoReloadCommand(plugin));
        commands.add(new GizmoHelpCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String arg, @NotNull String[] args) {
        if (args.length == 0) {
            Bukkit.dispatchCommand(sender, "gizmo help");
            return true;
        }

        SubCommands command = getCommand(args[0]);
        if (command == null) return true;

        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        command.onCommand(sender, newArgs);

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return commands.stream()
                    .map(SubCommands::name)
                    .collect(Collectors.toList());
        }

        //TODO setup more tab complete holders
        return new ArrayList<>();
    }

    private SubCommands getCommand(String name) {
        return commands.stream()
                .filter(cmd -> cmd.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}