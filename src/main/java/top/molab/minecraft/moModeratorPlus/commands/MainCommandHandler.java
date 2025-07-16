package top.molab.minecraft.moModeratorPlus.commands;


import cc.carm.lib.easyplugin.utils.ColorParser;
import org.apache.commons.lang3.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import top.molab.minecraft.moModeratorPlus.dataStorage.BanTypes;
import top.molab.minecraft.moModeratorPlus.runtimeDataManage.RuntimeDataManager;
import top.molab.minecraft.moModeratorPlus.utils.PunishExecuteUtils;
import top.molab.minecraft.moModeratorPlus.utils.TimeUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class MainCommandHandler implements CommandExecutor, TabCompleter {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (List.of("ban", "banip", "mute", "kick", "unban").contains(label)) {
            String[] argsTemp = new String[1];
            argsTemp[0] = label;
            args = ArrayUtils.addAll(argsTemp, args);
        }
        if (args[0].equalsIgnoreCase("reload")) {
            RuntimeDataManager.getInstance().init();
            sender.sendMessage(ColorParser.parse("&(#66ccff)已重载配置文件"));
            return true;
        }
        if (args[0].equalsIgnoreCase("unban") && args.length >= 2) {
            PunishExecuteUtils.DeletePunish(args[1], (Player) sender);
            return true;
        }
        if (args.length < 2 || args[0].equalsIgnoreCase("help")) {
            HelpUtils.sendHelpMessage(sender);
            return true;
        }
        BanTypes banType = BanTypes.getTypeByString(args[0].toLowerCase());
        if (Objects.requireNonNull(banType) == BanTypes.Kick) {
            PunishExecuteUtils.ExecutePunish(banType,
                    args[1],
                    args.length > 2 ? Arrays.copyOfRange(args, 2, args.length) : new String[]{RuntimeDataManager.getInstance().getConfig().getString("global.default-kick-message")},
                    0, 0,
                    (Player) sender);
            return true;
        }
        try {
            PunishExecuteUtils.ExecutePunish(
                    banType,
                    args[1],
                    args.length > 3 ? Arrays.copyOfRange(args, 3, args.length) : new String[]{RuntimeDataManager.getInstance().getConfig().getString("global.default-kick-message")},
                    TimeUtils.getTimeStamp(),
                    TimeUtils.getTimeStamp() + TimeUtils.ParseStringToTimeStamp(args[2]),
                    (Player) sender
            );
            return true;
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ColorParser.parse("&c时间格式错误"));
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            sender.sendMessage(ColorParser.parse("&c你需要输入封禁时间"));
            return true;
        } catch (Exception e) {
            sender.sendMessage(ColorParser.parse("&c出现未知错误，请查看日志进行排查"));
            throw new RuntimeException(e);
        }
    }

    @ParametersAreNonnullByDefault
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        switch (args.length) {
            case 1:
                return List.of("help", "kick", "ban", "banip", "mute", "unban", "reload");
            case 2:
                switch (args[0]) {
                    case "help":
                        return List.of();
                    case "kick":
                    case "ban":
                    case "banip":
                    case "mute":
                        List<String> online = new java.util.ArrayList<>(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
                        List<String> offline = Arrays.stream(Bukkit.getOfflinePlayers()).map(OfflinePlayer::getName).toList();
                        online.addAll(offline);
                        return online;
                    case "unban":
                        List<String> lst = RuntimeDataManager.getInstance().getLocalDataManager().getAllBanID();
                        lst.addAll(
                                RuntimeDataManager.getInstance().getLocalDataManager().getAllBanID()
                        );
                        return lst;
                }
            case 3:
                if (args[1].equals("kick")) {
                    return List.of("<Reason>");
                }
            case 4:
                return List.of("<Reason>");
        }
        return null;
    }

}
