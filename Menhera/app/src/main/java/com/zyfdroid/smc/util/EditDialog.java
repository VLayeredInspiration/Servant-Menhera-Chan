package com.zyfdroid.smc.util;
import android.app.*;
import android.content.*;
import android.widget.*;
import android.os.*;
import android.view.*;

public abstract class EditDialog extends Dialog
{
	String title;
	Context ctx;
	String def;
	public EditDialog(Context mctx,String mtitle,String defaultValue){
		super(mctx);
		title=mtitle;
		ctx=mctx;
		def=defaultValue;
	}
	EditText edt;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setTitle(title);
		LinearLayout base=new LinearLayout(ctx);
		base.setOrientation(base.VERTICAL);
		base.setGravity(Gravity.CENTER);
		edt=new EditText(ctx);
		base.addView(edt);
		LinearLayout.LayoutParams lyp=(LinearLayout.LayoutParams)edt.getLayoutParams();
		lyp.width=lyp.MATCH_PARENT;
		edt.setLayoutParams(lyp);
		edt.setText(def);
		LinearLayout btnbase=new LinearLayout(ctx);
		btnbase.setOrientation(btnbase.HORIZONTAL);
		btnbase.setGravity(Gravity.CENTER);
		base.addView(btnbase);
		lyp=(LinearLayout.LayoutParams)btnbase.getLayoutParams();
		lyp.width=lyp.MATCH_PARENT;
		btnbase.setLayoutParams(lyp);
		Button btnCancel=new Button(ctx);
		btnCancel.setText("取消");
		btnCancel.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					
					EditDialog.this.dismiss();
				}
			});
		Button btnOk=new Button(ctx);
		btnOk.setText("确定");
		btnOk.setOnClickListener(new Button.OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					
					onConfirmText(edt.getText().toString());
					dismiss();
				}
			});
		btnbase.addView(btnCancel);
		btnbase.addView(btnOk);
		setContentView(base);
	}


	public abstract void onConfirmText(String text);
}


