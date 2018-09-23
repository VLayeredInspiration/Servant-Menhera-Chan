package com.zyfdroid.smc.activity;

import android.view.*;
import android.widget.*;
import android.content.*;
import android.app.*;
import java.util.*;
import android.support.v4.content.res.*;
import android.content.res.*;
import android.view.View.*;
import android.widget.LinearLayout.*;
import android.view.animation.*;
import android.util.*;
import com.zyfdroid.smc.base.*;
import com.zyfdroid.smc.*;
import com.zyfdroid.smc.util.*;
import com.zyfdroid.smc.service.*;

public class MenheraActivity extends BaseActivity
{
	ImageView clickn_charimg;
	TextView clickn_charsay;
	LinearLayout baseView;
	
	@Override
	public void onPrepareUi()
	{
		// TODO: Implement this method
		
		//getWindow().setWindowAnimations(R.style.anim_menhera);
		overridePendingTransition(R.anim.appear,R.anim.disappear);
		setContentView(R.layout.notificanclick);
		initUi();
		Animation anime=AnimationUtils.loadAnimation(this,R.anim.enter);
		anime.setInterpolator(this,android.R.anim.overshoot_interpolator);
		baseView.startAnimation(anime);
		CharStatus cst=CharStatus.NORM1;
		try{
		if(null!=MaimService.curctx.status){
			cst=MaimService.curctx.status;
		}
		}catch(NullPointerException e){
			Main.e(e);
			startService(new Intent(this,MaimService.class));
		}
		fti(R.id.clickn_charsay).setText(l(cst.clicknText));
		fii(R.id.clickn_charimg).setImageResource(cst.clicknImg);
		if(cst.equals(CharStatus.NORM_DOZE)){
			MaimService.curctx.hangStat(CharStatus.NORM_INAT,System.currentTimeMillis()+3l*60l*1000l);
		}
		
		if(cst.equals(CharStatus.NIGHT)){
			MaimService.curctx.hangStat(CharStatus.NIGHT_WAKE,System.currentTimeMillis()+4l*60l*1000l);
		}
		
		
	}

	
	@Override
	public void onUiPrepared()
	{
		// TODO: Implement this method
		CharStatus cst=CharStatus.NORM1;
		try{
			if(null!=MaimService.curctx.status){
				cst=MaimService.curctx.status;
			}
		}catch(NullPointerException e){
			Main.e(e);
			startService(new Intent(this,MaimService.class));
		}
		if(cst.equals(CharStatus.IGNORED)){
			MaimService.curctx.stattilltime=-1;
			MaimService.curctx.changeNotifican(-1,null,null);
		}
		
		LinearLayout.LayoutParams lap=(LinearLayout.LayoutParams)clickn_charimg.getLayoutParams();
		if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
			lap.height=(int)((float)clickn_charimg.getWidth()/37f*32f);
		}else{
			lap.width=(int)((float)clickn_charimg.getHeight()/32f*37f);
		}/**/
		clickn_charimg.setLayoutParams(lap);
	}
	

	public void initUi(){
		baseView=(LinearLayout)findViewById(R.id.menheraBaseView);
		clickn_charimg=(ImageView)findViewById(R.id.clickn_charimg);
		clickn_charsay=(TextView)findViewById(R.id.clickn_charsay);
		ArrayList<HashMap<String,Object>> gridItems=new ArrayList<HashMap<String,Object>>();
		initFunction(gridItems);
		String[] datatags={"icon","text"};
		int[] applyContent={R.id.adpfunctionImageView,R.id.adpfunctionTextView1};
		
			
		
		SimpleAdapter gridAdapter=new SimpleAdapter(this,gridItems,R.layout.adp_function,datatags,applyContent);
		GridView funcView=(GridView)findViewById(R.id.notificanclickGridView);
		funcView.setAdapter(gridAdapter);
		funcView.setOnItemClickListener(new GridView.OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					// TODO: Implement this method
					HashMap<String,Object> item=(HashMap<String,Object>)p1.getAdapter().getItem(p3);
					onIntentedOnClickListener(Main.appFunctions.get((String)item.get("tag")).activityClass);
				}
			});
		if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
			changeGridView(funcView,gridItems);
		}
	}
	private void changeGridView(GridView mContentGv,List datas) {
        // item宽度
        int itemWidth = dp2px(this, 70);
        // item之间的间隔
        int itemPaddingH =dp2px(this, 1);
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
	private void initFunction(ArrayList<HashMap<String,Object>> f){
		String[] funcs={"_alarm","_pref"};
		//TODO:完成自定义功能区，加载功能
		
		
		
		
		
		for(int i=0;i<funcs.length;i++){
			FunctionItem curf=Main.appFunctions.get(funcs[i]);
			HashMap<String,Object> data=new HashMap<String,Object>();
			data.put("icon",curf.icon);
			data.put("text",curf.caption);
			data.put("tag",funcs[i]);
			f.add(data);
		}
		
	}
boolean misFinishing=false;
	@Override
	public void onBackPressed()
	{
		if(!misFinishing){
			
			// A EasterEgg
			if(Math.random()<0.1&&(MaimService.curctx.status==CharStatus.NORM3||MaimService.curctx.status==CharStatus.NORM2||MaimService.curctx.status==CharStatus.NORM1)){
				clickn_charimg.setImageResource(R.drawable.jogging);
				clickn_charsay.setText("溜了溜了～");
			}
			
			
		Animation anime=(AnimationUtils.loadAnimation(this,R.anim.exit));
		anime.setAnimationListener(new Animation.AnimationListener(){

				@Override
				public void onAnimationStart(Animation p1)
				{
					// TODO: Implement this method
				}

				@Override
				public void onAnimationEnd(Animation p1)
				{
					// TODO: Implement this method
					finish();
				}

				@Override
				public void onAnimationRepeat(Animation p1)
				{
					// TODO: Implement this method
				}
			});
			anime.setInterpolator(this,android.R.anim.anticipate_interpolator);
			baseView.startAnimation(anime);
			misFinishing=true;
		}
	}
	public void onIntentedOnClickListener(Class<Activity> clicki){
		if(null!=clicki){
			Intent clickIntent=new Intent(this,clicki);
			clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			clickIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
			clickIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
			startActivity(clickIntent);
		}
		
	}
	@Override
	public void finish()
	{
		// TODO: Implement this method
		super.finish();
		overridePendingTransition(R.anim.appear,R.anim.disappear);
	}

	
	
	
	
	
	
}



class IntentedOnClickListener implements View.OnClickListener
{
	Intent clickIntent;
	Context srcctx;
	public IntentedOnClickListener(Context ctx,Class<Activity> clicki){
		if(null!=clicki){
			clickIntent=new Intent(ctx,clicki);
			clickIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			clickIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
			clickIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		}
		srcctx=ctx;
	}
	
	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		if(null!=srcctx&&null!=clickIntent){
			srcctx.startActivity(clickIntent);
		}
	}
	
	
}


