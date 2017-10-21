package com.whuLoveStudyGroup.app;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by 635901193 on 2017/7/26.
 */

//除了一开始需要显示原本的信息和图片外，其余和AddCourseActivity类似

public class EditHomeworkActivity extends MyActivity {
    private EditText course,deadline,word;
    private Button selectCourse,selectDeadline,addPic,delPic;
    private ImageView image;
    private Uri imageUri;
    private boolean hasPic,finished;
    private String path = null;


    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;

    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_homework);

        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_homework_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        course = (EditText) findViewById(R.id.edit_homework_course);
        deadline = (EditText) findViewById(R.id.edit_homework_deadline);
        word = (EditText) findViewById(R.id.edit_homework_word);
        selectCourse = (Button) findViewById(R.id.edit_homework_course_select);
        selectDeadline = (Button) findViewById(R.id.edit_homework_deadline_select);
        addPic = (Button) findViewById(R.id.edit_homework_pic);
        delPic = (Button) findViewById(R.id.edit_homework_delete);
        image = (ImageView) findViewById(R.id.edit_homework_image);

        Intent intent = getIntent();
        id = intent.getStringExtra("time");
        List<Homework> homeworks = DataSupport.where("time = ?",id).find(Homework.class);
        hasPic = homeworks.get(0).getIsPhoto();
        course.setText(homeworks.get(0).getCourse());
        deadline.setText(homeworks.get(0).getDeadLine());
        word.setText(homeworks.get(0).getWord());
        finished = homeworks.get(0).getFinished();
        if(hasPic) imageUri = Uri.parse(homeworks.get(0).getPic());


        if(homeworks.get(0).getIsPhoto()){
            Glide.with(EditHomeworkActivity.this).load(Uri.parse(
                    homeworks.get(0).getPic())).into(image);
            image.setVisibility(View.VISIBLE);
            addPic.setVisibility(View.GONE);
            delPic.setVisibility(View.VISIBLE);
            hasPic = true;
        }

        List<Course> courses = DataSupport.findAll(Course.class);

        List<String> Names = new ArrayList<>();
        for(Course course:courses){
            boolean repeat = false;
            for (int i = 0;i<Names.size();i++){
                if(Names.get(i).equals(course.getName())) {
                    repeat = true;break;
                }
            }
            if (!repeat) Names.add(course.getName());
        }

        final String[] names = new String[Names.size()];
        for(int i = 0;i<Names.size();i++){
            names[i] = Names.get(i);
        }

        selectCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(EditHomeworkActivity.this);
                builder.setTitle("请选择课程");
                builder.setItems(names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            course.setText(names[which]);
                            course.setSelection(names[which].length());
                    }
                });
                builder.show();
            }
        });

        selectDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditHomeworkActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        deadline.setText(year + "." + (month+1) + "." + dayOfMonth + "(" + weekday(year,month,dayOfMonth) + ")");
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditHomeworkActivity.this);
                final String[] way = {"拍照","从相册选择"};
                builder.setItems(way, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                File outputImage = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),id + ".jpg");
                                path = outputImage.getPath();
                                try{
                                    if(outputImage.exists()){
                                        outputImage.delete();
                                    }
                                    outputImage.createNewFile();
                                } catch (IOException e){
                                    e.printStackTrace();
                                }
                                if (Build.VERSION.SDK_INT >= 24) {
                                    imageUri = FileProvider.getUriForFile(EditHomeworkActivity.this,"com.whuLoveStudyGroup.app.fileprovider",outputImage);
                                }else {
                                    imageUri = Uri.fromFile(outputImage);
                                }
                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                                startActivityForResult(intent,TAKE_PHOTO);
                                break;

                            case 1:
                                if (ContextCompat.checkSelfPermission(EditHomeworkActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(EditHomeworkActivity.this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
                                }else {
                                    openAlbum();
                                }

                            default:
                        }
                    }
                });
                builder.show();
            }
        });

        delPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPic.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
                delPic.setVisibility(View.GONE);
                hasPic = false;

                File outputImage = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),id + ".jpg");
                if(outputImage.exists())
                    outputImage.delete();

                outputImage = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),id + ".jpg");
                if(outputImage.exists())
                    outputImage.delete();
            }
        });

    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    hasPic = true;
                    addPic.setVisibility(View.GONE);
                    image.setVisibility(View.VISIBLE);
                    delPic.setVisibility(View.VISIBLE);
                    try {
                        Bitmap bitmap = BitmapFactory.decodeFile(path);

                        int digree = 0;
                        ExifInterface exifInterface = null;
                        try{
                            exifInterface = new ExifInterface(path);
                        }catch (IOException e){
                            e.printStackTrace();
                            exifInterface = null;
                        }

                        if (exifInterface != null){
                            int ori = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                            switch (ori){
                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    digree = 90;
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    digree = 180;
                                    break;
                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    digree = 270;
                                    break;
                                default:
                                    digree = 0;
                                    break;
                            }

                            if (digree != 0){
                                Matrix m = new Matrix();
                                m.postRotate(digree);
                                bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),m,true);
                            }
                        }
                        image.setImageBitmap(bitmap);

                        File outputImage = new File(path);
                        if(outputImage.exists()){
                            outputImage.delete();
                        }
                        outputImage.createNewFile();

                        FileOutputStream out = new FileOutputStream(outputImage);
                        bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
                        out.flush();out.close();

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(path,options);
                        int width = options.outWidth;
                        int height = options.outHeight;
                        int max = (width > height) ? width : height;
                        int intSampleSize = 1;
                        if (max > 1000) {
                            float ratio = (float) max / 1000.0f;
                            intSampleSize = (int) ratio;
                        }
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = intSampleSize;
                        Bitmap smallBitmap = BitmapFactory.decodeFile(path,options);

                        outputImage = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), id + ".jpg");
                        if(outputImage.exists()){
                            outputImage.delete();
                        }
                        outputImage.createNewFile();

                        out = new FileOutputStream(outputImage);
                        smallBitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
                        out.flush();out.close();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case CHOOSE_PHOTO:
                if(resultCode == RESULT_OK){
                    addPic.setVisibility(View.GONE);
                    image.setVisibility(View.VISIBLE);
                    delPic.setVisibility(View.VISIBLE);
                    hasPic = true;
                    if(Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data);
                    }else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        displayImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath){
        if (imagePath != null){
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

            int digree = 0;
            ExifInterface exifInterface = null;
            try{
                exifInterface = new ExifInterface(imagePath);
            }catch (IOException e){
                e.printStackTrace();
                exifInterface = null;
            }
            if (exifInterface != null){
                int ori = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                switch (ori){
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                    default:
                        digree = 0;
                        break;
                }
                if (digree != 0){
                    Matrix m = new Matrix();
                    m.postRotate(digree);
                    bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),m,true);
                }

            }

            image.setImageBitmap(bitmap);

            File outputImage = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), id + ".jpg");
            final String temp = outputImage.getPath();
            try{
                if(outputImage.exists()){
                    outputImage.delete();
                }
                outputImage.createNewFile();

                FileOutputStream out = new FileOutputStream(outputImage);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
                out.flush();out.close();

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(temp,options);
                int width = options.outWidth;
                int height = options.outHeight;
                int max = (width > height) ? width : height;
                int intSampleSize = 1;
                if (max > 1000) {
                    float ratio = (float) max / 1000.0f;
                    intSampleSize = (int) ratio;
                }
                options.inJustDecodeBounds = false;
                options.inSampleSize = intSampleSize;
                Bitmap smallBitmap = BitmapFactory.decodeFile(temp,options);

                File outputImage1 = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), id + ".jpg");
                if(outputImage1.exists()){
                    outputImage1.delete();
                }
                outputImage1.createNewFile();

                out = new FileOutputStream(outputImage1);
                smallBitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
                out.flush();out.close();

            } catch (IOException e){
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= 24) {
                imageUri = FileProvider.getUriForFile(EditHomeworkActivity.this,"com.whuLoveStudyGroup.app.fileprovider",outputImage);
            }else {
                imageUri = Uri.fromFile(outputImage);
            }


        }else {
            Toast.makeText(this,"fail",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_homework_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_homework_commit:
                editHomework();
                finish();
                break;
            default:
        }
        return true;
    }

    private void editHomework(){


        DataSupport.deleteAll(Homework.class,"time = ?",String.valueOf(id));
        Homework homework = new Homework();

        homework.setFinished(finished);
        ContentValues value = new ContentValues();
        value.put("homework",true);
        DataSupport.updateAll(Course.class,value,"name = ?",course.getText().toString());

        homework.setWord(word.getText().toString());
        homework.setDeadLine(deadline.getText().toString());
        homework.setIsPhoto(hasPic?true:false);
        homework.setCourse(course.getText().toString());
        homework.setTime(Long.valueOf(id));
        if(hasPic) homework.setPic(imageUri.toString());
        homework.save();

        Intent intent1 = new Intent("com.whuLoveStudyGroup.app.UPDATE_WIDGET");
        sendBroadcast(intent1);
    }

    private String weekday(int year,int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,day);

        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case 1:return "周日";
            case 2:return "周一";
            case 3:return "周二";
            case 4:return "周三";
            case 5:return "周四";
            case 6:return "周五";
            case 7:return "周六";
        }

        return "";
    }

}
