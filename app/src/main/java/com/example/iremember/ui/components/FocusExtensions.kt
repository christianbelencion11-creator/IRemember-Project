package com.example.iremember.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties

/**
 * Prevents left and right arrow keys from moving focus away from a text input field.
 * This ensures that on hardware keyboards, emulators, or desktop environments,
 * pressing arrow keys moves the cursor within the text rather than jumping focus
 * to another composable.
 */
@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
fun Modifier.preventArrowFocusEscape(cancelUpDown: Boolean = false): Modifier = this.focusProperties {
    left = FocusRequester.Cancel
    right = FocusRequester.Cancel
    if (cancelUpDown) {
        up = FocusRequester.Cancel
        down = FocusRequester.Cancel
    }
}
