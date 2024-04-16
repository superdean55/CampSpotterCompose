package hr.ferit.dejanmihic.campspottercompose.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import hr.ferit.dejanmihic.campspottercompose.ui.theme.CampSpotterComposeTheme
import hr.ferit.dejanmihic.campspottercompose.ui.theme.DarkerBlue
import hr.ferit.dejanmihic.campspottercompose.ui.theme.LightBlue
import hr.ferit.dejanmihic.campspottercompose.ui.theme.MediumBlue
import hr.ferit.dejanmihic.campspottercompose.ui.utils.standardQuadFromTo


@Composable
fun Background(
    anyComposable: @Composable () -> Unit,
){
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlue)
    ) {
        val width = constraints.maxWidth
        val height = constraints.maxHeight

        // Medium colored path
        val mediumColoredPoint1 = Offset(0f, height * 0.3f)
        val mediumColoredPoint2 = Offset(width * 0.1f, height * 0.35f)
        val mediumColoredPoint3 = Offset(width * 0.4f, height * 0.05f)
        val mediumColoredPoint4 = Offset(width * 0.75f, height * 0.7f)
        val mediumColoredPoint5 = Offset(width * 1.4f, -height.toFloat())

        val mediumColoredPath = Path().apply {
            moveTo(mediumColoredPoint1.x, mediumColoredPoint1.y)
            standardQuadFromTo(mediumColoredPoint1, mediumColoredPoint2)
            standardQuadFromTo(mediumColoredPoint2, mediumColoredPoint3)
            standardQuadFromTo(mediumColoredPoint3, mediumColoredPoint4)
            standardQuadFromTo(mediumColoredPoint4, mediumColoredPoint5)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }

        // Light colored path
        val lightPoint1 = Offset(0f, height * 0.6f)
        val lightPoint2 = Offset(width * 0.15f, height * 0.65f)
        val lightPoint3 = Offset(width * 0.4f, height * 0.55f)
        val lightPoint4 = Offset(width * 0.65f, height * 0.80f)
        val lightPoint5 = Offset(width * 1.4f, height * 0.4f)

        val lightColoredPath = Path().apply {
            moveTo(lightPoint1.x, lightPoint1.y)
            standardQuadFromTo(lightPoint1, lightPoint2)
            standardQuadFromTo(lightPoint2, lightPoint3)
            standardQuadFromTo(lightPoint3, lightPoint4)
            standardQuadFromTo(lightPoint4, lightPoint5)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawPath(
                path = mediumColoredPath,
                color = MediumBlue
            )
            drawPath(
                path = lightColoredPath,
                color = DarkerBlue
            )
        }
        anyComposable()
    }
}

@Preview(showBackground = true)
@Composable
fun BackgroundPreview(){
    CampSpotterComposeTheme {
        Background {

        }
    }
}