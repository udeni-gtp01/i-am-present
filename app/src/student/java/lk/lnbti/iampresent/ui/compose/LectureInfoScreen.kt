package lk.lnbti.iampresent.ui.compose

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lk.lnbti.app_student.view_model.AttendanceInfoViewModel
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.ui.theme.IAmPresentTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LectureAttendScreen(
    lectureId: String?,
    attendanceInfoViewModel: AttendanceInfoViewModel = hiltViewModel(),
    onCancelButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    // attendanceInfoViewModel.findLecture(lectureId!!)
    val lecture: Lecture? by attendanceInfoViewModel.lecture.observeAsState(null)
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

        bottomBar = {  }
    ) { padding ->
        Column(
            modifier
                .padding(padding)
        ) {
            lecture?.let {
                LectureInfoContent(
                    lecture = lecture!!,
                    onOpenButtonClicked = { lectureId ->
                        attendanceInfoViewModel.openForAttendance(lectureId)
                    },
                    qrText = attendanceInfoViewModel.qrText.toString()
                )
            }
            //SearchBar(Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_main_content)))
            //Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
            Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
        }
    }
}

@Composable
fun LectureInfoContent(
    lecture: Lecture,
    qrText: String,
    onOpenButtonClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    /*   LazyColumn(contentPadding = PaddingValues(16.dp)) {
           item {
               labelHeader(text = R.string.batch)
               labelBody(text = lecture.batch)
           }
           item {
               labelHeader(text = R.string.semester)
               labelBody(text = lecture.semester.toString())
           }
           item {
               labelHeader(text = R.string.subject)
               labelBody(text = lecture.subject)
           }
           item {
               labelHeader(text = R.string.location)
               labelBody(text = lecture.location)
           }
           item {
               labelHeader(text = R.string.starts_at)
               labelBody(text = "${lecture.startDate} @ ${lecture.startTime}")
           }
           item {
               labelHeader(text = R.string.ends_at)
               labelBody(text = "${lecture.endDate} @ ${lecture.endTime}")
           }
           item {
               labelHeader(text = R.string.lecturer_name)
               labelBody(text = lecture.lecturer.name)
           }
           item {
               labelHeader(text = R.string.lecturer_email)
               lecture.lecturer.email?.let { labelBody(text = it) }
           }
           item {
               labelHeader(text = R.string.current_status)
               labelBody(text = lecture.lectureStatus.statusName)
           }
           item {
               Button(
                   modifier = Modifier.fillMaxWidth(),
                   onClick = { onOpenButtonClicked(lecture.lectureId) }
               ) {
                   Text(
                       text = stringResource(R.string.open_for_attendance),
                       //fontSize = 16.sp
                   )
               }
           }
           if (lecture.lectureStatus.lectureStatusId == 2) {
               item {
                   Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
                   ShowQR(qrText)
               }
           }
       }*/
}

@Composable
fun labelHeader(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = text),
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_between_label_header)))
}

@Composable
fun labelBody(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        modifier = modifier.fillMaxWidth()
    )
    Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_between_label)))
}

@Composable
fun ShowQR(qrString: String = "") {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberQrBitmapPainter(qrString),
            contentDescription = "DEV Communit Code",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.size(135.dp),
        )
    }

}

@Composable
fun rememberQrBitmapPainter(
    content: String,
    size: Dp = 150.dp,
    padding: Dp = 0.dp
): BitmapPainter {
    val density = LocalDensity.current
    val sizePx = with(density) { size.roundToPx() }
    val paddingPx = with(density) { padding.roundToPx() }

    var bitmap by remember(content) {
        mutableStateOf<Bitmap?>(null)
    }
    LaunchedEffect(bitmap) {
        if (bitmap != null) return@LaunchedEffect

        launch(Dispatchers.IO) {
            val qrCodeWriter = QRCodeWriter()

            val encodeHints = mutableMapOf<EncodeHintType, Any?>()
                .apply {
                    this[EncodeHintType.MARGIN] = paddingPx
                }

            val bitmapMatrix = try {
                qrCodeWriter.encode(
                    content, BarcodeFormat.QR_CODE,
                    sizePx, sizePx, encodeHints
                )
            } catch (ex: WriterException) {
                null
            }
            val matrixWidth = bitmapMatrix?.width ?: sizePx
            val matrixHeight = bitmapMatrix?.height ?: sizePx

            val newBitmap = Bitmap.createBitmap(
                bitmapMatrix?.width ?: sizePx,
                bitmapMatrix?.height ?: sizePx,
                Bitmap.Config.ARGB_8888,
            )

            for (x in 0 until matrixWidth) {
                for (y in 0 until matrixHeight) {
                    val shouldColorPixel = bitmapMatrix?.get(x, y) ?: false
                    val pixelColor = if (shouldColorPixel) Color.BLACK else Color.WHITE

                    newBitmap.setPixel(x, y, pixelColor)
                }
            }

            bitmap = newBitmap
        }
    }
    return remember(bitmap) {
        val currentBitmap = bitmap ?: Bitmap.createBitmap(
            sizePx, sizePx,
            Bitmap.Config.ARGB_8888,
        ).apply { eraseColor(Color.TRANSPARENT) }

        BitmapPainter(currentBitmap.asImageBitmap())
    }
}

//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLectureInfoContent() {
    val jsonString: String =
        "{\"lectureid\":1,\"venue\":\"online\",\"startdate\":\"2023-12-12\",\"starttime\":\"08:00:00\",\"enddate\":\"2023-12-12\",\"endtime\":\"12:00:00\",\"semester\":2,\"subject\":\"kotlin\",\"batch\":\"gtp01\",\"lecturerid\":{\"userid\":1,\"name\":\"admin\",\"email\":\"admin@lnbti.edu.lk\",\"token\":null,\"roleid\":{\"roleid\":1,\"name\":\"admin\"},\"isuseravailable\":1},\"lecturestatusid\":{\"lecturestatusid\":1,\"statusname\":\"new\"},\"organizer\":{\"userid\":1,\"name\":\"admin\",\"email\":\"admin@lnbti.edu.lk\",\"token\":null,\"roleid\":{\"roleid\":1,\"name\":\"admin\"},\"isuseravailable\":1}}"
    val gson = Gson()

// Define a TypeToken for the list of Lecture objects
    val lectureType = object : TypeToken<Lecture>() {}.type

// Parse the JSON string into a list of Lecture objects
    val lecture: Lecture = gson.fromJson(jsonString, lectureType)
    IAmPresentTheme {
        //LectureInfoContent(lecture = lecture)
    }
}