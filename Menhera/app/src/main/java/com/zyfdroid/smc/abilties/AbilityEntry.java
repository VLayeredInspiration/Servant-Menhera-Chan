package com.zyfdroid.smc.abilties;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.zyfdroid.smc.Main;
import com.zyfdroid.smc.soul.receiver.CommonAlarmReceiver;

public abstract class AbilityEntry {
    public int icon;
    public String caption;
    public String abilityKey;
    public ShortMessageProvider shortMessageProvider;
    public int GID=-1;

    public IAbilityEventListener mAbilityEventListener;

    public void setAbilityEventListener(IAbilityEventListener mAbilityEventListener) {
        this.mAbilityEventListener = mAbilityEventListener;
    }

    public void setShortMessageProvider(ShortMessageProvider shortMessageProvider) {
        this.shortMessageProvider = shortMessageProvider;
    }

    public abstract void onAbilityInit(Context ctx);

    private Drawable iconDrawableCache;

    public Drawable getIconDrawable(Context ctx) {
        if (iconDrawableCache == null) {
            iconDrawableCache = ctx.getDrawable(icon);
        }
        return iconDrawableCache;
    }

    public String getCaption() {
        return caption;
    }

    public void startActivity(Context ctx, Class activityClass){
        Intent i=new Intent(ctx,activityClass);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        i.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        ctx.startActivity(i);
    }


    public void setAlarm(Context ctx, long timeMillisecond, Bundle datas){

        Intent i=new Intent(ctx, CommonAlarmReceiver.class);
        i.putExtra(CommonAlarmReceiver.ALARM_INFO_BUNDLE_KEY,datas);
        i.putExtra(Main.ABILITY_TAG,abilityKey);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx,GID,i,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager.AlarmClockInfo alarmClockInfo=new AlarmManager.AlarmClockInfo(timeMillisecond,pendingIntent);
    }

}
