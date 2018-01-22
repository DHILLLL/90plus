package com.whuLoveStudyGroup.app;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;


public class LoginActivity extends MyActivity {

    CircleImageView head;
    AutoCompleteTextView phone;
    EditText password;
    Button commit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        head = (CircleImageView)findViewById(R.id.login_head);
        phone = (AutoCompleteTextView)findViewById(R.id.login_phone);
        password = (EditText)findViewById(R.id.login_password);
        commit = (Button)findViewById(R.id.login_commit);

        commit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home://返回按钮点击事件
                finish();
                break;
            case R.id.login_register:
                Intent intent1 = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent1);
                break;
            case R.id.login_forget:
                Intent intent2 = new Intent(LoginActivity.this,ResetActivity.class);
                startActivity(intent2);
                break;
            default:
        }
        return true;
    }

}

