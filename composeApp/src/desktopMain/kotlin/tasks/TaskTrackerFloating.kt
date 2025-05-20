package org.queststudios.projecttask

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme as M3Theme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import objects.tasks.Task
import org.queststudios.projecttask.localization.Strings

@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun TaskTrackerFloating(
    tasks: List<Task>,
    onMaximize: () -> Unit
) {
    Surface(
        tonalElevation = 8.dp,
        shape = M3Theme.shapes.medium,
        color = M3Theme.colorScheme.surface,
        modifier = Modifier
            .widthIn(min = 340.dp, max = 600.dp)
            .padding(12.dp)
    ) {
        BoxWithConstraints {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(Strings.get("task.active"), style = M3Theme.typography.titleLarge, color = M3Theme.colorScheme.primary)
                Spacer(Modifier.height(8.dp))
                if (tasks.isEmpty()) {
                    Text(Strings.get("task.no_pending"), style = M3Theme.typography.bodyLarge, color = M3Theme.colorScheme.onSurface)
                } else {
                    val pendingTasks = tasks.filter { !it.isCompleted }
                    val nextTask = pendingTasks
                        .filter { !it.estimatedTime.isNullOrBlank() }
                        .minByOrNull { it.estimatedTime ?: "99:99:99" }
                    var showDetails by remember { mutableStateOf(false) }
                    if (nextTask != null) {
                        Text(Strings.get("task.next"), style = M3Theme.typography.titleMedium, color = M3Theme.colorScheme.secondary)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .pointerMoveFilter(
                                    onEnter = {
                                        showDetails = true
                                        false
                                    },
                                    onExit = {
                                        showDetails = false
                                        false
                                    }
                                )
                                .let { baseModifier ->
                                    // Ajuste automático de ancho si el nombre o detalles son largos
                                    if (showDetails && (nextTask.description.isNotBlank() || nextTask.notes.isNotEmpty())) {
                                        baseModifier.widthIn(min = 340.dp, max = 600.dp)
                                    } else {
                                        baseModifier
                                    }
                                }
                        ) {
                            Box {
                                Text(nextTask.name, style = M3Theme.typography.bodyLarge, color = M3Theme.colorScheme.onSurface)
                                if (showDetails) {
                                    Surface(
                                        tonalElevation = 8.dp,
                                        shape = M3Theme.shapes.small,
                                        color = M3Theme.colorScheme.surfaceVariant,
                                        modifier = Modifier
                                            .padding(top = 28.dp, start = 0.dp)
                                            .widthIn(min = 220.dp, max = 400.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            if (nextTask.description.isNotBlank()) {
                                                Text(nextTask.description, style = M3Theme.typography.bodyMedium)
                                            }
                                            if (nextTask.notes.isNotEmpty()) {
                                                Spacer(Modifier.height(4.dp))
                                                Text(Strings.get("task.notes"), style = M3Theme.typography.labelLarge)
                                                nextTask.notes.forEach { note ->
                                                    Text("- ${note.text}", style = M3Theme.typography.bodySmall, modifier = Modifier.padding(start = 8.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.width(8.dp))
                        }
                        Text(Strings.get("task.estimated_time", nextTask.estimatedTime), style = M3Theme.typography.bodyMedium, color = M3Theme.colorScheme.secondary)
                    } else {
                        Text(Strings.get("task.no_estimated"), style = M3Theme.typography.bodyMedium, color = M3Theme.colorScheme.outline)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(Strings.get("task.total_pending", pendingTasks.size), style = M3Theme.typography.bodyLarge, color = M3Theme.colorScheme.primary)
                }
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    ElevatedButton(onClick = onMaximize) {
                        Text(Strings.get("button.maximize"))
                    }
                }
            }
        }
    }
}
