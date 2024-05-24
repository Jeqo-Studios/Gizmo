package net.jeqo.gizmo;

import net.jeqo.gizmo.listeners.ClickableItems;
import net.jeqo.gizmo.listeners.PlayerScreeningListener;
import net.jeqo.gizmo.listeners.ScreenAdvanceListener;
import net.jeqo.gizmo.listeners.ScreenHandlers;
import net.jeqo.gizmo.Managers.ConfigManager;
import net.jeqo.gizmo.Managers.ScreeningManager;
import net.jeqo.gizmo.data.*;
import org.bukkit.Bukkit;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Gizmo extends JavaPlugin {

    //TODO change colour support to minimessage

    private final int pluginId = 16873;

    public ConfigManager configManager;
    public ScreeningManager screeningManager;

    @Override
    public void onEnable() {
        Utilities.log("|---[ GIZMO ]--------------------------------------------------------|");
        Utilities.log("|                           Plugin loaded.                           |");
        Utilities.log("|-------------------------------------------------[ MADE BY JEQO ]---|");

        loadManagers();
        loadListeners();

        loadCommands();
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
        screeningManager = new ScreeningManager(this);
    }

    public void loadCommands() {
        getCommand("gizmo").setExecutor(new Commands());
        TabCompleter tc = new CommandsTabManager(); Objects.requireNonNull(this.getCommand("gizmo")).setTabCompleter(tc);
    }

    private void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerScreeningListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ScreenHandlers(this), this);
        Bukkit.getPluginManager().registerEvents(new ScreenAdvanceListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ClickableItems(this), this);
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
