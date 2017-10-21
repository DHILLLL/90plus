package com.whuLoveStudyGroup.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 635901193 on 2017/7/22.
 */

public class commentFragment extends Fragment {

    private MyComment[] myComment = {
            new MyComment(R.drawable.jay,"Dongheyou","刚刚","雷军老师的课我有幸听过一次，感觉好极了，内容生动有干活，英语讲得也特别好。",8,2),
            new MyComment(R.drawable.jay,"Dongheyou","2016.1.2 21:42","这些评论都是假的，别点了",0,31)};

    private List<MyComment> myCommentList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private String currentCourse;
    private TextView ban;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        currentCourse  = intent.getStringExtra("course");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_comment,container,false);
        ban = (TextView) view.findViewById(R.id.comment_ban);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view3);

        List<Course> temp = DataSupport.where("name = ?",currentCourse).find(Course.class);
        if (temp.get(0).getLessoneID().equals("0")){
            ban.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            ban.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            initComments();
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new CommentAdapter(myCommentList);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
