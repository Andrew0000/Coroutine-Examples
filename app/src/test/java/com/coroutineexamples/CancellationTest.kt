package com.coroutineexamples

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import java.lang.Exception
import kotlin.test.Test
import kotlin.test.assertEquals

class CancellationTest {

    @Test
    fun `When catch CancellationException - coroutine continues`() = runTest {
        val result = mutableListOf<Int>()
        val job = launch {
            try {
                delay(100)
            } catch (e: Exception) {
                println("catch: $e")
            }
            result += 1
        }
        delay(50)
        job.cancelAndJoin()
        assertEquals(
            listOf(1),
            result
        )
    }

    @Test
    fun `When don't catch CancellationException - coroutine stops`() = runTest {
        val result = mutableListOf<Int>()
        val job = launch {
            try {
                delay(100)
            } catch (e: Exception) {
                println("catch: $e")
                if (e is CancellationException) throw e
            }
            result += 1
        }
        delay(50)
        job.cancelAndJoin()
        assertEquals(
            listOf(),
            result
        )
    }
}