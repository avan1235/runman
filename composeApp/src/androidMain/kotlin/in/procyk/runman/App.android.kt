package `in`.procyk.runman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        startKoin {
            androidContext(this@AppActivity)
        }
        setContent { App() }
    }
}

@Preview
@Composable
fun AppPreview() { App() }
