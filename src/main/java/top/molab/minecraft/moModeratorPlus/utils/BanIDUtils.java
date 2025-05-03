package top.molab.minecraft.moModeratorPlus.utils;

import top.molab.minecraft.moModeratorPlus.dataStorage.BanTypes;
import top.molab.minecraft.moModeratorPlus.runtimeDataManage.RuntimeDataManager;

import java.util.Random;

public class BanIDUtils {

    private static final String CHAR_SET = "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789";
    private static final Random rand = new Random();

    public static String getNewBanID() {
        while (true) {
            String BanID = RuntimeDataManager.getInstance().getConfig().getString("ban.ban-id-prefix") + generate6DigitCode();
            if (!RuntimeDataManager.getInstance().getLocalDataManager().hasBanID(BanID)) {
                return BanID;
            }
        }
    }

    public static String getNewBanIpID() {
        while (true) {
            String BanID = RuntimeDataManager.getInstance().getConfig().getString("banip.ban-id-prefix") + generate6DigitCode();
            if (!RuntimeDataManager.getInstance().getLocalDataManager().hasBanID(BanID)) {
                return BanID;
            }
        }
    }

    public static String getNewBanIDByType(BanTypes banTypes) {
        if (banTypes == null) {
            return getNewBanID();
        }
        return switch (banTypes) {
            case Kick -> getNewKickID();
            case BanIP -> getNewBanIpID();
            case Mute -> getNewMuteID();
            default -> getNewBanID();
        };
    }

    public static String getNewKickID() {
        return RuntimeDataManager.getInstance().getConfig().getString("kick.ban-id-prefix") + generate6DigitCode();
    }

    public static String getNewMuteID() {
        while (true) {
            String BanID = RuntimeDataManager.getInstance().getConfig().getString("mute.ban-id-prefix") + generate6DigitCode();
            if (!RuntimeDataManager.getInstance().getLocalDataManager().hasBanID(BanID)) {
                return BanID;
            }
        }
    }


    public static String generate6DigitCode() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(CHAR_SET.charAt(rand.nextInt(CHAR_SET.length())));
        }
        return sb.toString();
    }
}
