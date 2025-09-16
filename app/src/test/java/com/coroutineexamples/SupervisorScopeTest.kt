package com.coroutineexamples

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.test.Test
import kotlin.test.assertEquals

class SupervisorScopeTest {

    @Test
    fun `When inner coroutines finish - only then supervisorScope exits`() = runTest {
        val result = mutableListOf<Int>()
        result += 1
        supervisorScope {
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
    fun `When exception is thrown in launch - supervisorScope crashes`() = runTest {
        supervisorScope {
            launch {
                delay(100)
                throw TheSampleException()
            }
        }
    }

    @Test
    fun `When exception is thrown in launch with ExceptionHandler - supervisorScope continues`() = runTest {
        val result = mutableListOf<Int>()
        val handler = CoroutineExceptionHandler { _, exception ->
            result += 3
        }
        withContext(handler) {
            result += 1
            supervisorScope {
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
            result += 5
        }
        assertEquals(
            listOf(1, 2, 3, 4, 5),
            result
        )
    }

    @Test
    fun `When exception is thrown in async - supervisorScope continues`() = runTest {
        val result = mutableListOf<Int>()
        supervisorScope {
            async {
                delay(100)
                result += 1
                throw TheSampleException()
            }
            async {
                delay(200)
                result += 2
            }
        }
        assertEquals(
            listOf(1, 2),
            result
        )
    }

}