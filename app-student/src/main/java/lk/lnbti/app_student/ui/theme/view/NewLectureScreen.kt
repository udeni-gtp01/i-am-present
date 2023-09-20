package lk.lnbti.iampresent.ui.view

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
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
import com.google.accompanist.permissions.rememberPermissionState
import com.google.common.util.concurrent.ListenableFuture
import lk.lnbti.app_student.ui.theme.view.BarCodeAnalyser
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.constant.Constant
import lk.lnbti.iampresent.view_model.NewLectureViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun NewLectureScreen(
    newLectureViewModel: NewLectureViewModel = hiltViewModel(),
    onSaveButtonClicked: (String) -> Unit,
    onCancelButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
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
//        Column(
//            modifier
//                .padding(padding)
//        ) {
//
//            ScanQrContent(
//                onPresentButtonClicked = {},
//                modifier = modifier
//            )
//            Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
//        }
        Surface(modifier = Modifier.fillMaxSize()
            .padding(padding),
            //color = MaterialTheme.colorScheme.background
            ) {
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                val cameraPermissionState = rememberPermissionState(permission = Constant.PERMISSION_CAMERA)
                //PreviewViewComposable()
                Button(
                    onClick = {
                        cameraPermissionState.launchPermissionRequest()
                    }
                ) {
                    Text(text = "Camera Permission")
                }

                Spacer(modifier = Modifier.height(10.dp))

                CameraPreview()

//                Text(
//                    text = "Scan QR Code",
//                    modifier = Modifier.padding(padding)
//                )

            }
        }
    }
}
////////////////
@Composable
fun CameraPreview() {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    val barCodeVal = remember { mutableStateOf("") }

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
                val barcodeAnalyser = BarCodeAnalyser { barcodes ->
                    barcodes.forEach { barcode ->
                        barcode.rawValue?.let { barcodeValue ->
                            barCodeVal.value = barcodeValue
                            Toast.makeText(context, barcodeValue, Toast.LENGTH_SHORT).show()
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
////////////

//@androidx.annotation.OptIn(ExperimentalGetImage::class) @Composable
//fun ScanQrContent(
//
//    modifier: Modifier = Modifier,
//    onPresentButtonClicked: () -> Unit,
//) {
//    PreviewViewComposable()
//}
//
//@Composable
//fun NewOutlinedTextField(
//    value: String,
//    label: @Composable (() -> Unit)? = null,
//    onValueChange: (String) -> Unit,
//    singleLine: Boolean = true,
//    isError: Boolean = false,
//    keyboardType: KeyboardType = KeyboardType.Text,
//    keyboardActions: KeyboardActions = KeyboardActions.Default,
//    modifier: Modifier = Modifier,
//) {
//    OutlinedTextField(
//        value = value,
//        label = label,
//        onValueChange = onValueChange,
//        singleLine = singleLine,
//        isError = isError,
//        keyboardOptions = KeyboardOptions.Default.copy(
//            capitalization = KeyboardCapitalization.Sentences,
//            keyboardType = keyboardType
//        ),
//        keyboardActions = keyboardActions,
//        modifier = modifier.fillMaxWidth()
//    )
//    Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_between_field)))
//}
//
//@Composable
//fun NewDateField(
//    value: String,
//    label: String,
//    onValueChange: (String) -> Unit,
//) {
//    val context = LocalContext.current
//    val defaultDateFormat = SimpleDateFormat("yyyy-M-d")
//    val newDateFormat = SimpleDateFormat("MMMM d, yyyy")
//    val calendar = Calendar.getInstance()
//    val year = calendar[Calendar.YEAR]
//    val month = calendar[Calendar.MONTH]
//    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]
//    var selectedDate: String = ""
//
//    val datePicker = DatePickerDialog(
//        context,
//        { _: DatePicker, selectedYear: Int, selectedMonth: Int, SelectedDayOfMonth: Int ->
//            val newDate = "$selectedYear-${selectedMonth + 1}-$SelectedDayOfMonth"
//            val date = defaultDateFormat.parse(newDate)
//            selectedDate = newDateFormat.format(date)
//            onValueChange(selectedDate)
//        },
//        year,
//        month,
//        dayOfMonth
//    )
//    Row(
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier
//            .fillMaxWidth()
//    ) {
//        OutlinedButton(
//            onClick = { datePicker.show() }
//        ) {
//            Text(text = label)
//        }
//        Text(text = value)
//    }
//    Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_between_field)))
//}
//
//@Composable
//fun NewTimeField(
//    value: String,
//    label: String,
//    onValueChange: (String) -> Unit,
//) {
//    val context = LocalContext.current
//    val defaultTimeFormat = SimpleDateFormat("H:m")
//    val newTimeFormat = SimpleDateFormat("h:mm a")
//    val calendar = Calendar.getInstance()
//    val hour = calendar[Calendar.HOUR_OF_DAY]
//    val minute = calendar[Calendar.MINUTE]
//    var selectedTime: String = ""
//
//    val timePicker = TimePickerDialog(
//        context,
//        { _, selectedHour: Int, selectedMinute: Int ->
//            val newTime = "$selectedHour:$selectedMinute"
//            val time = defaultTimeFormat.parse(newTime)
//            selectedTime = newTimeFormat.format(time)
//            onValueChange(selectedTime)
//        },
//        hour,
//        minute,
//        true
//    )
//    Row(
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = Modifier
//            .fillMaxWidth()
//    ) {
//        OutlinedButton(
//            onClick = { timePicker.show() }
//        ) {
//            Text(text = label)
//        }
//        Text(text = value)
//
//    }
//    Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_between_field)))
//}
//
//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun PreviewNewLectureContent() {
//
//}
//
//
//@androidx.annotation.OptIn(ExperimentalGetImage::class) @Composable
//fun PreviewViewComposable() {
//    AndroidView(
//        { context ->
//            val cameraExecutor = Executors.newSingleThreadExecutor()
//            val previewView = PreviewView(context).also {
//                it.scaleType = PreviewView.ScaleType.FILL_CENTER
//            }
//            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
//            cameraProviderFuture.addListener({
//                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//                val preview = androidx.camera.core.Preview.Builder()
//                    .build()
//                    .also {
//                        it.setSurfaceProvider(previewView.surfaceProvider)
//                    }
//
//                val imageCapture = ImageCapture.Builder().build()
//
//                val imageAnalyzer = ImageAnalysis.Builder()
//                    .build()
//                    .also {
//                        it.setAnalyzer(cameraExecutor, BarcodeAnalyser {
//                            Toast.makeText(context, "Barcode found", Toast.LENGTH_SHORT).show()
//                        })
//                    }
//
//                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//
//                try {
//                    // Unbind use cases before rebinding
//                    cameraProvider.unbindAll()
//
//                    // Bind use cases to camera
//                    cameraProvider.bindToLifecycle(
//                        context as ComponentActivity,
//                        cameraSelector,
//                        preview,
//                        imageCapture,
//                        imageAnalyzer
//                    )
//
//                } catch (exc: Exception) {
//                    Log.e("DEBUG", "Use case binding failed", exc)
//                }
//            }, ContextCompat.getMainExecutor(context))
//            previewView
//        },
//        modifier = Modifier
//            .size(width = 250.dp, height = 250.dp)
//    )
//}