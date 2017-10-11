package com.example.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by 635901193 on 2017/7/20.
 */

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ViewHolder> {
    private Context context;
    private List<Score> scores;

    private static final String TAG = "dong";

    static class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView score,name,teacher,type,credit,gpa;

        public ViewHolder(View view){
            super(view);
            mView = view;
            score= (TextView) view.findViewById(R.id.score_score);
            name= (TextView) view.findViewById(R.id.score_name);
            teacher= (TextView) view.findViewById(R.id.score_teacher);
            type= (TextView) view.findViewById(R.id.score_type);
            credit= (TextView) view.findViewById(R.id.score_credit);
            gpa= (TextView) view.findViewById(R.id.score_gpa);

        }
    }

    public ScoreAdapter(List<Score> scores){
        this.scores = scores;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.score_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Score score = scores.get(position);
        if (score.getScore() == -1.0){
            holder.score.setText("无");
            holder.gpa.setText("无");
        }else{
            holder.score.setText(String.valueOf(score.getScore().intValue()));
            holder.gpa.setText(String.valueOf(score.getGpa()));
        }
        holder.name.setText(score.getName());
        holder.teacher.setText(score.getTeacher());
        holder.type.setText(score.getType());
        holder.credit.setText(String.valueOf(score.getCredit()));

    }

    @Override
    public int getItemCount() {
        return scores.size();
    }
}

