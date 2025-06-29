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

@file:JvmName("DispatcherProviderKt")

package di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext
import kotlin.jvm.JvmName


interface DispatcherProvider {

    /**
     * In production code, this maps to [kotlinx.coroutines.Dispatchers.Default]
     */
    val default: CoroutineDispatcher

    /**
     * In production code, this maps to [kotlinx.coroutines.Dispatchers.Main]
     */
    val main: MainCoroutineDispatcher

    /**
     * In production code, this maps to [kotlinx.coroutines.Dispatchers.Unconfined]
     */
    val unconfined: CoroutineDispatcher

    /**
     * In production code, this maps to [kotlinx.coroutines.Dispatchers.IO]
     */
    val io: CoroutineDispatcher

    /**
     * In production code, this maps to [kotlinx.coroutines.NonCancellable]
     */
    val nonCancellable: CoroutineContext
}

/**
 * If your application has dependency injection, do _NOT_ instantiate this.
 * Instead, inject the parent interface [DispatcherProvider]:
 *
 * ```
 * class Foo @Inject constructor(
 *   private val dispatcherProvider: DispatcherProvider
 * )
 * ```
 */
class DefaultDispatcherProvider : DispatcherProvider {

    override val default: CoroutineDispatcher = Dispatchers.Default
    override val main: MainCoroutineDispatcher = Dispatchers.Main
    override val unconfined: CoroutineDispatcher = Dispatchers.Unconfined
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val nonCancellable: CoroutineContext = NonCancellable
}