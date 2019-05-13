package com.zyfdroid.smc.soul.util;


import java.util.Calendar;
import java.util.GregorianCalendar;
/*
* DateTime,避免java.util.Date中getHours()等方法的@Deprecated
* */
public class McDate {
    private static final Calendar mCalendar=new GregorianCalendar();
    static{
        mCalendar.setFirstDayOfWeek(Calendar.SUNDAY);
    }
    private long timeMill;
    public McDate(){
        timeMill=System.currentTimeMillis();
    }

    public McDate(long time){
        timeMill=time;
    }

    public int getHours(){
        synchronized (mCalendar){
            mCalendar.setTimeInMillis(timeMill);
            return mCalendar.get(Calendar.HOUR_OF_DAY);
        }
    }
    public void setHours(int hour){
        synchronized (mCalendar){
            mCalendar.setTimeInMillis(timeMill);
            mCalendar.set(Calendar.HOUR_OF_DAY,hour);
            this.timeMill=mCalendar.getTimeInMillis();
        }
    }

    public int getMinutes(){
        synchronized (mCalendar){
            mCalendar.setTimeInMillis(timeMill);
            return mCalendar.get(Calendar.MINUTE);
        }
    }
    public void setMinutes(int minutes){
        synchronized (mCalendar){
            mCalendar.setTimeInMillis(timeMill);
            mCalendar.set(Calendar.MINUTE,minutes);
            this.timeMill=mCalendar.getTimeInMillis();
        }
    }
    public int getMonth(){
        synchronized (mCalendar){
            mCalendar.setTimeInMillis(timeMill);
            return mCalendar.get(Calendar.MONTH);
        }
    }
    public void setMonth(int month){
        synchronized (mCalendar){
            mCalendar.setTimeInMillis(timeMill);
            mCalendar.set(Calendar.MONTH,month);
            this.timeMill=mCalendar.getTimeInMillis();
        }
    }

    public int getDate(){
        synchronized (mCalendar){
            mCalendar.setTimeInMillis(timeMill);
            return mCalendar.get(Calendar.DAY_OF_MONTH);
        }
    }
    public void setDate(int date){
        synchronized (mCalendar){
            mCalendar.setTimeInMillis(timeMill);
            mCalendar.set(Calendar.DAY_OF_MONTH,date);
            this.timeMill=mCalendar.getTimeInMillis();
        }
    }

    /**
     * @return 获取年份,行为和j.u.Date相同,加上1900后为实际年份
     */
    public int getYear(){
        synchronized (mCalendar){
            mCalendar.setTimeInMillis(timeMill);
            return mCalendar.get(Calendar.YEAR)-1900;
        }
    }

    /**
     * @param year 设置年份,行为和j.u.Date相同,加上1900后为实际年份
     */
    public void setYear(int year){
        synchronized (mCalendar){
            mCalendar.setTimeInMillis(timeMill);
            mCalendar.set(Calendar.YEAR,year+1900);
            this.timeMill=mCalendar.getTimeInMillis();
        }
    }
    public int getSeconds(){
        synchronized (mCalendar){
            mCalendar.setTimeInMillis(timeMill);
            return mCalendar.get(Calendar.SECOND);
        }
    }
    public void setSeconds(int seconds){
        synchronized (mCalendar){
            mCalendar.setTimeInMillis(timeMill);
            mCalendar.set(Calendar.SECOND,seconds);
            this.timeMill=mCalendar.getTimeInMillis();
        }
    }

    public int getDay(){
        synchronized (mCalendar){
            mCalendar.setTimeInMillis(timeMill);
            return mCalendar.get(Calendar.DAY_OF_WEEK);
        }
    }

    /**
     * 行为和j.u.Date -> getTimezoneOffset行为相同
     * @return 返回值为当前时间加上多少分钟后是格林尼治的标准时间(当当前时区的GMT为正的时候会返回负数)
     */
    public long getTimezoneOffset(){
        synchronized (mCalendar){
            mCalendar.setTimeInMillis(timeMill);
            return mCalendar.get(Calendar.ZONE_OFFSET)/-60000L;
        }
    }


    /**
     * @return 距离参考时间点过了多少天,每天都会变化一次的一个数值
     */
    public long getDayStamp(){
        McDate epoch=new McDate();
        epoch.setYear(118);
        epoch.setMonth(0);
        epoch.setDate(1);
        epoch.setHours(0);
        epoch.setMinutes(0);
        epoch.setSeconds(0);
        return (timeMill-epoch.getTime()) / (86400L * 1000L);
    }

    /**
     * 一个每天都会变的值,区别是getDayStamp方法会将零点视为一天的开始,而这个方法可以指定一天开始的时刻
     * @param startHour 一天开始的小时
     * @param startMinute 一天开始的分钟
     * @return 一个每天都会变的值
     */
    public long getDayStamp(int startHour,int startMinute){
        long datestamp=getDayStamp();
        int thistimer=this.getHours() * 60 + this.getMinutes();
        int targettimer=startHour * 60 + startMinute;
        if(thistimer >= targettimer){
            return datestamp;
        }
        else {
            datestamp--;
            return datestamp;
        }
    }

    public void setTime(long time){
        this.timeMill=time;
    }

    public long getTime(){
        return this.timeMill;
    }

}
