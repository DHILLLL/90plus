package com.whuLoveStudyGroup.app;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AccountEditActivity extends AppCompatActivity {

    private Uri imageUri;
    private boolean hasPic = false;
    private String path = null;
    ImageView portrait;
    private static final String TAG = "dong,AEA";
    boolean choosing,changed = false;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //设置当前主题
        SharedPreferences sp = getSharedPreferences("data",MODE_PRIVATE);
        String theme = sp.getString("theme","candy");
        if (theme.equals("coffee")){
            setTheme(R.style.CoffeeT);
        }
        if (theme.equals("candy")){
            setTheme(R.style.AppThemeT);
        }
        if (theme.equals("harvest")){
            setTheme(R.style.HarvestT);
        }
        if (theme.equals("sea")){
            setTheme(R.style.SeaT);
        }
        if (theme.equals("pink")){
            setTheme(R.style.PinkT);
        }
        if (theme.equals("butterfly")){
            setTheme(R.style.ButterflyT);
        }
        if (theme.equals("forgive")){
            setTheme(R.style.ForgiveT);
        }
        if (theme.equals("sexless")){
            setTheme(R.style.SexlessT);
        }

        ActivityCollector.addActivity(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);


        Toolbar toolbar = (Toolbar) findViewById(R.id.account_edit_toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.account_edit_collapsing_toolbar);
        portrait = (ImageView) findViewById(R.id.account_edit_portrait);
        FloatingActionButton edit = (FloatingActionButton) findViewById(R.id.account_edit_finish);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        File outf = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "portrait.jpg");
        imageUri = Uri.fromFile(outf);

        collapsingToolbarLayout.setTitle("点我修改头像");
        portrait.setImageURI(imageUri);

        portrait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AccountEditActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AccountEditActivity.this, new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
                }else {
                    openAlbum();
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交修改资料
                finish();
            }
        });
    }

    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, 1);
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
            case 1:
                if(resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    crop(uri);
                }
            case 9:
                if (resultCode == RESULT_OK){
                    try{
                        Bundle extras = data.getExtras();
                        if (extras != null){
                            Bitmap temp = extras.getParcelable("data");
                            portrait.setImageBitmap(temp);
                        }

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(imageUri.getPath(),options);
                        int width = options.outWidth;
                        int intSampleSize = 1;
                        if (width > 600) {
                            intSampleSize = width / 600;
                            Log.d(TAG, "inSAmpleSize: " + intSampleSize);
                        }
                        options.inJustDecodeBounds = false;
                        options.inSampleSize = intSampleSize;
                        Bitmap smallBitmap = BitmapFactory.decodeFile(imageUri.getPath(),options);
                        Log.d(TAG, "width: " + smallBitmap.getWidth());

                        File outputImage = new File(imageUri.getPath());
                        if(outputImage.exists()){
                            outputImage.delete();
                        }
                        outputImage.createNewFile();

                        FileOutputStream out = new FileOutputStream(outputImage);
                        smallBitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
                        out.flush();out.close();

                        changed = true;
                        choosing = false;

                        if (!choosing) portrait.setImageURI(imageUri);

                    } catch (IOException e){
                        e.printStackTrace();
                    }
                    break;
                }
            default:
                break;
        }
    }

    void crop(Uri uri){

        choosing = true;
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri,"image/*");
        intent.putExtra("crop",true);
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("return-data",false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,9);



    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
