package net.jeqo.gizmo.utils;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.data.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import static net.jeqo.gizmo.data.Placeholders.gizmoPrefix;

public class UpdateChecker implements Listener {

    private final JavaPlugin plugin;
    private final int resourceId;
    public UpdateChecker(JavaPlugin plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }


    // Check for updates
    public void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
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
    public void playerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().isOp()) {
            Player p = e.getPlayer();
            new UpdateChecker(Gizmo.getInstance(), 106024).getVersion(version -> {
                if (!Gizmo.getInstance().getDescription().getVersion().equals(version)) {
                    p.sendMessage("");
                    p.sendMessage(Utilities.chatTranslate(gizmoPrefix() + "&eNew update! " + version + " is now available."));
                    p.sendMessage(Utilities.chatTranslate(gizmoPrefix() + "&eDownload it here: &nhttps://jeqo.net/gizmo"));
                    p.sendMessage("");
                }
            });
        }
    }
}
