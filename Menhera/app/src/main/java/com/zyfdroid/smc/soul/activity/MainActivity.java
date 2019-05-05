package com.zyfdroid.smc.soul.activity;

import android.content.*;

import com.zyfdroid.smc.soul.base.*;
import com.zyfdroid.smc.soul.service.*;

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
		return true;
	}
	
}
