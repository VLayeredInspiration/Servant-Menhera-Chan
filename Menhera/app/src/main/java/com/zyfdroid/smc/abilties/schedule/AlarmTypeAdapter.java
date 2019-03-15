package com.zyfdroid.smc.abilties.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyfdroid.smc.R;

public class AlarmTypeAdapter extends ArrayAdapter<String> {
    public AlarmTypeAdapter(Context ctx){
        super(ctx, R.layout.altp, MyAlarm.typeTitles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater lyf=LayoutInflater.from(getContext());
        View v0=lyf.inflate(R.layout.altp,parent,false);
        ((ImageView)v0.findViewById(R.id.altpImageView1)).setImageResource(MyAlarm.typeIcons[position]);
        ((TextView)v0.findViewById(R.id.altpTextView1)).setText(MyAlarm.typeTitles[position]+(MyAlarm.importance[position]?"(重要提醒)":""));
        return v0;
    }
}
