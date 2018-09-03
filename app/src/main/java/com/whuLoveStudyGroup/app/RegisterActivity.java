package com.whuLoveStudyGroup.app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.whuLoveStudyGroup.app.connWithServerUtil.ConnWithServer;

import java.util.Random;


public class RegisterActivity extends MyActivity {

    AutoCompleteTextView phone;
    EditText code,password,passwordConfirm,nickname;
    Button commit,get,wait;
    private static final String TAG = "dong,RegesterActivity";
    ConnWithServer connWithServer = new ConnWithServer();
    String currentCode,currentPhone,currentPassword,currentNickname;
    String rand;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        phone = (AutoCompleteTextView)findViewById(R.id.register_phone);
        code = (EditText)findViewById(R.id.register_code);
        password = (EditText)findViewById(R.id.register_password);
        passwordConfirm = (EditText)findViewById(R.id.register_password_confirm);
        nickname = (EditText)findViewById(R.id.register_nickname);
        commit = (Button)findViewById(R.id.register_commit);
        get = (Button)findViewById(R.id.register_get);
        wait = (Button)findViewById(R.id.register_wait);

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

        //提交注册
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(phone.getText())){
                    Toast.makeText(RegisterActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    currentPhone = phone.getText().toString();
                }

                if (TextUtils.isEmpty(code.getText())){
                    Toast.makeText(RegisterActivity.this, "请输入短信验证码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!code.getText().toString().equals(rand)){
                    Toast.makeText(RegisterActivity.this, "短信验证码错误", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    currentCode = code.getText().toString();
                }

                if (TextUtils.isEmpty(password.getText())){
                    Toast.makeText(RegisterActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(passwordConfirm.getText())){
                    Toast.makeText(RegisterActivity.this, "请确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!password.getText().toString().equals(passwordConfirm.getText().toString())){
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不相同", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    currentPassword = password.getText().toString();
                }

                if (TextUtils.isEmpty(nickname.getText())){
                    Toast.makeText(RegisterActivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    currentNickname = nickname.getText().toString();
                }

                int error = connWithServer.addUser(currentCode,currentPhone,currentNickname,currentPassword);

                switch (error){
                    case 0:
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setTitle("注册成功！");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
                        break;
                    case 404021:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(RegisterActivity.this, "请检查手机号是否有误", Toast.LENGTH_SHORT).show();
                        return;
                    case 404022:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(RegisterActivity.this, "该手机号已被注册", Toast.LENGTH_SHORT).show();
                        return;
                    case 404011:
                    case 404012:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(RegisterActivity.this, "请检查验证码是否有误", Toast.LENGTH_SHORT).show();
                        return;
                    case 404061:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(RegisterActivity.this, "用户名长度不能超过15个字符", Toast.LENGTH_SHORT).show();
                        return;
                    case 404031:
                    case 404034:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(RegisterActivity.this, "密码长度应在8-25位字符之间", Toast.LENGTH_SHORT).show();
                        return;
                    case 404032:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(RegisterActivity.this, "密码请不要使用纯字母或纯数字", Toast.LENGTH_SHORT).show();
                        return;
                    case 400:
                    case 402:
                    case 999:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(RegisterActivity.this, "出现错误" + error + ",请联系我们，谢谢！", Toast.LENGTH_SHORT).show();
                        return;
                    case 499:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(RegisterActivity.this, "服务器连接失败，请重试", Toast.LENGTH_SHORT).show();
                        return;
                    default:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(RegisterActivity.this, "出现技术性错误" + error + ",请联系我们，谢谢！", Toast.LENGTH_SHORT).show();
                        return;
                }


            }
        });

    }

    //获取验证码
    private boolean sendRequest(){
        if (TextUtils.isEmpty(phone.getText())){
            Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
            return false;
        }
        Random random = new Random();
        rand = String.valueOf(random.nextInt(900000) + 100000);
        Log.d(TAG, "code: " + rand);
        int error;
        error = connWithServer.getVerificationCode(rand,phone.getText().toString());
        switch (error){
            case 0:
                return true;
            case 402:
            case 499:
                Log.d(TAG, "sendRequest: " + error);
                Toast.makeText(this, "服务器连接失败，请重试", Toast.LENGTH_SHORT).show();
                return false;
            case 404021:
            case 40101:
                Log.d(TAG, "sendRequest: " + error);
                Toast.makeText(this, "请检查手机号是否有误", Toast.LENGTH_SHORT).show();
                return false;
            case 404011:
            case 400:
            case 40102:
            case 40103:
            case 999:
                Log.d(TAG, "sendRequest: " + error);
                Toast.makeText(this, "出现技术性错误" + error + "，请向我们反馈，谢谢！", Toast.LENGTH_SHORT).show();
                return false;
            default:
                Log.d(TAG, "sendRequest: " + error);
                Toast.makeText(this, "出现技术性错误" + error + "，请向我们反馈，谢谢！", Toast.LENGTH_SHORT).show();
                return false;
        }
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

