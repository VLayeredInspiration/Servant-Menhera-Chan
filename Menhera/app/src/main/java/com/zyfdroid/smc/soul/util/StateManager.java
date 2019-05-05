package com.zyfdroid.smc.soul.util;
import java.util.*;

public class StateManager
{
	private State currentState;

	int year=0;
	int month=0;
	int day=0;
	int week=0;
	int hour=0;
	int minute=0;
	float timeOfDay=0f;
	int lunar_month=0;
	int lunar_day=0;
	boolean lunar_leap=false;
	long stamp=-1;
	

	
	public State getState(){
		callCalcState();
		return currentState;
	}
	
	private void callCalcState(){
		updateTime();
		
		//TODO "Not implement yet.";
		
	}
	private long lastDisturb=-1;
	public void onDisturb(){
		lastDisturb=System.currentTimeMillis();
		
		//TODO "Not implement yet.";
	}
	
	private long hangTillTime=-1;
	private State hangState;
	private boolean cancelOnDisturb=false;
	public void hangState(State state, long duration){
		hangTillTime=System.currentTimeMillis()+duration;
		hangState=state;
		cancelOnDisturb=false;
	}
	public void hangStatUntilDisturb(State state, long timeout){
		hangState(state,timeout);
		cancelOnDisturb=true;
	}
	private float dayTime(int mHour,int mMinute){
		float totalminute=mHour*60+mMinute;
		return totalminute/1440f;
	}
	private void updateTime(){
		Date d=new Date(System.currentTimeMillis());
		year=d.getYear();
		month=d.getMonth()+1;
		day=d.getDate();
		week=d.getDate();
		hour=d.getHours();
		minute=d.getMinutes();
		timeOfDay=dayTime(hour,minute);
		LunarCalendar lc=LunarCalendar.solar2Lunar(Calendar.getInstance());
		lunar_month=lc.getMonth();
		lunar_day=lc.getDate();
		lunar_leap=lc.isLeapMonth();
		stamp=System.currentTimeMillis();
	}
	public class State{
		public String notifyText;
		public int notifyImg;
		public String[] largeText;
		public int[] largeImg;
		public String leaveText;
		public int leaveImg;
		public CtxRunnable special;
		public String getNotifyText(){
			return notifyText;
		}
		public int getNotifyImg(){
			return notifyImg;
		}
		public String getLargeText(){
			return largeText
			[(int)(largeText.length*Math.random())];
		}
		public int getLargeImg(){
			return largeImg
				[(int)(largeImg.length*Math.random())];
		}
		public String getLeaveText(){
			return leaveText;
		}
		public int getLeaveImg(){
			return leaveImg;
		}
	}
	
}

