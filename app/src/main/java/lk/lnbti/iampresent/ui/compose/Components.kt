package lk.lnbti.iampresent.ui.compose

import android.graphics.Bitmap
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import lk.lnbti.iampresent.R
import lk.lnbti.iampresent.data.Lecture
import lk.lnbti.iampresent.ui.theme.CommonColorScheme
import lk.lnbti.iampresent.ui.theme.Typography

/**
 * Composable function for creating a customized Top App Bar with a title and description.
 *
 * @param title The resource ID for the title text.
 * @param description The resource ID for the description text.
 * @param modifier Modifier for customizing the appearance of the Top App Bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    @StringRes title: Int,
    @StringRes description: Int,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        ),
        title = {
            Column(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                Text(
                    stringResource(id = title),
                    style = Typography.titleLarge
                )
                Text(
                    stringResource(id = description),
                    style = Typography.titleSmall
                )
            }
        },
        modifier = modifier
            .padding(top = 20.dp, bottom = 10.dp)
    )
}

/**
 * Composable function for displaying a single item in the list of lectures.
 *
 * @param item The lecture item to display.
 * @param onLectureItemClicked Callback function for handling clicks on lecture items.
 */
@Composable
fun LectureListItem(
    item: Lecture,
    onLectureItemClicked: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onLectureItemClicked(item.lectureId.toString()) }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = item.batch,
                style = MaterialTheme.typography.titleLarge,
                color = CommonColorScheme.dark_text
            )
            if (item.lectureStatus.lectureStatusId == 2) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = CommonColorScheme.status_ongoing),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner)),
                ) {
                    Text(
                        text = "${stringResource(id = R.string.ongoing)}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(7.dp),
                        color = CommonColorScheme.dark_text
                    )
                }
            } else if (item.lectureStatus.lectureStatusId == 3) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                    colors = CardDefaults.cardColors(containerColor = CommonColorScheme.status_complete),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner)),
                    modifier = Modifier
                ) {
                    Text(
                        text = "${stringResource(id = R.string.complete)}",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(7.dp),
                        color = CommonColorScheme.dark_text
                    )
                }
            }
        }
        Text(
            text = "${stringResource(R.string.subject)}: ${item.subject}",
            style = MaterialTheme.typography.bodyLarge,
            color = CommonColorScheme.dark_text
        )
        Text(
            text = "${stringResource(id = R.string.lecturer)}: ${item.lecturer.name}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 6.dp, bottom = 6.dp),
            color = CommonColorScheme.dark_text
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val timePainter: Painter = painterResource(id = R.drawable.time)
                Icon(
                    painter = timePainter,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = CommonColorScheme.dark_text
                )
                Text(
                    text = "${item.startTime} - ${item.endTime}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 10.dp),
                    color = CommonColorScheme.dark_text
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                val locationPainter: Painter = painterResource(id = R.drawable.location)
                Icon(
                    painter = locationPainter,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = CommonColorScheme.dark_text
                )
                Text(
                    text = "${item.location}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CommonColorScheme.dark_text
                )
            }
        }
    }
}


/**
 * Composable function for displaying a filter item with optional background color when selected.
 *
 * @param criteria The resource ID for the filter criteria text.
 * @param isSelected Boolean indicating whether the filter item is selected.
 * @param onClick Callback function for handling clicks on the filter item.
 */
@Composable
fun FilterItem(
    @StringRes criteria: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Text(
        text = stringResource(id = criteria),
        style = MaterialTheme.typography.bodyMedium,
        modifier = if (isSelected) {
            Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CommonColorScheme.shade_yellow,
                            CommonColorScheme.main_orange
                        )
                    ),
                    shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
                )

                .clickable { onClick() }
                .padding(10.dp)
                .defaultMinSize(minWidth = 50.dp)
        } else {
            Modifier
                .background(
                    color = Color.Transparent,
                )
                .clickable { onClick() }
                .padding(10.dp)
        },
        textAlign = TextAlign.Center,
        fontWeight = if (isSelected) {
            FontWeight.Bold
        } else {
            FontWeight.Normal
        },
        color = if (isSelected) {
            CommonColorScheme.dark_text
        } else {
            MaterialTheme.colorScheme.inverseSurface
        }
    )
}

