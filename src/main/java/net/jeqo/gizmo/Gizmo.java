package net.jeqo.gizmo;

import net.jeqo.gizmo.data.UpdateChecker;
import net.jeqo.gizmo.listeners.ClickableItemsListener;
import net.jeqo.gizmo.listeners.PlayerScreeningListener;
import net.jeqo.gizmo.listeners.ScreenAdvanceListener;
import net.jeqo.gizmo.listeners.ScreenHandlersListener;
import net.jeqo.gizmo.Managers.ConfigManager;
import net.jeqo.gizmo.Managers.ScreeningManager;
import net.jeqo.gizmo.data.*;
import org.bukkit.Bukkit;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Gizmo extends JavaPlugin {

    //TODO change colour support to minimessage as the & will be removed in the future

    private final int pluginId = 16873;

    public ConfigManager configManager;
    public ScreeningManager screeningManager;

    @Override
    public void onEnable() {
        this.getLogger().log(Level.INFO, "|---[ GIZMO ]--------------------------------------------------------|");
        this.getLogger().log(Level.INFO, "|                           Plugin loaded.                           |");
        this.getLogger().log(Level.INFO, "|-------------------------------------------------[ MADE BY JEQO ]---|");

        loadManagers();
        loadListeners();

        loadCommands();
        Metrics metrics = new Metrics(this, pluginId); updateChecker();
    }

    @Override
    public void onDisable() {
        this.getLogger().log(Level.INFO, "|---[ GIZMO ]--------------------------------------------------------|");
        this.getLogger().log(Level.INFO, "|                          Shutting down...                          |");
        this.getLogger().log(Level.INFO, "|-------------------------------------------------[ MADE BY JEQO ]---|");
    }

    private void loadManagers() {
        configManager = new ConfigManager(this);
        screeningManager = new ScreeningManager(this);
    }

    private void loadCommands() {
        getCommand("gizmo").setExecutor(new Commands());
        TabCompleter tc = new CommandsTabManager();
        this.getCommand("gizmo").setTabCompleter(tc);
    }

    private void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerScreeningListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ScreenHandlersListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ScreenAdvanceListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ClickableItemsListener(this), this);
    }

    private void updateChecker() {
        new UpdateChecker(this, 106024).getVersion(version -> {
            if (this.getDescription().getVersion().equals(version)) return;
            
            this.getLogger().warning("|---[ GIZMO ]--------------------------------------------------------|");
            this.getLogger().warning("|                  There is a new update available!                  |");
            this.getLogger().warning("|                       https://jeqo.net/gizmo                       |");
            this.getLogger().warning("|-------------------------------------------------[ MADE BY JEQO ]---|");
        });
    }
}
