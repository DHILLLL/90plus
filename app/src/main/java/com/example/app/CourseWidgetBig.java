package com.example.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class CourseWidgetBig extends AppWidgetProvider {

    private static final String TAG = "dongheyou";

    private final int[] weekday = {R.id.widget_big_sun, R.id.widget_big_mon, R.id.widget_big_tue,
            R.id.widget_big_wed, R.id.widget_big_thur, R.id.widget_big_fri, R.id.widget_big_sat};
    private final int[] weekday0 = {R.id.widget_big_sun0, R.id.widget_big_mon0, R.id.widget_big_tue0,
            R.id.widget_big_wed0, R.id.widget_big_thur0, R.id.widget_big_fri0, R.id.widget_big_sat0};
    private final int[] weekday1 = {R.id.widget_big_sun1, R.id.widget_big_mon1, R.id.widget_big_tue1,
            R.id.widget_big_wed1, R.id.widget_big_thur1, R.id.widget_big_fri1, R.id.widget_big_sat1};
    private final int[] color = {R.drawable.half_red, R.drawable.half_orange, R.drawable.half_yellow,
            R.drawable.half_green, R.drawable.half_cyan, R.drawable.half_blue, R.drawable.half_purple};
    private final int[] times = {R.id.widget_big_time1, R.id.widget_big_time2, R.id.widget_big_time3};
    private final int[] Courses = {R.id.widget_big_course1, R.id.widget_big_course2, R.id.widget_big_course3};
    private final int[] places = {R.id.widget_big_place1, R.id.widget_big_place2, R.id.widget_big_place3};
    private final int[] areas = {R.id.widget_big_area1, R.id.widget_big_area2, R.id.widget_big_area3};
    private final int[] homeworks = {R.id.widget_big_homework1, R.id.widget_big_homework2, R.id.widget_big_homework3};

    private int currentWeek;

    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.course_widget_big);
        refresh(remoteViews,context);
        appWidgetManager.updateAppWidget(appWidgetId,remoteViews);

    }


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

    }

    private void refresh(RemoteViews remoteViews, Context context){
        SharedPreferences sp = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        currentWeek = sp.getInt("currentWeekReal",1);

        final Calendar calendar = Calendar.getInstance();
        int cd = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        SharedPreferences.Editor editor = context.getSharedPreferences("widget", Context.MODE_PRIVATE).edit();
        editor.putInt("currentDay",cd);
        editor.apply();

        List<Course> courses = DataSupport.where("weekday = ? and weekFrom <= ? and weekTo >= ?",String.valueOf(cd), String.valueOf(currentWeek), String.valueOf(currentWeek)).order("hourFrom ASC").find(Course.class);
        if (currentWeek % 2 == 1){
            for(int i = courses.size() - 1;i>=0;i--){
                if (!courses.get(i).getEveryWeek())
                    courses.remove(i);
            }
        }

        for (int i = 0;i<7;i++){
            remoteViews.setOnClickPendingIntent(weekday[i],getPendingIntent(context,weekday[i],"change"));
            remoteViews.setViewVisibility(weekday0[i],View.VISIBLE);
            remoteViews.setViewVisibility(weekday1[i],View.GONE);
        }

        for (int i = 0;i<3;i++){
            remoteViews.setViewVisibility(areas[i],View.INVISIBLE);
            remoteViews.setInt(times[i],"setBackgroundResource",color[cd]);
        }

        remoteViews.setViewVisibility(weekday1[cd],View.VISIBLE);
        remoteViews.setViewVisibility(weekday0[cd],View.GONE);
        remoteViews.setViewVisibility(R.id.widget_big_next_icon,View.GONE);

        if (courses.size() == 0){
            remoteViews.setViewVisibility(R.id.widget_big_main,View.GONE);
            remoteViews.setViewVisibility(R.id.widget_big_no,View.VISIBLE);

        }else{
            remoteViews.setViewVisibility(R.id.widget_big_main,View.VISIBLE);
            remoteViews.setViewVisibility(R.id.widget_big_no,View.GONE);

            for (int j = 0;j<courses.size()&&j<3;j++){
                remoteViews.setOnClickPendingIntent(areas[j],getPendingIntent(context,areas[j],"app" + courses.get(j).getName()));
                remoteViews.setTextViewText(Courses[j],courses.get(j).getName());
                remoteViews.setTextViewText(places[j],"@" + courses.get(j).getPlace());
                remoteViews.setTextViewText(times[j],courses.get(j).getHourFrom() + "-" + courses.get(j).getHourTo());
                remoteViews.setViewVisibility(homeworks[j],courses.get(j).getHomework()?View.VISIBLE:View.GONE);
                remoteViews.setViewVisibility(areas[j],View.VISIBLE);
            }

            if (courses.size() > 3){
                remoteViews.setViewVisibility(R.id.widget_big_next_icon,View.VISIBLE);
                remoteViews.setOnClickPendingIntent(R.id.widget_big_next,getPendingIntent(context,R.id.widget_big_next,"next"));
                remoteViews.setImageViewResource(R.id.widget_big_next_icon,R.drawable.dao);
            }
        }

    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        currentWeek = CourseWidget.setCurrentWeek();
        SharedPreferences.Editor editor = context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        editor.putInt("currentWeek",currentWeek);
        editor.putInt("currentWeekReal",currentWeek);
        editor.apply();

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.course_widget_big);
        refresh(remoteViews,context);

        appWidgetManager.updateAppWidget(appWidgetIds,remoteViews);
    }

    private PendingIntent getPendingIntent(Context context, int resID, String action){
        Intent intent = new Intent();
        intent.setClass(context,CourseWidgetBig.class);
        intent.setAction(action);
        intent.setData(Uri.parse("id:" + resID));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
        return pendingIntent;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Intent intent1 = new Intent("com.example.app.UPDATE_WIDGET");
        context.sendBroadcast(intent1);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.course_widget_big);
        SharedPreferences sp1 = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        currentWeek = sp1.getInt("currentWeekReal",1);
        
        if (intent.getAction().equals("com.example.app.UPDATE_WIDGET") || intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            refresh(remoteViews,context);
        }
        
        if(intent.getAction().substring(0,3).equals("app")){
            Intent startAcIntent = new Intent();
            startAcIntent.setComponent(new ComponentName("com.example.app","com.example.app.courseActivity"));//第一个是包名，第二个是类所在位置的全称
            startAcIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startAcIntent.putExtra("course",intent.getAction().substring(3));
            context.startActivity(startAcIntent);
        }

        if(intent.getAction().equals("change")){

            Uri data = intent.getData();
            int resId = -1;
            if (data != null){
                resId = Integer.parseInt(data.getSchemeSpecificPart());
            }
            int i; for(i = 0;weekday[i] != resId;i++);

            List<Course> courses = DataSupport.where("weekday = ? and weekFrom <= ? and weekTo >= ?",String.valueOf((i==0)?7:i), String.valueOf(currentWeek), String.valueOf(currentWeek)).order("hourFrom ASC").find(Course.class);

            if (currentWeek % 2 == 1){
                for(int j = courses.size() - 1;j>=0;j--){
                    if (!courses.get(j).getEveryWeek())
                        courses.remove(j);
                }
            }

            SharedPreferences sp = context.getSharedPreferences("widget",Context.MODE_PRIVATE);
            int cd = sp.getInt("currentDay",0);
            SharedPreferences.Editor editor = context.getSharedPreferences("widget", Context.MODE_PRIVATE).edit();

            if(cd != i) {
                remoteViews.setViewVisibility(weekday0[cd],View.VISIBLE);
                remoteViews.setViewVisibility(weekday1[cd],View.GONE);
                editor.putInt("currentDay",i);
                editor.apply();

                remoteViews.setViewVisibility(weekday0[i],View.GONE);
                remoteViews.setViewVisibility(weekday1[i],View.VISIBLE);
                remoteViews.setInt(R.id.widget_big_time1,"setBackgroundResource",color[i]);
                remoteViews.setInt(R.id.widget_big_time2,"setBackgroundResource",color[i]);
                remoteViews.setInt(R.id.widget_big_time3,"setBackgroundResource",color[i]);

                remoteViews.setViewVisibility(R.id.widget_big_area1,View.INVISIBLE);
                remoteViews.setViewVisibility(R.id.widget_big_area2,View.INVISIBLE);
                remoteViews.setViewVisibility(R.id.widget_big_area3,View.INVISIBLE);
                remoteViews.setViewVisibility(R.id.widget_big_next_icon,View.GONE);

                if (courses.size() == 0){
                    remoteViews.setViewVisibility(R.id.widget_big_main,View.GONE);
                    remoteViews.setViewVisibility(R.id.widget_big_no,View.VISIBLE);
                }else {
                    remoteViews.setViewVisibility(R.id.widget_big_main, View.VISIBLE);
                    remoteViews.setViewVisibility(R.id.widget_big_no, View.GONE);

                    for (int j = 0; j < courses.size() && j < 3; j++) {
                        remoteViews.setOnClickPendingIntent(areas[j], getPendingIntent(context, areas[j], "app" + courses.get(j).getName()));
                        remoteViews.setTextViewText(Courses[j], courses.get(j).getName());
                        remoteViews.setTextViewText(places[j], "@" + courses.get(j).getPlace());
                        remoteViews.setTextViewText(times[j], courses.get(j).getHourFrom() + "-" + courses.get(j).getHourTo());
                        remoteViews.setViewVisibility(homeworks[j], courses.get(j).getHomework() ? View.VISIBLE : View.GONE);
                        remoteViews.setViewVisibility(areas[j], View.VISIBLE);
                        remoteViews.setImageViewResource(R.id.widget_big_next_icon, R.drawable.dao);
                    }

                    if (courses.size() > 3) {
                        remoteViews.setViewVisibility(R.id.widget_big_next_icon, View.VISIBLE);
                        remoteViews.setOnClickPendingIntent(R.id.widget_big_next, getPendingIntent(context, R.id.widget_big_next, "next"));
                    }
                }

            }
        }

        if (intent.getAction().equals("next")){
            SharedPreferences sp = context.getSharedPreferences("widget",Context.MODE_PRIVATE);
            int cd = sp.getInt("currentDay",0);
            List<Course> courses = DataSupport.where("weekday = ? and weekFrom <= ? and weekTo >= ?",String.valueOf((cd==0)?7:cd), String.valueOf(currentWeek), String.valueOf(currentWeek)).order("hourFrom ASC").find(Course.class);
            if (currentWeek % 2 == 1){
                for(int i = courses.size() - 1;i>=0;i--){
                    if (!courses.get(i).getEveryWeek())
                        courses.remove(i);
                }
            }

            remoteViews.setViewVisibility(R.id.widget_big_area1,View.INVISIBLE);
            remoteViews.setViewVisibility(R.id.widget_big_area2,View.INVISIBLE);
            remoteViews.setViewVisibility(R.id.widget_big_area3,View.INVISIBLE);

            if (courses.size() == 0){
                remoteViews.setViewVisibility(R.id.widget_big_main,View.GONE);
                remoteViews.setViewVisibility(R.id.widget_big_no,View.VISIBLE);
            }else {
                remoteViews.setViewVisibility(R.id.widget_big_main, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.widget_big_no, View.GONE);

                for (int k = 3; k < courses.size() && k < 6; k++) {
                    remoteViews.setOnClickPendingIntent(areas[k - 3], getPendingIntent(context, areas[k - 3], "app" + courses.get(k).getName()));
                    remoteViews.setTextViewText(Courses[k - 3], courses.get(k).getName());
                    remoteViews.setTextViewText(places[k - 3], "@" + courses.get(k).getPlace());
                    remoteViews.setTextViewText(times[k - 3], courses.get(k).getHourFrom() + "-" + courses.get(k).getHourTo());
                    remoteViews.setViewVisibility(homeworks[k - 3], courses.get(k).getHomework() ? View.VISIBLE : View.GONE);
                    remoteViews.setViewVisibility(areas[k - 3], View.VISIBLE);
                }
            }

            remoteViews.setImageViewResource(R.id.widget_big_next_icon,R.drawable.zheng);
            remoteViews.setOnClickPendingIntent(R.id.widget_big_next,getPendingIntent(context,R.id.widget_big_next,"before"));
        }

        if (intent.getAction().equals("before")){
            SharedPreferences sp = context.getSharedPreferences("widget",Context.MODE_PRIVATE);
            int cd = sp.getInt("currentDay",0);

            List<Course> courses = DataSupport.where("weekday = ? and weekFrom <= ? and weekTo >= ?",String.valueOf((cd==0)?7:cd), String.valueOf(currentWeek), String.valueOf(currentWeek)).order("hourFrom ASC").find(Course.class);
            if (currentWeek % 2 == 1){
                for(int i = courses.size() - 1;i>=0;i--){
                    if (!courses.get(i).getEveryWeek())
                        courses.remove(i);
                }
            }

            remoteViews.setViewVisibility(R.id.widget_big_area1,View.INVISIBLE);
            remoteViews.setViewVisibility(R.id.widget_big_area2,View.INVISIBLE);
            remoteViews.setViewVisibility(R.id.widget_big_area3,View.INVISIBLE);

            if (courses.size() == 0){
                remoteViews.setViewVisibility(R.id.widget_big_main,View.GONE);
                remoteViews.setViewVisibility(R.id.widget_big_no,View.VISIBLE);
            }else {
                remoteViews.setViewVisibility(R.id.widget_big_main, View.VISIBLE);
                remoteViews.setViewVisibility(R.id.widget_big_no, View.GONE);

                for (int k = 0; k < courses.size() && k < 3; k++) {
                    remoteViews.setOnClickPendingIntent(areas[k], getPendingIntent(context, areas[k], "app" + courses.get(k).getName()));
                    remoteViews.setTextViewText(Courses[k], courses.get(k).getName());
                    remoteViews.setTextViewText(places[k], "@" + courses.get(k).getPlace());
                    remoteViews.setTextViewText(times[k], courses.get(k).getHourFrom() + "-" + courses.get(k).getHourTo());
                    remoteViews.setViewVisibility(homeworks[k], courses.get(k).getHomework() ? View.VISIBLE : View.GONE);
                    remoteViews.setViewVisibility(areas[k], View.VISIBLE);
                }
            }
            remoteViews.setImageViewResource(R.id.widget_big_next_icon,R.drawable.dao);
            remoteViews.setOnClickPendingIntent(R.id.widget_big_next,getPendingIntent(context,R.id.widget_big_next,"next"));
        }

        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName name = new ComponentName(context,CourseWidgetBig.class);
        manager.updateAppWidget(name,remoteViews);

    }
}

