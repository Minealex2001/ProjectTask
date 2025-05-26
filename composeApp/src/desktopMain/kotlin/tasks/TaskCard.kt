package tasks

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import objects.tasks.Task
import org.queststudios.projecttask.localization.Strings
import androidx.compose.material3.MaterialTheme as M3Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    task: Task,
    onTaskUpdate: (Task) -> Unit,
    onNoteAdd: (String) -> Unit,
    onNoteDelete: (Int) -> Unit,
    estimatedTime: String?,
    onEstimatedTimeChange: (String) -> Unit,
    onDeleteTask: () -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(task.name) }
    var editedDescription by remember { mutableStateOf(task.description) }
    var newNoteText by remember { mutableStateOf("") }
    var editingNoteIndex by remember { mutableStateOf(-1) }
    var editingNoteText by remember { mutableStateOf("") }
    var editingTime by remember { mutableStateOf(estimatedTime ?: "") }
    var showEditTimePicker by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    ElevatedCard(
        shape = M3Theme.shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (task.isCompleted) 
                M3Theme.colorScheme.surfaceVariant 
            else 
                M3Theme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Título
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isEditing) {
                    Text(
                        text = task.name,
                        style = M3Theme.typography.titleLarge,
                        color = M3Theme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    OutlinedTextField(
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text(Strings.get("task.name")) },
                        modifier = Modifier.weight(1f)
                    )
                }
                Row {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = Strings.get("button.edit"),
                                tint = M3Theme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = Strings.get("button.delete"),
                                tint = M3Theme.colorScheme.error
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {
                                onTaskUpdate(task.copy(name = editedName, description = editedDescription))
                                isEditing = false
                            }
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = Strings.get("button.save"),
                                tint = M3Theme.colorScheme.primary
                            )
                        }
                        IconButton(
                            onClick = {
                                editedName = task.name
                                editedDescription = task.description
                                isEditing = false
                            }
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = Strings.get("button.cancel"),
                                tint = M3Theme.colorScheme.error
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Descripción
            if (!isEditing && task.description.isNotBlank()) {
                Text(
                    text = task.description,
                    style = M3Theme.typography.bodyMedium,
                    color = M3Theme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
            } else if (isEditing) {
                OutlinedTextField(
                    value = editedDescription,
                    onValueChange = { editedDescription = it },
                    label = { Text(Strings.get("task.description")) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
            }

            // Notas
            if (!isEditing && task.notes.isNotEmpty()) {
                Text(
                    text = Strings.get("task.notes"),
                    style = M3Theme.typography.labelLarge,
                    color = M3Theme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                task.notes.forEach { note ->
                    Text(
                        text = "• ${note.text}",
                        style = M3Theme.typography.bodySmall,
                        color = M3Theme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(Modifier.height(8.dp))
            } else if (isEditing && task.notes.isNotEmpty()) {
                Text(
                    text = Strings.get("task.notes"),
                    style = M3Theme.typography.labelLarge,
                    color = M3Theme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                task.notes.forEachIndexed { noteIndex, note ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "• ${note.text}",
                            style = M3Theme.typography.bodySmall,
                            color = M3Theme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = { onNoteDelete(noteIndex) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = Strings.get("button.delete_note"),
                                tint = M3Theme.colorScheme.error
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            // Tiempo estimado
            if (task.estimatedTime != null && task.estimatedTime!!.isNotBlank()) {
                Text(
                    text = Strings.get("task.estimated_time") + ": " + task.estimatedTime,
                    style = M3Theme.typography.bodySmall,
                    color = M3Theme.colorScheme.secondary
                )
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(Strings.get("dialog.delete_task_title")) },
            text = { Text(Strings.get("dialog.delete_task_message")) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteTask()
                    }
                ) {
                    Text(
                        Strings.get("button.delete"),
                        color = M3Theme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(Strings.get("button.cancel"))
                }
            }
        )
    }

    if (showEditTimePicker) {
        Dialog(onDismissRequest = { showEditTimePicker = false }) {
            Surface(
                shape = M3Theme.shapes.large,
                color = M3Theme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                // Material3 TimePicker
                val parts = editingTime.split(":")
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
                        OutlinedButton(onClick = { showEditTimePicker = false }) {
                            Text(Strings.get("button.cancel"))
                        }
                        Spacer(Modifier.width(8.dp))
                        ElevatedButton(onClick = {
                            val selectedHour = timePickerState.hour
                            val selectedMinute = timePickerState.minute
                            val formatted =
                                String.format("%02d:%02d:00", selectedHour, selectedMinute)
                            editingTime = formatted
                            onEstimatedTimeChange(formatted)
                            showEditTimePicker = false
                        }) {
                            Text(Strings.get("button.save"))
                        }
                    }
                }
            }
        }
    }
}
