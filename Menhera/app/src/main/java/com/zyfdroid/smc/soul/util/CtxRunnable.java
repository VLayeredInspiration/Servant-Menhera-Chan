package com.zyfdroid.smc.soul.util;
import android.content.*;

public abstract class CtxRunnable implements Runnable
{
	Context ctx;
	public CtxRunnable(Context ctx) {
		this.ctx = ctx;
	}
}
