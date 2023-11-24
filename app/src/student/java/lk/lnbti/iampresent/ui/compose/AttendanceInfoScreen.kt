package lk.lnbti.iampresent.ui.compose

import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
fun AttendanceInfoScreen(
    lectureId: String?,
    attendanceInfoViewModel: AttendanceInfoViewModel = hiltViewModel(),
    onCancelButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
     attendanceInfoViewModel.findLecture(lectureId!!)
    val lecture: Lecture? by attendanceInfoViewModel.lecture.observeAsState(null)
    val qrText: String? by attendanceInfoViewModel.qrText.observeAsState(null)
    Scaffold(
        topBar = {
            TopAppBar(
                title = R.string.lecture_details,
                description = R.string.lecture_attend_description,
                modifier = modifier
            )
        },
    ){ padding ->
        Column(
            modifier
                .padding(padding)
        ) {
            lecture?.let {
                AttendingLectureInfoSection(
                    lecture = it,
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
fun AttendingLectureInfoSection(
    lecture: Lecture,
    qrText: String?,
    modifier: Modifier = Modifier
) {
    LazyColumn(contentPadding = PaddingValues(dimensionResource(id = R.dimen.height_default_spacer))) {
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
        if (lecture.lectureStatus.lectureStatusId != 2) {
            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        CoroutineScope(Dispatchers.Default).launch {
                            onOpenButtonClicked(lecture.lectureId)
                        }
                    }
                ) {
                    Text(
                        text = stringResource(R.string.open_for_attendance),
                        //fontSize = 16.sp
                    )
                }
            }
        }
        if (lecture.lectureStatus.lectureStatusId == 2) {
            item {
                qrText?.let {
                    Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
                    ShowQR(qrText)
                    Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
                }
            }

            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        CoroutineScope(Dispatchers.Default).launch {
                            onCloseButtonClicked(lecture.lectureId)
                        }
                    }
                ) {
                    Text(
                        text = stringResource(R.string.close_for_attendance),
                    )
                }
            }
        }
        item {
            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onDeleteButtonClicked(lecture.lectureId) }
            ) {
                Text(
                    text = stringResource(R.string.delete_lecture),
                )
            }
        }
    }
}

@Composable
fun labelHeader(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = text),
        style = MaterialTheme.typography.labelMedium,
        modifier = modifier.fillMaxWidth(),
        color = CommonColorScheme.gray,
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