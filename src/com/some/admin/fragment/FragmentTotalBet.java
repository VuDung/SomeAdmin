package com.some.admin.fragment;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.some.admin.MainActivity;
import com.some.admin.R;
import com.some.admin.model.Total;
import com.some.admin.parser.DataParser;
import com.some.admin.tabbutton.RadioGroupController;
import com.some.admin.tabbutton.RadioGroupController.OnCheckedChangeListener;
import com.some.admin.tabbutton.TabButton;
import com.some.admin.tabbutton.TabButton.Type;
import com.some.admin.utilities.APIType;
import com.some.admin.utilities.Constains;
import com.some.admin.utilities.PreferenceUtils;

import covisoft.android.managebackgroundtask.Task;
import covisoft.android.managebackgroundtask.TaskAction;
import covisoft.android.managebackgroundtask.TaskResponse;
import covisoft.android.managebackgroundtask.listener.ITaskListenter;

public class FragmentTotalBet extends SherlockFragment implements OnCheckedChangeListener, ITaskListenter{

	private MainActivity mActivity;
	private TabButton tabLeft;
	private TabButton tabRight;
	private List<Total> mListTotal;
	private RadioGroupController tabButtonController;
	
	private TextView tvBetPlayerValue ;
	private TextView tvBetBankerValue ;
	private TextView tvBettieValue ;
	private TextView tvBetPlayerPairValue ;
	private TextView tvBetBankerPair ;
	
	private ProgressBar mProgress;
	private static final String TAG = FragmentTotalBet.class.getSimpleName();
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
		
		
		View v = inflater.inflate(R.layout.fragment_total, container, false);
		
		initView(v);
				
		return v;
	}
	
	private void setUpActionBar(){
		ActionBar mActionBar = mActivity.getSupportActionBar();
    	mActionBar.setDisplayHomeAsUpEnabled(true);
    	mActionBar.setHomeButtonEnabled(true);
	}
	
	private void initView(View v){
		mProgress = (ProgressBar)v.findViewById(R.id.pbTotal);
		tvBetPlayerValue = (TextView) v.findViewById(R.id.tvBetPlayerValue);
		tvBetBankerValue = (TextView) v.findViewById(R.id.tvBetBankerValue);
		tvBettieValue = (TextView) v.findViewById(R.id.tvBettieValue);
		tvBetPlayerPairValue = (TextView) v.findViewById(R.id.tvBetPlayerPairValue);
		tvBetBankerPair = (TextView) v.findViewById(R.id.tvBetBankerPairValue);
		
		tabLeft = (TabButton)v.findViewById(R.id.tabChannelFirst);
		tabLeft.setType(Type.left);
		
		tabRight = (TabButton)v.findViewById(R.id.tabChannelSecond);
		tabRight.setType(Type.right);
		
		tabButtonController = new RadioGroupController();
		tabButtonController.setOnCheckedChangeListener(this);
		tabButtonController.setRadioButtons(tabLeft, tabRight);
		tabButtonController.setSelection(0);
		
		Log.e(TAG, tabButtonController.getCheckedRadioButtonId() + "");
		
		callTask();
	}

	@Override
	public void onCheckedChanged(int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.tabChannelFirst:
			callTask();
			break;
		case R.id.tabChannelSecond:
			callTask();
			break;
		default:
			break;
		}
	}
	
	private void callTask(){
		mProgress.setVisibility(View.VISIBLE);
		Task task = new Task(this);
		task.loginTask(TaskAction.ActionGetTotal, APIType.TOTAL, null, true);
	}

	@Override
	public void onComplete(Task task, TaskResponse response) {
		// TODO Auto-generated method stub
		if(response.isSuccess() && response.getAction() == TaskAction.ActionGetTotal){
			mProgress.setVisibility(View.GONE);
			String data = (String)response.getData();
			DataParser parser = new DataParser();
			mListTotal = parser.parserTotal(data);
			initTotal();
		}
	}
	
	private void initTotal(){
//		if(tabLeft.isChecked()){
		if(tabButtonController.getCheckedRadioButtonId() == R.id.tabChannelFirst){
			tabButtonController.setSelection(0);
			tvBetPlayerValue.setText(mListTotal.get(0).getBetplayer());
    		tvBetBankerValue.setText(mListTotal.get(0).getBetbanker());
    		tvBettieValue.setText(mListTotal.get(0).getBettie());
    		tvBetPlayerPairValue.setText(mListTotal.get(0).getBetplayerpair());
    		tvBetBankerPair.setText(mListTotal.get(0).getBetbankerpair());
		}else{
			tabButtonController.setSelection(1);
			tvBetPlayerValue.setText(mListTotal.get(1).getBetplayer());
    		tvBetBankerValue.setText(mListTotal.get(1).getBetbanker());
    		tvBettieValue.setText(mListTotal.get(1).getBettie());
    		tvBetPlayerPairValue.setText(mListTotal.get(1).getBetplayerpair());
    		tvBetBankerPair.setText(mListTotal.get(1).getBetbankerpair());
		}
	}
	
}
