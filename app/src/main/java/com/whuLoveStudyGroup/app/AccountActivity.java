package com.whuLoveStudyGroup.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class AccountActivity extends AppCompatActivity {

    public static final String FRUIT_NAME = "fruit_name";
    public static final String FRUIT_IMAGE_ID = "fruit_image_id";

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
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.account_collapsing_toolbar);
        ImageView portrait = (ImageView) findViewById(R.id.account_portrait);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle("DHILLLL");
        Glide.with(this).load(R.drawable.jay2).into(portrait);
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
