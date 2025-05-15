package top.molab.minecraft.moModeratorPlus.utils;

import cc.carm.lib.easyplugin.utils.ColorParser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import top.molab.minecraft.moModeratorPlus.dataStorage.BanStat;
import top.molab.minecraft.moModeratorPlus.dataStorage.BanTypes;
import top.molab.minecraft.moModeratorPlus.message.KickMessageBuilder;
import top.molab.minecraft.moModeratorPlus.runtimeDataManage.RuntimeDataManager;

import java.util.Arrays;

public class PunishExecuteUtils {
    public static void ExecutePunish(BanTypes type, String playerName, String[] reason, long EffectiveTime, long ExpireTime, Player sender) {
        OfflinePlayer player = Bukkit.getPlayerExact(playerName);
        if (player != null) {

            if ((Arrays.asList(reason).contains(null) || reason.length == 0) && RuntimeDataManager.getInstance().getConfig().getBoolean(type.getType().toLowerCase() + ".force-reason")) {
                sender.sendMessage(ColorParser.parse("&a你必须输入封禁理由"));
                return;
            }
            if (!RuntimeDataManager.getInstance().getConfig().getBoolean(type.getType().toLowerCase() + ".enable")) {
                sender.sendMessage(ColorParser.parse("&a此命令尚未启用"));
                return;
            }
            if (!sender.hasPermission("moModPlus.admin." + type.getType().toLowerCase())) {
                sender.sendMessage(ColorParser.parse("&a你没有权限使用此命令"));
                return;
            }

            final BanStat banStat = new BanStat(
                    BanIDUtils.getNewBanIDByType(type),
                    type,
                    Arrays.asList(reason).contains(null) ?
                            RuntimeDataManager.getInstance().getConfig().getString("global.default-kick-message")
                            : String.join(" ", reason),
                    EffectiveTime,
                    ExpireTime,
                    player.isOnline() ? ((Player) player).getAddress().getHostName() : "",
                    playerName,
                    player.getUniqueId().toString(),
                    sender.getName(),
                    sender.getUniqueId().toString()
            );
            // 异步写入封禁
            new BukkitRunnable() {
                @Override
                public void run() {
                    RuntimeDataManager.getInstance().getLocalDataManager().addNewBanStat(banStat);
                }
            }.run();

            // 踢人
            if ((!type.equals(BanTypes.Mute)) && player.isOnline()) {
                ((Player) player).kickPlayer(new KickMessageBuilder().setTemplateByType(type).setBanStat(banStat).buildAsString(player));
            }
            // 踢BanIP，顺带发公告
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getAddress().getAddress().toString().equals(banStat.IP()) && type.equals(BanTypes.BanIP)) {
                    p.kickPlayer(new KickMessageBuilder().setTemplateByType(type).setBanStat(banStat).buildAsString(p));
                } else if (RuntimeDataManager.getInstance().getConfig().getBoolean(banStat.BanType().getType() + ".announce.enable")) {
                    // 发公告
                    p.sendMessage(new KickMessageBuilder()
                            .setMessageTemplate(
                                    RuntimeDataManager.getInstance().getConfig().getStringList(banStat.BanType().getType() + ".announce.message")
                            )
                            .setBanStat(banStat)
                            .buildAsString(player)
                    );
                }
            }

            // 告知封禁结果
            sender.sendMessage(ColorParser.parse("&(#66ccff)已经" + banStat.BanType().toString() + "玩家：" + banStat.PlayerName()));
        } else {
            sender.sendMessage(ColorParser.parse("&(#66ccff)玩家：" + playerName + "不在线，无法" + type.toString()));
        }
    }

    public static void DeletePunish(String banInfo, Player sender) {
        BanStat banstat = PlayerUtils.getBanStat(banInfo);
        if (banstat != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    RuntimeDataManager.getInstance().getLocalDataManager().deleteBanStat(banstat);
                }
            }.run();
        } else {
            sender.sendMessage(ColorParser.parse("&(#66ccff)封禁数据：" + banInfo + "不存在，无法删除"));
            return;
        }
        sender.sendMessage(ColorParser.parse("&(#66ccff)已经删除封禁：" + banInfo));
    }
}
