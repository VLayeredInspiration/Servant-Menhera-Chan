package com.zyfdroid.smc.util;
import android.app.*;
import android.content.*;
import android.os.*;
import com.zyfdroid.smc.*;
import android.widget.*;
import java.util.*;
import android.view.*;
import android.widget.DatePicker.*;

public abstract class MyDatePickerDialog extends Dialog{
	public MyDatePickerDialog(Context ctx){
		super(ctx);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_datapicker);
		initUi();
		setCancelable(false);
		setTitle("选择日期");

	}
	Button               dialogdatapickerButtonCancel;
	Button               dialogdatapickerButtonOk;
	DatePicker           dialogdatapickerDatePicker;
	TextView             dialogdatapickerLunarText;
	CheckBox             dialogdatapickerChkBoxUseLunar;
	Calendar tempCalendar;
	public void initUi(){
		dialogdatapickerDatePicker    =(DatePicker)          findViewById(R.id.dialogdatapickerDatePicker);
		dialogdatapickerLunarText     =(TextView)            findViewById(R.id.dialogdatapickerLunarText);
		dialogdatapickerChkBoxUseLunar=(CheckBox)            findViewById(R.id.dialogdatapickerChkBoxUseLunar);
		dialogdatapickerButtonCancel  =(Button)              findViewById(R.id.dialogdatapickerButtonCancel);
		dialogdatapickerButtonOk      =(Button)              findViewById(R.id.dialogdatapickerButtonOk);

		dialogdatapickerButtonOk.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					
					onDatePicked(dialogdatapickerDatePicker.getYear(),dialogdatapickerDatePicker.getMonth(),dialogdatapickerDatePicker.getDayOfMonth(),dialogdatapickerChkBoxUseLunar.isChecked());
					dismiss();

				}
			});
		dialogdatapickerButtonCancel.setOnClickListener(new View.OnClickListener(){

				@Override
				public void onClick(View p1)
				{
					
					//onDatePicked(dialogdatapickerDatePicker.getYear(),dialogdatapickerDatePicker.getMonth(),dialogdatapickerDatePicker.getDayOfMonth(),dialogdatapickerChkBoxUseLunar.isChecked());
					dismiss();

				}
			});

		Date d=new Date();
		dialogdatapickerDatePicker.init(d.getYear()+1900, d.getMonth(), d.getDate(), new OnDateChangedListener(){

				@Override
				public void onDateChanged(DatePicker p1, int p2, int p3, int p4)
				{
					
					try{
						if(null==tempCalendar){
							tempCalendar=Calendar.getInstance();
						}
						tempCalendar.set(p1.getYear(),p1.getMonth(),p1.getDayOfMonth());
						dialogdatapickerLunarText.setText("选定日期农历:"+LunarCalendar.solar2Lunar(tempCalendar).toString());
						if(!dialogdatapickerChkBoxUseLunar.isEnabled()){dialogdatapickerChkBoxUseLunar.setEnabled(true);}
					}
					catch(Exception e){
						dialogdatapickerChkBoxUseLunar.setChecked(false);
						dialogdatapickerChkBoxUseLunar.setEnabled(false);
						dialogdatapickerLunarText.setText("选定日期农历:暂无");
					}
					if(p2==1926&&p3==7&&p4==17){dialogdatapickerLunarText.setText("苟利国家生死以，岂因祸福避趋之🐸");
					}
				}
			});
		try{
			if(null==tempCalendar){
				tempCalendar=Calendar.getInstance();
			}
			//tempCalendar.set(p1.getYear(),p1.getMonth(),p1.getDayOfMonth());
			dialogdatapickerLunarText.setText("选定日期农历:"+LunarCalendar.solar2Lunar(tempCalendar).toString());
			if(!dialogdatapickerChkBoxUseLunar.isEnabled()){dialogdatapickerChkBoxUseLunar.setEnabled(true);}
		}
		catch(Exception e){
			dialogdatapickerChkBoxUseLunar.setChecked(false);
			dialogdatapickerChkBoxUseLunar.setEnabled(false);
			dialogdatapickerLunarText.setText("选定日期农历:暂无");

		}
	}

	@Override
	public void onBackPressed()
	{
		
		super.onBackPressed();
		dismiss();
	}

	public abstract void onDatePicked(int year,int month,int day,boolean useLunatic);
}
