package com.whuLoveStudyGroup.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RemoteViews;

import org.litepal.crud.DataSupport;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

/**
 * Implementation of App Widget functionality.
 */
public class HomeworkWidget extends AppWidgetProvider {

    private static final String TAG = "dongheyou";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

    }

    private int[] colors = {R.drawable.top_red, R.drawable.top_orange, R.drawable.top_yellow,
            R.drawable.top_green, R.drawable.top_cyan, R.drawable.top_blue, R.drawable.top_purple } ;

    private int randomColor(){
        Random random = new Random();
        return colors[random.nextInt(7)];
    }


    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.homework_widget);
        /*
        if(newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT) < 200){
            remoteViews.setTextViewTextSize(R.id.homework_widget_course, TypedValue.COMPLEX_UNIT_SP ,12);
            remoteViews.setTextViewTextSize(R.id.homework_widget_work, TypedValue.COMPLEX_UNIT_SP ,11);
            remoteViews.setTextViewTextSize(R.id.homework_widget_left, TypedValue.COMPLEX_UNIT_SP ,10);
            remoteViews.setTextViewTextSize(R.id.homework_widget_right, TypedValue.COMPLEX_UNIT_SP ,10);
            remoteViews.setTextViewTextSize(R.id.homework_widget_add, TypedValue.COMPLEX_UNIT_SP ,10);
        }else {
            remoteViews.setTextViewTextSize(R.id.homework_widget_course,TypedValue.COMPLEX_UNIT_SP,18);
            remoteViews.setTextViewTextSize(R.id.homework_widget_work, TypedValue.COMPLEX_UNIT_SP ,17);
            remoteViews.setTextViewTextSize(R.id.homework_widget_left, TypedValue.COMPLEX_UNIT_SP ,16);
            remoteViews.setTextViewTextSize(R.id.homework_widget_right, TypedValue.COMPLEX_UNIT_SP ,16);
            remoteViews.setTextViewTextSize(R.id.homework_widget_add, TypedValue.COMPLEX_UNIT_SP ,16);
        }*/

        SharedPreferences sp = context.getSharedPreferences("widget",Context.MODE_PRIVATE);
        set(context,remoteViews,sp.getInt("currentHomework",0));

        appWidgetManager.updateAppWidget(appWidgetId,remoteViews);
    }

    private void set(Context context, final RemoteViews remoteViews, int currentHomework){

        remoteViews.setOnClickPendingIntent(R.id.homework_widget_left,getPendingIntent(context,R.id.homework_widget_left,"change"));
        remoteViews.setOnClickPendingIntent(R.id.homework_widget_right,getPendingIntent(context,R.id.homework_widget_right,"change"));
        remoteViews.setOnClickPendingIntent(R.id.homework_widget_add,getPendingIntent(context,R.id.homework_widget_add,"add"));

        List<Homework> homeworks = DataSupport.where("finished = ?","0").order("time DESC").find(Homework.class);

        if (homeworks.size() != 0) {

            remoteViews.setViewVisibility(R.id.homework_widget_main,View.VISIBLE);
            remoteViews.setViewVisibility(R.id.homework_widget_no,View.GONE);

            currentHomework = (currentHomework <= homeworks.size())?currentHomework:homeworks.size() - 1;
            Homework homework = homeworks.get(currentHomework);

            remoteViews.setInt(R.id.homework_widget_course,"setBackgroundResource",randomColor());

            remoteViews.setOnClickPendingIntent(R.id.homework_widget_course,getPendingIntent(context,R.id.homework_widget_course,String.valueOf(homework.getTime())));
            remoteViews.setOnClickPendingIntent(R.id.homework_widget_pic,getPendingIntent(context,R.id.homework_widget_pic,homework.getPic()));

            remoteViews.setTextViewText(R.id.homework_widget_course,"(" + (currentHomework + 1) + "/" + homeworks.size() + ")" + homework.getCourse());

            if (homework.getWord().equals("") && homework.getDeadLine().equals("")){
                remoteViews.setViewVisibility(R.id.homework_widget_work, View.GONE);
            } else {
                remoteViews.setViewVisibility(R.id.homework_widget_work, View.VISIBLE);
                if (homework.getDeadLine().equals("")){
                    remoteViews.setTextViewText(R.id.homework_widget_work,homework.getWord());
                }else if (homework.getWord().equals("")){
                    remoteViews.setTextViewText(R.id.homework_widget_work,"\t" + homework.getDeadLine() + " 截止：");
                }else{
                    remoteViews.setTextViewText(R.id.homework_widget_work,"\t" + homework.getDeadLine() + " 截止：\n" + homework.getWord());
                }
            }
            if (homework.getIsPhoto()) {
                try {
                    //String uri = homework.getPic().replaceAll(".jpg","s.jpg");
                    Bitmap bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(),Uri.parse(homework.getPic()));
                    remoteViews.setImageViewBitmap(R.id.homework_widget_pic, bm);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                remoteViews.setViewVisibility(R.id.homework_widget_pic, View.VISIBLE);
            }else{
                remoteViews.setViewVisibility(R.id.homework_widget_pic, View.GONE);
            }

        }else{

            remoteViews.setViewVisibility(R.id.homework_widget_main,View.INVISIBLE);
            remoteViews.setViewVisibility(R.id.homework_widget_no,View.VISIBLE);

            remoteViews.setInt(R.id.homework_widget_course,"setBackgroundResource",randomColor());
            remoteViews.setTextViewText(R.id.homework_widget_course,"0/0");
            }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.homework_widget);

        remoteViews.setOnClickPendingIntent(R.id.homework_widget_left,getPendingIntent(context,R.id.homework_widget_left,"change"));
        remoteViews.setOnClickPendingIntent(R.id.homework_widget_right,getPendingIntent(context,R.id.homework_widget_right,"change"));
        remoteViews.setOnClickPendingIntent(R.id.homework_widget_add,getPendingIntent(context,R.id.homework_widget_add,"add"));

        SharedPreferences.Editor editor = context.getSharedPreferences("widget",Context.MODE_PRIVATE).edit();
        editor.putInt("currentHomework",0);
        editor.apply();

        set(context,remoteViews,0);

        appWidgetManager.updateAppWidget(appWidgetIds,remoteViews);
    }

    private PendingIntent getPendingIntent(Context context, int resID, String action){
        Intent intent = new Intent();
        intent.setClass(context,HomeworkWidget.class);
        intent.setAction(action);
        intent.setData(Uri.parse("id:" + resID));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
        return pendingIntent;
    }

    @Override
    public void onEnabled(Context context) {

        SharedPreferences.Editor editor = context.getSharedPreferences("data",Context.MODE_PRIVATE).edit();
        SharedPreferences sp = context.getSharedPreferences("data",Context.MODE_PRIVATE);

        if (sp.getBoolean("firstWidget",true)){

            Homework homework = new Homework();
            homework.setFinished(false);
            homework.setWord("1.点击上方彩色标题就可以标志为已完成\n2.再次点击可恢复为未完成\n3.点击上一条或下一条能起到刷新的作用\n4.万一（我是说万一）插件出现问题了，打开APP就会自动解决");
            homework.setDeadLine("");
            homework.setIsPhoto(false);
            homework.setCourse("我是作业小插件");

            final Calendar calendar = Calendar.getInstance();
            final long x = (long)(calendar.get(Calendar.YEAR)) * 10000000000L + (long)((calendar.get(Calendar.MONTH) + 1)) * 100000000L
                    + (long)(calendar.get(Calendar.DAY_OF_MONTH)) * 1000000L + (long)(calendar.get(Calendar.HOUR_OF_DAY)) * 10000L
                    + (long)(calendar.get(Calendar.MINUTE)) * 100L + (long)(calendar.get(Calendar.SECOND));
            homework.setTime(x);

            homework.save();

            editor.putBoolean("firstWidget",false);
            editor.apply();
        }

        Intent intent1 = new Intent("com.whuLoveStudyGroup.app.UPDATE_WIDGET");
        context.sendBroadcast(intent1);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        List<Homework> homeworks = DataSupport.where("finished = ?","0").order("time DESC").find(Homework.class);
        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.homework_widget);
        SharedPreferences sp = context.getSharedPreferences("widget",Context.MODE_PRIVATE);
        int currentHomework = sp.getInt("currentHomework",0);

        SharedPreferences.Editor editor = context.getSharedPreferences("widget",Context.MODE_PRIVATE).edit();

        if (intent.getAction().equals("com.whuLoveStudyGroup.app.UPDATE_WIDGET") || intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){
            editor.putInt("currentHomework",0);
            editor.apply();
            set(context,remoteViews,0);
        }

        Uri data = intent.getData();
        int resId = -1;
        if (data != null){
            resId = Integer.parseInt(data.getSchemeSpecificPart());
        }
        switch (resId){
            case R.id.homework_widget_left:
                if (homeworks.size() != 0) {
                    currentHomework = (currentHomework + homeworks.size() - 1) % homeworks.size();
                }else{
                    currentHomework = 0;
                }
                editor.putInt("currentHomework",currentHomework);
                editor.apply();
                set(context,remoteViews,currentHomework);
                break;
            case R.id.homework_widget_right:
                if (homeworks.size() != 0) {
                    currentHomework = (currentHomework + homeworks.size() + 1) % homeworks.size();
                }else{
                    currentHomework = 0;
                }
                editor.putInt("currentHomework",currentHomework);
                editor.apply();
                set(context,remoteViews,currentHomework);
                break;
            case R.id.homework_widget_course:
                String time = intent.getAction();
                List<Homework> homeworklist = DataSupport.where("time = ?",time).find(Homework.class);
                if (homeworklist.get(0).getFinished()){
                    remoteViews.setInt(R.id.homework_widget_course,"setBackgroundResource",randomColor());
                    ContentValues value = new ContentValues();
                    value.put("finished",false);
                    DataSupport.updateAll(Homework.class,value,"time = ?",time);

                    ContentValues value0 = new ContentValues();
                    value0.put("homework",true);
                    DataSupport.updateAll(Course.class,value0,"name = ?",homeworklist.get(0).getCourse());
                }else{
                    remoteViews.setInt(R.id.homework_widget_course,"setBackgroundResource",R.drawable.top_gray);
                    ContentValues value = new ContentValues();
                    value.put("finished",true);
                    DataSupport.updateAll(Homework.class,value,"time = ?",time);

                    List<Homework> homework1 = DataSupport.where("course = ? and finished = ?",homeworklist.get(0).getCourse(),"0").find(Homework.class);
                    if (homework1.size() == 0){
                        ContentValues value1 = new ContentValues();
                        value1.put("homework",false);
                        DataSupport.updateAll(Course.class,value1,"name = ?",homeworklist.get(0).getCourse());
                    }
                }
                break;
            case R.id.homework_widget_add:
                Intent startAcIntent = new Intent();
                startAcIntent.setComponent(new ComponentName("com.whuLoveStudyGroup.app","com.whuLoveStudyGroup.app.AddHomeworkActivity"));//第一个是包名，第二个是类所在位置的全称
                startAcIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startAcIntent);
                break;
            case R.id.homework_widget_pic:
                Intent startAcIntent1 = new Intent(Intent.ACTION_VIEW);
                startAcIntent1.setDataAndType(Uri.parse(intent.getAction()), "image/*");
                startAcIntent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startAcIntent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(startAcIntent1);
                break;
            default:
        }


        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName name = new ComponentName(context,HomeworkWidget.class);
        manager.updateAppWidget(name,remoteViews);

    }
}


