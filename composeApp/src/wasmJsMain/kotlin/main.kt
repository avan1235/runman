import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import `in`.procyk.runman.App
import `in`.procyk.runman.exported.KoinHelper
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val body = document.body ?: return
    KoinHelper.startKoin()
    ComposeViewport(body) {
        App()
    }
}
