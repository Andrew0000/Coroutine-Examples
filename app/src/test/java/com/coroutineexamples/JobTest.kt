package com.coroutineexamples

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class JobTest {

    private val result = mutableListOf<Int>()

    @Test
    fun `When nested Job is child - Parent waits it`() = runTest {
        val job = launch {
            delay(100)
            result += 1
            launch {
                delay(100)
                result += 2
            }
        }
        job.join()
        assertEquals(
            listOf(1, 2),
            result
        )
    }

    @Test
    fun `When nested Job is not child - Parent doesn't wait it`() = runTest {
        val job = launch {
            delay(100)
            result += 1
            // Not a child Job
            launch(Job()) {
                delay(100)
                result += 2
            }
        }
        job.join()
        assertEquals(
            listOf(1),
            result
        )
    }

    @Test
    fun `When nested Job is child - Parent cancels it`() = runTest {
        val job = launch {
            launch {
                delay(200)
                result += 2
            }
            delay(100)
            result += 1
        }
        delay(50)
        job.cancelAndJoin()
        testScheduler.advanceUntilIdle()
        assertEquals(
            listOf(),
            result
        )
    }

    @Test
    fun `When nested Job is not child - Parent doesn't cancel it`() = runTest {
        val job = launch {
            // Not a child Job
            launch(Job()) {
                delay(200)
                result += 2
            }
            delay(100)
            result += 1
        }
        delay(50)
        job.cancelAndJoin()
        testScheduler.advanceUntilIdle()
        assertEquals(
            listOf(2),
            result
        )
    }
}