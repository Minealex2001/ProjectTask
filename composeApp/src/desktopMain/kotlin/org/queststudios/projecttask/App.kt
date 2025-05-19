package org.queststudios.projecttask

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.ui.unit.dp
import objects.tasks.Task
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Paths
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlinx.serialization.builtins.ListSerializer

@Composable
@Preview
fun App() {
    val password = "projecttask2024" // Puedes cambiar esto o pedirlo al usuario si lo deseas
    var tasks by remember { mutableStateOf(loadTasksEncrypted(password).toMutableList()) }
    var showContent by remember { mutableStateOf(false) }
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var taskTime by remember { mutableStateOf("") }
    var editingIndex by remember { mutableStateOf(-1) }
    var editingTime by remember { mutableStateOf("") }
    var showTimePicker by remember { mutableStateOf(false) }
    var showEditTimePicker by remember { mutableStateOf(false) }
    var editTimePickerIndex by remember { mutableStateOf(-1) }
    MaterialTheme {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { showContent = !showContent }) {
                Text(if (showContent) "Ocultar formulario" else "Agregar Tarea")
            }
            AnimatedVisibility(showContent) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = taskName,
                        onValueChange = { taskName = it },
                        label = { Text("Nombre de la tarea") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = taskDescription,
                        onValueChange = { taskDescription = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = taskTime,
                            onValueChange = {},
                            label = { Text("Tiempo estimado (HH:mm:ss)") },
                            enabled = false,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { showTimePicker = true }) {
                            Text("Seleccionar tiempo")
                        }
                    }
                    if (showTimePicker) {
                        Box(Modifier.fillMaxSize()) {
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colors.onSurface.copy(alpha = 0.32f))
                            ) {}
                            Box(
                                Modifier
                                    .align(Alignment.Center)
                                    .background(MaterialTheme.colors.surface, shape = MaterialTheme.shapes.medium)
                                    .padding(24.dp)
                            ) {
                                TimePickerContent(
                                    initialTime = taskTime.ifBlank { "00:00:00" },
                                    onTimeSelected = {
                                        taskTime = it
                                        showTimePicker = false
                                    },
                                    onDismiss = { showTimePicker = false }
                                )
                            }
                        }
                    }
                    Button(onClick = {
                        if (taskName.isNotBlank() && taskDescription.isNotBlank()) {
                            val newTask = Task(
                                name = taskName,
                                description = taskDescription,
                                estimatedTime = if (taskTime.isNotBlank()) taskTime else null
                            )
                            tasks = tasks.toMutableList().apply { add(newTask) }
                            taskName = ""
                            taskDescription = ""
                            taskTime = ""
                        }
                    }) {
                        Text("Agregar")
                    }
                }
            }
            // Mostrar lista de tareas agregadas
            if (tasks.isNotEmpty()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Tareas agregadas:", style = MaterialTheme.typography.h6)
                    tasks.forEachIndexed { index, task ->
                        Column {
                            Text("- ${task.name}: ${task.description}")
                            Text("  Tiempo estimado: ${task.estimatedTime ?: "No asignado"}")
                            if (editingIndex == index) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    OutlinedTextField(
                                        value = editingTime,
                                        onValueChange = {},
                                        label = { Text("Editar tiempo (HH:mm:ss)") },
                                        enabled = false,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Button(onClick = {
                                        showEditTimePicker = true
                                        editTimePickerIndex = index
                                    }) { Text("Seleccionar tiempo") }
                                }
                                if (showEditTimePicker && editTimePickerIndex == index) {
                                    Box(Modifier.fillMaxSize()) {
                                        Box(
                                            Modifier
                                                .fillMaxSize()
                                                .background(MaterialTheme.colors.onSurface.copy(alpha = 0.32f))
                                        ) {}
                                        Box(
                                            Modifier
                                                .align(Alignment.Center)
                                                .background(MaterialTheme.colors.surface, shape = MaterialTheme.shapes.medium)
                                                .padding(24.dp)
                                        ) {
                                            TimePickerContent(
                                                initialTime = editingTime.ifBlank { "00:00:00" },
                                                onTimeSelected = {
                                                    editingTime = it
                                                    showEditTimePicker = false
                                                },
                                                onDismiss = { showEditTimePicker = false }
                                            )
                                        }
                                    }
                                }
                                Row {
                                    Button(onClick = {
                                        // Guardar el tiempo editado como String
                                        val updatedTasks = tasks.toMutableList()
                                        updatedTasks[index].estimatedTime = editingTime
                                        tasks = updatedTasks
                                        editingIndex = -1
                                        editingTime = ""
                                    }) { Text("Guardar") }
                                    Button(onClick = {
                                        editingIndex = -1
                                        editingTime = ""
                                    }) { Text("Cancelar") }
                                }
                            } else {
                                Button(onClick = {
                                    editingIndex = index
                                    editingTime = task.estimatedTime?.toString() ?: ""
                                }) { Text("Editar tiempo") }
                            }
                        }
                    }
                }
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            saveTasksEncrypted(tasks, password)
        }
    }
}

