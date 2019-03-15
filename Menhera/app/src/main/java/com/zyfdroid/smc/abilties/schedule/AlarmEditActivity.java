package com.zyfdroid.smc.abilties.schedule;

import android.annotation.SuppressLint;
import android.widget.*;
import android.view.*;

import java.util.*;

import android.app.*;
import android.content.*;
import android.view.View.*;
import android.widget.TimePicker.*;
import android.widget.SeekBar.*;

import com.zyfdroid.smc.base.*;
import com.zyfdroid.smc.*;
import com.zyfdroid.smc.util.*;

public class AlarmEditActivity extends BaseActivity {
    long currentAlarmId;
    MyAlarm currentAlarm;
    private int tarHour;
    private int tarMinute;
    ImageView alarmeditTypeImage;
    TextView alarmeditTextView;
    Switch alarmeditSwitch1;
    RadioGroup alarmeditTypeRadio;
    RadioButton alarmeditRadioTypeSingle;
    RadioButton alarmeditRadioTypeLoop;
    RadioButton alarmeditRadioTypeInterval;
    TextView alarmeditHintText;
    EditText alarmeditTextDescribe;
    TimePicker alarmeditTimePicker1;
    DatePicker alarmeditDatePicker1;
    LinearLayout alarmeditIntervalPannel;
    TextView alarmeditIntervalText;
    SeekBar alarmeditIntervalBar;
    TextView alarmeditOffsetText;
    SeekBar alarmeditOffsetBar;
    TextView alarmeditIntervallyNext;
    LinearLayout alarmeditWeekChoose;
    ToggleButton alarmeditWeek1;
    ToggleButton alarmeditWeek2;
    ToggleButton alarmeditWeek3;
    ToggleButton alarmeditWeek4;
    ToggleButton alarmeditWeek5;
    ToggleButton alarmeditWeek6;
    ToggleButton alarmeditWeek7;
    CheckBox alarmeditWeekDivided;
    CheckBox alarmeditWeekStartNow;
    LinearLayout alarmeditCustomAddition;
    RadioGroup alarmeditImportance;
    RadioButton alarmeditImptLow;
    RadioButton alarmeditImptNorm;
    RadioButton alarmeditImptHigh;
    TextView alarmeditDelayText;
    SeekBar alarmeditDelayBar;
    TextView alarmeditLunarAccording;

