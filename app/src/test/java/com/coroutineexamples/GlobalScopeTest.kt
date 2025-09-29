package com.coroutineexamples

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GlobalScopeTest {

    @Test
    @OptIn(DelicateCoroutinesApi::class)
    fun `GlobalScope is not tied to any other scope`() = runTest {
        val result = mutableListOf<Int>()
        GlobalScope.launch {
            delay(100)
            result += 1
        }
        testScheduler.advanceUntilIdle()
        assertEquals(
            listOf(),
            result
        )
    }
}