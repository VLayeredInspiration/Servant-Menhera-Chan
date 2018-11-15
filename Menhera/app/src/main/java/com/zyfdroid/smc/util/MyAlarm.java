package com.zyfdroid.smc.util;
import java.util.*;
import android.util.*;
import android.content.*;
import android.provider.*;
import com.zyfdroid.smc.*;
import com.zyfdroid.smc.activity.*;
import com.zyfdroid.smc.base.*;
import java.io.*;

public class MyAlarm
{
	public static final int[] typeIcons=new int[]{
		R.drawable.type_morning,
		R.drawable.type_getup,
		R.drawable.type_gotoschool,
		R.drawable.type_gotowork,
		R.drawable.type_food,
		R.drawable.type_food,
		R.drawable.type_food,
		R.drawable.type_gotobed,
		R.drawable.type_nightcall,
		R.drawable.type_birthday,
		R.drawable.type_invitation,
		R.drawable.type_homework,
		R.drawable.type_housework,
		R.drawable.type_meeting,
		R.drawable.type_television,
		R.drawable.type_takething,
		R.drawable.type_game,
		R.drawable.type_call,
		R.drawable.type_sport,
		R.drawable.type_reading,
		R.drawable.type_custom,
		R.drawable.type_medicine
	};
	public static final boolean f=false;
	public static final boolean t=true;
	public static boolean[] importance=new boolean[]{
		f,t,t,t,t,
		f,f,f,f,t,
		t,t,t,t,t,
		t,f,t,f,f,t,t
	};
	public int isImportant(){
		if(scheType!=TYPE_SCHE_CUSTOM){
		return importance[scheType] ? 51:50;
		}
		else{
			return this.alarmimportance;
		}
	}
	
	
	public static final int TYPE_SCHE_MORNING=0;
	public static final int TYPE_SCHE_GETUP=1;
	public static final int TYPE_SCHE_GOTO_SCHOOL=2;
	public static final int TYPE_SCHE_GOTO_WORK=3;
	public static final int TYPE_SCHE_BREAKFAST=4;
	public static final int TYPE_SCHE_LUNCH=5;
	public static final int TYPE_SCHE_SUPPER=6;
	public static final int TYPE_SCHE_GOTO_BED=7;
	public static final int TYPE_SCHE_NIGHT_CALL=8;
	public static final int TYPE_SCHE_BIRTHDAY=9;
	public static final int TYPE_SCHE_INVITATION=10;
	public static final int TYPE_SCHE_HOMEWORK=11;
	public static final int TYPE_SCHE_HOUSEWORK=12;
	public static final int TYPE_SCHE_MEETING=13;
	public static final int TYPE_SCHE_TVPROGRAM=14;
	public static final int TYPE_SCHE_TAKETHING=15;
	public static final int TYPE_SCHE_GAME=16;
	public static final int TYPE_SCHE_CALLTO=17;
	public static final int TYPE_SCHE_SPORT=18;
	public static final int TYPE_SCHE_READING=19;
	public static final int TYPE_SCHE_CUSTOM=20;
	public static final int TYPE_SCHE_MEDICINE=21;
	public long targetTime;
	public static final int TYPE_ALARM_ONCE=100;
	public static final int TYPE_ALARM_WEEKLY=101;
	
	
	
	
	public static final int TYPE_ALARM_INTERVAL=102;

	public static final int IMPORTANCE_LOW=50;
	public static final int IMPORTANCE_NORM=51;
	public static final int IMPORTANCE_HIGH=52;
	
	public int dayInterval=5;
	public int dayOffset=0;
	public int alarmimportance=51;
	
	public int retryInterval=9;// a wise number
	public int alarmImage=R.drawable.zimg_1;
	
	
	public int month;
	public int dayofmonth;
	public boolean[] week=new boolean[7];
	public  int hour;
	public int minute;
	public int type;
	
	public boolean isDivideWeek=false;
	public boolean oddWeek=false;
	
