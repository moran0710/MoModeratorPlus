package top.molab.minecraft.moModeratorPlus.Commands;

import cc.carm.lib.easyplugin.utils.ColorParser;
import org.apache.commons.text.StringSubstitutor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import top.molab.minecraft.moModeratorPlus.dataStorage.BanStat;
import top.molab.minecraft.moModeratorPlus.runtimeDataManage.RuntimeDataManager;
import top.molab.minecraft.moModeratorPlus.utils.PlayerUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;

public class UnBanHandler implements CommandExecutor {
    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length != 2) {
            HelpUtils.sendHelpMessage(sender);
            return true;
        }
        BanStat banStat = PlayerUtils.getBanStat(args[1]);
        if (banStat == null) {
            sender.sendMessage(ColorParser.parse("&(#66ccff)没有&6" + args[1] + " &(#66ccff)的封禁数据"));
            return true;
        }

        RuntimeDataManager.getInstance().getLocalDataManager().deleteBanStat(banStat);
        BanStat finalBanStat = banStat;
        HashMap<String, String> map = new HashMap<>() {{
            put("player", finalBanStat.PlayerName());
            put("operator", sender.getName());
            put("reason", finalBanStat.Reason());
            put("banid", finalBanStat.BanID());
            put("type", finalBanStat.BanType().getType());
        }};
        sender.sendMessage(
                StringSubstitutor.replace(
                        ColorParser.parse(
                                """
                                        &(#66ccff)===================
                                        &(#66ccff)# 玩家 &6${player} &(#66ccff)已被解禁
                                        &(#66ccff)# 封禁原因: &6${reason}
                                        &(#66ccff)# 封禁操作人: &6${operator}
                                        &(#66ccff)# BanID：&6${banid}
                                        &(#66ccff)# 封禁类型：&6${type}
                                        &(#66ccff)==================="""
                        ), map)
        );
        return true;
    }
}
