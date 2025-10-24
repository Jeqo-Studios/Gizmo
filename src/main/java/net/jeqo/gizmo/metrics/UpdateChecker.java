package net.jeqo.gizmo.metrics;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.logger.Logger;
import net.jeqo.gizmo.logger.LoggingLevel;
import org.bukkit.Bukkit;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

/**
 *                      A utility class intended to check for updates on the SpigotMC website
 * @param plugin        The plugin instance, type org.bukkit.plugin.java.JavaPlugin
 * @param resourceId    The resource ID of the plugin on SpigotMC, type int
 */
public record UpdateChecker(Gizmo plugin, int resourceId) {

    /**
     *                  Gets the latest version of the plugin available on SpigotMC
     * @param consumer  The consumer to accept the version, type java.util.function.Consumer[java.lang.String]
     */
    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin(), () -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(new URI("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId()))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                consumer.accept(response.body());
            } catch (Exception exception) {
                Logger.log(LoggingLevel.WARNING, "Unable to check for updates: " + exception.getMessage());
            }
        });
    }

    /**
     * Checks for updates and notifies the user via a log to console
     * getDescription() is still used because of the usage of a plugin.yml.
     * Not planned to change
     */
    public void check() {
        new UpdateChecker(Gizmo.getInstance(), resourceId).getVersion(version -> {
            String currentVersion = Gizmo.getInstance().getDescription().getVersion();

            if (VersionChecker.isVersionLower(currentVersion, version)) {
                Logger.logUpdateNotificationConsole();
            } else if (VersionChecker.isVersionHigher(currentVersion, version)) {
                Logger.logUnreleasedVersionNotification();
            }
        });
    }
}
