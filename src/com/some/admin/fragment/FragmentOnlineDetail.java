package com.some.admin.fragment;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.some.admin.MainActivity;
import com.some.admin.R;
import com.some.admin.model.Player;
import com.some.admin.parser.DataParser;
import com.some.admin.utilities.APIType;
import com.some.admin.utilities.Constains;
import com.some.admin.utilities.PreferenceUtils;

import covisoft.android.managebackgroundtask.Task;
import covisoft.android.managebackgroundtask.TaskAction;
import covisoft.android.managebackgroundtask.TaskResponse;
import covisoft.android.managebackgroundtask.listener.ITaskListenter;

public class FragmentOnlineDetail extends SherlockFragment implements ITaskListenter{

	private MainActivity mActivity;
	private String mUserLogin;
	private String mPlayer;
	
	private LinearLayout mContentDetail;
	private TextView mTextInfoRegister;
	private ProgressBar mProgressDetail;
	TextView tvBetPlayerValue ;
	TextView tvBetBankerValue ;
	TextView tvBettieValue ;
	TextView tvBetPlayerPairValue ;
	TextView tvBetBankerPair ;
	
	private List<Player> mListPlayer;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(activity instanceof MainActivity){
			mActivity = (MainActivity)activity;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setUpActionBar();
		setHasOptionsMenu(true);
		mUserLogin = PreferenceUtils.getPreference(mActivity, Constains.PREFS_USERNAME);
		mPlayer = getArguments().getString("player");
		
		View v = inflater.inflate(R.layout.fragment_online_detail, container, false);
		initView(v);
		return v;
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_fragment, menu);
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {
		case R.id.refresh:
			callTask();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
		
	}
	
	private void setUpActionBar(){
		ActionBar mActionBar = mActivity.getSupportActionBar();
    	mActionBar.setDisplayHomeAsUpEnabled(true);
    	mActionBar.setHomeButtonEnabled(true);
	}

	private void initView(View v){
		mContentDetail = (LinearLayout)v.findViewById(R.id.llContentDetail);
		mTextInfoRegister = (TextView)v.findViewById(R.id.tvInfoDetail);
		mProgressDetail = (ProgressBar)v.findViewById(R.id.pbDetail);
		
		tvBetPlayerValue = (TextView) v.findViewById(R.id.tvBetPlayerValue);
		tvBetBankerValue = (TextView) v.findViewById(R.id.tvBetBankerValue);
		tvBettieValue = (TextView) v.findViewById(R.id.tvBettieValue);
		tvBetPlayerPairValue = (TextView) v.findViewById(R.id.tvBetPlayerPairValue);
		tvBetBankerPair = (TextView) v.findViewById(R.id.tvBetBankerPairValue);
		
		callTask();
	}
	
	private void callTask(){
		mProgressDetail.setVisibility(View.VISIBLE);
		Task task = new Task(this);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("player", mPlayer);
		params.put("manager", mUserLogin);
		task.loginTask(TaskAction.ActionGetBetDetail, APIType.BETINFO, params, true);
	}

	@Override
	public void onComplete(Task task, TaskResponse response) {
		// TODO Auto-generated method stub
		if(response.isSuccess() && response.getAction() == TaskAction.ActionGetBetDetail){
			mProgressDetail.setVisibility(View.GONE);
			
			String data = (String)response.getData();
			DataParser parser = new DataParser();
			mListPlayer = parser.parserPlayer(data);
			if(mListPlayer.get(0).getGameType().equalsIgnoreCase("1")){
				mContentDetail.setVisibility(View.VISIBLE);
				mTextInfoRegister.setVisibility(View.GONE);
				
				tvBetPlayerValue.setText(mListPlayer.get(0).getBetplayer());				
				tvBetBankerValue.setText(mListPlayer.get(0).getBetbanker());				
				tvBettieValue.setText(mListPlayer.get(0).getBettie());				
				tvBetPlayerPairValue.setText(mListPlayer.get(0).getBetplayerpair());				
				tvBetBankerPair.setText(mListPlayer.get(0).getBetbankerpair());
			}else{
				mContentDetail.setVisibility(View.GONE);
				mTextInfoRegister.setVisibility(View.VISIBLE);
			}
		}
	}
	
}
