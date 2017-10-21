package com.whuLoveStudyGroup.app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * Created by 635901193 on 2017/7/22.
 */

public class courseActivity extends MyActivity {
    private TextView infomation;
    private TextView homework;
    private TextView comment;
    private TextView questions;
    private ViewPager viewPager;
    private ImageView cursor;
    private int offset = 0;
    private int position_one;
    private int position_two;
    private int position_three;
    private int bmpw;
    private int currIndex = 0;
    private ArrayList<Fragment> fragmentArrayList;
    private FragmentManager fragmentManager;
    public Context context;
    private FloatingActionButton fab;
    private static final String TAG = "course";
    private String currentCourse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.course);

        //接收当前课程名
        Intent intent = getIntent();
        currentCourse  = intent.getStringExtra("course");

        TextView title = (TextView) findViewById(R.id.course_title);
        title.setText(currentCourse);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_course);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //悬浮按钮在四个fragment时分别的事件
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currIndex){
                    case 0:
                        Intent intent0 = new Intent(courseActivity.this,EditCourseActivity.class);
                        intent0.putExtra("name",currentCourse);
                        startActivity(intent0);
                        break;
                    case 1:
                        Intent intent1 = new Intent(courseActivity.this,AddHomeworkActivity.class);
                        intent1.putExtra("course",currentCourse);
                        startActivity(intent1);
                        break;
                    case 2:
                        final EditText editText = new EditText(courseActivity.this);
                        new AlertDialog.Builder(courseActivity.this).setTitle("编写评论").setView(editText)
                                .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
                        break;
                    case 3:
                        break;
                    default:
                }
            }
        });

        initTextView();
        initImageView();
        initFragment();
        initViewPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.course_menu,menu);
        return true;
    }

    //删除按钮事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.course_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(courseActivity.this);
                builder.setMessage("您确定要删除该课程及其信息吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DataSupport.deleteAll(Course.class,"name = ?",currentCourse);
                        Intent intent1 = new Intent("com.whuLoveStudyGroup.app.UPDATE_WIDGET");
                        sendBroadcast(intent1);
                        finish();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            default:
        }
        return true;
    }

    private void initTextView(){
        homework = (TextView) findViewById(R.id.course_homework);
        infomation = (TextView) findViewById(R.id.course_infomation);
        comment = (TextView) findViewById(R.id.course_comment);
        questions = (TextView) findViewById(R.id.course_questions);

        infomation.setOnClickListener(new MyOnClickListener(0));
        homework.setOnClickListener(new MyOnClickListener(1));
        comment.setOnClickListener(new MyOnClickListener(2));
        questions.setOnClickListener(new MyOnClickListener(3));
    }

    //页面切换相关
    private void initViewPager(){
        viewPager = (ViewPager) findViewById(R.id.course_view_pager);
        viewPager.setAdapter(new MyFragmentPagerAdapter(fragmentManager,fragmentArrayList));
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    //页面切换动画相关
    private void initImageView(){
        cursor = (ImageView) findViewById(R.id.cursor);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        bmpw = (screenW/4);
        setBmpW(cursor,bmpw);
        offset = 0;
        position_one = (int) (screenW/4.0);
        position_two = position_one * 2;
        position_three = position_one * 3;
    }

    //页面切换相关
    private void initFragment(){
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new informationFragment());
        fragmentArrayList.add(new courseHomeworkFragment());
        fragmentArrayList.add(new commentFragment());
        fragmentArrayList.add(new questionsFragment());
        fragmentManager = getSupportFragmentManager();
    }

    //页面切换相关
    class MyOnClickListener implements View.OnClickListener{
        private int index = 0;
        public MyOnClickListener(int i){
            index = i;
        }

        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(index);
        }
    }

    //页面切换相关
    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{
        @Override
        public void onPageSelected(int position) {
            Animation animation = null;
            switch (position){
                case 0:
                    switch (currIndex){
                        case 1:animation = new TranslateAnimation(position_one,offset,0,0);break;
                        case 2:animation = new TranslateAnimation(position_two,offset,0,0);break;
                        case 3:animation = new TranslateAnimation(position_three,offset,0,0);break;
                        default:
                    }
                    break;
                case 1:
                    switch (currIndex){
                        case 0:animation = new TranslateAnimation(offset,position_one,0,0);break;
                        case 2:animation = new TranslateAnimation(position_two,position_one,0,0);break;
                        case 3:animation = new TranslateAnimation(position_three,position_one,0,0);break;
                        default:
                    }
                    break;
                case 2:
                    switch (currIndex){
                        case 0:animation = new TranslateAnimation(offset,position_two,0,0);break;
                        case 1:animation = new TranslateAnimation(position_one,position_two,0,0);break;
                        case 3:animation = new TranslateAnimation(position_three,position_two,0,0);break;
                        default:
                    }
                    break;
                case 3:
                    switch (currIndex){
                        case 0:animation = new TranslateAnimation(offset,position_three,0,0);break;
                        case 1:animation = new TranslateAnimation(position_one,position_three,0,0);break;
                        case 2:animation = new TranslateAnimation(position_two,position_three,0,0);break;
                        default:
                    }
                    break;
                default:
            }
            currIndex = position;
            animation.setFillAfter(true);
            animation.setDuration(100);
            cursor.startAnimation(animation);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    //页面切换动画相关
    private void setBmpW(ImageView imageView,int mWidth){
        ViewGroup.LayoutParams para;
        para = imageView.getLayoutParams();
        para.width = mWidth;
        imageView.setLayoutParams(para);
    }

}
