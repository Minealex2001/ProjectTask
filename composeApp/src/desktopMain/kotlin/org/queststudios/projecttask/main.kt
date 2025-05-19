package org.queststudios.projecttask

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.dp
import org.queststudios.projecttask.localization.Strings

//NO TOCAR
fun main() = application {
    Strings.load("es") // Cambia a "en" para inglés
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