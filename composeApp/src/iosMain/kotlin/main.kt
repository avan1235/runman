import androidx.compose.ui.window.ComposeUIViewController
import `in`.procyk.runman.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