    @SuppressLint("ClickableViewAccessibility")
    public void initUi() {
        Date d = new Date();
        tarHour = d.getHours();
        tarMinute = d.getMinutes();
        alarmeditTypeImage = findViewById(R.id.alarmeditTypeImage);
        alarmeditTextView = findViewById(R.id.alarmeditTextView);
        alarmeditSwitch1 = findViewById(R.id.alarmeditSwitch1);
        alarmeditTypeRadio = findViewById(R.id.alarmeditTypeRadio);
        alarmeditRadioTypeSingle = findViewById(R.id.alarmeditRadioTypeSingle);
        alarmeditRadioTypeLoop = findViewById(R.id.alarmeditRadioTypeLoop);
        alarmeditRadioTypeInterval = findViewById(R.id.alarmeditRadioTypeInterval);
        alarmeditHintText = findViewById(R.id.alarmeditHintText);
        alarmeditTextDescribe = findViewById(R.id.alarmeditTextDescribe);
        alarmeditTimePicker1 = findViewById(R.id.alarmeditTimePicker1);
        alarmeditDatePicker1 = findViewById(R.id.alarmeditDatePicker1);
        alarmeditIntervalPannel = findViewById(R.id.alarmeditIntervalPannel);
        alarmeditIntervalText = findViewById(R.id.alarmeditIntervalText);
        alarmeditIntervalBar = findViewById(R.id.alarmeditIntervalBar);
        alarmeditOffsetText = findViewById(R.id.alarmeditOffsetText);
        alarmeditOffsetBar = findViewById(R.id.alarmeditOffsetBar);
        alarmeditIntervallyNext = findViewById(R.id.alarmeditIntervallyNext);
        alarmeditWeekChoose = findViewById(R.id.alarmeditWeekChoose);
        alarmeditWeek1 = findViewById(R.id.alarmeditWeek1);
        alarmeditWeek2 = findViewById(R.id.alarmeditWeek2);
        alarmeditWeek3 = findViewById(R.id.alarmeditWeek3);
        alarmeditWeek4 = findViewById(R.id.alarmeditWeek4);
        alarmeditWeek5 = findViewById(R.id.alarmeditWeek5);
        alarmeditWeek6 = findViewById(R.id.alarmeditWeek6);
        alarmeditWeek7 = findViewById(R.id.alarmeditWeek7);
        alarmeditWeekDivided = findViewById(R.id.alarmeditWeekDivided);
        alarmeditWeekStartNow = findViewById(R.id.alarmeditWeekStartNow);
        alarmeditCustomAddition = findViewById(R.id.alarmeditCustomAddition);
        alarmeditImportance = findViewById(R.id.alarmeditImportance);
        alarmeditImptLow = findViewById(R.id.alarmeditImptLow);
        alarmeditImptNorm = findViewById(R.id.alarmeditImptNorm);
        alarmeditImptHigh = findViewById(R.id.alarmeditImptHigh);
        alarmeditDelayText = findViewById(R.id.alarmeditDelayText);
        alarmeditDelayBar = findViewById(R.id.alarmeditDelayBar);
        alarmeditLunarAccording = findViewById(R.id.alarmeditLunarAccording);


        alarmeditTimePicker1.setVisibility(View.VISIBLE);
        alarmeditIntervalBar.setMax(364);
        alarmeditWeekStartNow.setEnabled(false);
        alarmeditWeekDivided.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton p1, boolean p2) {

                alarmeditWeekStartNow.setEnabled(p2);
                if (p2) {
                    showHelp("什么是隔周提醒？", "当提醒需要区分单周和双周的时候，可以用到它。打开之后可以做到一周提醒一周不提醒。可以用右边的选项决定哪一周提醒，例如如果本周开始提醒的话，就是本周有提醒，下周无提醒，下下周再有提醒，本周无提醒则是下周开始有提醒，下下周无提醒，以此类推。\nP.S.每周从周日开始", R.drawable.confused, "what_is_oddweek");
                }
            }
        });

        alarmeditImportance.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup p1, int p2) {

                if (currentAlarm.scheType == MyAlarm.TYPE_SCHE_CUSTOM) {
                    switch (p2) {
                        case R.id.alarmeditImptLow:
                            currentAlarm.alarmimportance = MyAlarm.IMPORTANCE_LOW;
                            break;
                        case R.id.alarmeditImptNorm:
                            currentAlarm.alarmimportance = MyAlarm.IMPORTANCE_NORM;
                            break;
                        case R.id.alarmeditImptHigh:
                            currentAlarm.alarmimportance = MyAlarm.IMPORTANCE_HIGH;
                            break;
                    }
                }
            }
        });


        alarmeditIntervalBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar p1, int p2, boolean p3) {

                alarmeditOffsetBar.setMax(p2);
                alarmeditIntervalText.setText(String.format("每隔%d天:", p2 + 1));
                currentAlarm.dayInterval = p2 + 1;
                currentAlarm.targetTime = currentAlarm.nextTime();
                alarmeditIntervallyNext.setText(String.format("下一次提醒:%s", currentAlarm.getNextStr()));

            }

            @Override
            public void onStartTrackingTouch(SeekBar p1) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar p1) {

            }
        });
        alarmeditOffsetBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar p1, int p2, boolean p3) {

                alarmeditOffsetText.setText(String.format("偏移%d天:", p2 + 1));
                currentAlarm.dayOffset = p2 + 1;
                currentAlarm.targetTime = currentAlarm.nextTime();
                alarmeditIntervallyNext.setText(String.format("下一次提醒:%s", currentAlarm.getNextStr()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar p1) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar p1) {

            }
        });


        alarmeditDelayBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar p1, int p2, boolean p3) {

                alarmeditDelayText.setText(String.format("推迟%d分钟:", p2 + 1));
                currentAlarm.retryInterval = p2 + 1;
            }

            @Override
            public void onStartTrackingTouch(SeekBar p1) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar p1) {

            }
        });


        View.OnClickListener voc = new View.OnClickListener() {

            @Override
            public void onClick(View p1) {

                onRechooseType();
            }
        };
        alarmeditTypeImage.setOnClickListener(voc);

        alarmeditTextView.setOnClickListener(voc);

        alarmeditTypeRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup p1, int p2) {
                onCheckedChanged(p2);
            }

            public void onCheckedChanged(int clickedId) {

                switch (clickedId) {
                    case R.id.alarmeditRadioTypeSingle:
                        currentAlarm.type = MyAlarm.TYPE_ALARM_ONCE;
                        alarmeditDatePicker1.setVisibility(View.VISIBLE);
                        alarmeditLunarAccording.setVisibility(View.VISIBLE);
                        alarmeditIntervalPannel.setVisibility(View.GONE);
                        alarmeditWeekChoose.setVisibility(View.GONE);
                        alarmeditDatePicker1.init(new Date().getYear() + 1900, currentAlarm.month - 1, currentAlarm.dayofmonth, null);
                        break;
                    case R.id.alarmeditRadioTypeLoop:
                        currentAlarm.type = MyAlarm.TYPE_ALARM_WEEKLY;
                        alarmeditDatePicker1.setVisibility(View.GONE);
                        alarmeditLunarAccording.setVisibility(View.GONE);
                        alarmeditIntervalPannel.setVisibility(View.GONE);
                        alarmeditWeekChoose.setVisibility(View.VISIBLE);
                        alarmeditWeek1.setChecked(currentAlarm.week[0]);
                        alarmeditWeek2.setChecked(currentAlarm.week[1]);
                        alarmeditWeek3.setChecked(currentAlarm.week[2]);
                        alarmeditWeek4.setChecked(currentAlarm.week[3]);
                        alarmeditWeek5.setChecked(currentAlarm.week[4]);
                        alarmeditWeek6.setChecked(currentAlarm.week[5]);
                        alarmeditWeek7.setChecked(currentAlarm.week[6]);

                        break;

                    case R.id.alarmeditRadioTypeInterval:
                        currentAlarm.type = MyAlarm.TYPE_ALARM_INTERVAL;
                        alarmeditDatePicker1.setVisibility(View.GONE);
                        alarmeditLunarAccording.setVisibility(View.GONE);
                        alarmeditWeekChoose.setVisibility(View.GONE);
                        alarmeditIntervalPannel.setVisibility(View.VISIBLE);
                        alarmeditIntervalBar.setProgress(currentAlarm.dayInterval - 1);
                        alarmeditOffsetBar.setProgress(currentAlarm.dayOffset - 1);
                        showHelp("什么是间隔提醒？", "当[称呼]需要一个可以每隔几天提醒一次的提醒时，就可以选用这个。可以选择提醒的间隔天数。下一次的提醒时间会显示在下面，如果对提醒时间不满意，可以左右拖动偏移量使下一次提醒时间合适。", R.drawable.confused, "what_is_interval_alarm");

                        break;


                }
            }
        });

        alarmeditTimePicker1.setOnTimeChangedListener(new OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker p1, int p2, int p3) {

                if (!isTimeListenerLocked) {
                    tarHour = p2;
                    tarMinute = p3;
                    currentAlarm.hour = p2;
                    currentAlarm.minute = p3;
                    if (currentAlarm.type == MyAlarm.TYPE_ALARM_INTERVAL) {
                        alarmeditIntervallyNext.setText("下一次提醒:" + currentAlarm.getNextStr());

                    }
                }
            }
        });
        alarmeditDatePicker1.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View p1, MotionEvent p2) {


                if (p2.getAction() == MotionEvent.ACTION_DOWN) {
                    stopUpdate = false;
                }
                if (p2.getAction() == MotionEvent.ACTION_UP) {
                    stopUpdate = true;
                }

                return false;
            }


        });

    }

    Calendar tempCalendar;
    boolean stopUpdate = false;

    @Override
    public void onFrame() {

        if (null == tempCalendar) {
            tempCalendar = Calendar.getInstance();
        }
        boolean update = false;
        if (alarmeditDatePicker1.getYear() != tempCalendar.get(Calendar.YEAR)) {
            tempCalendar.set(Calendar.YEAR, alarmeditDatePicker1.getYear());
            update = true;
        }
        if (alarmeditDatePicker1.getMonth() != tempCalendar.get(Calendar.MONTH)) {
            tempCalendar.set(Calendar.MONTH, alarmeditDatePicker1.getMonth());
            update = true;
        }
        if (alarmeditDatePicker1.getDayOfMonth() != tempCalendar.get(Calendar.DAY_OF_MONTH)) {
            tempCalendar.set(Calendar.DAY_OF_MONTH, alarmeditDatePicker1.getDayOfMonth());
            update = true;
        }

        if (alarmeditLunarAccording.getVisibility() == View.VISIBLE && update && !stopUpdate) {
            try {
                alarmeditLunarAccording.setFocusable(true);
                alarmeditLunarAccording.setFocusableInTouchMode(true);
                alarmeditLunarAccording.requestFocus();

                alarmeditLunarAccording.setText(dateToText(tempCalendar.getTime()) + "\n农历参考:" + LunarCalendar.solar2Lunar(tempCalendar).toString());
            } catch (Exception e) {
                alarmeditLunarAccording.setText(dateToText(tempCalendar.getTime()) + "\n农历参考:无");
            }
        }
        super.onFrame();
    }

    String dateToText(Date d) {

        StringBuilder sb = new StringBuilder("在");
        sb.append(d.getYear() + 1900).append("年");
        String[] arw = {"日", "一", "二", "三", "四", "五", "六"};
        sb.append(d.getMonth() + 1).append("月");
        sb.append(d.getDate()).append("日");
        sb.append("周").append(arw[d.getDay()]);
        return sb.toString();
    }

    public void intervalminus(View p1) {

        if (alarmeditIntervalBar.getProgress() > 0) {
            alarmeditIntervalBar.setProgress(alarmeditIntervalBar.getProgress() - 1);
        }
    }

    public void intervalplus(View p1) {

        if (alarmeditIntervalBar.getProgress() < 364) {
            alarmeditIntervalBar.setProgress(alarmeditIntervalBar.getProgress() + 1);
        }
    }


    public void delayminus(View p1) {

        if (alarmeditDelayBar.getProgress() > 0) {
            alarmeditDelayBar.setProgress(alarmeditDelayBar.getProgress() - 1);
        }
    }

    public void delayplus(View p1) {

        if (alarmeditDelayBar.getProgress() < 59) {
            alarmeditDelayBar.setProgress(alarmeditDelayBar.getProgress() + 1);
        }
    }


    public void offsetminus(View p1) {

        if (alarmeditOffsetBar.getProgress() > 0) {
            alarmeditOffsetBar.setProgress(alarmeditOffsetBar.getProgress() - 1);
        }
    }

    public void offsetplus(View p1) {


        if (alarmeditOffsetBar.getProgress() < alarmeditOffsetBar.getMax()) {
            alarmeditOffsetBar.setProgress(alarmeditOffsetBar.getProgress() + 1);
        }
    }

    public void chooseCustomImg(View p1) {

        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle(l("怎样来提醒[称呼]？:"));
        adb.setAdapter(new ImgPickAdapter(this), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {
                currentAlarm.alarmImage = ImgPickAdapter.ids[p2];
                mtw("好，就这样提醒[称呼]了。", drawableFromStr(ImgPickAdapter.ids[p2]));
            }
        });
        adb.setNegativeButton("手滑了", null);
        adb.create().show();
    }

    @Override
    public void onUiPrepared() {

        super.onUiPrepared();
        canshowhelp = true;
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


    public void cancelAlarm(View p1) {

        finish();
    }

    public void preventAlarmBWG() {
        tw(l("由于某些原因，[称呼]不能设置这个时间的起床提醒。"));
        //todo :use something instead.
    }

    public void saveAlarm(View p1) {


        if (currentAlarm.scheType == MyAlarm.TYPE_SCHE_GETUP) {
            if (tarHour == 4 && tarMinute == 20) {

                preventAlarmBWG();
                return;
            }
            if (tarHour == 4 && tarMinute == 10) {

                preventAlarmBWG();
                return;
            }
        }


        switch (currentAlarm.type) {
            case MyAlarm.TYPE_ALARM_ONCE:
                currentAlarm.alarmTitle = alarmeditTextDescribe.getText().toString();
                currentAlarm.month = alarmeditDatePicker1.getMonth() + 1;
                currentAlarm.dayofmonth = alarmeditDatePicker1.getDayOfMonth();
                currentAlarm.hour = tarHour;
                currentAlarm.minute = tarMinute;
                currentAlarm.delays = 0;
                currentAlarm.putoff = 0;
                currentAlarm.enabled = alarmeditSwitch1.isChecked();
                currentAlarm.targetTime = currentAlarm.nextTime();
                currentAlarm.saveAlarm(this, currentAlarmId);
                break;
            case MyAlarm.TYPE_ALARM_WEEKLY:
                currentAlarm.alarmTitle = alarmeditTextDescribe.getText().toString();
                currentAlarm.week[0] = alarmeditWeek1.isChecked();
                currentAlarm.week[1] = alarmeditWeek2.isChecked();
                currentAlarm.week[2] = alarmeditWeek3.isChecked();
                currentAlarm.week[3] = alarmeditWeek4.isChecked();
                currentAlarm.week[4] = alarmeditWeek5.isChecked();
                currentAlarm.week[5] = alarmeditWeek6.isChecked();
                currentAlarm.week[6] = alarmeditWeek7.isChecked();
                boolean legal = false;
                for (int j = 0; j <= 6; j++) {
                    legal = legal || currentAlarm.week[j];
                }
                if (!legal) {
                    tw("[称呼]请至少选择一天");
                    return;
                }

                currentAlarm.hour = tarHour;
                currentAlarm.minute = tarMinute;
                currentAlarm.delays = 0;
                currentAlarm.putoff = 0;
                currentAlarm.enabled = alarmeditSwitch1.isChecked();


                if (alarmeditWeekDivided.isChecked()) {
                    currentAlarm.isDivideWeek = true;
                    if (alarmeditWeekStartNow.isChecked()) {
                        currentAlarm.oddWeek = MyAlarm.isOddWeek(System.currentTimeMillis());
                    } else {
                        currentAlarm.oddWeek = !MyAlarm.isOddWeek(System.currentTimeMillis());
                    }
                } else {
                    currentAlarm.isDivideWeek = false;
                }
                currentAlarm.targetTime = currentAlarm.nextTime();
                currentAlarm.saveAlarm(this, currentAlarmId);
                break;
            case MyAlarm.TYPE_ALARM_INTERVAL:

                currentAlarm.alarmTitle = alarmeditTextDescribe.getText().toString();
                currentAlarm.dayInterval = alarmeditIntervalBar.getProgress() + 1;
                currentAlarm.dayOffset = alarmeditOffsetBar.getProgress() + 1;
                currentAlarm.hour = tarHour;
                currentAlarm.minute = tarMinute;
                currentAlarm.delays = 0;
                currentAlarm.putoff = 0;
                currentAlarm.enabled = alarmeditSwitch1.isChecked();
                currentAlarm.targetTime = currentAlarm.nextTime();
                currentAlarm.saveAlarm(this, currentAlarmId);
                break;
        }
        AlarmListActivity.OnRefreshRequired();
        finish();
    }

    public void onRechooseType() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("是什么样的提醒？:");
        adb.setAdapter(new AlarmTypeAdapter(this), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface p1, int p2) {

                onChooseAlarm(p2);
            }
        });
        adb.setNegativeButton("手滑了", null);
        adb.create().show();
    }

    void onChooseAlarm(int type) {
        currentAlarm.scheType = type;
        alarmeditTypeImage.setImageResource(currentAlarm.getTypeIcon());
        alarmeditTextView.setText("提醒: " + currentAlarm.getTypeTitle());
        alarmeditTextDescribe.setHint(MyAlarm.hints[type]);
        alarmeditHintText.setText("提醒说明: " + MyAlarm.hints[type]);


        if (type == MyAlarm.TYPE_SCHE_CUSTOM) {
            alarmeditCustomAddition.setVisibility(View.VISIBLE);
        } else {
            alarmeditCustomAddition.setVisibility(View.GONE);
        }

    }


    @Override
    public void onPrepareUi() {

        FPS = 3;
        canshowhelp = false;
        Date d = new Date();
        currentAlarmId = getIntent().getLongExtra("currentAlarmId", 0);
        setContentView(R.layout.alarm_edit);
        initUi();
        currentAlarm = MyAlarm.loadAlarm(this, currentAlarmId);
        if (null == currentAlarm) {

            currentAlarm = MyAlarm.OnceAlarm(d.getMonth() + 1, d.getDate(), d.getHours(), d.getMinutes());
            currentAlarm.scheType = getIntent().getIntExtra("alarmType", 20);
        }

        alarmeditTypeImage.setImageResource(currentAlarm.getTypeIcon());
        alarmeditTextView.setText("提醒: " + currentAlarm.getTypeTitle());
        alarmeditSwitch1.setChecked(currentAlarm.enabled);
        switch (currentAlarm.type) {
            case MyAlarm.TYPE_ALARM_ONCE:
                alarmeditRadioTypeSingle.setChecked(true);
                alarmeditIntervalPannel.setVisibility(View.GONE);
                alarmeditDatePicker1.setVisibility(View.VISIBLE);
                alarmeditLunarAccording.setVisibility(View.VISIBLE);
                alarmeditWeekChoose.setVisibility(View.GONE);
                alarmeditDatePicker1.init(d.getYear() + 1900, currentAlarm.month - 1, currentAlarm.dayofmonth, null);
                break;
            case MyAlarm.TYPE_ALARM_WEEKLY:

                alarmeditRadioTypeLoop.setChecked(true);
                alarmeditDatePicker1.setVisibility(View.GONE);
                alarmeditLunarAccording.setVisibility(View.GONE);
                alarmeditWeekChoose.setVisibility(View.VISIBLE);
                alarmeditIntervalPannel.setVisibility(View.GONE);
                alarmeditWeek1.setChecked(currentAlarm.week[0]);
                alarmeditWeek2.setChecked(currentAlarm.week[1]);
                alarmeditWeek3.setChecked(currentAlarm.week[2]);
                alarmeditWeek4.setChecked(currentAlarm.week[3]);
                alarmeditWeek5.setChecked(currentAlarm.week[4]);
                alarmeditWeek6.setChecked(currentAlarm.week[5]);
                alarmeditWeek7.setChecked(currentAlarm.week[6]);
                alarmeditWeekDivided.setChecked(currentAlarm.isDivideWeek);
                alarmeditWeekStartNow.setChecked(MyAlarm.isOddWeek(System.currentTimeMillis()) == currentAlarm.oddWeek);
                break;
            case MyAlarm.TYPE_ALARM_INTERVAL:
                alarmeditRadioTypeInterval.setChecked(true);
                alarmeditDatePicker1.setVisibility(View.GONE);
                alarmeditLunarAccording.setVisibility(View.GONE);
                alarmeditWeekChoose.setVisibility(View.GONE);
                alarmeditIntervalPannel.setVisibility(View.VISIBLE);
                alarmeditIntervalBar.setProgress(currentAlarm.dayInterval - 1);
                alarmeditOffsetBar.setProgress(currentAlarm.dayOffset - 1);
                int p2 = currentAlarm.dayInterval;
                alarmeditOffsetBar.setMax(p2);
                alarmeditIntervalText.setText(String.format("每隔%d天:", p2));
                currentAlarm.dayInterval = p2 + 1;
                currentAlarm.targetTime = currentAlarm.nextTime();
                alarmeditIntervallyNext.setText(String.format("下一次提醒:%s", currentAlarm.getNextStr()));

        }
        currentAlarm.delays = 0;
        currentAlarm.putoff = 0;
        isTimeListenerLocked = true;
        alarmeditTimePicker1.setCurrentHour(currentAlarm.hour);
        isTimeListenerLocked = false;
        alarmeditTimePicker1.setCurrentMinute(currentAlarm.minute);

        alarmeditTextDescribe.setText(currentAlarm.alarmTitle.trim());
        alarmeditTextDescribe.setHint(MyAlarm.hints[currentAlarm.scheType]);
        alarmeditHintText.setText(String.format("提醒说明: %s", MyAlarm.hints[currentAlarm.scheType]));
        alarmeditDelayBar.setProgress(currentAlarm.retryInterval - 1);
        alarmeditDelayBar.setMax(59);
        if (currentAlarm.scheType == MyAlarm.TYPE_SCHE_CUSTOM) {
            alarmeditCustomAddition.setVisibility(View.VISIBLE);
            switch (currentAlarm.isImportant()) {
                case MyAlarm.IMPORTANCE_LOW:
                    alarmeditImptLow.setChecked(true);
                    break;
                case MyAlarm.IMPORTANCE_NORM:
                    alarmeditImptNorm.setChecked(true);
                    break;
                case MyAlarm.IMPORTANCE_HIGH:
                    alarmeditImptHigh.setChecked(true);
                    break;
            }


        } else {
            alarmeditCustomAddition.setVisibility(View.GONE);
        }

    }

    boolean isTimeListenerLocked = false;

    @Override
    public boolean isHiddenTitleBar() {

        return true;
    }

}

