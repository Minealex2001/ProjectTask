package org.queststudios.projecttask

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
        modifier = Modifier.widthIn(min = 320.dp, max = 420.dp)
    ) {
        OutlinedTextField(
            value = taskName,
            onValueChange = onTaskNameChange,
            label = { Text(Strings.get("task.name")) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = taskDescription,
            onValueChange = onTaskDescriptionChange,
            label = { Text(Strings.get("task.description")) },
            modifier = Modifier.fillMaxWidth()
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = taskTime,
                onValueChange = {},
                label = { Text(Strings.get("task.estimated_time")) },
                enabled = false,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = { onShowTimePickerChange(true) }) {
                Text(Strings.get("button.select_time"))
            }
        }
        if (showTimePicker) {
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
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = onCancel) { Text(Strings.get("button.cancel")) }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onAddTask) { Text(Strings.get("button.add")) }
        }
    }
}
