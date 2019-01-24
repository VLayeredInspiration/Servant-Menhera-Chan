package com.zyfdroid.smc.base;
import android.app.*;

public class FunctionItem{
	public int icon;
	public String caption;
	public Class<Activity> activityClass;
	public IFunctionEvent eventmanager;
	public FunctionItem(String mLabel,int mIcomRes,Class activity){
		caption=mLabel;
		icon=mIcomRes;
		activityClass=activity;
	}
	//todo complete these and then complete start page.
}
