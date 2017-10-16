package com.example.shop.util;
import android.content.Context;
import android.content.SharedPreferences;
/**
 * Created by Mr.yang on 12/7/2016.
 */



public class SharedPreferenceHelper {
    public static SharedPreferences preferences;

    //获得cookie
    public static String getCookie(Context context) {
        preferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        return preferences.getString("cookie", "");
    }


    //存储或更改cookie
    public static void saveCookie(Context context, String s) {
        preferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cookie", s);
        editor.apply();
    }

    //清空数据库
    public static void clearCookie(Context context) {
        preferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
