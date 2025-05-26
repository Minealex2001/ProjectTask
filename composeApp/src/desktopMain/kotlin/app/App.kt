package app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.IconButton
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import objects.notes.Note
import org.queststudios.projecttask.AddTaskScreen
import org.queststudios.projecttask.TaskTrackerFloating
import org.queststudios.projecttask.addTask
import org.queststudios.projecttask.localization.Strings
import org.queststudios.projecttask.storage.loadTasksEncrypted
import org.queststudios.projecttask.storage.saveTasksEncrypted
import org.queststudios.projecttask.ui.CustomDarkColorScheme
import org.queststudios.projecttask.ui.CustomLightColorScheme
import tasks.TaskCard
import java.io.File
import androidx.compose.material3.MaterialTheme as M3Theme

fun getThemeFromSettings(): Boolean {
    val configFile = File(System.getProperty("user.home"), "Documents/ProjectTask/settings.json")
    if (configFile.exists()) {
        try {
            val json = Json.parseToJsonElement(configFile.readText()).jsonObject
            val theme = json["theme"]?.jsonPrimitive?.content
            if (theme == "dark") return true
            if (theme == "light") return false
        } catch (_: Exception) {}
    }
    return false // Por defecto claro
}

fun saveThemeToSettings(isDark: Boolean) {
    val configFile = File(System.getProperty("user.home"), "Documents/ProjectTask/settings.json")
    val json: JsonObject = if (configFile.exists()) {
        try {
            Json.parseToJsonElement(configFile.readText()).jsonObject
        } catch (_: Exception) {
            buildJsonObject { }
        }
    } else buildJsonObject { }
    val updated = buildJsonObject {
        json.forEach { (k, v) -> put(k, v) }
        put("theme", if (isDark) "dark" else "light")
    }
    configFile.parentFile?.mkdirs()
    configFile.writeText(Json.encodeToString(JsonObject.serializer(), updated))
}

@Composable
@Preview
fun App() {
    var tasks by remember { mutableStateOf(loadTasksEncrypted().toMutableList()) }
    var maximized by remember { mutableStateOf(false) }
    var showContent by remember { mutableStateOf(false) }
    var taskName by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var taskTime by remember { mutableStateOf("") }
    var taskNote by remember { mutableStateOf("") }
    var editingIndex by remember { mutableStateOf(-1) }
    var editingTime by remember { mutableStateOf("") }
    var showTimePicker by remember { mutableStateOf(false) }
    var showEditTimePicker by remember { mutableStateOf(false) }
    var editTimePickerIndex by remember { mutableStateOf(-1) }
    var isDarkTheme by remember { mutableStateOf(getThemeFromSettings()) }

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
        saveThemeToSettings(isDarkTheme)
    }

    M3Theme(
        colorScheme = if (isDarkTheme) CustomDarkColorScheme else CustomLightColorScheme,
        typography = Typography(),
        shapes = Shapes()
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = M3Theme.colorScheme.background,
            tonalElevation = 8.dp
        ) {
            if (maximized) {
                Box(Modifier.fillMaxSize()) {
                    if (showContent) {
                        Dialog(onDismissRequest = { showContent = false }) {
                            Surface(
                                shape = M3Theme.shapes.large,
                                color = M3Theme.colorScheme.surface,
                                tonalElevation = 8.dp,
                                modifier = Modifier.padding(16.dp)
                            ) {
                                AddTaskScreen(
                                    taskName = taskName,
                                    onTaskNameChange = { taskName = it },
                                    taskDescription = taskDescription,
                                    onTaskDescriptionChange = { taskDescription = it },
                                    taskTime = taskTime,
                                    onTaskTimeChange = { taskTime = it },
                                    taskNote = taskNote,
                                    onTaskNoteChange = { taskNote = it },
                                    showTimePicker = showTimePicker,
                                    onShowTimePickerChange = { showTimePicker = it },
                                    onAddTask = {
                                        val updated = addTask(
                                            tasks,
                                            taskName,
                                            taskDescription,
                                            taskTime.ifBlank { null },
                                            taskNote
                                        )
                                        if (updated.size > tasks.size) {
                                            tasks = updated.toMutableList()
                                            taskName = ""
                                            taskDescription = ""
                                            taskTime = ""
                                            taskNote = ""
                                            showContent = false
                                        }
                                    },
                                    onCancel = {
                                        showContent = false
                                    }
                                )
                            }
                        }
                    }

                    Box(
                        Modifier
                            .fillMaxSize()
                            .align(Alignment.TopStart)
                            .padding(bottom = 120.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    Strings.get("task.added"),
                                    style = M3Theme.typography.headlineMedium,
                                    color = M3Theme.colorScheme.primary
                                )
                                IconButton(onClick = { toggleTheme() }) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowUp,
                                        contentDescription = if (isDarkTheme) "Cambiar a tema claro" else "Cambiar a tema oscuro",
                                        tint = M3Theme.colorScheme.primary
                                    )
                                }
                            }
                            Spacer(Modifier.height(16.dp))
                            if (tasks.isNotEmpty()) {
                                val scrollState = rememberScrollState()
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(scrollState)
                                ) {
                                    tasks.forEachIndexed { index, task ->
                                        TaskCard(
                                            task = task,
                                            onTaskUpdate = { updatedTask ->
                                                val updatedTasks = tasks.toMutableList()
                                                updatedTasks[index] = updatedTask
                                                tasks = updatedTasks
                                            },
                                            onNoteAdd = { noteText ->
                                                val updatedTasks = tasks.toMutableList()
                                                val updatedNotes = updatedTasks[index].notes.toMutableList()
                                                updatedNotes.add(Note(noteText))
                                                updatedTasks[index] = updatedTasks[index].copy(notes = updatedNotes)
                                                tasks = updatedTasks
                                            },
                                            onNoteDelete = { noteIndex ->
                                                val updatedTasks = tasks.toMutableList()
                                                val updatedNotes = updatedTasks[index].notes.toMutableList()
                                                updatedNotes.removeAt(noteIndex)
                                                updatedTasks[index] = updatedTasks[index].copy(notes = updatedNotes)
                                                tasks = updatedTasks
                                            },
                                            estimatedTime = task.estimatedTime,
                                            onEstimatedTimeChange = { newTime ->
                                                val updatedTasks = tasks.toMutableList()
                                                updatedTasks[index] = updatedTasks[index].copy(estimatedTime = newTime)
                                                tasks = updatedTasks
                                            },
                                            onDeleteTask = {
                                                val updatedTasks = tasks.toMutableList()
                                                updatedTasks.removeAt(index)
                                                tasks = updatedTasks
                                            }
                                        )
                                    }
                                }
                            } else {
                                Box(
                                    Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        Strings.get("task.empty_motivation"),
                                        style = M3Theme.typography.headlineSmall,
                                        color = M3Theme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    FloatingActionButton(
                        onClick = { maximized = false },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(24.dp),
                        containerColor = M3Theme.colorScheme.primaryContainer,
                        contentColor = M3Theme.colorScheme.onPrimaryContainer
                    ) {
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = Strings.get("button.minimize")
                        )
                    }

                    FloatingActionButton(
                        onClick = { showContent = !showContent },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 24.dp, bottom = 88.dp),
                        containerColor = M3Theme.colorScheme.secondaryContainer,
                        contentColor = M3Theme.colorScheme.onSecondaryContainer
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = Strings.get("button.add")
                        )
                    }
                }
            } else {
                TaskTrackerFloating(tasks = tasks, onMaximize = { maximized = true })
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            saveTasksEncrypted(tasks)
        }
    }
}

