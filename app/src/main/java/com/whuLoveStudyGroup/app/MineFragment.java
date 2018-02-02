package com.whuLoveStudyGroup.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 635901193 on 2017/7/22.
 */

public class MineFragment extends Fragment {

    private List<Lost> lostList;
    RecyclerView recyclerView;
    private LostAdapter adapter;
    private static final String TAG = "dongheyou";

    public class MyBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            onResume();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lnf_mine,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.lnf_mine_list);
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        lostList = new ArrayList<Lost>();
        for (int i = 0;i<20;i++)
            lostList.add(new Lost());
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new LostAdapter(lostList);
        recyclerView.setAdapter(adapter);
    }

}
