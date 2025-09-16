package com.coroutineexamples

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals

class CoroutineScopeTest {

    @Test
    fun `When inner coroutines finish - only then coroutineScope exits`() = runTest {
        val result = mutableListOf<Int>()
        result += 1
        coroutineScope {
            result += 2
            launch {
                delay(200)
                result += 5
            }
            launch {
                delay(100)
                result += 4
            }
            result += 3
        }
        result += 6
        assertEquals(
            listOf(1, 2, 3, 4, 5, 6),
            result
        )
    }

    @Test(expected = TheSampleException::class)
    fun `When exception is thrown in launch - coroutineScope crashes`() = runTest {
        coroutineScope {
            launch {
                delay(100)
                throw TheSampleException()
            }
        }
    }

    @Test(expected = TheSampleException::class)
    fun `When exception is thrown in launch with ExceptionHandler - coroutineScope crashes`() = runTest {
        val handler = CoroutineExceptionHandler { _, exception ->
            println("handler: $exception")
        }
        withContext(handler) {
            coroutineScope {
                launch {
                    delay(100)
                    throw TheSampleException()
                }
            }
        }
    }

    @Test
    fun `When exception in launch with ExceptionHandler and additional supervisorScope - coroutineScope continues`() = runTest {
        val result = mutableListOf<Int>()
        val handler = CoroutineExceptionHandler { _, exception ->
            result += 3
            println("handler: $exception")
        }
        withContext(handler) {
            coroutineScope {
                supervisorScope {
                    result += 1
                    launch {
                        delay(100)
                        result += 2
                        throw TheSampleException()
                    }
                    launch {
                        delay(200)
                        result += 4
                    }
                }
            }
            result += 5
        }
        assertEquals(
            listOf(1, 2, 3, 4, 5),
            result
        )
    }

    @Test(expected = TheSampleException::class)
    fun `When exception is thrown in async - coroutineScope crashes`() = runTest {
        coroutineScope {
            async {
                delay(100)
                throw TheSampleException()
            }
        }
    }

}