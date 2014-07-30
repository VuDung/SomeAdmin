package com.some.admin.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.some.admin.MainActivity;
import com.some.admin.R;
import com.some.admin.utilities.Constains;
import com.some.admin.utilities.PreferenceUtils;

public class FragmentMain extends SherlockFragment implements OnClickListener{

	private LinearLayout mLayoutOnline;
	private LinearLayout mLayoutInbound;
	private LinearLayout mLayoutOutbound;
	private LinearLayout mLayoutNewRegister;
	private LinearLayout mLayoutTotalBetting;
	
	private MainActivity mActivity;
	private String mUserLogin;
	
	private Bundle mExtras;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof MainActivity){
			mActivity = (MainActivity)activity;
		}
		
	}
	
	private void initPushNotification(){
    	try {
			if(mExtras.getString("status").equalsIgnoreCase("online")){
				mActivity.replaceFragment(new FragmentOnline());
			}else if (mExtras.getString("status").equalsIgnoreCase("inbound")) {
				mActivity.replaceFragment(new FragmentInbound());
			}else if (mExtras.getString("status").equalsIgnoreCase("outbound")) {
				mActivity.replaceFragment(new FragmentOutbound());
			}else if (mExtras.getString("status").equalsIgnoreCase("register")) {
				mActivity.replaceFragment(new FragmentNewRegister());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		setUpActionBar();
		setHasOptionsMenu(true);
		mUserLogin = PreferenceUtils.getPreference(mActivity, Constains.PREFS_USERNAME);
		
		View v = inflater.inflate(R.layout.fragment_main, container, false);
		
		initView(v);
		
		return v;
	}

	private void setUpActionBar(){
    	ActionBar mActionBar = mActivity.getSupportActionBar();
    	mActionBar.setDisplayHomeAsUpEnabled(false);
    	mActionBar.setHomeButtonEnabled(false);
    }

    private void initView(View v){
    	mLayoutOnline = (LinearLayout)v.findViewById(R.id.llOnline);
    	mLayoutInbound = (LinearLayout)v.findViewById(R.id.llInbound);
    	mLayoutOutbound = (LinearLayout)v.findViewById(R.id.llOutbound);
    	mLayoutNewRegister = (LinearLayout)v.findViewById(R.id.llNewRegister);
    	mLayoutTotalBetting = (LinearLayout)v.findViewById(R.id.llTotalBet);
    	
    	if(mUserLogin.toString().equalsIgnoreCase("moon107749")
				||mUserLogin.toString().equalsIgnoreCase("daldal")
				||mUserLogin.toString().equalsIgnoreCase("chun1215")
				||mUserLogin.toString().equalsIgnoreCase("khj1365")){
    		mLayoutTotalBetting.setVisibility(View.VISIBLE);
    		mLayoutTotalBetting.setOnClickListener(this);
    	}else{
    		mLayoutTotalBetting.setVisibility(View.GONE);
    	}
    	
    	mLayoutOnline.setOnClickListener(this);
    	mLayoutInbound.setOnClickListener(this);
    	mLayoutOutbound.setOnClickListener(this);
    	mLayoutNewRegister.setOnClickListener(this);
    	
    	mExtras = getArguments();
		if(mExtras != null){
			initPushNotification();
		}else{
			
		}
    	
    }
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llOnline:
			FragmentOnline fragOnline = new FragmentOnline();
			mActivity.replaceFragment(fragOnline);
			
			break;
		case R.id.llInbound:
			FragmentInbound fragInbound = new FragmentInbound();
			mActivity.replaceFragment(fragInbound);
			break;
		case R.id.llOutbound:
			FragmentOutbound fragOutbound = new FragmentOutbound();
			mActivity.replaceFragment(fragOutbound);
			break;
		case R.id.llNewRegister:
			FragmentNewRegister fragRegister = new FragmentNewRegister();
			mActivity.replaceFragment(fragRegister);
			break;
		case R.id.llTotalBet:
			FragmentTotalBet fragTotal = new FragmentTotalBet();
			mActivity.replaceFragment(fragTotal);
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		mExtras = null;
	}
	

	
}
