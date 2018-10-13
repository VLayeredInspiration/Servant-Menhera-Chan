package com.zyfdroid.smc.activity;

import android.view.*;
import android.widget.*;
import android.app.*;
import android.os.*;
import android.content.*;
import android.provider.*;
import android.net.*;
import com.zyfdroid.smc.base.*;
import com.zyfdroid.smc.service.*;

public class MainActivity extends BaseActivity 
{
	@Override
	
	public void onPrepareUi()
	{
		if(isFirstRun(this)){
			startActivity(new Intent(this,GuideActivity.class));
		}else{
			startService(new Intent(this,MaimService.class));
		}
		finish();
	}
	
	public static boolean isFirstRun(Context ctx){
		//TODO:判断跳转启动页
		return false;
	}
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public boolean isHiddenTitleBar()
	{
		// TODO: Implement this method
		return true;
	}
	
}
