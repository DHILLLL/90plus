package com.whuLoveStudyGroup.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.whuLoveStudyGroup.app.connWithServerUtil.ConnWithServerComment;
import com.whuLoveStudyGroup.app.connWithServerUtil.MyComment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 635901193 on 2017/7/22.
 */

public class commentFragment extends Fragment {

    private List<MyComment> listHot = new ArrayList<>();
    private List<MyComment> listNew = new ArrayList<>();
    private RecyclerView rvHot,rvNew;
    private CommentAdapter adapterHot,adapterNew;
    private String currentCourse,ID;
    private TextView ban;
    private ConnWithServerComment connWithServerComment;
    SwipeRefreshLayout swipeRefreshLayout;
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
        listHot = (List<MyComment>) connWithServerComment.getResponseData();
        error = connWithServerComment.queryNewestCommentsByCourseID(ID,0);
        listNew = (List<MyComment>) connWithServerComment.getResponseData();
        offset = connWithServerComment.getResponseOffset();
        listHot.addAll(listNew);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_comment,container,false);
        rvHot = (RecyclerView) view.findViewById(R.id.rv_hot);
        ban = (TextView) view.findViewById(R.id.comment_ban);
        //rvNew = (RecyclerView) view.findViewById(R.id.rv_new);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.sr_hot);
        swipeRefreshLayout.setEnabled(false);

        if (ID.equals("0")){
            ban.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.GONE);
            return view;
        }

        swipeRefreshLayout.setVisibility(View.VISIBLE);
        ban.setVisibility(View.GONE);
        LinearLayoutManager lmHot = new LinearLayoutManager(getContext());
        //LinearLayoutManager lmNew = new LinearLayoutManager(getContext());
        rvHot.setLayoutManager(lmHot);
        //.setLayoutManager(lmNew);
        adapterHot = new CommentAdapter(listHot);
        //adapterNew = new CommentAdapter(listNew);
        rvHot.setAdapter(adapterHot);
        //rvNew.setAdapter(adapterNew);

        View footer = LayoutInflater.from(getContext()).inflate(R.layout.footer, container, false);

        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int error = connWithServerComment.queryNewestCommentsByCourseID(ID,offset);
                offset = connWithServerComment.getResponseOffset();
                listHot.addAll((List<MyComment>) connWithServerComment.getResponseData());
                adapterHot.notifyDataSetChanged();
            }
        });

        adapterHot.setFooterView(footer);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