	public int delays=0;
	public int putoff=0;
	public boolean enabled=true;
	public int scheType=20;
	public String alarmTitle="";
	private MyAlarm(){
		Date def=new Date();
		month=def.getMonth()+1;
		dayofmonth=def.getDate();
		hour=def.getHours();
		minute=def.getMinutes();
		type=TYPE_ALARM_ONCE;
		week=new boolean[]{false,true,true,true,true,true,false};
	}
	public static MyAlarm OnceAlarm(int amonth,int adayofmonth,int ahour,int aminute){
		MyAlarm mal=new MyAlarm();
		mal.type=TYPE_ALARM_ONCE;
		mal.hour=ahour;
		mal.minute=aminute;
		mal.dayofmonth=adayofmonth;
		mal.month=amonth;
		mal.enabled=true;
		mal.targetTime=mal.nextTime();
		mal.alarmTitle=" ";
		mal.scheType=-1;
		return mal;
	}
	public void setAlarmContent(int atype,String atitle){
		scheType=atype;
		alarmTitle=atitle;
	}
	
	public static MyAlarm intervalAlarm(int ahour,int aminute,int mdayInterval,int mdayOffset)
	{
		MyAlarm mal=new MyAlarm();
		mal.type=mal.TYPE_ALARM_INTERVAL;
		mal.hour=ahour;
		mal.minute=aminute;
		mal.dayInterval=mdayInterval;
		mal.dayOffset=mdayOffset;
		mal.enabled=true;
		mal.targetTime=mal.nextTime();
		mal.alarmTitle=" ";
		mal.scheType=-1;
		return mal;
	}
	
	public static boolean isOddWeek(long time){
		Date d=new Date(time);
		time=time-((long)d.getTimezoneOffset()*60000l);
		return ((time+4l*86400000l)/(7l*24l*60l*60l*1000l))%2l==0l;

	}
	
	
	public static MyAlarm WeeklyDayAlarm(int ahour,int aminute,boolean[] days){
		MyAlarm mal=new MyAlarm();
		mal.type=TYPE_ALARM_WEEKLY;
		mal.hour=ahour;
		mal.minute=aminute;
		mal.enabled=true;
		if(days.length!=7){
			throw new IllegalStateException("Wrong Length Of Week Array, Required=7,length="+days.length);
		}
		else
		{
			boolean legal=false;
			for(int i=0;i<=6;i++){
				legal=legal||days[i];
			}
			if(!legal){throw new IllegalStateException("No Days Set Yet");}
		}
		System.arraycopy(days,0,mal.week,0,7);
		mal.targetTime=mal.nextTime();
		mal.setAlarmContent(-1," ");
		return mal;
	}
	public long nextTime(){
		Date date=new Date();
		date.setTime(System.currentTimeMillis());
		Calendar cld=Calendar.getInstance();
		switch(type){
			case TYPE_ALARM_ONCE:
				if(!enabled){return -1;}
				cld.setTimeInMillis(System.currentTimeMillis());
				cld.set(cld.HOUR_OF_DAY,hour);
				cld.set(cld.MINUTE,minute);
				cld.set(cld.MONTH,month-1);
				cld.set(cld.DAY_OF_MONTH,dayofmonth);
				cld.set(cld.SECOND,0);
				cld.set(cld.MILLISECOND,0);
				cld.get(cld.MILLISECOND);
				cld.add(cld.MINUTE,delays*retryInterval);
				if(cld.getTimeInMillis()<date.getTime()){
					cld.add(cld.YEAR,1);
					return cld.getTimeInMillis();
				}else
				{
					return cld.getTimeInMillis();
				}
			case TYPE_ALARM_WEEKLY:
				return nextWeekAlarmTime(cld,date);
				
				case TYPE_ALARM_INTERVAL:
					
					{
						return nextIntervalAlarmTime(date.getTime(),hour,minute,dayInterval,dayOffset,delays);
					}
		}
		throw new RuntimeException("Unexpected type:" + type);
	}
	private long nextIntervalAlarmTime(long t,int h,int m,int i,int o,int d){
		Date date=new Date(t);
		long targetDay=((((t/(24l*60l*60l*1000l))/i)-1)*i)*(24l*60l*60l*1000l)+(((long)date.getTimezoneOffset())*60l*1000l)+o*86400000l;
		Calendar cld2=Calendar.getInstance();
		cld2.setTimeInMillis(targetDay);
		cld2.set(cld2.HOUR_OF_DAY,h);
		cld2.set(cld2.MINUTE,m);
		cld2.set(cld2.SECOND,0);
		cld2.set(cld2.MILLISECOND,0);
		while(cld2.getTimeInMillis()<=(t)){
			cld2.add(cld2.DAY_OF_MONTH,i);
			cld2.get(cld2.DAY_OF_YEAR);
			System.out.println(cld2.getTimeInMillis());
		}
		cld2.add(cld2.MINUTE,retryInterval*d);
		return cld2.getTimeInMillis();
	}
	
