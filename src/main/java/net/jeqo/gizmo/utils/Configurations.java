package net.jeqo.gizmo.utils;

import net.jeqo.gizmo.Gizmo;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Manages the various configuration files used within the plugin
 */
public class Configurations {

    /**
     * Generate the screens configuration file if necessary and loads it
     */
    public static void generateScreensConfiguration() {
        Gizmo.setScreensConfigFile(new File(Gizmo.getInstance().getDataFolder(), "screens.yml"));
        if (!Gizmo.getScreensConfigFile().exists()) {
            Gizmo.getScreensConfigFile().getParentFile().mkdirs();
            Gizmo.getInstance().saveResource("screens.yml", false);
        }

        Gizmo.setScreensConfig(new YamlConfiguration());
        try {
            Gizmo.getScreensConfig().load(Gizmo.getScreensConfigFile());
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generate the messages configuration file if necessary and loads it
     */
    public static void generateMessagesConfiguration() {
        Gizmo.setMessagesConfigFile(new File(Gizmo.getInstance().getDataFolder(), "messages.yml"));
        if (!Gizmo.getMessagesConfigFile().exists()) {
            Gizmo.getMessagesConfigFile().getParentFile().mkdirs();
            Gizmo.getInstance().saveResource("messages.yml", false);
        }

        Gizmo.setMessagesConfig(new YamlConfiguration());
        try {
            Gizmo.getMessagesConfig().load(Gizmo.getMessagesConfigFile());
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
