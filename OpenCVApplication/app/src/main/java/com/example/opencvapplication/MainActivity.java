package com.example.opencvapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.Mat;
import org.opencv.android.JavaCameraView;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity  implements CvCameraViewListener2 {
    private final static String TAG = "opencv:MainActivity";
    private Mat tempMat;
    private Mat horizontalStructure;
    private Mat ellipseStructure;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch(status) {
                case LoaderCallbackInterface.SUCCESS:
                    Log.i(TAG, "Opencv Loaded successfully");
                    mOpenCvCameraView.enableView();
                    break;
                default:
                {
                    super.onManagerConnected(status);
                }
            }
        }
    };

    private JavaCameraView mOpenCvCameraView;

    /*static {
        if (!OpenCVLoader.initDebug()) {
            Log.i("opencv", "Opencv initialization failed !!");
        } else {
            Log.i("opencv", "Opencv Initialization successfull !!!");
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOpenCvCameraView = (JavaCameraView) findViewById(R.id.MainActivityCameraView);
        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView.setFocusableInTouchMode(true);
        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        tempMat = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        tempMat.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Imgproc.bilateralFilter(inputFrame.gray(), tempMat, 5, 200, 200);

        Imgproc.adaptiveThreshold(tempMat, tempMat, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, -2);

        Imgproc.erode(tempMat, tempMat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size( 1,tempMat.rows()/30)));
        Imgproc.dilate(tempMat, tempMat, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size( 1,tempMat.rows()/30)));
        return tempMat;
    }

    public void onResume() {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0, this, mLoaderCallback);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }
}
