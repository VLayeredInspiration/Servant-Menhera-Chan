package com.zyfdroid.smc.activity;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import java.util.*;

import android.util.*;
import android.content.res.*;
import android.widget.SeekBar.*;

import com.zyfdroid.smc.service.*;
import com.zyfdroid.smc.*;
import com.zyfdroid.smc.base.*;
import com.zyfdroid.smc.util.*;

public class ScheduleActivity extends BaseActivity {
    long alarmTime;
    int curalm = 0;
    boolean processed = true;
    public static ScheduleActivity lastInstance = null;

    Vibrator vibrator;
    KeyguardManager mKeyguardManager;
    // 键盘锁
    public KeyguardManager.KeyguardLock mKeyguardLock;
    // 电源管理器
    public PowerManager mPowerManager;
    // 唤醒锁
    public PowerManager.WakeLock mWake;
    long[] alarms;
    long[] normpattern = {0, 1500, 500};
    long[] morningpattern = {0, 70, 578, 62, 1757, 70, 485, 70, 1305, 229, 879, 210, 1053, 638, 1060, 719, 1970, 747, 503, 760, 314, 103, 72, 106, 80, 86, 84, 90, 76, 88, 82, 76, 94, 81, 70, 112, 217, 864, 569, 812, 474, 67, 89, 101, 68, 109, 72, 106, 104, 83, 91, 105, 119, 98, 93, 99, 391, 752, 667, 5871, 2466, 99, 149, 121, 1444, 105, 107, 106, 1096, 79, 85, 87, 57, 142, 1134, 93, 65, 98, 117, 88, 66, 127};

    @Override
    public void onPrepareUi() {
        lastInstance = this;
        alarms = getIntent().getLongArrayExtra("alarms");
        if (null == alarms) {
            finish();
            Main.e(new NullPointerException("alarms is null"));
            return;
        }
        Main.d("start scheduleActivity, length=" + alarms.length);
        processed = true;
        super.onPrepareUi();
        if (null == MaimService.curctx) {
            startService(new Intent(this, MaimService.class));
        }
        setContentView(R.layout.schedule);
        initUi();
        reReadAlarm();
        alarmTime = System.currentTimeMillis();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mKeyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);

        unlock();
        FPS = 1;
        curalm = 0;

