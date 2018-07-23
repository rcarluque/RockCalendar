package com.example.manue.rockcalendar;

public class MyLog {
    static final boolean DEBUG = true;

    public static void i(String tag, String string) {
        if (DEBUG) android.util.Log.i(tag, string);
    }

    public static void e(String tag, String string) {
        if (DEBUG) android.util.Log.e(tag, string);
    }

    public static void d(String tag, String string) {
        if (DEBUG) android.util.Log.d(tag, string);
    }

    public static void v(String tag, String string) {
        if (DEBUG) android.util.Log.v(tag, string);
    }

    public static void w(String tag, String string) {
        if (DEBUG) android.util.Log.w(tag, string);
    }
}
