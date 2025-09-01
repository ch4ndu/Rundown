/*
 * Copyright (c) 2025 https://github.com/ch4ndu
 *
 *  This file is part of Rundown (https://github.com/ch4ndu/Rundown).
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see https://www.gnu.org/licenses/.
 */

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import app.theme.FireflyAppTheme
import app.viewmodel.SyncState
import app.viewmodel.SyncWithServerViewModel
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