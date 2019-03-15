package com.zyfdroid.smc.abilties;

import android.content.Context;

/**
 * Created by Lenovo on 2019/3/14.
 */

public abstract class ShortMessageProvider<T extends AbilityEntry> {
    String description;

    public ShortMessageProvider(String description) {
        this.description = description;
    }

    public abstract void getString(Context ctx, T provider);
}
