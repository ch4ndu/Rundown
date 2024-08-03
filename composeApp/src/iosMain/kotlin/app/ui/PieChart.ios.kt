package app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import platform.UIKit.UIViewController

@Composable
actual fun PieChart(
    modifier: Modifier,
    pieEntriesMap: Map<String, Double>,
    setActiveTag: (String) -> Unit
) {
    Box(modifier = modifier) {
        Text("Not Implemented Yet!", modifier = Modifier.align(Alignment.Center))
    }
    val controller = remember { MySwiftUIControllerWrapper() }
    UIKitView(
        modifier = modifier,
        factory = { controller.view }

    )

}

class MySwiftUIControllerWrapper : UIViewController(nibName = null, bundle = null) {
    init {

//        val controller = MySwiftUIController()
//        addChildViewController(controller)
//        view.addSubview(controller.view)
//        controller.view.frame = view.bounds
//        controller.didMoveToParentViewController(this)
    }
}