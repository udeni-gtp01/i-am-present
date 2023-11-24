package lk.lnbti.iampresent.ui.compose

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.TimeUnit

/**
 * Custom image analysis class for qr code scanning.
 *
 * @param onBarcodeDetected Callback function invoked when qr codes are detected.
 */
@SuppressLint("UnsafeOptInUsageError")
class BarCodeAnalyser(
    private val onBarcodeDetected: (barcodes: List<Barcode>) -> Unit,
) : ImageAnalysis.Analyzer {
    private var lastAnalyzedTimeStamp = 0L

    /**
     * Analyzes the provided image for qr codes and invokes the callback onBarcodeDetected
     * when qr codes are detected.
     *
     * @param image The image to analyze.
     */
    override fun analyze(image: ImageProxy) {
        val currentTimestamp = System.currentTimeMillis()
        // Analyze the image only if a second has passed since the last analysis
        if (currentTimestamp - lastAnalyzedTimeStamp >= TimeUnit.SECONDS.toMillis(1)) {
            image.image?.let { imageToAnalyze ->
                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                    .build()
                val barcodeScanner = BarcodeScanning.getClient(options)
                val imageToProcess =
                    InputImage.fromMediaImage(imageToAnalyze, image.imageInfo.rotationDegrees)

                barcodeScanner.process(imageToProcess)
                    .addOnSuccessListener { barcodes ->
                        if (barcodes.isNotEmpty()) {
                            onBarcodeDetected(barcodes)
                        } else {
                            Log.d("TAG", "analyze: No barcode Scanned")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d("TAG", "BarcodeAnalyser: Something went wrong $exception")
                    }
                    .addOnCompleteListener {
                        image.close()
                    }
            }
            lastAnalyzedTimeStamp = currentTimestamp
        } else {
            // Close the image if less than a second has passed since the last analysis
            image.close()
        }
    }
}