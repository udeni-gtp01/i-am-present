package lk.lnbti.iampresent.ui.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.view_model.NewLectureViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewLectureScreen(
    newLectureViewModel: NewLectureViewModel = viewModel(),
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

        bottomBar = { BottomNavigation() }
    ) { padding ->
        Column(
            modifier
                .padding(padding)
        ) {
            val batch: String by newLectureViewModel.batch.observeAsState(initial = "")
            NewLectureContent(
                batch = batch,
                isBatchError = newLectureViewModel.isBatchError,
                onBatchValueChange = { newLectureViewModel.onBatchChange(it) },
                modifier = modifier
            )
            Spacer(Modifier.height(dimensionResource(id = R.dimen.height_default_spacer)))
        }
    }
}

@Composable
fun NewLectureContent(
    batch: String,
    isBatchError: Boolean,
    onBatchValueChange: (String) -> Unit,

//    semester: String,
//    subject: String,
//    lectureTopic: String,
//    startDate: String,
//    startTime: String,
//    endDate: String,
//    endTime: String,
//    venue: String,
//    lecturerEmail: String,
    modifier: Modifier = Modifier
) {
    LazyColumn(contentPadding = PaddingValues(16.dp)) {

        item {
            NewOutlinedTextField(
                value = batch,
                label = { if (isBatchError){Text(text = stringResource(id = R.string.enter_correct_batch))}else{Text(text = stringResource(id = R.string.enter_batch))} },
                onValueChange = onBatchValueChange,
                isError = isBatchError,
                modifier = modifier
            )
        }
    }
}

@Composable
fun NewOutlinedTextField(
    value: String,
    label: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        label = label,
        onValueChange = onValueChange,
        singleLine = singleLine,
        isError = isError,
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.Sentences,
            keyboardType = keyboardType
        ),
        keyboardActions = keyboardActions,
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewNewLectureContent() {
    NewLectureContent(batch = "", isBatchError = false, onBatchValueChange = {})
}