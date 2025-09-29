package com.coroutineexamples

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RunBlockingTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Default.limitedParallelism(1))
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Expected a failure with TestTimedOutException
    // (junit's "expected" keyword doesn't work in this scenario)
    @Test(timeout = 2_000)
    fun `Nested runBlocking blocks its thread and may cause a deadlock if it's the only thread in the dispatcher`() {
        runBlocking {
            withContext(Dispatchers.Main) {
                runBlocking {
                    withContext(Dispatchers.Main) {
                        println("Done")
                    }
                }
            }
        }
    }
}