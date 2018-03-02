package com.whuLoveStudyGroup.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 635901193 on 2017/7/20.
 */

//见书

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context context;
    private List<MyComment> myCommentList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView username,time,words,up,down,comment;
        CircleImageView image;

        public ViewHolder(View view){
            super(view);
            mView = view;
            username = (TextView) view.findViewById(R.id.comment_username);
            words = (TextView) view.findViewById(R.id.comment_words);
            up = (TextView) view.findViewById(R.id.comment_up);
            time = (TextView) view.findViewById(R.id.comment_time);
            down = (TextView) view.findViewById(R.id.comment_down);
            comment = (TextView) view.findViewById(R.id.comment_comment);
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MyComment myComment = myCommentList.get(position);

        holder.username.setText(myComment.getStarterUserID());
        holder.time.setText(String.valueOf(myComment.getUnixTime()));
        holder.up.setText("支持(" + myComment.getUpVoteCount() + ")");
        holder.down.setText("反对(" + myComment.getDownVoteCount() + ")");
        holder.words.setText(myComment.getComment());
        holder.comment.setText(myComment.getTotalCommentsNum() + "条评论");

        new Thread(new Runnable() {
            @Override
            public void run() {

            String url = myComment.getStarterUserImageThumbnailUrl();
            if(url.length() > 1){
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        byte[] picByte = response.body().bytes();
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(picByte,0,picByte.length);
//                        context.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                holder.image.setImageBitmap(bitmap);
//                            }
//                        });
                        holder.image.setImageBitmap(bitmap);
                    }
                });
            }



            }
        }).run();

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
