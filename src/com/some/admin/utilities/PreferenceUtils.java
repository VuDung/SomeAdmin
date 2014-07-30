package com.some.admin.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

	public static boolean setPreference(Context context, String key, String value){
		SharedPreferences prefs = context.getSharedPreferences(Constains.PREFS_LOGIN_SOMEADMIN, 0);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		return editor.commit();
	}
	
	public static String getPreference(Context context, String key){
		SharedPreferences prefs = context.getSharedPreferences(Constains.PREFS_LOGIN_SOMEADMIN, 0);
		String value = prefs.getString(key, "");
		return value;
	}
}
