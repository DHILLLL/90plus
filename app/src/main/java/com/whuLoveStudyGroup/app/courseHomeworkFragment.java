package com.whuLoveStudyGroup.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class courseHomeworkFragment extends Fragment {

    private List<Homework> homeworkList;
    private RecyclerView recyclerView;
    private HomeworkAdapter adapter;
    String currentCourse;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取当前课程名
        Intent intent = getActivity().getIntent();
        currentCourse  = intent.getStringExtra("course");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_homework,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view2);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        homeworkList = DataSupport.where("course = ?",currentCourse).order("time desc").find(Homework.class);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HomeworkAdapter(homeworkList);
        recyclerView.setAdapter(adapter);

    }

}
