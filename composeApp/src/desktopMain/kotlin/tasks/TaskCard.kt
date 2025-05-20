package org.queststudios.projecttask

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme as M3Theme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import org.queststudios.projecttask.localization.Strings
import objects.tasks.Task
import objects.notes.Note
import androidx.compose.ui.window.Dialog

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
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = M3Theme.colorScheme.primary,
                            unfocusedBorderColor = M3Theme.colorScheme.outline
                        )
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

            if (!isEditing) {
                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = M3Theme.typography.bodyMedium,
                        color = M3Theme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(8.dp))
                }
            } else {
                OutlinedTextField(
                    value = editedDescription,
                    onValueChange = { editedDescription = it },
                    label = { Text(Strings.get("task.description")) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = M3Theme.colorScheme.primary,
                        unfocusedBorderColor = M3Theme.colorScheme.outline
                    )
                )
                Spacer(Modifier.height(8.dp))
            }

            if (task.notes.isNotEmpty()) {
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
                        if (isEditing) {
                            IconButton(
                                onClick = { onNoteDelete(noteIndex) },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = Strings.get("button.delete"),
                                    tint = M3Theme.colorScheme.error,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            if (isEditing) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = newNoteText,
                        onValueChange = { newNoteText = it },
                        label = { Text(Strings.get("task.new_note")) },
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = M3Theme.colorScheme.primary,
                            unfocusedBorderColor = M3Theme.colorScheme.outline
                        )
                    )
                    IconButton(
                        onClick = {
                            if (newNoteText.isNotBlank()) {
                                onNoteAdd(newNoteText)
                                newNoteText = ""
                            }
                        }
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = Strings.get("button.add"),
                            tint = M3Theme.colorScheme.primary
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = editingTime,
                    onValueChange = {},
                    label = { Text(Strings.get("task.estimated_time")) },
                    enabled = false,
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        disabledBorderColor = M3Theme.colorScheme.outline,
                        disabledTextColor = M3Theme.colorScheme.onSurface
                    )
                )
                Spacer(Modifier.width(8.dp))
                FilledTonalButton(
                    onClick = { showEditTimePicker = true }
                ) {
                    Text(Strings.get("button.select_time"))
                }
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
                TimePickerContent(
                    initialTime = editingTime,
                    onTimeSelected = { newTime ->
                        editingTime = newTime
                        onEstimatedTimeChange(newTime)
                        showEditTimePicker = false
                    },
                    onDismiss = { showEditTimePicker = false }
                )
            }
        }
    }
}
