package com.whuLoveStudyGroup.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comment2Activity extends MyActivity {
    TextView username,time,words,up,down,footer;
    CircleImageView image;
    private List<MyComment> listHot = new ArrayList<>();
    private List<MyComment> listNew = new ArrayList<>();
    private RecyclerView rvHot;
    private Comment2Adapter adapterHot;
    private ConnWithServerComment connWithServerComment;
    SwipeRefreshLayout swipeRefreshLayout;
    int offset;
    private static final String TAG = "dongheyou";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.course_comment2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.comment2_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }

        username = (TextView) findViewById(R.id.comment2_username);
        words = (TextView) findViewById(R.id.comment2_words);
        up = (TextView) findViewById(R.id.comment2_up);
        time = (TextView) findViewById(R.id.comment2_time);
        down = (TextView) findViewById(R.id.comment2_down);
        image = (CircleImageView) findViewById(R.id.comment2_image);
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.comment2_layout);
        View footer = LayoutInflater.from(this).inflate(R.layout.footer,frameLayout,false);

        Intent intent = getIntent();
        final int commentId  = intent.getIntExtra("ID",0);
        connWithServerComment = new ConnWithServerComment();
        int error = connWithServerComment.queryCommentsByCommentID(commentId);
        final MyComment currentComment = (MyComment) connWithServerComment.getResponseData();

        String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(currentComment.getUnixTime()));
        if(current.substring(0,10).equals(date.substring(0,10))) date = date.substring(11);
        else if(current.substring(0,4).equals(date.substring(0,4))) date = date.substring(5);


        username.setText(currentComment.getStarterUsername());
        time.setText(date);
        up.setText("支持(" + currentComment.getUpVoteCount() + ")");
        down.setText("反对(" + currentComment.getDownVoteCount() + ")");
        words.setText(currentComment.getComment());

        if (currentComment.isUpVoted()){
            TextPaint paint = up.getPaint();
            paint.setFakeBoldText(true);
            up.setTextSize(16);
            up.setEnabled(false);
            down.setEnabled(false);

        }else if (currentComment.isDownVoted()){
            TextPaint paint = down.getPaint();
            paint.setFakeBoldText(true);
            down.setTextSize(16);
            up.setEnabled(false);
            down.setEnabled(false);

        }else{
            up.setTextSize(14);
            down.setTextSize(14);
            up.getPaint().setFakeBoldText(false);
            down.getPaint().setFakeBoldText(false);
            up.setEnabled(true);
            down.setEnabled(true);
            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    up.setText("支持(" + (currentComment.getUpVoteCount()+1) + ")");
                    TextPaint paint = up.getPaint();
                    paint.setFakeBoldText(true);
                    up.setTextSize(16);
                    up.setEnabled(false);
                    down.setEnabled(false);
                }
            });

            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    up.setText("反对(" + (currentComment.getDownVoteCount()+1) + ")");
                    TextPaint paint = down.getPaint();
                    down.setTextSize(16);
                    paint.setFakeBoldText(true);
                    up.setEnabled(false);
                    down.setEnabled(false);
                }
            });
        }

        Glide.with(Comment2Activity.this).load(currentComment.getStarterUserImageThumbnailUrl()).into(image);

        error = connWithServerComment.queryHottestCommentsByCommentID(commentId);
        listHot = (List<MyComment>) connWithServerComment.getResponseData();
        error = connWithServerComment.queryNewestCommentsByCommentID(commentId,0);
        listNew = (List<MyComment>) connWithServerComment.getResponseData();
        offset = connWithServerComment.getResponseOffset();
        listHot.addAll(listNew);

        rvHot = (RecyclerView) findViewById(R.id.rv_comment2);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.sr_comment2);
        swipeRefreshLayout.setEnabled(false);

        LinearLayoutManager lmHot = new LinearLayoutManager(this);
        rvHot.setLayoutManager(lmHot);
        adapterHot = new Comment2Adapter(listHot);
        rvHot.setAdapter(adapterHot);

        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int error = connWithServerComment.queryNewestCommentsByCommentID(commentId,offset);
                offset = connWithServerComment.getResponseOffset();
                listHot.addAll((List<MyComment>) connWithServerComment.getResponseData());
                adapterHot.notifyDataSetChanged();
            }
        });

        adapterHot.setFooterView(footer);
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
