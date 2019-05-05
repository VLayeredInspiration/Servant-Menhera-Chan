package com.zyfdroid.smc.abilties;

import android.content.Context;

/**
 * 一目了然 的信息提供类
 */

public abstract class ShortMessageProvider{
    String description;

    public ShortMessageProvider(String description) {
        this.description = description;
    }

    public abstract String getString(Context ctx, AbilityEntry provider);
}
