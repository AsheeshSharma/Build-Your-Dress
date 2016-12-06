package com.animelabs.asheeshsharma.dressupapp.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.animelabs.asheeshsharma.dressupapp.R;

/**
 * Created by Asheesh.Sharma on 05-12-2016.
 */
public class SharedPref {
    public final static String SAMPLES_SAVED = "d";
    public final static String SPLASH_SHOWN = "f";
    public  static void setAddedDrawbles(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_saved), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(SAMPLES_SAVED, 1);
        editor.commit();
    }

    public static int getAddedDrawables(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_saved), context.MODE_PRIVATE);
        return sharedPref.getInt(SAMPLES_SAVED,0);
    }
    public  static void setSplashShown(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_splash), context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(SPLASH_SHOWN, 1);
        editor.commit();
    }

    public static int getSplashShown(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_splash), context.MODE_PRIVATE);
        return sharedPref.getInt(SPLASH_SHOWN,0);
    }
}
