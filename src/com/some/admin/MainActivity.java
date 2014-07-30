package com.some.admin;


import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.some.admin.activity.LoginActivity;
import com.some.admin.fragment.FragmentInbound;
import com.some.admin.fragment.FragmentMain;
import com.some.admin.fragment.FragmentNewRegister;
import com.some.admin.fragment.FragmentOnline;
import com.some.admin.fragment.FragmentOutbound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends SherlockFragmentActivity {

	private static final String TAG = MainActivity.class.getSimpleName();
	private Bundle mExtras;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        mExtras = getIntent().getExtras();
        
        if (savedInstanceState == null ) {
        	FragmentMain fragMain = new FragmentMain();
        	if(mExtras != null){
        		fragMain.setArguments(mExtras);
        	}
        	replaceFragment(fragMain);
        }
        
    }
    
    
    
    public void replaceFragment(Fragment fragment){
    	String mBackStateName = fragment.getClass().getName();
    	String mFragmentTag = mBackStateName;
    	FragmentManager mFragManager = getSupportFragmentManager();
    	boolean isFragmentPopped = mFragManager.popBackStackImmediate(mBackStateName, 0);
    	if(!isFragmentPopped && mFragManager.findFragmentByTag(mFragmentTag) == null){
    		FragmentTransaction ft = mFragManager.beginTransaction();
    		ft.replace(R.id.container, fragment);
//    		ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left,
//    				R.anim.slide_out_right);

    		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
    		ft.addToBackStack(mBackStateName);
    		ft.commit();
    	}
    }


	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(getSupportFragmentManager().getBackStackEntryCount() == 1){
			finish();
		}else{
			super.onBackPressed();
		}
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.logout:
			Intent iLogin = new Intent(MainActivity.this, LoginActivity.class);
			startActivity(iLogin);
			finish();
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	

    

}
