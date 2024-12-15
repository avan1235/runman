import androidx.compose.ui.window.*
import `in`.procyk.runman.App
import `in`.procyk.runman.exported.KoinHelper

fun main() {
    KoinHelper.startKoin()
    application {
        Window(
            title = "Runman",
            state = rememberWindowState(placement = WindowPlacement.Fullscreen),
            onCloseRequest = ::exitApplication,
            decoration = WindowDecoration.Undecorated(),
            resizable = false,
        ) {
            App()
        }
    }
}
