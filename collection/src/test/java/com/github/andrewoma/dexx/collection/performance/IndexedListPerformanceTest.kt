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

import com.github.andrewoma.dexx.collection.Builder
import com.github.andrewoma.dexx.collection.Vector
import com.github.andrewoma.dexx.collection.mutable.MutableArrayList
import com.github.andrewoma.dexx.collection.performance.PerformanceMeasurement.Result
import org.junit.Test
import kotlin.system.measureNanoTime
import com.github.andrewoma.dexx.collection.List as DList

open class IndexedListPerformanceTest : PerformanceMeasurement {

    @Test fun append() {
        append(size = 100, operations = 10000, iterations = 1000)
        append(size = 10000, operations = 10000, iterations = 1000)
        append(size = 100000, operations = 10000, iterations = 100)
        append(size = 1000000, operations = 10000, iterations = 10)
    }

    @Test fun prepend() {
        prepend(size = 100, operations = 10000, iterations = 1000)
        prepend(size = 10000, operations = 10000, iterations = 1000)
        prepend(size = 100000, operations = 10000, iterations = 100)
    }

    @Test fun randomGet() {
        randomGet(size = 100, operations = 10000, iterations = 1000)
        randomGet(size = 10000, operations = 10000, iterations = 1000)
        randomGet(size = 100000, operations = 10000, iterations = 100)
        randomGet(size = 1000000, operations = 10000, iterations = 10)
    }

    @Test fun randomSet() {
        randomSet(size = 100, operations = 10000, iterations = 1000)
        randomSet(size = 10000, operations = 10000, iterations = 1000)
        randomSet(size = 100000, operations = 10000, iterations = 100)
        randomSet(size = 1000000, operations = 10000, iterations = 10)
    }

    @Test fun iterate() {
        iterate(size = 100000, operations = 100000, iterations = 1000)
        iterate(size = 1000000, operations = 1000000, iterations = 1000)
    }

    fun append(size: Int, operations: Int, iterations: Int) {
        if (disabled()) return

        compare("Append $operations values to indexed list of size $size", operations, iterations = iterations) { builder ->
            append(size, operations, builder)
        }
    }

    fun prepend(size: Int, operations: Int, iterations: Int) {
        if (disabled()) return

        compare("Prepend $operations values to indexed list of size $size", operations, iterations = iterations) { builder ->
            prepend(size, operations, builder)
        }
    }

    fun randomGet(size: Int, operations: Int, iterations: Int) {
        if (disabled()) return

        val operationInts = randomAccesses(operations, IntArray(size), false)

        compare("Randomly get $operations values by index from indexed list of size $size", operations, iterations = iterations) { builder ->
            randomGet(size, operationInts, builder)
        }
    }

    fun randomSet(size: Int, operations: Int, iterations: Int) {
        if (disabled()) return

        val operationInts = randomAccesses(operations, IntArray(size), false)

        compare("Randomly set $operations values by index from indexed list of size $size", operations, iterations = iterations) { builder ->
            randomSet(size, operationInts, builder)
        }
    }

    fun iterate(size: Int, operations: Int, iterations: Int) {
        if (disabled()) return

        compare("Iterate indexed list of size $size", operations, iterations = iterations) { builder ->
            iterate(size, builder)
        }
    }

    fun append(size: Int, operations: Int, builder: Builder<Int, out DList<Int>>): Result {
        for (i in 1..size) {
            builder.add(i)
        }
        var list = builder.build()

        var result = 0L
        val duration = measureNanoTime {
            for (i in 1..operations) {
                list = list.append(i)
                result += i
            }
        }

        result += list.size()

        return Result(duration, result)
    }

    fun prepend(size: Int, operations: Int, builder: Builder<Int, out DList<Int>>): Result {
        for (i in 1..size) {
            builder.add(i)
        }
        var list = builder.build()

        var result = 0L
        val duration = measureNanoTime {
            for (i in 1..operations) {
                list = list.prepend(i)
                result += i
            }
        }

        result += list.size()

        return Result(duration, result)
    }

    fun randomGet(size: Int, operationInts: IntArray, builder: Builder<Int, out DList<Int>>): Result {
        for (i in 1..size) {
            builder.add(i)
        }
        val list = builder.build()

        var result = 0L
        val duration = measureNanoTime {
            for (i in operationInts) {
                result += list[i].toLong()
            }
        }

        result += list.size()

        return Result(duration, result)
    }

    fun randomSet(size: Int, operationInts: IntArray, builder: Builder<Int, out DList<Int>>): Result {
        for (i in 1..size) {
            builder.add(i)
        }
        val list = builder.build()

        var result = 0L
        val duration = measureNanoTime {
            for (i in operationInts) {
                list[i] = i
                result += i
            }
        }

        result += list.size()

        return Result(duration, result)
    }

    fun iterate(size: Int, builder: Builder<Int, out DList<Int>>): Result {
        for (i in 1..size) {
            builder.add(i)
        }
        val list = builder.build()

        var result = 0L
        val duration = measureNanoTime {
            for (i in list) {
                result += i
            }
        }

        result += list.size()

        return Result(duration, result)
    }

    open fun compare(description: String, operations: Int, iterations: Int, f: (builder: Builder<Int, out DList<Int>>) -> PerformanceMeasurement.Result) {
        val java = time(iterations) { f(MutableArrayList.factory<Int>().newBuilder()) }
        val dexx = time(iterations) { f(Vector.factory<Int>().newBuilder()) }
        compare("IndexedList: $description", operations, java, dexx)
    }
}

