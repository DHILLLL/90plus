package com.whuLoveStudyGroup.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
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

import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginActivity extends MyActivity {

    CircleImageView head;
    AutoCompleteTextView phone;
    EditText password;
    Button commit;
    private static final String TAG = "dong.loginActivity";
    ConnWithServer connWithServer = new ConnWithServer();
    LocalBroadcastManager localBroadcastManager;
    
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




        localBroadcastManager = localBroadcastManager.getInstance(this);

        head = (CircleImageView)findViewById(R.id.login_head);
        phone = (AutoCompleteTextView)findViewById(R.id.login_phone);
        password = (EditText)findViewById(R.id.login_password);
        commit = (Button)findViewById(R.id.login_commit);

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!TextUtils.isEmpty(phone.getText())){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int error = connWithServer.getUserImageAddr(phone.getText().toString());
                            if (error == 0) {
                                String url = (String)connWithServer.getResponseData();
                                OkHttpClient okHttpClient = new OkHttpClient();
                                Request request = new Request.Builder().url(url).build();
                                Call call = okHttpClient.newCall(request);
                                call.enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {

                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        byte[] picByte = response.body().bytes();
                                        final Bitmap bitmap = BitmapFactory.decodeByteArray(picByte,0,picByte.length);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                head.setImageBitmap(bitmap);
                                            }
                                        });

                                    }
                                });

                            }else {
                                Log.d(TAG, "get portrait: " + error);
                            }

                        }
                    }).run();
                }

            }
        });

        commit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (TextUtils.isEmpty(phone.getText())){
                    Toast.makeText(LoginActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(password.getText())){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                int error = connWithServer.loginUser(phone.getText().toString(),password.getText().toString());

                switch (error) {
                    case 0:
                        /*
                        private String username = null;
                        private String phoneNumber = null;
                        private int isSexEqualBoy = -1;
                        private int grade = 0;
                        private String academy = null;
                        private String profession = null;
                        private String qqNumber = null;
                        private String signature = null;
                        private String imageUrl = null;
    */
                        User user = (User) connWithServer.getResponseData();
                        SharedPreferences.Editor editor = getSharedPreferences("account",MODE_PRIVATE).edit();
                        editor.putBoolean("login",true);
                        editor.putString("phone",user.getPhoneNumber());
                        editor.putString("nickname", user.getUsername());
                        editor.putInt("sex",user.getSex());
                        editor.putInt("grade",user.getGrade());
                        editor.putString("academy",user.getAcademy());
                        editor.putString("profession",user.getProfession());
                        editor.putString("qq",user.getQqNumber());
                        editor.putString("signature",user.getSignature());
                        editor.putString("url",user.getImageUrl());
                        editor.putInt("public",user.getIsPhoneNumberPublic());
                        editor.apply();

//                        Intent intent = new Intent("com.whuLoveStudyGroup.app.LOGIN");
//                        localBroadcastManager.sendBroadcast(intent);
                        finish();
                        break;
                    case 404021:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(LoginActivity.this, "请检查手机号是否有误", Toast.LENGTH_SHORT).show();
                        return;
                    case 404031:
                    case 404033:
                    case 404034:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(LoginActivity.this, "密码不正确", Toast.LENGTH_SHORT).show();
                        return;
                    case 404023:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(LoginActivity.this, "该手机号尚未注册", Toast.LENGTH_SHORT).show();
                        return;
                    case 400:
                    case 402:
                    case 999:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(LoginActivity.this, "出现错误" + error + ",请联系我们，谢谢！", Toast.LENGTH_SHORT).show();
                        return;
                    case 499:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(LoginActivity.this, "服务器连接失败，请重试", Toast.LENGTH_SHORT).show();
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