	public long nextDayTime(){
		delays=0;
		Date date=new Date(System.currentTimeMillis());
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		date.setTime(date.getTime()+86400000l);
		Calendar cld=Calendar.getInstance();
		
		cld.setTimeInMillis(System.currentTimeMillis());
		
		
		switch(type){
			case TYPE_ALARM_ONCE:
				if(!enabled){return -1;}
				cld.set(cld.HOUR_OF_DAY,hour);
				cld.set(cld.MINUTE,minute);
				cld.set(cld.MONTH,month-1);
				cld.set(cld.DAY_OF_MONTH,dayofmonth);
				cld.set(cld.SECOND,0);
				cld.set(cld.MILLISECOND,0);
				cld.get(cld.MILLISECOND);
				//cld.add(cld.MINUTE,delays*10);
				if(cld.getTimeInMillis()<date.getTime()){
					cld.add(cld.YEAR,1);
					return cld.getTimeInMillis();
				}else
				{
					return cld.getTimeInMillis();
				}
			case TYPE_ALARM_WEEKLY:
				return nextWeekAlarmTime(cld,date);
				
			case TYPE_ALARM_INTERVAL:

				{
					return nextIntervalAlarmTime(date.getTime(),hour,minute,dayInterval,dayOffset,delays);
				}
				
				
		}
		throw new RuntimeException("Unexpected type:" + type);
	}
	//在这堆东西出了n个bug之后决定通通推翻重写
	public long nextWeekAlarmTime(Calendar cld,Date date){
		//以周计算的提醒。cld:传入一个日历，date，假定的当前时间，返回下次提醒的时间戳
		if(!enabled){return -1;}
		
		//以防万一
		if(week.length!=7){
			throw new IllegalStateException("Wrong Length Of Week Array, Required=7,length="+week.length);
		}
		else
		{
			boolean legal=false;
			for(int i=0;i<=6;i++){
				legal=legal||week[i];
			}
			if(!legal){throw new IllegalStateException("No Days Set Yet");}
		}
		
		//计算开始
		cld.setTimeInMillis(System.currentTimeMillis());
		cld.set(cld.HOUR_OF_DAY,hour);//千万不要用cld.HOUR，上午下午不分的，害我调试了好久
		cld.set(cld.MINUTE,minute);//首先设置好时间
		cld.add(cld.MINUTE,retryInterval*delays);
		boolean matchTheNeed=false;//循环，判断是否满足需求，满足则打断循环，否则加一天后继续循环
		while(!matchTheNeed){
			boolean result=true;//与下面几个条件进行与运算
			result=result && cld.getTimeInMillis()>date.getTime();//时间判断
			result=result && week[cld.get(cld.DAY_OF_WEEK)-1];//周判断
			result=result && ((!isDivideWeek)||(isOddWeek(cld.getTimeInMillis())==oddWeek));//单双周判断
			matchTheNeed=result;//传回结果
			if(!matchTheNeed){cld.add(cld.DAY_OF_YEAR,1);}
		}
		return cld.getTimeInMillis();
		
	}
	
	
	
