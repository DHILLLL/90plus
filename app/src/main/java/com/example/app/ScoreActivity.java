package com.example.app;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreActivity extends MyActivity {
    private static final String TAG = "dong";  

    private ScoreAdapter adapter;
    Bitmap bmp;
    String s;
    List<Score> list,result;
    List<String> semesters = new ArrayList<>();
    ImageView image;
    EditText u,p,c;
    TextView title;
    View view1;
    RecyclerView recyclerView;

    private IntentFilter intentFilter;
    private MyBroadcastReceiver myBroadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;

    //设置接收广播事件
    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            onResume();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //停止接收广播
        localBroadcastManager.unregisterReceiver(myBroadcastReceiver);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置接收广播
        localBroadcastManager = localBroadcastManager.getInstance(this);

        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.app.UPDATE_SCORE");
        myBroadcastReceiver = new MyBroadcastReceiver();
        localBroadcastManager.registerReceiver(myBroadcastReceiver,intentFilter);

        setContentView(R.layout.activity_score);
        Toolbar toolbar = (Toolbar) findViewById(R.id.score_toolbar);
        title = (TextView) findViewById(R.id.score_semester);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        recyclerView = (RecyclerView) findViewById(R.id.score_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        list = DataSupport.select("semester").order("semester desc").find(Score.class);
        result = DataSupport.where("semester = ?",list.get(0).getSemester()).find(Score.class);

    }

    @Override
    protected void onResume() {
        super.onResume();

        title.setText(result.get(0).getSemester());
        adapter = new ScoreAdapter(result);
        recyclerView.setAdapter(adapter);

        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Score score : list){
                    boolean exist = false;
                    for(String semester : semesters){
                        if(score.getSemester().equals(semester)){
                            exist = true;
                            break;
                        }
                    }
                    if (!exist) semesters.add(score.getSemester());
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(ScoreActivity.this);

                builder.setItems(semesters.toArray(new String[semesters.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result = DataSupport.where("semester = ?",semesters.get(which)).find(Score.class);
                        Intent intent = new Intent("com.example.app.UPDATE_SCORE");
                        localBroadcastManager.sendBroadcast(intent);
                    }
                });
                builder.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.score_menu,menu);
        return true;
    }

    //设置左上右上按钮事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.score_refresh:
                refresh();
                break;
            default:
        }
        return true;
    }

    private void refresh(){

        final GetInfoFromJWXT getInfoFromJWXT = new GetInfoFromJWXT();
        view1 = View.inflate(ScoreActivity.this,R.layout.pa_dialog,null);
        u = (EditText)view1.findViewById(R.id.pa_username);
        p = (EditText)view1.findViewById(R.id.pa_password);
        c = (EditText)view1.findViewById(R.id.pa_code);
        image = (ImageView)view1.findViewById(R.id.pa_image);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    InputStream is = null;
                    try {
                        is = getInfoFromJWXT.getGenImg();
                    }
                    catch (GetInfoFromJWXT.NetworkErrorException ex) {
                        ex.printStackTrace();
                        Looper.prepare();
                        Toast.makeText(ScoreActivity.this, "网络连接错误，请重试", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    bmp = BitmapFactory.decodeStream(is);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ScoreActivity.this);
                            final AlertDialog dialog = builder.create();

                            dialog.setTitle("登录教务系统");
                            dialog.setView(view1,100,40,100,0);
                            image.setImageBitmap(bmp);
                            dialog.setButton(Dialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                try {
                                                    List<Map<String,String>> download  = getInfoFromJWXT.getScoresData(u.getText().toString(), getInfoFromJWXT.md5(p.getText().toString()), c.getText().toString(), getInfoFromJWXT.getCookie());

                                                    DataSupport.deleteAll(Score.class);

                                                    for (Map<String,String> map : download){
                                                        Score score = new Score();
                                                        score.setName(map.get("课程名称"));
                                                        score.setCredit(Double.parseDouble(map.get("学分")));
                                                        score.setNumber(map.get("课头号"));
                                                        score.setSemester(map.get("学年") + "-" + (Integer.parseInt(map.get("学年"))+1) + "学年" + map.get("学期") + "学期");
                                                        score.setTeacher(map.get("教师"));
                                                        score.setType(map.get("课程类型"));
                                                        if (map.get("成绩").equals("")){
                                                            score.setGpa(-1.0);
                                                            score.setScore(-1.0);
                                                        }else{
                                                            double SCORE = Double.parseDouble(map.get("成绩"));
                                                            score.setScore(SCORE);

                                                            if(SCORE >= 90.0){
                                                                score.setGpa(4.0);
                                                            }else if(SCORE >= 85.0){
                                                                score.setGpa(3.7);
                                                            }else if(SCORE >= 82.0){
                                                                score.setGpa(3.3);
                                                            }else if(SCORE >= 78.0){
                                                                score.setGpa(3.0);
                                                            }else if(SCORE >= 75.0){
                                                                score.setGpa(2.7);
                                                            }else if(SCORE >= 72.0){
                                                                score.setGpa(2.3);
                                                            }else if(SCORE >= 68.0){
                                                                score.setGpa(2.0);
                                                            }else if(SCORE >= 64.0){
                                                                score.setGpa(1.5);
                                                            }else if(SCORE >= 60.0){
                                                                score.setGpa(1.0);
                                                            }else {
                                                                score.setGpa(0.0);
                                                            }
                                                        }

                                                        score.save();
                                                    }

                                                    list = DataSupport.select("semester").order("semester desc").find(Score.class);
                                                    result = DataSupport.where("semester = ?",list.get(0).getSemester()).find(Score.class);

                                                    Intent intent = new Intent("com.example.app.UPDATE_SCORE");
                                                    localBroadcastManager.sendBroadcast(intent);

                                                } catch (GetInfoFromJWXT.VerificationCodeException e) {
                                                    e.printStackTrace();
                                                    Looper.prepare();
                                                    Toast.makeText(ScoreActivity.this, "验证码错误，请重试", Toast.LENGTH_SHORT).show();
                                                    Looper.loop();
                                                } catch (GetInfoFromJWXT.UsernamePasswordErrorException e) {
                                                    e.printStackTrace();
                                                    Looper.prepare();
                                                    Toast.makeText(ScoreActivity.this, "用户名/密码错误，请重试", Toast.LENGTH_SHORT).show();
                                                    Looper.loop();
                                                } catch (GetInfoFromJWXT.TimeoutException e) {
                                                    e.printStackTrace();
                                                    Looper.prepare();
                                                    Toast.makeText(ScoreActivity.this, "会话超时，请重试", Toast.LENGTH_SHORT).show();
                                                    Looper.loop();
                                                }

                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();
                                }
                            });
                            dialog.setButton(Dialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            dialog.show();

                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
