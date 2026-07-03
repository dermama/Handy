package com.edge.integrated.ui.terminal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.edge.integrated.ui.common.SectionHeader
import com.edge.integrated.ui.theme.TerminalBackground
import com.edge.integrated.ui.theme.TerminalGreen

private data class TerminalLine(
    val text: String,
    val isCommand: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen() {
    val lines = remember { mutableStateListOf<TerminalLine>() }
    val sessions = remember { mutableStateListOf("main") }
    var activeSession by remember { mutableStateOf("main") }
    var commandInput by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Terminal") },
            actions = {
                IconButton(onClick = {
                    val id = "session_${sessions.size}"
                    sessions.add(id)
                }) {
                    Icon(Icons.Default.Add, contentDescription = "New Session")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(TerminalBackground)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(lines) { line ->
                    Text(
                        text = line.text,
                        color = if (line.isCommand) TerminalGreen else Color.Gray,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF111122))
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$ ",
                color = TerminalGreen,
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp
            )
            OutlinedTextField(
                value = commandInput,
                onValueChange = { commandInput = it },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        "Enter command...",
                        color = Color.Gray,
                        fontFamily = FontFamily.Monospace
                    )
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontFamily = FontFamily.Monospace
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    cursorColor = TerminalGreen
                ),
                singleLine = true
            )
            IconButton(
                onClick = {
                    if (commandInput.isNotBlank()) {
                        lines.add(TerminalLine("$ $commandInput", isCommand = true))
                        lines.add(TerminalLine("[Executing in Termux environment...]"))
                        commandInput = ""
                    }
                }
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Run",
                    tint = TerminalGreen
                )
            }
        }
    }
}
