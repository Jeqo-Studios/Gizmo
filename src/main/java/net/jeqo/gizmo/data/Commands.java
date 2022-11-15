package net.jeqo.gizmo.data;

import me.clip.placeholderapi.PlaceholderAPI;
import net.jeqo.gizmo.Gizmo;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static net.jeqo.gizmo.listeners.Prime.saveInv;

public class Commands implements CommandExecutor {

    Gizmo plugin = Gizmo.getPlugin(Gizmo.class);

    String shift48 = plugin.getConfig().getString("Unicodes.shift-48");
    String shift1013 = plugin.getConfig().getString("Unicodes.shift-1013");
    String shift1536 = plugin.getConfig().getString("Unicodes.shift-1536");
    public static GameMode showGm;
    //if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Bukkit.getPluginManager().getPlugin("PlacehoderAPI") != null) {
            if (sender instanceof Player p) {
                if (args.length == 0) {
                    if (p.hasPermission("gizmo.reload")) {
                        p.sendMessage("");
                        p.sendMessage(Utilities.hex("   #ee0000/gizmo reload &7- Reloads the Gizmo config."));
                        p.sendMessage(Utilities.hex("   #ee0000/gizmo test &7- Displays a test welcome screen."));
                        p.sendMessage("");
                        p.sendMessage(Utilities.hex("   #ee0000Gizmo 1.0.2-BETA &7- Made by Jeqo"));
                        p.sendMessage("");
                    } else {
                        p.sendMessage("");
                        p.sendMessage(Utilities.hex("   #ee0000Gizmo 1.0.2-BETA &7- Made by Jeqo"));
                        p.sendMessage("");
                    }
                    return true;
                } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                    if (p.hasPermission("gizmo.reload")) {
                        plugin.reloadConfig();
                        if (Objects.equals(plugin.getConfig().getString("messages.config-reloaded"), "[]")) {
                        } else {
                            p.sendMessage(PlaceholderAPI.setPlaceholders(p.getPlayer(), Utilities.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.config-reloaded"))));
                        }
                    } else {
                        if (Objects.equals(plugin.getConfig().getString("messages.no-permission"), "[]")) {
                        } else {
                            p.sendMessage(PlaceholderAPI.setPlaceholders(p.getPlayer(), Utilities.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.no-permission"))));
                        }
                    }
                } else if (args[0].equalsIgnoreCase("show")) {
                    if (args.length > 1) {
                        Player t = Bukkit.getPlayer(args [1]);
                        if (p.hasPermission("gizmo.show")) {
                            if (t != null) {
                                saveInv.put(t.getName(), t.getInventory().getContents());
                                t.getInventory().clear();


                                if (Objects.equals(plugin.getConfig().getString("enable-background"), "true")) {
                                    Objects.requireNonNull(t.getPlayer()).openInventory(plugin.getServer().createInventory(null, 54, ChatColor.WHITE + shift1013 + plugin.getConfig().getString("Unicodes.background") + shift1536 + plugin.getConfig().getString("Unicodes.welcome-screen")));
                                    if (Objects.equals(plugin.getConfig().getString("messages.show-screen-others"), "[]")) {
                                    } else {
                                        p.sendMessage(PlaceholderAPI.setPlaceholders(t.getPlayer(), Utilities.hex("#ee0000[Gizmo] " + Objects.requireNonNull(plugin.getConfig().getString("messages.show-screen-others")))));
                                    }


                                } else if (Objects.equals(plugin.getConfig().getString("enable-background"), "false")) {
                                    Objects.requireNonNull(t.getPlayer()).openInventory(plugin.getServer().createInventory(null, 54, ChatColor.WHITE + shift48 + plugin.getConfig().getString("Unicodes.welcome-screen")));
                                    if (Objects.equals(plugin.getConfig().getString("messages.show-screen-others"), "[]")) {
                                    } else {
                                        p.sendMessage(PlaceholderAPI.setPlaceholders(t.getPlayer(), Utilities.hex("#ee0000[Gizmo] " + Objects.requireNonNull(plugin.getConfig().getString("messages.show-screen-others")))));
                                    }
                                }


                            } else {
                                if (Objects.equals(plugin.getConfig().getString("messages.player-not-found"), "[]")) {
                                } else {
                                    p.sendMessage(PlaceholderAPI.setPlaceholders(p.getPlayer(), Utilities.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.player-not-found"))));
                                }
                            }
                        } else {
                            if (Objects.equals(plugin.getConfig().getString("messages.no-permission"), "[]")) {
                            } else {
                                p.sendMessage(PlaceholderAPI.setPlaceholders(p.getPlayer(), Utilities.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.no-permission"))));
                            }
                        }


                    } else {
                        if (p.hasPermission("gizmo.show")) {

                            showGm = p.getGameMode();

                            saveInv.put(p.getName(), p.getInventory().getContents());
                            p.getInventory().clear();

                            if (Objects.equals(plugin.getConfig().getString("enable-background"), "true")) {
                                Objects.requireNonNull(p.getPlayer()).openInventory(plugin.getServer().createInventory(null, 54, ChatColor.WHITE + shift1013 + plugin.getConfig().getString("Unicodes.background") + shift1536 + plugin.getConfig().getString("Unicodes.welcome-screen")));
                                if (Objects.equals(plugin.getConfig().getString("messages.show-screen"), "[]")) {
                                } else {
                                    p.sendMessage(PlaceholderAPI.setPlaceholders(p.getPlayer(), Utilities.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.show-screen"))));
                                }
                            } else if (Objects.equals(plugin.getConfig().getString("enable-background"), "false")) {
                                Objects.requireNonNull(p.getPlayer()).openInventory(plugin.getServer().createInventory(null, 54, ChatColor.WHITE + shift48 + plugin.getConfig().getString("Unicodes.welcome-screen")));
                                if (Objects.equals(plugin.getConfig().getString("messages.show-screen"), "[]")) {
                                } else {
                                    p.sendMessage(PlaceholderAPI.setPlaceholders(p.getPlayer(), Utilities.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.show-screen"))));
                                }
                            }
                        } else {
                            if (Objects.equals(plugin.getConfig().getString("messages.no-permission"), "[]")) {
                            } else {
                                p.sendMessage(PlaceholderAPI.setPlaceholders(p.getPlayer(), Utilities.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.no-permission"))));
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

            // If PlaceholderAPI is not installed
        } else {
            if (sender instanceof Player p) {
                if (args.length == 0) {
                    usage(p);
                    return true;
                } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                    if (p.hasPermission("gizmo.reload")) {
                        plugin.reloadConfig();
                        if (Objects.equals(plugin.getConfig().getString("messages.config-reloaded"), "[]")) {
                        } else {
                            p.sendMessage(Utilities.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.config-reloaded")));
                        }
                    } else {
                        if (Objects.equals(plugin.getConfig().getString("messages.no-permission"), "[]")) {
                        } else {
                            p.sendMessage(Utilities.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.no-permission")));
                        }
                    }
                } else if (args[0].equalsIgnoreCase("show")) {
                    if (args.length > 1) {
                        Player t = Bukkit.getPlayer(args [1]);
                        if (p.hasPermission("gizmo.show")) {
                            if (t != null) {
                                saveInv.put(t.getName(), t.getInventory().getContents());
                                t.getInventory().clear();


                                if (Objects.equals(plugin.getConfig().getString("enable-background"), "true")) {
                                    Objects.requireNonNull(t.getPlayer()).openInventory(plugin.getServer().createInventory(null, 54, ChatColor.WHITE + shift1013 + plugin.getConfig().getString("Unicodes.background") + shift1536 + plugin.getConfig().getString("Unicodes.welcome-screen")));
                                    if (Objects.equals(plugin.getConfig().getString("messages.show-screen-others"), "[]")) {
                                    } else {
                                        p.sendMessage(Utilities.hex("#ee0000[Gizmo] " + Objects.requireNonNull(plugin.getConfig().getString("messages.show-screen-others"))));
                                    }
                                } else if (Objects.equals(plugin.getConfig().getString("enable-background"), "false")) {
                                    Objects.requireNonNull(t.getPlayer()).openInventory(plugin.getServer().createInventory(null, 54, ChatColor.WHITE + shift48 + plugin.getConfig().getString("Unicodes.welcome-screen")));
                                    if (Objects.equals(plugin.getConfig().getString("messages.show-screen-others"), "[]")) {
                                    } else {
                                        p.sendMessage(Utilities.hex("#ee0000[Gizmo] " + Objects.requireNonNull(plugin.getConfig().getString("messages.show-screen-others"))));
                                    }
                                }


                            } else {
                                if (Objects.equals(plugin.getConfig().getString("messages.player-not-found"), "[]")) {
                                } else {
                                    p.sendMessage(Utilities.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("mmessages.player-not-found")));
                                }
                            }
                        } else {
                            if (Objects.equals(plugin.getConfig().getString("messages.no-permission"), "[]")) {
                            } else {
                                p.sendMessage(Utilities.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.no-permission")));
                            }
                        }


                    } else {
                        if (p.hasPermission("gizmo.show")) {

                            showGm = p.getGameMode();

                            saveInv.put(p.getName(), p.getInventory().getContents());
                            p.getInventory().clear();

                            if (Objects.equals(plugin.getConfig().getString("enable-background"), "true")) {
                                Objects.requireNonNull(p.getPlayer()).openInventory(plugin.getServer().createInventory(null, 54, ChatColor.WHITE + shift1013 + plugin.getConfig().getString("Unicodes.background") + shift1536 + plugin.getConfig().getString("Unicodes.welcome-screen")));
                                if (Objects.equals(plugin.getConfig().getString("messages.show-screen"), "[]")) {
                                } else {
                                    p.sendMessage(Utilities.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.show-screen")));
                                }
                            } else if (Objects.equals(plugin.getConfig().getString("enable-background"), "false")) {
                                Objects.requireNonNull(p.getPlayer()).openInventory(plugin.getServer().createInventory(null, 54, ChatColor.WHITE + shift48 + plugin.getConfig().getString("Unicodes.welcome-screen")));
                                if (Objects.equals(plugin.getConfig().getString("messages.show-screen"), "[]")) {
                                } else {
                                    p.sendMessage(Utilities.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.show-screen")));
                                }
                            }
                        } else {
                            if (Objects.equals(plugin.getConfig().getString("messages.no-permission"), "[]")) {
                            } else {
                                p.sendMessage(Utilities.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.no-permission")));
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
    }

    // create a usage method
    void usage(CommandSender sender) {
        if (sender.hasPermission("gizmo.reload")) {
            sender.sendMessage("");
            sender.sendMessage(Utilities.hex("   #ee0000/gizmo reload &7- Reloads the Gizmo config."));
            sender.sendMessage(Utilities.hex("   #ee0000/gizmo show <player> &7- Displays a test welcome screen."));
            sender.sendMessage("");
            sender.sendMessage(Utilities.hex("   #ee0000Gizmo 1.0.9-BETA &7- Made by Jeqo (https://jeqo.net)"));
            sender.sendMessage("");
        } else {
            sender.sendMessage("");
            sender.sendMessage(Utilities.hex("   #ee0000Gizmo 1.0.9-BETA &7- Made by Jeqo (https://jeqo.net)"));
            sender.sendMessage("");
        }
    }
}
