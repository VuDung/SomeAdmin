package com.some.admin.fragment;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.some.admin.MainActivity;
import com.some.admin.R;
import com.some.admin.adapter.BoundAdapter;
import com.some.admin.model.Bound;
import com.some.admin.parser.DataParser;
import com.some.admin.utilities.APIType;
import com.some.admin.utilities.Constains;
import com.some.admin.utilities.PreferenceUtils;

import covisoft.android.managebackgroundtask.Task;
import covisoft.android.managebackgroundtask.TaskAction;
import covisoft.android.managebackgroundtask.TaskResponse;
import covisoft.android.managebackgroundtask.listener.ITaskListenter;

public class FragmentOutbound extends SherlockFragment implements ITaskListenter{

	private MainActivity mActivity;

	private ListView mListViewBound;
	private BoundAdapter mAdapter;
	private ProgressBar mProgressBound;
	private LinearLayout mContentBound;
	private TextView mTvInfo;
	private List<Bound> mListBound;
	private String mUserLogin;
	
	private static final String TAG = FragmentOutbound.class.getSimpleName();
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
		
		View v = inflater.inflate(R.layout.fragment_bound, container, false);
		
		initView(v);
		
		return v;
	}
	
	private void setUpActionBar(){
		ActionBar mActionBar = mActivity.getSupportActionBar();
    	mActionBar.setDisplayHomeAsUpEnabled(true);
    	mActionBar.setHomeButtonEnabled(true);
	}

	private void initView(View v){
		mContentBound = (LinearLayout)v.findViewById(R.id.llContentBound);
		mTvInfo = (TextView)v.findViewById(R.id.tvNoinfo);
		
		mProgressBound = (ProgressBar)v.findViewById(R.id.pbBound);
		mListViewBound = (ListView)v.findViewById(R.id.lvBound);
		mAdapter = new BoundAdapter(mActivity);
		
		mListViewBound.setAdapter(mAdapter);
		
		
		callTask();
		
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
	
	private void callTask(){
		mProgressBound.setVisibility(View.VISIBLE);
		Task mTask = new Task(this);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("username", mUserLogin);
		mTask.loginTask(TaskAction.ActionGetOutbound, APIType.OUTBOUND, params, true);
	}

	@Override
	public void onComplete(Task task, TaskResponse response) {
		// TODO Auto-generated method stub
		if(response.isSuccess() && response.getAction() == TaskAction.ActionGetOutbound){
			mProgressBound.setVisibility(View.GONE);
			
			String data = (String)response.getData();
			
			DataParser parser = new DataParser();
			mListBound = parser.parserBound(data);
			if(mListBound.size() > 0){
				mContentBound.setVisibility(View.VISIBLE);
				mTvInfo.setVisibility(View.GONE);
				mAdapter.setListBound(mListBound);
			}else{
				mContentBound.setVisibility(View.GONE);
				mTvInfo.setVisibility(View.VISIBLE);
			}
			Log.e(TAG, "List outbound size: " + mListBound.size());
			
		}
	}
	
	
}
