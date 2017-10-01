package com.example.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 635901193 on 2017/7/22.
 */

public class homeworkFragment extends Fragment {

    private List<Homework> homeworkList;
    RecyclerView recyclerView;
    private HomeworkAdapter adapter;
    private static final String TAG = "dongheyou";

    private IntentFilter intentFilter;
    private MyBroadcastReceiver myBroadcastReceiver;
    private LocalBroadcastManager localBroadcastManager;


    public class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(myBroadcastReceiver);
    }

    //设置广播接收器
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.app.UPDATE_HOMEWORK");
        localBroadcastManager = localBroadcastManager.getInstance(getContext());
        myBroadcastReceiver = new MyBroadcastReceiver();
        localBroadcastManager.registerReceiver(myBroadcastReceiver,intentFilter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homework,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        //根据当前筛选方式选择显示的作业
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data",Context.MODE_PRIVATE);
        int display = sharedPreferences.getInt("display",0);
        switch (display){
            case 0:
                homeworkList = DataSupport.order("time desc").find(Homework.class);
                break;
            case 1:
                homeworkList = DataSupport.where("finished = ?","0").order("time desc").find(Homework.class);
                break;
            case 2:
                homeworkList = DataSupport.where("finished = ?","1").order("time desc").find(Homework.class);
                break;
        }
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HomeworkAdapter(homeworkList);
        recyclerView.setAdapter(adapter);
    }

}
