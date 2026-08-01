package net.jeqo.gizmo.listener;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.data.ChatTranslate;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class ResourcePackDeclineListener implements Listener {

    @EventHandler
    public void onPackLoad(PlayerResourcePackStatusEvent e) {
        Player p = e.getPlayer();

        if (Objects.equals(Gizmo.getInstance().getConfig().getString("kick-on-decline"), "true")) {
            if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                disablePotionEffects(p);
            } else if (e.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED || e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                disablePotionEffects(p);
                java.util.List<String> kickMsgs = Gizmo.getInstance().getMessagesConfigurationCore().getStringList("kick-on-decline");
                String kickReason = ChatTranslate.translate(p, String.join("\n", kickMsgs));
                p.kickPlayer(kickReason);
            }
        } else if (Objects.equals(Gizmo.getInstance().getConfig().getString("kick-on-decline"), "false")) {
            if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
                disablePotionEffects(p);
            } else if (e.getStatus() == PlayerResourcePackStatusEvent.Status.DECLINED || e.getStatus() == PlayerResourcePackStatusEvent.Status.FAILED_DOWNLOAD) {
                disablePotionEffects(p);
                for (String msg : Gizmo.getInstance().getMessagesConfigurationCore().getStringList("no-pack-loaded")) {
                    p.sendMessage(ChatTranslate.translate(p, msg));
                }
            }
        }
    }

    private void disablePotionEffects(Player p) {

        if (Objects.equals(Gizmo.getInstance().getConfig().getString("clear-effects-on-load"), "false")){
            p.removePotionEffect(PotionEffectType.BLINDNESS);
            return;
        }

        for (org.bukkit.potion.PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }
    }
}
