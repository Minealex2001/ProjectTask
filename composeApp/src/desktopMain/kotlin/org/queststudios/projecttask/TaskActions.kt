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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.widthIn(min = 340.dp, max = 480.dp).padding(16.dp)
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
        Spacer(Modifier.height(12.dp))
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
        Spacer(Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
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
        if (showTimePicker) {
            Box(Modifier.fillMaxSize()) {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(M3Theme.colorScheme.scrim.copy(alpha = 0.32f))
                ) {}
                Box(
                    Modifier
                        .align(Alignment.Center)
                        .background(M3Theme.colorScheme.surface, shape = M3Theme.shapes.medium)
                        .padding(24.dp)
                ) {
                    org.queststudios.projecttask.TimePickerContent(
                        initialTime = taskTime.ifBlank { "00:00:00" },
                        onTimeSelected = {
                            onTaskTimeChange(it)
                            onShowTimePickerChange(false)
                        },
                        onDismiss = { onShowTimePickerChange(false) }
                    )
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            OutlinedButton(onClick = onCancel) { Text(Strings.get("button.cancel")) }
            Spacer(Modifier.width(8.dp))
            ElevatedButton(onClick = onAddTask) { Text(Strings.get("button.add")) }
        }
    }
}
