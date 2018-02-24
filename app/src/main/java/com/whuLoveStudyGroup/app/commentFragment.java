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
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
    private RecyclerView recyclerView,recyclerView1;
    private CommentAdapter adapter,adapter1;
    private String currentCourse;
    private TextView ban,more;
    ScrollView scrollView;

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
        recyclerView1 = (RecyclerView) view.findViewById(R.id.recycler_view33);
        scrollView = (ScrollView) view.findViewById(R.id.comment_all);
        more = (TextView) view.findViewById(R.id.comment_more);

        List<Course> temp = DataSupport.where("name = ?",currentCourse).find(Course.class);
        if (temp.get(0).getLessoneID().equals("0")){
            ban.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else {
            ban.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            initComments();
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView1.setLayoutManager(layoutManager1);
            adapter = new CommentAdapter(myCommentList);
            adapter1 = new CommentAdapter(myCommentList);
            recyclerView.setAdapter(adapter);
            recyclerView1.setAdapter(adapter1);
        }

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCommentList.add(myComment[0]);
                myCommentList.add(myComment[1]);
                myCommentList.add(myComment[0]);
                myCommentList.add(myComment[1]);
                myCommentList.add(myComment[0]);
                myCommentList.add(myComment[1]);

                adapter.notifyDataSetChanged();
                //recyclerView.scrollToPosition(6);
            }
        });

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
