package com.whuLoveStudyGroup.app;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 635901193 on 2017/7/20.
 */

//见书

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context context;
    private List<MyComment> myCommentList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView username,time,words,up,down;
        CircleImageView image;

        public ViewHolder(View view){
            super(view);
            mView = view;
            username = (TextView) view.findViewById(R.id.comment_username);
            words = (TextView) view.findViewById(R.id.comment_words);
            up = (TextView) view.findViewById(R.id.comment_up);
            time = (TextView) view.findViewById(R.id.comment_time);
            down = (TextView) view.findViewById(R.id.comment_down);
            image = (CircleImageView) view.findViewById(R.id.comment_image);
        }
    }

    public CommentAdapter(List<MyComment> myCommentList){
        this.myCommentList = myCommentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyComment myComment = myCommentList.get(position);
        Glide.with(context).load(myComment.getImage()).into(holder.image);
        holder.username.setText(myComment.getUsername());
        holder.time.setText(myComment.getTime());
        holder.up.setText("支持(" + myComment.getUp() + ")");
        holder.down.setText("反对(" + myComment.getDown() + ")");
        holder.words.setText(myComment.getWords());

        holder.up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "+1", Toast.LENGTH_SHORT).show();
            }
        });
        //holder.up.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        holder.down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "-1", Toast.LENGTH_SHORT).show();
            }
        });
        //holder.down.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public int getItemCount() {
        return myCommentList.size();
    }
}
