package com.coroutineexamples

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
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

    @Test
    fun `When cancel + nested launch - coroutine stops`() = runTest {
        val result = mutableListOf<Int>()
        val job = launch {
            launch {
                delay(100)
                result += 1
            }
        }
        delay(50)
        job.cancelAndJoin()
        delay(100)
        assertEquals(
            listOf(),
            result
        )
    }

    @Test
    fun `When cancel + nested launch + not a child Job - coroutine continues`() = runTest {
        val result = mutableListOf<Int>()
        val job = launch {
            // Another Job which is not a child of the parent Job
            launch(Job()) {
                delay(100)
                result += 1
            }
        }
        delay(50)
        job.cancelAndJoin()
        delay(100)
        assertEquals(
            listOf(1),
            result
        )
    }

}