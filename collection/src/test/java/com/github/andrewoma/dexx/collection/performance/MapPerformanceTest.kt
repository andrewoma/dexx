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
import com.github.andrewoma.dexx.collection.HashMap
import com.github.andrewoma.dexx.collection.Pair
import com.github.andrewoma.dexx.collection.mutable.MutableHashMap
import com.github.andrewoma.dexx.collection.performance.PerformanceMeasurement.Result
import org.junit.Test
import kotlin.system.measureNanoTime
import com.github.andrewoma.dexx.collection.Map as DMap

open class MapPerformanceTest : PerformanceMeasurement {

    @Test fun put() {
        put(size = 100, operations = 10000, iterations = 1000)
        put(size = 10000, operations = 10000, iterations = 1000)
        put(size = 100000, operations = 10000, iterations = 100)
        put(size = 1000000, operations = 10000, iterations = 10)
    }

    @Test fun containsKey() {
        containsKey(size = 100, operations = 100, iterations = 1000)
        containsKey(size = 10000, operations = 10000, iterations = 1000)
        containsKey(size = 100000, operations = 100000, iterations = 100)
        containsKey(size = 1000000, operations = 1000000, iterations = 10)
    }

    @Test fun remove() {
        remove(size = 100, operations = 100, iterations = 1000)
        remove(size = 10000, operations = 10000, iterations = 1000)
        remove(size = 100000, operations = 100000, iterations = 100)
        remove(size = 1000000, operations = 1000000, iterations = 10)
    }

    fun put(size: Int, operations: Int, iterations: Int) {
        if (disabled()) return

        val randomInts = uniqueRandomInts(size)
        val operationInts = randomInts(operations)

        compare("Put $operations random ints to map of size $size", operations, iterations = iterations) { builder ->
            put(randomInts, operationInts, builder)
        }
    }

    fun containsKey(size: Int, operations: Int, iterations: Int) {
        if (disabled()) return

        val randomInts = uniqueRandomInts(size)
        val operationInts = randomAccesses(operations, randomInts, true)

        compare("Check map of size $size contains element $operations times", operations, iterations = iterations) { builder ->
            containsKey(randomInts, operationInts, builder)
        }
    }

    fun remove(size: Int, operations: Int, iterations: Int) {
        if (disabled()) return

        val randomInts = uniqueRandomInts(size)
        val operationInts = randomAccesses(operations, randomInts, true)

        compare("Remove all items randomly from a map of size $size elements", operations, iterations = iterations) { builder ->
            remove(randomInts, operationInts, builder)
        }
    }

    fun put(randomInts: IntArray, operationInts: IntArray, builder: Builder<Pair<Int, Int>, out DMap<Int, Int>>): Result {
        for (i in randomInts) {
            builder.add(Pair(i, i))
        }
        var map = builder.build()

        var result = 0L
        val duration = measureNanoTime {
            for (i in operationInts) {
                map = map.put(i, i)
                result += i
            }
        }

        result += map.size()

        return Result(duration, result)
    }

    fun containsKey(randomInts: IntArray, operationInts: IntArray, builder: Builder<Pair<Int, Int>, out DMap<Int, Int>>): Result {
        for (i in randomInts) {
            builder.add(Pair(i, i))
        }
        val map = builder.build()

        var result = 0L
        val duration = measureNanoTime {
            for (i in operationInts) {
                val found = map.containsKey(i)
                result += if (found) 1 else 0
            }
        }

        result += map.size()

        return Result(duration, result)
    }

    fun remove(randomInts: IntArray, operationInts: IntArray, builder: Builder<Pair<Int, Int>, out DMap<Int, Int>>): Result {
        for (i in randomInts) {
            builder.add(Pair(i, i))
        }
        var map = builder.build()

        var result = 0L
        val duration = measureNanoTime {
            for (i in operationInts) {
                map = map.remove(i)
                result += i
            }
        }

        result += map.size()

        return Result(duration, result)
    }

    open fun compare(description: String, operations: Int, iterations: Int, f: (builder: Builder<Pair<Int, Int>, out DMap<Int, Int>>) -> PerformanceMeasurement.Result) {
        val java = time(iterations) { f(MutableHashMap.factory<Int, Int>().newBuilder()) }
        val dexx = time(iterations) { f(HashMap.factory<Int, Int>().newBuilder()) }
        compare("Map: $description", operations, java, dexx)
    }
}

