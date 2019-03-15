package com.zyfdroid.smc.activity;

import android.annotation.SuppressLint;
import android.view.*;
import android.widget.*;
import android.content.*;

import java.util.*;

import android.content.res.*;
import android.view.animation.*;
import android.util.*;

import com.zyfdroid.smc.abilties.AbilityEntry;
import com.zyfdroid.smc.abilties.AbilityManager;
import com.zyfdroid.smc.base.*;
import com.zyfdroid.smc.*;
import com.zyfdroid.smc.util.*;
import com.zyfdroid.smc.service.*;

public class MenheraActivity extends BaseActivity {
    ImageView clickn_charimg;
    TextView clickn_charsay;
    LinearLayout baseView;

    @Override
    public void onPrepareUi() {
        overridePendingTransition(R.anim.appear, R.anim.disappear);
        setContentView(R.layout.notificanclick);
        initUi();
        Animation anime = AnimationUtils.loadAnimation(this, R.anim.enter);
        anime.setInterpolator(this, android.R.anim.overshoot_interpolator);
        baseView.startAnimation(anime);
        CharStatus cst = CharStatus.NORM1;
        try {
            if (null != MaimService.curctx.status) {
                cst = MaimService.curctx.status;
            }
        } catch (NullPointerException e) {
            Main.e(e);
            startService(new Intent(this, MaimService.class));
        }
        fti(R.id.clickn_charsay).setText(l(cst.clicknText));
        fii(R.id.clickn_charimg).setImageResource(cst.clicknImg);
        if (cst.equals(CharStatus.NORM_DOZE)) {
            MaimService.curctx.hangStat(CharStatus.NORM_INAT, System.currentTimeMillis() + 3l * 60l * 1000l);
        }

        if (cst.equals(CharStatus.NIGHT)) {
            MaimService.curctx.hangStat(CharStatus.NIGHT_WAKE, System.currentTimeMillis() + 4l * 60l * 1000l);
        }


    }


    @Override
    public void onUiPrepared() {
        CharStatus cst = CharStatus.NORM1;
        try {
            if (null != MaimService.curctx.status) {
                cst = MaimService.curctx.status;
            }
        } catch (NullPointerException e) {
            Main.e(e);
            startService(new Intent(this, MaimService.class));
        }
        if (cst.equals(CharStatus.IGNORED)) {
            MaimService.curctx.stattilltime = -1;
            MaimService.curctx.changeNotifican(-1, null, null);
        }

        LinearLayout.LayoutParams lap = (LinearLayout.LayoutParams) clickn_charimg.getLayoutParams();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            lap.height = (int) ((float) clickn_charimg.getWidth() / 37f * 32f);
        } else {
            lap.width = (int) ((float) clickn_charimg.getHeight() / 32f * 37f);
        }
        clickn_charimg.setLayoutParams(lap);
    }


    public void initUi() {
        baseView = (LinearLayout) findViewById(R.id.menheraBaseView);
        clickn_charimg = (ImageView) findViewById(R.id.clickn_charimg);
        clickn_charsay = (TextView) findViewById(R.id.clickn_charsay);
        ArrayList<AbilityEntry> gridItems = new ArrayList<>();
        initFunction(gridItems);
        AbilityItemAdapter abilityItemAdapter=new AbilityItemAdapter(this,gridItems);
        GridView funcView = findViewById(R.id.notificanclickGridView);
        funcView.setAdapter(abilityItemAdapter);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            changeGridView(funcView, gridItems);
        }
    }

    private void changeGridView(GridView mContentGv, List datas) {
        // item宽度
        int itemWidth = dp2px(this, 70);
        // item之间的间隔
        int itemPaddingH = dp2px(this, 1);
        int size = datas.size();
        // 计算GridView宽度
        int gridviewWidth = size * (itemWidth + itemPaddingH);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
        mContentGv.setLayoutParams(params);
        mContentGv.setColumnWidth(itemWidth);
        mContentGv.setHorizontalSpacing(itemPaddingH);
        mContentGv.setStretchMode(GridView.NO_STRETCH);
        mContentGv.setNumColumns(size);
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    private void initFunction(ArrayList<AbilityEntry> f) {
        String[] funcs = {"_alarm", "_pref"};
        //TODO:完成自定义功能区，加载功能

        for (int i = 0; i < funcs.length; i++) {
            AbilityEntry ability = AbilityManager.servantAbilities.get(funcs[i]);
            f.add(ability);
        }

    }

    boolean misFinishing = false;

    @Override
    public void onBackPressed() {
        if (!misFinishing) {

            // A EasterEgg
            if (Math.random() < 0.1 && (MaimService.curctx.status == CharStatus.NORM3 || MaimService.curctx.status == CharStatus.NORM2 || MaimService.curctx.status == CharStatus.NORM1)) {
                clickn_charimg.setImageResource(R.drawable.jogging);
                clickn_charsay.setText("溜了溜了～");
            }


            Animation anime = (AnimationUtils.loadAnimation(this, R.anim.exit));
            anime.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation p1) {

                }

                @Override
                public void onAnimationEnd(Animation p1) {

                    finish();
                }

                @Override
                public void onAnimationRepeat(Animation p1) {

                }
            });
            anime.setInterpolator(this, android.R.anim.anticipate_interpolator);
            baseView.startAnimation(anime);
            misFinishing = true;
        }
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.appear, R.anim.disappear);
    }
}

class AbilityItemAdapter extends BaseAdapter{
    private ArrayList<AbilityEntry> abilities;
    private Context ctx;

    public AbilityItemAdapter(Context ctx,ArrayList<AbilityEntry> abilities) {
        this.abilities = abilities;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return abilities.size();
    }

    @Override
    public AbilityEntry getItem(int position) {
        return abilities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") View gridItem=LayoutInflater.from(ctx).inflate(R.layout.adp_function,null,false);
        AbilityEntry ability=getItem(position);
        ((ImageView) gridItem.findViewById(R.id.adpfunctionImageView)).setImageDrawable(ability.getIconDrawable(ctx));
        ((TextView) gridItem.findViewById(R.id.adpfunctionTextView1)).setText(ability.getCaption());
        gridItem.setOnClickListener(new AbilityOnClickListener(ctx,ability));
        return gridItem;
    }
}


class AbilityOnClickListener implements View.OnClickListener {
    AbilityEntry ability;
    Context srcctx;

    public AbilityOnClickListener(Context ctx, AbilityEntry ability) {
        this.ability = ability;
        srcctx = ctx;
    }

    @Override
    public void onClick(View p1) {
        if (!(ability.mAbilityEventListener == null)) {
            ability.mAbilityEventListener.onFunctionClick(srcctx);
        }
    }
}


