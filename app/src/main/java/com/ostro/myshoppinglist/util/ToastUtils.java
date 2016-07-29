package com.ostro.myshoppinglist.util;

import android.widget.Toast;

import com.ostro.myshoppinglist.MyShoppingListApp;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 29/07/2016.
 */

public class ToastUtils {
    public static Toast toast;

    public static void longToast(String text) {
        checkToast();
        toast = new Toast(MyShoppingListApp.get());
        toast = Toast.makeText(MyShoppingListApp.get(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void shortToast(String text) {
        checkToast();
        toast = new Toast(MyShoppingListApp.get());
        toast = Toast.makeText(MyShoppingListApp.get(), text, Toast.LENGTH_SHORT);
        toast.show();
    }

    private static void checkToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
