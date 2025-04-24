package top.molab.minecraft.moModeratorPlus.utils;

import top.molab.minecraft.moModeratorPlus.runtimeDataManage.RuntimeDataManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static long ParseStringToTimeStamp(String timeStr) {
        long totalSeconds = 0;
        int index = 0;
        while (index < timeStr.length()) {
            int numStart = index;
            while (index < timeStr.length() && Character.isDigit(timeStr.charAt(index))) {
                index++;
            }
            if (numStart == index) {
                throw new IllegalArgumentException("Invalid time format: " + timeStr);
            }
            int num = Integer.parseInt(timeStr.substring(numStart, index));
            if (index >= timeStr.length()) {
                throw new IllegalArgumentException("Invalid time format: " + timeStr);
            }
            char unit = timeStr.charAt(index);
            switch (unit) {
                case 'd':
                    totalSeconds += num * 86400L;
                    break;
                case 'h':
                    totalSeconds += num * 3600L;
                    break;
                case 'm':
                    totalSeconds += num * 60L;
                    break;
                case 's':
                    totalSeconds += num;
                    break;
                case 'y':
                    totalSeconds += num * 31536000L;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid time unit: " + unit);
            }
            index++;
        }
        return totalSeconds;
    }

    public static String FormatTimeStampToString(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(RuntimeDataManager.getInstance().getConfig().getString("global.time-format"));
        return sdf.format(new Date(timeStamp * 1000));
    }

    public static long getTimeStamp() {
        return System.currentTimeMillis() / 1000L;
    }


}