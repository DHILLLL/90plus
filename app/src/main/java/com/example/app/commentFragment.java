package com.example.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by 635901193 on 2017/7/22.
 */

public class commentFragment extends Fragment {

    private MyComment[] myComment = {
            new MyComment(R.drawable.jay,"Dongheyou","刚刚","雷军老师的课我有幸听过一次，感觉好极了，内容生动有干活，英语讲得也特别好。",8,2),
            new MyComment(R.drawable.jay,"Dongheyou","2016.1.2 21:42","我是来骗赞的",0,31)};

    private List<MyComment> myCommentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommentAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_comment,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view3);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        initComments();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommentAdapter(myCommentList);
        recyclerView.setAdapter(adapter);
    }

    private void initComments(){
        myCommentList.clear();
        myCommentList.add(myComment[0]);
        myCommentList.add(myComment[1]);
        myCommentList.add(myComment[0]);
        myCommentList.add(myComment[1]);
        myCommentList.add(myComment[0]);
        myCommentList.add(myComment[1]);
    }
}
