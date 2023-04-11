package net.jeqo.gizmo.data;

import me.clip.placeholderapi.PlaceholderAPI;
import net.jeqo.gizmo.Gizmo;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

import static net.jeqo.gizmo.listeners.Initiate.saveInv;
import static org.apache.commons.lang.StringUtils.isNumeric;

public class Commands implements CommandExecutor {

    Gizmo plugin = Gizmo.getPlugin(Gizmo.class);

    String shift48 = plugin.getConfig().getString("Unicodes.shift-48");
    String shift1013 = plugin.getConfig().getString("Unicodes.shift-1013");
    String shift1536 = plugin.getConfig().getString("Unicodes.shift-1536");

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (Bukkit.getPluginManager().getPlugin("PlacehoderAPI") != null) {

            if (sender instanceof Player p) {
                if (args.length == 0) {
                    usage(p);
                    return true;
                } else if (args[0].equalsIgnoreCase("fade")) {
                    if (p.hasPermission("gizmo.fade")) {
                        if (args.length == 4) {
                            if (isNumeric(args[1]) && isNumeric(args[2]) && isNumeric(args[3])) {
                                p.sendTitle((String) plugin.getConfig().get("Unicodes.background"), "", Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                            } else {
                                p.sendMessage(Utils.hex("#ee0000[Gizmo] " + "Only numbers can be used for time values!"));
                            }
                        } else {
                            p.sendMessage(Utils.hex("#ee0000[Gizmo] " + "Usage: /gizmo fade <in> <stay> <out>"));
                        }
                    } else {
                        if (Objects.equals(plugin.getConfig().getString("messages.no-permission"), "[]")) {
                        } else {
                            p.sendMessage(PlaceholderAPI.setPlaceholders(p.getPlayer(), Utils.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.no-permission"))));
                        }
                    }
                } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                    if (p.hasPermission("gizmo.reload")) {
                        plugin.reloadConfig();
                        if (Objects.equals(plugin.getConfig().getString("messages.config-reloaded"), "[]")) {
                        } else {
                            p.sendMessage(PlaceholderAPI.setPlaceholders(p.getPlayer(), Utils.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.config-reloaded"))));
                        }
                    } else {
                        if (Objects.equals(plugin.getConfig().getString("messages.no-permission"), "[]")) {
                        } else {
                            p.sendMessage(PlaceholderAPI.setPlaceholders(p.getPlayer(), Utils.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.no-permission"))));
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
                                        p.sendMessage(PlaceholderAPI.setPlaceholders(t.getPlayer(), Utils.hex("#ee0000[Gizmo] " + Objects.requireNonNull(plugin.getConfig().getString("messages.show-screen-others")))));
                                    }


                                } else if (Objects.equals(plugin.getConfig().getString("enable-background"), "false")) {
                                    Objects.requireNonNull(t.getPlayer()).openInventory(plugin.getServer().createInventory(null, 54, ChatColor.WHITE + shift48 + plugin.getConfig().getString("Unicodes.welcome-screen")));
                                    if (Objects.equals(plugin.getConfig().getString("messages.show-screen-others"), "[]")) {
                                    } else {
                                        p.sendMessage(PlaceholderAPI.setPlaceholders(t.getPlayer(), Utils.hex("#ee0000[Gizmo] " + Objects.requireNonNull(plugin.getConfig().getString("messages.show-screen-others")))));
                                    }
                                }


                            } else {
                                if (Objects.equals(plugin.getConfig().getString("messages.player-not-found"), "[]")) {
                                } else {
                                    p.sendMessage(PlaceholderAPI.setPlaceholders(p.getPlayer(), Utils.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.player-not-found"))));
                                }
                            }
                        } else {
                            if (Objects.equals(plugin.getConfig().getString("messages.no-permission"), "[]")) {
                            } else {
                                p.sendMessage(PlaceholderAPI.setPlaceholders(p.getPlayer(), Utils.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.no-permission"))));
                            }
                        }


                    } else {
                        if (p.hasPermission("gizmo.show")) {

                            saveInv.put(p.getName(), p.getInventory().getContents());
                            p.getInventory().clear();

                            if (Objects.equals(plugin.getConfig().getString("enable-background"), "true")) {
                                Objects.requireNonNull(p.getPlayer()).openInventory(plugin.getServer().createInventory(null, 54, ChatColor.WHITE + shift1013 + plugin.getConfig().getString("Unicodes.background") + shift1536 + plugin.getConfig().getString("Unicodes.welcome-screen")));
                                if (Objects.equals(plugin.getConfig().getString("messages.show-screen"), "[]")) {
                                } else {
                                    p.sendMessage(PlaceholderAPI.setPlaceholders(p.getPlayer(), Utils.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.show-screen"))));
                                }
                            } else if (Objects.equals(plugin.getConfig().getString("enable-background"), "false")) {
                                Objects.requireNonNull(p.getPlayer()).openInventory(plugin.getServer().createInventory(null, 54, ChatColor.WHITE + shift48 + plugin.getConfig().getString("Unicodes.welcome-screen")));
                                if (Objects.equals(plugin.getConfig().getString("messages.show-screen"), "[]")) {
                                } else {
                                    p.sendMessage(PlaceholderAPI.setPlaceholders(p.getPlayer(), Utils.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.show-screen"))));
                                }
                            }
                        } else {
                            if (Objects.equals(plugin.getConfig().getString("messages.no-permission"), "[]")) {
                            } else {
                                p.sendMessage(PlaceholderAPI.setPlaceholders(p.getPlayer(), Utils.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.no-permission"))));
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
                } else if (args[0].equalsIgnoreCase("fade")) {
                    if (p.hasPermission("gizmo.fade")) {
                        if (args.length == 4) {
                            if (isNumeric(args[1]) && isNumeric(args[2]) && isNumeric(args[3])) {
                                p.sendTitle((String) plugin.getConfig().get("Unicodes.background"), "", Integer.parseInt(args[1]), Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                            } else {
                                p.sendMessage(Utils.hex("#ee0000[Gizmo] " + "Only numbers can be used for time values!"));
                            }
                        } else {
                            p.sendMessage(Utils.hex("#ee0000[Gizmo] " + "Usage: /gizmo fade <in> <stay> <out>"));
                        }
                    } else {
                        if (Objects.equals(plugin.getConfig().getString("messages.no-permission"), "[]")) {
                        } else {
                            p.sendMessage(Utils.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.no-permission")));
                        }
                    }
                } else if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")) {
                    if (p.hasPermission("gizmo.reload")) {
                        plugin.reloadConfig();
                        if (Objects.equals(plugin.getConfig().getString("messages.config-reloaded"), "[]")) {
                        } else {
                            p.sendMessage(Utils.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.config-reloaded")));
                        }
                    } else {
                        if (Objects.equals(plugin.getConfig().getString("messages.no-permission"), "[]")) {
                        } else {
                            p.sendMessage(Utils.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.no-permission")));
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
                                        p.sendMessage(Utils.hex("#ee0000[Gizmo] " + Objects.requireNonNull(plugin.getConfig().getString("messages.show-screen-others"))));
                                    }
                                } else if (Objects.equals(plugin.getConfig().getString("enable-background"), "false")) {
                                    Objects.requireNonNull(t.getPlayer()).openInventory(plugin.getServer().createInventory(null, 54, ChatColor.WHITE + shift48 + plugin.getConfig().getString("Unicodes.welcome-screen")));
                                    if (Objects.equals(plugin.getConfig().getString("messages.show-screen-others"), "[]")) {
                                    } else {
                                        p.sendMessage(Utils.hex("#ee0000[Gizmo] " + Objects.requireNonNull(plugin.getConfig().getString("messages.show-screen-others"))));
                                    }
                                }


                            } else {
                                if (Objects.equals(plugin.getConfig().getString("messages.player-not-found"), "[]")) {
                                } else {
                                    p.sendMessage(Utils.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("mmessages.player-not-found")));
                                }
                            }
                        } else {
                            if (Objects.equals(plugin.getConfig().getString("messages.no-permission"), "[]")) {
                            } else {
                                p.sendMessage(Utils.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.no-permission")));
                            }
                        }


                    } else {
                        if (p.hasPermission("gizmo.show")) {

                            saveInv.put(p.getName(), p.getInventory().getContents());
                            p.getInventory().clear();

                            if (Objects.equals(plugin.getConfig().getString("enable-background"), "true")) {
                                Objects.requireNonNull(p.getPlayer()).openInventory(plugin.getServer().createInventory(null, 54, ChatColor.WHITE + shift1013 + plugin.getConfig().getString("Unicodes.background") + shift1536 + plugin.getConfig().getString("Unicodes.welcome-screen")));
                                if (Objects.equals(plugin.getConfig().getString("messages.show-screen"), "[]")) {
                                } else {
                                    p.sendMessage(Utils.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.show-screen")));
                                }
                            } else if (Objects.equals(plugin.getConfig().getString("enable-background"), "false")) {
                                Objects.requireNonNull(p.getPlayer()).openInventory(plugin.getServer().createInventory(null, 54, ChatColor.WHITE + shift48 + plugin.getConfig().getString("Unicodes.welcome-screen")));
                                if (Objects.equals(plugin.getConfig().getString("messages.show-screen"), "[]")) {
                                } else {
                                    p.sendMessage(Utils.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.show-screen")));
                                }
                            }
                        } else {
                            if (Objects.equals(plugin.getConfig().getString("messages.no-permission"), "[]")) {
                            } else {
                                p.sendMessage(Utils.hex("#ee0000[Gizmo] " + plugin.getConfig().getString("messages.no-permission")));
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

    void usage(CommandSender sender) {
        if (sender.hasPermission("gizmo.reload")) {
            sender.sendMessage("");
            sender.sendMessage(Utils.hex("   #ee0000/gizmo reload &7- Reloads the Gizmo config."));
            sender.sendMessage(Utils.hex("   #ee0000/gizmo show <player> &7- Displays a test welcome screen."));
            sender.sendMessage(Utils.hex("   #ee0000/gizmo fade <in> <stay> <out> &7- Display a fade/transition."));
            sender.sendMessage("");
            sender.sendMessage(Utils.hex("   #ee0000Gizmo 1.1.3-BETA &7- Made by Jeqo (https://jeqo.net)"));
            sender.sendMessage("");
        } else {
            sender.sendMessage("");
            sender.sendMessage(Utils.hex("   #ee0000Gizmo 1.1.3-BETA &7- Made by Jeqo (https://jeqo.net)"));
            sender.sendMessage("");
        }
    }
}
