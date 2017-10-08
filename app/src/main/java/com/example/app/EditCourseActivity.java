package com.example.app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 635901193 on 2017/7/26.
 */

//除了一开始需要显示原本的信息和图片外，其余和AddCourseActivity类似

public class EditCourseActivity extends MyActivity {

    private String currentCourse,note;
    private EditText place,teacher,type,credit,weekFrom,weekTo,timePerWeek,weekday0,
            weekday1,weekday2, hourFrom0,hourFrom1,hourFrom2,hourTo0,hourTo1,hourTo2;
    private TextView name;
    private RadioButton every0,not0,every1,not1,every2,not2;
    private int color;
    private boolean homework;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_course);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_course_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        name = (TextView) findViewById(R.id.edit_course_name);
        place = (EditText) findViewById(R.id.edit_course_place);
        teacher = (EditText) findViewById(R.id.edit_course_teacher);
        type = (EditText) findViewById(R.id.edit_course_type);
        credit = (EditText) findViewById(R.id.edit_course_credit);
        weekFrom = (EditText) findViewById(R.id.edit_course_week_from);
        weekTo = (EditText) findViewById(R.id.edit_course_week_to);
        timePerWeek = (EditText) findViewById(R.id.edit_course_times_per_week);
        weekday0 = (EditText) findViewById(R.id.edit_course_weekday0);
        weekday1 = (EditText) findViewById(R.id.edit_course_weekday1);
        weekday2 = (EditText) findViewById(R.id.edit_course_weekday2);
        hourFrom0 = (EditText) findViewById(R.id.edit_course_hour_from0);
        hourFrom1 = (EditText) findViewById(R.id.edit_course_hour_from1);
        hourFrom2 = (EditText) findViewById(R.id.edit_course_hour_from2);
        hourTo0 = (EditText) findViewById(R.id.edit_course_hour_to0);
        hourTo1 = (EditText) findViewById(R.id.edit_course_hour_to1);
        hourTo2 = (EditText) findViewById(R.id.edit_course_hour_to2);

        every0 = (RadioButton) findViewById(R.id.edit_course_every0);
        not0 = (RadioButton) findViewById(R.id.edit_course_not0);
        every1 = (RadioButton) findViewById(R.id.edit_course_every1);
        not1 = (RadioButton) findViewById(R.id.edit_course_not1);
        every2 = (RadioButton) findViewById(R.id.edit_course_every2);
        not2 = (RadioButton) findViewById(R.id.edit_course_not2);

        Intent intent = getIntent();
        currentCourse = intent.getStringExtra("name");
        List<Course> courses = DataSupport.where("name = ?",currentCourse).order("weekday ASC").find(Course.class);
        Course course = courses.get(0);
        color = course.getColor();
        homework = course.getHomework();
        note = course.getNote();


        name.setText(course.getName());
        place.setText(course.getPlace());
        teacher.setText(course.getTeacher());
        type.setText(course.getType());
        credit.setText(String.valueOf(course.getCredit()));
        weekFrom.setText(String.valueOf(course.getWeekFrom()));
        weekTo.setText(String.valueOf(course.getWeekTo()));
        timePerWeek.setText(String.valueOf(course.getTimesPerWeek()));
        switch (course.getTimesPerWeek()){
            case 3:
                weekday2.setText(String.valueOf(courses.get(2).getWeekday()));
                hourFrom2.setText(String.valueOf(courses.get(2).getHourFrom()));
                hourTo2.setText(String.valueOf(courses.get(2).getHourTo()));
                every2.setChecked(courses.get(2).getEveryWeek());
                not2.setChecked(!courses.get(2).getEveryWeek());
            case 2:
                weekday1.setText(String.valueOf(courses.get(1).getWeekday()));
                hourFrom1.setText(String.valueOf(courses.get(1).getHourFrom()));
                hourTo1.setText(String.valueOf(courses.get(1).getHourTo()));
                every1.setChecked(courses.get(1).getEveryWeek());
                not1.setChecked(!courses.get(1).getEveryWeek());
            case 1:
                weekday0.setText(String.valueOf(course.getWeekday()));
                hourFrom0.setText(String.valueOf(course.getHourFrom()));
                hourTo0.setText(String.valueOf(course.getHourTo()));
                every0.setChecked(course.getEveryWeek());
                not0.setChecked(!course.getEveryWeek());
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_course_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_course_commit:
                editCourse();
                break;
            default:
        }
        return true;
    }

    private void editCourse(){

        if ((TextUtils.isEmpty(weekFrom.getText().toString())) || (TextUtils.isEmpty(weekTo.getText().toString()))){

            AlertDialog.Builder builder = new AlertDialog.Builder(EditCourseActivity.this);
            builder.setMessage("上课周是不是忘填了？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            }).show();

            return;
        }

        if ((Integer.parseInt(weekFrom.getText().toString()) < 1) || (Integer.parseInt(weekFrom.getText().toString()) > 20) ||
                (Integer.parseInt(weekTo.getText().toString()) < 1) || (Integer.parseInt(weekTo.getText().toString()) > 20) ||
                (Integer.parseInt(weekFrom.getText().toString()) > Integer.parseInt(weekTo.getText().toString()))){

            AlertDialog.Builder builder = new AlertDialog.Builder(EditCourseActivity.this);
            builder.setMessage("上课周是不是填错了？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            }).show();

            return;
        }

        if(TextUtils.isEmpty(timePerWeek.getText().toString())){

            AlertDialog.Builder builder = new AlertDialog.Builder(EditCourseActivity.this);
            builder.setMessage("每周上课次数不能为空，请重新填写。");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            }).show();

            return;
        }

        DataSupport.deleteAll(Course.class,"name = ?",currentCourse);

        switch (Integer.parseInt(timePerWeek.getText().toString())){
            case 3:
                if ((TextUtils.isEmpty(hourFrom2.getText().toString())) || (TextUtils.isEmpty(hourTo2.getText().toString())) ||
                        (TextUtils.isEmpty(weekday2.getText().toString())) ||
                        (Integer.parseInt(hourFrom2.getText().toString()) < 1) || (Integer.parseInt(hourFrom2.getText().toString()) > 13) ||
                        (Integer.parseInt(hourTo2.getText().toString()) < 1) || (Integer.parseInt(hourTo2.getText().toString()) > 13) ||
                        (Integer.parseInt(hourFrom2.getText().toString()) > Integer.parseInt(hourTo2.getText().toString()) ||
                                (Integer.parseInt(weekday2.getText().toString()) < 1) || Integer.parseInt(weekday2.getText().toString()) > 7)){

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditCourseActivity.this);
                    builder.setMessage("上课时间是不是填错了？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).show();

                    return;
                }

                Course course = new Course();
                course.setNote(note);
                course.setColor(color);
                course.setName(name.getText().toString());
                course.setPlace(place.getText().toString());
                course.setTeacher(teacher.getText().toString());
                course.setType(type.getText().toString());
                course.setCredit(credit.getText().toString());
                course.setWeekFrom(Integer.parseInt(weekFrom.getText().toString()));
                course.setWeekTo(Integer.parseInt(weekTo.getText().toString()));
                course.setTimesPerWeek(Integer.parseInt(timePerWeek.getText().toString()));

                course.setWeekday(Integer.parseInt(weekday2.getText().toString()));
                course.setHourFrom(Integer.parseInt(hourFrom2.getText().toString()));
                course.setHourTo(Integer.parseInt(hourTo2.getText().toString()));

                course.setEveryWeek(every2.isChecked());
                course.setHomework(homework);

                course.save();

            case 2:
                if ((TextUtils.isEmpty(hourFrom1.getText().toString())) || (TextUtils.isEmpty(hourTo1.getText().toString())) ||
                        (TextUtils.isEmpty(weekday1.getText().toString())) ||
                        (Integer.parseInt(hourFrom1.getText().toString()) < 1) || (Integer.parseInt(hourFrom1.getText().toString()) > 13) ||
                        (Integer.parseInt(hourTo1.getText().toString()) < 1) || (Integer.parseInt(hourTo1.getText().toString()) > 13) ||
                        (Integer.parseInt(hourFrom1.getText().toString()) > Integer.parseInt(hourTo1.getText().toString()) ||
                                (Integer.parseInt(weekday1.getText().toString()) < 1) || Integer.parseInt(weekday1.getText().toString()) > 7)){

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditCourseActivity.this);
                    builder.setMessage("上课时间是不是填错了？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).show();

                    return;
                }
                Course course1 = new Course();
                course1.setNote(note);

                course1.setColor(color);
                course1.setName(name.getText().toString());
                course1.setPlace(place.getText().toString());
                course1.setTeacher(teacher.getText().toString());
                course1.setType(type.getText().toString());
                course1.setCredit(credit.getText().toString());
                course1.setWeekFrom(Integer.parseInt(weekFrom.getText().toString()));
                course1.setWeekTo(Integer.parseInt(weekTo.getText().toString()));
                course1.setTimesPerWeek(Integer.parseInt(timePerWeek.getText().toString()));

                course1.setWeekday(Integer.parseInt(weekday1.getText().toString()));
                course1.setHourFrom(Integer.parseInt(hourFrom1.getText().toString()));
                course1.setHourTo(Integer.parseInt(hourTo1.getText().toString()));

                course1.setEveryWeek(every1.isChecked());
                course1.setHomework(homework);

                course1.save();

            case 1:
                if ((TextUtils.isEmpty(hourFrom0.getText().toString())) || (TextUtils.isEmpty(hourTo0.getText().toString())) ||
                        (TextUtils.isEmpty(weekday0.getText().toString())) ||
                        (Integer.parseInt(hourFrom0.getText().toString()) < 1) || (Integer.parseInt(hourFrom0.getText().toString()) > 13) ||
                        (Integer.parseInt(hourTo0.getText().toString()) < 1) || (Integer.parseInt(hourTo0.getText().toString()) > 13) ||
                        (Integer.parseInt(hourFrom0.getText().toString()) > Integer.parseInt(hourTo0.getText().toString()) ||
                                (Integer.parseInt(weekday0.getText().toString()) < 1) || Integer.parseInt(weekday0.getText().toString()) > 7)){

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditCourseActivity.this);
                    builder.setMessage("上课时间是不是填错了？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).show();

                    return;
                }

                Course course2 = new Course();
                course2.setNote(note);

                course2.setColor(color);
                course2.setName(name.getText().toString());
                course2.setPlace(place.getText().toString());
                course2.setTeacher(teacher.getText().toString());
                course2.setType(type.getText().toString());
                course2.setCredit(credit.getText().toString());
                course2.setWeekFrom(Integer.parseInt(weekFrom.getText().toString()));
                course2.setWeekTo(Integer.parseInt(weekTo.getText().toString()));
                course2.setTimesPerWeek(Integer.parseInt(timePerWeek.getText().toString()));

                course2.setWeekday(Integer.parseInt(weekday0.getText().toString()));
                course2.setHourFrom(Integer.parseInt(hourFrom0.getText().toString()));
                course2.setHourTo(Integer.parseInt(hourTo0.getText().toString()));

                course2.setEveryWeek(every0.isChecked());
                course2.setHomework(homework);

                course2.save();
                break;
            default:
                AlertDialog.Builder builder = new AlertDialog.Builder(EditCourseActivity.this);
                builder.setMessage("每周上课次数应在1-3之间");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();

                return;
        }

        Intent intent1 = new Intent("com.example.app.UPDATE_WIDGET");
        sendBroadcast(intent1);

        finish();
    }

}
