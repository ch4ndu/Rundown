@file:OptIn(KoinExperimentalAPI::class)

package app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.theme.FireflyAppTheme
import app.ui.AppBarWithOptions
import app.ui.ThemedCard
import app.ui.TopAppBarActionButton
import app.ui.TopAppBarIcon
import app.ui.getTextColorForAmount
import app.viewmodel.AccountsViewModel
import domain.repository.ConnectivityStateManager
import domain.repository.HealthCheck
import fireflycomposemultiplatform.composeapp.generated.resources.Res
import fireflycomposemultiplatform.composeapp.generated.resources.ic_circle
import getDisplayWithCurrency
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import org.lighthousegames.logging.logging

private val log = logging()

@Composable
fun AccountsScreen(
    accountsViewModel: AccountsViewModel = koinViewModel(),
    connectivityStateManager: ConnectivityStateManager = koinInject(),
    onAccountClicked: (accountType: String, accountId: String, accountName: String) -> Unit
) {
    val accountItemsList =
        accountsViewModel.accountList.collectAsStateWithLifecycle(initialValue = null)

    val listState = rememberLazyListState()
    val dimensions = FireflyAppTheme.dimensions
    val isConnectedToServer by connectivityStateManager.isConnectedToServer.collectAsStateWithLifecycle(
        initialValue = HealthCheck.Unknown,
        minActiveState = Lifecycle.State.RESUMED
    )
    log.d { "isConnectedToServer:$isConnectedToServer" }
    val connectedTint by remember(isConnectedToServer) {
        mutableStateOf(
            when (isConnectedToServer) {
                HealthCheck.Connected -> Color.Green
                HealthCheck.Failed,
                HealthCheck.ExpiredToken -> Color.Red

                else -> null
            }
        )
    }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            AppBarWithOptions(title = "Accounts") {
                connectedTint?.let {
                    TopAppBarIcon(
                        painter = painterResource(resource = Res.drawable.ic_circle),
                        description = "",
                        tint = it
                    )
                }
                TopAppBarActionButton(
                    imageVector = Icons.Outlined.Refresh,
                    description = "Refresh"
                ) {
                    accountsViewModel.refreshData()
                }
            }
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .background(FireflyAppTheme.colorScheme.background)
        ) {
            val lastSynced =
                accountsViewModel.lastSyncedAt.collectAsStateWithLifecycle(initialValue = "unknown")
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(dimensions.contentMargin)
            ) {
                accountItemsList.value?.forEach { item ->
                    item {
                        ThemedCard(
                            onClick = {
                                onAccountClicked.invoke(
                                    item.attributes.type,
                                    "" + item.id,
                                    item.attributes.name
                                )
                            }
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(dimensions.listSpacing)
                                    .wrapContentHeight()
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = item.attributes.name,
                                    style = FireflyAppTheme.typography.titleLarge,
                                )
                                val currentBalance =
                                    item.attributes.current_balance.toDouble()
                                val currencySymbol =
                                    item.attributes.currency_symbol ?: "$"
                                Row {
                                    Text(
                                        text = "Current Balance: ",
                                        style = FireflyAppTheme.typography.bodyLarge,
                                    )
                                    Text(
                                        text = currentBalance.getDisplayWithCurrency(
                                            currencySymbol
                                        ),
                                        style = FireflyAppTheme.typography.bodyLarge
                                            .copy(
                                                color = currentBalance.getTextColorForAmount()
                                            ),
                                    )

                                }
                                Spacer(modifier = Modifier.height(5.dp))
                            }
                        }
                    }

                }
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                }

                item {
                    Text(
                        text = "LastSynced: ${lastSynced.value}",
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    )
                }
                item { Spacer(modifier = Modifier.height(dimensions.contentMargin)) }
            }
        }
        LaunchedEffect(isConnectedToServer) {
            if (isConnectedToServer == HealthCheck.ExpiredToken) {
                val result = snackbarHostState.showSnackbar(
                    message = "Need to Relogin",
                    actionLabel = "Go"
                )
                when (result) {
                    SnackbarResult.Dismissed -> {}
                    SnackbarResult.ActionPerformed -> {
                        //TODO launch login screen
                    }
                }
            }
        }
    }
}