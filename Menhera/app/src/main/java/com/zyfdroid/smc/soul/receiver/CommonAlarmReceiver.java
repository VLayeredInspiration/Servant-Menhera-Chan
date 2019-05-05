package com.zyfdroid.smc.soul.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.zyfdroid.smc.Main;
import com.zyfdroid.smc.abilties.AbilityEntry;
import com.zyfdroid.smc.abilties.AbilityManager;


public class CommonAlarmReceiver extends BroadcastReceiver {
    public static final String ALARM_INFO_BUNDLE_KEY="alarmInfoBundle";
    @Override
    public void onReceive(Context context, Intent intent) {
        String key=intent.getStringExtra(Main.ABILITY_TAG);
        if(AbilityManager.servantAbilities.containsKey(key)){
            AbilityEntry ability=AbilityManager.servantAbilities.get(key);
            if(null!=ability.mAbilityEventListener){
                ability.mAbilityEventListener.onAlarm(context,intent.getBundleExtra("value"));
            }
            else
            {
                Main.e(new Exception("Ability: "+key +" does not contains event listener"));
            }
        }
        else
        {
            Main.e(new Exception("Abilities do not contains such Ability: "+key));
        }
    }
}
