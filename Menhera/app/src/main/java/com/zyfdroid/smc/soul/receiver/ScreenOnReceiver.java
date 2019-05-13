package com.zyfdroid.smc.soul.receiver;

import android.content.*;

import com.zyfdroid.smc.soul.service.*;
import com.zyfdroid.smc.*;
import com.zyfdroid.smc.soul.activity.*;

public class ScreenOnReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context p1, Intent p2) {
        if (null != MaimService.mCurrentContext) {
            try {
                if (p2.getAction().contentEquals(p2.ACTION_SCREEN_OFF)) {
                    Main.d("ScreenOffReceiver");
                    try {
                        MaimService.mCurrentContext.callOnScreenOff();
                        MaimService.mCurrentContext.serviceThread.interrupt();
                    } catch (NullPointerException npe) {
                        Main.e(npe);
                        Main.d("Service not available, Ignoring.");
                    }
                } else {
                    Main.d("ScreenOnReveiver");
                    MaimService.mCurrentContext.callOnScreenOn();
                    MaimService.mCurrentContext.checkState();
                }
            } catch (Exception e) {
                Main.e(e);

                Intent i = new Intent(p1, BootActivity.class);
                i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
                p1.startActivity(i);
            }
        } else {
            Intent i = new Intent(p1, BootActivity.class);
            i.addFlags(i.FLAG_ACTIVITY_NEW_TASK);
            p1.startActivity(i);
        }
    }

}
