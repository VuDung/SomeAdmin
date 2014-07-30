package com.some.admin.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

public class NetworkUtils {
	
	private static final String TAG = NetworkUtils.class.getSimpleName();
	public static boolean isConnected(Context context) {
			
		ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

		//For 3G check
		boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
		            .isConnectedOrConnecting();
		//For WiFi Check
		boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
		            .isConnectedOrConnecting();

		if (!is3g && !isWifi) 
		{ 
			Log.e(TAG, "Network not available");
			return false;
		}
		Log.e(TAG, "Network available");
		return true;
		}
}
