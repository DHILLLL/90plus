package com.whuLoveStudyGroup.app;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by 635901193 on 2017/7/20.
 */

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.ViewHolder> {
    private Context context;
    private List<Function> functionList;
    private static final String TAG = "dong";

    static class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView name;
        ImageView image;

        public ViewHolder(View view){
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.func_name);
            image = (ImageView) view.findViewById(R.id.func_image);
        }
    }

    public FunctionAdapter(List<Function> functionList){
        this.functionList = functionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.function_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Intent intent;
                switch (position){
                    case 0:
                        intent = new Intent(context,ScoreActivity.class);
                        context.startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url1 = Uri.parse("http://202.114.64.162");
                        intent.setData(content_url1);
                        context.startActivity(intent);
                        break;
                    case 3:
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("请选择信息");
                        final String[] options = {"地图","校车","校园卡","校历","空调卡"};
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent;
                                switch (which){
                                    case 0:
                                        intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        Uri url = Uri.parse("https://gitee.com/makebignews/90Plus/raw/master/campus_map.jpg");
                                        intent.setData(url);
                                        context.startActivity(intent);
                                        break;
                                    case 1:
                                        intent = new Intent(context,BusActivity.class);
                                        context.startActivity(intent);
                                        break;
                                    case 2:
                                        intent = new Intent(context,CardActivity.class);
                                        context.startActivity(intent);
                                        break;
                                    case 3:
                                        intent = new Intent();
                                        intent.setAction("android.intent.action.VIEW");
                                        Uri content_url = Uri.parse("http://ugs.whu.edu.cn/xl/a2017_2018nxl.htm");
                                        intent.setData(content_url);
                                        context.startActivity(intent);
                                        break;
                                    case 4:
                                        intent = new Intent(context,AcActivity.class);
                                        context.startActivity(intent);
                                        break;
                                }
                            }
                        }).show();
                        break;
                    case 4:
                        intent = new Intent(context,LnfActivity.class);
                        context.startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(context,MovieActivity.class);
                        context.startActivity(intent);
                        break;
                    default:
                }
            }
        });

        return holder;
    }

    void openMap(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        SharedPreferences sp = context.getSharedPreferences("data",Context.MODE_PRIVATE);
        String uri = sp.getString("campus_map","failed");
        if (uri.equals("failed")){
            saveMap();
            sp = context.getSharedPreferences("data",Context.MODE_PRIVATE);
            uri = sp.getString("campus_map","failed");
        }
        intent.setDataAndType(Uri.parse(uri), "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);
        return;
    }

    void saveMap(){
        String str_uri;
        try{
            File outputImage = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),"map.jpg");
            outputImage.createNewFile();
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(),R.drawable.campus_map,opts);
            Log.d(TAG, "saveMap: " + opts.outWidth + " " + opts.outHeight);
            opts.inSampleSize = opts.outWidth / 4000;
            Log.d(TAG, "inSampleSize = " + opts.outWidth / 4000);
            opts.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.campus_map,opts);
            FileOutputStream out = new FileOutputStream(outputImage);
            bitmap.compress(Bitmap.CompressFormat.JPEG,10,out);
            out.flush();out.close();
            Uri uri;
            if (Build.VERSION.SDK_INT >= 24) {
                uri = FileProvider.getUriForFile(context,"com.whuLoveStudyGroup.app.fileprovider",outputImage);
            }else {
                uri = Uri.fromFile(outputImage);
            }
            str_uri = uri.toString();

        } catch (IOException e){
            e.printStackTrace();
            str_uri = "failed";
        }


        SharedPreferences.Editor editor = context.getSharedPreferences("data",Context.MODE_MULTI_PROCESS).edit();
        editor.putString("campus_map",str_uri);
        editor.apply();

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Function function = functionList.get(position);

        holder.name.setText(function.getName());
        Glide.with(context).load(function.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return functionList.size();
    }
}
