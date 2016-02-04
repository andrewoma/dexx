/*
 * Copyright (c) 2014 Andrew O'Malley
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.andrewoma.dexx.collection.performance

import com.github.andrewoma.dexx.TestMode
import java.security.SecureRandom
import java.util.*
import kotlin.test.assertEquals
import com.github.andrewoma.dexx.collection.Set as DSet

interface PerformanceMeasurement {
    data class Result(val nanoDuration: Long, val result: Long)

    fun uniqueRandomInts(size: Int): IntArray {
        val random = SecureRandom()
        val generated = LinkedHashSet<Int>()
        while (generated.size < size) {
            generated.add(random.nextInt())
        }

        val ints = IntArray(generated.size)
        var i = 0
        for (value in generated) {
            ints[i++] = value
        }

        return ints
    }

    fun randomInts(size: Int): IntArray {
        val random = SecureRandom()
        val ints = IntArray(size)
        for (i in 0..size - 1) {
            ints[i] = random.nextInt()
        }

        return ints
    }

    fun randomAccesses(size: Int, data: IntArray, returnValue: Boolean): IntArray {
        val random = SecureRandom()
        val accesses = IntArray(size)
        for (i in 0..size - 1) {
            val index = random.nextInt(data.size)
            accesses[i] = (if (returnValue)
                data[index]
            else
                index)
        }

        return accesses
    }

    fun compare(description: String, operations: Int, java: Result, dexx: Result) {
        fun nanoPerOp(nanoDuration: Long) = nanoDuration.toDouble() / operations.toDouble()

        fun delta(java: Long, dexx: Long): String {
            return if (java > dexx) {
                "Dexx is ${"%.2f".format(java.toDouble() / dexx.toDouble())} times faster"
            } else {
                "Java is ${"%.2f".format(dexx.toDouble() / java.toDouble())} times faster"
            }
        }

        assertEquals(java.result, dexx.result)
        println("BENCHMARK: $description: Java: ${nanoPerOp(java.nanoDuration)}ns/op Dexx: ${nanoPerOp(dexx.nanoDuration)}ns/op. ${delta(java.nanoDuration, dexx.nanoDuration)}")
    }

    fun time(iterations: Int, f: () -> Result): Result {
        var last: Result? = null
        val times = arrayListOf<Long>()
        repeat(iterations) {
            val result = f()
            if (last != null) {
                assertEquals(last!!.result, result.result)
            }
            last = result
            times.add(result.nanoDuration)
        }

        return Result(times.min()!!, last!!.result)
    }

    fun disabled() = !TestMode.isEnabled(TestMode.BENCHMARK)
}

