package com.zyfdroid.smc.soul.activity;
import android.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.net.*;
import android.os.*;
import android.provider.*;
import android.support.v4.app.*;
import android.support.v4.content.*;
import android.view.*;
import android.widget.*;
import com.zyfdroid.smc.R;
import com.zyfdroid.smc.soul.base.*;
import com.zyfdroid.smc.soul.util.*;
import java.util.*;
import com.zyfdroid.smc.*;


public class GuideActivity extends BaseActivity
{
int uiPage=0;
	@Override
	public void onPrepareUi()
	{
		
		super.onPrepareUi();
		FPS=50;
		initUi1();
		
	}

	@Override
	public void onUiPrepared()
	{
		
		super.onUiPrepared();
	}

	@Override
	public void onFrame()
	{
		
		if(timer2_enable){Timer2();}
	}

	@Override
	public boolean isHiddenTitleBar()
	{
		return true;
	}

	
	
	
	
	
	//======Start of Ui Page 1
	Button btnNotification;
	Button btnWhiteList;
	Button btnIgnoreBattery;
	Button btnStorage;

	public void initUi1(){
		uiPage=1;
		setContentView(R.layout.guide);
		btnNotification=(Button)findViewById(R.id.btnNotification);
		btnWhiteList=(Button)findViewById(R.id.btnWhiteList);
		btnIgnoreBattery=(Button)findViewById(R.id.btnIgnoreBattery);
		btnStorage=(Button)findViewById(R.id.btnStorage);
		if(null==whitelist){
			whitelist=querySafeAppInfo();
		}
	}
	public void setNotifican(View p1){
		toNotificationSetting();
		
	}
String[] whitelist;
	public void setWhiteList(View p1){
		AlertDialog.Builder adb=new AlertDialog.Builder(this);
		adb.setTitle("设置和(可能是)安全软件列表");
		
		adb.setItems(whitelist, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					
					try{
					startActivity(getPackageManager().getLaunchIntentForPackage(whitelist[p2].split("\\n")[1]));
					}catch(Exception e){tw("打开失败，这可能不是个安全软件");}
				}
			});
			adb.create().show();
	}

	public void ignoreBattery(View p1){
		ignoreBatteryOptimization(this);
	}

	public void setStorage(View p1){
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
			int result=ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);
			if(result!=PackageManager.PERMISSION_GRANTED){
				if (ActivityCompat.shouldShowRequestPermissionRationale(this,
																		Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
																			
					tw("授权已经被禁止，请在设置里手动开启");
				} else {
					//申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
					ActivityCompat.requestPermissions(this,
													  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,}, 4371);
				}
			}else{
				tw("授权已成功");
			}
		}else{
			tw("授权已成功");
		}
	}

	public void onPermissionsDone(View p1){
		initUiPage2();
	uiPage=2;	
		
		
		
		
		
		
	}
	
	@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 4371) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "授权已成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "授权以拒绝", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
	 String[] querySafeAppInfo() {
		PackageManager pm = this.getPackageManager();
		// 查询所有已经安装的应用程序
		List<ApplicationInfo> appInfos= pm.getInstalledApplications(PackageManager.GET_ACTIVITIES);// GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的
		List<ApplicationInfo> applicationInfos=new ArrayList<>();

		// 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);

		// 通过getPackageManager()的queryIntentActivities方法遍历,得到所有能打开的app的packageName
		List<ResolveInfo>  resolveinfoList = getPackageManager()
            .queryIntentActivities(resolveIntent, 0);
		Set<String> allowPackages=new HashSet();
		for (ResolveInfo resolveInfo:resolveinfoList){
			allowPackages.add(resolveInfo.activityInfo.packageName);
		}

		for (ApplicationInfo app:appInfos) {
			if (allowPackages.contains(app.packageName)){
				if(isSafeApp(app,pm)){
				
				applicationInfos.add(app);
				}
			}
		}
		String[] applists=new String[applicationInfos.size()];
		for(int i=0;i<applists.length;i++){
			applists[i]=""+applicationInfos.get(i).loadLabel(pm)+"\n"+applicationInfos.get(i).packageName;
		}
		return applists;
	}
