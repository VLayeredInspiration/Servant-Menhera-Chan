package com.zyfdroid.smc.soul.util;

public class Klog
{
	public static String log="";
	public static void v(Object ... s){
		String sb="";
		if(s.length!=0){
			for(int i=0;i<s.length;i++){
				sb=sb+"\n"+s[i].toString();
			}
		log=log+sb.trim()+"\n";
		}
		if(log.length()>4096){log=log.subSequence(log.length()-4096,log.length()).toString();}
	}
	

	public static void codeBreak(){
		throw new RuntimeException("Code Break");
	}
	
}
