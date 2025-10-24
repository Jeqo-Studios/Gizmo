package net.jeqo.gizmo.configuration;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.data.ChatTranslate;
import net.jeqo.gizmo.logger.Logger;
import net.jeqo.gizmo.logger.LoggingLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The configuration core that handles all configuration related tasks
 */
@Getter
public class ConfigurationCore {

    private final Gizmo plugin;
    private FileConfiguration configuration;
    private final File configFile;

    public ConfigurationCore(Gizmo plugin, String configFileName) {
        this.plugin = plugin;
        this.configFile = new File(plugin.getDataFolder(), configFileName);

        if (!configFile.exists()) {
            plugin.saveResource(configFileName, false);
        }

        this.configuration = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Initializes the configuration core by creating and copying the configuration file
     * if needed and sets all default values.
     */
    public void initialize() {
        Logger.log(LoggingLevel.INFO, "Initializing the configuration...");

        // Create the configuration file if it does not exist
        if (!this.getConfigFile().exists()) this.getPlugin().saveDefaultConfig();

        Logger.log(LoggingLevel.INFO, "Configuration " + this.getConfigFile().getName() + " initialized.");
    }

    /**
     * Reloads the configuration file and updates the configuration object
     * to reflect any changes made to the file.
     */
    public void reload() {
        this.configuration = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Gets an entire section and its keys
     *
     * @param path The path to the section in the configuration file
     * @return The section as keys
     */
    public Set<String> getSectionKeys(String path) {
        if (!configuration.contains(path)) {
            Logger.log(LoggingLevel.WARNING, String.format("Configuration %s does not exist in %s. Please add the path if needed.", this.getConfigFile().getName(), path));
            return null;
        }

        if (configuration.getConfigurationSection(path) == null) {
            Logger.log(LoggingLevel.WARNING, String.format("Configuration %s is null in %s. Please set the value.", this.getConfigFile().getName(), path));
            return null;
        }

        return this.configuration.getConfigurationSection(path).getKeys(false);
    }

    public ConfigurationSection getSection(String path) {
        if (!configuration.contains(path)) {
            Logger.log(LoggingLevel.WARNING, String.format("Configuration %s does not exist in %s. Please add the path if needed.", this.getConfigFile().getName(), path));
            return null;
        }

        if (configuration.getConfigurationSection(path) == null) {
            Logger.log(LoggingLevel.WARNING, String.format("Configuration %s is null in %s. Please set the value.", this.getConfigFile().getName(), path));
            return null;
        }

        return configuration.getConfigurationSection(path);
    }

    /**
     * Gets a string from the configuration file via a path to the name of the configuration
     * @param path The path, or name of the variable in the configuration file
     * @return A string if the value is found, otherwise the return is null and a warning is thrown
     */
    public String getString(String path) {
        if (!configuration.contains(path)) {
            Logger.log(LoggingLevel.WARNING, String.format("Configuration %s does not exist in %s. Please add the path if needed.", this.configFile.getName(), path));
            return null;
        }

        if (configuration.getString(path) == null) {
            Logger.log(LoggingLevel.WARNING, String.format("Configuration %s is null in %s. Please set the value.", this.configFile.getName(), path));
            return null;
        }

        return ChatTranslate.translate(null, configuration.getString(path));
    }

    public List<String> getStringList(String path) {
        if (!configuration.contains(path)) {
            Logger.log(LoggingLevel.WARNING, String.format("Configuration %s does not exist in %s. Please add the path if needed.", this.configFile.getName(), path));
            return null;
        }

        return configuration.getStringList(path).stream()
                .map(s -> ChatTranslate.translate(null, s))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a location from the configuration file. The location is stored as a string in the format and the
     * config prefixes must align and end with _WORLD and _COORDINATES.
     * @param path The path to the coordinate configuration line. NOT the world path
     * @return A location object if the location is found, otherwise null
     */
    public Location getLocation(String path) {
        String rawCoordinates = this.getString(path);
        double[] parsedCoordinates = Arrays.stream(rawCoordinates.split(",")).mapToDouble(Double::parseDouble).toArray(); // X, Y, Z
        String configWorldPath = path.replace("COORDINATES", "WORLD");
        String worldName = this.getString(configWorldPath);
        if (worldName == null) {
            Logger.log(LoggingLevel.WARNING, String.format("World %s does not exist in the configuration file. Please add the world if needed.", worldName));
            return null;
        }

        World worldInstance = Bukkit.getWorld(worldName);
        if (worldInstance == null) {
            Logger.log(LoggingLevel.WARNING, String.format("World %s does not exist in the server. Please add the world if needed.", worldName));
            return null;
        }

        return new Location(worldInstance, parsedCoordinates[0], parsedCoordinates[1], parsedCoordinates[2]);
    }

    /**
     * Gets an integer from the configuration file via a path to the name of the configuration
     * @param path The path, or name of the variable in the configuration file
     * @return An integer if the value is found, otherwise the return is -1 and a warning is thrown
     */
    public int getInteger(String path) {
        if (!configuration.contains(path)) {
            Logger.log(LoggingLevel.WARNING, String.format("Configuration %s does not exist in %s. Please add the path if needed.", this.configFile.getName(), path));
            return -1;
        }

        if (configuration.getInt(path) == -1) {
            Logger.log(LoggingLevel.WARNING, String.format("Configuration %s is null in %s. Please set the value.", this.configFile.getName(), path));
            return -1;
        }

        return configuration.getInt(path);
    }

    /**
     * Gets a boolean from the configuration file via a path to the name of the configuration
     * @param path The path, or name of the variable in the configuration file
     * @return A boolean if the value is found, otherwise the return is false and a warning is thrown
     */
    public boolean getBoolean(String path) {
        if (!configuration.contains(path)) {
            Logger.log(LoggingLevel.WARNING, String.format("Configuration %s does not exist in %s. Please add the path if needed.", this.configFile.getName(), path));
            return false;
        }

        if (configuration.get(path) == null) {
            Logger.log(LoggingLevel.WARNING, String.format("Configuration %s is null in %s. Please set the value.", this.configFile.getName(), path));
            return false;
        }

        return configuration.getBoolean(path);
    }

    /**
     * Gets a double from the configuration file via a path to the name of the configuration
     * @param path The path, or name of the variable in the configuration file
     * @return A double if the value is found, otherwise the return is -1 and a warning is thrown
     */
    public double getDouble(String path) {
        if (!configuration.contains(path)) {
            Logger.log(LoggingLevel.WARNING, String.format("Configuration %s does not exist in %s. Please add the path if needed.", this.configFile.getName(), path));
            return -1.0;
        }

        if (configuration.getDouble(path) == -1) {
            Logger.log(LoggingLevel.WARNING, String.format("Configuration %s is null in %s. Please set the value.", this.configFile.getName(), path));
            return -1.0;
        }

        return configuration.getDouble(path);
    }

    public boolean contains(String path) {
        return configuration.contains(path);
    }

    public ItemStack getItemStack(String path) {
        if (!configuration.contains(path)) {
            Logger.log(LoggingLevel.WARNING, String.format("Configuration %s does not exist in %s. Please add the path if needed.", this.configFile.getName(), path));
            return null;
        }

        ItemStack item = configuration.getItemStack(path);
        if (item == null) {
            Logger.log(LoggingLevel.WARNING, String.format("Configuration %s is null in %s. Please set the value.", this.configFile.getName(), path));
            return null;
        }

        return item;
    }
}