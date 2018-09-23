package com.zyfdroid.smc.receiver;
import android.content.*;
import com.zyfdroid.smc.activity.*;

public class BootReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context p1, Intent p2)
	{
		//
		Intent i=new Intent(p1,BootActivity.class);
		i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
		p1.startActivity(i);
	}
	
}
