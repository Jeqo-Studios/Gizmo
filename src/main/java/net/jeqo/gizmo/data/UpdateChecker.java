package net.jeqo.gizmo.data;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.Utils.ColourUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import static net.jeqo.gizmo.data.Placeholders.gizmoPrefix;

public class UpdateChecker implements Listener {

    private final Gizmo plugin;
    private final int resourceId;

    private final ColourUtils colourUtils = new ColourUtils();

    public UpdateChecker(Gizmo plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    // Check for updates
    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Unable to check for updates: " + exception.getMessage());
            }
        });
    }

    // Send a message to the player if there is a new update available
    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!player.hasPermission("gizmo.updatechecker")) return;

        new UpdateChecker(plugin, 106024).getVersion(version -> {
            if (plugin.getDescription().getVersion().equals(version)) return;

            player.sendMessage("");
            player.sendMessage(colourUtils.oldFormat(gizmoPrefix() + "&eNew update! " + version + " is now available."));
            player.sendMessage(colourUtils.oldFormat(gizmoPrefix() + "&eDownload it here: &nhttps://jeqo.net/gizmo"));
            player.sendMessage("");
        });
    }
}