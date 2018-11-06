package com.zyfdroid.smc.base;
public interface IFunctionEvent{
	public abstract void onNewDay();
	public abstract void onTimeInterval();
	public abstract void onAlarm();
	public abstract void onScreenOn();
	public abstract void onFunctionClick();
}
