package com.example.app;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.webkit.DownloadListener;

import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends MyActivity {
    private static final String TAG = "dong";

    private DownloadService.DownloadBinder downloadBinder;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

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
        adapter = new UpdateAdapter(list);
        recyclerView.setAdapter(adapter);

        Intent intent = new Intent(this, DownloadService.class);
        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);
        if (ContextCompat.checkSelfPermission(UpdateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(UpdateActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }

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
        final GetVersionInfoFromDB getVersionInfoFromDB = new GetVersionInfoFromDB();
        getVersionInfoFromDB.connAndGetVersionInfo();
        String[] verInfoFromDB = {"版本序号：" + getVersionInfoFromDB.getVersionID(), "版        本：" + getVersionInfoFromDB.getVersion(),
                "关键版本：" + getVersionInfoFromDB.isCritical(), "文件大小：" + getVersionInfoFromDB.getFileSize() + "MB",
                "下载地址：" + getVersionInfoFromDB.getDownloadAddress(), "更新日志：" + getVersionInfoFromDB.getChangeLog()};
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        // TODO change the code below!!!
        builder.setTitle("最新版本信息 -- DevUseOnly");
        builder.setItems(verInfoFromDB, null);
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = getVersionInfoFromDB.getDownloadAddress();
                downloadBinder.startDownload(url);
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


