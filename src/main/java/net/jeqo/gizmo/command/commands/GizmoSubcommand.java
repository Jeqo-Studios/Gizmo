package net.jeqo.gizmo.command.commands;

import net.jeqo.gizmo.command.GizmoCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class GizmoSubcommand extends GizmoCommand {
    @Override
    public String name() {
        return "gizmo";
    }

    @Override
    public String syntax() {
        return "gizmo <help|fade|reload|show> <args>";
    }

    @Override
    public String description() {
        return "Handles all of the Gizmo subcommands.";
    }

    @Override
    public ArrayList<GizmoCommand> subcommands() {
        ArrayList<GizmoCommand> subcommands = new ArrayList<>();
        subcommands.add(new FadeCommand());
        subcommands.add(new ReloadCommand());
        subcommands.add(new ShowCommand());
        subcommands.add(new HelpCommand());

        return subcommands;
    }

    @Override
    public int minArgs() {
        return 1;
    }

    @Override
    public int maxArgs() {
        return 5;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {}
}
