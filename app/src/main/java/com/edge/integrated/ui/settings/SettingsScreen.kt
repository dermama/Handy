package com.edge.integrated.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.WineBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edge.integrated.ui.common.InfoRow
import com.edge.integrated.ui.common.SectionHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var opencodePort by remember { mutableStateOf("8080") }
    var autoStartServer by remember { mutableStateOf(true) }
    var useLocalModelsFirst by remember { mutableStateOf(true) }
    var terminalFontSize by remember { mutableFloatStateOf(13f) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Settings") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Termux Settings
            SectionHeader(title = "Termux")
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
                                Icons.Default.Terminal,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Font Size: ${terminalFontSize.toInt()}sp",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    Slider(
                        value = terminalFontSize,
                        onValueChange = { terminalFontSize = it },
                        valueRange = 8f..24f,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Winlator Settings
            SectionHeader(title = "Winlator")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow("Wine Version", "wine-9.0 (built-in)")
                    InfoRow("Box86/64 Preset", "Intermediate")
                    InfoRow("VirGL Support", "Enabled")
                    InfoRow("DXVK Version", "v2.5.1")
                }
            }

            // OpenCode Settings
            SectionHeader(title = "OpenCode AI")
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
                        Text("Server Port")
                        Text(
                            text = opencodePort,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Auto-start server")
                        Switch(
                            checked = autoStartServer,
                            onCheckedChange = { autoStartServer = it }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Use local models first")
                        Switch(
                            checked = useLocalModelsFirst,
                            onCheckedChange = { useLocalModelsFirst = it }
                        )
                    }
                }
            }

            // Storage
            SectionHeader(title = "Storage")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow("Models", "128 MB")
                    InfoRow("Termux RootFS", "256 MB")
                    InfoRow("Winlator Containers", "0 B")
                    InfoRow("Total", "384 MB")
                }
            }

            // About
            SectionHeader(title = "About")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoRow("Version", "1.0.0-alpha1")
                    InfoRow("Edge Gallery", "AI Edge (Google)")
                    InfoRow("Termux", "0.118.0")
                    InfoRow("Winlator", "11.0.0")
                    InfoRow("OpenCode", "v1.17.13")
                }
            }
        }
    }
}
