package top.molab.minecraft.moModeratorPlus.message;

import cc.carm.lib.easyplugin.utils.ColorParser;
import me.clip.placeholderapi.PlaceholderAPI;
import org.apache.commons.text.StringSubstitutor;
import org.bukkit.entity.Player;
import top.molab.minecraft.moModeratorPlus.dataStorage.BanStat;
import top.molab.minecraft.moModeratorPlus.dataStorage.BanTypes;
import top.molab.minecraft.moModeratorPlus.runtimeDataManage.RuntimeDataManager;
import top.molab.minecraft.moModeratorPlus.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KickMessageBuilder {

    private List<String> messageTemplate;
    private Map<String, String> values;


    public KickMessageBuilder() {
    }

    public KickMessageBuilder setBanStat(BanStat banStat) {
        values = new HashMap<>();
        values.put("BanID", banStat.BanID());
        values.put("EffectiveTime", TimeUtils.FormatTimeStampToStringDate(banStat.EffectiveTime()));
        values.put("ExpireTime", TimeUtils.FormatTimeStampToStringDate(banStat.ExpireTime()));
        values.put("Reason", banStat.Reason());
        values.put("Operator", banStat.Operator());
        values.put("Duration", TimeUtils.FormatSecondsCustom(banStat.getDuration()));
        return this;
    }

    public KickMessageBuilder setMessageTemplate(List<String> messageTemplate) {
        this.messageTemplate = messageTemplate;
        return this;
    }

    public KickMessageBuilder useBanMessageTemplate() {
        messageTemplate = RuntimeDataManager.getInstance().getConfig().getStringList("ban.message");
        return this;
    }

    public KickMessageBuilder useBanIPMessageTemplate() {
        messageTemplate = RuntimeDataManager.getInstance().getConfig().getStringList("banip.message");
        return this;
    }

    public KickMessageBuilder useKickMessageTemplate() {
        messageTemplate = RuntimeDataManager.getInstance().getConfig().getStringList("kick.message");
        return this;
    }

    public KickMessageBuilder useMuteMessageTemplate() {
        messageTemplate = RuntimeDataManager.getInstance().getConfig().getStringList("mute.message");
        return this;
    }

    public KickMessageBuilder setTemplateByType(BanTypes type) {
        switch (type) {
            case Kick:
                useKickMessageTemplate();
                break;
            case Ban:
                useBanMessageTemplate();
                break;
            case BanIP:
                useBanIPMessageTemplate();
                break;
            case Mute:
                useMuteMessageTemplate();
                break;
            default:
                break;
        }
        return this;
    }

    public String buildAsString(Player playerToShow) {
        values.put("Player", playerToShow.getName());
        StringBuilder builder = new StringBuilder();
        for (String line : messageTemplate) {
            String messageTemplate = line;

            if (RuntimeDataManager.getInstance().isPapiEnabled()) {
                messageTemplate = PlaceholderAPI.setPlaceholders(playerToShow, messageTemplate);
            }
            String message = StringSubstitutor.replace(messageTemplate, values);
            builder.append(ColorParser.parse(message)).append("\n");
        }
        return builder.toString();
    }

    public List<String> buildAsStringArray(Player playerToShow) {
        values.put("Player", playerToShow.getName());
        List<String> messageList = new ArrayList<>();
        for (String line : messageTemplate) {
            String messageTemplate = line;

            if (RuntimeDataManager.getInstance().isPapiEnabled()) {
                messageTemplate = PlaceholderAPI.setPlaceholders(playerToShow, messageTemplate);
            }
            String message = StringSubstitutor.replace(messageTemplate, values);
            messageList.add(ColorParser.parse(message));
        }
        return messageList;
    }
}
