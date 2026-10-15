package com.example.iremember.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.iremember.ui.components.PremiumScaffold
import com.example.iremember.ui.components.ProfileAvatar
import com.example.iremember.ui.components.ProfileUpdatedSuccessDialog
import com.example.iremember.ui.components.TabMenuHeader
import com.example.iremember.ui.theme.Dimensions
import com.example.iremember.ui.theme.Spacing
import com.example.iremember.ui.theme.ThemeMode
import com.example.iremember.ui.theme.darkCardGradientFill
import com.example.iremember.ui.theme.darkSurfaceBorder
import com.example.iremember.ui.theme.isAppDarkTheme
import com.example.iremember.ui.theme.mainTabBottomScrollPadding
import com.example.iremember.viewmodel.ItemViewModel

/** Soft accent tints for settings/permissions rows (not brand-green only). */
private object ProfileIconAccents {
    val Appearance = Color(0xFFFBBF24)
    val Password = Color(0xFF60A5FA)
    val Notifications = Color(0xFFF59E0B)
    val Biometrics = Color(0xFF2DD4BF)
    val Camera = Color(0xFFA78BFA)
    val AppSettings = Color(0xFF94A3B8)
    val About = Color(0xFF818CF8)
}
data class ProfileDetailsUpdate(
    val displayName: String,
    val username: String,
    val bio: String,
    val fullName: String,
    val birthday: String,
    val family: String,
    val phone: String,
    val location: String
)

@Composable
fun ProfileScreen(
    viewModel: ItemViewModel,
    themeMode: ThemeMode = ThemeMode.Auto,
    onThemeModeChanged: (ThemeMode) -> Unit = {},
    profileImageUri: String = "",
    onProfileImageChanged: (String) -> Unit = {},
    showProfilePhotoUpdated: Boolean = false,
    onProfilePhotoUpdatedDismissed: () -> Unit = {},
    displayName: String = "IRemember User",
    username: String = "iremember_user",
    bio: String = "Keeping everyday essentials organized and easy to find.",
    fullName: String = "",
    birthday: String = "",
    family: String = "",
    phone: String = "",
    location: String = "",
    onEditProfileClick: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onPermissionsClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    onUserInteraction: () -> Unit = {},
    bottomNavVisible: Boolean = true,
    scrollToSettings: Boolean = false,
    onScrollToSettingsConsumed: () -> Unit = {},
    onTotalItemsClick: () -> Unit = {},
    onCategoriesClick: () -> Unit = {},
    onRecentClick: () -> Unit = {}
) {
    val bottomScrollPadding = mainTabBottomScrollPadding(bottomNavVisible)
    val scrollState = rememberScrollState()

    LaunchedEffect(scrollToSettings) {
        if (scrollToSettings) {
            scrollState.animateScrollTo(scrollState.maxValue)
            onScrollToSettingsConsumed()
        }
    }

    if (showProfilePhotoUpdated) {
        ProfileUpdatedSuccessDialog(onDone = onProfilePhotoUpdatedDismissed)
    }

    PremiumScaffold(
        onUserInteraction = onUserInteraction,
        headerHeight = Dimensions.headerContentWithMenu,
        headerContent = { collapseFraction ->
            TabMenuHeader(
                title = "My Profile",
                subtitle = "Complete your IRemember identity",
                onMenuClick = onMenuClick,
                collapseFraction = collapseFraction
            )
        }
    ) { scrollModifier ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .then(scrollModifier)
                    .verticalScroll(scrollState)
                    .padding(
                        start = Spacing.xl,
                        end = Spacing.xl,
                        top = Spacing.xl,
                        bottom = bottomScrollPadding
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(Spacing.lg)
            ) {
                ProfileCard(
                    profileImageUri = profileImageUri,
                    onProfileImageChanged = onProfileImageChanged,
                    displayName = displayName,
                    username = username,
                    bio = bio,
                    onEditClick = onEditProfileClick
                )

                PersonalDetailsCard(
                    fullName = fullName,
                    birthday = birthday,
                    family = family,
                    phone = phone,
                    location = location
                )

                SettingsCard(
                    themeMode = themeMode,
                    onThemeModeChanged = onThemeModeChanged,
                    onChangePassword = onChangePassword,
                    onPermissionsClick = onPermissionsClick,
                    onLogout = onLogout
                )

                AboutCard(onClick = onAboutClick)

                Spacer(Modifier.height(Spacing.xl))
            }
    }
}

