package com.whuLoveStudyGroup.app;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 635901193 on 2017/7/20.
 */

public class LostAdapter extends RecyclerView.Adapter<LostAdapter.ViewHolder> {
    private Context context;
    private List<Lost> lostList;


    static class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        CardView cardView;
        TextView course,work,deadLine;
        ImageView pic;
        CheckBox finished;

        public ViewHolder(View view){
            super(view);
            mView = view;
            cardView = (CardView) view;
            course = (TextView) view.findViewById(R.id.homework_course);
            work = (TextView) view.findViewById(R.id.homework_work);
            pic = (ImageView) view.findViewById(R.id.homework_pic);
            deadLine = (TextView) view.findViewById(R.id.homework_deadLine);
            finished = (CheckBox) view.findViewById(R.id.homework_finished);
        }
    }

    public LostAdapter(List<Lost> lostList){
        this.lostList = lostList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }

        View view = LayoutInflater.from(context).inflate(R.layout.lnf_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return lostList.size();
    }

}
