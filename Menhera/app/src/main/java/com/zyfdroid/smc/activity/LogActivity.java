package com.zyfdroid.smc.activity;
import android.app.*;
import android.os.*;
import android.widget.*;
import com.zyfdroid.smc.*;

public class LogActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logs);
		String logs=getIntent().getExtras().getString("crash");
		if(null!=logs){
			((TextView)findViewById(R.id.logsText)).setText(logs);
		}
	}
	
}
