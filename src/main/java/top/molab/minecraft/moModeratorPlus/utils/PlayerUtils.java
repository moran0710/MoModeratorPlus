package top.molab.minecraft.moModeratorPlus.utils;

import top.molab.minecraft.moModeratorPlus.dataStorage.BanStat;
import top.molab.minecraft.moModeratorPlus.runtimeDataManage.RuntimeDataManager;

public class PlayerUtils {
    public static BanStat getBanStat(String value) {
        BanStat banStat = RuntimeDataManager.getInstance().getLocalDataManager().getBanStatFromPlayerName(value);
        if (banStat == null) {
            banStat = RuntimeDataManager.getInstance().getLocalDataManager().getBanStatFromBanID(value);
            if (banStat == null) {
                banStat = RuntimeDataManager.getInstance().getLocalDataManager().getBanStatFromUUID(value);
            }
        }
        return banStat;
    }

}
