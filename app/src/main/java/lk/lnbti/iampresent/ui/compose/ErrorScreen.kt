package lk.lnbti.iampresent.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.ui.theme.CommonColorScheme

/**
 * Composable function that displays an error screen with a given error message and a retry button.
 *
 * @param errorMessage The error message to be displayed on the screen.
 * @param onRetry Callback function to be invoked when the user clicks the retry button.
 */
@Composable
fun ErrorScreen(errorMessage: String, onRetry: () -> Any) {
    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadError(errorMessage, onRetry)
    }
}

/**
 * Composable function that displays the error message and a retry button.
 *
 * @param errorMessage The error message to be displayed on the screen.
 * @param onRetry Callback function to be invoked when the user clicks the retry button.
 */
@Composable
fun LoadError(errorMessage: String, onRetry: () -> Any?) {
    Text(
        text = errorMessage,
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.dp_12))
            .fillMaxWidth(),
        overflow = TextOverflow.Visible
    )

    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.dp_12)))

    Button(
        colors = ButtonDefaults.outlinedButtonColors(contentColor = CommonColorScheme.dark_text),
        onClick = { onRetry() },
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CommonColorScheme.shade_yellow,
                        CommonColorScheme.main_orange,
                    )
                ),
                shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
            )
    ) {
        Text(
            text = stringResource(R.string.retry),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewErrorScreen() {
    ErrorScreen(
        errorMessage = "err",
        onRetry = {})
}

