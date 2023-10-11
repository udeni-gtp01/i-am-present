package lk.lnbti.app_student.ui.theme.view

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.res.stringResource
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
import lk.lnbti.iampresent.view_model.NewLectureViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun NewLectureScreen(
    newLectureViewModel: NewLectureViewModel = hiltViewModel(),
    onYesButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val qrData: String? by newLectureViewModel.qrData.observeAsState(null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.app_name)
                    )
                },
            )
        },

        //bottomBar = { BottomNavigation() }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            //color = MaterialTheme.colorScheme.background
        ) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                val cameraPermissionState =
                    rememberPermissionState(permission = Constant.PERMISSION_CAMERA)

                when {
                    cameraPermissionState.status.isGranted -> {
                        // Camera permission is granted, show the camera preview
                        CameraPreview(
                            qrData = qrData,
                            isValidQr = { qrData -> newLectureViewModel.isValidQr(qrData) },
                            onQrScanned = { newLectureViewModel.onQrDataChange() },
                            onYesButtonClicked = {
                                onYesButtonClicked()
                                newLectureViewModel.saveAttendance()
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
            .padding(50.dp),
        update = { previewView ->
            val cameraSelector: CameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
            val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
            val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                preview = androidx.camera.core.Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                cameraProviderShare=cameraProvider
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
                            //Toast.makeText(context, barcodeValue, Toast.LENGTH_SHORT).show()
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
    )
}

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
