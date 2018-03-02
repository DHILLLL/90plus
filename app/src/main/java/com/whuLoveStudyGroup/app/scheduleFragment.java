package com.whuLoveStudyGroup.app;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by 635901193 on 2017/7/22.
 */

public class scheduleFragment extends Fragment {
    PercentRelativeLayout sch;
    int size,firstDay;
    int items = 0;
    int currentWeek;
    int[][] shownCourses = new int[8][14];
    TextView month,mon,tue,wed,thu,fri,sat,sun;
    boolean showAll;

    Bitmap bmp;
    List<String> list = new ArrayList();
    ImageView image;
    EditText u,p,c;
    CheckBox cb;
    View view1;
    GetInfoFromJWXT getInfoFromJWXT;


    private static final String TAG = "dongheyou";

    private IntentFilter intentFilter;
    private MyBroadcastReceiver myBroadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;

    //设置接收广播事件
    public class MyBroadcastReceiver extends BroadcastReceiver {
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //设置接收广播
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.whuLoveStudyGroup.app.UPDATE_SCHEDULE");
        localBroadcastManager = localBroadcastManager.getInstance(getContext());
        myBroadcastReceiver = new MyBroadcastReceiver();
        localBroadcastManager.registerReceiver(myBroadcastReceiver,intentFilter);

        //更换主题后自动更新课程颜色
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean("changed",false)){
            changeSet();
            SharedPreferences.Editor editor = getContext().getSharedPreferences("data",Context.MODE_PRIVATE).edit();
            editor.putBoolean("changed",false);
            editor.apply();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.schedule,container,false);

        //获取屏幕宽度并计算对应课表长度
        WindowManager manager = getActivity().getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        sch = (PercentRelativeLayout) view.findViewById(R.id.schedule);
        sch.getLayoutParams().height = (int)(width * 13 / 7.3);
        size = (int)(width * 0.13);

        month = (TextView) view.findViewById(R.id.set_month);
        mon = (TextView) view.findViewById(R.id.set_monday);
        tue = (TextView) view.findViewById(R.id.set_tuesday);
        wed = (TextView) view.findViewById(R.id.set_wednesday);
        thu = (TextView) view.findViewById(R.id.set_thursday);
        fri = (TextView) view.findViewById(R.id.set_friday);
        sat = (TextView) view.findViewById(R.id.set_saturday);
        sun = (TextView) view.findViewById(R.id.set_sunday);




        //下拉更新课程
//        final SwipeRefreshLayout srl = (SwipeRefreshLayout) view.findViewById(R.id.schedule_refresh);
//        srl.setEnabled(false);
////        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
////            @Override
////            public void onRefresh() {
////               papapa();
////            }
////        });

