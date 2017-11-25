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

public class MovieActivity extends MyActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.movie_toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        /*
        recyclerView = (RecyclerView) findViewById(R.id.score_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);*/

    }

    @Override
    protected void onResume() {
        super.onResume();

    }



    //设置左上右上按钮事件
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
