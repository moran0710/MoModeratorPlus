package top.molab.minecraft.moModeratorPlus.dataStorage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import top.molab.minecraft.moModeratorPlus.MoModeratorPlus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/*
 * 使用.yml文件来管理数据
 */
public class FileDataManager implements ILocalDataManager {
    public final String ROOT = "BanList";
    public final String VERSION_PATH = "Version";
    public final String BANID_PATH = "BanID";
    public final String REASON_PATH = "Reason";
    public final String BAN_TYPE_PATH = "BanType";
    public final String EFFECTIVE_TIME_PATH = "EffectiveTime";
    public final String EXPIRE_TIME_PATH = "ExpireTime";
    public final String PLAYER_NAME_PATH = "PlayerName";
    public final String IP_PATH = "IP";
    public final String UUID_PATH = "UUID";
    public final String OPERATOR_PATH = "Operator";
    public final String OPERATOR_UUID_PATH = "OperatorUUID";
    private final File FILE = new File(MoModeratorPlus.instance.getDataFolder(), "storage" + File.separator + "banList.yml");
    private FileConfiguration data;


    public FileDataManager() {
        try {
            if (!FILE.exists()) {
                //noinspection ResultOfMethodCallIgnored
                FILE.getParentFile().mkdirs();
                FileConfiguration newFile = new YamlConfiguration();
                newFile.set(ROOT, new ArrayList<Map<String, Object>>());
                newFile.set(VERSION_PATH, VERSION);
                newFile.save(FILE);
            }
            this.data = YamlConfiguration.loadConfiguration(FILE);
            if (!this.data.getString(VERSION_PATH).equals(VERSION)) {
                MoModeratorPlus.instance.getLogger().warning("Database Version is not " + VERSION + ", Please Check Your Database");
                // TODO: 如果有版本更新，要在这里加转换工具，但是目前没有版本更新，所以不写
                throw new RuntimeException("Database Version is not " + VERSION + ", Please Check Your Database");
            }
            MoModeratorPlus.instance.getLogger().info("Enable FileDataManger Successfully");

        } catch (Exception e) {
            MoModeratorPlus.instance.getLogger().log(Level.SEVERE, "Enable FileDataManger Failed", e);
        }
    }

