package net.jeqo.gizmo.configuration;

import net.jeqo.gizmo.Gizmo;

/**
 * A class that contains configurations and information regarding the plugin
 */
public class PluginConfiguration {
    // The developer credits for the plugin, displayed on startup and in the help command
    public static final String DEVELOPER_CREDITS = "Jeqo and Gucci Fox";

    /**
     * Get the version of the plugin from the pom.xml file
     * @return The version of the plugin
     */
    public static String getVersion() {
        return Gizmo.getInstance().getDescription().getVersion();
    }

    /**
     * Get the name of the plugin from the plugin.yml file
     * @return The name of the plugin
     */
    public static String getName() {
        return Gizmo.getInstance().getDescription().getName();
    }

    /**
     * Get the description of the plugin from the plugin.yml file
     * @return The description of the plugin
     */
    public static String getDescription() {
        return Gizmo.getInstance().getDescription().getDescription();
    }

    /**
     * Gets the website URL of the plugin from the plugin.yml file
     * @return The website URL of the plugin
     */
    public static String getURL() {
        return Gizmo.getInstance().getDescription().getWebsite();
    }
}

