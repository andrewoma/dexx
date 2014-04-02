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

import org.junit.Test as test
import kotlin.test.assertEquals
import java.util.Comparator

class SortedMapsTest {
    private fun build<K, V>(vararg ts: kotlin.Pair<K, V>) : Map<K, V> {
        val builder = TreeMap.factory<K, V>(null, null).newBuilder()
        for (t in ts) { builder.add(Pair(t.first, t.second)) }
        return builder.build()
    }

    val c = object : Comparator<Int> {
        [suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")]
        override fun compare(o1: Int, o2: Int): Int {
            return o1.compareTo(o2) * -1
        }
    }

    private fun build<K, V>(comparator: Comparator<in K>?, vararg ts: kotlin.Pair<K, V>) : SortedMap<K, V> {
        val builder = TreeMap.factory<K, V>(comparator, null).newBuilder()
        for (t in ts) { builder.add(Pair(t.first, t.second)) }
        return builder.build()
    }

    test fun of() {
        assertEquals(build<Int, Int>(), SortedMaps.of<Int, Int>())
        assertEquals(build(1 to "a"), SortedMaps.of(1, "a"))
        assertEquals(build(1 to "a", 2 to "b"), SortedMaps.of(1, "a", 2, "b"))
        assertEquals(build(1 to "a", 2 to "b", 3 to "c"), SortedMaps.of(1, "a", 2, "b", 3, "c"))
        assertEquals(build(1 to "a", 2 to "b", 3 to "c", 4 to "d"), SortedMaps.of(1, "a", 2, "b", 3, "c", 4, "d"))
        assertEquals(build(1 to "a", 2 to "b", 3 to "c", 4 to "d", 5 to "e"), SortedMaps.of(1, "a", 2, "b", 3, "c", 4, "d", 5, "e"))
    }

    test fun ofWithComparator() {
        assertEquals(build<Int, Int>(c), SortedMaps.of<Int, Int>(c))
        assertEquals(build(c, 1 to "a"), SortedMaps.of(c, 1, "a"))
        assertEquals(build(c, 1 to "a", 2 to "b"), SortedMaps.of(c, 1, "a", 2, "b"))
        assertEquals(build(c, 1 to "a", 2 to "b", 3 to "c"), SortedMaps.of(c, 1, "a", 2, "b", 3, "c"))
        assertEquals(build(c, 1 to "a", 2 to "b", 3 to "c", 4 to "d"), SortedMaps.of(c, 1, "a", 2, "b", 3, "c", 4, "d"))
        assertEquals(build(c, 1 to "a", 2 to "b", 3 to "c", 4 to "d", 5 to "e"), SortedMaps.of(c, 1, "a", 2, "b", 3, "c", 4, "d", 5, "e"))
        assertEquals(5, SortedMaps.of(c, 1, "a", 2, "b", 3, "c", 4, "d", 5, "e").first()?.component1())
    }

    test fun copyOfCollection() {
        assertEquals(build(1 to "a", 2 to "b"), SortedMaps.copyOf(arrayListOf(Pair(1, "a"), Pair(2, "b"))))
    }

    test fun copyOfIterator() {
        assertEquals(build(1 to "a", 2 to "b"), SortedMaps.copyOf(arrayListOf(Pair(1, "a"), Pair(2, "b")).iterator()))
    }

    test fun copyOfArray() {
        assertEquals(build(1 to "a", 2 to "b"), SortedMaps.copyOf(arrayListOf(Pair(1, "a"), Pair(2, "b")).copyToArray()))
    }

    test fun copyOfTraversable() {
        assertEquals(build(1 to "a", 2 to "b"), SortedMaps.copyOfTraversable(Vector.empty<Pair<Int, String>>().append(Pair(1, "a")).append(Pair(2, "b"))))
    }

    test fun factory() {
        assertEquals(build(1 to "a", 2 to "b"), SortedMaps.factory<Int, String>().newBuilder().addAll(Pair(1, "a"), Pair(2, "b")).build())
    }

    test fun factoryWithComparator() {
        assertEquals(build(c, 1 to "a", 2 to "b"), SortedMaps.factory<Int, String>(c).newBuilder().addAll(Pair(1, "a"), Pair(2, "b")).build())
    }

    test fun builder() {
        assertEquals(build(1 to "a", 2 to "b"), SortedMaps.builder<Int, String>().addAll(Pair(1, "a"), Pair(2, "b")).build())
    }

    test fun builderWithComparator() {
        assertEquals(build(c, 1 to "a", 2 to "b"), SortedMaps.builder<Int, String>(c).addAll(Pair(1, "a"), Pair(2, "b")).build())
    }
}