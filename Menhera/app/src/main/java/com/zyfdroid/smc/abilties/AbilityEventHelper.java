package com.zyfdroid.smc.abilties;

import android.content.Context;

import com.zyfdroid.smc.Main;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Lenovo on 2019/3/15.
 */

public class AbilityEventHelper {
    public void callNewDay(Context ctx){
        callAbilityEvens(ctx,"onNewDay");
    }
    public void callTimeInterval(Context ctx){
        callAbilityEvens(ctx,"onTimeInterval");
    }
    public void callScreenOn(Context ctx){
        callAbilityEvens(ctx,"onScreenOn");
    }
    public void callScreenOff(Context ctx){
        callAbilityEvens(ctx,"onScreenOff");
    }

    private void callAbilityEvens(Context ctx,String eventName){
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
