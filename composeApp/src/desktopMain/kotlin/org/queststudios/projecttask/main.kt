package org.queststudios.projecttask

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.dp

//NO TOCAR
fun main() = application {
    val windowState = rememberWindowState(
        width = 340.dp,
        height = 180.dp,
        placement = androidx.compose.ui.window.WindowPlacement.Floating
    )
    Window(
        onCloseRequest = ::exitApplication,
        title = "ProjectTask",
        state = windowState,
        resizable = true
    ) {
        App()
    }
}