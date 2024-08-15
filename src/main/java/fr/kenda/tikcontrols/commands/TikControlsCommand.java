package fr.kenda.tikcontrols.commands;

import fr.kenda.tikcontrols.TikControls;
import fr.kenda.tikcontrols.utils.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TikControlsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] args) {

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reload")) {
                TikControls.getInstance().reloadConfig();
                commandSender.sendMessage(Messages.transformColor(TikControls.PREFIX + "&aPlugin has been reloaded"));
                return true;
            }
        }
        return false;
    }
}