@Composable
fun TimePickerContent(
    initialTime: String = "00:00:00",
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var hour by remember { mutableStateOf(initialTime.split(":").getOrNull(0)?.toIntOrNull() ?: 0) }
    var minute by remember { mutableStateOf(initialTime.split(":").getOrNull(1)?.toIntOrNull() ?: 0) }
    var second by remember { mutableStateOf(initialTime.split(":").getOrNull(2)?.toIntOrNull() ?: 0) }
    Column(Modifier.padding(16.dp)) {
        Text("Selecciona el tiempo", style = MaterialTheme.typography.h6)
        Row(verticalAlignment = Alignment.CenterVertically) {
            DropdownSelector(
                label = "Hora",
                value = hour,
                range = 0..23,
                onValueChange = { hour = it }
            )
            Spacer(Modifier.width(8.dp))
            DropdownSelector(
                label = "Minuto",
                value = minute,
                range = 0..59,
                onValueChange = { minute = it }
            )
            Spacer(Modifier.width(8.dp))
            DropdownSelector(
                label = "Segundo",
                value = second,
                range = 0..59,
                onValueChange = { second = it }
            )
        }
        Row(Modifier.padding(top = 16.dp)) {
            Button(onClick = {
                val timeStr = String.format("%02d:%02d:%02d", hour, minute, second)
                onTimeSelected(timeStr)
            }) { Text("Aceptar") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onDismiss) { Text("Cancelar") }
        }
    }
}

@Composable
fun DropdownSelector(label: String, value: Int, range: IntRange, onValueChange: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        Button(onClick = { expanded = true }) {
            Text("$label: $value")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            range.forEach {
                DropdownMenuItem(onClick = {
                    onValueChange(it)
                    expanded = false
                }) { Text(it.toString()) }
            }
        }
    }
}

fun saveTasksEncrypted(tasks: List<Task>, password: String) {
    val json = Json.encodeToString(ListSerializer(Task.serializer()), tasks)
    // Encriptar el JSON
    val key = password.padEnd(16, '0').substring(0, 16).toByteArray()
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    val secretKey = SecretKeySpec(key, "AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    val encrypted = cipher.doFinal(json.toByteArray(Charsets.UTF_8))
    val base64 = Base64.getEncoder().encodeToString(encrypted)
    // Guardar en Documentos/ProjectTask/tasks.json.enc
    val userHome = System.getProperty("user.home")
    val dir = Paths.get(userHome, "Documents", "ProjectTask").toFile()
    if (!dir.exists()) dir.mkdirs()
    val file = File(dir, "tasks.json.enc")
    file.writeText(base64)
}

fun loadTasksEncrypted(password: String): List<Task> {
    val userHome = System.getProperty("user.home")
    val file = Paths.get(userHome, "Documents", "ProjectTask", "tasks.json.enc").toFile()
    if (!file.exists()) return emptyList()
    val base64 = file.readText()
    val encrypted = Base64.getDecoder().decode(base64)
    val key = password.padEnd(16, '0').substring(0, 16).toByteArray()
    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    val secretKey = SecretKeySpec(key, "AES")
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    val json = cipher.doFinal(encrypted).toString(Charsets.UTF_8)
    return Json.decodeFromString(ListSerializer(Task.serializer()), json)
}