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
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 635901193 on 2017/7/22.
 */

public class questionsFragment extends Fragment {

    private Question[] questions = {
            new Question(R.drawable.jay,"Dongheyou","刚刚","这些问题都是假的。",0),
            new Question(R.drawable.jay,"Dongheyou","2016.1.2 21:42","仅供参考，点击无效。",3)};
    private List<Question> questionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private QuestionAdapter adapter;
    private String currentCourse;
    private TextView ban;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        currentCourse  = intent.getStringExtra("course");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_questions,container,false);
        ban = (TextView) view.findViewById(R.id.questions_ban);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view5);

        List<Course> temp = DataSupport.where("name = ?",currentCourse).find(Course.class);
        if (temp.get(0).getLessoneID().equals("0")){
            ban.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            ban.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            initComments();
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new QuestionAdapter(questionList);
            recyclerView.setAdapter(adapter);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void initComments(){
        questionList.clear();
        questionList.add(questions[0]);
        questionList.add(questions[1]);
        questionList.add(questions[0]);
        questionList.add(questions[1]);
        questionList.add(questions[0]);
        questionList.add(questions[1]);
    }
    
}


