package com.dongzi.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dongzi.camera.R;
import com.atech.glcamera.interfaces.FileCallback;
import com.atech.glcamera.interfaces.FilteredBitmapCallback;
import com.atech.glcamera.utils.FileUtils;
import com.atech.glcamera.utils.FilterFactory;
import com.atech.glcamera.views.GLCameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GLCameraActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imgSwitch;
    ImageView imgMode;
    ImageView imgFilter;
    ImageView imgCapter;
    GLCameraView mCameraView;
    RecyclerView rv;

    private boolean mRecordingEnabled = false;  // controls button state
    private int mode = 1;     //1.capture 2.record
    private List<FilterFactory.FilterType>filters = new ArrayList<>();
    private List<FilterInfo>infos = new ArrayList<>();
    private boolean isShowing = false;

    private boolean isDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glcamera);

        imgCapter = findViewById(R.id.img_capture);
        imgMode = findViewById(R.id.img_mode);
        imgSwitch = findViewById(R.id.img_switch);
        imgFilter = findViewById(R.id.img_filter);
        mCameraView = findViewById(R.id.glcamera);
        rv = findViewById(R.id.rv);



        imgMode.setOnClickListener(this);
        imgSwitch.setOnClickListener(this);
        imgFilter.setOnClickListener(this);
        imgCapter.setOnClickListener(this);

        initFilters();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        FilterAdapter adapter = new FilterAdapter(this,infos);
        rv.setAdapter(adapter);
        rv.setLayoutManager(linearLayoutManager);

        adapter.setFilterSeletedListener(new SelectedListener() {
            @Override
            public void onFilterSelected(int pos) {

                mCameraView.updateFilter(filters.get(pos));

            }
        });




        mCameraView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShowing){
                    rv.setVisibility(View.INVISIBLE);
                    isShowing = false;
                }

            }
        });

        mCameraView.enableBeauty(true);
        //set your own output file here
        // mCameraView.setOuputMP4File();
        //set record finish listener
        mCameraView.setrecordFinishedListnener(new FileCallback() {
            @Override
            public void onData(File file) {

                //update the gallery
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(file)));

            }
        });





    }

    private void initFilters(){


        filters.add(FilterFactory.FilterType.Original);
        filters.add(FilterFactory.FilterType.Sunrise);
        filters.add(FilterFactory.FilterType.Sunset);
        filters.add(FilterFactory.FilterType.BlackWhite);
        filters.add(FilterFactory.FilterType.WhiteCat);
        filters.add(FilterFactory.FilterType.BlackCat);
        filters.add(FilterFactory.FilterType.SkinWhiten);
        filters.add(FilterFactory.FilterType.Healthy);
        filters.add(FilterFactory.FilterType.Sakura);
        filters.add(FilterFactory.FilterType.Romance);
        filters.add(FilterFactory.FilterType.Latte);
        filters.add(FilterFactory.FilterType.Warm);
        filters.add(FilterFactory.FilterType.Calm);
        filters.add(FilterFactory.FilterType.Cool);
        filters.add(FilterFactory.FilterType.Brooklyn);
        filters.add(FilterFactory.FilterType.Sweets);
        filters.add(FilterFactory.FilterType.Amaro);
        filters.add(FilterFactory.FilterType.Antique);
        filters.add(FilterFactory.FilterType.Brannan);


        infos.add(new FilterInfo(R.drawable.filter_thumb_original,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_sunrise,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_sunset,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_1977,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_whitecat,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_blackcat,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_beauty,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_healthy,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_sakura,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_romance,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_latte,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_warm,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_calm,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_cool,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_brooklyn,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_sweets,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_amoro,"Amaro"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_antique,"??????"));
        infos.add(new FilterInfo(R.drawable.filter_thumb_brannan,"Brannan"));


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.img_switch:


                mCameraView.switchCamera();

                break;

            case R.id.img_mode:

                if (mode==1){
                    mode=2;
                    Toast.makeText(GLCameraActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();

                }else {
                    mode = 1;
                    Toast.makeText(GLCameraActivity.this,"??????????????????",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.img_filter:


                if (isShowing){

                    rv.setVisibility(View.INVISIBLE);

                }else {

                    rv.setVisibility(View.VISIBLE);
                }
                isShowing = !isShowing;

                break;

            case R.id.img_capture:

                if (mode==1){

                    onCapture();

                }else {

                    onRecord();
                }

                break;

        }


    }

    private void onCapture(){

        mCameraView.takePicture(new FilteredBitmapCallback() {
            @Override
            public void onData(Bitmap bitmap) {

                File file = FileUtils.createImageFile();
                //??????????????????
                try {
                    // ????????????
                    FileOutputStream fos;
                    fos = new FileOutputStream(file);
                    //??????jpg
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    bitmap.recycle();

                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(file)));

                    Toast.makeText(GLCameraActivity.this,"finished",Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });


    }


    @Override
    protected void onStop() {
        super.onStop();

        mRecordingEnabled = false;
        mCameraView.changeRecordingState(mRecordingEnabled);

    }

    private void onRecord(){

       mRecordingEnabled = !mRecordingEnabled;

       mCameraView.changeRecordingState(mRecordingEnabled);


       if (mRecordingEnabled){

           Toast.makeText(GLCameraActivity.this,"start",Toast.LENGTH_SHORT).show();

       }else{

           Toast.makeText(GLCameraActivity.this,"finished",Toast.LENGTH_SHORT).show();

       }

   }

}
