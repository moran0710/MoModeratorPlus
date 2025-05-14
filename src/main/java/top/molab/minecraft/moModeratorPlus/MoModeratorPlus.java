package top.molab.minecraft.moModeratorPlus;

import cc.carm.lib.easyplugin.utils.ColorParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import top.molab.minecraft.moModeratorPlus.commands.MainCommandHandler;
import top.molab.minecraft.moModeratorPlus.commands.OverrideCommandTapCompleter;
import top.molab.minecraft.moModeratorPlus.handler.MuteMessageCancel;
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
            Objects.requireNonNull(Bukkit.getPluginCommand("MoModeratorPlus")).setTabCompleter(new MainCommandHandler());
        }
        // 复写原版Ban命令等
        if (RuntimeDataManager.getInstance().getConfig().getBoolean("ban.override")) {
            Objects.requireNonNull(Bukkit.getPluginCommand("ban")).setExecutor(new MainCommandHandler());
            Objects.requireNonNull(Bukkit.getPluginCommand("ban")).setTabCompleter(new OverrideCommandTapCompleter());
        }
        if (RuntimeDataManager.getInstance().getConfig().getBoolean("kick.override")) {
            Objects.requireNonNull(Bukkit.getPluginCommand("kick")).setExecutor(new MainCommandHandler());
            Objects.requireNonNull(Bukkit.getPluginCommand("kick")).setTabCompleter(new OverrideCommandTapCompleter());
        }
        if (RuntimeDataManager.getInstance().getConfig().getBoolean("mute.override")) {
            Objects.requireNonNull(Bukkit.getPluginCommand("mute")).setExecutor(new MainCommandHandler());
            Objects.requireNonNull(Bukkit.getPluginCommand("mute")).setTabCompleter(new OverrideCommandTapCompleter());
        }
        if (RuntimeDataManager.getInstance().getConfig().getBoolean("banip.override")) {
            Objects.requireNonNull(Bukkit.getPluginCommand("banip")).setExecutor(new MainCommandHandler());
            Objects.requireNonNull(Bukkit.getPluginCommand("banip")).setTabCompleter(new OverrideCommandTapCompleter());
        }
        if (RuntimeDataManager.getInstance().getConfig().getBoolean("unban.override")) {
            Objects.requireNonNull(Bukkit.getPluginCommand("unban")).setExecutor(new MainCommandHandler());
            Objects.requireNonNull(Bukkit.getPluginCommand("unban")).setTabCompleter(new OverrideCommandTapCompleter());
        }

        Bukkit.getPluginManager().registerEvents(new PlayerJoinCancel(), this);
        Bukkit.getPluginManager().registerEvents(new MuteMessageCancel(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("MoModeratorPlus Disabled, Bye!");
    }
}
