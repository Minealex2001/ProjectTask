package org.queststudios.projecttask

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.ui.unit.dp
import objects.tasks.Task
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Paths
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlinx.serialization.builtins.ListSerializer
import androidx.compose.material3.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.queststudios.projecttask.TaskTrackerFloating
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import org.queststudios.projecttask.localization.Strings
import org.queststudios.projecttask.storage.saveTasksEncrypted
import org.queststudios.projecttask.storage.loadTasksEncrypted
import org.queststudios.projecttask.AddTaskScreen
import org.queststudios.projecttask.addTask

@Composable
@Preview
fun App() {
    var tasks by remember { mutableStateOf(loadTasksEncrypted().toMutableList()) }
    var maximized by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var taskTime by remember { mutableStateOf("") }
    var editingIndex by remember { mutableStateOf(-1) }
    var editingTime by remember { mutableStateOf("") }
    var showTimePicker by remember { mutableStateOf(false) }
    var showEditTimePicker by remember { mutableStateOf(false) }
    var editTimePickerIndex by remember { mutableStateOf(-1) }
    MaterialTheme {
        if (maximized) {
            Box(Modifier.fillMaxSize()) {
                // Popup para agregar tarea
                if (showContent) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.32f))
                    ) {}
                    Box(
                        Modifier
                            .align(Alignment.Center)
                            .background(Color.White, shape = MaterialTheme.shapes.medium)
                            .padding(32.dp)
                    ) {
                        AddTaskScreen(
                            taskName = taskName,
                            onTaskNameChange = { taskName = it },
                            taskDescription = taskDescription,
                            onTaskDescriptionChange = { taskDescription = it },
                            taskTime = taskTime,
                            onTaskTimeChange = { taskTime = it },
                            showTimePicker = showTimePicker,
                            onShowTimePickerChange = { showTimePicker = it },
                            onAddTask = {
                                val updated = addTask(tasks, taskName, taskDescription, if (taskTime.isNotBlank()) taskTime else null)
                                if (updated.size > tasks.size) {
                                    tasks = updated.toMutableList()
                                    taskName = ""
                                    taskDescription = ""
                                    taskTime = ""
                                    showContent = false
                                }
                            },
                            onCancel = { showContent = false }
                        )
                    }
                }
                // Lista de tareas con scroll y padding inferior para no tapar los botones flotantes
                Box(
                    Modifier
                        .fillMaxSize()
                        .align(Alignment.TopStart)
                        .padding(bottom = 120.dp) // deja espacio para los FABs
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        AnimatedVisibility(showContent) {
                            AddTaskScreen(
                                taskName = taskName,
                                onTaskNameChange = { taskName = it },
                                taskDescription = taskDescription,
                                onTaskDescriptionChange = { taskDescription = it },
                                taskTime = taskTime,
                                onTaskTimeChange = { taskTime = it },
                                showTimePicker = showTimePicker,
                                onShowTimePickerChange = { showTimePicker = it },
                                onAddTask = {
                                    val updated = addTask(tasks, taskName, taskDescription, if (taskTime.isNotBlank()) taskTime else null)
                                    if (updated.size > tasks.size) {
                                        tasks = updated.toMutableList()
                                        taskName = ""
                                        taskDescription = ""
                                        taskTime = ""
                                    }
                                },
                                onCancel = { showContent = false }
                            )
                        }
                    }
                    if (tasks.isNotEmpty()) {
                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                        ) {
                            Text(Strings.get("task.added"), style = MaterialTheme.typography.h6)
                            Spacer(Modifier.height(8.dp))
                            tasks.forEachIndexed { index, task ->
                                Card(
                                    shape = MaterialTheme.shapes.medium,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 8.dp)
                                ) {
                                    // Fondo personalizado según estado
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth() // Asegura que el contenido de la tarjeta ocupe todo el ancho
                                            .background(
                                                if (task.isCompleted) MaterialTheme.colors.onSurface.copy(alpha = 0.08f)
                                                else MaterialTheme.colors.surface
                                            )
                                            .padding(12.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(task.name, style = MaterialTheme.typography.subtitle1)
                                                Text(task.description, style = MaterialTheme.typography.body2)
                                                Text(Strings.get("task.estimated_time", task.estimatedTime ?: Strings.get("task.not_assigned")), style = MaterialTheme.typography.caption)
                                            }
                                            Spacer(Modifier.width(8.dp))
                                            if (editingIndex != index) {
                                                Button(onClick = {
                                                    editingIndex = index
                                                    editingTime = task.estimatedTime?.toString() ?: ""
                                                }) { Text(Strings.get("button.edit_time")) }
                                            }
                                        }
                                        if (editingIndex == index) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                OutlinedTextField(
                                                    value = editingTime,
                                                    onValueChange = {},
                                                    label = { Text(Strings.get("button.edit_time")) },
                                                    enabled = false,
                                                    modifier = Modifier.weight(1f)
                                                )
                                                Spacer(Modifier.width(8.dp))
                                                Button(onClick = {
                                                    showEditTimePicker = true
                                                    editTimePickerIndex = index
                                                }) { Text(Strings.get("button.select_time")) }
                                            }
                                            if (showEditTimePicker && editTimePickerIndex == index) {
                                                Box(Modifier.fillMaxSize()) {
                                                    Box(
                                                        Modifier
                                                            .fillMaxSize()
                                                            .background(Color.Black.copy(alpha = 0.32f))
                                                    ) {}
                                                    Box(
                                                        Modifier
                                                            .align(Alignment.Center)
                                                            .background(Color.White, shape = MaterialTheme.shapes.medium)
                                                            .padding(24.dp)
                                                    ) {
                                                        TimePickerContent(
                                                            initialTime = editingTime.ifBlank { "00:00:00" },
                                                            onTimeSelected = {
                                                                editingTime = it
                                                                showEditTimePicker = false
                                                            },
                                                            onDismiss = { showEditTimePicker = false }
                                                        )
                                                    }
                                                }
                                            }
                                            Row {
                                                Button(onClick = {
                                                    val updatedTasks = tasks.toMutableList()
                                                    updatedTasks[index].estimatedTime = editingTime
                                                    tasks = updatedTasks
                                                    editingIndex = -1
                                                    editingTime = ""
                                                }) { Text(Strings.get("button.save")) }
                                                Button(onClick = {
                                                    editingIndex = -1
                                                    editingTime = ""
                                                }) { Text(Strings.get("button.cancel")) }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                // Botón flotante para minimizar (abajo a la derecha)
                FloatingActionButton(
                    onClick = { maximized = false },
                    modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp)
                ) {
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = Strings.get("button.minimize"))
                }
                // Botón flotante para agregar tarea (abajo a la derecha, encima del de minimizar)
                FloatingActionButton(
                    onClick = { showContent = !showContent },
                    modifier = Modifier.align(Alignment.BottomEnd).padding(end = 24.dp, bottom = 88.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = Strings.get("button.add"))
                }
            }
        } else {
            TaskTrackerFloating(tasks = tasks, onMaximize = { maximized = true })
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            saveTasksEncrypted(tasks)
        }
    }
}

