package com.zyfdroid.smc.abilties.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;

import com.zyfdroid.smc.R;
import com.zyfdroid.smc.abilties.AbilityEntry;
import com.zyfdroid.smc.abilties.IAbilityEventListener;
import com.zyfdroid.smc.abilties.ShortMessageProvider;
import com.zyfdroid.smc.soul.service.MaimService;

import java.util.Date;


public class Schedule extends AbilityEntry implements IAbilityEventListener{

    @Override
    public void onAbilityInit(Context ctx) {
        this.icon= R.drawable.type_getup;
        this.caption="提醒";
        this.GID=0x7F090001;
        this.setAbilityEventListener(this);
        this.setShortMessageProvider(new ShortMessageProvider("下一个提醒") {
            int foldable;
            @Override
            public String getString(Context ctx, AbilityEntry provider) {

                long hookTime = -1;
                MyAlarm mal = null;
                long[] ids = MyAlarm.getAllAlarm(ctx);
                for (int i = 0; i < ids.length; i++) {
                    mal = MyAlarm.loadAlarm(ctx, ids[i]);
                    if (mal.enabled && mal.targetTime > System.currentTimeMillis() + 15000l) {
                        if (hookTime == -1) {
                            hookTime = mal.targetTime;
                        }
                        if (mal.targetTime <= hookTime) {
                            hookTime = mal.targetTime;
                        }
                    }
                }


                StringBuilder sb = new StringBuilder("下个提醒:");
                if (hookTime > 0) {
                    Date targetTime = new Date(hookTime);
                    sb.append(targetTime.getMonth() + 1);
                    sb.append("月").append(targetTime.getDate()).append("日");


                    if (targetTime.getHours() < 10) {
                        sb.append("0");
                    }
                    sb.append(targetTime.getHours());
                    sb.append(":");
                    if (targetTime.getMinutes() < 10) {
                        sb.append("0");
                    }
                    sb.append(targetTime.getMinutes());
                    return sb.toString();
                } else {
                    return "没有提醒任务。";
                }
            }
        });
    }



    @Override
    public void onNewDay(Context ctx) {

    }

    @Override
    public void onTimeInterval(Context ctx) {

    }

    @Override
    public void onAlarm(Context ctx, Bundle datas) {

    }


    @Override
    public void onScreenOn(Context ctx) {

    }

    @Override
    public void onScreenOff(Context ctx) {

    }

    @Override
    public void onFunctionClick(Context ctx) {
        startActivity(ctx, AlarmListActivity.class);
    }


    public void setAlarms(Context ctx){


    }


    public static void requireNext(Context ctx) {
        try {
            MaimService.mCurrentContext.mWakeLock = MaimService.mCurrentContext.mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MainService");
            MaimService.mCurrentContext.mWakeLock.acquire(10000);
            MaimService.mCurrentContext.checkScheduleWhileOff();
        } catch (Exception e) {
            ctx.startService(new Intent(ctx, MaimService.class));
        }
    }
}
