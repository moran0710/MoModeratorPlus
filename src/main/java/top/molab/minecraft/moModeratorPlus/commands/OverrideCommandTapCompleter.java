package top.molab.minecraft.moModeratorPlus.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.molab.minecraft.moModeratorPlus.runtimeDataManage.RuntimeDataManager;

import java.util.List;

public class OverrideCommandTapCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (args.length) {
            case 1:
                if (label.equalsIgnoreCase("unban")) {
                    return RuntimeDataManager.getInstance().getLocalDataManager().getAllPlayerName();
                }
                return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            case 2:
                if (label.equalsIgnoreCase("kick")) {
                    return List.of("<Reason>");
                }
                return List.of("<BanTime>");
            default:
                return List.of("<Reason>");
        }
    }
}
