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

val dispatcherProvider = module {
    single<DispatcherProvider> {
        DefaultDispatcherProvider()
    }
    single {
        val dispatcherProvider: DispatcherProvider = get()
        CoroutineScope(SupervisorJob() + dispatcherProvider.default)
    }
}