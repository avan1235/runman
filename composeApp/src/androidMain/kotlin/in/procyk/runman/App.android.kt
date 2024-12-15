package `in`.procyk.runman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import `in`.procyk.runman.exported.KoinHelper
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        KoinHelper.startKoin {
            androidContext(this@AppActivity)
        }
        setContent { App() }
    }
}
