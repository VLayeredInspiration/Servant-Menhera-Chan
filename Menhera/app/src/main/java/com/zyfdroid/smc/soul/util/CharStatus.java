package com.zyfdroid.smc.soul.util;
import com.zyfdroid.smc.*;

@Deprecated
public enum CharStatus
{
	NORM1(R.drawable.norm,"『有什么需要，[称呼]可以随时来找我！』",
		 R.drawable.zimg_activate,"我来了！[称呼]有什么事吗？？"),
	NORM2(R.drawable.free,"『有什么需要，随时找我。』",
		  R.drawable.zimg_activate,"我来了！[称呼]有什么事吗？？"),
	NORM3(R.drawable.chg,"『？？？？？？』",
		  R.drawable.zimg_activate,"我来了！[称呼]有什么事吗？？"),
	NORM_INAT(R.drawable.see,"『随时待命』",
		 R.drawable.ask,"请问[称呼]有什么需要帮忙的？"),
	NORM_DOZE(R.drawable.doze,"Zzzzz...",
	R.drawable.wakefromdoze,"『哈欠~』[称呼]有什么事吗？"),
	NIGHT(R.drawable.sleeping,"Zzzzz...",
	R.drawable.wakeup,"已经很晚了，[称呼]还不睡觉吗？我会帮上[称呼]的忙。[称呼]早点睡好吗？"),
	NIGHT_EASTER(R.drawable.night_easter,"『？？？？？？』",
	R.drawable.ask,"？？？？？？？？"),
	NIGHT_WAKE(R.drawable.wakefromdoze,"『[称呼]一定要早点睡哦。。。』",
	R.drawable.wakefromdoze,"已经很晚了，[称呼]还不睡觉吗？我会帮上[称呼]的忙。[称呼]早点睡好吗？"),
	IGNORED(R.drawable.unhappy,"好像没有理我，[称呼]是不是把我给忘了？",
	R.drawable.welcome,"啊，[称呼]终于回来了！"),
	DEEP_NIGHT(R.drawable.sleeping,"Zzzzz...",
	R.drawable.wakeup,"哈欠~"),
	;
	
	
	
	
	
	
	public int charImg;
	public String barText;
	public int clicknImg;
	public String clicknText;
	CharStatus(int mcharImg, String mbarText, int mclicknImg, String mclicknText){charImg=mcharImg;barText=mbarText;clicknImg=mclicknImg;clicknText=mclicknText;}
}

