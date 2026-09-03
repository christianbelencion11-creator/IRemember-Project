package com.example.iremember.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.iremember.ui.theme.Spacing
import com.example.iremember.ui.theme.ThemeMode
import com.example.iremember.ui.theme.gradientEndColor
import com.example.iremember.ui.theme.gradientStartColor

@Composable
fun IRememberNavigationDrawerContent(
    displayName: String,
    username: String = "",
    profileImageUri: String,
    themeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    onCategories: () -> Unit,
    onNotes: () -> Unit,
    onActivityLog: () -> Unit,
    onAppSettings: () -> Unit,
    onPermissions: () -> Unit,
    onHelpFeedback: () -> Unit,
    onAboutApp: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    val headerBrush = Brush.linearGradient(
        listOf(gradientStartColor(), gradientEndColor())
    )
    val handle = username.trim().removePrefix("@").ifBlank {
        displayName.trim().lowercase().replace(" ", "")
    }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .fillMaxWidth(0.85f)
            .clip(RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp))
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Centered Profile Header matching Image 5
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerBrush)
        ) {
            HeaderAtmosphere(modifier = Modifier.matchParentSize())

            // Theme toggle at top-right
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = Spacing.sm, end = Spacing.md),
                contentAlignment = Alignment.TopEnd
            ) {
                ThemeModeHeaderControl(
                    themeMode = themeMode,
                    onThemeModeChanged = onThemeModeChanged
                )
            }

            // Centered Avatar, Name, and Email/handle
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(top = Spacing.md, bottom = Spacing.xl, start = Spacing.xl, end = Spacing.xl),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileAvatar(
                    imageUri = profileImageUri,
                    onImageSelected = {},
                    size = 72.dp,
                    editable = false
                )
                Spacer(modifier = Modifier.height(Spacing.sm))
                Text(
                    text = displayName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = if (username.contains("@")) username else "@$handle",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.88f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        // Menu items grouped by sections matching Image 5
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(vertical = Spacing.md)
        ) {
            // Section 1: Manage Content
            DrawerSectionHeader(title = "Manage Content")
            DrawerMenuRow(
                icon = Icons.Outlined.Folder,
                title = "Categories",
                subtitle = "Manage folders, tags, and locations",
                onClick = onCategories
            )
            DrawerMenuRow(
                icon = Icons.AutoMirrored.Filled.Notes,
                title = "Notes & Lists",
                subtitle = "Your saved notes and item lists",
                onClick = onNotes
            )
            DrawerMenuRow(
                icon = Icons.Outlined.History,
                title = "Activity Log",
                subtitle = "Audit of found, edited, and deleted items",
                onClick = onActivityLog
            )

            DrawerSectionDivider()

            // Section 2: System & Settings
            DrawerSectionHeader(title = "System & Settings")
            DrawerMenuRow(
                icon = Icons.Default.Settings,
                title = "App Settings",
                subtitle = "Preferences, notifications, and theme",
                onClick = onAppSettings
            )
            DrawerMenuRow(
                icon = Icons.Default.Security,
                title = "Permissions",
                subtitle = "Camera, notifications, and storage",
                onClick = onPermissions
            )

            DrawerSectionDivider()

            // Section 3: Information & Support
            DrawerSectionHeader(title = "Information & Support")
            DrawerMenuRow(
                icon = Icons.AutoMirrored.Outlined.HelpOutline,
                title = "Help & Feedback",
                subtitle = "User guide and contact support",
                onClick = onHelpFeedback
            )
            DrawerMenuRow(
                icon = Icons.Outlined.Info,
                title = "About App",
                subtitle = "Version, terms of service, and privacy policy",
                onClick = onAboutApp
            )
        }

        // Bottom Sign Out button matching Image 5
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = Spacing.xl, vertical = Spacing.md)
        ) {
            Button(
                onClick = onSignOut,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = Spacing.sm)
                        .size(20.dp)
                )
                Text(
                    text = "Sign Out",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun DrawerSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
        modifier = Modifier.padding(horizontal = Spacing.xl, vertical = Spacing.xs)
    )
}

@Composable
private fun DrawerSectionDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = Spacing.xl, vertical = Spacing.sm),
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
    )
}

@Composable
private fun DrawerMenuRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.xl, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.md)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 1.dp)
                )
            }
        }
    }
}
