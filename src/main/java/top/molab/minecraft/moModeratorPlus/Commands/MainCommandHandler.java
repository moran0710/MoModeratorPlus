package top.molab.minecraft.moModeratorPlus.Commands;

import cc.carm.lib.easyplugin.utils.ColorParser;
import org.apache.commons.text.StringSubstitutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import top.molab.minecraft.moModeratorPlus.dataStorage.BanStat;
import top.molab.minecraft.moModeratorPlus.runtimeDataManage.RuntimeDataManager;
import top.molab.minecraft.moModeratorPlus.utils.PlayerUtils;
import top.molab.minecraft.moModeratorPlus.utils.TimeUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;

public class MainCommandHandler implements CommandExecutor, TabCompleter {

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            HelpUtils.sendHelpMessage(sender);
            return true;
        }
        if (!sender.hasPermission("moModPlus.admin.command." + args[0])) {
            sender.sendMessage("&c你没有权限");
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
            case "reload":
                RuntimeDataManager.getInstance().init();
                sender.sendMessage(ColorParser.parse("&(#66ccff)重载成功"));
                return true;
            case "search":
                BanStat banstat = PlayerUtils.getBanStat(args[1]);
                if (banstat == null) {
                    sender.sendMessage(ColorParser.parse("&(#66ccff)没有&6" + args[1] + " &(#66ccff)的封禁数据"));
                    return true;
                }
                HashMap<String, String> map = new HashMap<>() {{
                    put("player", banstat.PlayerName());
                    put("operator", sender.getName());
                    put("reason", banstat.Reason());
                    put("banid", banstat.BanID());
                    put("type", banstat.BanType().getType());
                    put("effectiveTime", TimeUtils.FormatTimeStampToStringDate(banstat.EffectiveTime()));
                    put("expireTime", TimeUtils.FormatTimeStampToStringDate(banstat.ExpireTime()));
                    put("duration", TimeUtils.FormatSecondsCustom(banstat.getDuration()));
                }};
                sender.sendMessage(
                        StringSubstitutor.replace(
                                ColorParser.parse(
                                        """
                                                &(#66ccff)===================
                                                &(#66ccff)# 玩家 &6${player} &(#66ccff)的封禁数据
                                                &(#66ccff)# 封禁原因: &6${reason}
                                                &(#66ccff)# 封禁操作人: &6${operator}
                                                &(#66ccff)# BanID：&6${banid}
                                                &(#66ccff)# 封禁类型：&6${type}
                                                &(#66ccff)# 封禁开始时间: &6${effectiveTime}
                                                &(#66ccff)# 封禁结束时间: &6${expireTime}
                                                &(#66ccff)# 封禁时长: &6${duration}
                                                &(#66ccff)==================="""
                                ), map)
                );
                return true;
        }
        HelpUtils.sendHelpMessage(sender);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return List.of("help", "kick", "ban", "banip", "mute", "unban", "reload", "search");
        } else {
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
                    return new UnBanHandler().onTabComplete(sender, command, alias, args);
                case "reload":
                    return List.of("reload");
                case "search":
                    List<String> bans = RuntimeDataManager.getInstance().getLocalDataManager().getAllBanID();
                    bans.addAll(RuntimeDataManager.getInstance().getLocalDataManager().getAllPlayerName());
                    return bans;

            }
        }
        return null;
    }

}
