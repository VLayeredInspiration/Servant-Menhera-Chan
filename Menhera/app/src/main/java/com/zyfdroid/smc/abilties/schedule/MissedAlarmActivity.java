package com.zyfdroid.smc.abilties.schedule;

import android.content.*;
import android.view.*;
import android.widget.*;
import android.os.*;

import com.zyfdroid.smc.soul.base.*;
import com.zyfdroid.smc.*;
import com.zyfdroid.smc.soul.service.*;

public class MissedAlarmActivity extends BaseActivity {

    long[] alarmId;
    static MissedAlarmActivity lastctx = null;

    ImageView missedalarmCharImg;
    TextView missedalarmHintText;
    ListView missedalarmList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (null != lastctx) {
            try {
                lastctx.refresh();
                finish();
            } catch (Exception e) {
                Main.e(e);
                lastctx = this;
            }
        } else {
            lastctx = this;
        }
        if (null != ScheduleActivity.lastInstance) {
            ScheduleActivity.lastInstance.finish();
        }
    }

    public void refresh() {
        alarmId = getIntent().getLongArrayExtra("alarms");
        missedalarmList.setAdapter(new AlarmAdapter2(this, toLong(alarmId)));
    }

    public void avoid(View p1) {
        showHelp("如何防止这样的事情再次发生:", "由于有的系统对内存管的太严，我可能偶尔会被系统“赶”出内存。[称呼]需要到各个优化软件以及设置里，手动添加应用到白名单以及允许自启和后台运行。另外，重启手机后要及时叫一下我(点桌面上的图标)\n\n", R.drawable.sorry, "");
    }

    public void initUi() {
        missedalarmCharImg = findViewById(R.id.missedalarmCharImg);
        missedalarmHintText = findViewById(R.id.missedalarmHintText);
        missedalarmList = findViewById(R.id.missedalarmList);
        missedalarmCharImg.setImageResource(R.drawable.sorry2);
        missedalarmHintText.setText(l("真是非常抱歉！我把[称呼]的事情给忘了！"));
    }

    public void onConfirm(View p1) {
        for (int i = 0; i < alarmId.length; i++) {
            MyAlarm mal = MyAlarm.loadAlarm(this, alarmId[i]);
            mal.targetTime = mal.nextDayTime();
            if (mal.type == MyAlarm.TYPE_ALARM_ONCE) {
                mal.enabled = false;
            }
            mal.saveAlarm(this, alarmId[i]);
        }
        try {
            MaimService.mCurrentContext.cancelHang();
        } catch (Exception e) {
        }
        finishAndRemoveTask();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }


    @Override
    public void onPrepareUi() {
        setContentView(R.layout.missed_alarm);
        alarmId = getIntent().getLongArrayExtra("alarms");
        initUi();
        missedalarmList.setAdapter(new AlarmAdapter2(this, toLong(alarmId)));

    }

    @Override
    public void onUiPrepared() {

        super.onUiPrepared();
        Vibrator vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(1000);

    }

    @Override
    public void onFrame() {

    }

    @Override
    protected void onDestroy() {

        lastctx = null;
        super.onDestroy();
    }


    public Long[] toLong(long[] l) {
        Long[] ls = new Long[l.length];
        for (int i = 0; i < l.length; i++) {
            ls[i] = l[i];
        }
        return ls;
    }

    public long[] tolong(Long[] l) {
        long[] ls = new long[l.length];
        for (int i = 0; i < l.length; i++) {
            ls[i] = l[i];
        }
        return ls;
    }

    class AlarmAdapter2 extends ArrayAdapter<Long> {
        public AlarmAdapter2(Context ctx, Long[] datas) {
            super(ctx, R.layout.adapter_alarm_norm, datas);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.adapter_alarm_norm, parent, false);
            TextView adpalarm2TimeText = v.findViewById(R.id.adpalarm2TimeText);
            TextView adpalarm2DetailText = v.findViewById(R.id.adpalarm2DetailText);
            TextView adpalarm2PassTime = v.findViewById(R.id.adpalarm2PassTime);
            ImageView adpalarm2TypeIcon = v.findViewById(R.id.adpalarm2TypeImage);
            MyAlarm mal = MyAlarm.loadAlarm(getContext(), getItem(position));
            adpalarm2TypeIcon.setImageResource(mal.getTypeIcon());
            adpalarm2TimeText.setText(mal.getNextStr());
            adpalarm2DetailText.setText(mal.getTypeTitle() + (mal.alarmTitle.trim().isEmpty() ? "" : ":") + ((MissedAlarmActivity) getContext()).l(mal.alarmTitle));
            adpalarm2PassTime.setText("超时" + timeToText(System.currentTimeMillis() - mal.targetTime));
            return v;
        }

        String timeToText(long t) {
            long tolSec = t / 1000;
            long scs = tolSec % 60;
            long msc = (tolSec / 60) % 60;
            long hrs = tolSec / 3600;
            StringBuilder sb = new StringBuilder();
            if (hrs != 0) {
                sb.append(hrs).append("小时");
            }
            if (msc != 0) {
                sb.append(msc).append("分");
            }
            sb.append(scs).append("秒");
            return sb.toString();
        }

        String tobit(String instr) {
            return instr.length() > 1 ? instr : "0" + instr;
        }

    }

}