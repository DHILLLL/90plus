package com.whuLoveStudyGroup.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

public class AccountActivity extends AppCompatActivity {

    public static final String FRUIT_NAME = "fruit_name";
    public static final String FRUIT_IMAGE_ID = "fruit_image_id";
    LocalBroadcastManager localBroadcastManager;
    ImageView portrait;
    Uri imageUri;
    TextView sex,grade,academy,profession,phone,qq,signature;
    SharedPreferences sp1;
    CollapsingToolbarLayout collapsingToolbarLayout;

    private static final String TAG = "dong,AccountActivity";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //设置当前主题
        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        String theme = sp.getString("theme","candy");
        if (theme.equals("coffee")){
            setTheme(R.style.CoffeeT);
        }
        if (theme.equals("candy")){
            setTheme(R.style.AppThemeT);
        }
        if (theme.equals("harvest")){
            setTheme(R.style.HarvestT);
        }
        if (theme.equals("sea")){
            setTheme(R.style.SeaT);
        }
        if (theme.equals("pink")){
            setTheme(R.style.PinkT);
        }
        if (theme.equals("butterfly")){
            setTheme(R.style.ButterflyT);
        }
        if (theme.equals("forgive")){
            setTheme(R.style.ForgiveT);
        }
        if (theme.equals("sexless")){
            setTheme(R.style.SexlessT);
        }

        ActivityCollector.addActivity(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);


        Toolbar toolbar = (Toolbar) findViewById(R.id.account_toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.account_collapsing_toolbar);
        portrait = (ImageView) findViewById(R.id.account_portrait);
        sex = (TextView) findViewById(R.id.account_sex);
        grade = (TextView) findViewById(R.id.account_grade);
        academy = (TextView) findViewById(R.id.account_academy);
        profession = (TextView) findViewById(R.id.account_profession);
        phone = (TextView) findViewById(R.id.account_phone);
        qq = (TextView) findViewById(R.id.account_qq);
        signature = (TextView) findViewById(R.id.account_description);

        final FloatingActionButton edit = (FloatingActionButton) findViewById(R.id.account_edit);
        Button logout = (Button) findViewById(R.id.account_logout);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }





        localBroadcastManager = localBroadcastManager.getInstance(this);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this,AccountEditActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AccountActivity.this);
                builder.setTitle("确定要退出登录吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = getSharedPreferences("account",MODE_PRIVATE).edit();
                        editor.putBoolean("login",false);
                        editor.apply();
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        sp1 = getSharedPreferences("account",MODE_PRIVATE);

        File outf = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), sp1.getString("phone","") + "portrait.jpg");
        imageUri = Uri.fromFile(outf);

        collapsingToolbarLayout.setTitle(sp1.getString("nickname",""));
        switch (sp1.getInt("sex",-1)){
            case -1:
                sex.setText("保密");
                break;
            case 0:
                sex.setText("女生");
                break;
            case 1:
                sex.setText("男生");
                break;
            default:
        }
        grade.setText(sp1.getInt("grade",0) + "级");
        academy.setText(sp1.getString("academy",""));
        profession.setText(sp1.getString("profession",""));
        phone.setText((sp1.getInt("public",0)==1)?sp1.getString("phone",""):"保密");
        qq.setText(sp1.getString("qq",""));
        signature.setText(sp1.getString("signature",""));

        Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath());
        portrait.setImageBitmap(bitmap);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
