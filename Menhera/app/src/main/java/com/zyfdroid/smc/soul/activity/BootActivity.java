package com.zyfdroid.smc.soul.activity;
import android.os.*;
import android.content.*;
import com.zyfdroid.smc.soul.base.*;
import com.zyfdroid.smc.soul.service.*;

public class BootActivity extends BaseActivity
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		if(!MainActivity.isFirstRun(this)){
		try{
			startService(new Intent(this,MaimService.class));
			finish();
		}catch(Exception e){
			utw("可能是设置里禁用了后台服务？去设置看看，然后再试试。","Menhera Chan无法启动");
		}
		}
		
	}
	
	
}
