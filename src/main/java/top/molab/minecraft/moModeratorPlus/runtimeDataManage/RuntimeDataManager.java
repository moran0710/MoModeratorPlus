package top.molab.minecraft.moModeratorPlus.runtimeDataManage;

import org.bukkit.configuration.file.FileConfiguration;
import top.molab.minecraft.moModeratorPlus.MoModeratorPlus;
import top.molab.minecraft.moModeratorPlus.dataStorage.FileDataManager;
import top.molab.minecraft.moModeratorPlus.dataStorage.ILocalDataManager;

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

        // 准备数据管理器
        if (this.config.getBoolean("global.database.yaml.enable")) {
            localDataManager = new FileDataManager();
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

    public void ReloadConfig() {
        config = MoModeratorPlus.instance.getConfig();
    }

    public boolean isPapiEnabled() {
        return papiEnabled;
    }

    public void setPapiEnabled(boolean papiEnabled) {
        this.papiEnabled = papiEnabled;
    }
}