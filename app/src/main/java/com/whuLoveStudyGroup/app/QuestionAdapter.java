package com.whuLoveStudyGroup.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 635901193 on 2017/7/20.
 */

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private Context context;
    private List<Question> questionList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView username,time,words,answer;
        CircleImageView image;

        public ViewHolder(View view){
            super(view);
            mView = view;
            username = (TextView) view.findViewById(R.id.question_username);
            words = (TextView) view.findViewById(R.id.question_words);
            time = (TextView) view.findViewById(R.id.question_time);
            answer = (TextView) view.findViewById(R.id.question_answer);
            image = (CircleImageView) view.findViewById(R.id.question_image);
        }
    }

    public QuestionAdapter(List<Question> QuestionList){
        this.questionList = QuestionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.question_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Question question = questionList.get(position);
//        Glide.with(context).load(question.getImage()).into(holder.image);
//        holder.username.setText(question.getUsername());
//        holder.time.setText(question.getTime());
//        holder.answer.setText(question.getAnswer() + "条回答");
//        holder.words.setText(question.getWords());

    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }
}
