package com.example.app;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends MyActivity {
    private static final String TAG = "dong";

    private UpdateInformation ui = new UpdateInformation("Version 0.5.1.171009","1. 解决了修改课程信息后作业标记失效的bug\n2. 解决了更换主题闪退的bug（也许吧）\n3. 添加了不显示非当周课程的选项（侧滑菜单-系统设置中）");

    private List<UpdateInformation> list = new ArrayList<>();
    private UpdateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.update_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.update_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        init();
        Log.d(TAG, String.valueOf(list.size()));
        adapter = new UpdateAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    private void init(){
        for (int i = 0;i < 20;i++)
            list.add(ui);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_menu,menu);
        return true;
    }

    //设置左上右上按钮事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.update_update:
                update();
                break;
            default:
        }
        return true;
    }

    private void update(){
        GetVersionInfoFromDB getVersionInfoFromDB = new GetVersionInfoFromDB();
        getVersionInfoFromDB.connAndGetVersionInfo();
        String[] verInfoFromDB = {"版本序号：" + getVersionInfoFromDB.getVersionID(), "版  本：" + getVersionInfoFromDB.getVersion(),
                "关键版本：" + getVersionInfoFromDB.isCritical(), "下载地址：" + getVersionInfoFromDB.getDownloadAddress(), "更新日志：" + getVersionInfoFromDB.getChangeLog()};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        // TODO change the code below!!!
        builder.setTitle("最新版本信息 -- DevUseOnly");
        builder.setItems(verInfoFromDB, null);
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

}
