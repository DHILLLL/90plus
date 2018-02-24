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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AccountEditActivity extends AppCompatActivity {

    private Uri imageUri;
    File outfS;
    private String path = null;
    ImageView portrait;
    private static final String TAG = "dongheyou,AEA";
    boolean choosing,changed = false;
    TextView nickname,sex,grade,academy,profession,qq,signature;
    FloatingActionButton finish;
    ConnWithServer connWithServer = new ConnWithServer();
    RadioButton boy,girl,secret,phone,not;

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
        nickname = (TextView) findViewById(R.id.account_edit_nickname);
        grade = (TextView) findViewById(R.id.account_edit_grade);
        academy = (TextView) findViewById(R.id.account_edit_academy);
        profession = (TextView) findViewById(R.id.account_edit_profession);
        boy = (RadioButton) findViewById(R.id.account_edit_boy);
        girl = (RadioButton) findViewById(R.id.account_edit_girl);
        secret = (RadioButton) findViewById(R.id.account_edit_secret);
        phone = (RadioButton) findViewById(R.id.account_edit_phone);
        not = (RadioButton) findViewById(R.id.account_edit_not);
        qq = (TextView) findViewById(R.id.account_edit_qq);
        signature = (TextView) findViewById(R.id.account_edit_description);
        finish = (FloatingActionButton) findViewById(R.id.account_edit_finish);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final SharedPreferences sp1 = getSharedPreferences("account",MODE_PRIVATE);

        final File outf = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), sp1.getString("phone","") + "portrait.jpg");
        imageUri = Uri.fromFile(outf);

        outfS = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), sp1.getString("phone","") + "portraitS.jpg");

        collapsingToolbarLayout.setTitle("点我修改头像");
        switch (sp1.getInt("sex",-1)){
            case -1:
                secret.setChecked(true);
                break;
            case 0:
                girl.setChecked(true);
                break;
            case 1:
                boy.setChecked(true);
                break;
            default:
        }

        switch (sp1.getInt("public",0)){
            case 0:
                not.setChecked(true);
                break;
            case 1:
                phone.setChecked(true);
                break;
            default:
        }

        nickname.setText(sp1.getString("nickname",""));
        grade.setText(sp1.getInt("grade",0) + "");
        academy.setText(sp1.getString("academy",""));
        profession.setText(sp1.getString("profession",""));
        phone.setText(sp1.getString("phone",""));
        qq.setText(sp1.getString("qq",""));
        signature.setText(sp1.getString("signature",""));

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

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sex = -1;
                if (boy.isChecked()){
                    sex = 1;
                }else if (girl.isChecked()){
                    sex = 0;
                }else if (secret.isChecked()){
                    sex = -1;
                }
                int error = connWithServer.editUser(TextUtils.isEmpty(academy.getText())?null:academy.getText().toString(),
                        phone.isChecked()?1:0,
                        TextUtils.isEmpty(grade.getText())?null:Integer.parseInt(grade.getText().toString()),
                        sp1.getString("phone",""),
                        TextUtils.isEmpty(profession.getText())?null:profession.getText().toString(),
                        TextUtils.isEmpty(qq.getText())?null:qq.getText().toString(),
                        sex,
                        TextUtils.isEmpty(signature.getText())?null:signature.getText().toString(),
                        TextUtils.isEmpty(nickname.getText())?null:nickname.getText().toString(),
                        changed?outf:null
                        );
                Log.d(TAG, "error: " + error);

                switch (error) {
                    case 0:
                        Toast.makeText(AccountEditActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

                        User user = (User) connWithServer.getResponseData();
                        SharedPreferences.Editor editor = getSharedPreferences("account", MODE_PRIVATE).edit();
                        editor.putBoolean("login", true);
                        editor.putString("phone", user.getPhoneNumber());
                        editor.putString("nickname", user.getUsername());
                        editor.putInt("sex", user.getSex());
                        editor.putInt("grade", user.getGrade());
                        editor.putString("academy", user.getAcademy());
                        editor.putString("profession", user.getProfession());
                        editor.putString("qq", user.getQqNumber());
                        editor.putString("signature", user.getSignature());
                        editor.putString("url", user.getImageUrl());
                        editor.putInt("public", user.getIsPhoneNumberPublic());
                        editor.apply();

                        finish();
                        break;
                    case 40407:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(AccountEditActivity.this, "学院名称过长", Toast.LENGTH_SHORT).show();
                        return;
                    case 404021:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(AccountEditActivity.this, "请检查手机号是否有误", Toast.LENGTH_SHORT).show();
                        return;
                    case 404061:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(AccountEditActivity.this, "昵称过长", Toast.LENGTH_SHORT).show();
                        return;
                    case 40408:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(AccountEditActivity.this, "专业名称过长", Toast.LENGTH_SHORT).show();
                    case 40409:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(AccountEditActivity.this, "个性签名过长", Toast.LENGTH_SHORT).show();
                        return;
                    case 40405:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(AccountEditActivity.this, "QQ号格式不正确", Toast.LENGTH_SHORT).show();
                        return;
                    case 400:
                    case 402:
                    case 999:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(AccountEditActivity.this, "出现错误" + error + ",请联系我们，谢谢！", Toast.LENGTH_SHORT).show();
                        return;
                    case 499:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(AccountEditActivity.this, "服务器连接失败，请重试", Toast.LENGTH_SHORT).show();
                        return;
                    default:
                        Log.d(TAG, "sendRequest: " + error);
                        Toast.makeText(AccountEditActivity.this, "出现技术性错误" + error + ",请联系我们，谢谢！", Toast.LENGTH_SHORT).show();
                        return;

                }
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

                        File outputImage = new File(imageUri.getPath());
                        if(outputImage.exists() && outputImage.length()>0){

                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(imageUri.getPath(),options);
                            int width = options.outWidth;
                            int intSampleSize = 1;
                            if (width > 600) {
                                intSampleSize = width / 600;
                            }
                            options.inJustDecodeBounds = false;
                            options.inSampleSize = intSampleSize;
                            Bitmap bitmap = BitmapFactory.decodeFile(imageUri.getPath(),options);
                            width = bitmap.getWidth();
                            portrait.setImageBitmap(bitmap);

                            FileOutputStream out = new FileOutputStream(outputImage);
                            bitmap.compress(Bitmap.CompressFormat.JPEG,100,out);
                            out.flush();out.close();

                            if (width > 80) {
                                intSampleSize = width / 80;
                            }
                            options.inSampleSize = intSampleSize;
                            Bitmap smallBitmap = BitmapFactory.decodeFile(imageUri.getPath(),options);

                            FileOutputStream out1 = new FileOutputStream(outfS);
                            smallBitmap.compress(Bitmap.CompressFormat.JPEG,100,out1);
                            out1.flush();out1.close();

                            changed = true;

                        }


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
