package com.zyfdroid.smc.abilties.schedule;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;

import com.zyfdroid.smc.R;
import com.zyfdroid.smc.abilties.AbilityEntry;
import com.zyfdroid.smc.abilties.AbilityManager;
import com.zyfdroid.smc.abilties.IAbilityEventListener;
import com.zyfdroid.smc.abilties.ShortMessageProvider;
import com.zyfdroid.smc.soul.service.MaimService;

import java.util.ArrayList;
import java.util.Date;


public class Schedule extends AbilityEntry implements IAbilityEventListener{

    public static Schedule getInstance(){
        return (Schedule) AbilityManager.servantAbilities.get("_alarm");
    }

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
        Intent i=new Intent(ctx,ScheduleActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("alarms",datas.getLongArray("alarms"));
        ctx.startActivity(i);
    }


    @Override
    public void onScreenOn(Context ctx) {
        checkMissingSchedule(ctx);
        calculateAlarmClock(ctx);
    }

    @Override
    public void onScreenOff(Context ctx) {
        calculateAlarmClock(ctx);
    }

    @Override
    public void onFunctionClick(Context ctx) {
        startActivity(ctx, AlarmListActivity.class);
    }





    public static void requireNext(Context ctx) {

    }


    void checkMissingSchedule(Context ctx) {

        MyAlarm mal = null;
        ArrayList<Long> missedAlarmId = new ArrayList<Long>();
        long[] ids = MyAlarm.getAllAlarm(ctx);
        for (int i = 0; i < ids.length; i++) {
            mal = MyAlarm.loadAlarm(ctx, ids[i]);
            if (mal.targetTime <= System.currentTimeMillis() - 90000l) {
                if (mal.enabled) {
                    missedAlarmId.add(ids[i]);
                } else {
                    mal.targetTime = mal.nextTime();
                    mal.saveAlarm(ctx, ids[i]);
                }
            }
        }
        long[] mids = new long[missedAlarmId.size()];
        for (int i = 0; i < mids.length; i++) {
            mids[i] = missedAlarmId.get(i);
        }
        if (mids.length != 0) {
            Intent i = new Intent(ctx, MissedAlarmActivity.class);
            i.putExtra("alarms", mids);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(i);
        }
    }


    void calculateAlarmClock(Context ctx){
        long hookTime = -1;
        MyAlarm mal = null;
        ArrayList<Long> firstAlarmId = new ArrayList<Long>();
        long[] ids = MyAlarm.getAllAlarm(ctx);
        for (int i = 0; i < ids.length; i++) {
            mal = MyAlarm.loadAlarm(ctx, ids[i]);
            if (mal.enabled && mal.targetTime > System.currentTimeMillis() + 0l) {
                if (hookTime == -1) {
                    hookTime = mal.targetTime;
                }
                if (mal.targetTime <= hookTime) {
                    hookTime = mal.targetTime;
                }
            }
        }
        for (int i = 0; i < ids.length; i++) {
            mal = MyAlarm.loadAlarm(ctx, ids[i]);
            if (mal.enabled && mal.targetTime - hookTime >= 0l && mal.targetTime - hookTime < 60l * 1000l) {
                firstAlarmId.add(ids[i]);
            }
        }


        if (hookTime != -1) {
            long[] alarms = new long[firstAlarmId.size()];
            for (int i = 0; i < firstAlarmId.size(); i++) {
                alarms[i] = firstAlarmId.get(i);
            }
            setAlarmClock(ctx,hookTime, alarms);
        } else {
            setAlarmClock(ctx,-1, null);
        }
    }

    public void setAlarmClock(Context ctx,long triggleTime, long[] alarmIds){
        Bundle alarms=new Bundle();
        alarms.putLongArray("alarms",alarmIds);
        setAlarm(ctx,triggleTime,alarms);
    }

}
