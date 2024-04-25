package hr.ferit.dejanmihic.campspottercompose.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import hr.ferit.dejanmihic.campspottercompose.R

// Set of Material typography styles to start with
val MavenPro = FontFamily(
    Font(R.font.mavenpro_regular),
    Font(R.font.mavenpro_bold)
)

val OpenSansCondensed = FontFamily(
    Font(R.font.opensans_condensed_bold),
    Font(R.font.opensans_condensed_regular),
    Font(R.font.opensans_condensed_italic)
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = MavenPro,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 32.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = MavenPro,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp,
        lineHeight = 29.sp
    ),
    titleSmall = TextStyle(
        fontFamily = MavenPro,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = MavenPro,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 30.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = MavenPro,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodySmall = TextStyle(
        fontFamily = MavenPro,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    labelMedium = TextStyle(
        fontFamily = MavenPro,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = OpenSansCondensed,
        fontWeight = FontWeight.Light,
        fontStyle = FontStyle.Italic,
        fontSize = 12.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = MavenPro,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp
    ),
    
)