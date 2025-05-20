@file:OptIn(KoinExperimentalAPI::class)

package app.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.theme.FireflyAppTheme
import app.viewmodel.SyncState
import app.viewmodel.SyncWithServerViewModel
import collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@Composable
fun FirstSyncScreen(
    syncWithServerViewModel: SyncWithServerViewModel = koinViewModel(),
    onSyncComplete: () -> Unit
) {
    val syncStatus by syncWithServerViewModel.syncStatus
//        .distinctUntilChanged { old, new -> old == new }
        .collectAsStateWithLifecycle(initialValue = SyncState.IDLE)
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = FireflyAppTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.align(Alignment.Center).wrapContentSize()) {
                when (syncStatus) {
                    SyncState.IDLE -> {
                        Text("Initializing")
                    }

                    SyncState.InProgress -> {
                        Text("Syncing data with server")
                    }

                    SyncState.Done -> {
                        Text("Ready!")
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
        LaunchedEffect(Unit) {
            syncWithServerViewModel.startFirstSync()
        }
        when (syncStatus) {
            SyncState.Done -> {
                onSyncComplete.invoke()
            }

            SyncState.IDLE,
            SyncState.InProgress -> {

            }
        }
    }

}