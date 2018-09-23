package com.zyfdroid.smc.receiver;
import android.content.*;
import android.os.*;
import com.zyfdroid.smc.service.*;
import com.zyfdroid.smc.*;
import com.zyfdroid.smc.activity.*;

public class ScreenOnReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context p1, Intent p2)
	{
		// TODO: Implement this method
		if(null!=MaimService.curctx){
			try{
				if(p2.getAction().contentEquals(p2.ACTION_SCREEN_OFF)){
					Main.d("ScreenOffReceiver");
					try{
					MaimService.curctx.mWakeLock = MaimService.curctx.mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MainService");  
					MaimService.curctx.mWakeLock.acquire(10000);
					MaimService.curctx.checkScheduleWhileOff();
					MaimService.curctx.serviceThread.interrupt();
					}catch(NullPointerException npe){
						Main.e(npe);
						Main.d("Service not available, Ignoring.");
					}
				}
				else{
					Main.d("ScreenOnReveiver");
					MaimService.curctx.resumeTask();
			MaimService.curctx.checkScheduleWhileOff();
			MaimService.curctx.checkSchedule();
			}
			}catch(Exception e){
				Main.e(e);
				
				Intent i=new Intent(p1,BootActivity.class);
				i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
				p1.startActivity(i);
			}
		}else{
			Intent i=new Intent(p1,BootActivity.class);
			i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
			p1.startActivity(i);
		}
	}
	
}
