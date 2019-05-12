package com.zyfdroid.smc.soul.service;

import android.app.*;
import android.content.*;
import android.os.*;
import android.graphics.*;
import android.support.annotation.RequiresApi;
import android.widget.*;

import android.util.*;
import android.support.v4.app.NotificationCompat;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;

import java.util.*;

import com.zyfdroid.smc.abilties.AbilityEventHelper;
import com.zyfdroid.smc.abilties.AbilityManager;
import com.zyfdroid.smc.abilties.schedule.MissedAlarmActivity;
import com.zyfdroid.smc.abilties.schedule.MyAlarm;
import com.zyfdroid.smc.abilties.schedule.Schedule;
import com.zyfdroid.smc.abilties.schedule.ScheduleActivity;
import com.zyfdroid.smc.soul.receiver.*;
import com.zyfdroid.smc.soul.util.*;
import com.zyfdroid.smc.*;
import com.zyfdroid.smc.soul.activity.*;

public class MaimService extends Service implements Runnable {
    public static String refer = "主人";
    public static int interval = 60000;
    public static int bgcolor = 0xffB9E6FF;
    public static MaimService mCurrentContext;
    private Intent hangIntent;
    McDate d = new McDate();
    private int hangImg;
    private String hangText;
    public ScreenOnReceiver screenOnReceiver;
    private long hangTill = -1;
    public CharStatus status = CharStatus.NORM1;
    // 键盘管理器
    KeyguardManager mKeyguardManager;
    // 键盘锁
    public KeyguardLock mKeyguardLock;
    // 电源管理器
    public PowerManager mPowerManager;
    // 唤醒锁
    public PowerManager.WakeLock mWake;
    public AlarmManager mAlmMgr;
    public static final boolean fg = true;
    public boolean confirmWakeUp = true;
    public long confirmWakeUpTime = -1;

    @Override
    public IBinder onBind(Intent p1) {
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        mCurrentContext = this;
        serviceThread = null;
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        mAlmMgr = (AlarmManager) getSystemService(ALARM_SERVICE);
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        screenOnReceiver = new ScreenOnReceiver();
        registerReceiver(screenOnReceiver, filter);
        Main.d("OnServiceCreated.....................");
    }

    public static Context getGlobalContext(){
        return mCurrentContext;
    }

    public PowerManager.WakeLock mWakeLock = null;


    Thread chkScheduleThread;

    //TODO: bookmark, perform DayStamp

    public void setHangHint(int charset, String text, Intent act, long till) {
        this.hangImg = charset;
        this.hangText = text;
        this.hangIntent = act;
        this.hangTill = till;
        changeNotifican(-1, null, null);
    }

    public void cancelHang() {
        this.hangTill = -1;
        changeNotifican(-1, null, null);
    }

    CharStatus hangstat;
    public long stattilltime = -1;

    public void hangStat(CharStatus cs, long tilltime) {
        hangstat = cs;
        stattilltime = tilltime;
        changeNotifican(-1, null, null);
    }




    public void checkScheduleWhileOff() {
        AbilityEventHelper.callScreenOff(this);


        if (null != chkScheduleThread) {
            chkScheduleThread.interrupt();
            chkScheduleThread = null;
        }
        chkScheduleThread = new Thread(new Runnable() {

            @Override
            public void run() {
                long hookTime = -1;
                MyAlarm mal = null;
                ArrayList<Long> firstAlarmId = new ArrayList<Long>();
                long[] ids = MyAlarm.getAllAlarm(MaimService.this);
                for (int i = 0; i < ids.length; i++) {
                    mal = MyAlarm.loadAlarm(MaimService.this, ids[i]);
                    if (mal.enabled && mal.targetTime > System.currentTimeMillis() + 0l) {
                        if (hookTime == -1) {
                            hookTime = mal.targetTime;
                        }
                        if (mal.targetTime <= hookTime) {
                            hookTime = mal.targetTime;
                        }
                    }
                }
                for (int i = 0; i < ids.length; i++) {
                    mal = MyAlarm.loadAlarm(MaimService.this, ids[i]);
                    if (mal.enabled && mal.targetTime - hookTime >= 0l && mal.targetTime - hookTime < 60l * 1000l) {
                        firstAlarmId.add(ids[i]);
                    }
                }


                if (hookTime != -1) {
                    long[] alarms = new long[firstAlarmId.size()];
                    for (int i = 0; i < firstAlarmId.size(); i++) {
                        alarms[i] = firstAlarmId.get(i);
                    }
                    setAlarmClock(hookTime, alarms);
                } else {
                    setAlarmClock(-1, null);
                }

                changeNotifican(-1, null, null);


                try {
                    if (null != MaimService.this.mWakeLock && MaimService.this.mWakeLock.isHeld()) {
                        MaimService.this.mWakeLock.release();
                    }
                } catch (Exception e) {
                    Main.e(e);
                }

            }


        });
        chkScheduleThread.start();
    }

