package top.molab.minecraft.moModeratorPlus.dataStorage;

import java.util.List;

/**
 * 本地封禁数据管理接口，提供封禁状态查询和封禁信息获取功能
 */
public interface ILocalDataManager {

    /**
     * 通过玩家名称检查是否存在于封禁名单
     *
     * @param PlayerName 需要检查的玩家名称（大小写敏感）
     * @return 存在封禁记录返回true，否则返回false
     */
    public boolean hasBannedPlayer(String PlayerName);

    /**
     * 通过BanID检查是否存在于封禁名单
     *
     * @param BanID 需要检查的BanID
     * @return 存在封禁记录返回true，否则返回false
     */
    public boolean hasBanID(String BanID);

    /**
     * 通过IP地址检查是否存在于封禁名单
     *
     * @param IP 需要检查的IPv4/IPv6地址字符串
     * @return 存在封禁记录返回true，否则返回false
     */
    public boolean hasBannedIP(String IP);

    /**
     * 通过UUID检查是否存在于封禁名单
     *
     * @param UUID 需要检查的玩家唯一标识符
     * @return 存在封禁记录返回true，否则返回false
     */
    public boolean hasBannedUUID(String UUID);

    /**
     * 通过玩家名称获取完整的封禁状态信息
     *
     * @param PlayerName 需要查询的玩家名称（大小写敏感）
     * @return 包含封禁详细信息的BanStat对象，未找到时返回null
     */
    public BanStat getBanStatFromPlayerName(String PlayerName);

    /**
     * 通过BanID获取完整的封禁状态信息
     *
     * @param BanID 需要查询的BanID
     * @return 包含封禁详细信息的BanStat对象，未找到时返回null
     */
    public BanStat getBanStatFromBanID(String BanID);

    /**
     * 通过IP地址获取完整的封禁状态信息
     *
     * @param IP 需要查询的IPv4/IPv6地址字符串
     * @return 包含封禁详细信息的BanStat对象，未找到时返回null
     */
    public BanStat getBanStatFromIP(String IP);

    /**
     * 通过UUID获取完整的封禁状态信息
     *
     * @param UUID 需要查询的玩家唯一标识符
     * @return 包含封禁详细信息的BanStat对象，未找到时返回null
     */
    public BanStat getBanStatFromUUID(String UUID);

    /**
     * 增加封禁信息
     */
    public void addNewBanStat(BanStat banStat);

    /**
     * 更新封禁信息
     */
    public void updateBanStat(BanStat banStat);

    /**
     * 删除封禁信息
     */
    public void deleteBanStat(BanStat banStat);

    /**
     * 获取所有封禁UUID
     */
    public List<String> getAllUUID();

    /**
     * 获取所有封禁玩家名
     */
    public List<String> getAllPlayerName();

    /**
     * 获取所有封禁BanID
     */
    public List<String> getAllBanID();


}

