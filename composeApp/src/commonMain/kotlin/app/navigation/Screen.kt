package app.navigation

import fireflycomposemultiplatform.composeapp.generated.resources.Res
import fireflycomposemultiplatform.composeapp.generated.resources.ic_accounts
import fireflycomposemultiplatform.composeapp.generated.resources.ic_budget
import fireflycomposemultiplatform.composeapp.generated.resources.ic_categories
import fireflycomposemultiplatform.composeapp.generated.resources.ic_dashboard
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource

open class Screen(
    open val title: String,
    val icon: DrawableResource,
    val route: Route
)

object Screens {

    val Accounts = Screen(
        title = "Accounts",
        icon = Res.drawable.ic_accounts,
        route = Route.Accounts
    )

    val Categories =
        Screen(
            title = "Categories",
            icon = Res.drawable.ic_categories,
            route = Route.Categories
        )

    val Budgets = Screen(
        title = "Budgets",
        icon = Res.drawable.ic_budget,
        route = Route.Budgets
    )

    val Dashboard = Screen(
        title = "Dashboard",
        icon = Res.drawable.ic_dashboard,
        route = Route.Dashboard
    )

}

@Serializable
sealed class Route() {
    @Serializable
    data object Accounts : Route() {
        @Serializable
        data class AccountOverview(
            val accountType: String,
            val accountId: String,
            val accountName: String
        )
        @Serializable
        data class AccountCashFlowDetails(
            val accountId: String,
            val startDate: String,
            val endDate: String,
        )
    }

    @Serializable
    data object Categories : Route() {

        @Serializable
        data class CategoryDetails(val category: String)
    }

    @Serializable
    data object Budgets : Route()

    @Serializable
    data object Dashboard : Route() {
        @Serializable
        data object DailySpending
    }

    @Serializable
    data object AuthScreen : Route()
}