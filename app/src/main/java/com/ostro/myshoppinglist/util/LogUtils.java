package com.ostro.myshoppinglist.util;

import android.util.Log;

import com.ostro.myshoppinglist.BuildConfig;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 19/08/2016.
 */

public class LogUtils {

    private static final String LOG_PREFIX = "salti_";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;
    private static final boolean forceDebug = true;

    private LogUtils() {
    }

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - (LOG_PREFIX_LENGTH == 0 ? 0 : LOG_PREFIX_LENGTH - 1));
        }

        return LOG_PREFIX + str;
    }

    private static boolean canLog() {
        return BuildConfig.DEBUG || forceDebug;
    }

    @SuppressWarnings("unused")
    public static void LOGD(String tag, String message) {
        if (canLog()) {
            tag = makeLogTag(tag);
            Log.i(tag, message);
        }
    }

    @SuppressWarnings("unused")
    public static void LOGD(String tag, String message, Throwable cause) {
        if (canLog()) {
            tag = makeLogTag(tag);
            Log.i(tag, message, cause);
        }
    }

    @SuppressWarnings("unused")
    public static void LOGV(String tag, String message) {
        if (canLog()) {
            tag = makeLogTag(tag);
            Log.v(tag, message);
        }
    }

    @SuppressWarnings("unused")
    public static void LOGV(String tag, String message, Throwable cause) {
        if (canLog()) {
            tag = makeLogTag(tag);
            Log.v(tag, message, cause);
        }
    }

    @SuppressWarnings("unused")
    public static void LOGI(String tag, String message) {
        if (canLog()) {
            tag = makeLogTag(tag);
            Log.i(tag, message);
        }
    }

    @SuppressWarnings("unused")
    public static void LOGI(String tag, String message, Throwable cause) {
        if (canLog()) {
            tag = makeLogTag(tag);
            Log.i(tag, message, cause);
        }
    }

    @SuppressWarnings("unused")
    public static void LOGW(String tag, String message) {
        if (canLog()) {
            tag = makeLogTag(tag);
            Log.w(tag, message);
        }
    }

    @SuppressWarnings("unused")
    public static void LOGW(String tag, String message, Throwable cause) {
        if (canLog()) {
            tag = makeLogTag(tag);
            Log.w(tag, message, cause);
        }
    }

    @SuppressWarnings("unused")
    public static void LOGE(String tag, String message) {
        if (canLog()) {
            tag = makeLogTag(tag);
            Log.e(tag, message);
        }
    }

    @SuppressWarnings("unused")
    public static void LOGE(String tag, String message, Throwable cause) {
        if (canLog()) {
            tag = makeLogTag(tag);
            Log.e(tag, message, cause);
        }
    }

}