    @Override
    public boolean hasBannedPlayer(String PlayerName) {
        List<Map<?, ?>> banList = data.getMapList(ROOT);
        for (Map<?, ?> ban : banList) {
            if (ban.get(PLAYER_NAME_PATH).equals(PlayerName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasBanID(String BanID) {
        List<Map<?, ?>> banList = data.getMapList(ROOT);
        for (Map<?, ?> ban : banList) {
            if (ban.get(BANID_PATH).equals(BanID)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasBannedIP(String IP) {
        List<Map<?, ?>> banList = data.getMapList(ROOT);
        for (Map<?, ?> ban : banList) {
            if (ban.get(IP_PATH).equals(IP)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasBannedUUID(String BanID) {
        List<Map<?, ?>> banList = data.getMapList(ROOT);
        for (Map<?, ?> ban : banList) {
            if (ban.get(UUID_PATH).equals(BanID)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public BanStat getBanStatFromPlayerName(String PlayerName) {
        List<Map<?, ?>> banList = data.getMapList(ROOT);
        for (Map<?, ?> ban : banList) {
            if (ban.get(PLAYER_NAME_PATH).equals(PlayerName)) {
                return MakeBanStatFromMap(ban);
            }
        }
        return null;
    }


    @Override
    public BanStat getBanStatFromBanID(String BanID) {
        List<Map<?, ?>> banList = data.getMapList(ROOT);
        for (Map<?, ?> ban : banList) {
            if (ban.get(BANID_PATH).equals(BanID)) {
                return MakeBanStatFromMap(ban);
            }
        }
        return null;
    }

    @Override
    public BanStat getBanStatFromIP(String IP) {
        List<Map<?, ?>> banList = data.getMapList(ROOT);
        for (Map<?, ?> ban : banList) {
            if (ban.get(IP_PATH).equals(IP)) {
                return MakeBanStatFromMap(ban);
            }
        }
        return null;
    }

    @Override
    public BanStat getBanStatFromUUID(String UUID) {
        List<Map<?, ?>> banList = data.getMapList(ROOT);
        for (Map<?, ?> ban : banList) {
            if (ban.get(UUID_PATH).equals(UUID)) {
                return MakeBanStatFromMap(ban);
            }
        }
        return null;
    }

    @Override
    public synchronized void addNewBanStat(BanStat banStat) {
        Map<String, Object> ban = new HashMap<>();
        ban.put(BANID_PATH, banStat.BanID());
        ban.put(REASON_PATH, banStat.Reason());
        ban.put(BAN_TYPE_PATH, banStat.BanType().name());
        ban.put(EFFECTIVE_TIME_PATH, banStat.EffectiveTime());
        ban.put(EXPIRE_TIME_PATH, banStat.ExpireTime());
        ban.put(IP_PATH, banStat.IP());
        ban.put(PLAYER_NAME_PATH, banStat.PlayerName());
        ban.put(UUID_PATH, banStat.UUID());
        ban.put(OPERATOR_PATH, banStat.Operator());
        ban.put(OPERATOR_UUID_PATH, banStat.OperatorUUID());

        List<Map<?, ?>> lst = data.getMapList(ROOT);
        lst.add(ban);
        data.set(ROOT, lst);

        saveData();
    }

    @Override
    public synchronized void updateBanStat(BanStat banStat) {
        this.addNewBanStat(banStat);
    }

    @Override
    public synchronized void deleteBanStat(BanStat banStat) {
        Map<?, ?> correctBan = null;
        List<Map<?, ?>> banList = data.getMapList(ROOT);
        for (Map<?, ?> ban : banList) {
            if (ban.get(UUID_PATH).equals(banStat.UUID())) {
                correctBan = ban;
                break;
            }
        }
        if (correctBan != null) {
            banList.remove(correctBan);
            data.set(ROOT, banList);
            saveData();
        }
    }

    private BanStat MakeBanStatFromMap(Map<?, ?> ban) {
        return new BanStat(
                (String) ban.get(BANID_PATH),
                BanTypes.getTypeByString(((String) ban.get(BAN_TYPE_PATH)).toLowerCase()),
                (String) ban.get(REASON_PATH),
                ((Number) ban.get(EFFECTIVE_TIME_PATH)).longValue(),
                ((Number) ban.get(EXPIRE_TIME_PATH)).longValue(),
                (String) ban.get(IP_PATH),
                (String) ban.get(PLAYER_NAME_PATH),
                (String) ban.get(UUID_PATH),
                (String) ban.get(OPERATOR_PATH),
                (String) ban.get(OPERATOR_UUID_PATH)
        );
    }

    private synchronized void saveData() {
        try {
            data.save(FILE);
            MoModeratorPlus.instance.getLogger().info("Saved ban list successfully");
        } catch (IOException e) {
            MoModeratorPlus.instance.getLogger().log(Level.SEVERE, "Failed to save ban list", e);
        }
    }

    @Override
    public List<String> getAllUUID() {
        List<String> uuids = new ArrayList<>();
        List<Map<?, ?>> banList = data.getMapList(ROOT);
        for (Map<?, ?> ban : banList) {
            uuids.add((String) ban.get(UUID_PATH));
        }
        return uuids;
    }

    @Override
    public List<String> getAllPlayerName() {
        List<String> playerNames = new ArrayList<>();
        List<Map<?, ?>> banList = data.getMapList(ROOT);
        for (Map<?, ?> ban : banList) {
            playerNames.add((String) ban.get(PLAYER_NAME_PATH));
        }
        return playerNames;
    }

    @Override
    public List<String> getAllBanID() {
        List<String> banIDs = new ArrayList<>();
        List<Map<?, ?>> banList = data.getMapList(ROOT);
        for (Map<?, ?> ban : banList) {
            banIDs.add((String) ban.get(BANID_PATH));
        }
        return banIDs;
    }
}

