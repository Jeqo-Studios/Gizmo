package net.jeqo.gizmo.logger;

import net.jeqo.gizmo.configuration.PluginConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * A utility class intended to log messages easily to the Bukkit console
 */
public class Logger {

    /**
     * Log a message to the console
     * @param level The logging level
     * @param message The message to log
     */
    public static void log(LoggingLevel level, String message) {
        String logMessage = level.getColor() + "[" + level.getName() + "] " + ChatColor.RESET + message;
        Bukkit.getConsoleSender().sendMessage(logMessage);
    }

    /**
     * Logs an initialization message to the Bukkit console
     */
    public static void logInitialization() {
        log(LoggingLevel.INFO, "Initializing the " + PluginConfiguration.NAME + " plugin...");
    }

    /**
     * Logs a startup message to the Bukkit console containing plugin information
     */
    public static void logStartup() {
        log(LoggingLevel.INFO, PluginConfiguration.NAME + " plugin has initialized");
        log(LoggingLevel.INFO, "Version: " + PluginConfiguration.VERSION);
        log(LoggingLevel.INFO, "Developers: " + PluginConfiguration.DEVELOPER_CREDITS);
    }

    /**
     * Logs an update notification to the Bukkit console
     */
    public static void logUpdateNotificationConsole() {
        log(LoggingLevel.INFO,"A new update is available for " + PluginConfiguration.NAME + " plugin");
        log(LoggingLevel.INFO, "You can find the update at: " + PluginConfiguration.PLUGIN_DOWNLOAD_PAGE);
    }

    /**
     * Logs an unreleased plugin version notification to the Bukkit console
     */
    public static void logUnreleasedVersionNotification() {
        log(LoggingLevel.INFO, "The version for the " + PluginConfiguration.NAME + " plugin is higher than latest version");
        log(LoggingLevel.INFO, "You are currently running an unreleased version of the plugin that is NOT stable");
    }

    /**
     * Logs a shutdown message to the Bukkit console
     */
    public static void logShutdown() {
        log(LoggingLevel.INFO, PluginConfiguration.NAME + " plugin has been shutdown gracefully");
    }
}
