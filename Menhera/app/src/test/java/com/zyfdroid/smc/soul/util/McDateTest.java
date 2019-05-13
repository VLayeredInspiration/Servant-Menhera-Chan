package com.zyfdroid.smc.soul.util;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class McDateTest {

    @Test
    public void getDayStamp() {
        McDate mcDate=new McDate();
        System.out.printf("当前日期戳是(基于零点):%d \n",mcDate.getDayStamp());
        System.out.printf("当前日期戳是(基于六点半):%d \n",mcDate.getDayStamp(6,30));
        mcDate.setHours(0);
        mcDate.setMinutes(0);
        mcDate.setSeconds(0);
        System.out.printf("今天00:00:00日期戳是(基于零点):%d \n",mcDate.getDayStamp());
        System.out.printf("今天00:00:00日期戳是(基于六点半):%d \n",mcDate.getDayStamp(6,30));
        mcDate.setHours(23);
        mcDate.setMinutes(59);
        mcDate.setSeconds(59);
        System.out.printf("今天23:59:59日期戳是(基于零点):%d \n",mcDate.getDayStamp());
        System.out.printf("今天23:59:59日期戳是(基于六点半):%d \n",mcDate.getDayStamp(6,30));


        System.out.printf("当前时区偏移(我的方法):%d \n",mcDate.getTimezoneOffset());
        System.out.printf("当前时区偏移(j.u.Date方法):%d \n",new Date().getTimezoneOffset());

        System.out.printf("获取年(我的方法):%d \n",mcDate.getYear());
        System.out.printf("获取年(j.u.Date方法):%d \n",new Date().getYear());

    }
}