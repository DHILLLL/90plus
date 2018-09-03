package com.whuLoveStudyGroup.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whuLoveStudyGroup.app.connWithServerUtil.MyComment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 635901193 on 2017/7/20.
 */

//见书

public class Comment2Adapter extends RecyclerView.Adapter<Comment2Adapter.ViewHolder> {
    private Context context;
    private List<MyComment> myCommentList;
    private static final String TAG = "dongheyou";


    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的

    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;

    class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView username,time,words,up,down;
        CircleImageView image;

        public ViewHolder(View view){
            super(view);

            if (itemView == mFooterView){
                return;
            }
            mView = view;
            username = (TextView) view.findViewById(R.id.comment3_username);
            words = (TextView) view.findViewById(R.id.comment3_words);
            up = (TextView) view.findViewById(R.id.comment3_up);
            time = (TextView) view.findViewById(R.id.comment3_time);
            down = (TextView) view.findViewById(R.id.comment3_down);
            image = (CircleImageView) view.findViewById(R.id.comment3_image);
        }
    }



    public Comment2Adapter(List<MyComment> myCommentList){
        this.myCommentList = myCommentList;
    }

    //HeaderView和FooterView的get和set函数

    public View getFooterView() {
        return mFooterView;
    }
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount()-1);
    }

    /** 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    * */
    @Override
    public int getItemViewType(int position) {

        if (position == getItemCount()-1){
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }


        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new ViewHolder(mFooterView);
        }

        View view = LayoutInflater.from(context).inflate(R.layout.comment2_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( final ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof ViewHolder) {
                final MyComment myComment = myCommentList.get(position);

                String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date());
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(myComment.getUnixTime()));
                if(current.substring(0,10).equals(date.substring(0,10))) date = date.substring(11);
                else if(current.substring(0,4).equals(date.substring(0,4))) date = date.substring(5);


                holder.username.setText(myComment.getStarterUsername());
                holder.time.setText(date);
                holder.up.setText("支持(" + myComment.getUpVoteCount() + ")");
                holder.down.setText("反对(" + myComment.getDownVoteCount() + ")");
                holder.words.setText(myComment.getComment());

                if (myComment.isUpVoted()){
                    TextPaint paint = holder.up.getPaint();
                    paint.setFakeBoldText(true);
                    holder.up.setTextSize(16);
                    holder.down.getPaint().setFakeBoldText(false);
                    holder.down.setTextSize(14);
                    holder.up.setEnabled(false);
                    holder.down.setEnabled(false);

                }else if (myComment.isDownVoted()){
                    TextPaint paint = holder.down.getPaint();
                    paint.setFakeBoldText(true);
                    holder.down.setTextSize(16);
                    holder.up.getPaint().setFakeBoldText(false);
                    holder.up.setTextSize(14);
                    holder.up.setEnabled(false);
                    holder.down.setEnabled(false);

                }else {
                    holder.up.setTextSize(14);
                    holder.down.setTextSize(14);
                    holder.up.getPaint().setFakeBoldText(false);
                    holder.down.getPaint().setFakeBoldText(false);
                    holder.up.setEnabled(true);
                    holder.down.setEnabled(true);
                    holder.up.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.up.setText("支持(" + (myComment.getUpVoteCount() + 1) + ")");
                            TextPaint paint = holder.up.getPaint();
                            paint.setFakeBoldText(true);
                            holder.up.setTextSize(16);
                            holder.up.setEnabled(false);
                            holder.down.setEnabled(false);
                        }
                    });

                    holder.down.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.up.setText("反对(" + (myComment.getDownVoteCount() + 1) + ")");
                            TextPaint paint = holder.down.getPaint();
                            holder.down.setTextSize(16);
                            paint.setFakeBoldText(true);
                            holder.up.setEnabled(false);
                            holder.down.setEnabled(false);
                        }
                    });
                }

                Glide.with(context).load(myComment.getStarterUserImageThumbnailUrl()).into(holder.image);

            }
            return;
        }else{
            return;
        }

    }

    @Override
    public int getItemCount() {
        if(mFooterView == null){
            return myCommentList.size();
        }else {
            return myCommentList.size() + 1;
        }
    }
}
