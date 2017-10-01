package com.example.app;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 635901193 on 2017/7/22.
 */

public class informationFragment extends Fragment {
    private static final String TAG = "dongheyou";
    private String currentCourse;
    private TextView name,time,week,teacher,type,credit,place;
    private EditText note;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        currentCourse  = intent.getStringExtra("course");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_infomation,container,false);
        name = (TextView) view.findViewById(R.id.coure_name);
        time = (TextView) view.findViewById(R.id.coure_time);
        teacher = (TextView) view.findViewById(R.id.coure_teacher);
        week = (TextView) view.findViewById(R.id.coure_week);
        type = (TextView) view.findViewById(R.id.coure_type);
        credit = (TextView) view.findViewById(R.id.coure_credit);
        place = (TextView) view.findViewById(R.id.coure_place);
        note = (EditText) view.findViewById(R.id.coure_note);
        return view;
    }

    String oe(Course course){
        if (course.getEveryWeek())
            return "";
        else return " 每2周";
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Course> courses = DataSupport.where("name = ?",currentCourse).order("weekday ASC").find(Course.class);
        Course course = courses.get(0);
        name.setText(course.getName());

        //显示时间
        switch (course.getTimesPerWeek()){
            case 1:
                time.setText("周"+ tC(course.getWeekday()) + course.getHourFrom() + "-" + course.getHourTo() + "节" + oe(course));
                break;
            case 2:
                time.setText("周"+ tC(course.getWeekday()) + course.getHourFrom() + "-" + course.getHourTo() + "节" + oe(course)
                        + "；周"+ tC(courses.get(1).getWeekday()) + courses.get(1).getHourFrom() + "-" + courses.get(1).getHourTo() + "节" + oe(courses.get(1)));
                break;
            case 3:
                time.setText("周"+ tC(course.getWeekday()) + course.getHourFrom() + "-" + course.getHourTo() + "节" + oe(course)
                        + "；周"+ tC(courses.get(1).getWeekday()) + courses.get(1).getHourFrom() + "-" + courses.get(1).getHourTo() + "节" + oe(courses.get(1))
                        + "；周"+ tC(courses.get(2).getWeekday()) + courses.get(2).getHourFrom() + "-" + courses.get(2).getHourTo() + "节" + oe(courses.get(2)));
                break;
            default:
        }

        place.setText(course.getPlace());
        teacher.setText(course.getTeacher());
        week.setText(course.getWeekFrom() + "-" + course.getWeekTo() + "周");
        type.setText(course.getType());
        credit.setText(course.getCredit());
        if(!(course.getNote() == null)) {
            note.setText(course.getNote());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //离开时保存笔记
        if(note.getText().toString() != null){
            ContentValues values = new ContentValues();
            values.put("note",note.getText().toString());
            DataSupport.updateAll(Course.class,values,"name = ?",currentCourse);
        }

    }

    String tC(int n){
        switch (n){
            case 1:return "一 ";
            case 2:return "二 ";
            case 3:return "三 ";
            case 4:return "四 ";
            case 5:return "五 ";
            case 6:return "六 ";
            case 7:return "日 ";
            default:return "";
        }
    }
}
