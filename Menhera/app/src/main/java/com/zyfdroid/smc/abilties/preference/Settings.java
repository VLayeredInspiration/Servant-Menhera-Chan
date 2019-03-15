package com.zyfdroid.smc.abilties.preference;

import android.content.Context;
import android.os.Bundle;

import com.zyfdroid.smc.R;
import com.zyfdroid.smc.abilties.AbilityEntry;
import com.zyfdroid.smc.abilties.IAbilityEventListener;

/**
 * Created by Lenovo on 2019/3/14.
 */

public class Settings extends AbilityEntry implements IAbilityEventListener {
    @Override
    public void onNewDay(Context ctx) {

    }

    @Override
    public void onTimeInterval(Context ctx) {

    }

    @Override
    public void onAlarm(Context ctx, Bundle data) {

    }

    @Override
    public void onScreenOn(Context ctx) {

    }

    @Override
    public void onScreenOff(Context ctx) {

    }

    @Override
    public void onFunctionClick(Context ctx) {
       startActivity(ctx,PreferenceActivity.class);
    }

    @Override
    public void onAbilityInit(Context ctx) {
        this.icon= R.drawable.ic_preference;
        this.caption="个性化";
        this.setAbilityEventListener(this);
    }


}
