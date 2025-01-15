package net.jeqo.gizmo.commands.manager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (sender.hasPermission("gizmo.reload")) {
            if (args.length == 1) {
                return StringUtil.copyPartialMatches(args[0], Arrays.asList("fade", "help", "reload", "rl", "show"), new ArrayList<>());
            } else {
                return Collections.emptyList();
            }
        }
        return null;
    }
}