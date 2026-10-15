package com.example.iremember.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.iremember.ui.theme.Dimensions
import com.example.iremember.ui.theme.Spacing
import com.example.iremember.util.ReminderTimeUtils
import com.example.iremember.util.formatDate
import com.example.iremember.util.formatDateTime
import java.util.Calendar

/**
 * Remind Me card — toggle with Date and Time pickers.
 */
@Composable
fun RemindMeCard(
    enabled: Boolean,
    hour: Int,
    minute: Int,
    nextAt: Long = 0L,
    isActive: Boolean = false,
    showActions: Boolean = false,
    onToggle: (Boolean) -> Unit,
    onTimePicked: (hour: Int, minute: Int) -> Unit = { _, _ -> },
    onScheduleChanged: (hour: Int, minute: Int, targetMillis: Long) -> Unit = { _, _, _ -> },
    onEnable: () -> Unit = {},
    onSnooze: () -> Unit = {},
    onStop: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val initialCalendar = remember(nextAt, hour, minute) {
        Calendar.getInstance().apply {
            if (nextAt > System.currentTimeMillis()) {
                timeInMillis = nextAt
            } else {
                set(Calendar.HOUR_OF_DAY, hour.coerceIn(0, 23))
                set(Calendar.MINUTE, minute.coerceIn(0, 59))
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                if (timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }
        }
    }

    var localYear by remember(nextAt, hour, minute) { mutableIntStateOf(initialCalendar.get(Calendar.YEAR)) }
    var localMonth by remember(nextAt, hour, minute) { mutableIntStateOf(initialCalendar.get(Calendar.MONTH)) }
    var localDay by remember(nextAt, hour, minute) { mutableIntStateOf(initialCalendar.get(Calendar.DAY_OF_MONTH)) }
    var localHour by remember(nextAt, hour, minute) { mutableIntStateOf(initialCalendar.get(Calendar.HOUR_OF_DAY)) }
    var localMinute by remember(nextAt, hour, minute) { mutableIntStateOf(initialCalendar.get(Calendar.MINUTE)) }
    var pendingEnable by remember { mutableStateOf(false) }

    fun currentCalculatedMillis(): Long {
        return ReminderTimeUtils.buildWallClockMillis(localYear, localMonth, localDay, localHour, localMinute)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted && pendingEnable) {
            pendingEnable = false
            onToggle(true)
            onEnable()
        } else {
            pendingEnable = false
        }
    }

    fun requestEnable() {
        if (Build.VERSION.SDK_INT >= 33) {
            pendingEnable = true
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        } else {
            onToggle(true)
            onEnable()
        }
    }

    fun openDatePicker() {
        val dpd = DatePickerDialog(
            context,
            { _, y, m, d ->
                localYear = y
                localMonth = m
                localDay = d
                val millis = currentCalculatedMillis()
                onScheduleChanged(localHour, localMinute, millis)
                onTimePicked(localHour, localMinute)
                if (enabled || isActive) {
                    onEnable()
                }
            },
            localYear,
            localMonth,
            localDay
        )
        dpd.datePicker.minDate = System.currentTimeMillis() - 1000
        dpd.show()
    }

    fun openTimePicker() {
        TimePickerDialog(
            context,
            { _, h, m ->
                localHour = h
                localMinute = m
                val millis = currentCalculatedMillis()
                onScheduleChanged(localHour, localMinute, millis)
                onTimePicked(localHour, localMinute)
                if (enabled || isActive) {
                    onEnable()
                }
            },
            localHour,
            localMinute,
            false
        ).show()
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimensions.cardCornerRadius),
        elevation = CardDefaults.cardElevation(defaultElevation = Dimensions.cardElevation),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.lg)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.md),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Alarm,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Column {
                        Text(
                            text = "Remind Me",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Set date & time alert notification",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Switch(
                    checked = enabled || isActive,
                    onCheckedChange = { checked ->
                        if (checked) {
                            requestEnable()
                        } else {
                            onToggle(false)
                            onStop()
                        }
                    }
                )
            }

            if (enabled || isActive) {
                Spacer(Modifier.height(Spacing.md))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                ) {
                    OutlinedButton(
                        onClick = { openDatePicker() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = Spacing.xs)
                                .size(18.dp)
                        )
                        Text(
                            text = formatDate(currentCalculatedMillis()),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    OutlinedButton(
                        onClick = { openTimePicker() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = Spacing.xs)
                                .size(18.dp)
                        )
                        Text(
                            text = ReminderTimeUtils.formatTime(localHour, localMinute),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Spacer(Modifier.height(Spacing.xs))
                Text(
                    text = "Scheduled: ${formatDateTime(currentCalculatedMillis())}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = Spacing.xs)
                )
                if (showActions && isActive) {
                    Spacer(Modifier.height(Spacing.md))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Spacing.sm)
                    ) {
                        OutlinedButton(
                            onClick = onSnooze,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Snooze 1h")
                        }
                        Button(
                            onClick = onStop,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Stop")
                        }
                    }
                }
            }
        }
    }
}
