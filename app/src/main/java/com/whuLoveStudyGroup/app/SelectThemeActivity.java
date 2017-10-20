package com.whuLoveStudyGroup.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by 635901193 on 2017/7/26.
 */

public class SelectThemeActivity extends MyActivity {
    private static final String TAG = "dongheyou";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_select);

        Toolbar toolbar = (Toolbar) findViewById(R.id.theme_select_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //选中主题的半透明特效
        Button blockCandy = (Button) findViewById(R.id.theme_select_block_candy);
        Button blockSea = (Button) findViewById(R.id.theme_select_block_sea);
        Button blockPink = (Button) findViewById(R.id.theme_select_block_pink);
        Button blockHarvest = (Button) findViewById(R.id.theme_select_block_harvest);
        Button blockForgive = (Button) findViewById(R.id.theme_select_block_forgive);
        Button blockCoffee = (Button) findViewById(R.id.theme_select_block_coffee);
        Button blockWine = (Button) findViewById(R.id.theme_select_block_wine);
        Button blockButterfly = (Button) findViewById(R.id.theme_select_block_butterfly);

        blockButterfly.setVisibility(View.GONE);
        blockSea.setVisibility(View.GONE);
        blockPink.setVisibility(View.GONE);
        blockHarvest.setVisibility(View.GONE);
        blockCoffee.setVisibility(View.GONE);
        blockForgive.setVisibility(View.GONE);
        blockWine.setVisibility(View.GONE);
        blockCandy.setVisibility(View.GONE);

        //获取当前主题
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        String theme1 = sharedPreferences.getString("theme","candy");
        if(theme1.equals("candy")) blockCandy.setVisibility(View.VISIBLE);
        if(theme1.equals("butterfly")) blockButterfly.setVisibility(View.VISIBLE);
        if(theme1.equals("sexless")) blockWine.setVisibility(View.VISIBLE);
        if(theme1.equals("sea")) blockSea.setVisibility(View.VISIBLE);
        if(theme1.equals("pink")) blockPink.setVisibility(View.VISIBLE);
        if(theme1.equals("harvest")) blockHarvest.setVisibility(View.VISIBLE);
        if(theme1.equals("coffee")) blockCoffee.setVisibility(View.VISIBLE);
        if(theme1.equals("forgive")) blockForgive.setVisibility(View.VISIBLE);

        //为所有主题添加点击事件
        CardView candy = (CardView) findViewById(R.id.theme_select_candy);
        candy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mdialog("candy","糖果甜心");
            }
        });

        CardView sea = (CardView) findViewById(R.id.theme_select_sea);
        sea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mdialog("sea","漫步海边");
            }
        });

        CardView pink = (CardView) findViewById(R.id.theme_select_pink);
        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mdialog("pink","粉樱魅惑");
            }
        });

        CardView harvest = (CardView) findViewById(R.id.theme_select_harvest);
        harvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mdialog("harvest","丰收季节");
            }
        });

        CardView forgive = (CardView) findViewById(R.id.theme_select_forgive);
        forgive.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mdialog("forgive","选择原谅");
            }
        });

        CardView butterfly = (CardView) findViewById(R.id.theme_select_butterfly);
        butterfly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mdialog("butterfly","跃然紫上");
            }
        });

        CardView coffee = (CardView) findViewById(R.id.theme_select_coffee);
        coffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mdialog("coffee","咖啡时刻");
            }
        });

        CardView wine = (CardView) findViewById(R.id.theme_select_wine);
        wine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mdialog("sexless","嗯性冷淡");
            }
        });
    }

    //弹出提示对话框
    private void Mdialog(final String theme,String name){
        AlertDialog.Builder dialog = new AlertDialog.Builder(SelectThemeActivity.this);
        dialog.setMessage("您确定要更换为" + name +"主题吗？");
        dialog.setCancelable(false);
        dialog.setNegativeButton("算了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        dialog.setPositiveButton("换吧", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putString("theme",theme);
                editor.putBoolean("changed",true);
                editor.apply();

                finish();
                final Intent intent = new Intent(SelectThemeActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            default:
        }
        return true;
    }

}