	@Override
	public String toString(){
		StringBuilder sbuilder=new StringBuilder("");
		switch(type)
		{
			case TYPE_ALARM_ONCE:
				sbuilder.append(type).append("|");
				sbuilder.append(month).append("|");
				sbuilder.append(dayofmonth).append("|");
				sbuilder.append(hour).append("|");
				sbuilder.append(minute).append("|");
				sbuilder.append(targetTime).append("|");
				sbuilder.append(enabled).append("|");
				sbuilder.append(delays).append("|");

				sbuilder.append(retryInterval).append("|");

				sbuilder.append(scheType).append("|");
				sbuilder.append(alarmTitle.replace("|","")).append("|");
				
				sbuilder.append(alarmimportance).append("|");
				sbuilder.append(retryInterval).append("|");
				sbuilder.append(alarmImage);
				
				
				
				return sbuilder.toString();
			case TYPE_ALARM_WEEKLY:
				sbuilder.append(type).append("|");
				sbuilder.append(hour).append("|");
				sbuilder.append(minute).append("|");
				for(int i=0;i<=6;i++){
					sbuilder.append(week[i]?"1":"0");
				}
				sbuilder.append("|");
				sbuilder.append(targetTime).append("|");
				sbuilder.append(enabled).append("|");
				sbuilder.append(delays).append("|");
				sbuilder.append(retryInterval).append("|");
				sbuilder.append(scheType).append("|");
				sbuilder.append(alarmTitle.replace("|","")).append("|");
				
				
				sbuilder.append(alarmimportance).append("|");
				sbuilder.append(retryInterval).append("|");
				sbuilder.append(alarmImage).append("|");;
				sbuilder.append(isDivideWeek).append("|");
				sbuilder.append(oddWeek);
				return sbuilder.toString();
				
				
			case TYPE_ALARM_INTERVAL:
				
			
				sbuilder.append(type).append("|");
				sbuilder.append(dayInterval).append("|");
				sbuilder.append(dayOffset).append("|");
				sbuilder.append(hour).append("|");
				sbuilder.append(minute).append("|");
				sbuilder.append(targetTime).append("|");
				sbuilder.append(enabled).append("|");
				sbuilder.append(delays).append("|");

				sbuilder.append(retryInterval).append("|");

				sbuilder.append(scheType).append("|");
				sbuilder.append(alarmTitle.replace("|","")).append("|");
				
				sbuilder.append(alarmimportance).append("|");
				sbuilder.append(retryInterval).append("|");
				sbuilder.append(alarmImage);
				
				
				
				
				
				
				return sbuilder.toString();
				
				
				
				
		}

		throw new IllegalStateException("Unknown alarm type");
	}

	public static MyAlarm fromString(String str){
		MyAlarm mal=new MyAlarm();
		
		try{
		if(str.startsWith(String.valueOf(TYPE_ALARM_ONCE))){
			String[] s=str.split("\\|");
			mal.type=Integer.valueOf(s[0]);
			mal.month=Integer.valueOf(s[1]);
			mal.dayofmonth=Integer.valueOf(s[2]);
			mal.hour=Integer.valueOf(s[3]);
			mal.minute=Integer.valueOf(s[4]);
			mal.targetTime=Long.valueOf(s[5]);
			mal.enabled=s[6].equalsIgnoreCase("true");
			mal.delays=Integer.valueOf(s[7]);
			mal.retryInterval=Integer.valueOf(s[8]);
			mal.scheType=Integer.valueOf(s[9]);
			mal.alarmTitle=s[10];
			
			mal.alarmimportance=Integer.valueOf(s[11]);
			mal.retryInterval=Integer.valueOf(s[12]);
			mal.alarmImage=Integer.valueOf(s[13]);
			
			
			return mal;
		}
		if(str.startsWith(String.valueOf(TYPE_ALARM_WEEKLY))){
			String[] s=str.split("\\|");
			mal.type=Integer.valueOf(s[0]);
			mal.hour=Integer.valueOf(s[1]);
			mal.minute=Integer.valueOf(s[2]);
			for(int i=0;i<=6;i++)
			{
				mal.week[i]=s[3].charAt(i)=='1';
			}
			mal.targetTime=Long.valueOf(s[4]);
			mal.enabled=s[5].equalsIgnoreCase("true");
			mal.delays=Integer.valueOf(s[6]);
			mal.retryInterval=Integer.valueOf(s[7]);
			mal.scheType=Integer.valueOf(s[8]);
			mal.alarmTitle=s[9];
			
			mal.alarmimportance=Integer.valueOf(s[10]);
			mal.retryInterval=Integer.valueOf(s[11]);
			mal.alarmImage=Integer.valueOf(s[12]);
			mal.isDivideWeek=s[13].equalsIgnoreCase("true");
			mal.oddWeek=s[14].equalsIgnoreCase("true");
			return mal;
		}
		
		if(str.startsWith(String.valueOf(TYPE_ALARM_INTERVAL))){
			String[] s=str.split("\\|");
			
			mal.type=Integer.valueOf(s[0]);
			mal.dayInterval=Integer.valueOf(s[1]);
			mal.dayOffset=Integer.valueOf(s[2]);
			mal.hour=Integer.valueOf(s[3]);
			mal.minute=Integer.valueOf(s[4]);
			mal.targetTime=Long.valueOf(s[5]);
			mal.enabled=s[6].equalsIgnoreCase("true");
			mal.delays=Integer.valueOf(s[7]);
			mal.retryInterval=Integer.valueOf(s[8]);
			mal.scheType=Integer.valueOf(s[9]);
			mal.alarmTitle=s[10];
			
			
			mal.alarmimportance=Integer.valueOf(s[11]);
			mal.retryInterval=Integer.valueOf(s[12]);
			mal.alarmImage=Integer.valueOf(s[13]);
			
			
			
			return mal;
		}
		}catch(ArrayIndexOutOfBoundsException e){return mal;}
		
		
		throw new IllegalStateException("unknown alarm type");
	}
	
