package com.skyd.anivu.ui.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContentPaste
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.skyd.anivu.R
import com.skyd.anivu.ui.local.LocalTextFieldStyle

enum class AniVuTextFieldStyle(val value: String) {
    Normal("Normal"),
    Outlined("Outlined");

    companion object {
        fun toEnum(value: String): AniVuTextFieldStyle {
            return if (value == Normal.value) Normal else Outlined
        }
    }
}

val DefaultTrailingIcon: @Composable () -> Unit = {}

@Composable
fun AniVuTextField(
    modifier: Modifier = Modifier,
    value: String,
    label: String = "",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    style: AniVuTextFieldStyle = AniVuTextFieldStyle.toEnum(LocalTextFieldStyle.current),
    autoRequestFocus: Boolean = true,
    onValueChange: (String) -> Unit,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    isPassword: Boolean = false,
    placeholder: String = "",
    trailingIcon: @Composable (() -> Unit)? = DefaultTrailingIcon,
    errorMessage: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    colors: TextFieldColors =
        if (style == AniVuTextFieldStyle.Normal) TextFieldDefaults.colors()
        else OutlinedTextFieldDefaults.colors(),
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(autoRequestFocus) {
        if (autoRequestFocus) focusRequester.requestFocus()
    }

    val newModifier =
        modifier.run { if (autoRequestFocus) focusRequester(focusRequester) else this }
    val newLabel: @Composable (() -> Unit)? =
        if (label.isBlank()) null
        else {
            { Text(label) }
        }
    val newOnValueChange: (String) -> Unit = { if (!readOnly) onValueChange(it) }
    val newVisualTransformation =
        if (isPassword && !showPassword) PasswordVisualTransformation() else visualTransformation
    val newPlaceholder: @Composable () -> Unit = {
        Text(
            text = placeholder,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
            style = MaterialTheme.typography.bodyMedium
        )
    }
    val newTrailingIcon: (@Composable () -> Unit)? = if (trailingIcon == DefaultTrailingIcon) {
        {
            if (value.isNotEmpty()) {
                AniVuIconButton(
                    imageVector = if (isPassword) {
                        if (showPassword) Icons.Rounded.Visibility
                        else Icons.Rounded.VisibilityOff
                    } else Icons.Rounded.Close,
                    contentDescription = if (isPassword) {
                        if (showPassword) stringResource(R.string.password_visibility_off)
                        else stringResource(R.string.password_visibility_on)
                    } else stringResource(R.string.clear_input_text),
                    onClick = {
                        if (isPassword) {
                            showPassword = !showPassword
                        } else if (!readOnly) {
                            onValueChange("")
                        }
                    }
                )
            } else {
                AniVuIconButton(
                    imageVector = Icons.Rounded.ContentPaste,
                    contentDescription = stringResource(R.string.paste),
                    tint = MaterialTheme.colorScheme.primary,
                    onClick = { onValueChange(clipboardManager.getText()?.text.orEmpty()) }
                )
            }
        }
    } else {
        trailingIcon
    }

    when (style) {
        AniVuTextFieldStyle.Normal -> TextField(
            modifier = newModifier,
            maxLines = maxLines,
            readOnly = readOnly,
            enabled = enabled,
            value = value,
            label = newLabel,
            onValueChange = newOnValueChange,
            visualTransformation = newVisualTransformation,
            placeholder = newPlaceholder,
            isError = errorMessage.isNotEmpty(),
            singleLine = maxLines == 1,
            trailingIcon = newTrailingIcon,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            colors = colors,
        )

        AniVuTextFieldStyle.Outlined -> OutlinedTextField(
            modifier = newModifier,
            maxLines = maxLines,
            readOnly = readOnly,
            enabled = enabled,
            value = value,
            label = newLabel,
            onValueChange = newOnValueChange,
            visualTransformation = newVisualTransformation,
            placeholder = newPlaceholder,
            isError = errorMessage.isNotEmpty(),
            singleLine = maxLines == 1,
            trailingIcon = newTrailingIcon,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            colors = colors,
        )
    }
}
