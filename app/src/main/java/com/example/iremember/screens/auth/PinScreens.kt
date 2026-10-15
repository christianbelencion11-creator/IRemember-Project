package com.example.iremember.screens.auth

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.iremember.ui.components.AuthShell
import com.example.iremember.ui.components.PinDots
import com.example.iremember.ui.components.PinKeypad
import com.example.iremember.ui.theme.Spacing
import com.example.iremember.util.AuthPreferences

private const val PIN_LENGTH = 6

@Composable
fun SetupPinScreen(
    authPreferences: AuthPreferences,
    onComplete: () -> Unit
) {
    var step by remember { mutableIntStateOf(0) }
    var firstPin by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    fun resetToCreate(keepError: String? = null) {
        step = 0
        firstPin = ""
        pin = ""
        error = keepError
    }

    AuthShell(
        title = when (step) {
            0 -> "Create your MPIN"
            else -> "Confirm your MPIN"
        },
        subtitle = when (step) {
            0 -> "Set a 6-digit PIN to secure your account"
            else -> "Enter the same MPIN again to confirm"
        },
        centerContent = true,
        contentTopPadding = 276.dp
    ) {
        PinDots(enteredLength = pin.length, pinLength = PIN_LENGTH)
        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = Spacing.sm)
            )
        }
        Spacer(Modifier.height(Spacing.lg))
        PinKeypad(
            onDigit = { digit ->
                error = null
                if (pin.length < PIN_LENGTH) {
                    pin += digit
                    if (pin.length == PIN_LENGTH) {
                        if (step == 0) {
                            firstPin = pin
                            pin = ""
                            step = 1
                        } else if (pin == firstPin) {
                            authPreferences.setPin(pin)
                            onComplete()
                        } else {
                            resetToCreate("PINs do not match. Start over and try again.")
                        }
                    }
                }
            },
            onBackspace = {
                if (pin.isNotEmpty()) pin = pin.dropLast(1)
            },
            onCancel = if (step == 1) {
                { resetToCreate() }
            } else {
                null
            }
        )
        if (step == 1) {
            Spacer(Modifier.height(Spacing.sm))
            TextButton(
                onClick = { resetToCreate() },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Start over")
            }
        }
    }
}

@Composable
fun UnlockScreen(
    authPreferences: AuthPreferences,
    onUnlocked: () -> Unit,
    onUsePassword: () -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    AuthShell(
        title = "Enter your MPIN",
        subtitle = authPreferences.getUsername().ifBlank { "Enter your MPIN" },
        centerContent = true,
        contentTopPadding = 276.dp
    ) {
        PinDots(enteredLength = pin.length, pinLength = PIN_LENGTH)
        error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = Spacing.sm)
            )
        }
        Spacer(Modifier.height(Spacing.lg))
        PinKeypad(
            onDigit = { digit ->
                error = null
                if (pin.length < PIN_LENGTH) {
                    pin += digit
                    if (pin.length == PIN_LENGTH) {
                        if (authPreferences.verifyPin(pin)) {
                            onUnlocked()
                        } else {
                            error = "Incorrect PIN. Try again."
                            pin = ""
                        }
                    }
                }
            },
            onBackspace = {
                if (pin.isNotEmpty()) pin = pin.dropLast(1)
            }
        )
        Spacer(Modifier.height(Spacing.sm))
        TextButton(
            onClick = {
                authPreferences.logout()
                onUsePassword()
            },
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Sign in with password")
        }
    }
}
