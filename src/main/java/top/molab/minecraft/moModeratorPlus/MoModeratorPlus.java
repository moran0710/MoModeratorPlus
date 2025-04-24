package top.molab.minecraft.moModeratorPlus;

import cc.carm.lib.easyplugin.utils.ColorParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import top.molab.minecraft.moModeratorPlus.Commands.MainCommandHandler;
import top.molab.minecraft.moModeratorPlus.handler.PlayerJoinCancel;
import top.molab.minecraft.moModeratorPlus.runtimeDataManage.RuntimeDataManager;

import java.text.MessageFormat;
import java.util.Objects;

public final class MoModeratorPlus extends JavaPlugin {

    public static MoModeratorPlus instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ColorParser.parse("&(#66ccff)--------------------------------------------"));
        Bukkit.getConsoleSender().sendMessage(ColorParser.parse("&(#66ccff)| MoModeratorPlus   | OpenMoPlugin Project |"));
        Bukkit.getConsoleSender().sendMessage(ColorParser.parse("&(#66ccff)| Author: Moran0710 | By MoCStudio         |"));
        Bukkit.getConsoleSender().sendMessage(ColorParser.parse("&(#66ccff)| A Advanced Moderation Plugin             |"));
        Bukkit.getConsoleSender().sendMessage(ColorParser.parse("&(#66ccff)--------------------------------------------"));
        getLogger().info(MessageFormat.format("MoModeratorPlus, Version {0}", getDescription().getVersion()));
        getLogger().info("Author: Moran0710 | OpenMoPlugin Project");
        getLogger().info("");

        this.init();

        // PAPI依赖
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("Found PlaceholderAPI, Enable PAPI Functions");
            RuntimeDataManager.getInstance().setPapiEnabled(true);
        } else {
            getLogger().warning("PlaceholderAPI not found, PAPI Functions Disabled");
            RuntimeDataManager.getInstance().setPapiEnabled(false);
        }

        getLogger().info("MoModPlus Enabled!");
        getLogger().info("");
    }

    private void init() {
        saveDefaultConfig();
        instance = this;
        RuntimeDataManager.getInstance().init();
        if (Bukkit.getPluginCommand("MoModeratorPlus") != null) {
            Objects.requireNonNull(Bukkit.getPluginCommand("MoModeratorPlus")).setExecutor(new MainCommandHandler());
        }
        Bukkit.getPluginManager().registerEvents(new PlayerJoinCancel(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("MoModeratorPlus Disabled, Bye!");
    }
}
