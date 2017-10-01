package com.example.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 635901193 on 2017/7/22.
 */

public class questionsFragment extends Fragment {

    private Question[] questions = {
            new Question(R.drawable.jay,"Dongheyou","刚刚","挖掘机术哪家强？",0),
            new Question(R.drawable.jay,"Dongheyou","2016.1.2 21:42","这个老师给分怎么样。。",3)};
    private List<Question> questionList = new ArrayList<>();
    private RecyclerView recyclerView;
    private QuestionAdapter adapter;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_questions,container,false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view5);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        initComments();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new QuestionAdapter(questionList);
        recyclerView.setAdapter(adapter);
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


