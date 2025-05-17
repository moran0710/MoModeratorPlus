package top.molab.minecraft.moModeratorPlus.dataStorage;


public record BanStat(String BanID, BanTypes BanType, String Reason,
                      long EffectiveTime, long ExpireTime,
                      String IP, String PlayerName, String UUID,
                      String Operator, String OperatorUUID) {
    public long getDuration() {
        return ExpireTime - EffectiveTime;
    }

    @Override
    public String toString() {
        return "Execute Ban->" +
                "BannedPlayerName: " + PlayerName + " | " +
                "UUID: " + UUID + " | " +
                "BanID: " + BanID + " | " +
                "BanType: " + BanType + " | " +
                "Reason: " + Reason + " | " +
                "EffectiveTime: " + EffectiveTime + " | " +
                "ExpireTime: " + ExpireTime + " | " +
                "IP: " + IP + " | " +
                "Operator: " + Operator + " | " +
                "OperatorUUID: " + OperatorUUID;
    }
}
