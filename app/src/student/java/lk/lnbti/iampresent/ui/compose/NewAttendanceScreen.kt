package lk.lnbti.iampresent.ui.compose

import LoadingScreen
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.common.util.concurrent.ListenableFuture
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.constant.Constant
import lk.lnbti.iampresent.data.Result
import lk.lnbti.iampresent.view_model.NewAttendanceViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Composable function for displaying the NewAttendanceScreen.
 *
 * @param newAttendanceViewModel ViewModel for managing new attendance.
 * @param onYesButtonClicked Callback when 'Yes' button is clicked.
 * @param onRetryButtonClicked Callback when 'Retry' button is clicked.
 * @param modifier Additional modifier for styling.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NewAttendanceScreen(
    newAttendanceViewModel: NewAttendanceViewModel = hiltViewModel(),
    onYesButtonClicked: () -> Unit,
    onRetryButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val qrData: String? by newAttendanceViewModel.qrData.observeAsState(null)
    val saveAttendanceResult by newAttendanceViewModel.saveAttendanceResult.observeAsState(null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = R.string.join_lecture,
                description = R.string.join_lecture_description,
                modifier = modifier
            )
        },

        ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (saveAttendanceResult) {
                    is Result.Loading -> {
                        // Handle loading state
                        LoadingScreen()
                    }

                    is Result.Success<*> -> {
                        // Handle success state
                        newAttendanceViewModel.resetSaveAttendanceResult()
                        onYesButtonClicked()
                    }

                    is Result.Error -> {
                        // Handle error state
                        val errorMessage = (saveAttendanceResult as Result.Error).message
                        ErrorScreen(
                            errorMessage = errorMessage,
                            onRetry = {
                                newAttendanceViewModel.resetSaveAttendanceResult()
                                onRetryButtonClicked()
                            })
                    }

                    else -> {
                        Spacer(modifier = Modifier.height(10.dp))
                        val cameraPermissionState =
                            rememberPermissionState(permission = Constant.PERMISSION_CAMERA)

                        when {
                            cameraPermissionState.status.isGranted -> {
                                // Camera permission is granted, show the camera preview
                                CameraPreview(
                                    qrData = qrData,
                                    isValidQr = { qrData -> newAttendanceViewModel.isValidQr(qrData) },
                                    onQrScanned = { newAttendanceViewModel.onQrDataChange() },
                                    onYesButtonClicked = {
                                        newAttendanceViewModel.saveAttendance()
                                    }
                                )
                            }

                            cameraPermissionState.status.shouldShowRationale -> {
                                // Permission is denied, but a rationale should be shown
                                Text("Please grant camera permission in app settings.")
                            }

                            else -> {
                                // Permission is denied, show the permission request button
                                Button(
                                    onClick = {
                                        cameraPermissionState.launchPermissionRequest()
                                    }
                                ) {
                                    Text(text = "Request Camera Permission")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

/**
 * Composable function for the camera preview.
 *
 * @param qrData The QR code data.
 * @param onQrScanned Callback when a QR code is scanned.
 * @param isValidQr Callback to check if the scanned QR code is valid.
 * @param onYesButtonClicked Callback when 'Yes' button is clicked.
 */
@Composable
fun CameraPreview(
    qrData: String?,
    onQrScanned: () -> Unit,
    isValidQr: (String) -> Boolean,
    onYesButtonClicked: () -> Unit
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var cameraProviderShare: ProcessCameraProvider? by remember { mutableStateOf(null) }
    var preview by remember { mutableStateOf<Preview?>(null) }
    var showSuccessDialog by rememberSaveable { mutableStateOf(false) } // State variable to control the dialog
    var showErrorDialog by rememberSaveable { mutableStateOf(false) } // State variable to control the dialog

    if (showSuccessDialog) {
        ConfirmationDialog(
            qrData = qrData,
            onConfirm = {
                cameraProviderShare?.unbindAll()
                onYesButtonClicked() // Save attendance
                showSuccessDialog = false // Close the dialog

            },
            onDismiss = {
                showSuccessDialog = false // Close the dialog
            }
        )
    } else if (showErrorDialog) {
        ErrorDialog(
            onDismiss = {
                showErrorDialog = false // Close the dialog
            }
        )
    }


    AndroidView(
        factory = { AndroidViewContext ->
            PreviewView(AndroidViewContext).apply {
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
    ) { previewView ->
        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()
        val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
        val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
            ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            cameraProviderShare = cameraProvider
            val barcodeAnalyser = BarCodeAnalyser { barcodes ->
                barcodes.forEach { barcode ->
                    barcode.rawValue?.let { barcodeValue ->
                        //read qr
                        if (isValidQr(barcodeValue)) {
                            onQrScanned()
                            showSuccessDialog = true // Display the dialog when QR is detected
                        } else {
                            showErrorDialog = true
                        }
                    }
                }
            }
            val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, barcodeAnalyser)
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )
            } catch (e: Exception) {
                Log.d("TAG", "CameraPreview: ${e.localizedMessage}")
            }
        }, ContextCompat.getMainExecutor(context))
    }
}

/**
 * Composable function for the confirmation dialog.
 *
 * @param onConfirm Callback when confirmation is accepted.
 * @param onDismiss Callback when dialog is dismissed.
 * @param qrData The QR code data.
 */
@Composable
fun ConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    qrData: String?
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Are you present?") },
        text = { Text("Do you want to check in to this lecture?\n${qrData}") },
        confirmButton = {
            Button(
                onClick = onConfirm
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("No")
            }
        }
    )
}

/**
 * Composable function for the error dialog.
 *
 * @param onDismiss Callback when dialog is dismissed.
 */
@Composable
fun ErrorDialog(
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Error") },
        text = { Text("Please scan QR again") },
        confirmButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("OK")
            }
        }
    )
}