	public String getTypeTitle(){
		return typeTitles[scheType];
	}
	public String getNextStr(){
		//if(!enabled){return "不会";}
		StringBuilder sb=new StringBuilder("在");
		
		Date d=new Date(targetTime);
		sb.append(d.getYear()+1900).append("年");
		String[] arw={"日","一","二","三","四","五","六"};
		sb.append(d.getMonth()+1).append("月");
		sb.append(d.getDate()).append("日");
		sb.append("周").append(arw[d.getDay()]);
		sb.append(" ");
		if(d.getHours()<10){sb.append("0");}
		sb.append(d.getHours());
		sb.append(":");
		if(d.getMinutes()<10){sb.append("0");}
		sb.append(d.getMinutes());
		return sb.toString();
	}
	
	public int getTypeIcon(){
		return typeIcons[scheType];
	}
	public void saveAlarm(Context ctx,long alarmId){
		ctx.getSharedPreferences("alarms",ctx.MODE_PRIVATE).edit().putString(String.valueOf(alarmId),this.toString()).commit();
	}
	public static MyAlarm loadAlarm(Context ctx,long alarmId){
		try{
		return MyAlarm.fromString(ctx.getSharedPreferences("alarms",ctx.MODE_PRIVATE).getString(String.valueOf(alarmId),""));
		}catch(Exception e){
			return null;
		}
	}
	public static long[] getAllAlarm(Context ctx){
		SharedPreferences shp=ctx.getSharedPreferences("alarms",ctx.MODE_PRIVATE);
		Set p=shp.getAll().keySet();
		String[] arrs=new String[p.size()];
		p.toArray(arrs);
		long[] alarmids=new long[arrs.length];
		for(int i=0;i<alarmids.length;i++){
			alarmids[i]=Long.valueOf(arrs[i]);
		}
		return alarmids;
	}
	public static String[] getAllAlarmString(Context ctx){
		SharedPreferences shp=ctx.getSharedPreferences("alarms",ctx.MODE_PRIVATE);
		Set p=shp.getAll().keySet();
		String[] arrs=new String[p.size()];
		p.toArray(arrs);
		return arrs;
	}
	
