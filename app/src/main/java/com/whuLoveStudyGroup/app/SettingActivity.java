package com.whuLoveStudyGroup.app;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class SettingActivity extends MyActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        initToolbar();
        getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingFragment()).commit();

     /*   LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(SettingActivity.this);
        Intent intent1 = new Intent("com.example.app.UPDATE_SCHEDULE");
        localBroadcastManager.sendBroadcast(intent1);*/
    }

    private void initToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar_preference);
        toolbar.setTitle("Settings");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }
}
