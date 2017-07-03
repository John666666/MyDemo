package com.john.mydemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.john.mydemo.R;

import java.io.File;

public class TakePictureActivity extends Activity implements View.OnClickListener {

    public final static int TAKE_PICTURE_SMALL = 10;
    public final static int TAKE_PICTURE_LARGE = 11;

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        findViewById(R.id.btn_take_picture_small).setOnClickListener(this);
        findViewById(R.id.btn_take_picture_large).setOnClickListener(this);
        imageView = (ImageView) findViewById(R.id.iv_result);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_take_picture_small:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Bundle bundle = new Bundle();
                startActivityForResult(intent, TAKE_PICTURE_SMALL, bundle);
                break;
            case R.id.btn_take_picture_large:
                // Create the File where the photo should go
                File photoFile = new File("/sdcard/test.png");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE_SMALL:
                if(resultCode == RESULT_OK) {
                    Bundle extras = data.getExtras();
                    Bitmap photo = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(photo);
                }
                break;
            case TAKE_PICTURE_LARGE:

                break;
        }
    }
}