    @Deprecated
    synchronized void setAlarmClock(long triggleTime, long[] alarmIds) {
        if (null == alarmIds && triggleTime != -1) {
            Main.e(new NullPointerException("alarms is null but trigger time is not -1"));
        }

        Intent i = new Intent(this, ScheduleActivity.class);
        i.putExtra("alarms", alarmIds);
        PendingIntent p = PendingIntent.getActivity(this, 0, i, 0);

        if (triggleTime == -1) {
            mAlmMgr.cancel(p);
            return;
        }
        AlarmManager.AlarmClockInfo aci = new AlarmManager.AlarmClockInfo(triggleTime, p);
        Main.d("An alarm clock is set to " + new Date(triggleTime).toString());
        mAlmMgr.setAlarmClock(aci, aci.getShowIntent());
        //↑一个提醒也不会落下的秘诀
    }


    boolean isLockedBefore;

    public void checkState() {
        d.setTime(System.currentTimeMillis());
        status = CharStatus.NORM1;
        Random r = new Random();
        r.setSeed((System.currentTimeMillis() / 600000l) * 600000l);
        if (r.nextDouble() < 0.2d) {
            status = CharStatus.NORM2;
        }
        if (r.nextDouble() < 0.03d) {
            status = CharStatus.NORM3;
        }
        if (d.getMonth() <= 7 && d.getMonth() >= 5) {
            if (d.getHours() >= 12 && d.getHours() <= 13) {
                status = CharStatus.NORM_DOZE;
            }
        }
        if (d.getHours() >= 22 || d.getHours() < 2) {
            status = CharStatus.NIGHT;
            if (new Random().nextFloat() < 0.03f) {
                status = CharStatus.NIGHT_EASTER;
            }
        }
        if (d.getHours() < 6 && d.getHours() >= 2) {
            status = CharStatus.DEEP_NIGHT;
        }
        changeNotifican(-1, null, null);
    }




    //invoke checking alarms at other context



    void relock() {
        if (isLockedBefore) {
            if (mWake != null) {
                mWake.release();
                mWake = null;
            }
            if (mKeyguardLock != null) {
                mKeyguardLock.reenableKeyguard();
            }
        }
    }

    //TODO Unimplemented method
    String getSubStr() {
        //TODO implements this::
        Schedule s= (Schedule) AbilityManager.servantAbilities.get("_alarm");
        return s.shortMessageProvider.getString(this,s);
        //Temp code

    }

