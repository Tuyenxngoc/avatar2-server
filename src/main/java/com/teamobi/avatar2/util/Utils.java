package com.teamobi.avatar2.util;

import com.teamobi.avatar2.constant.CommonConstant;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * @author tuyen
 */
public class Utils {

    private static final Random RANDOM;
    private static final DateTimeFormatter DATE_TIME_FORMATTER;

    static {
        RANDOM = new Random();
        DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(CommonConstant.PATTERN_DATE_TIME);
    }

    /**
     * Generates a random integer within the specified range (inclusive).
     *
     * @param x1 the lower bound of the range (inclusive)
     * @param x2 the upper bound of the range (inclusive)
     * @return a random integer between {@code x1} and {@code x2} (inclusive)
     * @throws IllegalArgumentException if {@code x1} is greater than {@code x2}
     */
    public static int nextInt(int x1, int x2) {
        return x1 + RANDOM.nextInt(x2 - x1 + 1);
    }

    public static int nextInt(int max) {
        return RANDOM.nextInt(max);
    }

    public static int nextInt(int[] probabilities) {
        int sum = 0;
        for (int prob : probabilities) {
            sum += prob;
        }

        int randomNumber = RANDOM.nextInt(sum);

        int cumulativeSum = 0;
        for (int i = 0; i < probabilities.length; i++) {
            cumulativeSum += probabilities[i];
            if (randomNumber < cumulativeSum) {
                return i;
            }
        }
        return -1;
    }

    public static String getStringNumber(float num) {
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        format.setMinimumFractionDigits(0);
        format.setRoundingMode(RoundingMode.DOWN);
        if (num >= 1000000000) {
            return format.format((num / 1000000000)) + "b";
        } else if (num >= 1000000) {
            return format.format((num / 1000000)) + "m";
        } else if (num >= 1000) {
            return format.format((num / 1000)) + "k";
        } else {
            return String.valueOf((int) num);
        }
    }

    public static String getStringTimeBySecond(long s) {
        s = Math.abs(s);
        if (s >= 31104000) {
            return (s / 31104000) + " năm";
        } else if (s >= 2592000) {
            return (s / 2592000) + " tháng";
        } else if (s >= 604800) {
            return (s / 604800) + " tuần";
        } else if (s >= 86400) {
            return (s / 86400) + " ngày";
        } else if (s >= 3600) {
            return (s / 3600) + " giờ";
        } else if (s >= 60) {
            return (s / 60) + " phút";
        } else {
            return s + " giây";
        }
    }

    public static byte[] getFile(String url) {
        Path path = Paths.get(url);
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveFile(String url, byte[] data) {
        Path path = Paths.get(url);
        try {
            Files.createDirectories(path.getParent());
            try (FileOutputStream fos = new FileOutputStream(url)) {
                fos.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calculates the level progress percentage based on the current XP and required XP.
     *
     * @param currentXp  The current XP points of the player.
     * @param requiredXp The required XP points to reach the next level.
     * @return A byte value representing the level progress percentage.
     * If the required XP is 0, it returns 0.
     * If the calculated percentage is less than 0, it returns 0.
     * If the calculated percentage is greater than 100, it returns 100.
     * Otherwise, it returns the calculated percentage as a byte value.
     */
    public static byte calculateLevelPercent(float currentXp, float requiredXp) {
        if (requiredXp == 0) {
            return 0;
        }
        int percentage = (int) Math.floor((currentXp / requiredXp) * 100);
        if (percentage < 0) {
            return 0;
        }
        if (percentage > 100) {
            return 100;
        }
        return (byte) percentage;
    }

    /**
     * Formats the given LocalDateTime to a string using the specified pattern.
     *
     * @param dateTime the LocalDateTime to format
     * @return a formatted date-time string
     */
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    /**
     * Clamps the given value to the specified range [min, max].
     * If the value is less than the minimum, it returns the minimum.
     * If the value is greater than the maximum, it returns the maximum.
     * Otherwise, it returns the value itself.
     *
     * @param value the value to clamp
     * @param min   the minimum value of the range
     * @param max   the maximum value of the range
     * @return the clamped value
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

}
