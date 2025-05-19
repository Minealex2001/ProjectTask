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
import org.queststudios.projecttask.localization.Strings
import androidx.compose.ui.window.Dialog

/**
 * Agrega una nueva tarea a la lista existente y retorna la lista actualizada.
 */
fun addTask(
    tasks: List<Task>,
    name: String,
    description: String,
    estimatedTime: String?
): List<Task> {
    if (name.isBlank() || description.isBlank()) return tasks
    val newTask = Task(
        name = name,
        description = description,
        estimatedTime = estimatedTime?.takeIf { it.isNotBlank() }
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
    showTimePicker: Boolean,
    onShowTimePickerChange: (Boolean) -> Unit,
    onAddTask: () -> Unit,
    onCancel: () -> Unit
) {
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
                onValueChange = onTaskNameChange,
                label = { Text(Strings.get("task.name"), style = M3Theme.typography.labelLarge) },
                modifier = Modifier.fillMaxWidth(),
                shape = M3Theme.shapes.medium,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = M3Theme.colorScheme.primary,
                    unfocusedBorderColor = M3Theme.colorScheme.outline
                )
            )
            OutlinedTextField(
                value = taskDescription,
                onValueChange = onTaskDescriptionChange,
                label = { Text(Strings.get("task.description"), style = M3Theme.typography.labelLarge) },
                modifier = Modifier.fillMaxWidth(),
                shape = M3Theme.shapes.medium,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = M3Theme.colorScheme.primary,
                    unfocusedBorderColor = M3Theme.colorScheme.outline
                )
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
                ElevatedButton(onClick = onAddTask) { Text(Strings.get("button.add")) }
            }
        }
    }
}
