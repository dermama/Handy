package com.edge.integrated.ui.winlator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.edge.integrated.model.ContainerInfo
import com.edge.integrated.model.ContainerStatus
import com.edge.integrated.ui.common.EmptyState
import com.edge.integrated.ui.common.InfoRow
import com.edge.integrated.ui.common.StatusIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WinlatorScreen() {
    val containers = emptyList<ContainerInfo>()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Winlator") },
            actions = {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Add, contentDescription = "New Container")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        if (containers.isEmpty()) {
            EmptyState(
                title = "No Containers",
                subtitle = "Create a container to run Windows applications",
                actionLabel = "Create Container",
                onAction = { }
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
            ) {
                items(containers) { container ->
                    ContainerCard(container = container)
                }
            }
        }
    }
}

@Composable
private fun ContainerCard(
    container: ContainerInfo
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Android,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = container.name,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = container.wineVersion,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                StatusIndicator(
                    isActive = container.status == ContainerStatus.RUNNING
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            InfoRow("Box Preset", container.boxPreset.name)
            InfoRow("Resolution", container.resolution)
            InfoRow("DXVK", if (container.dxvkEnabled) "Enabled" else "Disabled")

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (container.status == ContainerStatus.RUNNING) {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Stop, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Stop")
                    }
                } else {
                    Button(onClick = { }) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Launch")
                    }
                }
            }
        }
    }
}