    public void changeNotifican(int avator, String mainstr, String substr) {
        if (Build.VERSION.SDK_INT >= 26) {
            changeNotificationImpl26(avator, mainstr, substr);

        } else {
            changeNotificationImpl21(avator, mainstr, substr);

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void changeNotificationImpl26(int avator, String mainstr, String substr) {

        try {
            bgcolor = Integer.parseInt(getSharedPreferences("settings", MODE_PRIVATE).getString("notification_color", "00ffddf4").trim().toUpperCase(), 16) + 0xff000000;
        } catch (Exception e) {
            Main.e(e);
        }
        refer = getSharedPreferences("settings", MODE_PRIVATE).getString("caller", "主人").trim();
        if (refer.isEmpty()) {
            refer = "主人";
        }

        if (stattilltime > System.currentTimeMillis()) {
            status = hangstat;
        }
        if (avator == -1) {
            avator = status.charImg;
            if (hangTill >= System.currentTimeMillis()) {
                avator = hangImg;
            }
        }
        if (null == mainstr) {
            mainstr = status.barText;
            if (hangTill >= System.currentTimeMillis()) {
                mainstr = hangText;
            }
        }
        if (null == substr) {
            substr = getSubStr();
        }

        if (hangTill > System.currentTimeMillis()) {
            avator = hangImg;
            mainstr = hangText;
        }


        if (mPowerManager.isScreenOn()) {
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            Notification.Builder builder = new Notification.Builder(this);
            //设置状态栏的通知图标
            builder.setSmallIcon(R.drawable.ic_notifican);
            //设置通知栏横条的图标
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
            //禁止用户点击删除按钮删除
            builder.setAutoCancel(false);
            //禁止滑动删除
            builder.setOngoing(true);
            //右上角的时间显示
            builder.setShowWhen(false);
            //设置通知栏的标题内容
            builder.setContentTitle("Menhera");
            builder.setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE);
            RemoteViews rvs = new RemoteViews(getPackageName(), R.layout.remoteview);
            rvs.setTextViewText(R.id.remoteviewTextView1, l(mainstr));
            rvs.setTextViewText(R.id.remoteviewTextView2, l(substr));
            //创建通知
            rvs.setImageViewBitmap(R.id.remoteviewImageView1, BitmapFactory.decodeResource(getResources(), avator));
            rvs.setInt(R.id.remoteviewbg, "setBackgroundColor", bgcolor);
            builder.setStyle(new Notification.BigTextStyle());
            builder.setCustomContentView(rvs);
            builder.setCustomBigContentView(rvs);
            builder.setVisibility(Notification.VISIBILITY_SECRET);
            Intent tgt;
            if (hangTill < System.currentTimeMillis()) {
                tgt = new Intent(this, MenheraActivity.class);
            } else {
                tgt = hangIntent;
            }
            builder.setContentIntent(PendingIntent.getActivity(this, 0, tgt, 0));

            if (android.os.Build.VERSION.SDK_INT >= 26) {


                builder.setChannelId("1");
            }


            Notification notification = builder.build();
            if (fg) {
                notification.flags = notification.flags | notification.FLAG_FOREGROUND_SERVICE;
            }


            mNotifyMgr.notify(4731, notification);
        }


    }
    private void changeNotificationImpl21(int avator, String mainstr, String substr) {


        try {
            bgcolor = Integer.parseInt(getSharedPreferences("settings", MODE_PRIVATE).getString("notification_color", "00ffddf4").trim().toUpperCase(), 16) + 0xff000000;
        } catch (Exception e) {
            Main.e(e);
        }
        refer = getSharedPreferences("settings", MODE_PRIVATE).getString("caller", "主人").trim();
        if (refer.isEmpty()) {
            refer = "主人";
        }

        if (stattilltime > System.currentTimeMillis()) {
            status = hangstat;
        }
        if (avator == -1) {
            avator = status.charImg;
            if (hangTill >= System.currentTimeMillis()) {
                avator = hangImg;
            }
        }
        if (null == mainstr) {
            mainstr = status.barText;
            if (hangTill >= System.currentTimeMillis()) {
                mainstr = hangText;
            }
        }
        if (null == substr) {
            substr = getSubStr();
        }

        if (hangTill > System.currentTimeMillis()) {
            avator = hangImg;
            mainstr = hangText;
        }


        if (mPowerManager.isScreenOn()) {
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            //设置状态栏的通知图标
            builder.setSmallIcon(R.drawable.ic_notifican);
            //设置通知栏横条的图标
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
            //禁止用户点击删除按钮删除
            builder.setAutoCancel(false);
            //禁止滑动删除
            builder.setOngoing(true);
            //右上角的时间显示
            builder.setShowWhen(false);
            //设置通知栏的标题内容
            builder.setContentTitle("Menhera");
            builder.setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE);
            RemoteViews rvs = new RemoteViews(getPackageName(), R.layout.remoteview);
            rvs.setTextViewText(R.id.remoteviewTextView1, l(mainstr));
            rvs.setTextViewText(R.id.remoteviewTextView2, l(substr));
            //创建通知
            rvs.setImageViewBitmap(R.id.remoteviewImageView1, BitmapFactory.decodeResource(getResources(), avator));
            rvs.setInt(R.id.remoteviewbg, "setBackgroundColor", bgcolor);
            builder.setStyle(new NotificationCompat.BigTextStyle());
            builder.setCustomContentView(rvs);
            builder.setCustomBigContentView(rvs);
            builder.setVisibility(NotificationCompat.VISIBILITY_SECRET);
            Intent tgt;
            if (hangTill < System.currentTimeMillis()) {
                tgt = new Intent(this, MenheraActivity.class);
            } else {
                tgt = hangIntent;
            }
            builder.setContentIntent(PendingIntent.getActivity(this, 0, tgt, 0));


            Notification notification = builder.build();
            if (fg) {
                notification.flags = notification.flags | notification.FLAG_FOREGROUND_SERVICE;
            }


            mNotifyMgr.notify(4731, notification);
        }


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Main.d("onStartCommand");
        if (fg) {
            createNotificatiion();
        }
        prepareTask();
        checkScheduleWhileOff();
        checkState();

        return START_STICKY;

    }

    public void resumeTask() {
        AbilityEventHelper.callScreenOn(this);
        prepareTask();
    }

    Handler hdl = new Handler();

    void prepareTask() {
        if (null == serviceThread ||
                serviceThread.interrupted()) {
            serviceThread = null;
            serviceThread = new Thread(this);
            serviceThread.start();
        }
    }

