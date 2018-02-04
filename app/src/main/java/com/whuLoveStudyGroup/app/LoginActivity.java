package com.whuLoveStudyGroup.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;


public class LoginActivity extends MyActivity {

    CircleImageView head;
    AutoCompleteTextView phone;
    EditText password;
    Button commit;
    private static final String TAG = "dong.loginActivity";
    ConnWithServer connWithServer = new ConnWithServer();
    
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
                
                if (TextUtils.isEmpty(phone.getText())){
                    Toast.makeText(LoginActivity.this, "请输入手机号。", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(password.getText())){
                    Toast.makeText(LoginActivity.this, "请输入密码。", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                int error = connWithServer.loginUser(phone.getText().toString(),password.getText().toString());

                switch (error) {
                    case 0:
                        finish();
                        break;
                    case 404021:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(LoginActivity.this, "请检查手机号是否有误。", Toast.LENGTH_SHORT).show();
                        return;
                    case 404031:
                    case 404033:
                    case 404034:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(LoginActivity.this, "密码不正确。", Toast.LENGTH_SHORT).show();
                        return;
                    case 404023:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(LoginActivity.this, "该手机号尚未注册。", Toast.LENGTH_SHORT).show();
                        return;
                    case 400:
                    case 402:
                    case 999:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(LoginActivity.this, "出现错误" + error + ",请联系我们，谢谢！", Toast.LENGTH_SHORT).show();
                        return;
                    case 499:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(LoginActivity.this, "服务器连接失败，请重试。", Toast.LENGTH_SHORT).show();
                        return;
                    default:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(LoginActivity.this, "出现技术性错误" + error + ",请联系我们，谢谢！", Toast.LENGTH_SHORT).show();
                        return;
                }
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

