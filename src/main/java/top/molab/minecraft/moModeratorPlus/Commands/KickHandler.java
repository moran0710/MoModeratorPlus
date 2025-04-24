package top.molab.minecraft.moModeratorPlus.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import top.molab.minecraft.moModeratorPlus.dataStorage.BanStat;
import top.molab.minecraft.moModeratorPlus.dataStorage.BanTypes;
import top.molab.minecraft.moModeratorPlus.message.KickMessageBuilder;
import top.molab.minecraft.moModeratorPlus.runtimeDataManage.RuntimeDataManager;
import top.molab.minecraft.moModeratorPlus.utils.BanIDUtils;
import top.molab.minecraft.moModeratorPlus.utils.TimeUtils;

import java.util.Arrays;

public class KickHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String operator = "";
        String operatorUUID = "";
        if (args.length <= 1) {
            HelpUtils.sendHelpMessage(sender);
            return true;
        }
        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage("§c玩家不存在");
            return true;
        }
        String reason = "";
        if (args.length == 2 && RuntimeDataManager.getInstance().getConfig().getBoolean("kick.force-reason")) {
            sender.sendMessage("§c必须输入踢出理由");
            return true;
        } else {
            reason = String.join("\n", Arrays.copyOfRange(args, 2, args.length));
        }

        //获取处理人的信息
        if (sender instanceof Player) {
            Player senderPlayer = (Player) sender;
            operator = senderPlayer.getName();
            operatorUUID = senderPlayer.getUniqueId().toString();
        } else {
            operator = "CONSOLE";
            operatorUUID = "CONSOLE";
        }
        BanStat banStat = new BanStat(
                BanIDUtils.getNewKickID(),
                BanTypes.getTypeByString("kick"),
                reason,
                TimeUtils.getTimeStamp(),
                0,
                "",
                player.getName(),
                player.getUniqueId().toString(),
                operator,
                operatorUUID
        );
        player.kickPlayer(new KickMessageBuilder().useKickMessageTemplate().setBanStat(banStat).buildAsString(player));
        return true;
    }
}
