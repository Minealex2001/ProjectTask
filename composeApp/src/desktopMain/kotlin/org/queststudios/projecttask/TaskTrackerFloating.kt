package org.queststudios.projecttask

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import objects.tasks.Task

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
                    tasks.forEach { task ->
                        Column(Modifier.padding(bottom = 8.dp)) {
                            Text(task.name, style = MaterialTheme.typography.body1)
                            Text(task.description, style = MaterialTheme.typography.body2)
                            Text("Tiempo estimado: ${task.estimatedTime ?: "No asignado"}", style = MaterialTheme.typography.body2)
                            if (task.notes.isNotEmpty()) {
                                Text("Notas:", style = MaterialTheme.typography.body2)
                                task.notes.forEach { note ->
                                    Text("- ${note.text}", style = MaterialTheme.typography.body2, modifier = Modifier.padding(start = 8.dp))
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                        }
                    }
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
