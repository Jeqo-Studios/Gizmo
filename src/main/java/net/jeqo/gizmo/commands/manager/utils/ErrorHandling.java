package net.jeqo.gizmo.commands.manager.utils;

import net.jeqo.gizmo.Gizmo;
import net.jeqo.gizmo.configuration.PluginConfiguration;
import net.jeqo.gizmo.utils.MessageTranslations;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import static net.jeqo.gizmo.utils.Placeholders.gizmoPrefixNoDec;

public class ErrorHandling {

 public static void usage(CommandSender sender) {
     Component blank = Component.text("");
     MessageTranslations messageTranslations = new MessageTranslations(Gizmo.getInstance());

     if (sender.hasPermission("gizmo.reload")) {
         sender.sendMessage(blank);
         sender.sendMessage(messageTranslations.getSerializedString("#ee0000Usages:"));
         sender.sendMessage(messageTranslations.getSerializedString("<white>/gizmo fade <in> <stay> <out> [player] <gray>- " + Gizmo.getCommandCore().getCommandDescription("fade")));
         sender.sendMessage(messageTranslations.getSerializedString("<white>/gizmo reload <gray>- " + Gizmo.getCommandCore().getCommandDescription("reload")));
         sender.sendMessage(messageTranslations.getSerializedString("<white>/gizmo show <player> <gray>- " + Gizmo.getCommandCore().getCommandDescription("show")));
         sender.sendMessage(blank);
         sender.sendMessage(messageTranslations.getSerializedString(gizmoPrefixNoDec() + " " + Gizmo.getInstance().getDescription().getVersion() + " <white>- Made by " + PluginConfiguration.DEVELOPER_CREDITS + " (<underlined>https://jeqo.net<reset>)"));
         sender.sendMessage(blank);
     } else {
         sender.sendMessage(blank);
         sender.sendMessage(messageTranslations.getSerializedString(gizmoPrefixNoDec() + " " + Gizmo.getInstance().getDescription().getVersion() + " <white>- Made by " + PluginConfiguration.DEVELOPER_CREDITS + " (<underlined>https://jeqo.net<reset>)"));
         sender.sendMessage(blank);
     }
 }
}
