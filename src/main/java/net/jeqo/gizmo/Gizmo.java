package net.jeqo.gizmo;

import net.jeqo.gizmo.data.*;
import net.jeqo.gizmo.listeners.PlayerScreening;
import net.jeqo.gizmo.listeners.ScreenAdvance;
import net.jeqo.gizmo.listeners.ScreenHandlers;
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

    @Override
    public void onEnable() {
        Utilities.log("|---[ GIZMO ]--------------------------------------------------------|");
        Utilities.log("|                           Plugin loaded.                           |");
        Utilities.log("|-------------------------------------------------[ MADE BY JEQO ]---|");

        instance = this;
        loadListeners(); loadCommands();
        Metrics metrics = new Metrics(this, pluginId); updateChecker();

        getConfig().options().copyDefaults(); saveDefaultConfig();
        createScreensConfig(); createMessagesConfig();
    }
    @Override
    public void onDisable() {
        Utilities.log("|---[ GIZMO ]--------------------------------------------------------|");
        Utilities.log("|                          Shutting down...                          |");
        Utilities.log("|-------------------------------------------------[ MADE BY JEQO ]---|");
    }



    public static HashMap<UUID, String> playerTracker = new HashMap<>();
    public static Gizmo instance;
    int pluginId = 16873;
    public static Gizmo getInstance() {
        return instance;
    }

    public void loadListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerScreening(), this);
        Bukkit.getPluginManager().registerEvents(new ScreenHandlers(), this);
        Bukkit.getPluginManager().registerEvents(new ScreenAdvance(), this);
    }
    public void loadCommands() {
        Objects.requireNonNull(getCommand("gizmo")).setExecutor(new Commands());
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



    // screens.yml
    private File screensConfigFile;
    private FileConfiguration screensConfig;
    public FileConfiguration getScreensConfig() {
        return this.screensConfig;
    }
    private void createScreensConfig() {
        screensConfigFile = new File(getDataFolder(), "screens.yml");
        if (!screensConfigFile.exists()) {
            screensConfigFile.getParentFile().mkdirs();
            saveResource("screens.yml", false);
        }

        screensConfig = new YamlConfiguration();
        try {
            screensConfig.load(screensConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }



    // messages.yml
    private File messagesConfigFile;
    private FileConfiguration messagesConfig;
    public FileConfiguration getMessagesConfig() {
        return this.messagesConfig;
    }
    private void createMessagesConfig() {
        messagesConfigFile = new File(getDataFolder(), "messages.yml");
        if (!messagesConfigFile.exists()) {
            messagesConfigFile.getParentFile().mkdirs();
            saveResource("messages.yml", false);
        }

        messagesConfig = new YamlConfiguration();
        try {
            messagesConfig.load(messagesConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
