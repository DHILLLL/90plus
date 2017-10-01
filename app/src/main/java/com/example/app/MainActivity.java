package com.example.app;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends MyActivity implements
        BottomNavigationBar.OnTabSelectedListener,ViewPager.OnPageChangeListener{
    private static final String TAG = "dongheyou";

    private ViewPager viewPager;
    private BottomNavigationBar bottomNavigationBar;
    private List<Fragment> fragments;
    private DrawerLayout drawerLayout;
    private TextView middleTitle;
    private int currentWeek;
    private scheduleFragment sf;
    LocalBroadcastManager localBroadcastManager;

    Bitmap bmp;
    String s;
    List<String> list = new ArrayList();
    ImageView image;
    EditText u,p,c;
    View view1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.finishOthers();

        //DataSupport.deleteAll(Course.class);

        localBroadcastManager = LocalBroadcastManager.getInstance(MainActivity.this);

        Intent intent2 = new Intent("com.example.app.UPDATE_WIDGET");
        sendBroadcast(intent2);

        //第一次安装时初始化数据
        initiation(11);

        //获取当前周
        currentWeek = CourseWidget.setCurrentWeek();
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putInt("currentWeek",currentWeek);
        editor.apply();

        setContentView(R.layout.activity_main);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        initToolBar();
        initBottomNavigationBar();
        initViewPager();
        initNavigationView();

        CircleImageView civ = (CircleImageView) findViewById(R.id.left);
        civ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);

            }
        });




    }

    //初始化侧栏选项
    private void initNavigationView(){
        NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem item) {

                switch (item.getItemId()){
                    case R.id.theme:
                        item.setChecked(true);
                        Intent intent = new Intent(MainActivity.this,SelectThemeActivity.class);
                        startActivity(intent);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Thread.sleep(50);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        item.setChecked(false);
                                    }
                                });
                            }
                        }).start();
                        break;
                }
                return false;
            }
        });
    }

    private void initToolBar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.setDisplayShowTitleEnabled(false);
        middleTitle = (TextView) findViewById(R.id.middle_title);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    //右上各按钮点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_course:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("选择添加方式");
                final String[] addOptions = {"从课表导入","手动添加"};
                builder.setItems(addOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0){
                            papapa();
                        } else{
                            Intent intent = new Intent(MainActivity.this,AddCourseActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                builder.show();
                break;
            case R.id.add_homework:
                Intent intent1 = new Intent(MainActivity.this,AddHomeworkActivity.class);
                intent1.putExtra("course","");
                startActivity(intent1);
                break;
            default:
        }
        return true;
    }

    private void papapa(){

        final GetCoursesInfo getCoursesInfo = new GetCoursesInfo();
        view1 = View.inflate(MainActivity.this,R.layout.pa_dialog,null);
        u = (EditText)view1.findViewById(R.id.pa_username);
        p = (EditText)view1.findViewById(R.id.pa_password);
        c = (EditText)view1.findViewById(R.id.pa_code);
        image = (ImageView)view1.findViewById(R.id.pa_image);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://210.42.121.134/servlet/GenImg").build();
                    Response response = client.newCall(request).execute();
                    InputStream is = response.body().byteStream();
                    bmp = BitmapFactory.decodeStream(is);

                    Headers headers = response.headers();
                    List<String> cookies = headers.values("Set-Cookie");
                    String sesssion = cookies.get(0);
                    s = sesssion.substring(0,sesssion.indexOf(";"));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                                                List<Map<String,String>> maps = getCoursesInfo.getCourseData(u.getText().toString(),getCoursesInfo.md5(p.getText().toString()),c.getText().toString(),s);
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

                                                for (Map map : maps){
                                                    Course course = new Course();
                                                    course.setNote("");
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

                                                    if(map.get("weekInterVal=").toString().equals("1"))
                                                        course.setEveryWeek(true);
                                                    else
                                                        course.setEveryWeek(false);

                                                    course.setHomework(false);
                                                    course.save();
                                                }

                                                changeSet();

                                                Intent intent = new Intent("com.example.app.UPDATE_HOMEWORK");
                                                localBroadcastManager.sendBroadcast(intent);
                                                Intent intent1 = new Intent("com.example.app.UPDATE_SCHEDULE");
                                                localBroadcastManager.sendBroadcast(intent1);
                                                Intent intent2 = new Intent("com.example.app.UPDATE_WIDGET");
                                                sendBroadcast(intent2);

                                                Toast.makeText(MainActivity.this, "导入成功", Toast.LENGTH_SHORT).show();

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

    private void changeSet(){
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
        SharedPreferences sp = getSharedPreferences("data", Context.MODE_PRIVATE);
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
        Intent intent = new Intent("com.example.app.UPDATE_HOMEWORK");
        localBroadcastManager.sendBroadcast(intent);

    }

    //左右翻页后改变标题和按钮
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (viewPager.getCurrentItem()){
            case 0:
                menu.findItem(R.id.add_course).setVisible(true);
                menu.findItem(R.id.add_homework).setVisible(false);
                middleTitle.setText("第" +currentWeek+ "周");
                //标题1点击事件
                middleTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        final String[] week = {"第1周","第2周","第3周","第4周","第5周","第6周",
                                "第7周","第8周","第9周","第10周","第11周","第12周","第13周",
                                "第14周","第15周","第16周","第17周","第18周","第19周","第20周"};
                        builder.setItems(week, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                                editor.putInt("currentWeek",which+1);
                                editor.apply();
                                middleTitle.setText("第"+ (which+1) +"周");
                                SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
                                currentWeek = sp.getInt("currentWeek",1);

                                Intent intent = new Intent("com.example.app.UPDATE_SCHEDULE");
                                localBroadcastManager.sendBroadcast(intent);
                            }
                        });
                        builder.show();
                    }
                });
                break;
            case 1:
                menu.findItem(R.id.add_course).setVisible(false);
                menu.findItem(R.id.add_homework).setVisible(true);
                SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
                switch (sharedPreferences.getInt("display",0)){
                    case 0: middleTitle.setText("全部作业");break;
                    case 1: middleTitle.setText("未完成");break;
                    case 2: middleTitle.setText("已完成");break;
                    default:
                }
                //标题2点击事件
                middleTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        final String[] displays = {"显示全部","仅未完成","仅已完成"};
                        builder.setItems(displays, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                                editor.putInt("display",which);
                                editor.apply();
                                switch (which) {
                                    case 0:
                                        middleTitle.setText("全部作业");
                                        break;
                                    case 1:
                                        middleTitle.setText("仅未完成");
                                        break;
                                    case 2:
                                        middleTitle.setText("仅已完成");
                                        break;
                                    default:
                                        break;
                                }
                                Intent intent = new Intent("com.example.app.UPDATE_HOMEWORK");
                                localBroadcastManager.sendBroadcast(intent);
                            }
                        });
                        builder.show();
                    }
                });
                break;
            case 2:
                menu.findItem(R.id.add_course).setVisible(false);
                menu.findItem(R.id.add_homework).setVisible(false);
                middleTitle.setText("校园生活");
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    //初始化底栏
    private void initBottomNavigationBar(){
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bnb);
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.setAutoHideEnabled(true);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.ukebiao,"课表"))
                .addItem(new BottomNavigationItem(R.drawable.iizuoye,"作业"))
                .addItem(new BottomNavigationItem(R.drawable.ixiaoyuan,"校园"))
                .initialise();
    }

    //翻页相关
    private void initViewPager(){
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        fragments = new ArrayList<Fragment>();
        sf = new scheduleFragment();
        fragments.add(sf);
        fragments.add(new homeworkFragment());
        fragments.add(new playgroundFragment());

        viewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager(),fragments));
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(0);
    }



    @Override
    public void onTabSelected(int position) {
        viewPager.setCurrentItem(position);
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigationBar.selectTab(position);
        invalidateOptionsMenu();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private void initiation(int version){
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        //检测是否是第一次安装程序
        if(sharedPreferences.getBoolean("FirstStart" + String.valueOf(version),true)) {
            SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
            //标记从此不是第一次
            editor.putBoolean("FirstStart" + String.valueOf(version), false);
            editor.remove("FirstStart" + String.valueOf(version - 1));
            editor.putString("theme", "candy");
            editor.apply();


            /*

            DataSupport.deleteAll(Course.class);

            //添加示范课程数据
            Course course0 = new Course();
            course0.setName("计算机安全保密");
            course0.setPlace("3区3-401");
            course0.setTeacher("罗敏");
            course0.setType("专业选修");
            course0.setHomework(false);
            course0.setCredit("2.0");
            course0.setWeekFrom(1);
            course0.setWeekTo(12);
            course0.setTimesPerWeek(1);
            course0.setWeekday(1);
            course0.setHourFrom(3);
            course0.setHourTo(5);
            course0.setColor(0XFF93CBDC);
            course0.setEveryWeek(true);
            course0.save();

            Course course1 = new Course();
            course1.setName("计算机网络与通信原理");
            course1.setPlace("3区2-212");
            course1.setTeacher("胡继承");
            course1.setType("专业必修");
            course1.setHomework(false);
            course1.setCredit("3.0");
            course1.setWeekFrom(1);
            course1.setWeekTo(18);
            course1.setTimesPerWeek(1);
            course1.setWeekday(2);
            course1.setHourFrom(3);
            course1.setHourTo(5);
            course1.setColor(0XFF93A7DC);
            course1.setEveryWeek(true);
            course1.save();

            Course course2 = new Course();
            course2.setName("C++程序设计");
            course2.setPlace("3区1-327");
            course2.setTeacher("李雪飞");
            course2.setType("专业选修");
            course2.setHomework(false);
            course2.setCredit("3.0");
            course2.setWeekFrom(1);
            course2.setWeekTo(9);
            course2.setTimesPerWeek(2);
            course2.setWeekday(3);
            course2.setHourFrom(1);
            course2.setHourTo(2);
            course2.setColor(0XFFA493DC);
            course2.setEveryWeek(true);
            course2.save();

            Course course22 = new Course();
            course22.setName("C++程序设计");
            course22.setPlace("3区1-327");
            course22.setTeacher("李雪飞");
            course22.setType("专业选修");
            course22.setHomework(false);
            course22.setCredit("3.0");
            course22.setWeekFrom(1);
            course22.setWeekTo(9);
            course22.setTimesPerWeek(2);
            course22.setColor(0XFFA493DC);
            course22.setWeekday(5);
            course22.setHourFrom(1);
            course22.setHourTo(2);
            course22.setEveryWeek(true);
            course22.save();

            Course course3 = new Course();
            course3.setName("编译原理");
            course3.setPlace("3区2-104");
            course3.setTeacher("应时");
            course3.setType("专业必修");
            course3.setHomework(false);
            course3.setCredit("3.0");
            course3.setWeekFrom(1);
            course3.setWeekTo(18);
            course3.setTimesPerWeek(1);
            course3.setWeekday(4);
            course3.setHourFrom(3);
            course3.setHourTo(5);
            course3.setColor(0XFFC993DC);
            course3.setEveryWeek(true);
            course3.save();

            Course course4 = new Course();
            course4.setName("算法设计与分析");
            course4.setPlace("3区1-528");
            course4.setTeacher("田纲");
            course4.setType("专业选修");
            course4.setHomework(false);
            course4.setCredit("3.0");
            course4.setWeekFrom(1);
            course4.setWeekTo(18);
            course4.setTimesPerWeek(1);
            course4.setWeekday(1);
            course4.setHourFrom(6);
            course4.setHourTo(8);
            course4.setColor(0XFFDC93CB);
            course4.setEveryWeek(true);
            course4.save();

            Course course5 = new Course();
            course5.setName("计算机体系结构");
            course5.setPlace("3区1-203");
            course5.setTeacher("陈丹");
            course5.setType("专业必修");
            course5.setHomework(false);
            course5.setCredit("2.0");
            course5.setWeekFrom(1);
            course5.setWeekTo(12);
            course5.setTimesPerWeek(1);
            course5.setWeekday(2);
            course5.setHourFrom(6);
            course5.setHourTo(8);
            course5.setColor(0XFFDC93A7);
            course5.setEveryWeek(true);
            course5.save();
            */


            //删除旧主题数据
            DataSupport.deleteAll(Theme.class);

            //添加主题数据
            Theme theme = new Theme();
            theme.setName("coffee");theme.setColor0(0XFFAC9288);theme.setColor1(0XFFDBA775);theme.setColor2(0XFFC9B7A4);
            theme.setColor3(0XFFD2B48C);theme.setColor4(0XFFD38E82);theme.setColor5(0XFFC4A59C);theme.setColor6(0XFFDA9A7B);
            theme.setColor7(0XFFDCA68E);theme.setColor8(0XFFE0CCB1);theme.setColor9(0XFFBC8F8F);theme.setColor10(0XFFF7BF90);
            theme.setColor11(0XFFBFA683);theme.setColor12(0XFFD0A179);theme.setColor13(0XFFE3C4B7);theme.setColor14(0XFFBCAF98);
            theme.save();

            Theme theme1 = new Theme();
            theme1.setName("forgive");theme1.setColor0(0XFFADDAAF);theme1.setColor1(0XFF9CCC65);theme1.setColor2(0XFF7DCAC2);
            theme1.setColor3(0XFFB4D88A);theme1.setColor4(0XFFA4DAD5);theme1.setColor5(0XFF99D19B);theme1.setColor6(0XFF8FBC8F);
            theme1.setColor7(0XFFAFCFAF);theme1.setColor8(0XFFA2E8A2);theme1.setColor9(0XFFB4E4A1);theme1.setColor10(0XFF5CBCB3);
            theme1.setColor11(0XFFC5E0A5);theme1.setColor12(0XFF90BDAA);theme1.setColor13(0XFF89CB8C);theme1.setColor14(0XFFB9DACC);
            theme1.save();

            Theme theme2 = new Theme();
            theme2.setName("butterfly");theme2.setColor0(0XFF9EA7D9);theme2.setColor1(0XFFEDCCFF);theme2.setColor2(0XFFE3AAFF);
            theme2.setColor3(0XFFEFD0F5);theme2.setColor4(0XFFC6BDCA);theme2.setColor5(0XFFB993B0);theme2.setColor6(0XFFD08BE1);
            theme2.setColor7(0XFFB388FF);theme2.setColor8(0XFFCDB2C6);theme2.setColor9(0XFFA59BE1);theme2.setColor10(0XFFAC7FA0);
            theme2.setColor11(0XFF9678CD);theme2.setColor12(0XFFA597AC);theme2.setColor13(0XFFBEC4E6);theme2.setColor14(0XFF8792D2);
            theme2.save();

            Theme theme3 = new Theme();
            theme3.setName("harvest");theme3.setColor0(0XFFCDDC39);theme3.setColor1(0XFFFDD835);theme3.setColor2(0XFFFBC02D);
            theme3.setColor3(0XFFFFE608);theme3.setColor4(0XFFC0CA33);theme3.setColor5(0XFFDFE981);theme3.setColor6(0XFFFDDE91);
            theme3.setColor7(0XFFFFAC67);theme3.setColor8(0XFFFEE889);theme3.setColor9(0XFFFFC599);theme3.setColor10(0XFFBDDD98);
            theme3.setColor11(0XFFA4D072);theme3.setColor12(0XFFFFD166);theme3.setColor13(0XFFD5E9BE);theme3.setColor14(0XFFF6DD00);
            theme3.save();

            Theme theme4 = new Theme();
            theme4.setName("sea");theme4.setColor0(0XFF147CBD);theme4.setColor1(0XFFABCED8);theme4.setColor2(0XFF6CA0D0);
            theme4.setColor3(0XFF477FBD);theme4.setColor4(0XFF88ADA6);theme4.setColor5(0XFFAAB2B4);theme4.setColor6(0XFF8CCFDE);
            theme4.setColor7(0XFF478B9F);theme4.setColor8(0XFF87ACB3);theme4.setColor9(0XFF7AC0C9);theme4.setColor10(0XFFC0C0EC);
            theme4.setColor11(0XFFB1D3EC);theme4.setColor12(0XFF8989D9);theme4.setColor13(0XFF44CEF6);theme4.setColor14(0XFF8989D9);
            theme4.save();

            Theme theme5 = new Theme();
            theme5.setName("pink");theme5.setColor0(0XFFE0BCCF);theme5.setColor1(0XFFE6D4E0);theme5.setColor2(0XFFC5A4B0);
            theme5.setColor3(0XFFD5BAAC);theme5.setColor4(0XFFC68AAF);theme5.setColor5(0XFFC6BFDC);theme5.setColor6(0XFFE8E0B5);
            theme5.setColor7(0XFFEBDBC9);theme5.setColor8(0XFFB4CCF0);theme5.setColor9(0XFFACA3A0);theme5.setColor10(0XFFE198B4);
            theme5.setColor11(0XFFD69090);theme5.setColor12(0XFFF5B1AA);theme5.setColor13(0XFFC0A2C7);theme5.setColor14(0XFFF1BF99);
            theme5.save();

            Theme theme6 = new Theme();
            theme6.setName("candy");theme6.setColor0(0XFF93CBDC);theme6.setColor1(0XFF93A7DC);theme6.setColor2(0XFFA493DC);
            theme6.setColor3(0XFFC993DC);theme6.setColor4(0XFFDC93CB);theme6.setColor5(0XFFDC93A7);theme6.setColor6(0XFFDCA493);
            theme6.setColor7(0XFFDCC993);theme6.setColor8(0XFFCBDC93);theme6.setColor9(0XFFA7DC93);theme6.setColor10(0XFF93DCC9);
            theme6.setColor11(0XFF65B5CD);theme6.setColor12(0XFF3C9CB9);theme6.setColor13(0XFFCD7D65);theme6.setColor14(0XFFB9593C);
            theme6.save();

            Theme theme7 = new Theme();
            theme7.setName("sexless");theme7.setColor0(0XFFC6BDCA);theme7.setColor1(0XFFC9B7A4);theme7.setColor2(0XFF99A38D);
            theme7.setColor3(0XFFA6C3DC);theme7.setColor4(0XFFB6C2B3);theme7.setColor5(0XFFA4C5A5);theme7.setColor6(0XFFBEC4E6);
            theme7.setColor7(0XFFE6D2E6);theme7.setColor8(0XFFE0CCB1);theme7.setColor9(0XFFABD9D3);theme7.setColor10(0XFFD7CB9C);
            theme7.setColor11(0XFFB0D3CB);theme7.setColor12(0XFF84B9CB);theme7.setColor13(0XFFE3C4B7);theme7.setColor14(0XFFAFADCF);
            theme7.save();

            Homework homework0 = new Homework();
            homework0.setFinished(false);
            homework0.setWord("5.第4条提到的惊喜并不是切换主题，仔细看看课程颜色是不是有了变化呢\n6.添加作业可以选择现有课程也可以自拟标题，如果选择现有课程的话颜色会与其保持一致的\n7.谢谢您的使用");
            homework0.setDeadLine("");
            homework0.setIsPhoto(false);
            homework0.setCourse("软件小贴士（下）");

            final Calendar calendar = Calendar.getInstance();
            final long x = (long)(calendar.get(Calendar.YEAR)) * 10000000000L + (long)((calendar.get(Calendar.MONTH) + 1)) * 100000000L
                    + (long)(calendar.get(Calendar.DAY_OF_MONTH)) * 1000000L + (long)(calendar.get(Calendar.HOUR_OF_DAY)) * 10000L
                    + (long)(calendar.get(Calendar.MINUTE)) * 100L + (long)(calendar.get(Calendar.SECOND));
            homework0.setTime(x);

            homework0.save();



            Homework homework = new Homework();
            homework.setFinished(false);
            homework.setWord("1.本APP带有课程表和作业的桌面小插件哦\n2.如果某个科目有未完成的作业，它会在课程表界面和插件上显示出一个萌萌的小折角\n3.点击\"第X周\"或者\"全部作业\"会显示切换选项\n4.在课程表界面下拉有惊喜哦");
            homework.setDeadLine("");
            homework.setIsPhoto(false);
            homework.setCourse("软件小贴士（上）");
            homework.setTime(x+1);

            homework.save();

        }
    }



}
