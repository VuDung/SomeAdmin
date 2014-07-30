package com.some.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.some.admin.MainActivity;
import com.some.admin.R;
import com.some.admin.model.Bound;
import com.some.admin.model.Online;
import com.some.admin.model.Register;
import com.some.admin.parser.DataParser;
import com.some.admin.utilities.Constains;
import com.some.admin.utilities.PreferenceUtils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SMService extends Service{
	private static final String TAG = SMService.class.getSimpleName();
	
	public static List<Online> mListOnline = new ArrayList<Online>();
	public static List<Bound> mListInbound = new ArrayList<Bound>();
	public static List<Bound> mListOutbound = new ArrayList<Bound>();
	public static List<Register> mListRegister = new ArrayList<Register>();
	
	private List<Online> mTempListOnline = new ArrayList<Online>();
	private List<Bound> mTempListInbound = new ArrayList<Bound>();
	private List<Bound> mTempListOutbound = new ArrayList<Bound>();
	private List<Register> mTempListRegister = new ArrayList<Register>();
	
	private Thread mThreadInService;
	
	private IBinder mBinder = new LocalBinder();
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	public class LocalBinder extends Binder{
		public SMService getServerInstance(){
			return SMService.this;
		}
	}
	
	private static String mUserLogin = "";
	
	/*
	 * Push variable declare
	 */
	
	private NotificationManager mNotificationManager;
	private Timer mTimerService;
	private	final int UPDATE_TIME_PERIOD = 500;
	
	private Bundle mBundleExtras;
	
	/*
	 * Variable popup push
	 */
	
	private WindowManager mWindowManager;
	private WindowManager.LayoutParams mLayoutParams;
	public View mPopupView;
	
	private LinearLayout llBtnView;
	private ImageView imgBtnClose;
	private TextView mTextPush;
	
	private static String mText = "";
	private Handler mHandler;
	
	@SuppressLint("HandlerLeak") 
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		
		mTimerService = new Timer();
		
		mWindowManager = (WindowManager)getSystemService(WINDOW_SERVICE);
		mPopupView = View.inflate(getBaseContext(), R.layout.layout_push_popup, null);
		
		
		imgBtnClose = (ImageView)mPopupView.findViewById(R.id.imgBtnClose);
		llBtnView = (LinearLayout)mPopupView.findViewById(R.id.llBtnView);
		mTextPush = (TextView)mPopupView.findViewById(R.id.tvPush);
		
		imgBtnClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mPopupView != null && mPopupView.isShown()){
					mWindowManager.removeView(mPopupView);
					mPopupView = null;
				}
			}
		});
		
		llBtnView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mPopupView != null && mPopupView.isShown()){
					mWindowManager.removeView(mPopupView);
					mPopupView = null;
				}
				
				Intent iApp = new Intent(getApplicationContext(), MainActivity.class);
				iApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				iApp.putExtras(mBundleExtras);
				startActivity(iApp);
			}
		});
		
		mLayoutParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
		 	    WindowManager.LayoutParams.MATCH_PARENT,
		 	    WindowManager.LayoutParams.TYPE_PHONE,
		 	    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
		 	    PixelFormat.TRANSLUCENT);
		mLayoutParams.gravity = Gravity.CENTER;
		mLayoutParams.x = 0;
		mLayoutParams.y = mPopupView.getHeight() / 2;
		
		mHandler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				try {
					mTextPush.setText(mText);
					mWindowManager.addView(mPopupView, mLayoutParams);
				} catch (Exception e) {
					if(mPopupView != null){
						mWindowManager.removeView(mPopupView);
						mWindowManager.addView(mPopupView, mLayoutParams);
					}
				}
			}
			
		};
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mUserLogin = PreferenceUtils.getPreference(getApplicationContext(), Constains.PREFS_USERNAME);
		if(mUserLogin != "" && mUserLogin.length() > 0){
			Log.e(TAG, "Service start");
			mThreadInService = new Thread(){

				@Override
				public void run() {
					super.run();
					checkUserOnline();
					checkInbound();
					checkOutbound();
					checkNewRegister();
				}
			};
			mThreadInService.start();
		}
		return START_STICKY;
	}
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mTimerService.cancel();
	}

	
	private void checkUserOnline(){
		
		final String urlOnGameUser = "http://adminsome.com/onGameuser.html?username=" + mUserLogin;
		final DataParser parser = new DataParser();
		
		String xml = parser.getXmlFromUrl(urlOnGameUser);		
		mListOnline = parser.parserOnline(xml);
		Log.e(TAG, "List online size: " + mListOnline.size());
		
		mTimerService.schedule(new TimerTask() {			
			@Override
			public void run() {
				String xmlTemp = parser.getXmlFromUrl(urlOnGameUser);
				mTempListOnline = parser.parserOnline(xmlTemp);
				Log.e(TAG, "Temp list online size: " + mTempListOnline.size());
				if(mTempListOnline.size() > 0 ){
					if(mListOnline.size() > 0){
						List<Online> itemDiff = new ArrayList<Online>();
						itemDiff.addAll(mTempListOnline);
						
						for(int i = 0; i < mTempListOnline.size(); i++){
							for(int j = 0; j < mListOnline.size(); j++){
								//method equals(object)
								if(mTempListOnline.get(i).equals(mListOnline.get(j))){
									itemDiff.remove(mTempListOnline.get(i));
								}
							}
						}
						Log.e(TAG, "List Online different size: " + itemDiff.size());
						if(itemDiff.size() > 0){
							showNotification(itemDiff, "online");
						}
						mListOnline = mTempListOnline;
						Log.e(TAG, "List online | Temp size after push: " + mListOnline.size() + "|" + mTempListOnline.size());
					
					}else if (mListOnline != mTempListOnline) {
						showNotification(mTempListOnline, "online");
						mListOnline = mTempListOnline;
					}
				}
			}
		}, UPDATE_TIME_PERIOD, UPDATE_TIME_PERIOD);
	}
	private void checkInbound(){
		final String urlInbound = "http://adminsome.com/inboundMoney.html?username=" + mUserLogin;
		final DataParser parser = new DataParser();
		String xml = parser.getXmlFromUrl(urlInbound);
		mListInbound = parser.parserBound(xml);
		
		mTimerService.schedule(new TimerTask() {
			
			@Override
			public void run() {
				String xmlTemp = parser.getXmlFromUrl(urlInbound);
				mTempListInbound = parser.parserBound(xmlTemp);
				if(mTempListInbound.size() > 0){
					if(mListInbound.size() > 0){
						List<Bound> itemDiff = new ArrayList<Bound>(); 
						itemDiff.addAll(mTempListInbound);
						
						for(int i = 0; i < mTempListInbound.size(); i++){
							for(int j = 0; j < mListInbound.size(); j++){
								//method equals(object)
								if(mTempListInbound.get(i).equals(mListInbound.get(j))){
									itemDiff.remove(mTempListInbound.get(i));
								}
							}
						}
						Log.e(TAG, "List Inbound different size: " + itemDiff.size());
						if(itemDiff.size() > 0){
							showNotification(itemDiff, "inbound");
						}
						mListInbound = mTempListInbound;
						Log.e(TAG, "List inbound | Temp size after push: " + mListInbound.size() + "|" + mTempListInbound.size());
					}
					else{
						showNotification(mTempListInbound, "inbound");
						mListInbound = mTempListInbound;
					}
					
				}
			
			}
		}, UPDATE_TIME_PERIOD, UPDATE_TIME_PERIOD);
	}
	private void checkOutbound(){
		final String urlOutbound = "http://adminsome.com/outboundMoney.html?username=" + mUserLogin;
		final DataParser parser = new DataParser();
		String xml = parser.getXmlFromUrl(urlOutbound);
		mListOutbound = parser.parserBound(xml);
		mTimerService.schedule(new TimerTask() {
			
			@Override
			public void run() {
				String xmlTemp = parser.getXmlFromUrl(urlOutbound);
				mTempListOutbound = parser.parserBound(xmlTemp);
				if(mTempListOutbound.size() > 0){
					if(mListOutbound.size() > 0){
						List<Bound> itemDiff = new ArrayList<Bound>(); 
						itemDiff.addAll(mTempListOutbound);
						for(int i = 0; i < mTempListOutbound.size(); i++){
							for(int j = 0; j < mListOutbound.size(); j++){
								//method equals(object)
								if(mTempListOutbound.get(i).equals(mListOutbound.get(j))){
									itemDiff.remove(mTempListOutbound.get(i));
								}
							}
						}
						Log.e(TAG, "List Outbound different size: " + itemDiff.size());
						if(itemDiff.size() > 0){
							showNotification(itemDiff, "outbound");
						}
						mListOutbound = mTempListOutbound;
						Log.e(TAG, "List Outbound | Temp size after push: " + mListOutbound.size() + "|" + mTempListOutbound.size());
					}
					else{
						showNotification(mTempListOutbound, "outbound");
						mListOutbound = mTempListOutbound;
					}
					
				}
			}
		}, UPDATE_TIME_PERIOD, UPDATE_TIME_PERIOD);
	}
	private void checkNewRegister(){
		final String urlRegister = "http://adminsome.com/getNewuser.html?username=" + mUserLogin;
		final DataParser parser = new DataParser();
		String xml = parser.getXmlFromUrl(urlRegister);
		mListRegister = parser.parserRegister(xml);
		mTimerService.schedule(new TimerTask() {
			
			@Override
			public void run() {
				String xmlTemp = parser.getXmlFromUrl(urlRegister);
				mTempListRegister = parser.parserRegister(xmlTemp);
				if(mTempListRegister.size() > 0){
					if(mListRegister.size() > 0){
						List<Register> itemDiff = new ArrayList<Register>(); 
						itemDiff.addAll(mTempListRegister);
						for(int i = 0; i < mTempListRegister.size(); i++){
							for(int j = 0; j < mListRegister.size(); j++){
								//method equals(object)
								if(mTempListRegister.get(i).equals(mListRegister.get(j))){
									itemDiff.remove(mTempListRegister.get(i));
								}
							}
						}
						Log.e(TAG, "List Register different size: " + itemDiff.size());
						if(itemDiff.size() > 0){
							showNotification(itemDiff, "register");
						}
						mListRegister = mTempListRegister;
						Log.e(TAG, "List register | Temp size after push: " + mListRegister.size() + "|" + mTempListRegister.size());
					}
					else{
						showNotification(mTempListRegister, "register");
						mListRegister = mTempListRegister;
					}
					
				}
			}
		}, UPDATE_TIME_PERIOD, UPDATE_TIME_PERIOD);
	}

	private void showNotification(List<?> list, String status){
		String mTitle = "Bacarat: You got a new Message!";
		/*
		 * Remove text
		 */
		mText = "";
		
		Log.e(TAG, "Status push: " + status);
		if(status.equalsIgnoreCase("online")){
			mText = "Have ";
			@SuppressWarnings("unchecked")
			List<Online> listItem = (List<Online>)list;
			for(int i = 0; i < listItem.size(); i++){
				if(listItem.size() > 1){
					mText += listItem.get(i).getNamePlayer() + ",";
				}else{
					mText += listItem.get(i).getNamePlayer();
				}
			}
			mText += " " + status;
		}else{
			mText = "Have " + list.size() + " " + status;
		}
		
		mBundleExtras = new Bundle();
		mBundleExtras.putString("status", status);
		mBundleExtras.putString("text", mText);
		
		int notifyId = 1;
		NotificationCompat.Builder mBuilder = new Builder(this)
													.setContentTitle(mTitle)
													.setContentText(mText)
													.setSmallIcon(R.drawable.ic_logo);
		Intent resultIntent = new Intent(this, MainActivity.class);
		resultIntent.putExtras(mBundleExtras);
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		
		mBuilder.setContentIntent(resultPendingIntent);
		mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
		mBuilder.setAutoCancel(true);
//		mBuilder.setVibrate()
		
		mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		
		mNotificationManager.notify(notifyId, mBuilder.build());
		mNotificationManager.cancel(notifyId);
		
		Message me = mHandler.obtainMessage();
		mHandler.sendMessage(me);
	}
	
	
	
}
