import androidx.compose.ui.window.ComposeUIViewController
import di.allModules
import di.initKoin

fun MainViewController() = ComposeUIViewController(configure = {
    initKoin {
        modules(allModules)
    }
}) {
    App()
}

