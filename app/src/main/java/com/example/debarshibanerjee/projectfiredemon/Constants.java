package com.example.debarshibanerjee.projectfiredemon;

/**
 * Created by debarshibanerjee on 28/09/17.
 */

public class Constants {
    // BASE NAMESPACE used in Content provider contracts
    public static final String BASE_NAMESPACE = "com.example.debarshibanerjee.projectfiredemon";

    // We will have a cache of 10MB
    public static final long CACHE_SIZE = 10 * 1024 * 1024;

    public static final String KOLKATA_LATITUDE = "22.512296";
    public static final String KOLKATA_LONGITUDE = "88.3249813";

    public static final long FUSED_LOCATION_MAX_WAIT_TIME = 20 * 1000;

    public static final int SENTINEL_MEETUP_ID = -2;

    public static final int LOCATION_REQUEST_CODE = 1001;
    public static final int CONTACTS_REQUEST_CODE = 1002;
    public static final int LOCATION_SETTINGS_REQUEST_CODE = 1003;

    public static final int VALID_NAME_LENGTH = 24;

    public static final float REVERSE_GEOCODING_THRESHOLD_METRES = 5.0f;
    public static final float USER_REACHED_VENUE_THRESHOLD_METRES = 50.0f;
    public static final float LOCATION_UPDATE_THRESHOLD_METRES = 10.0f;

    public static final int PROFILE_PIC_WIDTH = 512;
    public static final int PROFILE_PIC_HEIGHT = 512;

    public static final String CHAT_BOT_MEMBER_ID = "0";

    public static final long AD_REQUEST_TIMEOUT_MSEC = 180 * 1000;

    public static final long FINISH_MEETUP_ALARM_INTERVAL_MSEC = 6 * 3600 * 1000;

    public static final long HOUR_IN_MSECS = 3600 * 1000;

    public static final long DEFAULT_MEETUP_DURATION_IN_MSECS = 4 * HOUR_IN_MSECS;

}
