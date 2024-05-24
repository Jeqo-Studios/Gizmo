package net.jeqo.gizmo.Managers;

import net.jeqo.gizmo.Gizmo;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {

    private final Gizmo plugin;

    private FileConfiguration config, screens, lang;

    public ConfigManager(Gizmo plugin) {
        this.plugin = plugin;

        load();
    }

    public void load() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
            plugin.saveResource("config.yml", false);
            plugin.saveResource("screens.yml", false);
            plugin.saveResource("Lang/uk-lang.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
        screens = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "screens.yml"));
        lang = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "Lang/" + config.getString("lang")));
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getScreens() {
        return screens;
    }

    public FileConfiguration getLang() {
        return lang;
    }
}
