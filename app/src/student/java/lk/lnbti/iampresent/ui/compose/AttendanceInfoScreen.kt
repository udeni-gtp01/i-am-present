package lk.lnbti.iampresent.ui.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import lk.lnbti.app_student.view_model.AttendanceInfoViewModel
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.ui.theme.CommonColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceInfoScreen(
    lectureId: String?,
    attendanceInfoViewModel: AttendanceInfoViewModel = hiltViewModel(),
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
    ) { padding ->
        Column(
            modifier
                .padding(padding)
        ) {
            lecture?.let {
                AttendingLectureInfoSection(
                    lecture = it,
                    qrText = qrText
                )
            }
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
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.height_default_spacer))
    ) {
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
                }
        }

        item {
            qrText?.let {
                Column {
                    Divider()
                    Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
                    ShowQR(qrText)
                    Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
                }
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