        if (null != alarms && alarms.length != 0) {
            displayalarm(curalm);
            try {
                MaimService.curctx.cancelHang();
                Intent i = this.getIntent();
                MaimService.curctx.setHangHint(R.drawable.hint, "提醒正在进行(点击查看)", i, System.currentTimeMillis() + 55000l);
                MaimService.curctx.changeNotifican(-1, null, null);
            } catch (Exception e) {
                Main.e(e);
            }
        } else {
            processed = true;
            finishAndRemoveTask();
        }

    }


    int drawableFromStr(String id) {
        try {
            return R.drawable.class.getDeclaredField(id).getInt(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return R.drawable.hide;
    }


    void displayalarm(int almidx) {
        vibrator.cancel();
        MyAlarm mal = MyAlarm.loadAlarm(this, alarms[almidx]);

        if (System.currentTimeMillis() - mal.targetTime > -500l || isCheck()) {


            scheduleTypeIcon.setImageResource(mal.getTypeIcon());

            scheduleTypeTitle.setText(l(mal.getTypeTitle()));
            AlarmHint ah = mal.getTipString();
            scheduleCharImage.setImageResource(drawableFromStr(ah.imgid));
            scheduleTextView.setText(l(ah.hintText));
            scheduleTextDescibe.setText(l(mal.alarmTitle));
            scheduleDelayText.setText("→  推迟" + mal.retryInterval + "分钟提醒");

            scheduleDragger.setProgress(scheduleDragger.getMax() / 2);
            if (mal.scheType == mal.TYPE_SCHE_GETUP) {
                vibrator.vibrate(morningpattern, 0);
            } else {
                vibrator.vibrate(normpattern, 0);

            }
        } else {


            Main.e(new IllegalStateException("Unexpected alarm " + mal.alarmTitle + " at time " + System.currentTimeMillis() + " with target time " + mal.targetTime));
            curalm++;
            if (curalm >= alarms.length) {
                MaimService.curctx.cancelHang();
                processed = true;
                resetCheck();
                finishAndRemoveTask();
            } else {
                displayalarm(curalm);
            }
        }
    }


    void reReadAlarm() {

        long hookTime = -1;
        MyAlarm mal = null;
        ArrayList<Long> firstAlarmId = new ArrayList<Long>();
        long[] ids = MyAlarm.getAllAlarm(this);
        for (int i = 0; i < ids.length; i++) {
            mal = MyAlarm.loadAlarm(this, ids[i]);
            if (mal.enabled && mal.targetTime > System.currentTimeMillis() - 60000l) {
                if (hookTime == -1) {
                    hookTime = mal.targetTime;
                }
                if (mal.targetTime <= hookTime) {
                    hookTime = mal.targetTime;
                }
            }
        }
        for (int i = 0; i < ids.length; i++) {
            mal = MyAlarm.loadAlarm(this, ids[i]);
            if (mal.enabled && mal.targetTime - hookTime == 0) {
                firstAlarmId.add(ids[i]);
            }
        }


        if (hookTime != -1) {
            long[] malarms = new long[firstAlarmId.size()];
            for (int i = 0; i < firstAlarmId.size(); i++) {
                malarms[i] = firstAlarmId.get(i);
            }
            alarms = malarms;
        } else {
            processed = true;
            finishAndRemoveTask();
        }


    }


    TextView scheduleAnimSwipe;
    ImageView scheduleTypeIcon;
    TextView scheduleTypeTitle;
    ImageView scheduleCharImage;
    TextView scheduleTextView;
    TextView scheduleTextDescibe;
    TextView scheduleDelayText;
    SeekBar scheduleDragger;

    @SuppressLint("ClickableViewAccessibility")
    public void initUi() {
        alarmTime = System.currentTimeMillis();
        scheduleAnimSwipe = (TextView) findViewById(R.id.scheduleAnimSwipe);
        scheduleTypeIcon = (ImageView) findViewById(R.id.scheduleTypeIcon);
        scheduleTypeTitle = (TextView) findViewById(R.id.scheduleTypeTitle);
        scheduleCharImage = (ImageView) findViewById(R.id.scheduleCharImage);
        scheduleTextView = (TextView) findViewById(R.id.scheduleTextView);
        scheduleTextDescibe = (TextView) findViewById(R.id.scheduleTextDescibe);
        scheduleDelayText = (TextView) findViewById(R.id.scheduleTextMinuteDelay);
        scheduleDragger = (SeekBar) findViewById(R.id.scheduleDragger);
        scheduleDragger.setMax(1440);
        scheduleDragger.setProgress(scheduleDragger.getMax());
        scheduleDragger.setOnTouchListener(new View.OnTouchListener() {
            boolean canAction = false;

            @Override
            public boolean onTouch(View p1, MotionEvent p2) {
                if (p2.getAction() == p2.ACTION_DOWN) {
                    if (p2.getX() < p1.getWidth() / 2 + p1.getHeight() / 2 && p2.getX() > p1.getWidth() / 2 - p1.getHeight() / 2) {
                        canAction = false;
                    } else {
                        canAction = true;
                    }
                }
                return canAction;
            }
        });
        scheduleDragger.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar p1, int p2, boolean p3) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar p1) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar p1) {


                if (p1.getProgress() < p1.getMax() / 12) {
                    confirmAlarm(p1);
                    return;
                }
                if (p1.getProgress() > p1.getMax() / 12 * 11) {
                    delayAlarm(p1);
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    scheduleDragger.setProgress(scheduleDragger.getMax() / 2, true);
                } else {
                    scheduleDragger.setProgress(scheduleDragger.getMax() / 2);
                }
            }


        });
    }

    public void delayAlarm(View p1) {

        MyAlarm mal = MyAlarm.loadAlarm(this, alarms[curalm]);
        mal.targetTime = System.currentTimeMillis() + ((long) mal.retryInterval) * 60l * 1000l;
        mal.delays = 0;
        mal.saveAlarm(this, alarms[curalm]);
        if (alarms.length - 1 > curalm) {
            curalm += 1;
            displayalarm(curalm);
        } else {
            processed = true;
            resetCheck();
            MaimService.curctx.cancelHang();
            finishAndRemoveTask();
        }

    }
    @Override
    public void onUiPrepared() {
        super.onUiPrepared();
        ImageView clickn_charimg = scheduleCharImage;
        LinearLayout.LayoutParams lap = (LinearLayout.LayoutParams) clickn_charimg.getLayoutParams();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            lap.height = (int) ((float) clickn_charimg.getWidth() / 37f * 32f);
        } else {
            lap.width = (int) ((float) clickn_charimg.getHeight() / 32f * 37f);
        }
        clickn_charimg.setLayoutParams(lap);
    }

    public void confirmAlarm(View p1) {
        MyAlarm mal = MyAlarm.loadAlarm(this, alarms[curalm]);
        mal.targetTime = mal.nextDayTime();
        mal.delays = 0;
        if (mal.type == mal.TYPE_ALARM_ONCE) {
            mal.enabled = false;
        }
        mal.saveAlarm(this, alarms[curalm]);
        if (alarms.length - 1 > curalm) {
            curalm += 1;
            displayalarm(curalm);
        } else {
            MaimService.curctx.cancelHang();
            processed = true;
            resetCheck();
            finishAndRemoveTask();
        }

    }

    int j = 0;

    @Override
    public void onFrame() {
        if ((System.currentTimeMillis() - alarmTime) > 55000l) {
            if (!isCheck()) {
                dismissedAlarm();
            }
        }
        if ((System.currentTimeMillis() - alarmTime > 300)) {
            processed = false;
        }

        j++;
        if (j >= 3) {
            j = 0;
        }
        scheduleAnimSwipe.setText("《" + loopSpace(12 + 6 * j) + "》");
    }

    String loopSpace(int c) {
        String s = "";
        for (int i = 0; i < c; i++) {
            s = s + " ";
        }
        return s;
    }

    @Override
    public void onBackPressed() {
    }

    public void dismissedAlarm() {
        for (int i = 0; i < alarms.length; i++) {
            MyAlarm mal = MyAlarm.loadAlarm(this, alarms[i]);
            if (mal.isImportant() > mal.IMPORTANCE_LOW) {

                if (mal.isImportant() == mal.IMPORTANCE_NORM) {
                    if (mal.delays < 3) {
                        mal.delays += (isCheck() ? 0 : 1);
                        mal.targetTime = mal.nextTime();
                        mal.saveAlarm(this, alarms[i]);
                    } else {
                        mal.delays = 0;
                        mal.saveAlarm(this, alarms[i]);
                        MaimService.curctx.hangStat(CharStatus.IGNORED, System.currentTimeMillis() + 3600000);
                    }
                }
                if (mal.isImportant() == mal.IMPORTANCE_HIGH) {
                    if (mal.delays < 17) {
                        mal.delays += (isCheck() ? 0 : 1);
                        mal.targetTime = mal.nextTime();
                        mal.saveAlarm(this, alarms[i]);
                    } else {
                        mal.delays = 0;
                        mal.saveAlarm(this, alarms[i]);
                        MaimService.curctx.hangStat(CharStatus.IGNORED, System.currentTimeMillis() + 3600000);
                    }
                }
            } else {
                mal.delays = 0;
                mal.targetTime = mal.nextDayTime();
                mal.saveAlarm(this, alarms[i]);
            }
        }
        try {
            Intent i = new Intent(MaimService.curctx, getClass());
            i.putExtra("alarms", alarms);
            raiseChk();
            MaimService.curctx.setHangHint(R.drawable.hint, l("『[称呼]有没有忘记什么事？』查看>"), i, System.currentTimeMillis() + 540000l);
        } catch (Exception e) {
            Main.e(e);
        }
        finishAndRemoveTask();
    }

    @Override
    protected void onDestroy() {
        vibrator.cancel();
        try {
            MaimService.requireNext(this);
        } catch (Exception e) {
            Main.e(e);
        }
        relock();
        lastInstance = null;
        super.onDestroy();
    }

    boolean isLockedBefore = false;

    void unlock() {
        if (!mPowerManager.isScreenOn()) {
            mKeyguardLock = mKeyguardManager.newKeyguardLock("unLock");
            mKeyguardLock.reenableKeyguard();
            mKeyguardLock.disableKeyguard();
            mWake = mPowerManager.newWakeLock
                    (PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "MainService");
            mWake.acquire(60000);
            isLockedBefore = true;
        } else {
            isLockedBefore = false;
        }
    }

    void relock() {
        try {
            if (isLockedBefore) {
                if (mWake != null && !mWake.isHeld()) {
                    mWake.release();
                    mWake = null;
                }
                if (mKeyguardLock != null) {
                    mKeyguardLock.reenableKeyguard();
                }
            }
        } catch (Exception e) {
            Main.e(e);
        }
    }

    boolean isCheck() {
        return !loadstate("sche_ischk").isEmpty();
    }

    void resetCheck() {
        savestate("sche_ischk", "");
    }

    void raiseChk() {
        savestate("sche_ischk", "chk");
    }
}
