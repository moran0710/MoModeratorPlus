package top.molab.minecraft.moModeratorPlus.runtimeDataManage;

import org.bukkit.configuration.file.FileConfiguration;
import top.molab.minecraft.moModeratorPlus.MoModeratorPlus;
import top.molab.minecraft.moModeratorPlus.dataStorage.FileDataManager;
import top.molab.minecraft.moModeratorPlus.dataStorage.ILocalDataManager;
import top.molab.minecraft.moModeratorPlus.dataStorage.MySQLDataManager;

import java.sql.SQLException;
import java.util.Objects;

public class RuntimeDataManager {
    private static volatile RuntimeDataManager instance;
    private FileConfiguration config;
    private ILocalDataManager localDataManager;
    private boolean papiEnabled;

    private RuntimeDataManager() {
    }

    public static RuntimeDataManager getInstance() {
        if (instance == null) {
            synchronized (RuntimeDataManager.class) {
                if (instance == null) {
                    instance = new RuntimeDataManager();
                }
            }
        }
        return instance;
    }

    public void init() {
        // 读取配置文件
        MoModeratorPlus.instance.reloadConfig();
        this.config = MoModeratorPlus.instance.getConfig();
        if (!Objects.equals(this.config.getString("version"), "1.0")) {
            MoModeratorPlus.instance.getLogger().warning("Config Version is not 1.0, Please Check Your Config");
            MoModeratorPlus.instance.saveConfig();
            MoModeratorPlus.instance.reloadConfig();
        }
        for (String item : new String[]{"ban.ban-id-prefix", "mute.ban-id-prefix", "kick.ban-id-prefix", "banip.ban-id-prefix"}) {
            if (Objects.requireNonNull(this.config.getString(item)).length() > 8) {
                MoModeratorPlus.instance.getLogger().warning("Config Item " + item + " is too long, Please Check Your Config");
                throw new RuntimeException("Prefix too long");
            }
        }

        // 准备数据管理器
        if (this.config.getBoolean("global.database.yaml.enable")) {
            localDataManager = new FileDataManager();
        } else if (this.config.getBoolean("global.database.mysql.enable")) {
            try {
                localDataManager = new MySQLDataManager();
            } catch (SQLException e) {
                MoModeratorPlus.instance.getLogger().warning("MySQL Database Error");
                throw new RuntimeException(e);
            }
        } else {
            MoModeratorPlus.instance.getLogger().info("No Database Enabled, Use YAML");
            localDataManager = new FileDataManager();
        }

        MoModeratorPlus.instance.getLogger().info("DataManager Init Successfully");
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public ILocalDataManager getLocalDataManager() {
        return localDataManager;
    }

    public boolean isPapiEnabled() {
        return papiEnabled;
    }

    public void setPapiEnabled(boolean papiEnabled) {
        this.papiEnabled = papiEnabled;
    }
}