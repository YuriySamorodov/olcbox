package org.olcbox.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import org.olcbox.app.data.share.SubscriptionShareItem
import org.olcbox.app.update.AppUpdateInfo
import org.olcbox.app.update.AppUpdateSettings

@Serializable
data class ApplicationSocksProxySettings(
    val host: String = "127.0.0.1",
    val port: Int = DEFAULT_PORT,
    val username: String = "",
    val password: String = ""
) {
    companion object {
        const val DEFAULT_PORT = 10808
        const val MIN_PORT = 1024
        const val MAX_PORT = 65535
        const val MAX_CREDENTIAL_LENGTH = 64

        fun isValidPort(port: Int): Boolean = port in MIN_PORT..MAX_PORT
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationSettingsSheet(
    updateSettings: AppUpdateSettings,
    updateStatusText: String?,
    updateDownloadProgress: Float?,
    updateOffer: AppUpdateInfo?,
    subscriptions: List<SubscriptionShareItem>,
    logs: List<String>,
    connectionSummary: String,
    connectionDetails: List<Pair<String, String>>,
    socksProxySettings: ApplicationSocksProxySettings? = null,
    isConnectionActive: Boolean = false,
    onDismiss: () -> Unit,
    onCopyConfigClick: () -> Unit,
    onSaveLogsClick: () -> Unit,
    onShareLogsClick: () -> Unit,
    onUpdateIntervalSelected: (Int) -> Unit,
    onCheckUpdatesClick: () -> Unit,
    onDownloadUpdateClick: (AppUpdateInfo) -> Unit,
    onLaterUpdateClick: (AppUpdateInfo) -> Unit,
    onSubscriptionShareClick: (String) -> Unit,
    onSubscriptionRefreshClick: (String) -> Unit,
    onSocksProxySettingsSaved: (String, String, Int) -> Unit = { _, _, _ -> },
    onSocksProxyPasswordRegenerated: () -> Unit = {}
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Application settings")

            SectionCard(title = "Connection") {
                Text(connectionSummary)
                connectionDetails.forEach { (label, value) ->
                    Text("$label: $value")
                }
                Spacer(Modifier.height(4.dp))
                Button(onClick = onCopyConfigClick) {
                    Text("Copy config")
                }
            }

            SectionCard(title = "Updates") {
                Text("Channel: ${updateSettings.channel}")
                Text("Check every ${updateSettings.intervalHours}h")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AppUpdateSettings.INTERVAL_PRESETS.forEach { interval ->
                        val selected = interval == updateSettings.intervalHours
                        if (selected) {
                            Button(onClick = { onUpdateIntervalSelected(interval) }) {
                                Text("${interval}h")
                            }
                        } else {
                            OutlinedButton(onClick = { onUpdateIntervalSelected(interval) }) {
                                Text("${interval}h")
                            }
                        }
                    }
                }
                updateStatusText?.let { Text(it) }
                if (updateDownloadProgress != null) {
                    Text("Download progress: ${(updateDownloadProgress * 100).toInt()}%")
                }
                Button(onClick = onCheckUpdatesClick) {
                    Text("Check updates")
                }
                updateOffer?.let { offer ->
                    Text("Available: ${offer.version}")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { onDownloadUpdateClick(offer) }) { Text("Download") }
                        OutlinedButton(onClick = { onLaterUpdateClick(offer) }) { Text("Later") }
                    }
                }
            }

            SectionCard(title = "Subscriptions") {
                Text("${subscriptions.size} subscriptions")
                Button(onClick = onCopyConfigClick) {
                    Text("Copy subscription config")
                }
                Button(onClick = { subscriptions.firstOrNull()?.let { onSubscriptionShareClick(it.url) } }) {
                    Text("Share first subscription")
                }
                Button(onClick = { subscriptions.firstOrNull()?.let { onSubscriptionRefreshClick(it.url) } }) {
                    Text("Refresh first subscription")
                }
            }

            SectionCard(title = "Logs") {
                Text("${logs.size} log lines")
                Button(onClick = onSaveLogsClick) { Text("Save logs") }
                OutlinedButton(onClick = onShareLogsClick) { Text("Share logs") }
                logs.take(3).forEach { logLine ->
                    Text(logLine)
                }
            }

            if (socksProxySettings != null) {
                SectionCard(title = "SOCKS proxy") {
                    Text("${socksProxySettings.host}:${socksProxySettings.port}")
                    Text("User: ${socksProxySettings.username.ifBlank { "<none>" }}")
                    Text("Connection active: $isConnectionActive")
                    Button(
                        onClick = {
                            onSocksProxySettingsSaved(
                                socksProxySettings.host,
                                socksProxySettings.password,
                                socksProxySettings.port
                            )
                        }
                    ) {
                        Text("Save proxy settings")
                    }
                    OutlinedButton(onClick = onSocksProxyPasswordRegenerated) {
                        Text("Regenerate password")
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable () -> Unit
) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(title)
            HorizontalDivider()
            content()
        }
    }
}