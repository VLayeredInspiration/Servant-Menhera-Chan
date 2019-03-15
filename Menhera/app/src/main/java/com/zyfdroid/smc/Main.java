package com.zyfdroid.smc;

import android.app.*;
import android.content.*;

import com.zyfdroid.smc.abilties.AbilityEntry;
import com.zyfdroid.smc.abilties.AbilityManager;
import com.zyfdroid.smc.abilties.schedule.Schedule;
import com.zyfdroid.smc.abilties.preference.Settings;
import com.zyfdroid.smc.util.*;

import java.io.*;
import java.util.*;

public class Main extends Application {

    static Context ctx;



    public static final String SEX_MALE = "♂";
    public static final String SEX_FEMALE = "♀";
    public static final String SEX_NONE = "？";
    public static final int OCUP_STUDENT = 0;
    public static final int OCUP_WORKER = 1;
    public static final int OCUP_FREE = 2;

    public static final String ABILITY_TAG="abilityTag";

    private static Object synclock = new Object();
    public static final boolean logOn = true;

    @Override
    public void onCreate() {
        ctx = this;
        initLogs();
        AbilityManager.onAppInited(this);
        super.onCreate();
        if (true) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(getApplicationContext());
        }
    }

    private void initLogs(){
        if (logOn) {
            try {
                stdOut = new PrintStream("/sdcard/AppCrashs/log.log");
            } catch (FileNotFoundException e) {
            }
        }
    }


    public static void d(Object c) {
        synchronized (synclock) {
            if (!logOn) {
                return;
            }
            if (null != stdOut) {
                stdOut.print('[');
                stdOut.print(new Date().toLocaleString());
                stdOut.print("]");
                stdOut.print("[");

                StackTraceElement[] stes = Thread.currentThread().getStackTrace();
                if (stes.length > 3) {
                    stdOut.print(stes[3].getFileName());
                    stdOut.print(':');
                    stdOut.print(stes[3].getLineNumber());
                    stdOut.print(']');
                }

                stdOut.println(c.toString());
            }
        }
    }

    public static void e(Exception err) {
        synchronized (synclock) {
            StringBuilder sb = new StringBuilder("发生错误:");
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
            sb.append(writer.toString());
            d(sb.toString());
        }
    }

    public static PrintStream stdOut;

    public static String gs(int id) {
        try {
            return ctx.getResources().getString(id);
        } catch (Exception e) {
            e(e);
        }
        return "";
    }
}

