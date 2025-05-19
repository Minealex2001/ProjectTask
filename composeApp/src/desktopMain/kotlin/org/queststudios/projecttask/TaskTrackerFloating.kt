package org.queststudios.projecttask

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import objects.tasks.Task

@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun TaskTrackerFloating(
    tasks: List<Task>,
    onMaximize: () -> Unit
) {
    Surface(
        elevation = 8.dp,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .widthIn(min = 340.dp, max = 600.dp)
            .padding(12.dp)
    ) {
        BoxWithConstraints {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text("Tareas activas", style = MaterialTheme.typography.h6)
                Spacer(Modifier.height(8.dp))
                if (tasks.isEmpty()) {
                    Text("No tienes tareas pendientes", style = MaterialTheme.typography.body1)
                } else {
                    val pendingTasks = tasks.filter { !it.isCompleted }
                    val nextTask = pendingTasks
                        .filter { !it.estimatedTime.isNullOrBlank() }
                        .minByOrNull { it.estimatedTime ?: "99:99:99" }
                    var showDetails by remember { mutableStateOf(false) }
                    if (nextTask != null) {
                        Text("Próxima tarea:", style = MaterialTheme.typography.body1)
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
                                Text(nextTask.name, style = MaterialTheme.typography.body1)
                                if (showDetails) {
                                    Surface(
                                        elevation = 8.dp,
                                        shape = MaterialTheme.shapes.small,
                                        color = MaterialTheme.colors.surface.copy(alpha = 0.98f),
                                        modifier = Modifier
                                            .padding(top = 28.dp, start = 0.dp)
                                            .widthIn(min = 220.dp, max = 400.dp)
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            if (nextTask.description.isNotBlank()) {
                                                Text(nextTask.description, style = MaterialTheme.typography.body2)
                                            }
                                            if (nextTask.notes.isNotEmpty()) {
                                                Spacer(Modifier.height(4.dp))
                                                Text("Notas:", style = MaterialTheme.typography.body2)
                                                nextTask.notes.forEach { note ->
                                                    Text("- ${note.text}", style = MaterialTheme.typography.body2, modifier = Modifier.padding(start = 8.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.width(8.dp))
                        }
                        Text("Tiempo estimado: ${nextTask.estimatedTime}", style = MaterialTheme.typography.body2)
                    } else {
                        Text("No hay tareas con tiempo estimado", style = MaterialTheme.typography.body2)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Total pendientes: ${pendingTasks.size}", style = MaterialTheme.typography.body1)
                }
                Spacer(Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(onClick = onMaximize) {
                        Text("Maximizar")
                    }
                }
            }
        }
    }
}
