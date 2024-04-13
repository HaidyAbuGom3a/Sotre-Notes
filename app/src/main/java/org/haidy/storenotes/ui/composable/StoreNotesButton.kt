package org.haidy.storenotes.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.haidy.storenotes.ui.theme.Purple40
import org.haidy.storenotes.ui.theme.White

@Composable
fun StoreNotesButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    iconPainter: Painter? = null,
    containerColor: Color = Purple40,
    contentColor: Color = White,
    enabled: Boolean = true,
    fontWeight: FontWeight = FontWeight.Bold,
    fontSize: TextUnit = 16.sp,
    isLoading: Boolean = false
) {
    Button(
        { onClick() },
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(16.dp),
        enabled = enabled
    ) {

        AnimatedVisibility(visible = iconPainter != null) {
            Image(painter = iconPainter!!, contentDescription = null)
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            if(isLoading) "Loading..." else text,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = fontWeight,
            fontSize = fontSize
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}