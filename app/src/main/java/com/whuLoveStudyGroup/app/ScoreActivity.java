package com.whuLoveStudyGroup.app;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoreActivity extends MyActivity {
    private static final String TAG = "dong";  

    private ScoreAdapter adapter;
    Bitmap bmp;
    String sort[] = {"score","name","credit","teacher","type","gpa"};
    int SORT = 0;
    int time = 0;
    List<Score> list,result;
    List<String> semesters = new ArrayList<>();
    ImageView image;
    EditText u,p,c;
    TextView title,Sscore,Sname,Scredit,Steacher,Stype,Sgpa;
    TextView[] tvs = {Sscore,Sname,Scredit,Steacher,Stype,Sgpa};
    View view1;
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    CheckBox cb;
    String[] resource = {"大一上学期","大一下学期","大二上学期","大二下学期","大三上学期","大三下学期","大四上学期","大四下学期"};
    String[] xueqi;
    int way;
    List<Integer> chosenSemesters,chosenTypes;
    String[] types = {"公共必修","公共选修","专业必修","专业选修"};

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

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
                .detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        //设置接收广播
        localBroadcastManager = localBroadcastManager.getInstance(this);

        intentFilter = new IntentFilter();
        intentFilter.addAction("com.whuLoveStudyGroup.app.UPDATE_SCORE");
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

        tvs[0] = (TextView)findViewById(R.id.score_sort_score);
        tvs[1] = (TextView)findViewById(R.id.score_sort_name);
        tvs[2] = (TextView)findViewById(R.id.score_sort_credit);
        tvs[3] = (TextView)findViewById(R.id.score_sort_teacher);
        tvs[4] = (TextView)findViewById(R.id.score_sort_type);
        tvs[5] = (TextView)findViewById(R.id.score_sort_gpa);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.score_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        list = DataSupport.select("semester").order("semester desc").find(Score.class);

        if (list.size() != 0){

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

            xueqi = new String[semesters.size()];
            for (int i = 0;i<semesters.size();i++){
                xueqi[i] = resource[semesters.size() - 1 - i];
            }


            tvs[SORT].setBackgroundColor(0xFFAAAAAA);
            tvs[SORT].setTextColor(0xFFFFFFFF);

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ScoreActivity.this);
                    builder.setItems(xueqi, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            time = which;
                            Intent intent = new Intent("com.whuLoveStudyGroup.app.UPDATE_SCORE");
                            localBroadcastManager.sendBroadcast(intent);
                        }
                    });
                    builder.show();
                }
            });

            result = DataSupport.where("semester = ?",semesters.get(time)).order(sort[SORT] + " desc").find(Score.class);
            title.setText(xueqi[time]);

            adapter = new ScoreAdapter(result);
            recyclerView.setAdapter(adapter);

            for(int i = 0;i<6;i++){
                Log.d(TAG, "**** " + i);
                final int temp = i;
                tvs[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvs[SORT].setBackgroundColor(0xFFFFFFFF);
                        tvs[SORT].setTextColor(0xFF777777);
                        SORT = temp;

                        Intent intent = new Intent("com.whuLoveStudyGroup.app.UPDATE_SCORE");
                        localBroadcastManager.sendBroadcast(intent);
                    }
                });
            }

        }else{
            title.setText("请下拉更新信息");
        }

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
            case R.id.score_calculate:
                if(list.size() != 0){
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ScoreActivity.this);
                    builder.setTitle("请选择计算方式");
                    final String[] options = {"累计保研GPA(无公选)","累计平均GPA(全部)","当前页保研GPA(无公选)","当前页平均GPA(全部)","累计均绩","自定义"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<Score> temp;
                            switch (which){
                                case 0:
                                    temp = DataSupport.where("type != ?","公共选修").find(Score.class);
                                    way = 0;
                                    calculate(temp);
                                    break;
                                case 1:
                                    temp = DataSupport.findAll(Score.class);
                                    way = 0;
                                    calculate(temp);
                                    break;
                                case 2:
                                    temp = DataSupport.where("type != ? and semester = ?","公共选修",semesters.get(time)).find(Score.class);
                                    way = 0;
                                    calculate(temp);
                                    break;
                                case 3:
                                    temp = DataSupport.where("semester = ?",semesters.get(time)).find(Score.class);
                                    way = 0;
                                    calculate(temp);
                                    break;
                                case 4:
                                    temp = DataSupport.findAll(Score.class);
                                    way = 1;
                                    calculate(temp);
                                    break;
                                case 5:
                                    DIY();
                                    break;
                                default:

                            }
                        }
                    }).show();
                }else {
                    Toast.makeText(ScoreActivity.this,"还是先刷新成绩吧",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
        return true;
    }

    private void DIY(){
        final boolean[] bs = new boolean[xueqi.length];
        for(boolean b : bs) b = false;
        chosenSemesters = new ArrayList<>();
        chosenTypes = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(ScoreActivity.this);
        builder.setTitle("选择计算项目");
        final String[] options = {"平均GPA","均绩","总学分"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                way = which;

                AlertDialog.Builder builder = new AlertDialog.Builder(ScoreActivity.this);
                builder.setTitle("选择计算学期(可多选)");
                builder.setMultiChoiceItems(xueqi, bs, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked) chosenSemesters.add(which);
                        else {
                            for(int i = 0;i<chosenSemesters.size();i++){
                                if (chosenSemesters.get(i) == which) chosenSemesters.remove(i);
                            }
                        }
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(ScoreActivity.this);
                        builder.setTitle("选择计算类型(可多选)");
                        builder.setMultiChoiceItems(types, new boolean[]{false,false,false,false}, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if(isChecked) chosenTypes.add(which);
                                else {
                                    for(int i = 0;i<chosenTypes.size();i++){
                                        if (chosenTypes.get(i) == which) chosenTypes.remove(i);
                                    }
                                }
                            }
                        });
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                List<Score> temp = new ArrayList<Score>();
                                for(int i = 0;i < chosenTypes.size();i++){
                                    for(int j = 0;j<chosenSemesters.size();j++){
                                        temp.addAll(DataSupport.where("type = ? and semester = ?",types[chosenTypes.get(i)],semesters.get(chosenSemesters.get(j))).find(Score.class));
                                    }
                                }
                                calculate(temp);
                            }
                        });
                        builder.show();
                    }
                });
                builder.show();

            }
        }).show();
    }

    private void calculate(List<Score> temp){

        double credits = 0,total = 0,answer = 0;
        String str;
        BigDecimal bd;

        switch (way){
            case 0:
                for(Score m : temp){
                    if (m.getScore() != -1.0){
                        total += m.getCredit() * m.getGpa();
                        credits += m.getCredit();
                    }
                }
                if (credits == 0){
                    str = "无计算结果";
                    break;
                }
                bd = new BigDecimal(total);
                total = bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                bd = new BigDecimal(total / credits);
                answer = bd.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
                str = "参与计算总GPA * 学分：" + total + "\n参与计算总学分：" + credits + "\n结果：" + answer;
                break;
            case 1:
                for(Score m : temp){
                    if (m.getScore() != -1.0){
                        total += m.getCredit() * m.getScore();
                        credits += m.getCredit();
                    }
                }
                if (credits == 0){
                    str = "无计算结果";
                    break;
                }
                bd = new BigDecimal(total);
                total = bd.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                bd = new BigDecimal(total / credits);
                answer = bd.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
                str = "参与计算总GPA * 成绩：" + total + "\n参与计算总学分：" + credits + "\n结果：" + answer;
                break;
            case 2:
                for (Score m : temp)
                    answer += m.getCredit();
                str = "结果：" + answer;
                break;
            default:
                str = "";
        }


        AlertDialog.Builder builder1 = new AlertDialog.Builder(ScoreActivity.this);
        builder1.setMessage(str);
        builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        }).show();
    }

    private void refresh(){

        final GetInfoFromJWXT getInfoFromJWXT = new GetInfoFromJWXT();
        view1 = View.inflate(ScoreActivity.this,R.layout.pa_dialog,null);
        u = (EditText)view1.findViewById(R.id.pa_username);
        p = (EditText)view1.findViewById(R.id.pa_password);
        c = (EditText)view1.findViewById(R.id.pa_code);
        cb = (CheckBox)view1.findViewById(R.id.pa_check);
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
                        Toast.makeText(ScoreActivity.this, "网络连接错误，请检查网络连接后重试", Toast.LENGTH_SHORT).show();
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
                            image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    InputStream is2 = null;
                                    try {
                                        is2 = getInfoFromJWXT.getGenImg();
                                    }
                                    catch (GetInfoFromJWXT.NetworkErrorException ex) {
                                        ex.printStackTrace();
                                        Toast.makeText(ScoreActivity.this, "网络连接错误，请重试", Toast.LENGTH_SHORT).show();
                                    }
                                    bmp = BitmapFactory.decodeStream(is2);
                                    image.setImageBitmap(bmp);
                                }
                            });

                            SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);

                            if(sp.getBoolean("remember",false)){
                                u.setText(sp.getString("username",""));
                                p.setText(sp.getString("password",""));
                                cb.setChecked(true);
                            }

                            dialog.setButton(Dialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (cb.isChecked()){
                                        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                                        editor.putString("username",u.getText().toString());
                                        editor.putString("password",p.getText().toString());
                                        editor.putBoolean("remember",true);
                                        editor.apply();
                                    }else{
                                        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                                        editor.putBoolean("remember",false);
                                        editor.apply();
                                    }

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

                                                    Intent intent = new Intent("com.whuLoveStudyGroup.app.UPDATE_SCORE");
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
