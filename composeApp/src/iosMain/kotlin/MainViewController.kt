import androidx.compose.ui.interop.UIKitViewController
import androidx.compose.ui.window.ComposeUIViewController
import di.dispatcherProvider
import di.initKoin
import di.networkModule
import di.platformModule
import di.sharedModule
import di.viewModelModule
import platform.UIKit.UIViewController

fun MainViewController() = ComposeUIViewController(configure = {
    initKoin {
        modules(sharedModule, platformModule, dispatcherProvider, networkModule, viewModelModule)
    }
}) {
    App()
}

