package com.mariofronza.face_collection_app.ui.photos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.mariofronza.face_collection_app.R;
import com.mariofronza.face_collection_app.repositories.PhotosRepository;
import com.mariofronza.face_collection_app.utils.SessionManager;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvException;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class HandlePhotoActivity extends CameraActivity implements CvCameraViewListener2, View.OnClickListener {

    private Mat mRgba;
    private Mat mGray;
    private Button takePictureButton;
    private TextView tvPhotoType;
    private boolean isButtonEnable = false;

    private CascadeClassifier mJavaDetector;
    private CameraBridgeViewBase mOpenCvCameraView;

    private Rect currentReact;
    private int photoId;
    private String photoType;

    private SessionManager sessionManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_handle_photo);

        Intent intent = getIntent();
        photoId = intent.getExtras().getInt("photoId");
        photoType = intent.getExtras().getString("photoType");

        sessionManager = new SessionManager(this);

        tvPhotoType = findViewById(R.id.tvPhotoType);

        takePictureButton = findViewById(R.id.btnTakePicture);
        takePictureButton.setEnabled(isButtonEnable);
        takePictureButton.setOnClickListener(this);

        mOpenCvCameraView = findViewById(R.id.fd_activity_surface_view);
        mOpenCvCameraView.setVisibility(CameraBridgeViewBase.VISIBLE);
        mOpenCvCameraView.setCameraIndex(0);
        mOpenCvCameraView.setCvCameraViewListener(this);

    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mGray = new Mat();
        mRgba = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        mGray = new Mat();
        mRgba = new Mat();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();

        MatOfRect faces = new MatOfRect();

        if (mJavaDetector != null) {
            mJavaDetector.detectMultiScale(mGray, faces, 1.1, 2, 2,
                    new Size(450, 450), new Size());
        }

        Rect[] facesArray = faces.toArray();
        isButtonEnable = facesArray.length != 0;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                takePictureButton.setEnabled(isButtonEnable);
            }
        });


        for (Rect rect : facesArray) {
            currentReact = rect;
            Imgproc.rectangle(mRgba, rect.br(), rect.tl(), new Scalar(0, 255, 0, 255), 3);
        }

        return mRgba;
    }


    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null) {
            mOpenCvCameraView.disableView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mOpenCvCameraView.disableView();
    }


    @Override
    public void onClick(View v) {
        goToConfirmActivity();
    }

    private void goToConfirmActivity() {
        String token = sessionManager.fetchAuthToken();
        Bitmap bitmap = convertMatToBitMap(mGray);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        Intent intent = new Intent(this, ConfirmPhotoActivity.class);
        intent.putExtra("token", token);
        intent.putExtra("photoId", photoId);
        intent.putExtra("photoType", photoType);
        intent.putExtra("bitmap", byteArray);
        startActivity(intent);
    }


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            if (status == LoaderCallbackInterface.SUCCESS) {
                System.loadLibrary("detection_based_tracker");

                try {
                    InputStream is = getResources().openRawResource(R.raw.lbpcascade_frontalface);
                    File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
                    File mCascadeFile = new File(cascadeDir, "lbpcascade_frontalface.xml");
                    FileOutputStream os = new FileOutputStream(mCascadeFile);

                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                    is.close();
                    os.close();

                    mJavaDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
                    if (mJavaDetector.empty()) {
                        mJavaDetector = null;
                    }

                    cascadeDir.delete();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                mOpenCvCameraView.setCameraIndex(1);
                mOpenCvCameraView.enableView();
            } else {
                super.onManagerConnected(status);
            }
        }
    };


    private Bitmap convertMatToBitMap(Mat input) {
        Bitmap bmp = null;
        try {
            Mat cropped = new Mat(input, currentReact);
            bmp = Bitmap.createBitmap(cropped.cols(), cropped.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(cropped, bmp);
        } catch (CvException e) {
            Log.d("Exception", e.getMessage());
        }
        return bmp;
    }


}