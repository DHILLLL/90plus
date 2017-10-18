package com.example.app;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Looper;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class UpdateActivity extends MyActivity {
    private static final String TAG = "dong";
    private final GetVersionInfoFromDB getVersionInfoFromDB = new GetVersionInfoFromDB();

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
        try {
            unbindService(connection);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private UpdateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.update_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }


        Intent intent = new Intent(UpdateActivity.this, DownloadService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);

    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    getVersionInfoFromDB.connAndGetVersionInfo();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.update_list);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(UpdateActivity.this);
                            recyclerView.setLayoutManager(layoutManager);
                            adapter = new UpdateAdapter(getVersionInfoFromDB.getUpdateHistory());
                            recyclerView.setAdapter(adapter);
                        }
                    });

                } catch (GetVersionInfoFromDB.UnknownErrorException ex) {
                    ex.printStackTrace();
                    Looper.prepare();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpdateActivity.this, "未知错误，请稍后重试", Toast.LENGTH_SHORT).show();

                        }
                    });
                    Looper.loop();
                } catch (GetVersionInfoFromDB.NetworkErrorException ex) {
                    ex.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UpdateActivity.this, "网络连接错误，请检查网络连接后重试", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
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
        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        Log.d(TAG, "" + sp.getInt("version_id",0));
        try {
            final boolean critical = getVersionInfoFromDB.isLatestCritical();
            if (getVersionInfoFromDB.getLatestVersionID() > sp.getInt("version_id", 0)) {
                BigDecimal bigDecimal = new BigDecimal((double) getVersionInfoFromDB.getLatestFileSize() / (1024 * 1024));
                final String verInfoFromDB = "版  本  号：" + getVersionInfoFromDB.getLatestVersion() + "\n大        小：" +
                        bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "MB\n\n更新日志：\n" + getVersionInfoFromDB.getLatestChangeLog() +
                        (critical ? "\n\n(此为关键版本，若不更新则无法使用！)" : "");
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                builder.setTitle("检测到新版本");
                builder.setMessage(verInfoFromDB);
                builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = getVersionInfoFromDB.getLatestDownloadAddress();
                        downloadBinder.startDownload(url);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (critical)
                            ActivityCollector.finishAll();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(this, "当前已为最新版本", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(UpdateActivity.this, "网络连接错误，请检查网络连接后重试", Toast.LENGTH_SHORT).show();
        }

    }
}
