package me.ycdev.android.arch.wrapper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * A wrapper class to avoid security issues when sending/receiving broadcast.
 */
public class BroadcastHelper {
    private static final String PERM_COMMON_BROADCAST = ".permission.COMMON_BROADCAST";

    private BroadcastHelper() {
        // nothing to do
    }

    /**
     * Register a receiver both for internal broadcast or system broadcast.
     */
    public static Intent register(Context cxt, BroadcastReceiver receiver, IntentFilter filter) {
        String perm = cxt.getPackageName() + PERM_COMMON_BROADCAST;
        return cxt.registerReceiver(receiver, filter, perm, null);
    }

    /**
     * Send a broadcast to internal receivers.
     */
    public static void sendToInternal(Context cxt, Intent intent) {
        String perm = cxt.getPackageName() + PERM_COMMON_BROADCAST;
        intent.setPackage(cxt.getPackageName()); // only works on Android 4.0 and higher versions
        cxt.sendBroadcast(intent, perm);
    }

    /**
     * Send a broadcast to external receivers.
     */
    public static void sendToExternal(Context cxt, Intent intent) {
        cxt.sendBroadcast(intent);
    }
}
