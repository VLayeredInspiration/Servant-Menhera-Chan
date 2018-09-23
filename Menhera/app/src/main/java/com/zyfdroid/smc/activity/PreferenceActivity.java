package com.zyfdroid.smc.activity;
import android.content.*;
import android.os.*;
import android.preference.*;
import android.widget.*;
import android.view.*;
import android.app.*;
import android.view.View.*;
import com.zyfdroid.smc.*;
import com.zyfdroid.smc.service.*;

public class PreferenceActivity extends android.preference.PreferenceActivity implements Preference.OnPreferenceClickListener
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		getPreferenceManager().setSharedPreferencesName("settings");
		addPreferencesFromResource(R.xml.preferences);
		findPreference("birthday").setOnPreferenceClickListener(this);
	}


	@Override
	public boolean onPreferenceClick(Preference preference)
	{
		// TODO: Implement this method
		if(preference.getKey().contentEquals("birthday")){
			onBirthdayPreferenceClick();
			return true;
		}
		return false;
	}

	
	

	@Override
	protected void onDestroy()
	{
		// TODO: Implement this method
		super.onDestroy();
		try{
			MaimService.curctx.changeNotifican(-1,null,null);
		}catch(NullPointerException npe){
			Main.e(npe);
			startService(new Intent(this,MaimService.class));
		}
		catch(Exception e){Main.e(e);}
	}

	void tw(String s){
	Toast.makeText(this,s,1000).show();
	}
	
	
	
	void onBirthdayPreferenceClick(){
		AlertDialog.Builder adb=new AlertDialog.Builder(this);
		
	}
	
	
	
	
	
	
}


abstract class DatePickerDialog extends Dialog
{
public DatePickerDialog(Context ctx){
	super(ctx);
}
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
	}
	
	
	
	public abstract void onDatePickResult(boolean comfirm,int year,int month,int date,boolean isLunar)
	
	
}


	
