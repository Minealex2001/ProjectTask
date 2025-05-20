package org.queststudios.projecttask

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.dp
import java.io.File
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.queststudios.projecttask.localization.Strings

//NO TOCAR
fun getLanguageFromArgsOrConfig(args: Array<String>): String {
    // 1. Check command-line args
    val langArg = args.firstOrNull { it.startsWith("--lang=") }
    if (langArg != null) {
        return langArg.removePrefix("--lang=")
    }
    // 2. Check settings.json
    val configFile = File(System.getProperty("user.home"), "Documents/ProjectTask/settings.json")
    if (configFile.exists()) {
        try {
            val json = Json.parseToJsonElement(configFile.readText()).jsonObject
            val lang = json["language"]?.jsonPrimitive?.content
            if (!lang.isNullOrBlank()) return lang
        } catch (_: Exception) {}
    }
    // 3. Default to Spanish
    return "es"
}

fun main(args: Array<String>) = application {
    val lang = getLanguageFromArgsOrConfig(args)
    Strings.load(lang)
    val windowState = rememberWindowState(
        width = 340.dp,
        height = 180.dp,
        placement = androidx.compose.ui.window.WindowPlacement.Floating
    )
    Window(
        onCloseRequest = ::exitApplication,
        title = Strings.get("app.title"),
        state = windowState,
        resizable = true
    ) {
        App()
    }
}