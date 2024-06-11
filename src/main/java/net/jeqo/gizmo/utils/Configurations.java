package net.jeqo.gizmo.utils;

import lombok.Getter;
import lombok.Setter;
import net.jeqo.gizmo.Gizmo;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Manages the various configuration files used within the plugin
 */
public class Configurations {
    @Setter @Getter
    public static File messagesConfigFile;
    @Setter @Getter
    public static FileConfiguration messagesConfig;
    @Setter @Getter
    public static File screensConfigFile;
    @Setter @Getter
    public static FileConfiguration screensConfig;

    /**
     * Generate the screens configuration file if necessary and loads it
     */
    public static void generateScreensConfiguration() {
        setScreensConfigFile(new File(Gizmo.getInstance().getDataFolder(), "screens.yml"));
        if (!getScreensConfigFile().exists()) {
            getScreensConfigFile().getParentFile().mkdirs();
            Gizmo.getInstance().saveResource("screens.yml", false);
        }

        setScreensConfig(new YamlConfiguration());
        try {
            getScreensConfig().load(getScreensConfigFile());
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate the messages configuration file if necessary and loads it
     */
    public static void generateMessagesConfiguration() {
        setMessagesConfigFile(new File(Gizmo.getInstance().getDataFolder(), "messages.yml"));
        if (!getMessagesConfigFile().exists()) {
            getMessagesConfigFile().getParentFile().mkdirs();
            Gizmo.getInstance().saveResource("messages.yml", false);
        }

        setMessagesConfig(new YamlConfiguration());
        try {
            getMessagesConfig().load(getMessagesConfigFile());
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