    public void createNotificatiion() {
        //stry{
        bgcolor = Integer.parseInt(getSharedPreferences("settings", MODE_PRIVATE).getString("notification_color", "00ffddf4").trim().toUpperCase(), 16) + 0xff000000;
        //}catch(Exception e){}
        if (Build.VERSION.SDK_INT < 26) {
            //使用兼容版本
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            //设置状态栏的通知图标
            builder.setSmallIcon(R.drawable.ic_notifican);
            //设置通知栏横条的图标
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
            //禁止用户点击删除按钮删除
            builder.setAutoCancel(false);
            //禁止滑动删除
            builder.setOngoing(true);
            //右上角的时间显示
            builder.setShowWhen(false);
            //设置通知栏的标题内容
            builder.setContentTitle("Menhera");
            builder.setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE);
            RemoteViews rvs = new RemoteViews(getPackageName(), R.layout.remoteview);
            rvs.setTextViewText(R.id.remoteviewTextView1, "     ");
            //创建通知
            rvs.setTextViewText(R.id.remoteviewTextView2, "");
            rvs.setImageViewBitmap(R.id.remoteviewImageView1, BitmapFactory.decodeResource(getResources(), R.drawable.hide));
            rvs.setInt(R.id.remoteviewbg, "setBackgroundColor", bgcolor);
            builder.setStyle(new NotificationCompat.BigTextStyle());
            builder.setCustomContentView(rvs);
            builder.setCustomBigContentView(rvs);
            builder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MenheraActivity.class), 0));
            builder.setVisibility(NotificationCompat.VISIBILITY_SECRET);

            Notification notification = builder.build();

            //设置为前台服务
            startForeground(4731, notification);
        } else {
            //使用兼容版本
            Notification.Builder builder = new Notification.Builder(this);
            //设置状态栏的通知图标
            builder.setSmallIcon(R.drawable.ic_notifican);
            //设置通知栏横条的图标
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
            //禁止用户点击删除按钮删除
            builder.setAutoCancel(false);
            //禁止滑动删除
            builder.setOngoing(true);
            //右上角的时间显示
            builder.setShowWhen(false);
            //设置通知栏的标题内容
            builder.setContentTitle("Menhera");
            builder.setDefaults(NotificationCompat.FLAG_ONLY_ALERT_ONCE);
            RemoteViews rvs = new RemoteViews(getPackageName(), R.layout.remoteview);
            rvs.setTextViewText(R.id.remoteviewTextView1, "     ");
            //创建通知
            rvs.setTextViewText(R.id.remoteviewTextView2, "");
            rvs.setImageViewBitmap(R.id.remoteviewImageView1, BitmapFactory.decodeResource(getResources(), R.drawable.hide));
            rvs.setInt(R.id.remoteviewbg, "setBackgroundColor", bgcolor);
            builder.setStyle(new Notification.BigTextStyle());
            builder.setCustomContentView(rvs);
            builder.setCustomBigContentView(rvs);
            builder.setContentIntent(PendingIntent.getActivity(this, 0, new Intent(this, MenheraActivity.class), 0));
            builder.setVisibility(Notification.VISIBILITY_SECRET);
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


            String channelID = "1";

            String channelName = "MenheraChan本体";

            NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_LOW);

            mNotifyMgr.createNotificationChannel(channel);


            builder.setChannelId("1");
            Notification notification = builder.build();

            //设置为前台服务
            startForeground(4731, notification);

        }
    }

    @Override
    public void onDestroy() {

        Main.d("OnDestory");
        if (null != serviceThread) {
            serviceThread.interrupt();
        }
        mCurrentContext = null;
        unregisterReceiver(screenOnReceiver);
        super.onDestroy();
    }

    //long lts = 0;
    public Thread serviceThread = null;

    @Override
    public void run() {
        try {
            while (true) {
                if (mPowerManager.isScreenOn()) {
                    AbilityEventHelper.callTimeInterval(this);
                    checkState();
                } else {
                    serviceThread = null;
                    return;
                }
                Thread.sleep(timeBeforeNextMinute(interval));
            }


        } catch (InterruptedException e) {
            serviceThread = null;
            return;
        } catch (Exception e) {
            Main.e(new AndroidRuntimeException("执行周期任务时发生意料之外的情况",e));
        }
    }

    long timeBeforeNextMinute(long cinterval) {
        return cinterval - System.currentTimeMillis() % cinterval;
    }


    //将文本中的[称呼]替换成设定的称呼
    String l(String s) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < MaimService.refer.length(); i++) {
            sb.append(MaimService.refer.charAt(i)).append("~");
        }
        return s.replace("[称呼]", MaimService.refer).replace("[称呼2]", sb.toString());
    }


}
