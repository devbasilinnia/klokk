package com.theapache64.kineticwallclock.composable

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.theapache64.kineticwallclock.theme.CodGray
import com.theapache64.kineticwallclock.theme.CodGray_2
import kotlin.math.cos
import kotlin.math.sin


private val NEEDLE_COLOR = Color.White

@Composable
fun Clock(
    _needleOneDegree: Float = 270f,
    _needleTwoDegree: Float = 0f,
    durationInMillis: Int = 500,
    easing: Easing = LinearEasing,
    debug: Boolean = false,
    modifier: Modifier = Modifier,
) {

    val needleOneDegree = (_needleOneDegree * Math.PI / 180).toFloat()
    val needleTwoDegree = (_needleTwoDegree * Math.PI / 180).toFloat()
    val animationSpec = tween<Float>(durationMillis = durationInMillis, easing = easing)

    val needleOneDegreeAnim by animateFloatAsState(
        needleOneDegree,
        animationSpec = animationSpec
    )

    val needleTwoDegreeAnim by animateFloatAsState(
        needleTwoDegree,
        animationSpec = animationSpec,
    )


    Canvas(
        modifier = modifier
    ) {

        val needleWidth = size.minDimension * 0.05f

        // Background
        val radius = size.minDimension / 2f

        drawCircle(
            color = CodGray_2,
            radius = radius
        )

        drawCircle(
            color = NEEDLE_COLOR,
            radius = needleWidth * 0.487f
        )


        // Needle One
        drawLine(
            color = NEEDLE_COLOR,
            start = center,
            end = Offset(
                // Finding end coordinate for the given degree
                x = center.x + radius * sin(needleOneDegreeAnim),
                y = center.y - radius * cos(needleOneDegreeAnim),
            ),
            strokeWidth = needleWidth
        )


        // Needle two
        val needleTwoEndX = center.x + radius * sin(needleTwoDegreeAnim)
        val needleTwoEndY = center.y - radius * cos(needleTwoDegreeAnim)
        drawLine(
            color = NEEDLE_COLOR,
            start = center,
            end = Offset(
                // Finding end coordinate for the given degree
                x = needleTwoEndX,
                y = needleTwoEndY,
            ),
            strokeWidth = needleWidth
        )

        /*if(debug){
            drawIntoCanvas {
                it.nativeCanvas.drawString(
                    "Hello World",
                    needleTwoEndX,
                    needleTwoEndY,
                    org.jetbrains.skija.Font
                    )
            }
        }*/


    }

}


// Preview
fun main(args: Array<String>) {
    Window {
        var needleOneDegree by remember { mutableStateOf(270f) }
        var needleTwoDegree by remember { mutableStateOf(180f) }


        val scope = rememberCoroutineScope()

        Box(
            modifier = Modifier.fillMaxSize().background(CodGray)
        ) {

            Clock(
                _needleOneDegree = needleOneDegree,
                _needleTwoDegree = needleTwoDegree,
                modifier = Modifier.size(600.dp),
                durationInMillis = 2000
            )

            Button(
                onClick = {
                    needleOneDegree = getMirroredAngleFor(needleOneDegree)
                    needleTwoDegree = getMirroredAngleFor(needleTwoDegree)

                    /* scope.launch {
                         delay(5000)
                         needleOneDegree = (0..360).random().toFloat()
                         needleTwoDegree = (0..360).random().toFloat()
                     }*/
                }
            ) {
                Text(text = "Animate")
            }
        }
    }
}

fun getMirroredAngleFor(current: Float): Float {
    return if (current > 0 && current < 180) {
        180 - current
    } else {
        (360 - (current % 180))
    }
}
