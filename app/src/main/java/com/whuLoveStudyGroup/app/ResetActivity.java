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


public class ResetActivity extends MyActivity {

    AutoCompleteTextView phone;
    EditText code,password,passwordConfirm;
    Button commit,get,wait;
    private static final String TAG = "dong,ResetActivity";
    ConnWithServer connWithServer = new ConnWithServer();
    String currentCode,currentPhone,currentPassword;
    String rand;

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

        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(phone.getText())){
                    Toast.makeText(ResetActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    currentPhone = phone.getText().toString();
                }

                if (TextUtils.isEmpty(code.getText())){
                    Toast.makeText(ResetActivity.this, "请输入短信验证码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!code.getText().toString().equals(rand)){
                    Toast.makeText(ResetActivity.this, "短信验证码错误", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    currentCode = code.getText().toString();
                }

                if (TextUtils.isEmpty(password.getText())){
                    Toast.makeText(ResetActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(passwordConfirm.getText())){
                    Toast.makeText(ResetActivity.this, "请确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }else if(!password.getText().toString().equals(passwordConfirm.getText().toString())){
                    Toast.makeText(ResetActivity.this, "两次输入的密码不相同", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    currentPassword = password.getText().toString();
                }


                int error = connWithServer.changePassword(currentCode,currentPhone,currentPassword);

                switch (error){
                    case 0:
                        AlertDialog.Builder builder = new AlertDialog.Builder(ResetActivity.this);
                        builder.setTitle("密码修改成功！");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        }).show();
                        break;
                    case 404021:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(ResetActivity.this, "请检查手机号是否有误", Toast.LENGTH_SHORT).show();
                        return;
                    case 404011:
                    case 404012:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(ResetActivity.this, "请检查验证码是否有误", Toast.LENGTH_SHORT).show();
                        return;
                    case 404061:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(ResetActivity.this, "用户名长度不能超过15个字符", Toast.LENGTH_SHORT).show();
                        return;
                    case 404031:
                    case 404034:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(ResetActivity.this, "密码长度应在8-25位字符之间", Toast.LENGTH_SHORT).show();
                        return;
                    case 404032:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(ResetActivity.this, "密码请不要使用纯字母或纯数字", Toast.LENGTH_SHORT).show();
                        return;
                    case 404042:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(ResetActivity.this, "该手机号尚未注册", Toast.LENGTH_SHORT).show();
                        return;
                    case 400:
                    case 404:
                    case 999:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(ResetActivity.this, "出现错误" + error + ",请联系我们，谢谢！", Toast.LENGTH_SHORT).show();
                        return;
                    case 499:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(ResetActivity.this, "服务器连接失败，请重试", Toast.LENGTH_SHORT).show();
                        return;
                    default:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(ResetActivity.this, "出现技术性错误" + error + ",请联系我们，谢谢！", Toast.LENGTH_SHORT).show();
                        return;
                }
            }
        });

    }

    private boolean sendRequest(){
        if (TextUtils.isEmpty(phone.getText())){
            Toast.makeText(this, "请输入手机号码。", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "服务器连接失败，请重试。", Toast.LENGTH_SHORT).show();
                return false;
            case 404021:
            case 40101:
                Log.d(TAG, "sendRequest: " + error);
                Toast.makeText(this, "请检查手机号是否有误。", Toast.LENGTH_SHORT).show();
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

