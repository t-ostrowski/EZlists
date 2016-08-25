package com.ostro.ezlists.util;

import android.widget.Toast;

import com.ostro.ezlists.EZlistsApp;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 29/07/2016.
 */

public class ToastUtils {
    public static Toast toast;

    public static void longToast(String text) {
        checkToast();
        toast = new Toast(EZlistsApp.get());
        toast = Toast.makeText(EZlistsApp.get(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void shortToast(String text) {
        checkToast();
        toast = new Toast(EZlistsApp.get());
        toast = Toast.makeText(EZlistsApp.get(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    private static void checkToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
