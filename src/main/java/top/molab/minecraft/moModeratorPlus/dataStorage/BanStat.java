package top.molab.minecraft.moModeratorPlus.dataStorage;


public record BanStat(String BanID, BanTypes BanType, String Reason,
                      long EffectiveTime, long ExpireTime,
                      String IP, String PlayerName, String UUID,
                      String Operator, String OperatorUUID) {
    public long getDuration() {
        return ExpireTime - EffectiveTime;
    }
}
