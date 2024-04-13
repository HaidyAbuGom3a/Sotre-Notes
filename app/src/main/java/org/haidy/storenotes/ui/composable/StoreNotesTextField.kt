package org.haidy.storenotes.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.haidy.storenotes.ui.theme.Black
import org.haidy.storenotes.ui.theme.Typography

@Composable
fun StoreNotesTextField(
    onValueChange: (String) -> Unit,
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    hintColor: Color = Black.copy(alpha = 0.6f),
    trailingPainter: Painter? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    radius: Dp = 16.dp,
    errorMessage: String = "",
    isError: Boolean = errorMessage.isNotEmpty(),
    onTrailingIconClick: () -> Unit = {},
    isSingleLine: Boolean = true,
    showPassword: Boolean = false,
    readOnly: Boolean = false,
    trailingIconEnabled: Boolean = onTrailingIconClick != {},
    iconTint: Color? = null,
    outlinedTextFieldDefaults: TextFieldColors = outlinedTextFieldColorDefaults()

) {
    Column(
        horizontalAlignment = Alignment.Start
    ) {
        OutlinedTextField(
            modifier = modifier
                .heightIn(min = 48.dp, max = 160.dp),
            value = text,
            placeholder = {
                Text(
                    hint,
                    style = Typography.bodyMedium,
                    color = hintColor,
                )
            },
            onValueChange = onValueChange,
            shape = RoundedCornerShape(radius),
            textStyle = Typography.bodyMedium.copy(color = Black),
            singleLine = isSingleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransform(keyboardType, showPassword),
            isError = isError,
            trailingIcon = {
                trailingPainter?.let {
                    IconButton(
                        onClick = onTrailingIconClick,
                        enabled = trailingIconEnabled,
                    ) {
                        Icon(
                            painter = trailingPainter,
                            contentDescription = "trailing icon",
                            tint = iconTint ?: Black.copy(alpha = 0.6f)
                        )
                    }
                }
            },
            colors = outlinedTextFieldDefaults,
            readOnly = readOnly
        )
        AnimatedVisibility(isError) {
            Text(
                text = errorMessage,
                modifier = Modifier.padding(top = 8.dp),
                style = Typography.bodyLarge,
                color = Color.Red.copy(alpha = 0.6f)
            )
        }
    }

}

@Composable
fun outlinedTextFieldColorDefaults() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Black.copy(0.2f),
    unfocusedBorderColor = Black.copy(0.3f),
)

@Composable
private fun visualTransform(
    keyboardType: KeyboardType,
    showPassword: Boolean
): VisualTransformation {
    return if (showPassword || keyboardType != KeyboardType.Password && keyboardType != KeyboardType.NumberPassword) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation()
    }
}