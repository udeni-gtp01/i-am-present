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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.ui.theme.CommonColorScheme
import lk.lnbti.iampresent.ui.theme.IAmPresentTheme
import lk.lnbti.iampresent.view_model.LectureInfoViewModel

@Composable
fun LectureInfoScreen(
    lectureId: String?,
    lectureInfoViewModel: LectureInfoViewModel = hiltViewModel(),
    onCancelButtonClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
    onEditButtonClicked: () -> Unit,
    onTodayNavButtonClicked: () -> Unit,
    onAllNavButtonClicked: () -> Unit,
    modifier: Modifier = Modifier,
    onReportsNavButtonClicked: () -> Unit
) {
    lectureInfoViewModel.findLecture(lectureId!!)
    val lecture: Lecture? by lectureInfoViewModel.lecture.observeAsState(null)
    val qrText: String? by lectureInfoViewModel.qrText.observeAsState(null)
    Scaffold(
        topBar = {
            TopAppBar(
                title = R.string.lecture_details,
                description = R.string.lecture_details_description,
                modifier = modifier
            )
        },

        bottomBar = {
            CoordinatorBottomNavigation(
                onTodayNavButtonClicked = onTodayNavButtonClicked,
                onReportsNavButtonClicked = onReportsNavButtonClicked,
                onAllNavButtonClicked = onAllNavButtonClicked,
                isTodayNavItemSelected = false,
                isReportsNavItemSelected = false,
                isAllNavItemSelected = false
            )
        }
    ) { padding ->
        Column(
            modifier
                .padding(padding)
        ) {
            lecture?.let {
                LectureInfoContent(
                    lecture = it,
                    onOpenButtonClicked = { lectureId ->
                        lectureInfoViewModel.openForAttendance(lectureId)
                    },
                    onCloseButtonClicked = { lectureId ->
                        lectureInfoViewModel.closeForAttendance(lectureId)
                    },
                    qrText = qrText,
                    onDeleteButtonClicked = { deleteLectureId ->
                        lectureInfoViewModel.deleteLecture(deleteLectureId)
                        onDeleteButtonClicked()
                    }
                )
            }
            Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
        }
    }
}

@Composable
fun LectureInfoContent(
    lecture: Lecture,
    qrText: String?,
    onOpenButtonClicked: (Long) -> Unit,
    onDeleteButtonClicked: (Long) -> Unit,
    onCloseButtonClicked: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(contentPadding = PaddingValues(dimensionResource(id = R.dimen.height_default_spacer))) {
        item {
            Column {
                LabelHeader(text = R.string.batch)
                LabelBody(text = lecture.batch)
                LabelHeader(text = R.string.semester)
                LabelBody(text = lecture.semester.toString())
                LabelHeader(text = R.string.subject)
                LabelBody(text = lecture.subject)
                LabelHeader(text = R.string.current_status)
                LabelBody(text = lecture.lectureStatus.statusName)
                Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
                Divider()
            }
        }
        item {
            Column {
                Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
                LabelHeader(text = R.string.lecturer_name)
                LabelBody(text = lecture.lecturer.name)
                LabelHeader(text = R.string.lecturer_email)
                lecture.lecturer.email?.let { LabelBody(text = it) }
                Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
                Divider()
            }
        }
        item {
            Column {
                Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
                LabelHeader(text = R.string.starts_at)
                LabelBody(text = "${lecture.startDate} @ ${lecture.startTime}")
                LabelHeader(text = R.string.ends_at)
                LabelBody(text = "${lecture.endDate} @ ${lecture.endTime}")
                LabelHeader(text = R.string.location)
                LabelBody(text = lecture.location)
                Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
                Divider()
            }
        }

        if (lecture.lectureStatus.lectureStatusId != 2) {
            item {
                Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
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
                Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
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
            Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
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
fun LabelHeader(
    @StringRes text: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = text),
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier.fillMaxWidth(),
        color = CommonColorScheme.gray,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        fontWeight = FontWeight.Bold
    )
    Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_between_label_header)))
}

@Composable
fun LabelBody(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        modifier = modifier.fillMaxWidth(),
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
    Spacer(Modifier.height(dimensionResource(id = R.dimen.padding_between_label)))
}