class ImgPickAdapter extends ArrayAdapter<String> {
    static final String[] ids = {"zimg_1",
            "zimg_2",
            "zimg_3",
            "zimg_4",
            "zimg_5",
            "zimg_6",
            "zimg_7",
            "zimg_8",
            "zimg_9",
            "zimg_10",
            "zimg_11",
            "zimg_12",
            "zimg_13",
            "zimg_14",
            "zimg_15",
            "zimg_16",
            "zimg_17",
            "zimg_18",
            "zimg_19",
            "zimg_20",
            "zimg_21",
            "zimg_22",
            "zimg_23",
            "zimg_24",
            "zimg_25",
            "zimg_26",
            "zimg_27",
            "zimg_28",
            "zimg_29",
            "zimg_30",
            "zimg_31",
            "zimg_32",
            "zimg_33",
            "zimg_34",
            "zimg_35",
            "zimg_36",
            "zimg_37",
            "zimg_38",
            "zimg_39",
            "zimg_40",
            "zimg_41",
            "zimg_42"
    };

    public ImgPickAdapter(Context ctx) {
        super(ctx, R.layout.img, ids);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = LayoutInflater.from(getContext()).inflate(R.layout.img, parent, false);
        ImageView im = v.findViewById(R.id.imgImage);
        im.setImageResource(drawableFromStr(ids[position]));
        return v;
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


}
