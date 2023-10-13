import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import lk.lnbti.iampresent.R

@Composable
fun ErrorScreen(errorMessage: String, onRetry: () -> Any) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadError(errorMessage, onRetry) // Circular progress indicator
    }
}

@Composable
fun LoadError(errorMessage: String, onRetry: () -> Any?) {
    Text(
        text = "Error: $errorMessage",
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(16.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Button(
            onClick = { onRetry() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.retry))
        }
        OutlinedButton(
            onClick = {
                FirebaseCrashlytics.getInstance().log(errorMessage)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.report_problem))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewErrorScreen() {
    ErrorScreen(
        errorMessage = "errorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerrorerror",
        onRetry = {})
}

