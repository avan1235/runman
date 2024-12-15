package `in`.procyk.runman.exported

import io.ktor.client.*
import org.koin.core.KoinApplication
import org.koin.dsl.module

object KoinHelper {
    fun startKoin(platformAppDeclaration: KoinApplication.() -> Unit = {}) {
        org.koin.core.context.startKoin {
            modules(
                module {
                    single { HttpClient() }
                }
            )
            platformAppDeclaration()
        }
    }
}