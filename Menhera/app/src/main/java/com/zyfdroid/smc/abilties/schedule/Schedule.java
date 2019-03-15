package com.zyfdroid.smc.abilties.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;

import com.zyfdroid.smc.R;
import com.zyfdroid.smc.abilties.AbilityEntry;
import com.zyfdroid.smc.abilties.IAbilityEventListener;
import com.zyfdroid.smc.service.MaimService;


public class Schedule extends AbilityEntry implements IAbilityEventListener{

    @Override
    public void onAbilityInit(Context ctx) {
        this.icon= R.drawable.type_getup;
        this.caption="提醒";
        this.GID=0x7F090001;
        this.setAbilityEventListener(this);
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
            MaimService.curctx.mWakeLock = MaimService.curctx.mPowerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MainService");
            MaimService.curctx.mWakeLock.acquire(10000);
            MaimService.curctx.checkScheduleWhileOff();
        } catch (Exception e) {
            ctx.startService(new Intent(ctx, MaimService.class));
        }
    }
}
