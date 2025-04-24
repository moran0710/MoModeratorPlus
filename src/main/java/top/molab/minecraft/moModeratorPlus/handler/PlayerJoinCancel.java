package top.molab.minecraft.moModeratorPlus.handler;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import top.molab.minecraft.moModeratorPlus.dataStorage.BanStat;
import top.molab.minecraft.moModeratorPlus.message.KickMessageBuilder;
import top.molab.minecraft.moModeratorPlus.runtimeDataManage.RuntimeDataManager;
import top.molab.minecraft.moModeratorPlus.utils.TimeUtils;

import java.util.UUID;

public class PlayerJoinCancel implements Listener {

    @EventHandler
    public void onPlayerJoin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        boolean isBanned = RuntimeDataManager.getInstance().getLocalDataManager().hasBannedUUID(uuid.toString());

        if (isBanned) {
            BanStat banStat = RuntimeDataManager.getInstance().getLocalDataManager().getBanStatFromUUID(uuid.toString());
            if (banStat.ExpireTime() < TimeUtils.getTimeStamp()) {
                RuntimeDataManager.getInstance().getLocalDataManager().deleteBanStat(banStat);
                return;
            }
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, new KickMessageBuilder().setTemplateByType(banStat.BanType()).setBanStat(banStat).buildAsString(Bukkit.getPlayer(uuid)));
        }
    }
}
