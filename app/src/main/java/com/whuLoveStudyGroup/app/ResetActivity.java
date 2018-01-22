package com.whuLoveStudyGroup.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;


public class ResetActivity extends MyActivity {

    AutoCompleteTextView phone;
    EditText code,password,passwordConfirm;
    Button commit,get,wait;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset);

        Toolbar toolbar = (Toolbar) findViewById(R.id.reset_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        phone = (AutoCompleteTextView)findViewById(R.id.reset_phone);
        code = (EditText)findViewById(R.id.reset_code);
        password = (EditText)findViewById(R.id.reset_password);
        passwordConfirm = (EditText)findViewById(R.id.reset_password_confirm);
        commit = (Button)findViewById(R.id.reset_commit);
        get = (Button)findViewById(R.id.reset_get);
        wait = (Button)findViewById(R.id.reset_wait);

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sendRequest()){
                    get.setVisibility(View.GONE);
                    wait.setText("60s");
                    wait.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for (int i = 60;i >= 0;i--){
                                final int I = i;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        wait.setText(I + "s");
                                    }
                                });
                                try {
                                    Thread.sleep(1000);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    get.setVisibility(View.VISIBLE);
                                    wait.setVisibility(View.GONE);
                                }
                            });
                        }
                    }).start();
                }
            }
        });

    }

    private boolean sendRequest(){
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home://返回按钮点击事件
                finish();
                break;
            default:
        }
        return true;
    }

}