	public AlarmHint getTipString(){
		AlarmHint cst=new AlarmHint(R.drawable.doze,"Zzz");
		switch (scheType){
			
			case TYPE_SCHE_MORNING:
				if(hour<=10&&hour>=4){
					cst.imgid=R.drawable.morning;
					cst.hintText=randomOne("早上好，[称呼]！","早安，[称呼]！","今天又是元气满满的一天呢，[称呼]");
				}else{
					cst.imgid=R.drawable.ask;
					cst.hintText="[称呼]，这个点的早安。。。是什么意思？？？";
				}
			break;
			
			case TYPE_SCHE_GETUP:
				cst.hintText="[称呼][称呼]，起床了！";
				cst.imgid=R.drawable.morning;
				if(hour<6){
					cst.imgid=R.drawable.activate;
					cst.hintText="[称呼]起的好早啊！一定是有什么重要的事吧。";
				}
				if(hour<=12&&hour>=9){
					cst.imgid=R.drawable.welcome;
					cst.hintText="太晚起床对身体不好，[称呼]以后可要早起哦！";
				}
				
				if(delays>0){
					cst.imgid=R.drawable.strike;
					cst.hintText="起~床~啦~，[称呼2]！";
					if(delays>1){
						cst.hintText="[称呼][称呼]，赶快起床！再晚就来不及了！";
					}
				}
			break;
			
			case TYPE_SCHE_GOTO_SCHOOL:
				cst.imgid=R.drawable.activate;
				cst.hintText=randomOne("到了上学的时间了，[称呼]","上学时间到了，[称呼]")+
				"\n上课时一定要好好听讲，和老师积极互动，认真思考老师提出的问题，祝你学业进步！";
			break;
			case TYPE_SCHE_GOTO_WORK:
				cst.imgid=R.drawable.completion;
				cst.hintText="该去上班了，[称呼]";
			break;
			case TYPE_SCHE_BREAKFAST:
				cst.hintText="[称呼]，吃早饭了！早饭一定要吃好哦！";
				cst.imgid=R.drawable.food;
				if(hour<=12&&hour>=9){
					cst.imgid=R.drawable.unsatisfied;
					cst.hintText="[称呼]怎么那么不爱惜自己的身体？早晨很重要的，一定不要拖延得太晚。";
				}
			break;
			
			case TYPE_SCHE_LUNCH:
				cst.hintText="[称呼]，吃午饭时间到了";
				cst.imgid=R.drawable.food;
			break;
			case TYPE_SCHE_SUPPER:
				cst.hintText="吃晚饭了，[称呼]。\n晚饭不要吃得太多哦。";
				cst.imgid=R.drawable.lessfood;
			break;
			case TYPE_SCHE_GOTO_BED:
				cst.hintText="该睡觉了，[称呼]一定要好好休息。。。";
				cst.imgid=R.drawable.night;
				if(hour>=22 ||hour<=3){
					cst.hintText="[称呼]怎么这么晚才睡觉？这样下去对[称呼]的身体不好的。。。";
					cst.imgid=R.drawable.wakefromdoze;
				}
			break;
			case TYPE_SCHE_NIGHT_CALL:
				if(hour>=19&&hour<=22){
					cst.hintText=randomOne("晚安，[称呼]！","做个好梦，[称呼]！");
					cst.imgid=R.drawable.night;
				}
				else{
					if(hour>22 ||hour<3){
						cst.hintText="[称呼]这么晚才睡觉吗？这样下去对身体不好的。。。";
						cst.imgid=R.drawable.wakefromdoze;
					}
					else
					{
						cst.hintText="[称呼]，这个点的晚安提醒。。。意味着什么呢？";
						cst.imgid=R.drawable.ask;
					}
				}
			break;
			case TYPE_SCHE_BIRTHDAY:
				
				if((this.alarmTitle).trim().isEmpty()){
					cst.hintText="生日快乐，[称呼]！";
					cst.imgid=R.drawable.cheer;
				}else{
					cst.hintText="今天是一个重要的人的生日，去给Ta庆祝一下吧！";
					cst.imgid=R.drawable.activate;
				}
			break;
			case TYPE_SCHE_INVITATION:
				cst.hintText=randomOne("[称呼]，有没有忘记和Ta的约定。","[称呼]，千万不要忘记和Ta的约定");
				cst.imgid=R.drawable.activate;
			break;
			case TYPE_SCHE_HOMEWORK:
				cst.hintText=randomOne("[称呼]该写作业了","[称呼]，放下手机，作业时间到了","作业即使很多也是要努力完成的，[称呼]");
				cst.imgid=R.drawable.sorry;
			break;
			case TYPE_SCHE_HOUSEWORK:
				cst.hintText=randomOne("该做家务了，[称呼]。要保持精致的生活习惯哦！");
				cst.imgid=R.drawable.norm;
			break;
			case TYPE_SCHE_MEETING:
				cst.hintText="[称呼]有一个会议需要参加。";
				cst.imgid=R.drawable.knock;
				if(delays!=0){
					cst.hintText="[称呼2]！会议要迟到了！";
					cst.imgid=R.drawable.strike;
				}
			break;
			case TYPE_SCHE_TVPROGRAM:
				cst.hintText="[称呼]，你想看的电视节目要开始了！";
				cst.imgid=R.drawable.knock;
			break;
			case TYPE_SCHE_TAKETHING:
				cst.imgid=R.drawable.ask;
				cst.hintText=randomOne("[称呼]，可别忘了带东西哦。","[称呼]，请检查一下要带的东西");
				if(alarmTitle.trim().isEmpty()){
					cst.hintText=cst.hintText +"\n如果记不住什么是东西的话，可以让我来帮忙记下来(提醒列表-编辑提醒事件-输入详细说明)";
				}
			break;
			case TYPE_SCHE_GAME:
				cst.imgid=R.drawable.game;
				cst.hintText="[称呼]，打会游戏，放松一下吧。(可别太沉迷哦)";
			break;
			
			case TYPE_SCHE_CALLTO:
				cst.imgid=R.drawable.call;
				cst.hintText="[称呼]，记得打电话问候一下某个人哦。";
			break;
			
			case TYPE_SCHE_SPORT:
				cst.imgid=R.drawable.jogging;
				cst.hintText=randomOne("[称呼]，坚持运动，能保持一个好心情哦。","[称呼]，不要整天宅着，也要多出去锻炼身体。","[称呼]，出去活动一下吧。");
			break;
			
			case TYPE_SCHE_READING:
				cst.imgid=R.drawable.encourage;
				cst.hintText=randomOne("看会书吧，[称呼]","读书能修身养性哦，[称呼]");
			break;
			
			case TYPE_SCHE_CUSTOM:
				if(alarmImage==-1){
				cst.imgid=R.drawable.activate;
				}else{
					cst.imgid=alarmImage;
				}
				
				if(alarmTitle.trim().isEmpty()){
					cst.hintText="[称呼]，有件事情别忘记哦。";
					cst.hintText=cst.hintText +"\n如果记不住是什么事情的话，可以让我来帮忙记下来(提醒列表-编辑提醒事件-输入详细说明)";
				}else{
					cst.hintText=alarmTitle;
				}
			break;
			case TYPE_SCHE_MEDICINE:
				cst.imgid=R.drawable.confused;
				cst.hintText="[称呼]该吃药了。";
				if(delays!=0){
					cst.hintText=cst.hintText+"按时吃药才能好得更快哦。";
				}
				if(alarmTitle.trim().isEmpty()){
					cst.hintText=cst.hintText +"\n我可以帮你记住服用的药和剂量。我可是很可靠的哦。(提醒列表-编辑提醒事件-输入详细说明)";
				}
			break;
		}
		return cst;
	}
	public static final String[] typeTitles=new String[]{
		"早安","起床","上学","上班","早餐",
		"午餐","晚餐","睡觉","晚安","生日",
		"邀约","作业","家务","会议","电视节目",
		"带上东西","游戏","电话问候","健身","阅读","自定义","吃药"
	};
	public static final String[] hints=new String[]{
		"早安","起床","上学","上班","早餐",
		"午餐","晚餐","睡觉","晚安","生日的对象可以写在这里(留空为自己)",
		"邀约对象可以写在这里","作业内容可以写在这里","家务内容可以写在这里","会议内容和地点可以写在这里","关注的电视节目可以写在这里",
		"带上什么东西可以写在这里","什么游戏可以写在这里","电话问候对象可以写在这里","健身计划可以写在这里","阅读内容可以写在这里","提醒内容可以写在这里","类型和剂量可以写在这里"
	};
	
	public String randomOne(String... strs){
		if(null!=strs&&strs.length>0){
			return strs[(int)(Math.random()*strs.length)];
		}
		return "";
	}
	
	
	
	
}


