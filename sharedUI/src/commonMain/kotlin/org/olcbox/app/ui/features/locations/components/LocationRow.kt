package org.olcbox.app.ui.features.locations.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.olcbox.app.data.model.LocationConfig
import org.olcbox.app.ui.features.locations.LocationItem
import org.olcbox.app.util.parseEmojiAndName

@Composable
fun LocationRow(location: LocationItem, isSelected: Boolean, isLoading: Boolean, pingMs: Int?, isError: Boolean = false, settingsEnabled: Boolean = true, onSettingsClick: () -> Unit = {}, onClick: () -> Unit) {
    val metadata = location.metadata
    val rawName = metadata?.name?.takeIf { it.isNotBlank() } ?: location.fullName
    val fallbackIcon = metadata?.icon?.takeIf { it.isNotBlank() } ?: metadata?.subscription?.icon?.takeIf { it.isNotBlank() } ?: ""
    val (emoji, parsedName) = parseEmojiAndName(rawName, fallbackIcon)
    val cleanName = parsedName.ifBlank { location.config?.displayName().orEmpty() }
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().height(76.dp).clip(RoundedCornerShape(16.dp)).padding(horizontal = 20.dp, vertical = 8.dp)) {
        if (emoji.isNotEmpty()) { Text(text = emoji, fontSize = 20.sp); Spacer(modifier = Modifier.width(8.dp)) }
        Column(modifier = Modifier.weight(1f)) {
            Text(text = cleanName, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Text(text = locationSubtitle(location), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        if (pingMs != null) Text(text = "$pingMs ms", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (isError) Text(text = "Offline", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.width(8.dp))
        if (settingsEnabled) { IconButton(onClick = onSettingsClick, modifier = Modifier.size(40.dp)) { Icon(Icons.Default.MoreVert, contentDescription = "Settings", tint = MaterialTheme.colorScheme.onSurfaceVariant) } }
        Spacer(modifier = Modifier.width(8.dp))
        if (isSelected) { Icon(Icons.Rounded.CheckCircle, contentDescription = "Selected location", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp)) } else { Surface(modifier = Modifier.size(24.dp), shape = CircleShape, color = Color.Transparent) { Box(modifier = Modifier.size(24.dp)) } }
    }
}

private fun locationSubtitle(location: LocationItem): String {
    val config = location.config
    val metadata = location.metadata
    val providerName = config?.providerName() ?: LocationConfig.providerDisplayName(LocationConfig.DEFAULT_BYPASS_PROVIDER)
    val transportName = config?.transportName() ?: LocationConfig.transportDisplayName(LocationConfig.DEFAULT_TRANSPORT)
    return listOfNotNull(providerName, transportName, metadata?.comment?.takeIf { it.isNotBlank() }).joinToString(" · ")
}
