package com.example.iremember.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

fun formatDateTime(timestamp: Long): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy 'at' h:mm a", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

fun formatRelativeTime(timestamp: Long, now: Long = System.currentTimeMillis()): String {
    if (timestamp <= 0L) return ""
    val diff = (now - timestamp).coerceAtLeast(0L)
    val minutes = diff / 60_000L
    val hours = diff / 3_600_000L
    val days = diff / 86_400_000L
    return when {
        minutes < 1L -> "Just now"
        minutes < 60L -> "${minutes}m ago"
        hours < 24L -> "${hours}h ago"
        days < 7L -> "${days}d ago"
        else -> formatDate(timestamp)
    }
}

/**
 * Formats the current date in uppercase format: "THURSDAY, SEPT 3".
 * September is formatted as "SEPT" matching the user's sample format.
 */
fun formatHeaderCalendarDate(now: Long = System.currentTimeMillis()): String {
    val date = Date(now)
    val dayOfWeek = SimpleDateFormat("EEEE", Locale.ENGLISH).format(date).uppercase(Locale.ENGLISH)
    val rawMonth = SimpleDateFormat("MMM", Locale.ENGLISH).format(date).uppercase(Locale.ENGLISH)
    val month = if (rawMonth == "SEP") "SEPT" else rawMonth
    val dayOfMonth = SimpleDateFormat("d", Locale.ENGLISH).format(date)
    return "$dayOfWeek, $month $dayOfMonth"
}


