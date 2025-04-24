package top.molab.minecraft.moModeratorPlus.Commands;

import cc.carm.lib.easyplugin.utils.ColorParser;
import org.bukkit.command.CommandSender;

public class HelpUtils {
    public static void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)=============================="));
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)MoModeratorPlus 用法："));
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)时间格式：<封禁年数>y<封禁天数>d<封禁小时数>h<封禁分钟数>m<封禁秒数>s"));
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)例如：1d2h3m4s：1天2小时3分钟4秒 2h3s 两小时三秒"));
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)/mm+ help：&l&7显示帮助"));
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)/mm+ mute <用户名> <时长> <原因>：&l&7禁言某人"));
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)/mm+ kick <用户名> <原因>：&l&7 踢出某人"));
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)/mm+ ban <用户名> <时长> <原因>：&l&7封禁某人"));
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)/mm+ unban <用户名>&l&7： 解封某人"));
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)/mm+ banip <IP> <时长> <原因>：&l&7封禁IP"));
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)/mm+ unbanip <IP>：&l&7解封IP"));
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)/mm+ unmute <用户名>：&l&7解禁某人"));
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)/mm+ banlist：&l&7 封禁列表"));
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)/mm+ search [user/banid] <参数>：&l&7查询对应的封禁"));
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)/mm+ reload：&l&7重载配置文件"));
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)Powered By OpenMoPlugins Project"));
        sender.sendMessage(ColorParser.parse("&l&(#66ccff)=============================="));
    }

}
