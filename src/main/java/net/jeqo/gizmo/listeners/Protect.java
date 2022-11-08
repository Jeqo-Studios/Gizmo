package net.jeqo.gizmo.listeners;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.data.Utilities;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;
import java.util.Objects;

public class Protect implements Listener {

    Gizmo plugin = Gizmo.getPlugin(Gizmo.class);

    public static GameMode joinGm;

    public static Location joinLoc;


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        joinGm = p.getGameMode();
        p.setInvulnerable(true);

        if (Objects.equals(plugin.getConfig().getString("blindness-during-prompt"), "true")) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999, 1, false, false));
        }


        if (p.getGameMode().equals(GameMode.SURVIVAL) || p.getGameMode().equals(GameMode.ADVENTURE)) {
            p.teleport(getLocation(p));
        }

        if (Objects.equals(plugin.getConfig().getString("player-invulnerable-during-load"), "true")) {
            p.setGameMode(GameMode.SPECTATOR);
        }
    }


    @EventHandler
    public void onPackLoad(PlayerResourcePackStatusEvent e) {
        Player p = e.getPlayer();

        if (Objects.equals(plugin.getConfig().getString("kick-on-decline"), "true")) {
            if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                {
                    p.setGameMode(joinGm);
                    p.setInvulnerable(false);
                    p.removePotionEffect(PotionEffectType.BLINDNESS);

                }

            } else if (e.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED || e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                p.setGameMode(joinGm);
                p.setInvulnerable(false);
                p.removePotionEffect(PotionEffectType.BLINDNESS);
                p.kickPlayer(Utilities.hex(Objects.requireNonNull(plugin.getConfig().getString("messages.kick-on-decline")).replace(",", "\n").replace("[", "").replace("]", "")));
            }


        } else if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {

            p.setGameMode(joinGm);
            p.setInvulnerable(false);
            p.removePotionEffect(PotionEffectType.BLINDNESS);
        }
    }


    public Location getLocation(Player player){
        Location location = player.getLocation().clone();

        while (!location.getBlock().getRelative(BlockFace.DOWN).getType().isSolid() && location.getY() != 0){
            location.add(0, -1, 0);
        }

        if (location.getY() == 0){
            return player.getLocation();
        }

        location.add(0, 2.25, 0);
        return location;
    }
}
