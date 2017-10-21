package com.whuLoveStudyGroup.app;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.List;

/**
 * Created by 635901193 on 2017/7/20.
 */

public class HomeworkAdapter extends RecyclerView.Adapter<HomeworkAdapter.ViewHolder> {
    private Context context;
    private List<Homework> HomeworkList;
    private int colorPrimary;
    private LocalBroadcastManager localBroadcastManager;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        CardView cardView;
        TextView course,work,deadLine;
        ImageView pic;
        CheckBox finished;

        public ViewHolder(View view){
            super(view);
            mView = view;
            cardView = (CardView) view;
            course = (TextView) view.findViewById(R.id.homework_course);
            work = (TextView) view.findViewById(R.id.homework_work);
            pic = (ImageView) view.findViewById(R.id.homework_pic);
            deadLine = (TextView) view.findViewById(R.id.homework_deadLine);
            finished = (CheckBox) view.findViewById(R.id.homework_finished);
        }
    }

    public HomeworkAdapter(List<Homework> homeworkList){
        HomeworkList = homeworkList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }

        localBroadcastManager = LocalBroadcastManager.getInstance(context);

        //根据主题确定主颜色
        SharedPreferences sharedPreferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        if (sharedPreferences.getString("theme","candy").equals("candy")) colorPrimary = 0xFF00CDA5;
        if (sharedPreferences.getString("theme","candy").equals("coffee")) colorPrimary = 0xFF8F6D52;
        if (sharedPreferences.getString("theme","candy").equals("sea")) colorPrimary = 0xFF229DCD;
        if (sharedPreferences.getString("theme","candy").equals("pink")) colorPrimary = 0xFFF09199;
        if (sharedPreferences.getString("theme","candy").equals("harvest")) colorPrimary = 0xFFFF9235;
        if (sharedPreferences.getString("theme","candy").equals("butterfly")) colorPrimary = 0xFF83668A;
        if (sharedPreferences.getString("theme","candy").equals("forgive")) colorPrimary = 0xFF66BB6A;
        if (sharedPreferences.getString("theme","candy").equals("sexless")) colorPrimary = 0xFFAACBCC;

        View view = LayoutInflater.from(context).inflate(R.layout.homework_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        //作业完成按钮事件
        holder.finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取当前作业项
                int position = holder.getAdapterPosition();
                Homework homework = HomeworkList.get(position);

                //获取当前筛选作业筛选方式（0为显示全部，1为仅未完成，2为仅已完成）
                SharedPreferences sharedPreferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
                int display = sharedPreferences.getInt("display",0);

                //从未完成到已完成时
                if(holder.finished.isChecked()) {
                    //更改作业数据库信息
                    ContentValues values = new ContentValues();
                    values.put("finished",true);
                    DataSupport.updateAll(Homework.class,values,"time = ?",String.valueOf(homework.getTime()));

                    //查询该作业的课程（如果存在）是否还有未完成作业
                    List<Homework> homeworks = DataSupport.where("course = ? and finished = ?",homework.getCourse(),"0").find(Homework.class);
                    //没有的话就将该课程数据库信息设置为无作业
                    if (homeworks.size() == 0){
                        ContentValues value = new ContentValues();
                        value.put("homework",false);
                        DataSupport.updateAll(Course.class,value,"name = ?",homework.getCourse());
                    }

                    //将该做业颜色设置成灰色
                    holder.course.setBackgroundColor(0xFFABABAB);
                }
                else {

                    ContentValues values = new ContentValues();
                    values.put("finished",false);
                    DataSupport.updateAll(Homework.class,values,"time = ?",String.valueOf(homework.getTime()));

                    //如果有对应课程，将颜色改成课程颜色，否则改成主题主色
                    List<Course> courses = DataSupport.where("name = ?",homework.getCourse()).find(Course.class);
                    if (courses.size() != 0)
                        holder.course.setBackgroundColor(courses.get(0).getColor());
                    else
                        holder.course.setBackgroundColor(colorPrimary);

                    //设置对应课程有作业
                    ContentValues value = new ContentValues();
                    value.put("homework",true);
                    DataSupport.updateAll(Course.class,value,"name = ?",homework.getCourse());

                }

                //如果存在筛选，发送广播刷新列表
                if (display != 0){
                    Intent intent = new Intent("com.whuLoveStudyGroup.app.UPDATE_HOMEWORK");
                    localBroadcastManager.sendBroadcast(intent);
                }

                //发送广播刷新课程表
                Intent intent = new Intent("com.whuLoveStudyGroup.app.UPDATE_SCHEDULE");
                localBroadcastManager.sendBroadcast(intent);

                //发送广播刷新插件
                Intent intent1 = new Intent("com.whuLoveStudyGroup.app.UPDATE_WIDGET");
                context.sendBroadcast(intent1);
            }
        });

        //点击图片则调用系统图片浏览器查看大图
        holder.pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Homework homework = HomeworkList.get(position);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(homework.getPic()), "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);


            }
        });

        //长按图片弹出选项
        holder.pic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final int position = holder.getAdapterPosition();
                final Homework homework = HomeworkList.get(position);

                //设置选项对话框
                final AlertDialog.Builder builder0 = new AlertDialog.Builder(context);
                String[] operations = {"编辑","删除"};
                builder0.setItems(operations, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //选择编辑后，转到EditHomeworkActivity
                        if(which == 0){
                            Intent intent = new Intent(context,EditHomeworkActivity.class);
                            intent.putExtra("time",String.valueOf(homework.getTime()));
                            context.startActivity(intent);
                        }
                        else {//选择删除后，弹出确定对话框
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("确定要删除该项作业吗？");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //删除数据库信息
                                    DataSupport.deleteAll(Homework.class,"time = ?",String.valueOf(homework.getTime()));

                                    //查询该作业的课程（如果存在）是否还有未完成作业，无则改数据
                                    List<Homework> homeworks = DataSupport.where("course = ? and finished = ?",homework.getCourse(),"0").find(Homework.class);
                                    if (homeworks.size() == 0){
                                        ContentValues value = new ContentValues();
                                        value.put("homework",false);
                                        DataSupport.updateAll(Course.class,value,"name = ?",homework.getCourse());
                                    }

                                    //如果作业有图片则一并删除
                                    if (homework.getIsPhoto()){
                                        File outputImage = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),homework.getTime() + ".jpg");
                                        if(outputImage.exists())
                                            outputImage.delete();

                                    }

                                    //发送广播更新作业 课程表和插件
                                    Intent intent = new Intent("com.whuLoveStudyGroup.app.UPDATE_SCHEDULE");
                                    localBroadcastManager.sendBroadcast(intent);
                                    Intent intent1 = new Intent("com.whuLoveStudyGroup.app.UPDATE_WIDGET");
                                    context.sendBroadcast(intent1);
                                    Intent intent2 = new Intent("com.whuLoveStudyGroup.app.UPDATE_HOMEWORK");
                                    localBroadcastManager.sendBroadcast(intent2);
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.show();
                        }
                    }
                });
                builder0.show();
                return false;
            }
        });

        //长按作业其他地方也弹出选项，同上
        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final int position = holder.getAdapterPosition();
                final Homework homework = HomeworkList.get(position);
                final AlertDialog.Builder builder0 = new AlertDialog.Builder(context);
                String[] operations = {"编辑","删除"};
                builder0.setItems(operations, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                            Intent intent = new Intent(context,EditHomeworkActivity.class);
                            intent.putExtra("time",String.valueOf(homework.getTime()));
                            context.startActivity(intent);
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("确定要删除该项作业吗？");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DataSupport.deleteAll(Homework.class,"time = ?",String.valueOf(homework.getTime()));

                                    List<Homework> homeworks = DataSupport.where("course = ? and finished = ?",homework.getCourse(),"0").find(Homework.class);
                                    if (homeworks.size() == 0){
                                        ContentValues value = new ContentValues();
                                        value.put("homework",false);
                                        DataSupport.updateAll(Course.class,value,"name = ?",homework.getCourse());
                                    }

                                    if (homework.getIsPhoto()){
                                        File outputImage = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),homework.getTime() + ".jpg");
                                        if(outputImage.exists())
                                            outputImage.delete();

                                    }

                                    Intent intent = new Intent("com.whuLoveStudyGroup.app.UPDATE_SCHEDULE");
                                    localBroadcastManager.sendBroadcast(intent);
                                    Intent intent1 = new Intent("com.whuLoveStudyGroup.app.UPDATE_WIDGET");
                                    context.sendBroadcast(intent1);
                                    Intent intent2 = new Intent("com.whuLoveStudyGroup.app.UPDATE_HOMEWORK");
                                    localBroadcastManager.sendBroadcast(intent2);
                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            builder.show();
                        }
                    }
                });
                builder0.show();
                return false;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Homework homework = HomeworkList.get(position);

        holder.course.setText(homework.getCourse());
        holder.deadLine.setText(homework.getDeadLine());
        holder.finished.setChecked(homework.getFinished());

        //设置颜色
        if(homework.getFinished()){
            holder.course.setBackgroundColor(0xFFABABAB);
        }else {
            List<Course> courses = DataSupport.where("name = ?",homework.getCourse()).find(Course.class);
            if (courses.size() != 0){
                holder.course.setBackgroundColor(courses.get(0).getColor());
            }else {
                holder.course.setBackgroundColor(colorPrimary);
            }
        }

        //有文字的话显示，否则隐藏文字区域
        if(homework.getWord().equals(""))
            holder.work.setVisibility(View.GONE);
        else {
            holder.work.setVisibility(View.VISIBLE);
            holder.work.setText(homework.getWord());
        }

        //有图片的话显示，否则隐藏图片区域
        if(homework.getIsPhoto()){
            //String uri = homework.getPic().replaceAll(".jpg","s.jpg");
            //Glide.with(context).load(Uri.parse(homework.getPic())).into(holder.pic);
            holder.pic.setImageURI(Uri.parse(homework.getPic()));
            holder.pic.setVisibility(View.VISIBLE);
        }else holder.pic.setVisibility(View.GONE);

    }


    @Override
    public int getItemCount() {
        return HomeworkList.size();
    }

}
