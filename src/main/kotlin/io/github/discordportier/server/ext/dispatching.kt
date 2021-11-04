package io.github.discordportier.server.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <R> io(noinline block: suspend CoroutineScope.() -> R): R = withContext(Dispatchers.IO, block)
