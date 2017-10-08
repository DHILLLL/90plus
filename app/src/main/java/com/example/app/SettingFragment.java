package com.example.app;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;

/**
 * Created by 635901193 on 2017/10/8.
 */

public class SettingFragment extends PreferenceFragment {

    private static final String TAG = "dong";

    CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("key1");


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName("Setting");
        addPreferencesFromResource(R.xml.preferences);

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference.getKey().equals("key2")){

        }
        return true;
    }
}
