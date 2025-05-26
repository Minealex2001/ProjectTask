package org.queststudios.projecttask

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import objects.tasks.Task
import org.queststudios.projecttask.localization.Strings
import androidx.compose.material3.MaterialTheme as M3Theme

@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun TaskTrackerFloating(
    tasks: List<Task>,
    onMaximize: () -> Unit
) {
    Surface(
        tonalElevation = 8.dp,
        shape = M3Theme.shapes.large,
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
                Text(
                    Strings.get("task.active"),
                    style = M3Theme.typography.headlineMedium,
                    color = M3Theme.colorScheme.primary
                )
                Spacer(Modifier.height(16.dp))
                if (tasks.isEmpty()) {
                    Text(
                        Strings.get("task.no_pending"),
                        style = M3Theme.typography.bodyLarge,
                        color = M3Theme.colorScheme.onSurfaceVariant
                    )
                } else {
                    val pendingTasks = tasks.filter { !it.isCompleted }
                    val nextTask = pendingTasks
                        .filter { !it.estimatedTime.isNullOrBlank() }
                        .minByOrNull { it.estimatedTime ?: "99:99:99" }
                    var showDetails by remember { mutableStateOf(false) }

                    if (nextTask != null) {
                        ElevatedCard(
                            shape = M3Theme.shapes.medium,
                            colors = CardDefaults.elevatedCardColors(
                                containerColor = M3Theme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    Strings.get("task.next"),
                                    style = M3Theme.typography.labelLarge,
                                    color = M3Theme.colorScheme.onPrimaryContainer
                                )
                                Spacer(Modifier.height(8.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box {
                                        Text(
                                            nextTask.name,
                                            style = M3Theme.typography.titleMedium,
                                            color = M3Theme.colorScheme.onPrimaryContainer
                                        )
                                        if (showDetails) {
                                            Surface(
                                                tonalElevation = 8.dp,
                                                shape = M3Theme.shapes.medium,
                                                color = M3Theme.colorScheme.surface,
                                                modifier = Modifier
                                                    .padding(top = 28.dp, start = 0.dp)
                                                    .widthIn(min = 220.dp, max = 400.dp)
                                            ) {
                                                Column(modifier = Modifier.padding(16.dp)) {
                                                    if (nextTask.description.isNotBlank()) {
                                                        Text(
                                                            nextTask.description,
                                                            style = M3Theme.typography.bodyMedium,
                                                            color = M3Theme.colorScheme.onSurface
                                                        )
                                                    }
                                                    if (nextTask.notes.isNotEmpty()) {
                                                        Spacer(Modifier.height(8.dp))
                                                        Text(
                                                            Strings.get("task.notes"),
                                                            style = M3Theme.typography.labelLarge,
                                                            color = M3Theme.colorScheme.primary
                                                        )
                                                        nextTask.notes.forEach { note ->
                                                            Text(
                                                                "• ${note.text}",
                                                                style = M3Theme.typography.bodySmall,
                                                                color = M3Theme.colorScheme.onSurfaceVariant,
                                                                modifier = Modifier.padding(start = 8.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    Spacer(Modifier.width(8.dp))
                                }
                                Text(
                                    Strings.get("task.estimated_time", nextTask.estimatedTime),
                                    style = M3Theme.typography.bodyMedium,
                                    color = M3Theme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    } else {
                        Text(
                            Strings.get("task.no_estimated"),
                            style = M3Theme.typography.bodyMedium,
                            color = M3Theme.colorScheme.outline
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        Strings.get("task.total_pending", pendingTasks.size),
                        style = M3Theme.typography.titleMedium,
                        color = M3Theme.colorScheme.primary
                    )
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    FilledTonalButton(
                        onClick = onMaximize,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = M3Theme.colorScheme.secondaryContainer,
                            contentColor = M3Theme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Icon(
                            Icons.Default.KeyboardArrowUp,
                            contentDescription = Strings.get("button.maximize")
                        )
                    }
                }
            }
        }
    }
}
