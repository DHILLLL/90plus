package com.whuLoveStudyGroup.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    private MyComment[] hottest,newest;

    private List<MyComment> listHot = new ArrayList<>();
    private List<MyComment> listNew = new ArrayList<>();
    private RecyclerView rvHot,rvNew;
    private CommentAdapter adapterHot,adapterNew;
    private String currentCourse,ID;
    private TextView ban,more;
    private ConnWithServerComment connWithServerComment;
    int offset;
    private static final String TAG = "dongheyou";
    ScrollView scrollView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        currentCourse  = intent.getStringExtra("course");
        ID  = intent.getStringExtra("ID");
        connWithServerComment = new ConnWithServerComment();
        int error = connWithServerComment.queryHottestCommentsByCourseID(ID);
        hottest = (MyComment[]) connWithServerComment.getResponseData();
        Log.d(TAG, "hottest: " + hottest.length);
        error = connWithServerComment.queryNewestCommentsByCourseID(ID,0);
        newest = (MyComment[]) connWithServerComment.getResponseData();
        offset = 20;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_comment,container,false);
        ban = (TextView) view.findViewById(R.id.comment_ban);
        rvHot = (RecyclerView) view.findViewById(R.id.rv_hot);
        rvNew = (RecyclerView) view.findViewById(R.id.rv_new);
        scrollView = (ScrollView) view.findViewById(R.id.comment_all);
        more = (TextView) view.findViewById(R.id.comment_more);

        if (ID.equals("0")){
            ban.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else {
            ban.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            initComments();
            LinearLayoutManager lmHot = new LinearLayoutManager(getContext());
            LinearLayoutManager lmNew = new LinearLayoutManager(getContext());
            rvHot.setLayoutManager(lmHot);
            rvNew.setLayoutManager(lmNew);
            adapterHot = new CommentAdapter(listHot);
            adapterNew = new CommentAdapter(listNew);
            rvHot.setAdapter(adapterHot);
            rvNew.setAdapter(adapterNew);
        }

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int error = connWithServerComment.queryNewestCommentsByCourseID(ID,offset);
                offset += 20;
                newest = (MyComment[]) connWithServerComment.getResponseData();
                for(MyComment myComment : newest){
                    listNew.add(myComment);
                }
                adapterNew.notifyDataSetChanged();
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
        for(MyComment myComment : hottest){
            listHot.add(myComment);
        }

        for(MyComment myComment : newest){
            listNew.add(myComment);
        }
    }
}
