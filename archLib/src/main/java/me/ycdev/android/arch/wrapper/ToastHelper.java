package me.ycdev.android.arch.wrapper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * A wrapper class for Toast so that we can customize and unify the UI in future.
 */
@SuppressWarnings("unused")
public class ToastHelper {
    private ToastHelper() {
        // nothing to do
    }

    private static void checkDuration(int duration) {
        if (duration != Toast.LENGTH_SHORT && duration != Toast.LENGTH_LONG) {
            throw new IllegalArgumentException("Invalid parameter 'duration: " + duration);
        }
    }

    public static void show(@NonNull Context cxt, @StringRes int msgResId, int duration) {
        checkDuration(duration);
        Toast.makeText(cxt, msgResId, duration).show();
    }

    public static void show(@NonNull Context cxt, @NonNull CharSequence msg, int duration) {
        checkDuration(duration);
        Toast.makeText(cxt, msg, duration).show();
    }

}
