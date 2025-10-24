package net.jeqo.gizmo.configuration;

public class PluginConfiguration {
    public static final String NAME = "Gizmo";
    public static final String VERSION = "2.0.0";
    public static final String DEVELOPER_CREDITS = "Jeqo and Gucci Fox";
    @Deprecated
    public static final String DESCRIPTION = "";
    public static final String PLUGIN_DOWNLOAD_PAGE = "https://www.spigotmc.org/resources/gizmo-custom-welcome-screen-more.106024/";

    public static final String PLUGIN_CONFIG_FILE = "config.yml";
    public static final String MESSAGES_CONFIG_FILE = "messages.yml";
    public static final String SCREENS_CONFIG_FILE = "screens.yml";

    public static final int BSTATS_PLUGIN_ID = 16873;
    public static final int SPIGOTMC_PLUGIN_ID = 106024;

    /**
     * Checks if the server and plugin are running in PaperMC mode
     * @return A boolean to determine if the server is running PaperMC
     */
    public static boolean isRunningPaper() {
        boolean isPaper = false;
        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
            isPaper = true;
        } catch (ClassNotFoundException ignored) {}

        return isPaper;
    }
}
