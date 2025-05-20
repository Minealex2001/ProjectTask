package org.queststudios.projecttask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme as M3Theme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import objects.tasks.Task
import objects.notes.Note
import org.queststudios.projecttask.localization.Strings
import androidx.compose.ui.window.Dialog

/**
 * Agrega una nueva tarea a la lista existente y retorna la lista actualizada.
 */
// Expand addTask to accept initial note
fun addTask(
    tasks: List<Task>,
    name: String,
    description: String,
    estimatedTime: String?,
    noteText: String?
): List<Task> {
    if (name.isBlank()) return tasks  // Description optional
    val notesList = noteText?.takeIf { it.isNotBlank() }?.let { listOf(Note(it)) } ?: emptyList()
    val newTask = Task(
        name = name,
        description = description,
        estimatedTime = estimatedTime?.takeIf { it.isNotBlank() },
        notes = notesList
    )
    return tasks + newTask
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    taskName: String,
    onTaskNameChange: (String) -> Unit,
    taskDescription: String,
    onTaskDescriptionChange: (String) -> Unit,
    taskTime: String,
    onTaskTimeChange: (String) -> Unit,
    taskNote: String,
    onTaskNoteChange: (String) -> Unit,
    showTimePicker: Boolean,
    onShowTimePickerChange: (Boolean) -> Unit,
    onAddTask: () -> Unit,
    onCancel: () -> Unit
) {
    var showError by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        ) {
            OutlinedTextField(
                value = taskName,
                onValueChange = {
                    onTaskNameChange(it)
                    if (showError && it.isNotBlank()) showError = false
                },
                label = { Text(Strings.get("task.name")) },
                isError = showError,
                modifier = Modifier.fillMaxWidth()
            )
            if (showError) {
                Text(
                    text = Strings.get("error.title_required"),
                    color = M3Theme.colorScheme.error,
                    style = M3Theme.typography.bodySmall
                )
            }
            OutlinedTextField(
                value = taskDescription,
                onValueChange = onTaskDescriptionChange,
                label = { Text(Strings.get("task.description")) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = taskNote,
                onValueChange = onTaskNoteChange,
                label = { Text(Strings.get("task.new_note")) },
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = taskTime,
                    onValueChange = {},
                    label = { Text(Strings.get("task.estimated_time"), style = M3Theme.typography.labelLarge) },
                    enabled = false,
                    modifier = Modifier.weight(1f),
                    shape = M3Theme.shapes.medium
                )
                Spacer(Modifier.width(8.dp))
                ElevatedButton(onClick = { onShowTimePickerChange(true) }) {
                    Text(Strings.get("button.select_time"))
                }
            }
            // Replace old time picker overlay with Material3 Dialog/TimePicker
            if (showTimePicker) {
                Dialog(onDismissRequest = { onShowTimePickerChange(false) }) {
                    Surface(
                        shape = M3Theme.shapes.medium,
                        color = M3Theme.colorScheme.surface,
                        tonalElevation = 8.dp
                    ) {
                        val parts = taskTime.split(":")
                        val initHour = parts.getOrNull(0)?.toIntOrNull() ?: 0
                        val initMinute = parts.getOrNull(1)?.toIntOrNull() ?: 0
                        val timePickerState = rememberTimePickerState(
                            initialHour = initHour,
                            initialMinute = initMinute
                        )
                        Column(
                            modifier = Modifier.padding(24.dp)
                        ) {
                            TimePicker(
                                state = timePickerState
                            )
                            Spacer(Modifier.height(16.dp))
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                OutlinedButton(onClick = { onShowTimePickerChange(false) }) {
                                    Text(Strings.get("button.cancel"))
                                }
                                Spacer(Modifier.width(8.dp))
                                ElevatedButton(onClick = {
                                    val selectedHour = timePickerState.hour
                                    val selectedMinute = timePickerState.minute
                                    val formatted = String.format("%02d:%02d:00", selectedHour, selectedMinute)
                                    onTaskTimeChange(formatted)
                                    onShowTimePickerChange(false)
                                }) {
                                    Text(Strings.get("button.save"))
                                }
                            }
                        }
                    }
                }
            }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(onClick = onCancel) { Text(Strings.get("button.cancel")) }
                ElevatedButton(onClick = {
                    if (taskName.isBlank()) {
                        showError = true
                    } else {
                        onAddTask()
                    }
                }) {
                    Text(Strings.get("button.add"))
                }
            }
        }
    }
}
