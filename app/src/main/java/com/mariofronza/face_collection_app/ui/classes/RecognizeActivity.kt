package com.mariofronza.face_collection_app.ui.classes

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mariofronza.face_collection_app.R
import com.mariofronza.face_collection_app.api.ApiService
import com.mariofronza.face_collection_app.repositories.PhotosRepository
import com.mariofronza.face_collection_app.ui.photos.PhotosViewModel
import com.mariofronza.face_collection_app.ui.photos.PhotosViewModelFactory
import com.mariofronza.face_collection_app.utils.SessionManager
import kotlinx.android.synthetic.main.activity_recognize_student.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.opencv.android.*
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.*
import kotlin.concurrent.thread

class RecognizeActivity : CameraActivity(), CvCameraViewListener2 {
    lateinit var mRgba: Mat
    lateinit var mGray: Mat

    private var mJavaDetector: CascadeClassifier? = null
    private var classId = 0

    private var sessionManager: SessionManager? = null
    private lateinit var photosViewModel: PhotosViewModel
    private lateinit var photosViewModelFactory: PhotosViewModelFactory

    private var recognizeLoading = false

    lateinit var currentReact: Rect
    private var cameraIndex: Int = 1
    private var totalFaces: Int = 0
    private var readyToRecognize: Boolean = true
    private var hasNoStudent = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_recognize_student)
        val intent = intent
        classId = intent.extras!!.getInt("classId")
        sessionManager = SessionManager(this)

        val api = ApiService()
        val photosRepository = PhotosRepository(api)

        photosViewModelFactory = PhotosViewModelFactory(photosRepository)
        photosViewModel =
            ViewModelProvider(this, photosViewModelFactory).get(PhotosViewModel::class.java)

        sessionManager = SessionManager(this)

        photosViewModel.recognizeResponse.observe(this, Observer { sessionResponse ->
            clRecognize.visibility = View.VISIBLE
            tvRecognizeId.text = sessionResponse.student.id.toString()
            tvRecognizeName.text = sessionResponse.student.user.name
            pbRecognize.visibility = View.GONE
            recognizeLoading = false
            readyToRecognize = totalFaces == 0
        })

        photosViewModel.error.observe(this, Observer { message ->
            tvRecognizeError.visibility = View.VISIBLE
            tvRecognizeError.text = message
            pbRecognize.visibility = View.GONE
            recognizeLoading = false
            readyToRecognize = totalFaces == 0
        })

        cvRecognize.visibility = CameraBridgeViewBase.VISIBLE
        cvRecognize.setCameraIndex(cameraIndex)
        cvRecognize.setCvCameraViewListener(this)

        switchCam.setOnClickListener {
            cameraIndex = if (cameraIndex == 1) {
                0
            } else {
                1
            }
            runOnUiThread {
                cvRecognize.disableView();
                cvRecognize.setCameraIndex(cameraIndex);
                cvRecognize.enableView();
            }
        }


    }

    private val mLoaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            if (status == SUCCESS) {
                System.loadLibrary("detection_based_tracker")
                try {
                    val `is` = resources.openRawResource(R.raw.lbpcascade_frontalface)
                    val cascadeDir = getDir("cascade", Context.MODE_PRIVATE)
                    val mCascadeFile = File(cascadeDir, "lbpcascade_frontalface.xml")
                    val os = FileOutputStream(mCascadeFile)
                    val buffer = ByteArray(4096)
                    var bytesRead: Int
                    while (`is`.read(buffer).also { bytesRead = it } != -1) {
                        os.write(buffer, 0, bytesRead)
                    }
                    `is`.close()
                    os.close()
                    mJavaDetector = CascadeClassifier(mCascadeFile.absolutePath)
                    if (mJavaDetector!!.empty()) {
                        mJavaDetector = null
                    }
                    cascadeDir.delete()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                cvRecognize!!.enableView()
            } else {
                super.onManagerConnected(status)
            }
        }
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        mGray = Mat()
        mRgba = Mat()
    }

    override fun onCameraViewStopped() {
        mGray = Mat()
        mRgba = Mat()
    }

    override fun onCameraFrame(inputFrame: CvCameraViewFrame): Mat {
        val faces = MatOfRect()
        mRgba = inputFrame.rgba()
        mGray = inputFrame.gray()

        val rotImage = Imgproc.getRotationMatrix2D(
            Point(
                (mRgba.cols().div(2)).toDouble(),
                (mRgba.rows().div(2)).toDouble()
            ), 90.0, 1.0
        )
        Imgproc.warpAffine(mRgba, mRgba, rotImage, mRgba.size())
        Imgproc.warpAffine(mGray, mGray, rotImage, mRgba.size())

        val flipCode = if (cameraIndex == 1) {
            1
        } else {
            0
        }

        Core.flip(mRgba, mRgba, flipCode)
        Core.flip(mGray, mGray, flipCode)


        mJavaDetector!!.detectMultiScale(
            mGray, faces, 1.05, 5, 2,
            Size(400.0, 400.0)
        )
        val facesArray = faces.toArray()
        totalFaces = facesArray.size

        if (facesArray.isEmpty()) {
            readyToRecognize = true
        }

        for (rect in facesArray) {
            Imgproc.rectangle(mRgba, rect.br(), rect.tl(), Scalar(0.0, 255.0, 0.0, 255.0), 3)
            currentReact = rect
        }

        runOnUiThread {
            if (facesArray.isNotEmpty() && !recognizeLoading && readyToRecognize) {
                pbRecognize.visibility = View.VISIBLE
                clRecognize.visibility = View.GONE
                tvRecognizeError.visibility = View.GONE
            }
        }

        thread() {
            if (facesArray.isNotEmpty() && !recognizeLoading && readyToRecognize) {
                recognizeLoading = true
                photosViewModel.recognize(
                    sessionManager?.fetchAuthToken()!!,
                    classId,
                    preparePhoto(mGray)
                )
            }
        }

        return mRgba
    }

    private fun preparePhoto(mat: Mat): MultipartBody.Part {
        val bitmap = convertMatToBitMap(mat)

        val stream = ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        val finalBitMap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

        val file = File(cacheDir, "student-photo");
        convertFileToBitmap(file, finalBitMap);

        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        return MultipartBody.Part.createFormData(
            "file",
            file.name + ".jpg", requestFile
        )
    }

    private fun convertFileToBitmap(file: File, bitmap: Bitmap) {
        file.createNewFile()
        val os = BufferedOutputStream(FileOutputStream(file))
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, os)
        os.close()
    }

    private fun convertMatToBitMap(input: Mat): Bitmap? {
        var bmp: Bitmap? = null
        try {
            val cropped = Mat(input, currentReact)
            bmp = Bitmap.createBitmap(cropped.cols(), cropped.rows(), Bitmap.Config.ARGB_8888)
            Utils.matToBitmap(cropped, bmp)
        } catch (e: CvException) {
            Log.d("Exception", e.message!!)
        }
        return bmp
    }

    override fun getCameraViewList(): List<CameraBridgeViewBase?> {
        return listOf(cvRecognize)
    }

    public override fun onPause() {
        super.onPause()
        if (cvRecognize != null) {
            cvRecognize!!.disableView()
        }
    }

    public override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback)
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        cvRecognize!!.disableView()
    }
}