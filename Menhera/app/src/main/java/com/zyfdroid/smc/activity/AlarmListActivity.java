package com.zyfdroid.smc.activity;

import android.view.*;
import android.app.*;
import android.content.*;
import android.widget.*;
import java.util.*;
import android.widget.AdapterView.*;
import com.zyfdroid.smc.base.*;
import com.zyfdroid.smc.*;
import com.zyfdroid.smc.util.*;
import com.zyfdroid.smc.service.*;

public class AlarmListActivity extends BaseActivity
{

	@Override
	public void onPrepareUi()
	{
		
		setContentView(R.layout.alarm_list);
		ctx=this;
	}
	public void addAlarmClick(View p1){
		AlertDialog.Builder adb=new AlertDialog.Builder(this);
		adb.setTitle("是什么样的提醒？:");
		
		adb.setAdapter(new AADP(this), new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					
					onChooseAlarm(p2);
				}
			});
			adb.setNegativeButton("手滑了",null);
			adb.create().show();
			showHelp("什么是重要提醒?:","如果[称呼]没有注意到重要提醒的话，我每隔十分钟会再次提醒，直到[称呼]能注意到为止。但是我最多也只能提醒三次(之后的提醒肯定都来不及了啦！)",R.drawable.confused,"help_important");
	}
	
	void onChooseAlarm(int type){
		Intent i=new Intent(this,AlarmEditActivity.class);
		i.putExtra("currentAlarmId",System.currentTimeMillis());
		i.putExtra("alarmType",type);
		startActivity(i);
	}
	
	void onAlarmClick(long almid){
		Intent i=new Intent(this,AlarmEditActivity.class);
		i.putExtra("currentAlarmId",almid);
		startActivity(i);
	}
	long longclickedItemId=0;
	void onAlarmLongClick(long almid){
		longclickedItemId=almid;
		AlertDialog.Builder adb=new AlertDialog.Builder(this);
		adb.setTitle("更改");
		adb.setItems(new String[]{"删除提醒","提前一天","推迟一天","提前15分钟","往后15分钟","恢复原来的时间"}, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					
					onAlarmAction(longclickedItemId,p2);
				}
			});
		adb.create().show();
	}
	
	void onAlarmAction(long alarmId,int action){
		switch(action) {
			case 0:
				{
					getSharedPreferences("alarms",MODE_PRIVATE).edit().remove(String.valueOf(alarmId)).commit();
					tw("提醒已取消");
				}
				break;
			case 1:
				{
					shiftAlarm(alarmId,-86400000l);
				}
				break;
			case 2:
				{
					shiftAlarm(alarmId,86400000l);
				}
				break;
			case 3:
				{
					shiftAlarm(alarmId,-15l*60l*1000l);
				}
				break;
			case 4:
				{
					shiftAlarm(alarmId,15l*60l*1000l);
				}
				break;
			case 5:
				{
					MyAlarm mal=MyAlarm.loadAlarm(this,alarmId);
					mal.delays=0;
					mal.targetTime=mal.nextTime();
					mal.saveAlarm(this,alarmId);
					tw("下次"+mal.getNextStr()+"提醒");
				}
				break;
		}
		loadAlarmList();
		
	}
	
	void shiftAlarm(long alarmId,long shiftTime){
		MyAlarm mal=MyAlarm.loadAlarm(this,alarmId);
		if(mal.targetTime+shiftTime<System.currentTimeMillis()){
			tw("这。。。[称呼]是要穿越吗？");
		}else{
			mal.targetTime+=shiftTime;
			mal.saveAlarm(this,alarmId);
			tw("下次"+mal.getNextStr()+"提醒");
		}
	}

	@Override
	public void onBackPressed()
	{
		
		if(null!=MaimService.curctx){
		MaimService.curctx.checkScheduleWhileOff();
		}else{
			startService(new Intent(this,MaimService.class));
		}
		super.onBackPressed();
	}
	
	
	
	
	
	AlarmAdapter adl;
	void loadAlarmList(){
		adl=new AlarmAdapter(this);
	((ListView)findViewById(R.id.alarmlistListView)).setAdapter(adl);
	
		((ListView)findViewById(R.id.alarmlistListView)).setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					
					onAlarmClick(Long.valueOf(adl.getItem((int)p4)));
				}
			});
		((ListView)findViewById(R.id.alarmlistListView)).setOnItemLongClickListener(new OnItemLongClickListener(){

				@Override
				public boolean onItemLongClick(AdapterView<?> p1, View p2, int p3, long p4)
				{
					
					onAlarmLongClick(Long.valueOf(adl.getItem((int)p4)));
					return true;
				}
			});
	}

	
	
	
	
	
	@Override
	protected void onResume()
	{
		
		super.onResume();
		loadAlarmList();
	}
static AlarmListActivity ctx;
	static void OnRefreshRequired(){
		try{
		ctx.loadAlarmList();
		}catch(Exception e){
			Main.e(e);
		}
	}
	
	
	
	@Override
	public boolean isHiddenTitleBar()
	{
		
		return true;
	}
	
	
	
	
}


class AlarmAdapter extends ArrayAdapter<String>{
public AlarmAdapter(Context ctx){
	super(ctx,R.layout.adp_alarm,MyAlarm.getAllAlarmString(ctx));
}

@Override
public View getView(int position, View convertView, ViewGroup parent)
{
	View v0=LayoutInflater.from(getContext()).inflate(R.layout.adp_alarm,null,false);
	long alarmId=Long.valueOf(getItem(position));
	MyAlarm mal=MyAlarm.loadAlarm(getContext(),alarmId);
	((ImageView)v0.findViewById(R.id.adpalarmTypeImage)).setImageResource(mal.getTypeIcon());
	((TextView)v0.findViewById(R.id.adpalarmTimeText)).setText((mal.hour>9 ? String.valueOf(mal.hour) : "0" + mal.hour) + ":" + (mal.minute>9 ? String.valueOf(mal.minute) : "0" + mal.minute));
	((TextView)v0.findViewById(R.id.adpalarmDetailText)).setText(mal.getTypeTitle()+(mal.alarmTitle.trim().isEmpty() ? "" : ":")+((AlarmListActivity)getContext()).l(mal.alarmTitle));
	((TextView)v0.findViewById(R.id.adpalarmNextTime)).setText("下次将" + mal.getNextStr() + "响起");
	Switch sw=((Switch)v0.findViewById(R.id.adpalarmSwitch));
	sw.setTag(position);
	sw.setChecked(mal.enabled);
	sw.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton p1, boolean p2)
			{
				
				MyAlarm mal=MyAlarm.loadAlarm(getContext(),Long.valueOf(getItem((int)p1.getTag())));
				mal.enabled=p2;
				if(mal.enabled){mal.targetTime=mal.nextTime();}
				mal.saveAlarm(getContext(),Long.valueOf(getItem((int)p1.getTag())));
				
			}
		});
	return v0;
}

}




class AADP extends ArrayAdapter<String>{
	public AADP(Context ctx){
		super(ctx,R.layout.altp,MyAlarm.typeTitles);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		
		LayoutInflater lyf=LayoutInflater.from(getContext());
		View v0=lyf.inflate(R.layout.altp,parent,false);
		((ImageView)v0.findViewById(R.id.altpImageView1)).setImageResource(MyAlarm.typeIcons[position]);
		((TextView)v0.findViewById(R.id.altpTextView1)).setText(MyAlarm.typeTitles[position]+(MyAlarm.importance[position]?"(重要提醒)":""));
		return v0;
	}
	
	
}

