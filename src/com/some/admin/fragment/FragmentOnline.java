package com.some.admin.fragment;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.some.admin.MainActivity;
import com.some.admin.R;
import com.some.admin.adapter.OnlineAdapter;
import com.some.admin.model.Online;
import com.some.admin.parser.DataParser;
import com.some.admin.utilities.APIType;
import com.some.admin.utilities.Constains;
import com.some.admin.utilities.PreferenceUtils;

import covisoft.android.managebackgroundtask.Task;
import covisoft.android.managebackgroundtask.TaskAction;
import covisoft.android.managebackgroundtask.TaskResponse;
import covisoft.android.managebackgroundtask.listener.ITaskListenter;

public class FragmentOnline extends SherlockFragment implements ITaskListenter, OnItemClickListener{

	private MainActivity mActivity;
	private ListView mListViewOnline;
	private OnlineAdapter mAdapter;
	private List<Online> mListOnline;
	private ProgressBar mProgressOnline;
	
	private String mUserLogin;
	private static final String TAG = FragmentOnline.class.getSimpleName();
	
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
		
		View v = inflater.inflate(R.layout.fragment_online, container, false);
		
		mUserLogin = PreferenceUtils.getPreference(mActivity, Constains.PREFS_USERNAME);
		
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
		mProgressOnline = (ProgressBar)v.findViewById(R.id.pbOnline);
		mListViewOnline = (ListView)v.findViewById(R.id.lvOnline);
		mAdapter = new OnlineAdapter(mActivity);
		mListViewOnline.setAdapter(mAdapter);
		
		if(mUserLogin.toString().equalsIgnoreCase("moon107749")
				||mUserLogin.toString().equalsIgnoreCase("daldal")
				||mUserLogin.toString().equalsIgnoreCase("chun1215")
				||mUserLogin.toString().equalsIgnoreCase("khj1365")){
			mListViewOnline.setOnItemClickListener(this);
		}
		callTask();
		
	}
	
	private void callTask(){
		mProgressOnline.setVisibility(View.VISIBLE);
		Task mTask = new Task(this);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("username", mUserLogin);
		mTask.loginTask(TaskAction.ActionGetOnline, APIType.ONLINE, params, true);
	}

	@Override
	public void onComplete(Task task, TaskResponse response) {
		if(response.isSuccess() && response.getAction() == TaskAction.ActionGetOnline){
			mProgressOnline.setVisibility(View.GONE);
			
			String data = (String)response.getData();
			DataParser parser = new DataParser();
			mListOnline = parser.parserOnline(data);
			
			Log.e(TAG, "List Online size: " + mListOnline.size());
			if(mListOnline.size() == 0){
				Toast.makeText(mActivity, "No One Online", Toast.LENGTH_SHORT).show();
			}
			mAdapter.setListOnline(mListOnline);
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		FragmentOnlineDetail fragOnlineDetail = new FragmentOnlineDetail();
		Bundle args = new Bundle();
		args.putString("player",mListOnline.get(position).getNamePlayer());
		fragOnlineDetail.setArguments(args);
		mActivity.replaceFragment(fragOnlineDetail);
	}
}
