package top.molab.minecraft.moModeratorPlus;

import cc.carm.lib.easyplugin.utils.ColorParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.MessageFormat;

public final class MoModeratorPlus extends JavaPlugin {

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

        getLogger().info("MoModPlus Enabled!");
        getLogger().info("");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("MoModeratorPlus Disabled, Bye!");
    }
}