/**
 * Composable function for displaying a header for a group of items in the list.
 *
 * @param header The text to be displayed as the header.
 */
@Composable
fun ListGroupHeader(header: String) {
    Text(
        text = header,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background
            )
            .padding(10.dp),
        fontWeight = FontWeight.Bold,
    )
}

/**
 * Composable function for wrapping content in a styled list item.
 *
 * @param content The content to be displayed inside the list item.
 */
@Composable
fun ListItemContent(
    content: @Composable() (ColumnScope.() -> Unit)
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(R.dimen.padding_between_label))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        CommonColorScheme.main_orange,
                        CommonColorScheme.main_blue
                    )
                ),
                shape = RoundedCornerShape(dimensionResource(R.dimen.round_corner))
            )
    ) {
        Column(
            content = content
        )
    }
}

/**
 * Composable function for displaying a FloatingActionButton with an "Add" icon.
 *
 * @param onNewLectureClicked Callback function for handling clicks on the "Add" button.
 */
@Composable
fun AddNewLectureButton(onNewLectureClicked: () -> Unit) {
    FloatingActionButton(
        shape = MaterialTheme.shapes.large.copy(CornerSize(percent = 50)),
        onClick = onNewLectureClicked,
        contentColor = MaterialTheme.colorScheme.primary,
        containerColor = Color.Transparent,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp, 56.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            CommonColorScheme.nav_light_blue,
                            CommonColorScheme.nav_blue
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, contentDescription = null, tint = CommonColorScheme.white)

        }
    }
}

/**
 * Composable function for creating a custom Bottom Navigation Bar with three navigation buttons.
 *
 * @param onTodayNavButtonClicked Callback function for handling clicks on the "Today" navigation button.
 * @param onReportsNavButtonClicked Callback function for handling clicks on the "Reports" navigation button.
 * @param onAllNavButtonClicked Callback function for handling clicks on the "All" navigation button.
 */
@Composable
fun BottomNavigation(
    onTodayNavButtonClicked: () -> Unit,
    onReportsNavButtonClicked: () -> Unit,
    onAllNavButtonClicked: () -> Unit,
) {
    NavigationBar(
        containerColor = CommonColorScheme.nav_blue,
        contentColor = Color.White
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.today))
            },
            colors = NavigationBarItemDefaults
                .colors(
                    selectedIconColor = CommonColorScheme.shade_yellow,
                    selectedTextColor = CommonColorScheme.shade_yellow,
                    unselectedTextColor = Color.White,
                    indicatorColor = CommonColorScheme.nav_light_blue,
                    unselectedIconColor = Color.White,

                    ),
            selected = true,
            onClick = onTodayNavButtonClicked,
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.reports))
            },
            selected = false,
            onClick = onReportsNavButtonClicked,
            colors = NavigationBarItemDefaults
                .colors(
                    selectedIconColor = CommonColorScheme.shade_yellow,
                    selectedTextColor = CommonColorScheme.shade_yellow,
                    unselectedTextColor = Color.White,
                    indicatorColor = CommonColorScheme.nav_light_blue,
                    unselectedIconColor = Color.White,
                )
        )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = null
                )
            },
            label = {
                Text(stringResource(R.string.all))
            },
            colors = NavigationBarItemDefaults
                .colors(
                    selectedIconColor = CommonColorScheme.shade_yellow,
                    selectedTextColor = CommonColorScheme.shade_yellow,
                    unselectedTextColor = Color.White,
                    indicatorColor = CommonColorScheme.nav_light_blue,
                    unselectedIconColor = Color.White,
                ),
            selected = false,
            onClick = onAllNavButtonClicked
        )
    }
}

@Composable
fun ShowQR(qrString: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberQrBitmapPainter(qrString),
            contentDescription = "Scan me",
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

        launch(Dispatchers.Default) {
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
                    val pixelColor =
                        if (shouldColorPixel) android.graphics.Color.BLACK else android.graphics.Color.WHITE
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
        ).apply { eraseColor(android.graphics.Color.TRANSPARENT) }
        BitmapPainter(currentBitmap.asImageBitmap())
    }
}