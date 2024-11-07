package com.teamobi.avatar2.constant;

/**
 * @author tuyen
 */
public class GameConstants {
    private static final int MAX_INT_VALUE = 2_000_000_000;

    public static final int MAX_XU = MAX_INT_VALUE;
    public static final int MIN_XU = -MAX_INT_VALUE;
    public static final int MAX_LUONG = MAX_INT_VALUE;
    public static final int MIN_LUONG = -MAX_INT_VALUE;
    public static final int MAX_XP = MAX_INT_VALUE;
    public static final int MIN_XP = -MAX_INT_VALUE;

    public static final String RESOURCE_BASE_URL = "src/main/resources";
    public static final String RESOURCE_HD_PATH = RESOURCE_BASE_URL + "/hd";
    public static final String RESOURCE_MEDIUM_PATH = RESOURCE_BASE_URL + "/medium";
    public static final String RESOURCE_OBJECT_PATH = "%s/object/%d.png";
}
