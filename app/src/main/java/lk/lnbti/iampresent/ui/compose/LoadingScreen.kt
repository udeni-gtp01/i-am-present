import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.ui.theme.CommonColorScheme

/**
 * Composable function that represents a loading screen with a circular progress indicator.
 */
@Composable
fun LoadingScreen() {
    Column(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CommonColorScheme.main_orange,
                        CommonColorScheme.main_blue
                    )
                )
            )
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadingAnimation() // Circular progress indicator
    }
}

/**
 * Composable function that displays a circular loading animation with customizable parameters.
 *
 * @param indicatorSize The size of the circular progress indicator.
 * @param circleColors The list of colors for the circular progress indicator.
 * @param animationDuration The duration of the rotation animation in milliseconds.
 */
@Composable
fun LoadingAnimation(
    indicatorSize: Dp = 100.dp,
    circleColors: List<Color> = listOf(
        CommonColorScheme.shade_blue,
        CommonColorScheme.shade_green,
        CommonColorScheme.shade_yellow,
        CommonColorScheme.shade_orange,
        CommonColorScheme.shade_purple,
    ),
    animationDuration: Int = 360
) {

    val infiniteTransition =
        rememberInfiniteTransition(label = stringResource(id = R.string.loading))

    val rotateAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                easing = LinearEasing
            )
        ), label = stringResource(id = R.string.loading)
    )

    CircularProgressIndicator(
        modifier = Modifier
            .size(size = indicatorSize)
            .rotate(degrees = rotateAnimation)
            .border(
                width = 4.dp,
                brush = Brush.sweepGradient(circleColors),
                shape = CircleShape
            ),
        progress = 1f,
        strokeWidth = 1.dp,
        color = MaterialTheme.colorScheme.background
    )
}