package top.molab.minecraft.moModeratorPlus.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import top.molab.minecraft.moModeratorPlus.MoModeratorPlus;
import top.molab.minecraft.moModeratorPlus.dataStorage.BanStat;
import top.molab.minecraft.moModeratorPlus.dataStorage.BanTypes;
import top.molab.minecraft.moModeratorPlus.dataStorage.ILocalDataManager;
import top.molab.minecraft.moModeratorPlus.message.KickMessageBuilder;
import top.molab.minecraft.moModeratorPlus.runtimeDataManage.RuntimeDataManager;
import top.molab.minecraft.moModeratorPlus.utils.BanIDUtils;
import top.molab.minecraft.moModeratorPlus.utils.TimeUtils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;

public class BanHander implements CommandExecutor {

    @Override
    @ParametersAreNonnullByDefault
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String banType = args[0].toLowerCase();
        String reasonTemp = RuntimeDataManager.getInstance().getConfig().getString("global.default-kick-message");
        if (!RuntimeDataManager.getInstance().getConfig().getBoolean(banType + ".enable")) {
            sender.sendMessage("§c该命令已被禁用，或者找不到该命令");
            return true;
        }
        ILocalDataManager dataManager = RuntimeDataManager.getInstance().getLocalDataManager();
        try {
            // 参数太少
            if (args.length <= 2) {
                HelpUtils.sendHelpMessage(sender);
                return true;
            }

            // 处理理由
            if (args.length == 3) {
                if (RuntimeDataManager.getInstance().getConfig().getBoolean("ban.force-reason")) {
                    sender.sendMessage("§c必须输入封禁理由");
                    return true;
                }
            } else {
                reasonTemp = String.join("\n", Arrays.copyOfRange(args, 3, args.length));
            }
            final String reason = reasonTemp;

            // 获取玩家
            Player player = Bukkit.getPlayerExact(args[1].toLowerCase());
            if (player == null) {
                sender.sendMessage("§c玩家不存在");
                return true;
            }
            String operatorTemp;
            String operatorUUIDTemp;

            //获取处理人的信息
            if (sender instanceof Player) {
                Player senderPlayer = (Player) sender;
                operatorTemp = senderPlayer.getName();
                operatorUUIDTemp = senderPlayer.getUniqueId().toString();
            } else {
                operatorTemp = "CONSOLE";
                operatorUUIDTemp = "CONSOLE";
            }
            final String operator = operatorTemp;
            final String operatorUUID = operatorUUIDTemp;

            // 计算封禁时长
            long expireTime = TimeUtils.getTimeStamp() + TimeUtils.ParseStringToTimeStamp(args[2]);

            // 写入封禁记录
            new BukkitRunnable() {
                @Override
                public void run() {
                    final BanStat banStat = new BanStat(
                            BanIDUtils.getNewBanID(),
                            BanTypes.getTypeByString(banType),
                            reason,
                            TimeUtils.getTimeStamp(),
                            expireTime,
                            player.getAddress().getHostName(),
                            player.getName(),
                            player.getUniqueId().toString(),
                            operator,
                            operatorUUID
                    );
                    // 增加或更新封禁信息
                    if (dataManager.hasBannedPlayer(player.getName())) {
                        dataManager.updateBanStat(banStat);
                    } else {
                        dataManager.addNewBanStat(banStat);
                    }

                    // 如果玩家在线，回到主线程踢出
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (player.isOnline()) {
                                player.kickPlayer(new KickMessageBuilder().setTemplateByType(BanTypes.getTypeByString(banType)).setBanStat(banStat).buildAsString(player));
                            }
                        }
                    }.runTask(MoModeratorPlus.instance);
                    // player.kickPlayer(new KickMessageBuilder().setTemplateByType(BanTypes.getTypeByString(banType)).setBanStat(banStat).buildAsString(player));
                }
            }.runTaskAsynchronously(MoModeratorPlus.instance);
            return true;


        } catch (IllegalArgumentException e) {
            sender.sendMessage("§c时间串格式不正确");
            return true;
        }
    }
}
