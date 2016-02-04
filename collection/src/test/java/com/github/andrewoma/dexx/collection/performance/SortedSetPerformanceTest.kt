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
import com.github.andrewoma.dexx.collection.TreeSet
import com.github.andrewoma.dexx.collection.mutable.MutableTreeSet
import com.github.andrewoma.dexx.collection.performance.PerformanceMeasurement.Result
import org.junit.Test
import kotlin.system.measureNanoTime
import com.github.andrewoma.dexx.collection.Set as DSet

class SortedSetPerformanceTest : SetPerformanceTest() {

    override fun compare(description: String, operations: Int, iterations: Int, f: (builder: Builder<Int, out DSet<Int>>)
    -> PerformanceMeasurement.Result) {
        val java = time(iterations) { f(MutableTreeSet.factory<Int>(null).newBuilder()) }
        val dexx = time(iterations) { f(TreeSet.factory<Int>(null).newBuilder()) }
        compare("SortedSet: $description", operations, java, dexx)
    }

    @Test fun addSorted() {
        addSorted(size = 100, iterations = 1000)
        addSorted(size = 10000, iterations = 1000)
        addSorted(size = 100000, iterations = 100)
        addSorted(size = 1000000, iterations = 10)
    }

    @Test fun addReverseSorted() {
        addReverseSorted(size = 100, iterations = 1000)
        addReverseSorted(size = 10000, iterations = 1000)
        addReverseSorted(size = 100000, iterations = 100)
        addReverseSorted(size = 1000000, iterations = 10)
    }

    fun addSorted(size: Int, iterations: Int) {
        if (disabled()) return

        compare("Add $size ints in order to empty set", operations = size, iterations = iterations) { builder ->
            addSorted(size, builder)
        }
    }

    fun addSorted(size: Int, builder: Builder<Int, out DSet<Int>>): Result {
        var set = builder.build()

        var result = 0L
        val duration = measureNanoTime {
            for (i in 1..size) {
                set = set.add(i)
                result += i
            }
        }

        result += set.size()

        return Result(duration, result)
    }

    fun addReverseSorted(size: Int, iterations: Int) {
        if (disabled()) return

        compare("Add $size ints in reverse order to empty set", operations = size, iterations = iterations) { builder ->
            addReverseSorted(size, builder)
        }
    }

    fun addReverseSorted(size: Int, builder: Builder<Int, out DSet<Int>>): Result {
        var set = builder.build()

        var result = 0L
        val duration = measureNanoTime {
            for (i in size.downTo(1)) {
                set = set.add(i)
                result += i
            }
        }

        result += set.size()

        return Result(duration, result)
    }
}

