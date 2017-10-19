package com.whuLoveStudyGroup.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by 635901193 on 2017/7/27.
 */

public class MyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //设置当前主题
        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        String theme = sp.getString("theme","candy");
        if (theme.equals("coffee")){
            setTheme(R.style.Coffee);
        }
        if (theme.equals("candy")){
            setTheme(R.style.AppTheme);
        }
        if (theme.equals("harvest")){
            setTheme(R.style.Harvest);
        }
        if (theme.equals("sea")){
            setTheme(R.style.Sea);
        }
        if (theme.equals("pink")){
            setTheme(R.style.Pink);
        }
        if (theme.equals("butterfly")){
            setTheme(R.style.Butterfly);
        }
        if (theme.equals("forgive")){
            setTheme(R.style.Forgive);
        }
        if (theme.equals("sexless")){
            setTheme(R.style.Sexless);
        }

        ActivityCollector.addActivity(this);
    }

    //直接退出程序
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
