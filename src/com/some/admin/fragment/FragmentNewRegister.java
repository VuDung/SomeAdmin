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
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.some.admin.MainActivity;
import com.some.admin.R;
import com.some.admin.adapter.RegisterAdapter;
import com.some.admin.model.Register;
import com.some.admin.parser.DataParser;
import com.some.admin.utilities.APIType;
import com.some.admin.utilities.Constains;
import com.some.admin.utilities.PreferenceUtils;

import covisoft.android.managebackgroundtask.Task;
import covisoft.android.managebackgroundtask.TaskAction;
import covisoft.android.managebackgroundtask.TaskResponse;
import covisoft.android.managebackgroundtask.listener.ITaskListenter;

public class FragmentNewRegister extends SherlockFragment implements ITaskListenter{

	private MainActivity mActivity;
	
	private ListView mListViewRegister;
	private RegisterAdapter mAdapter;
	private List<Register> mListRegister;
	private ProgressBar mProgressRegister;
	private LinearLayout mContentRegister;
	private TextView mTextViewInfo;
	
	private String mUserLogin;
	private static final String TAG = FragmentNewRegister.class.getSimpleName();
	
	
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
		
		View v = inflater.inflate(R.layout.fragment_register, container, false);
		
		initView(v);
		
		return v;
	}
	
	private void setUpActionBar(){
		ActionBar mActionBar = mActivity.getSupportActionBar();
    	mActionBar.setDisplayHomeAsUpEnabled(true);
    	mActionBar.setHomeButtonEnabled(true);
	}
	
	private void initView(View v){
		mContentRegister = (LinearLayout)v.findViewById(R.id.llContentRegister);
		mTextViewInfo = (TextView)v.findViewById(R.id.tvInfoRegister);
		mProgressRegister = (ProgressBar)v.findViewById(R.id.pbRegister);
		mListViewRegister = (ListView)v.findViewById(R.id.lvRegister);
		mAdapter = new RegisterAdapter(mActivity);
		
		mListViewRegister.setAdapter(mAdapter);
		
		
		
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
		mProgressRegister.setVisibility(View.VISIBLE);
		Task mTask = new Task(this);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("username", mUserLogin);
		mTask.loginTask(TaskAction.ActionGetRegister, APIType.REGISTER, params, true);
	}

	@Override
	public void onComplete(Task task, TaskResponse response) {
		// TODO Auto-generated method stub
		if(response.isSuccess() && response.getAction() == TaskAction.ActionGetRegister){
			mProgressRegister.setVisibility(View.GONE);
			
			String data = (String)response.getData();
			DataParser parser = new DataParser();
			mListRegister = parser.parserRegister(data);
			if(mListRegister.size() > 0){
				mContentRegister.setVisibility(View.VISIBLE);
				mTextViewInfo.setVisibility(View.GONE);
				mAdapter.setListRegister(mListRegister);
			}else{
				mContentRegister.setVisibility(View.GONE);
				mTextViewInfo.setVisibility(View.VISIBLE);
			}
			Log.e(TAG, "List Online size: " + mListRegister.size());
			
		}
	}
}
