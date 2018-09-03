package com.whuLoveStudyGroup.app;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;

/**
 * Created by 635901193 on 2017/10/8.
 */

public class SettingFragment extends PreferenceFragment {

    private static final String TAG = "dong";

    CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("key1");


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getPreferenceManager().setSharedPreferencesName("Setting");
        addPreferencesFromResource(R.xml.preferences);

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference.getKey().equals("delCourses")){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("确定要删除吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DataSupport.deleteAll(Course.class);
                    Toast.makeText(getActivity(), "课程信息已全部删除！", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();

        }else if (preference.getKey().equals("delScores")){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("确定要删除吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DataSupport.deleteAll(Score.class);
                    Toast.makeText(getActivity(), "成绩信息已全部删除！", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();

        }else if (preference.getKey().equals("delHomeworks")){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("确定要删除吗？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    List<Homework> homeworks = DataSupport.findAll(Homework.class);
                    for (Homework homework : homeworks){
                        File outputImage = new File(MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),homework.getTime() + ".jpg");
                        if(outputImage.exists())
                            outputImage.delete();
                        Toast.makeText(getActivity(), "作业信息（包括图片）已全部删除！", Toast.LENGTH_SHORT).show();
                    }
                    DataSupport.deleteAll(Homework.class);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.show();

            }
        return true;
    }
}
