package org.queststudios.projecttask

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import objects.tasks.Task
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.sql.Date
import java.sql.Time

@Composable
@Preview
fun App() {
    var tasks by remember { mutableStateOf(mutableListOf<Task>()) }
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { showContent = !showContent }) {
                Text("Add Task")
            }
            AnimatedVisibility(showContent) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = "",
                        onValueChange = {},
                        label = { Text("Task Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(onClick = {
                        tasks.add(
                            Task(
                                "New Task",
                                "Test",
                                Date.valueOf("2025-07-25"),
                                Time.valueOf("15:00:00")
                            )
                        )
                    }) {
                        Text("Add")
                    }
                }
            }
        }
    }
}