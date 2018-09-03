package com.whuLoveStudyGroup.app;

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
import android.widget.EditText;
import android.widget.RadioButton;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 635901193 on 2017/7/26.
 */

public class AddCourseActivity extends MyActivity {
    private static final String TAG = "dong";

    EditText name,id,place,teacher,type,credit,weekFrom,weekTo,timePerWeek,weekday0,
            weekday1,weekday2, hourFrom0,hourFrom1,hourFrom2,hourTo0,hourTo1,hourTo2;
    RadioButton every0,every1,every2;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_course);

        //toobar初始化
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_course_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        name = (EditText) findViewById(R.id.add_course_name);
        id = (EditText) findViewById(R.id.add_course_id);
        place = (EditText) findViewById(R.id.add_course_place);
        teacher = (EditText) findViewById(R.id.add_course_teacher);
        type = (EditText) findViewById(R.id.add_course_type);
        credit = (EditText) findViewById(R.id.add_course_credit);
        weekFrom = (EditText) findViewById(R.id.add_course_week_from);
        weekTo = (EditText) findViewById(R.id.add_course_week_to);
        timePerWeek = (EditText) findViewById(R.id.add_course_times_per_week);
        weekday0 = (EditText) findViewById(R.id.add_course_weekday0);
        weekday1 = (EditText) findViewById(R.id.add_course_weekday1);
        weekday2 = (EditText) findViewById(R.id.add_course_weekday2);
        hourFrom0 = (EditText) findViewById(R.id.add_course_hour_from0);
        hourFrom1 = (EditText) findViewById(R.id.add_course_hour_from1);
        hourFrom2 = (EditText) findViewById(R.id.add_course_hour_from2);
        hourTo0 = (EditText) findViewById(R.id.add_course_hour_to0);
        hourTo1 = (EditText) findViewById(R.id.add_course_hour_to1);
        hourTo2 = (EditText) findViewById(R.id.add_course_hour_to2);
        every0 = (RadioButton) findViewById(R.id.add_course_every0);every0.setChecked(true);
        every1 = (RadioButton) findViewById(R.id.add_course_every1);every1.setChecked(true);
        every2 = (RadioButton) findViewById(R.id.add_course_every2);every2.setChecked(true);

    }

    //引入菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_course_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home://返回按钮点击事件
                finish();
                break;
            case R.id.add_course_commit://确定按钮点击事件
                addCourse();
                break;
            default:
        }
        return true;
    }

    private void addCourse(){

        List<Course> temp = DataSupport.where("name = ?",name.getText().toString()).find(Course.class);
        if (temp.size() != 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(AddCourseActivity.this);
            builder.setMessage("此课程已存在！");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            }).show();

            return;
        }

        if ((TextUtils.isEmpty(weekFrom.getText().toString())) || (TextUtils.isEmpty(weekTo.getText().toString()))){

            AlertDialog.Builder builder = new AlertDialog.Builder(AddCourseActivity.this);
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

            AlertDialog.Builder builder = new AlertDialog.Builder(AddCourseActivity.this);
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

            AlertDialog.Builder builder = new AlertDialog.Builder(AddCourseActivity.this);
            builder.setMessage("每周上课次数不能为空，请重新填写。");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            }).show();

            return;
        }

        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        String theme = sp.getString("theme","candy");//获取当前主题
        int number = sp.getInt(theme,0);//获取该主题颜色分发到哪个了，防止连续添加课程时颜色分配重复
        List<Theme> themes = DataSupport.where("name = ?",theme).find(Theme.class);

        //每周有几次课，就添加几组数据到数据库中
        switch (Integer.parseInt(timePerWeek.getText().toString())){
            case 3:
                if ((TextUtils.isEmpty(hourFrom2.getText().toString())) || (TextUtils.isEmpty(hourTo2.getText().toString())) ||
                        (TextUtils.isEmpty(weekday2.getText().toString())) ||
                        (Integer.parseInt(hourFrom2.getText().toString()) < 1) || (Integer.parseInt(hourFrom2.getText().toString()) > 13) ||
                        (Integer.parseInt(hourTo2.getText().toString()) < 1) || (Integer.parseInt(hourTo2.getText().toString()) > 13) ||
                        (Integer.parseInt(hourFrom2.getText().toString()) > Integer.parseInt(hourTo2.getText().toString()) ||
                        (Integer.parseInt(weekday2.getText().toString()) < 1) || Integer.parseInt(weekday2.getText().toString()) > 7)){

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddCourseActivity.this);
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
                //添加颜色信息
                switch (number%15){
                    case 0:
                        course.setColor(themes.get(0).getColor0());break;
                    case 1:
                        course.setColor(themes.get(0).getColor1());break;
                    case 2:
                        course.setColor(themes.get(0).getColor2());break;
                    case 3:
                        course.setColor(themes.get(0).getColor3());break;
                    case 4:
                        course.setColor(themes.get(0).getColor4());break;
                    case 5:
                        course.setColor(themes.get(0).getColor5());break;
                    case 6:
                        course.setColor(themes.get(0).getColor6());break;
                    case 7:
                        course.setColor(themes.get(0).getColor7());break;
                    case 8:
                        course.setColor(themes.get(0).getColor8());break;
                    case 9:
                        course.setColor(themes.get(0).getColor9());break;
                    case 10:
                        course.setColor(themes.get(0).getColor10());break;
                    case 11:
                        course.setColor(themes.get(0).getColor11());break;
                    case 12:
                        course.setColor(themes.get(0).getColor12());break;
                    case 13:
                        course.setColor(themes.get(0).getColor13());break;
                    case 14:
                        course.setColor(themes.get(0).getColor14());break;
                    default:
                }

                //添加其他信息
                course.setName(name.getText().toString());
                course.setLessoneID(id.getText().toString().equals("")?"0":id.getText().toString());
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
                course.setHomework(false);

                course.save();

            case 2:
                if ((TextUtils.isEmpty(hourFrom1.getText().toString())) || (TextUtils.isEmpty(hourTo1.getText().toString())) ||
                        (TextUtils.isEmpty(weekday1.getText().toString())) ||
                        (Integer.parseInt(hourFrom1.getText().toString()) < 1) || (Integer.parseInt(hourFrom1.getText().toString()) > 13) ||
                        (Integer.parseInt(hourTo1.getText().toString()) < 1) || (Integer.parseInt(hourTo1.getText().toString()) > 13) ||
                        (Integer.parseInt(hourFrom1.getText().toString()) > Integer.parseInt(hourTo1.getText().toString()) ||
                                (Integer.parseInt(weekday1.getText().toString()) < 1) || Integer.parseInt(weekday1.getText().toString()) > 7)){

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddCourseActivity.this);
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
                switch (number%15){
                    case 0:
                        course1.setColor(themes.get(0).getColor0());break;
                    case 1:
                        course1.setColor(themes.get(0).getColor1());break;
                    case 2:
                        course1.setColor(themes.get(0).getColor2());break;
                    case 3:
                        course1.setColor(themes.get(0).getColor3());break;
                    case 4:
                        course1.setColor(themes.get(0).getColor4());break;
                    case 5:
                        course1.setColor(themes.get(0).getColor5());break;
                    case 6:
                        course1.setColor(themes.get(0).getColor6());break;
                    case 7:
                        course1.setColor(themes.get(0).getColor7());break;
                    case 8:
                        course1.setColor(themes.get(0).getColor8());break;
                    case 9:
                        course1.setColor(themes.get(0).getColor9());break;
                    case 10:
                        course1.setColor(themes.get(0).getColor10());break;
                    case 11:
                        course1.setColor(themes.get(0).getColor11());break;
                    case 12:
                        course1.setColor(themes.get(0).getColor12());break;
                    case 13:
                        course1.setColor(themes.get(0).getColor13());break;
                    case 14:
                        course1.setColor(themes.get(0).getColor14());break;
                    default:
                }


                course1.setName(name.getText().toString());
                course1.setLessoneID(id.getText().toString().equals("")?"0":id.getText().toString());
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
                course1.setHomework(false);

                course1.save();

            case 1:
                if ((TextUtils.isEmpty(hourFrom0.getText().toString())) || (TextUtils.isEmpty(hourTo0.getText().toString())) ||
                        (TextUtils.isEmpty(weekday0.getText().toString())) ||
                        (Integer.parseInt(hourFrom0.getText().toString()) < 1) || (Integer.parseInt(hourFrom0.getText().toString()) > 13) ||
                        (Integer.parseInt(hourTo0.getText().toString()) < 1) || (Integer.parseInt(hourTo0.getText().toString()) > 13) ||
                        (Integer.parseInt(hourFrom0.getText().toString()) > Integer.parseInt(hourTo0.getText().toString()) ||
                                (Integer.parseInt(weekday0.getText().toString()) < 1) || Integer.parseInt(weekday0.getText().toString()) > 7)){

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddCourseActivity.this);
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
                switch (number%15){
                    case 0:
                        course2.setColor(themes.get(0).getColor0());break;
                    case 1:
                        course2.setColor(themes.get(0).getColor1());break;
                    case 2:
                        course2.setColor(themes.get(0).getColor2());break;
                    case 3:
                        course2.setColor(themes.get(0).getColor3());break;
                    case 4:
                        course2.setColor(themes.get(0).getColor4());break;
                    case 5:
                        course2.setColor(themes.get(0).getColor5());break;
                    case 6:
                        course2.setColor(themes.get(0).getColor6());break;
                    case 7:
                        course2.setColor(themes.get(0).getColor7());break;
                    case 8:
                        course2.setColor(themes.get(0).getColor8());break;
                    case 9:
                        course2.setColor(themes.get(0).getColor9());break;
                    case 10:
                        course2.setColor(themes.get(0).getColor10());break;
                    case 11:
                        course2.setColor(themes.get(0).getColor11());break;
                    case 12:
                        course2.setColor(themes.get(0).getColor12());break;
                    case 13:
                        course2.setColor(themes.get(0).getColor13());break;
                    case 14:
                        course2.setColor(themes.get(0).getColor14());break;
                    default:
                }


                course2.setName(name.getText().toString());
                course2.setLessoneID(id.getText().toString().equals("")?"0":id.getText().toString());
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
                course2.setHomework(false);

                course2.save();
                break;
            default:
                AlertDialog.Builder builder = new AlertDialog.Builder(AddCourseActivity.this);
                builder.setMessage("每周上课次数应在1-3之间");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).show();

                return;
        }

        //颜色分发数加1并保存到本地
        number++;
        SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
        editor.putInt(theme,number);
        editor.apply();

        //发送广播更新插件
        Intent intent1 = new Intent("com.whuLoveStudyGroup.app.UPDATE_WIDGET");
        sendBroadcast(intent1);

        finish();
    }

}
