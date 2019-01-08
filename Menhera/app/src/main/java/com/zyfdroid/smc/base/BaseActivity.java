package com.zyfdroid.smc.base;

import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;
import android.graphics.drawable.*;
import android.media.*;
import android.os.*;
import android.support.annotation.RequiresApi;
import android.text.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import java.io.*;
import android.support.v4.widget.*;
import com.zyfdroid.smc.service.*;
import com.zyfdroid.smc.util.*;


public class BaseActivity extends Activity
{
	//logic
	//将文本中的[称呼]替换成设定的称呼
	public String l(String s){
		StringBuilder sb=new StringBuilder("");
		for(int i=0;i<MaimService.refer.length();i++){
			sb.append(MaimService.refer.charAt(i)).append("~");
		}
		return s.replace("[称呼]",MaimService.refer).replace("[称呼2]",sb.toString());
	}
	public static  int FPS=60;
	Thread FrameThread;
public boolean canshowhelp=true;;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		if(isHiddenTitleBar()){this.requestWindowFeature(Window.FEATURE_NO_TITLE);}
		if(isFullScreen()){this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);		}
		if(isImmerseMode()){onHide();}
		FrameThread = new Thread(new Runnable(){

				@Override
				public void run()
				{
					
					Runnable UiTask=new Runnable(){

						@Override
						public void run()
						{
							onFrame();
							
						}
					};
					try
					{
						Thread.sleep(250);
					}
					catch (InterruptedException e)
					{}
					runOnUiThread(new Runnable(){

							@Override
							public void run()
							{
								onUiPrepared();
							}
						});
					int spf=1000 / FPS;
					while(true){
						try
						{
							Thread.sleep(spf);
							runOnUiThread(UiTask);
						}
						catch (InterruptedException e)
						{return;}
					}

				}
			});
		onPrepareUi();
		FrameThread.start();
	}






	boolean isCreating=true;
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		
		super.onWindowFocusChanged(hasFocus);
		if(isCreating){
			isCreating=false;
			//onUiPrepared();
			//FrameThread.start();
		}
		if(hasFocus&&isImmerseMode()){
		onHide();
		}
	}

	@Override
	protected void onDestroy()
	{
		
		FrameThread.interrupt();
		super.onDestroy();

	}


	public void onHide() {
		//4.1及以上通用flags组合
		int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().getDecorView().setSystemUiVisibility(
                flags | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
			);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			getWindow().getDecorView().setSystemUiVisibility(flags);
		}
	}
	


