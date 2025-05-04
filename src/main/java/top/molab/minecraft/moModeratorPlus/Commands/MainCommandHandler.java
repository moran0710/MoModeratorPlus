package top.molab.minecraft.moModeratorPlus.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class MainCommandHandler implements CommandExecutor, TabCompleter {

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
            case "mute":
                new BanHander().onCommand(sender, command, label, args);
                return true;
            case "unban":
                new UnBanHandler().onCommand(sender, command, label, args);
                return true;
        }
        HelpUtils.sendHelpMessage(sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1:
                return List.of("help", "kick", "ban", "banip", "mute", "unban");
            default:
                switch (args[0].toLowerCase()) {
                    case "help":
                        return List.of("help");
                    case "kick":
                        return new KickHandler().onTabComplete(sender, command, alias, args);
                    case "ban":
                    case "banip":
                    case "mute":
                        return new BanHander().onTabComplete(sender, command, alias, args);
                    case "unban":
                }
        }
        return List.of("<理由>");
    }

}
