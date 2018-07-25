package com.xujl.task;

import android.util.Log;

/**
 * Created by Administrator on 2018/4/1.
 */

class Logger {
    static boolean sIsDebug = true;

    public static void e (String tag, String msg) {
        if (sIsDebug) {
            Log.e(tag, msg);
        }
    }
    public static void e (String tag, String msg,Throwable throwable) {
        if (sIsDebug) {
            Log.e(tag, msg,throwable);
        }
    }
    public static void e (String tag, Exception e) {
        if (sIsDebug) {
            Log.e(tag, "", e);
        }
    }

    public static void d (String tag, String msg) {
        if (sIsDebug) {
            Log.d(tag, msg);
        }
    }
}
