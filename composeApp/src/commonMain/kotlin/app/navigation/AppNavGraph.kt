package app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import app.theme.FireflyAppTheme
import app.ui.screens.AccountCashFlowDetails
import app.ui.screens.AccountOverviewScreen
import app.ui.screens.AccountsScreen
import app.ui.screens.BudgetListOverviewScreen
import app.ui.screens.CategoriesOverviewScreen
import app.ui.screens.CategoryDetailsScreen
import app.ui.screens.DailySpendingDetailsScreen
import app.ui.screens.HomeScreen
import data.database.serializers.DateSerializer
import kotlinx.datetime.format
import org.lighthousegames.logging.logging

@Composable
fun AppNavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    startDestination: Route
) {
    NavHost(
        navController = navController,
        modifier = Modifier.padding(innerPadding),
        startDestination = startDestination
    ) {
        addCategoriesScreen(navController)
        addBudgetsScreen(navController)
        addAccountsScreen(navController)
        addDashboardScreen(navController)
    }
}

private fun NavGraphBuilder.addCategoriesScreen(navController: NavHostController) {
    composable<Route.Categories> {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = FireflyAppTheme.colorScheme.background
        ) {
            CategoriesOverviewScreen(onCategoryDetailsClick = { category ->
                navController.navigate(
                    Route.Categories.CategoryDetails(category)
                )
            }, onBack = {
                navController.popBackStack()
            })
        }
    }

    composable<Route.Categories.CategoryDetails> {
        val categoryDetails: Route.Categories.CategoryDetails = it.toRoute()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = FireflyAppTheme.colorScheme.background
        ) {
            CategoryDetailsScreen(selectedCategory = categoryDetails.category) {
                navController.popBackStack()
            }
        }
    }
}

private fun NavGraphBuilder.addBudgetsScreen(navController: NavHostController) {
    composable<Route.Budgets> {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = FireflyAppTheme.colorScheme.background
        ) {
            BudgetListOverviewScreen()
        }
    }
}

private fun NavGraphBuilder.addDashboardScreen(navController: NavHostController) {
    composable<Route.Dashboard> {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = FireflyAppTheme.colorScheme.background
        ) {
            HomeScreen {
                navController.navigate(Route.Dashboard.DailySpending)
            }
        }
    }
    composable<Route.Dashboard.DailySpending> {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = FireflyAppTheme.colorScheme.background
        ) {
            DailySpendingDetailsScreen {
                navController.popBackStack()
            }
        }
    }
}

private val log = logging()

private fun NavGraphBuilder.addAccountsScreen(navController: NavHostController) {
    composable<Route.Accounts> {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            AccountsScreen(
                onAccountClicked = { accountType, accountId, accountName ->
                    navController.navigate(
                        Route.Accounts.AccountOverview(
                            accountType = accountType,
                            accountId = accountId,
                            accountName = accountName
                        )
                    )
                }
            )
        }
    }

    composable<Route.Accounts.AccountOverview> {
        val accountOverview: Route.Accounts.AccountOverview = it.toRoute()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = FireflyAppTheme.colorScheme.background
        ) {
            AccountOverviewScreen(
                accountType = accountOverview.accountType,
                accountId = accountOverview.accountId,
                accountName = accountOverview.accountName,
                onBack = {
                    navController.popBackStack(route = Route.Accounts, inclusive = false)
                },
                onAccountCashFlowDetailsClick = { dateRange ->
                    log.d { "chandu:startDate:${dateRange.startDate.format(DateSerializer.isoFormat)}" }
                    log.d { "chandu:endDate:${dateRange.endDate.format(DateSerializer.isoFormat)}" }
                    navController.navigate(
                        Route.Accounts.AccountCashFlowDetails(
                            accountId = accountOverview.accountId,
                            startDate = dateRange.startDate.format(DateSerializer.isoFormat),
                            endDate = dateRange.endDate.format(DateSerializer.isoFormat)
                        )
                    )
                }
            )
        }
    }
    composable<Route.Accounts.AccountCashFlowDetails> {
        val accountCashFlowDetails: Route.Accounts.AccountCashFlowDetails = it.toRoute()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = FireflyAppTheme.colorScheme.background
        ) {
            AccountCashFlowDetails(
                accountId = accountCashFlowDetails.accountId,
                startDate = accountCashFlowDetails.startDate,
                endDate = accountCashFlowDetails.endDate
            ) {
                navController.popBackStack()
            }
        }
    }
}