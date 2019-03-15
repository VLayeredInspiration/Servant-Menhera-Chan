package com.zyfdroid.smc.abilties;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public interface IAbilityEventListener {
	public abstract void onNewDay(Context ctx);
	public abstract void onTimeInterval(Context ctx);
	public abstract void onAlarm(Context ctx, Bundle datas);
	public abstract void onScreenOn(Context ctx);
	public abstract void onScreenOff(Context ctx);
	public abstract void onFunctionClick(Context ctx);
}
