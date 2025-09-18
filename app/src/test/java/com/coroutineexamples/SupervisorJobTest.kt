package com.coroutineexamples

import com.coroutineexamples.utils.TheSampleException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SupervisorJobTest {

    @Test
    fun `When exception in the scope + SupervisorJob + ExceptionHandler - scope continues`() = runTest {
        val handler = CoroutineExceptionHandler { _, exception ->
            // Empty
        }
        val scope = CoroutineScope(SupervisorJob() + handler)
        val result = mutableListOf<Int>()
        val job = scope.launch {
            delay(100)
            throw TheSampleException()
        }
        val job2 = scope.launch {
            delay(200)
            result += 1
        }
        job.join()
        job2.join()
        assertEquals(
            listOf(1),
            result
        )
    }

    @Test
    fun `When exception in the scope + Job + ExceptionHandler - scope fails`() = runTest {
        val handler = CoroutineExceptionHandler { _, exception ->
            // Empty
        }
        val scope = CoroutineScope(Job() + handler)
        val result = mutableListOf<Int>()
        val job = scope.launch {
            delay(100)
            throw TheSampleException()
        }
        val job2 = scope.launch {
            delay(200)
            // Will not happen because scope fails
            result += 1
        }
        job.join()
        job2.join()
        assertEquals(
            listOf(),
            result
        )
    }

    @Test(expected = TheSampleException::class)
    fun `When exception in the scope + SupervisorJob + launch - crash`() = runTest {
        val scope = CoroutineScope(SupervisorJob())
        val result = mutableListOf<Int>()
        val job = scope.launch {
            delay(100)
            throw TheSampleException()
        }
        val job2 = scope.launch {
            delay(200)
            result += 1
        }
        job.join()
        job2.join()
    }

    @Test
    fun `When exception in the scope + SupervisorJob + async - scope continues`() = runTest {
        val scope = CoroutineScope(SupervisorJob())
        val result = mutableListOf<Int>()
        val job = scope.async {
            delay(100)
            throw TheSampleException()
        }
        val job2 = scope.async {
            delay(200)
            result += 1
        }
        job.join()
        job2.join()
        assertEquals(
            listOf(1),
            result
        )
    }

}