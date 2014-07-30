package com.some.admin;

import java.util.List;

import covisoft.android.managebackgroundtask.api.API;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.Context;

public class MainApplication extends Application{

	private static MainApplication mInstance;
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		API.HOST_URL = "http://adminsome.com";
	}


	public static MainApplication getInstance(){
		if(mInstance == null){
			mInstance = new MainApplication();
		}
		return mInstance;
	}
	
	public boolean isServiceRunning(String serviceClassName){
        final ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        final List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(serviceClassName)){
                return true;
            }
        }
        return false;
     }
}
