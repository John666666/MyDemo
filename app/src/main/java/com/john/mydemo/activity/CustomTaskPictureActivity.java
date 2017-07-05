package com.john.mydemo.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.john.mydemo.R;
import com.john.mydemo.view.CameraView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CustomTaskPictureActivity extends Activity {
    private final static String TAG = "CustomTaskPictureActivity";
    private FrameLayout frameLayout;
    private CameraView cameraView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_task_picture);
        frameLayout = (FrameLayout) findViewById(R.id.camera_container);
        cameraView = new CameraView(this);
        frameLayout.addView(cameraView);

        button = (Button) findViewById(R.id.btn_shutter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                writeFile(data);
                            }
                        }).start();
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        //进入Activity1秒后自动拍照
        /*new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                button.callOnClick();
            }
        }.start();*/
    }

    @SuppressLint("LongLogTag")
    private void writeFile(byte[] data) {
        if (data == null) {
            return;
        }
        Log.i(TAG, "照片数据大小: "+data.length);
        File pictureFile = new File("/sdcard/test.jpg");
        // 将得到的照片进行270°旋转，使其竖直
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Matrix matrix = new Matrix();
        matrix.preRotate(270);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        OutputStream out = null;
        try {
            out = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            Log.i(TAG, "相片保存成功");
        } catch (Exception error) {
            Log.e(TAG, "相片保存失败");
        } finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
