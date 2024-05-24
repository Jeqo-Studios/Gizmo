package net.jeqo.gizmo;

import net.jeqo.gizmo.Managers.ConfigManager;
import net.jeqo.gizmo.Managers.PlayerManager;
import net.jeqo.gizmo.data.*;
import net.jeqo.gizmo.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class Gizmo extends JavaPlugin implements Listener {

    public ConfigManager configManager;
    public PlayerManager playerManager;

    @Override
    public void onEnable() {
        Utilities.log("|---[ GIZMO ]--------------------------------------------------------|");
        Utilities.log("|                           Plugin loaded.                           |");
        Utilities.log("|-------------------------------------------------[ MADE BY JEQO ]---|");

        loadManagers();

        instance = this;
        loadListeners(); loadCommands();
        Metrics metrics = new Metrics(this, pluginId); updateChecker();
    }

    @Override
    public void onDisable() {
        Utilities.log("|---[ GIZMO ]--------------------------------------------------------|");
        Utilities.log("|                          Shutting down...                          |");
        Utilities.log("|-------------------------------------------------[ MADE BY JEQO ]---|");
    }

    private void loadManagers() {
        configManager = new ConfigManager(this);
        playerManager = new PlayerManager();
    }



    public static Gizmo instance;
    int pluginId = 16873;
    public static Gizmo getInstance() {
        return instance;
    }

    public void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerScreening(), this);
        Bukkit.getPluginManager().registerEvents(new ScreenHandlers(), this);
        Bukkit.getPluginManager().registerEvents(new ScreenAdvance(), this);
        Bukkit.getPluginManager().registerEvents(new ClickableItems(), this);
    }
    public void loadCommands() {
        getCommand("gizmo").setExecutor(new Commands());
        TabCompleter tc = new CommandsTabManager(); Objects.requireNonNull(this.getCommand("gizmo")).setTabCompleter(tc);
    }

    public void updateChecker() {
        new UpdateChecker(this, 106024).getVersion(version -> {
            if (!this.getDescription().getVersion().equals(version)) {
                Utilities.warn("|---[ GIZMO ]--------------------------------------------------------|");
                Utilities.warn("|                  There is a new update available!                  |");
                Utilities.warn("|                       https://jeqo.net/gizmo                       |");
                Utilities.warn("|-------------------------------------------------[ MADE BY JEQO ]---|");
            }
        });
    }
}
