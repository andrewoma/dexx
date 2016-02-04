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

package com.github.andrewoma.dexx.collection

import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class SortedSetsTest {
    val c = Comparator<Int> { o1, o2 -> o1.compareTo(o2) * -1 }

    private fun <T> build(comparator: Comparator<in T>?, vararg ts: T): SortedSet<T> {
        val builder = TreeSet.factory<T>(comparator).newBuilder()
        for (t in ts) {
            builder.add(t)
        }
        return builder.build()
    }

    @Test fun of() {
        assertEquals(build<Int>(null), SortedSets.of<Int>())
        assertEquals(build(null, 1), SortedSets.of(1))
        assertEquals(build(null, 1, 2), SortedSets.of(1, 2))
        assertEquals(build(null, 1, 2, 3), SortedSets.of(1, 2, 3))
        assertEquals(build(null, 1, 2, 3, 4), SortedSets.of(1, 2, 3, 4))
        assertEquals(build(null, 1, 2, 3, 4, 5), SortedSets.of(1, 2, 3, 4, 5))
        assertEquals(build(null, 1, 2, 3, 4, 5, 6), SortedSets.of(1, 2, 3, 4, 5, 6))
        assertEquals(build(null, 1, 2, 3, 4, 5, 6, 7), SortedSets.of(1, 2, 3, 4, 5, 6, 7))
        assertEquals(build(null, 1, 2, 3, 4, 5, 6, 7, 8), SortedSets.of(1, 2, 3, 4, 5, 6, 7, 8))
        assertEquals(build(null, 1, 2, 3, 4, 5, 6, 7, 8, 9), SortedSets.of(1, 2, 3, 4, 5, 6, 7, 8, 9))
        assertEquals(build(null, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10), SortedSets.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        assertEquals(build(null, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), SortedSets.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11))
        assertEquals(build(null, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), SortedSets.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))
    }

    @Test fun ofWithComparator() {
        assertEquals(build(c), SortedSets.of(c))
        assertEquals(build(c, 1), SortedSets.of(c, 1))
        assertEquals(build(c, 1, 2), SortedSets.of(c, 1, 2))
        assertEquals(build(c, 1, 2, 3), SortedSets.of(c, 1, 2, 3))
        assertEquals(build(c, 1, 2, 3, 4), SortedSets.of(c, 1, 2, 3, 4))
        assertEquals(build(c, 1, 2, 3, 4, 5), SortedSets.of(c, 1, 2, 3, 4, 5))
        assertEquals(build(c, 1, 2, 3, 4, 5, 6), SortedSets.of(c, 1, 2, 3, 4, 5, 6))
        assertEquals(build(c, 1, 2, 3, 4, 5, 6, 7), SortedSets.of(c, 1, 2, 3, 4, 5, 6, 7))
        assertEquals(build(c, 1, 2, 3, 4, 5, 6, 7, 8), SortedSets.of(c, 1, 2, 3, 4, 5, 6, 7, 8))
        assertEquals(build(c, 1, 2, 3, 4, 5, 6, 7, 8, 9), SortedSets.of(c, 1, 2, 3, 4, 5, 6, 7, 8, 9))
        assertEquals(build(c, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10), SortedSets.of(c, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        assertEquals(build(c, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), SortedSets.of(c, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11))
        assertEquals(build(c, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), SortedSets.of(c, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))
    }

    @Test fun copyOfIterable() {
        assertEquals(build(null, 1, 2, 3), SortedSets.copyOf(java.util.Arrays.asList(1, 2, 3)))
    }

    @Test fun copyOfIterator() {
        assertEquals(build(null, 1, 2, 3), SortedSets.copyOf(Vector.empty<Int>().append(1).append(2).append(3).iterator()))
    }

    @Test fun copyOfArray() {
        assertEquals(build(null, 1, 2, 3), SortedSets.copyOf(java.util.Arrays.asList(1, 2, 3).toTypedArray()))
    }

    @Test fun copyOfTraversable() {
        assertEquals(build(null, 1, 2, 3), SortedSets.copyOfTraversable(Vector.empty<Int>().append(1).append(2).append(3)))
    }

    @Test fun factory() {
        assertEquals(build(null, 1, 2, 3), SortedSets.factory<Int>().newBuilder().addAll(1, 2, 3).build())
    }

    @Test fun copyOfIterableWithComparator() {
        assertEquals(build(c, 1, 2, 3), SortedSets.copyOf(c, java.util.Arrays.asList(1, 2, 3)))
    }

    @Test fun copyOfIteratorWithComparator() {
        assertEquals(build(c, 1, 2, 3), SortedSets.copyOf(c, Vector.empty<Int>().append(1).append(2).append(3).iterator()))
    }

    @Test fun copyOfArrayWithComparator() {
        assertEquals(build(c, 1, 2, 3), SortedSets.copyOf(c, java.util.Arrays.asList(1, 2, 3).toTypedArray()))
    }

    @Test fun copyOfTraversableWithComparator() {
        assertEquals(build(c, 1, 2, 3), SortedSets.copyOfTraversable(c, Vector.empty<Int>().append(1).append(2).append(3)))
    }

    @Test fun factoryWithComparator() {
        assertEquals(build(c, 1, 2, 3), SortedSets.factory<Int>(c).newBuilder().addAll(1, 2, 3).build())
    }

    @Test fun builder() {
        assertEquals(build(null, 1, 2, 3), SortedSets.builder<Int>().addAll(1, 2, 3).build())
    }

    @Test fun builderWithComparator() {
        assertEquals(build(c, 1, 2, 3), SortedSets.builder(c).addAll(1, 2, 3).build())
    }
}