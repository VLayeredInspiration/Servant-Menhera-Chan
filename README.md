# Servant-Menhera-Chan

最近热门的表情包Menhera做为虚拟助手来了<br>
<br>


-
<br>
>This project do not compat other languages.<br>
>このプロジェクトは他の言語と互換性がありません。<br>
>If the repository violates the copyright of any party, please leave a comment. We will delete the content immediately after seeing the comment<br>
>リポジトリがいかなる当事者の著作権を侵害する場合は、コメントを残してください。コメントが表示された直後にコンテンツが削除されます
<br>
<br>
(请尽情吐槽(提意见))
UI渣(程序员那完全不存在的审美)欢迎指点<br>
文案渣(语文从未及格过)欢迎指正和修改<br>
以及`可能`存在的反人类设计<br>
感觉idea不错的话欢迎给星星<br>

之前围观了一些大神的作品，然后深度学习了一番，觉得自己的代码架构要改。然后在整理时第一次感觉自己的代码风格💩一样
所以要努力在下一阶段尽量像正常人的代码画风靠拢

最近学了很多，正在一步步重构代码

<br>

<br>

当然版本:半成品
-

>由于存储空间原因，将不再提供Line表情贴纸的下载，可以到以下仓库去获取最新的表情贴纸

>https://github.com/a-wing/Menhera-chan

项目已经迁移至Android Studio,重新提供下载



![点进去后右上角下载](Menhera/app/release/app-release.apk)



P.S. Do not use the fxxking "Instant Run" in Android Studio

<br>
<br>


>来自上海市经济管理学校 手机编程社
<br>

>本作品暂无版权，开源。内有未经授权的美术作品，请在使用时尊重原作者的著作权。
>>支持正版美术素材(指表情贴纸)的方法:
>>http://www.bilibili.com/video/av21720927
>>懒得看视频的，我大概讲一下:翻墙下载Line(一个像微信的APP)。那里有正版，也就4元左右
<br>

<br>



当前已添加功能:
-
<br>
驻在通知栏里<br>
普通的提醒功能<br>
{<br>
21种提醒类型，涵盖生活大部分方面<br>
提醒重要性分门别类<br>
自定义提醒样式<br>
&nbsp;<br>
单次提醒&nbsp;每周提醒(可设置单双周提醒)&nbsp;间隔多少天提醒&nbsp;<br>
再次提醒超时随意设置<br>
自动检查因为后台被清或者手机关机造成错过的提醒<br>
最近在网上看到了关于俄罗斯自杀性蓝鲸游戏，于是禁止了凌晨四点十分的提醒的设置。但具体提醒尚未完善
}<br>

<br>
简单的设置<br>
{<br>
可以更换通知栏通知的背景颜色<br>
可以更换称呼<br>
}<br>
<br>
<br>
<br>

别问我之前一直在下面的，那么大一堆的计划哪儿去了，在另一个文件里
<br>
![](Menhera/app/src/main/res/drawable-xhdpi/activate.png)
<br>


最近参加比赛训练，提交会变没有


<br>
给大家一个好东西(根据layout一键生成JAVA代码，或者adapter的工具)

```Java
class XMLProc{
	public static void procLayout(String path){
		try
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(path)));//"/storage/emulated/0/chapter_edit.xml")));
			String s="";
			br.skip(5);
			String currentUiType="";
			StringBuilder callbacks=new StringBuilder("");
			StringBuilder fields=new StringBuilder("");
			StringBuilder method=new StringBuilder("public void initUi(){\n");
			while((s=br.readLine())!=null){
				s=s.trim();
				if(s.startsWith("<")&&!s.startsWith("</")){
					currentUiType=s.replace("<","").trim();
				}
				if(s.startsWith("android:id=\"@+id/")){
					String id=(s.replace("android:id=\"@+id/","").replace("\"","").replace(">","").replace("/",""));
					fields.append(currentUiType).append(" ").append(id).append(";\n");
					method.append("\t").append(id).append("=(").append(currentUiType).append(")findViewById(R.id.").append(id).append(");\n");
				}
				if(s.startsWith("android:onClick=\"")){
					String cbn=s.replace("android:onClick=\"","").replace("\"","").replace(">","").replace("/","");
					if(!callbacks.toString().contains(cbn+"(View p1)"))
						callbacks.append("public void ").append(cbn).append("(View p1){\n").append("\t//TODO:Implements this method.\n}\n\n");
				}
			}
			method.append("}");
			System.out.println(fields.toString());
			System.out.println(method.toString());
			System.out.println(callbacks.toString());
		}
		catch (FileNotFoundException e)
		{}
		catch (IOException e)
		{}
	}

	public static void procAdapter(String path,String adpName,String arraytype){
		try
		{
			BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(path)));//"/storage/emulated/0/chapter_edit.xml")));
			String s="";
			br.skip(5);
			
			String[] t1=path.split("/");
			String name=t1[t1.length-1].replace(".xml","");
			
			String currentUiType="";
			StringBuilder fields=new StringBuilder("@Override\npublic View getView(int position, View convertView, ViewGroup parent)\n{\nView v=LayoutInflater.from(getContext()).inflate(R.layout.%lay%,parent,false);".replace("%lay%",name));
			StringBuilder method=new StringBuilder("public ");
			method.append(adpName).append("(Context ctx,").append(arraytype).append("[] datas)\n{\nsuper(ctx,R.layout.%n%,datas);\n}\n".replace("%n%",name));
			while((s=br.readLine())!=null){
				s=s.trim();
				if(s.startsWith("<")&&!s.startsWith("</")){
					currentUiType=s.replace("<","").trim();
				}
				if(s.startsWith("android:id=\"@+id/")){
					String id=(s.replace("android:id=\"@+id/","").replace("\"","").replace(">","").replace("/",""));
					fields.append(currentUiType).append(" ").append(id)
					.append("=(").append(currentUiType).append(")v.findViewById(R.id.").append(id).append(");\n");
				}
				
			}
			
			fields.append("\n//TODO:Implement this method.\n\nreturn v;\n}");
			System.out.println("class "+adpName+ " extends ArrayAdapter<" + arraytype+">{");
			System.out.println(method.toString());
			System.out.println(fields.toString());
			System.out.println("}");
			
		}
		catch (FileNotFoundException e)
		{}
		catch (IOException e)
		{}
	}
}

```

项目中还有一些自制/转载的，实用的类，各位可以按需要拿去使用
-
据不完全统计:

InputBox 弹出一个输入框，有确定取消，可以输入文字。无需xml

MyAlarm: 提醒功能的功能核心，可以用来管理闹钟，无需xml，需要有sharedPreference配合(不建议用数据库。因为数据库频繁加载消耗CPU，保持打开会造成文件句柄占用)

MyDatePickerDialog: 一个日期选择对话框，支持农历。用法参考源码。需要dialog_datapicker.xml和LunarCalendar.java

LunarCalendar: 农历日历，来自 https://github.com/heqiao2010/LunarCalendar (我不知道为什么这么好的项目star这么少。大家把他顶上去)

CrashHandler:由CSDN上的，写log的修改而来，可以弹出错误日志界面(调试的时候非常有用，尤其是在手机上编程时)

和其他一些什么奇怪的东西
