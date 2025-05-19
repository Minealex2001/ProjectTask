package org.queststudios.projecttask

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme as M3Theme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Color
import org.queststudios.projecttask.localization.Strings
import objects.tasks.Task
import objects.notes.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskCard(
    task: Task,
    onTaskUpdate: (Task) -> Unit,
    onNoteAdd: (String) -> Unit,
    onNoteEdit: (Int, String) -> Unit,
    onNoteDelete: (Int) -> Unit,
    estimatedTime: String?,
    onEstimatedTimeChange: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf(task.name) }
    var editedDescription by remember { mutableStateOf(task.description) }
    var newNoteText by remember { mutableStateOf("") }
    var editingNoteIndex by remember { mutableStateOf(-1) }
    var editingNoteText by remember { mutableStateOf("") }
    var editingTime by remember { mutableStateOf(estimatedTime ?: "") }
    var showEditTimePicker by remember { mutableStateOf(false) }
    var editTimePickerIndex by remember { mutableStateOf(-1) }

    ElevatedCard(
        shape = M3Theme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = if (task.isCompleted) M3Theme.colorScheme.surfaceVariant else M3Theme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    AnimatedVisibility(visible = isEditing, enter = fadeIn(), exit = fadeOut()) {
                        Column {
                            OutlinedTextField(
                                value = editedName,
                                onValueChange = { editedName = it },
                                label = { Text(Strings.get("task.name")) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = editedDescription,
                                onValueChange = { editedDescription = it },
                                label = { Text(Strings.get("task.description")) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    AnimatedVisibility(visible = !isEditing, enter = fadeIn(), exit = fadeOut()) {
                        Column {
                            Text(task.name, style = M3Theme.typography.titleMedium, color = M3Theme.colorScheme.onSurface)
                            Text(task.description, style = M3Theme.typography.bodyMedium, color = M3Theme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Text(Strings.get("task.estimated_time", estimatedTime ?: Strings.get("task.not_assigned")), style = M3Theme.typography.labelMedium, color = M3Theme.colorScheme.secondary)
                }
                Spacer(Modifier.width(8.dp))
                AnimatedVisibility(visible = !isEditing, enter = fadeIn(), exit = fadeOut()) {
                    FilledTonalButton(onClick = { isEditing = true }) { Text(Strings.get("button.edit"), color = M3Theme.colorScheme.onPrimary) }
                }
            }
            AnimatedVisibility(visible = isEditing, enter = fadeIn(), exit = fadeOut()) {
                Row(Modifier.padding(top = 8.dp)) {
                    ElevatedButton(onClick = {
                        onTaskUpdate(task.copy(name = editedName, description = editedDescription))
                        isEditing = false
                    }) { Text(Strings.get("button.save")) }
                    Spacer(Modifier.width(8.dp))
                    OutlinedButton(onClick = {
                        editedName = task.name
                        editedDescription = task.description
                        isEditing = false
                    }) { Text(Strings.get("button.cancel")) }
                }
            }
            // Notas
            Spacer(Modifier.height(8.dp))
            val showNotesHeader = isEditing || task.notes.isNotEmpty()
            if (showNotesHeader) {
                Text(Strings.get("task.notes"), style = M3Theme.typography.labelLarge)
            }
            AnimatedVisibility(visible = isEditing, enter = fadeIn(), exit = fadeOut()) {
                Column {
                    task.notes.forEachIndexed { noteIndex, note ->
                        AnimatedVisibility(visible = editingNoteIndex == noteIndex, enter = fadeIn(), exit = fadeOut()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedTextField(
                                    value = editingNoteText,
                                    onValueChange = { editingNoteText = it },
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = {
                                    onNoteEdit(noteIndex, editingNoteText)
                                    editingNoteIndex = -1
                                    editingNoteText = ""
                                }) { Icon(Icons.Default.Check, contentDescription = Strings.get("button.save")) }
                                IconButton(onClick = {
                                    editingNoteIndex = -1
                                    editingNoteText = ""
                                }) { Icon(Icons.Default.Close, contentDescription = Strings.get("button.cancel")) }
                            }
                        }
                        AnimatedVisibility(visible = editingNoteIndex != noteIndex, enter = fadeIn(), exit = fadeOut()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("- ${note.text}", style = M3Theme.typography.bodySmall, modifier = Modifier.weight(1f))
                                IconButton(onClick = {
                                    editingNoteIndex = noteIndex
                                    editingNoteText = note.text
                                }) { Icon(Icons.Default.Edit, contentDescription = Strings.get("button.edit")) }
                                IconButton(onClick = { onNoteDelete(noteIndex) }) { Icon(Icons.Default.Delete, contentDescription = Strings.get("button.delete")) }
                            }
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = newNoteText,
                            onValueChange = { newNoteText = it },
                            label = { Text(Strings.get("task.new_note")) },
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = {
                            if (newNoteText.isNotBlank()) {
                                onNoteAdd(newNoteText)
                                newNoteText = ""
                            }
                        }) { Icon(Icons.Default.Add, contentDescription = Strings.get("button.add")) }
                    }
                }
            }
            AnimatedVisibility(visible = !isEditing, enter = fadeIn(), exit = fadeOut()) {
                Column {
                    task.notes.forEach { note ->
                        Text("- ${note.text}", style = M3Theme.typography.bodySmall)
                    }
                }
            }
            // Edición de tiempo estimado
            Spacer(Modifier.height(8.dp))
            AnimatedVisibility(visible = isEditing, enter = fadeIn(), exit = fadeOut()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = editingTime,
                        onValueChange = {},
                        label = { Text(Strings.get("task.estimated_time")) },
                        enabled = false,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(8.dp))
                    FilledTonalButton(onClick = { showEditTimePicker = true }) {
                        Text(Strings.get("button.select_time"), color = M3Theme.colorScheme.onPrimary)
                    }
                }
            }
            AnimatedVisibility(visible = showEditTimePicker, enter = fadeIn(), exit = fadeOut()) {
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
                            initialTime = editingTime.ifBlank { "00:00:00" },
                            onTimeSelected = {
                                editingTime = it
                                showEditTimePicker = false
                                onEstimatedTimeChange(it)
                            },
                            onDismiss = { showEditTimePicker = false }
                        )
                    }
                }
            }
        }
    }
}