boolean isSafeApp(ApplicationInfo api,PackageManager pm){
	try{
	String appname=new String(api.loadLabel(pm).toString());
	Main.d(appname);
	String pkgname=api.packageName;
	String[] appnameKw=new String[]{"安全","管家","卫视","清理","优化","360","后台","杀手","Arbi","电池","任务","Battery","osed","Clean","CLEAN","clean"};
	String[] pkgnameKw=new String[]{"killer","guard","safe","xposed","optim","clean","battery","secur","task","anti","android.setting"};
	for(int i=0;i<appnameKw.length;i++){
		if(appname.contains(appnameKw[i])){return true;}
	}
	for(int i=0;i<pkgnameKw.length;i++){
		if(pkgname.contains(pkgnameKw[i])){return true;}
	}
	}catch(Exception e){Main.e(e);}
	return false;
}

	
	//=======End of Ui Page 1
	
	//=======Start of uipage2
	ImageView guide2_charimg;
	TextView guide2_chartext;
	Button guide2ButtonNext;

	public void initUiPage2(){
		setContentView(R.layout.guide2);
		guide2_charimg=(ImageView)findViewById(R.id.guide2_charimg);
		guide2_chartext=(TextView)findViewById(R.id.guide2_chartext);
		guide2_chartext.setText("");
		guide2ButtonNext=(Button)findViewById(R.id.guide2ButtonNext);
		timer2_enable=true;
		guide2ButtonNext.setEnabled(false);
		
	}
	boolean timer2_enable=false;
	public static final String charIntro="你好！主人，我是您的虚拟助手Menhera酱，初次见面，今后请多关照哟~！\n(・ω< )★";
	
	

	
	int textAddCountDown=0;
	int textPos=0;
	float currentAlpha=0.0f;
	void Timer2(){
		if(currentAlpha>1){currentAlpha=1;}
		guide2_charimg.setAlpha(currentAlpha);
		currentAlpha+=(1f/150f);
		textAddCountDown--;
		if(textAddCountDown<0){
			textAddCountDown=8;
			guide2_chartext.setText(guide2_chartext.getText()+String.valueOf(charIntro.charAt(textPos)));
			textPos++;
		}
		if(textPos>=charIntro.length()){
			timer2_enable=false;
			guide2ButtonNext.setEnabled(true);
		}
	}
	
	
	
	
	
	public void onNextBtnClick2(View p1){
		
		uiPage=3;
		initUiPage3();
		
	}
	
	//=======End of uipage2
	
	
	
	
	
	//==================================begin of uipage3
	
	
	RadioButton          guide3RadioSxNone;
	RadioButton          guide3RadioSxMale;
	RadioButton          guide3RadioSxFemale;
	Button               guide3ButtonBirthday;
	Button               guide3ButtonOccupation;
	Button               guide3ButtonCaller;
	TextView             guide3InfoHinter;
	Button               guide3OkButton;

	public void initUiPage3(){
		setContentView(R.layout.guide3);
		guide3RadioSxNone             =(RadioButton)         findViewById(R.id.guide3RadioSxNone);
		guide3RadioSxMale             =(RadioButton)         findViewById(R.id.guide3RadioSxMale);
		guide3RadioSxFemale           =(RadioButton)         findViewById(R.id.guide3RadioSxFemale);
		guide3ButtonBirthday          =(Button)              findViewById(R.id.guide3ButtonBirthday);
		guide3ButtonOccupation        =(Button)              findViewById(R.id.guide3ButtonOccupation);
		guide3ButtonCaller            =(Button)              findViewById(R.id.guide3ButtonCaller);
		guide3InfoHinter              =(TextView)            findViewById(R.id.guide3InfoHinter);
		guide3OkButton                =(Button)              findViewById(R.id.guide3OkButton);
	guide3OkButton.setEnabled(false);
	}
	boolean infoSxChosen=false;
	boolean infoBirthChosen=false;
	boolean infoOccupationChosen=false;
	boolean infoCallChosen=false;
	public void onRadioSxNoneClick(View p1){
		infoSxChosen=true;
		saveSetting("user_sx","？");
	}

	public void onRadioSxMaleClick(View p1){
		infoSxChosen=true;
		saveSetting("user_sx","♂");
	}

	public void onRadioSxFemaleClick(View p1){
		infoSxChosen=true;
		saveSetting("user_sx","♀");
	}

	public void onChooseBirthdayClick(View p1){
		new MyDatePickerDialog(this){

			@Override
			public void onDatePicked(int year, int month, int day, boolean useLunatic)
			{
				Date d=new Date();
				d.setYear(year-1900);
				d.setMonth(month);
				d.setDate(day);
				Date d2=new Date(System.currentTimeMillis());
				d2.setYear(d2.getYear()-3);
				if(d.after(d2)){
					tw("这个生日日期感觉不太对，主人要认真起来\n(ง •̀_•́)ง");
					onChooseBirthdayClick(null);
					return;
				}
				
				saveSetting("user_calcbirthday",d.getTime());
				if(useLunatic){
					LunarCalendar lunar=new LunarCalendar(year,month,day,false,false);
					saveSetting("user_celibirthday","年"+lunar.toString().split("年",2)[1]);
					guide3ButtonBirthday.setText(lunar.toString()+" >");
				}
				else
				{
					saveSetting("user_celibirthday","-"+month+"-"+day);
					guide3ButtonBirthday.setText(year+"-"+(month+1)+"-"+day+" >");
				}
				saveSetting("user_birthdaylunatic",useLunatic);
				infoBirthChosen=true;
				showHint("生日，用于生日提醒，以及生物钟节律计算(详见说明)");
			}
		}.show();
	}

	public void onChooseOccupationClick(View p1){
		AlertDialog.Builder adb=new AlertDialog.Builder(this);
		adb.setTitle("选择个人状态");
		adb.setItems(new CharSequence[]{"在校学生","从业人员(找工作中也算哦)","佛系自由业"}, new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
					saveSetting("user_occupation",p2);
					chkFinish();
					showHint("个人状态，决定了Menhera帮助主人的方式。");
					guide3ButtonOccupation.setText(new String[]{"在校学生","从业人员(找工作中也算哦)","佛系自由业"}[p2]+" >");
					infoOccupationChosen=true;
				}
			});
			adb.setCancelable(false);
			adb.create().show();
	}

	public void onChooseCallerClick(View p1){
		new EditDialog(this, "Menhera应该如何称呼主人", "主人"){

			@Override
			public void onConfirmText(String text)
			{
				if(text.trim().length()<1){
					saveSetting("caller","主人");
				}else{
					if(text.trim().length()>5){
						tw("太长的称呼Menhera会记不住的");
						onChooseCallerClick(null);
					}
					else
					{
						saveSetting("caller",text);
						showHint("好的，"+text+"！");
						guide3ButtonCaller.setText(text+" >");
						infoCallChosen=true;
					}
				}
			}
			
			
		}.show();
	}

	public void showHint(String s){
		guide3InfoHinter.setText(s);
	}
	
	public void chkFinish(){
		boolean result=true;
		result=result && infoSxChosen;
		result=result && infoBirthChosen;
		result=result && infoOccupationChosen;
		result=result && infoCallChosen;
		guide3OkButton.setEnabled(result);
	}
	public void saveSetting(String key,String value){
		getSharedPreferences("settings",MODE_PRIVATE).edit().putString(key,value).commit();
		chkFinish();
	}
	public void saveSetting(String key,int value){
		getSharedPreferences("settings",MODE_PRIVATE).edit().putInt(key,value).commit();
		chkFinish();
	}
	public void saveSetting(String key,boolean value){
		getSharedPreferences("settings",MODE_PRIVATE).edit().putBoolean(key,value).commit();
		chkFinish();
	}
	public void saveSetting(String key,long value){
		getSharedPreferences("settings",MODE_PRIVATE).edit().putLong(key,value).commit();
		chkFinish();
	}
	
	
	
	//==================================end of uipage3
	
	
	
	
	@Override
	public void onBackPressed()
	{
		

	}
	
	
	
	private void toNotificationSetting() {  
		Intent localIntent = new Intent();  
		localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		if (Build.VERSION.SDK_INT >= 9) {  
			localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");  
			localIntent.setData(Uri.fromParts("package", getPackageName(), null));  
		} else if (Build.VERSION.SDK_INT <= 8) {  
			localIntent.setAction(Intent.ACTION_VIEW);  
			localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");  
			localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());  
		}  
		startActivity(localIntent);  
	}  
	
	public void ignoreBatteryOptimization(Activity activity) {  
		if(android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
			try{
				PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);  

				boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());  
//tw(hasIgnored);
				//  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。  
				if(!hasIgnored) {  
					Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);  
					intent.setData(Uri.parse("package:"+activity.getPackageName()));  
					startActivity(intent);  
				}  else{tw("已经加入电池优化白名单。");}
			}catch (Exception e){
				//twErr(e);
			}
	}
	
}
