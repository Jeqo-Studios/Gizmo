package net.jeqo.gizmo.data;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.listeners.PlayerScreening;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.Objects;

import static net.jeqo.gizmo.data.Placeholders.gizmoPrefix;
import static net.jeqo.gizmo.data.Placeholders.gizmoPrefixNoDec;
import static net.jeqo.gizmo.data.Utilities.*;

public class Commands implements CommandExecutor {

    Gizmo plugin = Gizmo.getPlugin(Gizmo.class);

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player p) {
            if (args.length == 0) {
                usage(p);
                return true;
            } else if (args[0].equalsIgnoreCase("help")) {
                usage(p);
            } else if (args[0].equalsIgnoreCase("fade")) {
                if (p.hasPermission("gizmo.fade")) {
                    if (args.length == 5) {
                        Player target = Bukkit.getPlayer(args[4]);
                        if (target == null) {
                            p.sendMessage(chatTranslate(gizmoPrefix() + "&7That player is not online!"));
                            return true;
                        }
                        if (args[1].matches("[0-9]+") && args[2].matches("[0-9]+") && args[3].matches("[0-9]+")) {
                            target.sendTitle((String) pullScreensConfig("Unicodes.background"), "", Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                        } else {
                            p.sendMessage(chatTranslate(gizmoPrefix() + "&7Only numbers can be used for time values!"));
                        }
                    } else if (args.length == 4) {
                        if (args[1].matches("[0-9]+") && args[2].matches("[0-9]+") && args[3].matches("[0-9]+")) {
                            p.sendTitle((String) pullScreensConfig("Unicodes.background"), "", Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                        } else {
                            p.sendMessage(chatTranslate(gizmoPrefix() + "&7Only numbers can be used for time values!"));
                        }
                    } else {
                        p.sendMessage(chatTranslate(gizmoPrefix() + "&7Usage: /gizmo fade <in> <stay> <out> [player]"));
                    }
                } else {
                    if (Objects.equals(pullMessagesConfig("no-permission"), "[]")) {
                    } else {
                        p.sendMessage(chatTranslate(gizmoPrefix() + pullMessagesConfig("no-permission")));
                    }
                }
            } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                if (p.hasPermission("gizmo.reload")) {
                    plugin.reloadConfig();
                    if (Objects.equals(pullMessagesConfig("config-reloaded"), "[]")) {
                    } else {
                        p.sendMessage(chatTranslate(gizmoPrefix() + pullMessagesConfig("config-reloaded")));
                    }
                } else {
                    if (Objects.equals(pullMessagesConfig("messages.no-permission"), "[]")) {
                    } else {
                        p.sendMessage(chatTranslate(gizmoPrefix() + pullMessagesConfig("no-permission")));
                    }
                }
            } else if (args[0].equalsIgnoreCase("show")) {
                if (args.length > 1) {
                    Player t = Bukkit.getPlayer(args [1]);
                    if (p.hasPermission("gizmo.show")) {
                        if (t != null) {
                            plugin.screeningManager.welcomeScreen(t);
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
                        if (Objects.equals(pullMessagesConfig("messages.no-permission"), "[]")) {
                        } else {
                            p.sendMessage(chatTranslate(gizmoPrefix() + pullMessagesConfig("no-permission")));
                        }
                    }
                } else {
                    if (p.hasPermission("gizmo.show")) {
                        plugin.screeningManager.welcomeScreen(p);
                    } else {
                        if (Objects.equals(pullMessagesConfig("no-permission"), "[]")) {
                        } else {
                            p.sendMessage(chatTranslate(gizmoPrefix() + pullMessagesConfig("no-permission")));
                        }
                    }
                }
            }
        } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
            plugin.reloadConfig();
            plugin.getLogger().info("|--[ GIZMO ]---------------------------------------------------------|");
            plugin.getLogger().info("|                          Config reloaded.                          |");
            plugin.getLogger().info("|-------------------------------------------------[ MADE BY JEQO ]---|");
        } return true;
    }

    void usage(CommandSender sender) {
        if (sender.hasPermission("gizmo.reload")) {
            sender.sendMessage("");
            sender.sendMessage(chatTranslate("#ee0000Usages:"));
            sender.sendMessage(chatTranslate("&f/gizmo fade <in> <stay> <out> [player] &7- Displays a fade."));
            sender.sendMessage(chatTranslate("&f/gizmo reload &7- Reloads the Gizmo configs (not recommended)."));
            sender.sendMessage(chatTranslate("&f/gizmo show <player> &7- Force displays the welcome screen."));
            sender.sendMessage("");
            sender.sendMessage(chatTranslate(gizmoPrefixNoDec() + " " + plugin.getDescription().getVersion() + " &f- Made by Jeqo (&nhttps://jeqo.net&r)"));
            sender.sendMessage("");
        } else {
            sender.sendMessage("");
            sender.sendMessage(chatTranslate(gizmoPrefixNoDec() + " " + plugin.getDescription().getVersion() + " &f- Made by Jeqo (&nhttps://jeqo.net&r)"));
            sender.sendMessage("");
        }
    }
}
