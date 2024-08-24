@file:OptIn(KoinExperimentalAPI::class)

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.navigation.AppNavGraph
import app.navigation.Route
import app.navigation.Screen
import app.navigation.Screens
import app.theme.FireflyAppTheme
import app.theme.NavBarDarkColor
import app.theme.NavBarLightColor
import app.ui.screens.AuthScreen
import app.ui.screens.FirstSyncScreen
import app.viewmodel.AuthViewModel
import di.networkModule
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.lighthousegames.logging.logging

@Composable
@Preview
fun App() {
    FireflyAppTheme(isSystemInDarkTheme()) {
        KoinContext {
            var authComplete by remember { mutableStateOf(false) }
            var syncNeeded by remember { mutableStateOf(true) }

            val authViewModel = koinViewModel<AuthViewModel>()
            val onLoginClick = remember {
                { url: String, token: String ->
                    authViewModel.tryAuth(url, token)
                }
            }
            if (!authComplete) {
                AuthScreen(
                    authViewModel = authViewModel,
                    onLoginClick = onLoginClick,
                    onAuthSuccess = {
                    unloadKoinModules(networkModule)
                    loadKoinModules(networkModule)
                    authComplete = true
                    },
                    runDemoClick = {
                        syncNeeded = false
                        authComplete = true
                        authViewModel.runDemo()
                    }
                )
            } else if (syncNeeded) {
                FirstSyncScreen {
                    syncNeeded = false
                }
            } else {
                mainUi(forceShowBottomBarState = remember { mutableStateOf(null) })
            }
        }
    }
}

@Composable
private fun mainUi(
    forceShowBottomBarState: State<Boolean?>
): NavHostController {
    val navTabs =
        listOf(
            Screens.Accounts,
            Screens.Categories,
            Screens.Budgets,
            Screens.Dashboard,
        )
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val forceShowBottomBar by forceShowBottomBarState
    val navigationPrefix = remember { "app.navigation.Route." }
    val showBottomBarForCurrentScreen = remember(navBackStackEntry) {
        navTabs.any { tab ->
            navBackStackEntry?.destination?.route == "$navigationPrefix${tab.title}"
        }
    }
    val showBottomBar = forceShowBottomBar ?: showBottomBarForCurrentScreen

    FireflyAppTheme(darkTheme = isSystemInDarkTheme()) {
        Scaffold(topBar = {},
            bottomBar = {
                if (showBottomBar)
                    BottomBar(navController = navController, tabs = navTabs)
            }
        ) { innerPadding ->
            AppNavGraph(
                innerPadding = innerPadding,
                navController = navController,
                startDestination = Route.Accounts
            )
        }
    }
    return navController
}

private val log = logging()

@Composable
private fun BottomBar(
    navController: NavHostController,
    tabs: List<Screen>
) {
    NavigationBar(
        containerColor = if (isSystemInDarkTheme()) NavBarDarkColor else NavBarLightColor,
        tonalElevation = 10.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route
        tabs.forEach { tab ->
            val label = tab.title
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(tab.icon),
                        contentDescription = label
                    )
                },
                label = { Text(label) },
                selected = navBackStackEntry?.destination?.hierarchy?.any { it.route?.contains(tab.route.toString()) == true } == true,
                onClick = {
                    if (currentDestination?.contains(tab.route.toString()) == false) {
                        navController.navigate(tab.route) {
                            popUpTo(Screens.Accounts.route)
//                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                },
            )
        }
    }
}