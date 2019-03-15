package com.zyfdroid.smc.abilties;

import android.content.Context;

import com.zyfdroid.smc.abilties.preference.Settings;
import com.zyfdroid.smc.abilties.schedule.Schedule;

import java.util.HashMap;

/**
 * Created by Lenovo on 2019/3/15.
 */

public class AbilityManager {
    public static final HashMap<String, AbilityEntry> servantAbilities = new HashMap<>();

    public static void onAppInited(Context appContext){
        preInitAbility(servantAbilities);
        initAbilities(appContext);
    }

    private static void preInitAbility(HashMap<String, AbilityEntry> f) {
        f.put("_alarm", new Schedule());
        f.put("_pref", new Settings());
    }

    private static void initAbilities(Context ctx){
        int gid=0;
        for(String key : servantAbilities.keySet()){
            AbilityEntry ability=servantAbilities.get(key);
            ability.abilityKey=key;
            ability.GID=gid;
            gid++;
            ability.onAbilityInit(ctx);
        }
    }

}