@Composable
fun TimePickerContent(
    initialTime: String = "00:00:00",
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var hour by remember { mutableStateOf(initialTime.split(":").getOrNull(0)?.toIntOrNull() ?: 0) }
    var minute by remember { mutableStateOf(initialTime.split(":").getOrNull(1)?.toIntOrNull() ?: 0) }
    var second by remember { mutableStateOf(initialTime.split(":").getOrNull(2)?.toIntOrNull() ?: 0) }
    Column(Modifier.padding(16.dp)) {
        Text(Strings.get("timepicker.title"), style = MaterialTheme.typography.h6)
        Row(verticalAlignment = Alignment.CenterVertically) {
            DropdownSelector(
                label = Strings.get("timepicker.hour"),
                value = hour,
                range = 0..23,
                onValueChange = { hour = it }
            )
            Spacer(Modifier.width(8.dp))
            DropdownSelector(
                label = Strings.get("timepicker.minute"),
                value = minute,
                range = 0..59,
                onValueChange = { minute = it }
            )
            Spacer(Modifier.width(8.dp))
            DropdownSelector(
                label = Strings.get("timepicker.second"),
                value = second,
                range = 0..59,
                onValueChange = { second = it }
            )
        }
        Row(Modifier.padding(top = 16.dp)) {
            Button(onClick = {
                val timeStr = String.format("%02d:%02d:%02d", hour, minute, second)
                onTimeSelected(timeStr)
            }) { Text(Strings.get("button.save")) }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onDismiss) { Text(Strings.get("button.cancel")) }
        }
    }
}

@Composable
fun DropdownSelector(label: String, value: Int, range: IntRange, onValueChange: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(onClick = { expanded = true }) {
            Text("$label: $value")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            range.forEach {
                DropdownMenuItem(onClick = {
                    onValueChange(it)
                    expanded = false
                }) { Text(it.toString()) }
            }
        }
    }
}