        return view;
    }

    private void papapa(){

        getInfoFromJWXT = new GetInfoFromJWXT();
        view1 = View.inflate(getContext(),R.layout.pa_dialog,null);
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
                        Toast.makeText(getContext(), "网络连接错误，请重试", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                    bmp = BitmapFactory.decodeStream(is);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            final AlertDialog dialog = builder.create();

                            dialog.setTitle("登录教务系统");
                            dialog.setView(view1,100,40,100,0);

                            SharedPreferences sp = getActivity().getSharedPreferences("data",getActivity().MODE_PRIVATE);

                            if(sp.getBoolean("remember",false)){
                                u.setText(sp.getString("username",""));
                                p.setText(sp.getString("password",""));
                                cb.setChecked(true);
                            }

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
                                        Toast.makeText(getContext(), "网络连接错误，请检查网络连接后重试", Toast.LENGTH_SHORT).show();
                                    }
                                    bmp = BitmapFactory.decodeStream(is2);
                                    image.setImageBitmap(bmp);
                                }
                            });
                            dialog.setButton(Dialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (cb.isChecked()){
                                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",getActivity().MODE_PRIVATE).edit();
                                        editor.putString("username",u.getText().toString());
                                        editor.putString("password",p.getText().toString());
                                        editor.putBoolean("remember",true);
                                        editor.apply();
                                    }else{
                                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",getActivity().MODE_PRIVATE).edit();
                                        editor.putBoolean("remember",false);
                                        editor.apply();
                                    }

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                List<Map<String, String>> maps = null;
                                                try {
                                                    maps = getInfoFromJWXT.getCourseData(u.getText().toString(), getInfoFromJWXT.md5(p.getText().toString()), c.getText().toString(), getInfoFromJWXT.getCookie());
                                                } catch (GetInfoFromJWXT.VerificationCodeException e) {
                                                    e.printStackTrace();
                                                    Looper.prepare();
                                                    Toast.makeText(getContext(), "验证码错误，请重试", Toast.LENGTH_SHORT).show();
                                                    Looper.loop();
                                                } catch (GetInfoFromJWXT.UsernamePasswordErrorException e) {
                                                    e.printStackTrace();
                                                    Looper.prepare();
                                                    Toast.makeText(getContext(), "用户名/密码错误，请重试", Toast.LENGTH_SHORT).show();
                                                    Looper.loop();
                                                } catch (GetInfoFromJWXT.TimeoutException e) {
                                                    e.printStackTrace();
                                                    Looper.prepare();
                                                    Toast.makeText(getContext(), "会话超时，请重试", Toast.LENGTH_SHORT).show();
                                                    Looper.loop();
                                                }
                                                Map<String,Integer> names = new HashMap<>();
                                                for (Map map : maps){
                                                    String course = map.get("lessonName").toString();
                                                    if(!names.containsKey(course)){
                                                        names.put(course,1);
                                                    }else {
                                                        Integer temp = names.get(course) + 1;
                                                        names.put(course,temp);
                                                    }
                                                }

                                                List<Course> temp;

                                                for (Map map : maps){
                                                    temp = DataSupport.where("name = ?",map.get("lessonName").toString()).find(Course.class);
                                                    if (temp.size() >= names.get(map.get("lessonName").toString())) continue;

                                                    Course course = new Course();
                                                    course.setName(map.get("lessonName").toString());
                                                    course.setPlace(map.get("areaName").toString() + " " + map.get("classRoom").toString());
                                                    course.setTeacher(map.get("teacherName").toString());
                                                    course.setType(map.get("planType").toString());
                                                    course.setCredit(map.get("credit").toString());
                                                    course.setWeekFrom(Integer.valueOf(map.get("beginWeek").toString()));
                                                    course.setWeekTo(Integer.parseInt(map.get("endWeek").toString()));
                                                    course.setTimesPerWeek(names.get(map.get("lessonName").toString()));
                                                    course.setWeekday(Integer.parseInt(map.get("day").toString()));
                                                    course.setHourFrom(Integer.parseInt(map.get("beginTime").toString()));
                                                    course.setHourTo(Integer.parseInt(map.get("endTime").toString()));
                                                    course.setNote(map.get("note").toString());
                                                    course.setLessoneID(map.get("lessonID").toString());

                                                    if(map.get("weekInterVal=").toString().equals("1"))
                                                        course.setEveryWeek(true);
                                                    else
                                                        course.setEveryWeek(false);

                                                    course.setHomework(false);
                                                    course.save();
                                                }

                                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("data",getActivity().MODE_PRIVATE).edit();
                                                editor.putInt("firstDay",getInfoFromJWXT.getTermStartDayIndex() - 7);
                                                currentWeek = CourseWidget.setCurrentWeek();
                                                editor.putInt("currentWeek",currentWeek);
                                                editor.apply();

                                                changeSet();

                                                final Intent intent = new Intent(getContext(),MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                getActivity().overridePendingTransition(0,0);



                                                Looper.prepare();
                                                Toast.makeText(getContext(), "课程刷新完毕", Toast.LENGTH_SHORT).show();
                                                Looper.loop();

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


    //显示日期
    private void setDate(){
        if(firstDay == 0) return;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR,firstDay + 7 * currentWeek);
        month.setText((calendar.get(Calendar.MONTH) + 1) + "月");

        sun.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        calendar.add(Calendar.DATE,1);
        mon.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        calendar.add(Calendar.DATE,1);
        tue.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        calendar.add(Calendar.DATE,1);
        wed.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        calendar.add(Calendar.DATE,1);
        thu.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        calendar.add(Calendar.DATE,1);
        fri.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        calendar.add(Calendar.DATE,1);
        sat.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));

    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sp,sp1;

        try {
            //获取当前周
            sp = getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
            currentWeek = sp.getInt("currentWeek",1);
            firstDay = sp.getInt("firstDay",0);
            sp1 = getContext().getSharedPreferences("Setting",Context.MODE_PRIVATE);
            showAll = sp1.getBoolean("showAll",true);
        }catch (Exception e){
            e.printStackTrace();
        }


        //刷新
        refresh();
    }

    public void changeSet(){
        //获取全部课程名的无重复列表
        List<Course> courses = DataSupport.findAll(Course.class);
        List<String> names = new ArrayList<>();
        for(Course course:courses){
            boolean repeat = false;
            for (int i = 0;i<names.size();i++){
                if(names.get(i).equals(course.getName())) {
                    repeat = true;break;
                }
            }
            if (!repeat) names.add(course.getName());
        }

        //获取当前主题包含颜色
        SharedPreferences sp = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        List<Theme> themes = DataSupport.where("name = ?",sp.getString("theme","candy")).find(Theme.class);
        Theme theme = themes.get(0);
        int[] colors = {theme.getColor0(),theme.getColor1(),theme.getColor2(),
                theme.getColor3(),theme.getColor4(),theme.getColor5(),theme.getColor6(),
                theme.getColor7(),theme.getColor8(),theme.getColor9(),theme.getColor10(),
                theme.getColor11(),theme.getColor12(),theme.getColor13(),theme.getColor14()};
        //打乱颜色
        Random r = new Random();
        for(int i = 0;i<15;i++){
            int x = r.nextInt(15);
            int temp = colors[i];
            colors[i] = colors[x];
            colors[x] = temp;
        }
        //为每门课分配打乱后的颜色
        for(int i = 0;i<names.size();i++){
            ContentValues value = new ContentValues();
            value.put("color",colors[i]);
            DataSupport.updateAll(Course.class,value,"name = ?",names.get(i));
        }

        //发送广播更新作业界面
        Intent intent = new Intent("com.whuLoveStudyGroup.app.UPDATE_HOMEWORK");
        localBroadcastManager.sendBroadcast(intent);

        refresh();
    }

    //将参数中的课程显示在课程表上
    private void AddItem(final Course course, boolean thisWeek){
        if(!showAll && !thisWeek) return;


        int max = 0,wd = course.getWeekday(),hf = course.getHourFrom(),ht = course.getHourTo();
        for(int i = hf;i <= ht;i++){
            if (shownCourses[wd][i] > max) max = shownCourses[wd][i];
            shownCourses[wd][i]++;
        }


        //该课程总layout
        FrameLayout frameLayout2 = new FrameLayout(getContext());

        //添加tag方便刷新时清除旧课程
        frameLayout2.setTag(items);

        //设置长度
        int length2 = course.getHourTo() - course.getHourFrom() + 1;
        PercentRelativeLayout.LayoutParams rlp2 = new PercentRelativeLayout.LayoutParams(size,size * length2);

        //设置水平位置
        switch (course.getWeekday()){
            case 1:
                rlp2.addRule(PercentRelativeLayout.RIGHT_OF,R.id.scd_sunday);break;
            case 2:
                rlp2.addRule(PercentRelativeLayout.RIGHT_OF,R.id.scd_monday);break;
            case 3:
                rlp2.addRule(PercentRelativeLayout.RIGHT_OF,R.id.scd_tuesday);break;
            case 4:
                rlp2.addRule(PercentRelativeLayout.RIGHT_OF,R.id.scd_wednesday);break;
            case 5:
                rlp2.addRule(PercentRelativeLayout.RIGHT_OF,R.id.scd_thursday);break;
            case 6:
                rlp2.addRule(PercentRelativeLayout.RIGHT_OF,R.id.scd_friday);break;
            case 7:
                rlp2.addRule(PercentRelativeLayout.RIGHT_OF,R.id.scd_month);break;
            default:
        }

        //设置垂直位置
        switch (course.getHourFrom()){
            case 1:
                rlp2.addRule(PercentRelativeLayout.BELOW,R.id.scd_month);break;
            case 2:
                rlp2.addRule(PercentRelativeLayout.BELOW,R.id.scd_c1);break;
            case 3:
                rlp2.addRule(PercentRelativeLayout.BELOW,R.id.scd_c2);break;
            case 4:
                rlp2.addRule(PercentRelativeLayout.BELOW,R.id.scd_c3);break;
            case 5:
                rlp2.addRule(PercentRelativeLayout.BELOW,R.id.scd_c4);break;
            case 6:
                rlp2.addRule(PercentRelativeLayout.BELOW,R.id.scd_c5);break;
            case 7:
                rlp2.addRule(PercentRelativeLayout.BELOW,R.id.scd_c6);break;
            case 8:
                rlp2.addRule(PercentRelativeLayout.BELOW,R.id.scd_c7);break;
            case 9:
                rlp2.addRule(PercentRelativeLayout.BELOW,R.id.scd_c8);break;
            case 10:
                rlp2.addRule(PercentRelativeLayout.BELOW,R.id.scd_c9);break;
            case 11:
                rlp2.addRule(PercentRelativeLayout.BELOW,R.id.scd_c10);break;
            case 12:
                rlp2.addRule(PercentRelativeLayout.BELOW,R.id.scd_c11);break;
            case 13:
                rlp2.addRule(PercentRelativeLayout.BELOW,R.id.scd_c12);break;
        }

        frameLayout2.setLayoutParams(rlp2);
        sch.addView(frameLayout2);

        //圆角图形
        CardView cardview2 = new CardView(getContext());
        FrameLayout.LayoutParams fl12 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        fl12.setMargins(3 + max * 10,3,3,3);
        cardview2.setLayoutParams(fl12);
        cardview2.setRadius(12);
        cardview2.setCardBackgroundColor(thisWeek?course.getColor():0XFFEAEAEA);
        frameLayout2.addView(cardview2);

        //文字信息
        TextView textview2 = new TextView(getContext());
        CardView.LayoutParams cl2 = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cl2.setMargins(6,9,6,9);
        textview2.setLayoutParams(cl2);
        textview2.setGravity(Gravity.TOP|Gravity.CENTER);
        textview2.setText(course.getName() + (course.getPlace().equals("")? "":" @ " ) + course.getPlace());
        textview2.setTextColor(thisWeek?0xFFFFFFFF:0xFFABABAB);
        textview2.setTextSize(12);
        cardview2.addView(textview2);

        //作业标记
        if(course.getHomework()){
            Button corner2 = new Button(getContext());
            FrameLayout.LayoutParams fl22 = new FrameLayout.LayoutParams(40,40);
            fl22.setMargins(size - 40,length2 * size - 40,0,0);
            corner2.setLayoutParams(fl22);
            corner2.setBackgroundResource(R.drawable.corner);
            frameLayout2.addView(corner2);
        }

        //点击事件
        if(max == 0){
            frameLayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),courseActivity.class);
                    intent.putExtra("course",course.getName());
                    intent.putExtra("ID",course.getLessoneID());
                    startActivity(intent);
                }
            });
        }else{
            frameLayout2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    final List<Course> temp = DataSupport.where("weekday = ? and hourFrom <= ? and hourTo >= ? or weekday = ? and hourFrom >= ? and hourTo <= ? or weekday = ? and hourFrom <= ? and hourTo >= ?"
                            ,String.valueOf(course.getWeekday()),String.valueOf(course.getHourFrom()),String.valueOf(course.getHourFrom()),
                            String.valueOf(course.getWeekday()),String.valueOf(course.getHourFrom()),String.valueOf(course.getHourTo()),
                            String.valueOf(course.getWeekday()),String.valueOf(course.getHourTo()),String.valueOf(course.getHourTo())).find(Course.class);
                    String[] options = new String[temp.size()];
                    for(int i = 0;i<temp.size();i++){
                        options[i] = temp.get(i).getName() + " @ " + temp.get(i).getPlace();
                    }
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(),courseActivity.class);
                            intent.putExtra("course",temp.get(which).getName());
                            intent.putExtra("ID",temp.get(which).getLessoneID());

                            startActivity(intent);
                        }
                    }).show();
                }
            });
        }


        //已添加课程数加一
        items++;

    }

    public void refresh(){
        setDate();
        clear();
        //获取所有课程
        List<Course> courses = DataSupport.order("hourFrom asc,hourTo desc").find(Course.class);
        //依次显示课程
        for(Course course: courses){

            if ((course.getWeekFrom() <= currentWeek) && course.getWeekTo() >= currentWeek){
                if (course.getEveryWeek() || (!course.getEveryWeek() && (currentWeek % 2 == 0))){
                    AddItem(course,true);
                }else {
                    AddItem(course,false);
                }
            }else {
                AddItem(course,false);
            }
        }
    }

    public void clear(){
        //根据已添加课程数和tag删除所有课程显示
        for(int j = 0;j<items;j++){
            FrameLayout f = (FrameLayout) sch.findViewWithTag(j);
            sch.removeView(f);
        }
        for(int i = 0;i<8;i++){
            for(int j = 0;j<14;j++){
                shownCourses[i][j] = 0;
            }
        }
    }


}
