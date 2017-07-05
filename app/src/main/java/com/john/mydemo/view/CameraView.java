package com.john.mydemo.view;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private final static String TAG = "CameraPreview";
    private SurfaceHolder mHolder;
    private Camera mCamera;

    private static CameraView mCameraView;

    /**
     * 拍照
     */
    public void takePicture(Camera.ShutterCallback shutterCallback, Camera.PictureCallback rawPictureCallback,
                            Camera.PictureCallback pictureCallback) {
        if(mCamera == null) {
            return;
        }
        mCamera.takePicture(shutterCallback, rawPictureCallback, pictureCallback);
    }

    public CameraView(Context context) {
        super(context);

        //获取摄像头
        int num = Camera.getNumberOfCameras();
        if(num < 1) {
            Log.e(TAG, "无法获取到摄像头");
            return;
        }
        Log.i(TAG, "找到"+num+"个摄像头");
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        try {
            if(num < 2) {
                Camera.getCameraInfo(0, cameraInfo);
                mCamera = Camera.open(0);
            } else {
                for(int i = 0; i < num; i++) {
                    releaseCamera();
                    Camera.getCameraInfo(i, cameraInfo);
                    if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                        mCamera = Camera.open(i);
                        break;
                    }
                    mCamera = Camera.open(i);
                }
            }
            if(cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Log.i(TAG, "打开后置摄像头");
            } else {
                Log.i(TAG, "打开前置摄像头");
            }
        } catch (Exception e) {
            Log.e(TAG, "打开摄像头失败", e);
            releaseCamera();
        }
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //设置拍摄参数
        setCameraParams(cameraInfo);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        Log.i(TAG, "释放摄像头资源");
        releaseCamera();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
          // preview surface does not exist
          return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    private void releaseCamera() {
        if(mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private void setCameraParams(Camera.CameraInfo cameraInfo) {
        if(mCamera == null) return;
        Camera.Parameters params = mCamera.getParameters();
//        params.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO); //自动对焦

        Log.d(TAG, "设置预览大小");
        List<Camera.Size> supportedPreviewSizes = params.getSupportedPreviewSizes();
        Camera.Size previewSize = adjustSize(supportedPreviewSizes, 1024);
        params.setPreviewSize(previewSize.width, previewSize.height);
        mCamera.setDisplayOrientation(90);


        Log.d(TAG, "设置图片大小");
        List<Camera.Size> supportedPictureSizes = params.getSupportedPictureSizes();
        Camera.Size pictureSize = adjustSize(supportedPictureSizes, 1080);
        params.setPictureSize(pictureSize.width, pictureSize.height);
        params.setJpegQuality(100); //设置照片质量
        try {
            mCamera.setParameters(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Camera.Size adjustSize(List<Camera.Size> supportSizeList, int assignWidth) {
        if(supportSizeList == null) return null;
        int absDiff = 0;
        Camera.Size size = null;
        for(Camera.Size supportSize : supportSizeList) {
            Log.d(TAG, "支持的size: "+supportSize.width +", "+supportSize.height);
            int tempAbsDiff = Math.abs(supportSize.width - assignWidth);
            if(absDiff == 0) {
                size = supportSize;
                absDiff = tempAbsDiff;
                continue;
            }
            if(tempAbsDiff < absDiff) {
                size = supportSize;
                absDiff = tempAbsDiff;
                continue;
            }
        }
        Log.d(TAG, "最终选出的最佳大小: "+size.width+","+size.height);
        return size;
    }
}