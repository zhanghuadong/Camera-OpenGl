package com.dongzi.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.atech.glcamera.camera.CameraCore;
import com.atech.glcamera.interfaces.FileCallback;
import com.atech.glcamera.interfaces.ImageCallback;
import com.atech.glcamera.utils.FileUtils;
import com.atech.glcamera.views.CameraView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    Button btncapture;
    ImageView imgSwitch;
    Button btnRecord;
    CameraView mCameraView;

    boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btncapture = findViewById(R.id.btn_capture);
        imgSwitch = findViewById(R.id.img_switch);
        btnRecord = findViewById(R.id.btn_record);
        mCameraView = findViewById(R.id.glcamera);


        btncapture.setOnClickListener(this);
        imgSwitch.setOnClickListener(this);
        btnRecord.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_capture:

                onCapture();
                break;

            case R.id.btn_record:

                onRecord();

                break;

            case R.id.img_switch:

                mCameraView.switchCamera();



                break;
        }

    }

    private void onCapture() {


        mCameraView.takePicture(new ImageCallback() {
            @Override
            public void onData(final byte[] bytes) {


                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        //????????????

                        File newfile = FileUtils.createImageFile();

                        FileOutputStream fos;
                        try {
                            fos = new FileOutputStream(newfile);
                            fos.write(bytes);
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //??????????????????????????????????????????????????????????????????????????????
                        rotateImageView(CameraCore.mCameraId, CameraCore.mOrientation, newfile.getAbsolutePath());
                    }
                }).start();

            }

        });

    }

    /**
     * ????????????
     *
     * @param cameraId    ??????????????????
     * @param orientation ????????????????????????
     * @param path        ????????????
     */
    private void rotateImageView(int cameraId, int orientation, String path) {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        Matrix matrix = new Matrix();
        //0?????????
        if (cameraId == 0) {
            if (orientation == 90) {
                matrix.postRotate(90);
            }
        }
        //1?????????
        if (cameraId == 1) {
            //???????????????270???
            matrix.postRotate(270);
        }
        // ??????????????????
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        File file = new File(path);
        //??????????????????
        try {
            // ????????????
            FileOutputStream fos;
            fos = new FileOutputStream(file);
            //??????jpg
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            resizedBitmap.recycle();

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(file)));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ??????
     */
    public void onRecord() {

        if (isRecording) {

            mCameraView.stopRecord(new FileCallback() {
                @Override
                public void onData(File file) {

                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.fromFile(file)));
                    Toast.makeText(CameraActivity.this, "record finished", Toast.LENGTH_SHORT).show();

                }
            });

            btnRecord.setText("????????????");

        } else {

            mCameraView.startRecord();
            btnRecord.setText("????????????");

        }

        isRecording = !isRecording;

    }
}
