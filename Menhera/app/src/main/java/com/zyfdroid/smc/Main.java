package com.zyfdroid.smc;
import android.app.*;
import android.content.*;
import com.zyfdroid.smc.activity.*;
import com.zyfdroid.smc.util.*;

import java.io.*;
import java.util.*;
import com.zyfdroid.smc.base.*;
public class Main extends Application
{
	public static final HashMap<String,FunctionItem> appFunctions=new HashMap<String,FunctionItem>();
	static Context ctx;
	//consted value pool
	public static final String SEX_MALE="♂";
	public static final String SEX_FEMALE="♀";
	public static final String SEX_NONE="？";
	
	public static final int OCUP_STUDENT=0;
	public static final int OCUP_WORKER=1;
	public static final int OCUP_FREE=2;
	//application inpoint
	private static Object synclock=new Object();
	public static final boolean logOn=true;
	    @Override  
	    public void onCreate() {  
		ctx=this;
		if(logOn){
			try
			{
				stdOut = new PrintStream("/sdcard/AppCrashs/log.log");}
			catch (FileNotFoundException e)
			{}
			}
			initFunction(appFunctions);
			super.onCreate();  
			
	
			
				if(true){
		        CrashHandler crashHandler = CrashHandler.getInstance();  
		        crashHandler.init(getApplicationContext());  
				}
		}
		
		public void initFunction(HashMap<String,FunctionItem> f){
			f.put("_alarm",new FunctionItem("提醒",R.drawable.type_getup,AlarmListActivity.class));
			f.put("_pref",new FunctionItem("个性化",R.drawable.ic_preference,PreferenceActivity.class));

		
		}
	public static void d(Object c){
		synchronized(synclock){
		if(!logOn){return;}
		if(null!=stdOut){
			stdOut.print(new Date());
			stdOut.print(":");
			stdOut.println(c.toString());
		}
		}
	}
		public static void e(Exception err){
			synchronized(synclock){
			StringBuilder sb=new StringBuilder("发生错误:");
			sb.append("\n");

			Writer writer = new StringWriter();  
			PrintWriter printWriter = new PrintWriter(writer);  
			err.printStackTrace(printWriter);  
			Throwable cause = err.getCause();  
			while (cause != null) {  
				cause.printStackTrace(printWriter);  
				cause = cause.getCause();  
			}  
			printWriter.close();  
			sb.append( writer.toString());  
			d(sb.toString());
			}
		}
	public static PrintStream stdOut;
	public static String gs(int id){
		try{
		return ctx.getResources().getString(id);
		}catch(Exception e){
			e(e);
		}
		return "";
	}
}