@Composable
private fun ProfileCard(
    profileImageUri: String,
    onProfileImageChanged: (String) -> Unit,
    displayName: String,
    username: String,
    bio: String,
    onEditClick: () -> Unit
) {
    val dark = isAppDarkTheme()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        border = if (dark) darkSurfaceBorder() else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (dark) 0.dp else Dimensions.cardElevation
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (dark) Color.Transparent else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .darkCardGradientFill(dark)
                .padding(Spacing.xl),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileAvatar(
                imageUri = profileImageUri,
                onImageSelected = onProfileImageChanged,
                size = 92.dp,
                editable = true
            )
            Spacer(Modifier.height(Spacing.md))
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "@$username",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = Spacing.xxs)
            )
            Text(
                text = bio,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = Spacing.sm)
            )
            Spacer(Modifier.height(Spacing.lg))
            Button(
                onClick = onEditClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    modifier = Modifier.padding(end = Spacing.sm)
                )
                Text("Edit Profile")
            }
        }
    }
}

@Composable
private fun PersonalDetailsCard(
    fullName: String,
    birthday: String,
    family: String,
    phone: String,
    location: String
) {
    val dark = isAppDarkTheme()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        border = if (dark) darkSurfaceBorder() else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (dark) 0.dp else Dimensions.cardElevation
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (dark) Color.Transparent else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .darkCardGradientFill(dark)
                .padding(Spacing.xl)
        ) {
            Text(
                text = "Personal details",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(Spacing.md))
            DetailRow("Full name", fullName)
            DetailRow("Birthday", birthday)
            DetailRow("Family", family)
            DetailRow("Phone", phone)
            DetailRow("Location", location, showDivider = false)
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, showDivider: Boolean = true) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value.ifBlank { "Not set" },
            style = MaterialTheme.typography.bodyLarge,
            color = if (value.isBlank()) {
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.padding(top = 2.dp, bottom = Spacing.md)
        )
        if (showDivider) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
            Spacer(Modifier.height(Spacing.md))
        }
    }
}

@Composable
private fun SettingsCard(
    themeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    onChangePassword: () -> Unit,
    onPermissionsClick: () -> Unit,
    onLogout: () -> Unit
) {
    var themeMenuExpanded by remember { mutableStateOf(false) }
    val dark = isAppDarkTheme()
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        border = if (dark) darkSurfaceBorder() else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (dark) 0.dp else Dimensions.cardElevation
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (dark) Color.Transparent else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .darkCardGradientFill(dark)
                .padding(Spacing.xl)
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(Spacing.md))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { themeMenuExpanded = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = themeModeIcon(themeMode),
                    contentDescription = null,
                    tint = ProfileIconAccents.Appearance,
                    modifier = Modifier.padding(end = Spacing.md)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Appearance",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        when (themeMode) {
                            ThemeMode.Dark -> "Dark"
                            ThemeMode.Light -> "Light"
                            ThemeMode.Auto -> "Auto · following system"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box {
                    TextButton(onClick = { themeMenuExpanded = true }) {
                        Text(themeMode.name)
                    }
                    DropdownMenu(
                        expanded = themeMenuExpanded,
                        onDismissRequest = { themeMenuExpanded = false }
                    ) {
                        ThemeMode.entries.forEach { mode ->
                            DropdownMenuItem(
                                text = { Text(mode.name) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = themeModeIcon(mode),
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    onThemeModeChanged(mode)
                                    themeMenuExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(Spacing.lg))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            Spacer(Modifier.height(Spacing.md))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onChangePassword)
                    .padding(vertical = Spacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = ProfileIconAccents.Password,
                    modifier = Modifier.padding(end = Spacing.md)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Change Password",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Update your account password",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(Spacing.md))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            Spacer(Modifier.height(Spacing.md))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onPermissionsClick)
                    .padding(vertical = Spacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = ProfileIconAccents.AppSettings,
                    modifier = Modifier.padding(end = Spacing.md)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Permissions",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Camera, notifications, and biometrics",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.height(Spacing.md))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            Spacer(Modifier.height(Spacing.lg))
            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.padding(end = Spacing.sm)
                )
                Text("Sign Out")
            }
        }
    }
}

private fun themeModeIcon(mode: ThemeMode) = when (mode) {
    ThemeMode.Light -> Icons.Default.LightMode
    ThemeMode.Dark -> Icons.Default.DarkMode
    ThemeMode.Auto -> Icons.Default.BrightnessAuto
}



@Composable
private fun AboutCard(onClick: () -> Unit) {
    val dark = isAppDarkTheme()
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        border = if (dark) darkSurfaceBorder() else null,
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (dark) 0.dp else Dimensions.cardElevation
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (dark) Color.Transparent else MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .darkCardGradientFill(dark)
                .padding(Spacing.xl)
        ) {
            Text(
                text = "About Application",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(Spacing.md))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))
            Spacer(Modifier.height(Spacing.md))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = ProfileIconAccents.About,
                    modifier = Modifier.padding(end = Spacing.md)
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "IRemember",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "Version, privacy, and local data notice",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
