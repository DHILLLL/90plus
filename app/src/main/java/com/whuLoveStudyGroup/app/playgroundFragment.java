package com.whuLoveStudyGroup.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 635901193 on 2017/7/22.
 */

public class playgroundFragment extends Fragment {

    /*
    private Function[] functions = {new Function("烦心事",R.drawable.main_trouble),new Function("查成绩",R.drawable.main_grade)
        ,new Function("查课",R.drawable.main_search),new Function("交通",R.drawable.main_map),
        new Function("赛事",R.drawable.main_match),new Function("讲座",R.drawable.main_lecture),
        new Function("失物招领",R.drawable.main_lnf),new Function("梅操电影",R.drawable.main_movie)};*/

    private Function[] functions = {new Function("成绩查询",R.drawable.main_grade),new Function("敬请期待",R.drawable.main_trouble),
            new Function("敬请期待",R.drawable.main_search),new Function("敬请期待",R.drawable.main_map),
            new Function("敬请期待",R.drawable.main_lnf),new Function("敬请期待",R.drawable.main_movie)};

    private List<Function> functionList = new ArrayList<>();
    private FunctionAdapter adapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playground,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view4);
        return view;
    }

    private void initFunctions(){
        functionList.clear();
        for (int i = 0;i<functions.length;i++){
            functionList.add(functions[i]);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initFunctions();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new FunctionAdapter(functionList);
        recyclerView.setAdapter(adapter);
    }
}
