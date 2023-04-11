package net.jeqo.gizmo;

import net.jeqo.gizmo.data.*;
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
    public static Gizmo instance;

    @Override
    public void onEnable() {

        Utils.log("|---[ GIZMO ]--------------------------------------------------------|");
        Utils.log("|                           Plugin loaded.                           |");
        Utils.log("|-------------------------------------------------[ MADE BY JEQO ]---|");

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("|---[ GIZMO - WARNING ]----------------------------------------------|");
            getLogger().warning("|        Disabling PlaceholderAPI support--plugin not found.         |");
            getLogger().warning("|-------------------------------------------------[ MADE BY JEQO ]---|");
        }

        instance = this;
        loadListeners(); loadCommands();
        Metrics metrics = new Metrics(this, pluginId); updateChecker();
        getConfig().options().copyDefaults(); saveDefaultConfig();
    }


    @Override
    public void onDisable() {
        Utils.log("|---[ GIZMO ]--------------------------------------------------------|");
        Utils.log("|                          Shutting down...                          |");
        Utils.log("|-------------------------------------------------[ MADE BY JEQO ]---|");
    }















    int pluginId = 16873;
    public void updateChecker() {
        new UpdateChecker(this, 106024).getVersion(version -> {
            if (!this.getDescription().getVersion().equals(version)) {
                Utils.warn("|---[ GIZMO ]--------------------------------------------------------|");
                Utils.warn("|                  There is a new update available!                  |");
                Utils.warn("|                   https://jeqo.net/spigot/gizmo                    |");
                Utils.warn("|-------------------------------------------------[ MADE BY JEQO ]---|");
            }
        });
    }


    public void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new Initiate(), this);
        Bukkit.getPluginManager().registerEvents(new Advance(), this);
        Bukkit.getPluginManager().registerEvents(new Handlers(), this);
    }

    public void loadCommands() {
        Objects.requireNonNull(getCommand("gizmo")).setExecutor(new Commands());
        TabCompleter tc = new Tab(); Objects.requireNonNull(this.getCommand("gizmo")).setTabCompleter(tc);
    }


    public static String getMessage(String id) {
        return Utils.hex(getInstance().getConfig().getString("messages." + id, ""));
    }


    public static Gizmo getInstance() {
        return instance;
    }

    public boolean papiLoaded() {
        return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
    }
}