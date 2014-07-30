package com.some.admin.activity;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.some.admin.MainActivity;
import com.some.admin.MainApplication;
import com.some.admin.R;
import com.some.admin.parser.DataParser;
import com.some.admin.service.SMService;
import com.some.admin.utilities.APIType;
import com.some.admin.utilities.Constains;
import com.some.admin.utilities.NetworkUtils;
import com.some.admin.utilities.PreferenceUtils;

import covisoft.android.managebackgroundtask.Task;
import covisoft.android.managebackgroundtask.TaskAction;
import covisoft.android.managebackgroundtask.TaskResponse;
import covisoft.android.managebackgroundtask.listener.ITaskListenter;

public class LoginActivity extends SherlockActivity implements OnClickListener, ITaskListenter{

	private EditText mUserName;
	private EditText mPassword;
	private Button mButtonLogin;
	private ProgressBar mProgressLogin;
	private static final String TAG = LoginActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		initView();
	}
	
	private void initView(){
		mUserName = (EditText)findViewById(R.id.txtUserName);
		mPassword = (EditText)findViewById(R.id.txtPassword);
		mProgressLogin = (ProgressBar)findViewById(R.id.pbLogin);
		
		mButtonLogin = (Button)findViewById(R.id.btnLogin);
		mButtonLogin.setOnClickListener(this);
		
		String userNamePrefs = PreferenceUtils.getPreference(this, Constains.PREFS_USERNAME);
		String passwordPrefs = PreferenceUtils.getPreference(this, Constains.PREFS_PASSWORD);
		
		if(userNamePrefs.length() > 0 && passwordPrefs.length() > 0){
			mUserName.setText(userNamePrefs);
			mPassword.setText(passwordPrefs);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnLogin:
			mProgressLogin.setVisibility(View.VISIBLE);
			if(NetworkUtils.isConnected(this)){
				Task mTask = new Task(this);
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("username", mUserName.getText().toString());
				params.put("password", mPassword.getText().toString());
				mTask.loginTask(TaskAction.ActionLogin, APIType.LOGIN, params, true);
			}else{
				Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
			}
			
			break;

		default:
			break;
		}
	}

	@Override
	public void onComplete(Task task, TaskResponse response) {
		// TODO Auto-generated method stub
		if(response.isSuccess() && response.getAction() == TaskAction.ActionLogin){
			mProgressLogin.setVisibility(View.GONE);
			
			String data = (String)response.getData();
			Log.e(TAG, data);
			DataParser parser = new DataParser();
			boolean loginStatus = parser.parserLogin(data);
			if(loginStatus){
				PreferenceUtils.setPreference(getApplicationContext(), Constains.PREFS_USERNAME, mUserName.getText().toString());
				PreferenceUtils.setPreference(getApplicationContext(), Constains.PREFS_PASSWORD, mPassword.getText().toString());
				
//				boolean isServiceRunning = MainApplication.getInstance().isServiceRunning(SMService.class.getName());
//				if(!isServiceRunning){
					Intent iService = new Intent(getApplicationContext(), SMService.class);
					startService(iService);
//				}
				Intent iMain = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(iMain);
				finish();
			}else{
				Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
			}
		}
	}

	
}
