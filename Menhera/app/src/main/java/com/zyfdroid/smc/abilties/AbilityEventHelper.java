package com.zyfdroid.smc.abilties;

import android.content.Context;

import com.zyfdroid.smc.Main;

/**
 * 用来统一调用事件的类
 */

public class AbilityEventHelper {
    public static void callNewDay(Context ctx){callAbilityEvents(ctx,"onNewDay");}
    public static void callTimeInterval(Context ctx){


        callAbilityEvents(ctx,"onTimeInterval");
    }
    public static void callScreenOn(Context ctx){callAbilityEvents(ctx,"onScreenOn");}
    public static void callScreenOff(Context ctx){callAbilityEvents(ctx,"onScreenOff");}

    private static void callAbilityEvents(Context ctx, String eventName){
        for(AbilityEntry ability : AbilityManager.servantAbilities.values()){
            IAbilityEventListener abilityEventListener=ability.mAbilityEventListener;
            if(null!=abilityEventListener){
                try {
                    abilityEventListener.getClass().getDeclaredMethod(eventName,Context.class).invoke(abilityEventListener,ctx);
                } catch (Exception e) {
                    Main.e(e);
                }
            }
        }
    }

}
