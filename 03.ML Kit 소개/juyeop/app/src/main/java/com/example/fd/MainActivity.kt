package com.example.fd

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.fd.ui.theme.FDTheme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val IMG_FILE_NAME = "img_face_test.png"
    }

    private val bitmap: MutableState<Bitmap?> by lazy {
        mutableStateOf(assetsToBitmap(IMG_FILE_NAME))
    }

    private val faceDetectorOptions: FaceDetectorOptions by lazy {
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .setMinFaceSize(0.1F)
            .enableTracking()
            .build()
    }

    private val faceDetector: FaceDetector by lazy {
        FaceDetection.getClient(faceDetectorOptions)
    }

    private val inputImage: InputImage? by lazy {
        bitmap.value?.let {
            InputImage.fromBitmap(it, 0)
        }
    }

    private val onButtonClickListener: () -> Unit = {
        inputImage?.let { image ->
            faceDetector.process(image)
                .addOnSuccessListener {
                    val resultBitmap = bitmap.value?.expressResultToBitmap(it)
                    bitmap.value = resultBitmap
                }
                .addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FDTheme {
                MainScreen(
                    bitmap = bitmap.value,
                    onButtonClickListener = onButtonClickListener
                )
            }
        }
    }

    private fun Context.assetsToBitmap(fileName: String): Bitmap? {
        return try {
            assets.open(fileName).run {
                BitmapFactory.decodeStream(this)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            null
        }
    }

    private fun Bitmap.expressResultToBitmap(faces: List<Face>): Bitmap? {
        val resultBitmap = copy(config, true)
        val canvas = Canvas(resultBitmap)

        faces.map {
            val id = it.trackingId

            with(canvas) {
                drawFaceBoundingBox(it.boundingBox)
                drawLandmarks(it.allLandmarks)
                drawContours(it.allContours)
            }

            it.smilingProbability?.let {
                Toast.makeText(this@MainActivity, getString(R.string.smile_face, id), Toast.LENGTH_LONG).show()
            }
        }

        return resultBitmap
    }

    private fun Canvas.drawFaceBoundingBox(bounds: Rect) {
        Paint().apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeWidth = 4.0f
            isAntiAlias = true
            drawRect(
                bounds,
                this
            )
        }
    }

    private fun Canvas.drawLandmarks(landmarks: List<FaceLandmark>) {
        val paint = Paint().apply {
            color = Color.BLUE
            style = Paint.Style.FILL
            strokeWidth = 10.0F
            isAntiAlias = true
        }

        landmarks.map {
            drawPoint(
                it.position.x,
                it.position.y,
                paint
            )
        }
    }

    private fun Canvas.drawContours(contours: List<FaceContour>) {
        val paint = Paint().apply {
            color = Color.GREEN
            style = Paint.Style.FILL
            strokeWidth = 5.0F
            isAntiAlias = true
        }

        contours.map { contour ->
            contour.points.map {
                drawPoint(
                    it.x,
                    it.y,
                    paint
                )
            }
        }
    }
}