//hooks

	public void onFrame(){

	}
	public void onPrepareUi(){

	}
	public void onUiPrepared(){

	}
	
	
	public boolean isHiddenTitleBar()
	{
	return false;
	}
	
	
	public boolean isFullScreen()
	{
	return false;
	}
	
	public boolean isImmerseMode()
	{
	return false;
	}
	
	
	
	
	
	
	
	//Useful Methods:

	public static String sdc(String path){
		String sdpath=Environment.getExternalStorageDirectory().toString();
		if(  (!sdpath.endsWith("/"))  &&  (!path.startsWith("/"))  ){
			return sdpath+"/"+path;
		}else{
			return sdpath+ path;
		}
	}



	public String ugtext(int id){
		return ((EditText)findViewById(id)).getText().toString();
	}

	public static String helpflag;
	public void showHelp(String title,String text,int imageid,String flag){
	if(canshowhelp){
		helpflag=flag;
		if(loadstate("help_" +helpflag).isEmpty()){
			LinearLayout ll=new LinearLayout(this);
			ll.setGravity(Gravity.CENTER);
			ll.setOrientation(ll.VERTICAL);
			ImageView imv=new ImageView(this);
			imv.setImageResource(imageid);
			TextView txv=new TextView(this);
			txv.setText(l(text));
			try{
				TextViewCompat.setTextAppearance(txv,android.R.style.TextAppearance_Large);
			//txv.setTextAppearance(android.R.attr.textAppearanceLarge);
			}catch(NoSuchMethodError e){}
			ll.addView(imv);
			ll.addView(txv);
			
			AlertDialog.Builder adb=new AlertDialog.Builder(this).setTitle(l(title)).setView(ll).setPositiveButton("确定", null);
			
			if(!flag.isEmpty()){
			adb.setNegativeButton("知道啦，不再提示", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface p1, int p2)
					{
						
						savestate("help_" +helpflag,"showed");
					}
				});
			}
				
				
				
				adb.create().show();}}
	}

	public void mtw(String text, int imageid){
		Toast t=Toast.makeText(this,text,Toast.LENGTH_LONG);
		LinearLayout ll=new LinearLayout(this);
		ll.setGravity(Gravity.CENTER);
		ll.setOrientation(ll.VERTICAL);
		ImageView imv=new ImageView(this);
		imv.setImageResource(imageid);
		TextView txv=new TextView(this);
		txv.setText(l(text));
		txv.setTextColor(0xffffffff);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			txv.setTextAppearance(android.R.style.TextAppearance_Large);
		}
		else
		{
			TextViewCompat.setTextAppearance(txv,android.R.style.TextAppearance_Large);
		}
		ll.addView(imv);
		ll.addView(txv);
		ll.setBackgroundColor(0xa0282836);
		t.setView(ll);
		t.show();
	}
	
	
	
	
	
	
	
	public void utw(String text,String title){
		AlertDialog.Builder adb=new AlertDialog.Builder(this);
		adb.setTitle(title);
		adb.setMessage(text);
		adb.setPositiveButton("确定",null);
		adb.create();
		adb.show();
	}//change3

	public void prevPic(Bitmap mp){//change4
		BaseActivity ctx=this;
		Toast t=Toast.makeText(this,"",Toast.LENGTH_LONG);
		ImageView imv=new ImageView(ctx);
		LinearLayout pannel=new LinearLayout(ctx);
		pannel.setBackgroundResource(android.R.drawable.toast_frame);
		pannel.setPadding(10,10,10,10);
		pannel.setOrientation(LinearLayout.VERTICAL);
		imv.setImageBitmap(mp);
		imv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		TextView tv=new TextView(ctx);
		tv.setText("图片预览");
		tv.setTextColor(0xffffffff);
		tv.setBackgroundColor(0x9f000009);
		tv.setGravity(Gravity.CENTER);
		pannel.setGravity(Gravity.CENTER);
		pannel.addView(tv);
		pannel.addView(imv);
		t.setView(pannel);
		t.setDuration(Toast.LENGTH_LONG);
		t.show();
	}

	public void prevPic(Drawable mp){//change4
		BaseActivity ctx=this;
		Toast t=Toast.makeText(this,"",Toast.LENGTH_LONG);
		ImageView imv=new ImageView(ctx);
		LinearLayout pannel=new LinearLayout(ctx);
		pannel.setBackgroundResource(android.R.drawable.toast_frame);
		pannel.setPadding(20,20,20,20);
		pannel.setOrientation(LinearLayout.VERTICAL);
		imv.setImageDrawable(mp);
		imv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		TextView tv=new TextView(ctx);
		tv.setText("图片预览");
		tv.setTextColor(0xffffffff);
		tv.setBackgroundColor(0x9f000009);
		tv.setGravity(Gravity.CENTER);
		pannel.setGravity(Gravity.CENTER);
		pannel.addView(tv);
		pannel.addView(imv);
		t.setView(pannel);
		t.setDuration(Toast.LENGTH_LONG);
		t.show();
	}
	public void prevPic(int resid){//change4
		BaseActivity ctx=this;
		Toast t=Toast.makeText(this,"",Toast.LENGTH_LONG);
		ImageView imv=new ImageView(ctx);
		LinearLayout pannel=new LinearLayout(ctx);
		pannel.setBackgroundResource(android.R.drawable.toast_frame);
		pannel.setPadding(10,10,10,10);
		pannel.setOrientation(LinearLayout.VERTICAL);
		imv.setImageResource(resid);
		imv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		TextView tv=new TextView(ctx);
		tv.setText("图片预览");
		tv.setTextColor(0xffffffff);
		tv.setBackgroundColor(0x9f000009);
		tv.setGravity(Gravity.CENTER);
		pannel.setGravity(Gravity.CENTER);
		pannel.addView(tv);
		pannel.addView(imv);
		t.setView(pannel);
		t.setDuration(Toast.LENGTH_LONG);
		t.show();
	}

	public static Bitmap getDiskBitmap(String pathString)
	{
		Bitmap bitmap = null;
		try
		{
			File file = new File(pathString);
			if(file.exists())
			{
				bitmap = BitmapFactory.decodeFile(pathString);
			}
			else{
				throw new IOException("Not such file or dictionary");
			}
		} catch (Exception e)
		{
			// TODO: handle exception
			throw new AndroidRuntimeException("Unable to load picture");
		}


		return bitmap;
	}
	
	public void copyBigDataToSD(String in,String strOutFileName)// throws IOException 
    {  
		int row=0;
		try{
			InputStream myInput;  
			File f=new File((strOutFileName));
			row=1;
			if(f.exists()){f.delete();//f.createNewFile();
			}
			row=9;
			//f.mkdirs();
			f.createNewFile();
			row=2;
			OutputStream myOutput = new FileOutputStream(f.toString());  
			row=3;

			myInput = getAssets().open(in);  
			row=4;
			byte[] buffer = new byte[1024];  
			row=5;
			int length = myInput.read(buffer);
			row=6;
			while(length > 0)
			{
				myOutput.write(buffer, 0, length); 
				length = myInput.read(buffer);
			}
			row=7;
			myOutput.flush();  
			myInput.close();  
			myOutput.close();        
			row=8;
		}catch (IOException e){
			throw new AndroidRuntimeException("Copy assets to "+strOutFileName+" Error at row "+ String.valueOf(row)+e.getMessage());
		}
    }
	
	public void twErr(Exception e){
		
		StringBuilder sb=new StringBuilder("发生错误:");
		sb.append("\n");

		Writer writer = new StringWriter();  
		PrintWriter printWriter = new PrintWriter(writer);  
		e.printStackTrace(printWriter);  
		Throwable cause = e.getCause();  
		while (cause != null) {  
			cause.printStackTrace(printWriter);  
			cause = cause.getCause();  
		}  
		printWriter.close();  
		sb.append( writer.toString());  
		tw(sb.toString());
	}

	Bitmap getPicFromAssets(String fName) throws IOException
	{
		int fsize;
		Bitmap img;
		AssetManager amr=getResources().getAssets();
		InputStream ips=amr.open(fName);
		fsize=ips.available();
		byte[] fle=new byte[fsize];
		ips.read(fle);
		img=BitmapFactory.decodeByteArray(fle, 0, fle.length);
		return img;
	}
	
	public String getTextFromAssets(String fileName)
	{
		String result = "";
		try
		{
			InputStream in = getResources().getAssets().open(fileName);
//获取文件的字节数
			int lenght = in.available();
//创建byte数组
			byte[] buffer = new byte[lenght];
//将文件中的数据读到byte数组中

			in.read(buffer);
			String wRes=new String(buffer, "UTF-8");
			result = wRes.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
public void tw(Object text)
{
Toast.makeText(this,l(text.toString()),Toast.LENGTH_LONG).show();
}
public static void syso(Object p1){
	android.util.Log.d("com.zyfdroid.smc",p1.toString());
	Klog.v(p1);
}
public Button fbi(int id){
	return (Button)findViewById(id);
}
	public TextView fti(int id){
		return (TextView)findViewById(id);
	}
	public LinearLayout fli(int id){
		return (LinearLayout)findViewById(id);
	}
	public EditText fei(int id){
		return (EditText)findViewById(id);
	}
	public RelativeLayout fri(int id){
		return (RelativeLayout)findViewById(id);
	}
	public ImageView fii(int id){
		return (ImageView)findViewById(id);
	}
	public MediaPlayer bfm(int resid){
		MediaPlayer mp=MediaPlayer.create(this,resid);
		try
		{
			mp.prepare();
		}
		catch (IOException e)
		{}
		catch (IllegalStateException e)
		{}
		catch(Exception e){}
		mp.start();
		return mp;
	}
	/*
	public static String frai(Activity ctx,String filename){
		
		try
		{
			ctx.getResources().getAssets().open(filename, AssetManager.ACCESS_STREAMING).;
		}
		catch (IOException e)
		{return null;}
		
		
	}*/
	public void savestate(String key,String value){
		getSharedPreferences("preference",MODE_PRIVATE).edit().putString(key,value).commit();
		
	}
	public String loadstate(String key){
		return getSharedPreferences("preference",MODE_PRIVATE).getString(key,"");
	}
	

	public void adaptLayout(Activity ctx,int baseViewid,float screenrate){
		assert(!isCreating);
		int basicHeight;int basicWidth;
		basicHeight=getWindowManager().getDefaultDisplay().getHeight();
		basicWidth=getWindowManager().getDefaultDisplay().getWidth();
		View vw=ctx.findViewById(baseViewid);
		ViewGroup.LayoutParams lp=vw.getLayoutParams();
		if(((float)basicWidth)/((float)basicHeight)>screenrate){
			//屏幕比较宽,应以高为基准
			float rate=screenrate;
			float h=basicHeight;
			lp.width=(int)(h*rate);
		}else{
			//屏幕比较窄,应以宽为基准
			float rate=1/screenrate;
			float w=basicWidth;
			lp.height=(int)(w*rate);
			//lp.height=((int)(((float)(basicWidth))*(7f/5f)));
		}
		vw.setLayoutParams(lp);
	}

	public static boolean getFileFromBytes(byte[] b, String outputFile) {  
        BufferedOutputStream stream = null;  
        File file = null;  
        try {  
            file = new File(outputFile);  
			if(file.exists()){file.delete();}
			if(!file.createNewFile()){throw new Exception("");}
            FileOutputStream fstream = new FileOutputStream(file);  
            stream = new BufferedOutputStream(fstream);  
            stream.write(b);  
        } catch (Exception e) {  
            e.printStackTrace();  
			return false;
        } finally {  
            if (stream != null) {  
                try {  
                    stream.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
        }  
        return true;  
    }  

    public static byte[] Bitmap2Bytes(Bitmap bm){  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);  
        return baos.toByteArray();  
    }  

	public static Bitmap getBitmapFromFile(File f) {

        if (!f.exists()) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; /* just decode bound , no memory use */
        BitmapFactory.decodeFile(f.getPath(), options); /* get image info to options */

		// float realWidth = options.outWidth; /* here is the original size of this image */
		//  float realHeight = options.outHeight;
        float scale = 1;
        options.inSampleSize = (int) scale;
        options.inJustDecodeBounds = false; /* will really product an image , would use memory */

        return BitmapFactory.decodeFile(f.getPath(), options);
	}



	
	
}
