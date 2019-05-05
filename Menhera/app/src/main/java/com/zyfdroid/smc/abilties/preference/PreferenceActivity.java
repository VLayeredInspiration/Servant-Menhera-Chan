package com.zyfdroid.smc.abilties.preference;

import android.content.*;
import android.os.*;
import android.preference.*;
import android.widget.*;
import android.app.*;

import com.zyfdroid.smc.*;
import com.zyfdroid.smc.soul.service.*;

public class PreferenceActivity extends android.preference.PreferenceActivity implements Preference.OnPreferenceClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName("settings");
        addPreferencesFromResource(R.xml.preferences);
        findPreference("birthday").setOnPreferenceClickListener(this);


    }


    @Override
    public boolean onPreferenceClick(Preference preference) {

        if (preference.getKey().contentEquals("birthday")) {
            onBirthdayPreferenceClick();
            return true;
        }
        return false;
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
        try {
            MaimService.mCurrentContext.changeNotifican(-1, null, null);
        } catch (NullPointerException npe) {
            Main.e(npe);
            startService(new Intent(this, MaimService.class));
        } catch (Exception e) {
            Main.e(e);
        }
    }

    void tw(String s) {
        Toast.makeText(this, s, 1000).show();
    }


    void onBirthdayPreferenceClick() {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);

    }


}


abstract class DatePickerDialog extends Dialog {
    //TODO Implement this
    public DatePickerDialog(Context ctx) {
        super(ctx);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


    public abstract void onDatePickResult(boolean comfirm, int year, int month, int date, boolean isLunar);


}


	
