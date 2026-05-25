package org.olcbox.app.ui.features.locations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.olcbox.app.data.model.LocationConfig
import org.olcbox.app.ui.components.PingButton
import org.olcbox.app.ui.features.home.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSettingsTopBar(shareEnabled: Boolean, onBack: () -> Unit, onShare: () -> Unit) {
    TopAppBar(
        title = { Text("Location settings") },
        navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") } },
        actions = { IconButton(onClick = onShare, enabled = shareEnabled) { Icon(Icons.Outlined.Share, contentDescription = "Share location") } }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSettingsScreen(viewModel: LocationViewModel, homeViewModel: HomeScreenViewModel, onShareLocationRequested: (LocationConfig) -> Unit = {}, onBack: () -> Unit) {
    val density = LocalDensity.current
    val isKeyboardVisible = WindowInsets.ime.getBottom(density) > 0
    Scaffold(topBar = { LocationSettingsTopBar(shareEnabled = true, onBack = onBack, onShare = { onShareLocationRequested(viewModel.editingConfig) }) }, bottomBar = { if (!isKeyboardVisible) { Column { HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant) } } }) { innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding).imePadding(), contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item { Text("Location settings placeholder") }
            item { PingButton(homeViewModel = homeViewModel) }
        }
    }
}
