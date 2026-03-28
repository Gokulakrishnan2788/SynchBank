package com.architect.banking.feature.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.architect.banking.core.ui.theme.ArchitectColors
import com.architect.banking.core.ui.theme.ArchitectTypography
import com.architect.banking.engine.navigation.NavigationEngine
import com.architect.banking.engine.sdui.renderer.SDUIRenderer
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var dialogMessage by rememberSaveable { mutableStateOf<String?>(null) }
    var showImagePickerSheet by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Temp URI for camera capture
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    // Camera launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraImageUri?.let { uri ->
                viewModel.handleIntent(ProfileIntent.UpdateProfileImage(uri.toString()))
            }
        }
        scope.launch { sheetState.hide() }
        showImagePickerSheet = false
    }

    // Gallery launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.handleIntent(ProfileIntent.UpdateProfileImage(it.toString())) }
        scope.launch { sheetState.hide() }
        showImagePickerSheet = false
    }

    // Permission launcher — only called when camera isn't yet granted
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) showImagePickerSheet = true
    }

    fun onEditProfile() {
        val cameraGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
        if (cameraGranted) {
            showImagePickerSheet = true
        } else {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProfileEffect.Navigate ->
                    NavigationEngine.navigate(navController, effect.action)
                is ProfileEffect.ShowDialog ->
                    dialogMessage = effect.message
                is ProfileEffect.LaunchLanguageSettings -> {
                    context.startActivity(
                        Intent(Settings.ACTION_LOCALE_SETTINGS)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
                is ProfileEffect.LaunchDisplaySettings -> {
                    context.startActivity(
                        Intent(Settings.ACTION_DISPLAY_SETTINGS)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
            }
        }
    }

    dialogMessage?.let { message ->
        AlertDialog(
            onDismissRequest = { dialogMessage = null },
            text = { Text(text = message) },
            confirmButton = {
                TextButton(onClick = { dialogMessage = null }) { Text(text = "OK") }
            },
        )
    }

    SDUIRenderer(
        screenModel = state.screenModel,
        onAction = { actionId ->
            if (actionId == "EDIT_PROFILE") {
                onEditProfile()
            } else {
                viewModel.handleIntent(ProfileIntent.HandleAction(actionId))
            }
        },
        modifier = Modifier,
        error = state.error,
    )

    if (showImagePickerSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showImagePickerSheet = false
            },
            sheetState = sheetState,
            containerColor = ArchitectColors.White,
            modifier = Modifier.navigationBarsPadding(),
        ) {
            Column(modifier = Modifier.padding(bottom = 32.dp)) {
                Text(
                    text = "Update Profile Photo",
                    style = ArchitectTypography.Heading3.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = ArchitectColors.NavyPrimary,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                )
                HorizontalDivider(color = ArchitectColors.LightGray)

                Surface(
                    onClick = {
                        val tempFile = File.createTempFile("profile_", ".jpg", context.cacheDir)
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            tempFile,
                        )
                        cameraImageUri = uri
                        cameraLauncher.launch(uri)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    color = ArchitectColors.White,
                ) {
                    ListItem(
                        headlineContent = {
                            Text(
                                "Open Camera",
                                style = ArchitectTypography.Body,
                                color = ArchitectColors.NavyPrimary,
                            )
                        },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = ArchitectColors.TealAccent,
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = ArchitectColors.White),
                    )
                }

                HorizontalDivider(
                    color = ArchitectColors.LightGray,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                Surface(
                    onClick = {
                        galleryLauncher.launch(
                            androidx.activity.result.PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    color = ArchitectColors.White,
                ) {
                    ListItem(
                        headlineContent = {
                            Text(
                                "Choose from Gallery",
                                style = ArchitectTypography.Body,
                                color = ArchitectColors.NavyPrimary,
                            )
                        },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.Photo,
                                contentDescription = null,
                                tint = ArchitectColors.TealAccent,
                            )
                        },
                        colors = ListItemDefaults.colors(containerColor = ArchitectColors.White),
                    )
                }
            }
        }
    }
}
