package top.molab.minecraft.moModeratorPlus.handler;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import top.molab.minecraft.moModeratorPlus.dataStorage.BanStat;
import top.molab.minecraft.moModeratorPlus.dataStorage.BanTypes;
import top.molab.minecraft.moModeratorPlus.message.KickMessageBuilder;
import top.molab.minecraft.moModeratorPlus.runtimeDataManage.RuntimeDataManager;
import top.molab.minecraft.moModeratorPlus.utils.TimeUtils;

import java.util.List;

public class MuteMessageCancel implements Listener {

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        BanStat banStat = RuntimeDataManager.getInstance().getLocalDataManager().getBanStatFromUUID(event.getPlayer().getUniqueId().toString());
        if (banStat == null) {
            return;
        }
        if (banStat.BanType() != BanTypes.Mute) {
            return;
        }
        if (banStat.ExpireTime() < TimeUtils.getTimeStamp()) {
            RuntimeDataManager.getInstance().getLocalDataManager().deleteBanStat(banStat);
            return;
        }
        event.setCancelled(true);

        List<String> messgae = new KickMessageBuilder().setTemplateByType(banStat.BanType()).setBanStat(banStat).buildAsStringArray(event.getPlayer());
        for (String message : messgae) {
            event.getPlayer().sendMessage(message);
        }

    }
}
