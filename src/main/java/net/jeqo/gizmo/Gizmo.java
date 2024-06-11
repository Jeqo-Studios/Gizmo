package net.jeqo.gizmo;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.gizmo.data.*;
import net.jeqo.gizmo.listeners.*;
import net.jeqo.gizmo.logger.Logger;
import net.jeqo.gizmo.utils.Configurations;
import net.jeqo.gizmo.utils.UpdateChecker;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class Gizmo extends JavaPlugin {
    @Getter @Setter
    public static File messagesConfigFile;
    @Getter @Setter
    public static FileConfiguration messagesConfig;
    @Getter @Setter
    public static File screensConfigFile;
    @Getter @Setter
    public static FileConfiguration screensConfig;
    @Getter @Setter
    public static Gizmo instance;
    @Getter
    public static HashMap<UUID, String> playerTracker = new HashMap<>();
    @Getter @Setter
    private static ListenerCore listenerCore;

    @Override
    public void onEnable() {
        // Create an instance of the plugin
        setInstance(this);

        // Send startup message
        Logger.logStartup();

        // Register core managers within the plugin
        loadCommands();
        setListenerCore(new ListenerCore(getInstance()));

        // Stage listeners
        getListenerCore().stageListener(new PlayerScreening());
        getListenerCore().stageListener(new ScreenHandlers());
        getListenerCore().stageListener(new ScreenAdvance());
        getListenerCore().stageListener(new ClickableItems());

        // Register all handlers
        getListenerCore().registerListeners();

        // Startup metrics and update checker
        int pluginId = 16873;
        new Metrics(this, pluginId);
        updateChecker();

        // Generate config(s) and set defaults
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        Configurations.generateScreensConfiguration();
        Configurations.generateMessagesConfiguration();
    }

    @Override
    public void onDisable() {
        // Log shutdown message
        Logger.logShutdown();

        // Unregister all listeners in the manager
        getListenerCore().unregisterListeners();
    }

    /**
     * Loads all Gizmo commands and tab completions
     */
    public void loadCommands() {
        Objects.requireNonNull(getCommand("gizmo")).setExecutor(new Commands());
        TabCompleter tabCompleter = new CommandsTabManager();
        Objects.requireNonNull(this.getCommand("gizmo")).setTabCompleter(tabCompleter);
    }

    /**
     * Checks for updates and notifies the user via a log to console
     * getDescription() is still used because of the usage of a plugin.yml.
     * Not planned to change
     */
    public void updateChecker() {
        new UpdateChecker(this, 106024).getVersion(version -> {
            if (!this.getDescription().getVersion().equals(version)) {
                Logger.logUpdateNotificationConsole();
            }
        });
    }
}
