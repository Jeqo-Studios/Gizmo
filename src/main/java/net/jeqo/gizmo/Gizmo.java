package net.jeqo.gizmo;

import net.jeqo.gizmo.data.Commands;
import net.jeqo.gizmo.data.Metrics;
import net.jeqo.gizmo.data.Tab;
import net.jeqo.gizmo.data.Utilities;
import net.jeqo.gizmo.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class Gizmo extends JavaPlugin implements Listener {

    public static HashMap<UUID, String> playerTracker = new HashMap<>();

    @Override
    public void onEnable() {

        Utilities.log("|---[ GIZMO ]--------------------------------------------------------|");
        Utilities.log("|                           Plugin loaded.                           |");
        Utilities.log("|-------------------------------------------------[ MADE BY JEQO ]---|");


        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("|---[ GIZMO - WARNING ]----------------------------------------------|");
            getLogger().warning("|        Disabling PlaceholderAPI support--plugin not found.         |");
            getLogger().warning("|-------------------------------------------------[ MADE BY JEQO ]---|");
        }


        loadListeners(); loadCommands();
        int pluginId = 16873; Metrics metrics = new Metrics(this, pluginId);
        getConfig().options().copyDefaults(); saveDefaultConfig();
    }


    @Override
    public void onDisable() {
        Utilities.log("|---[ GIZMO ]--------------------------------------------------------|");
        Utilities.log("|                          Shutting down...                          |");
        Utilities.log("|-------------------------------------------------[ MADE BY JEQO ]---|");
    }


    public void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new Prime(), this);
        Bukkit.getPluginManager().registerEvents(new Break(), this);
        Bukkit.getPluginManager().registerEvents(new Advance(), this);
        Bukkit.getPluginManager().registerEvents(new Protect(), this);
    }

    public void loadCommands() {
        Objects.requireNonNull(getCommand("gizmo")).setExecutor(new Commands());
        TabCompleter tc = new Tab(); Objects.requireNonNull(this.getCommand("gizmo")).setTabCompleter(tc);
    }

    public boolean papiLoaded() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }
}