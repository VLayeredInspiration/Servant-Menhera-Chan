package com.zyfdroid.smc.util;

import android.app.*;
import android.os.*;
import android.content.*;
import android.widget.*;

import java.util.*;

import android.view.View.*;
import android.view.*;

import com.zyfdroid.smc.activity.*;
import com.zyfdroid.smc.*;
import com.zyfdroid.smc.base.*;

public class CharTalkDialog extends Dialog {
    TimerTask timerHandler = null;
    java.util.Timer timerHandlerImpl = null;
    AlarmHint[] dialogue;
    ArrayList<Timer> timers = new ArrayList<Timer>();
    TimerExecuter timerExec = new TimerExecuter();
    Activity currentCtx;

    public CharTalkDialog(Activity ctx, AlarmHint[] dialogues) {
        super(ctx);
        currentCtx = ctx;
        dialogue = dialogues;
    }

    LinearLayout chartalkdialogLinearLayout1;
    ImageView chartalkdialogImageView1;
    TextView chartalkdialogTextView1;

    public void initUi() {
        setContentView(R.layout.char_talk_dialog);
        chartalkdialogLinearLayout1 = (LinearLayout) findViewById(R.id.chartalkdialogLinearLayout1);
        chartalkdialogImageView1 = (ImageView) findViewById(R.id.chartalkdialogImageView1);
        chartalkdialogTextView1 = (TextView) findViewById(R.id.chartalkdialogTextView1);
    }

    @Override
    public void dismiss() {

        timerHandler.cancel();
        timerHandler = null;
        timerHandlerImpl = null;
        dialogue = null;
        timers.clear();
        timers = null;
        super.dismiss();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setCancelable(false);
        initUi();
        //handle timer event
        timerHandler = new TimerTask() {
            @Override
            public void run() {
                for (Timer t : timers) {
                    if (t.enabled) {
                        timerExec.target = t;

                        currentCtx.runOnUiThread(timerExec);
                        Main.d(64);
                    }
                }
            }
        };
        timerHandlerImpl = new java.util.Timer();
        timerHandlerImpl.schedule(timerHandler, 1000, 40);
        Main.d(74);

        //end of timer event
        chartalkdialogLinearLayout1.setClickable(true);
        chartalkdialogLinearLayout1.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View p1, MotionEvent p2) {

                if (p2.getAction() == p2.ACTION_UP) {
                    onClick();
                    return true;
                }
                return false;
            }
        });
        onLoad();

    }

    Timer timer1;

    void onLoad() {
        timer1 = new Timer() {

            @Override
            void onTick() {
                Timer1_Timer();
            }
        };
        timer1.enabled = true;
        timers.add(timer1);
        onClick();
        alphacreasing = 0.03f;
    }

    int ptrCurrent = 0;
    int ptrTextCurrent = 0;
    int textCd = 3;
    int animStatus = 0;//0,wait,1,anim
    float ptrAlphaCurrent = 0f;
    float alphacreasing = 0.08f;

    void Timer1_Timer() {
        if (animStatus - 1 >= 0) {
            Main.d("113");
            ptrAlphaCurrent += alphacreasing;
            if (alphacreasing != 0) {
                if (alphacreasing > 0) {
                    if (ptrAlphaCurrent > 1f) {
                        ptrAlphaCurrent = 1f;
                    }
                }
                if (alphacreasing < 0) {
                    if (ptrAlphaCurrent < 0f) {
                        ptrAlphaCurrent = 0f;
                        if (ptrCurrent >= 0) {
                            alphacreasing = 0.08f;
                        } else {
                            dismiss();
                        }
                    }
                }
            }
            Main.d("130");
            chartalkdialogImageView1.setAlpha(ptrAlphaCurrent);
            if (ptrCurrent < 0) {
                return;
            }

            chartalkdialogTextView1.setText(dialogue[ptrCurrent].hintText.substring(0, ptrTextCurrent));

            if (ptrTextCurrent >= dialogue[ptrCurrent].hintText.length()) {
                ptrAlphaCurrent = 1f;
                chartalkdialogImageView1.setAlpha(ptrAlphaCurrent);
                animStatus = 0;
                alphacreasing = -0.08f;
                ptrCurrent++;
                ptrTextCurrent = 0;
                return;
            }
            Main.d("140");

            textCd--;
            if (textCd < 0) {
                ptrTextCurrent++;
                textCd = 3;
                Main.d("147");
            }
        }
    }

    void onClick() {
        if (ptrCurrent < dialogue.length) {
            if (ptrCurrent != -1)
                chartalkdialogImageView1.setImageResource(drawableFromStr(dialogue[ptrCurrent].imgid));
            animStatus = 1;
        } else {
            ptrCurrent = -1;
            alphacreasing = -0.03f;
            animStatus = 1;
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


}

abstract class Timer {
    boolean enabled = false;

    abstract void onTick();
}

class TimerExecuter implements Runnable {
    public Timer target;

    @Override
    public void run() {

        target.onTick();
    }


}

