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
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

/**
 * Created by 635901193 on 2017/7/22.
 */

public class LnfActivity extends MyActivity {
    private TextView found;
    private TextView lost;
    private TextView mine;

    private ViewPager viewPager;
    private ImageView cursor;
    private int offset = 0;
    private int position_one;
    private int position_two;
    private int bmpw;
    private int currIndex = 0;
    private ArrayList<Fragment> fragmentArrayList;
    private FragmentManager fragmentManager;
    public Context context;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lnf);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_lnf);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //悬浮按钮在四个fragment时分别的事件
        fab = (FloatingActionButton) findViewById(R.id.lnf_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currIndex){
                    case 0:
                        Toast.makeText(LnfActivity.this, "found", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(LnfActivity.this, "lost", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(LnfActivity.this, "mine", Toast.LENGTH_SHORT).show();
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

    private void initTextView(){
        found = (TextView) findViewById(R.id.lnf_found);
        lost = (TextView) findViewById(R.id.lnf_lost);
        mine = (TextView) findViewById(R.id.lnf_mine);

        found.setOnClickListener(new MyOnClickListener(0));
        lost.setOnClickListener(new MyOnClickListener(1));
        mine.setOnClickListener(new MyOnClickListener(2));

    }

    //页面切换相关
    private void initViewPager(){
        viewPager = (ViewPager) findViewById(R.id.lnf_view_pager);
        viewPager.setAdapter(new MyFragmentPagerAdapter(fragmentManager,fragmentArrayList));
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    //页面切换动画相关
    private void initImageView(){
        cursor = (ImageView) findViewById(R.id.lnf_cursor);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        bmpw = (screenW/3);
        setBmpW(cursor,bmpw);
        offset = 0;
        position_one = (int) (screenW/3.0);
        position_two = (int) (position_one*2);
    }

    //页面切换相关
    private void initFragment(){
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new FoundFragment());
        fragmentArrayList.add(new LostFragment());
        fragmentArrayList.add(new MineFragment());

        fragmentManager = getSupportFragmentManager();
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
                        default:
                    }
                    break;
                case 1:
                    switch (currIndex){
                        case 0:animation = new TranslateAnimation(offset,position_one,0,0);break;
                        case 2:animation = new TranslateAnimation(position_two,position_one,0,0);break;
                        default:
                    }
                    break;
                case 2:
                    switch (currIndex){
                        case 0:animation = new TranslateAnimation(offset,position_two,0,0);break;
                        case 1:animation = new TranslateAnimation(offset,position_two,0,0);break;
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
