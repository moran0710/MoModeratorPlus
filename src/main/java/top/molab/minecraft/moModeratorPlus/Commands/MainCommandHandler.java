package top.molab.minecraft.moModeratorPlus.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.annotation.ParametersAreNonnullByDefault;

public class MainCommandHandler implements CommandExecutor {

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            HelpUtils.sendHelpMessage(sender);
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "help":
                HelpUtils.sendHelpMessage(sender);
                return true;
            case "kick":
                new KickHandler().onCommand(sender, command, label, args);
                return true;
            case "banip":
            case "ban":
                new BanHander().onCommand(sender, command, label, args);
                return true;
        }
        return false;
    }

}
