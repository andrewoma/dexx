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

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.fail
import kotlin.test.assertTrue
import kotlin.test.assertNull
import com.github.andrewoma.dexx.collection.internal.base.AbstractIterable
import com.github.andrewoma.dexx.collection.AbstractMapTest.WrappedBuilderFactory
import org.junit.Test as test

abstract class AbstractSortedMapTest() : AbstractMapTest() {
    override abstract fun <K, V> mapFactory(): BuilderFactory<Pair<K,V>, out SortedMap<K,V>>

    private fun <K, V> buildMap(vararg entries : kotlin.Pair<K, V>) = buildMap_(*entries) as SortedMap<K, V>

    test fun sortedForEach() {
        val map = buildMap(1 to "A", 3 to "C", 2 to "B", 5 to "E", 4 to "D")
        val actual = arrayListOf<Int>()
        map.forEach { actual.add(it.component1()!!) }
        assertEquals(listOf(1, 2, 3, 4, 5), actual)
    }

    test fun range() {
        val map = buildMap(1 to "A", 2 to "B", 3 to "C")
        assertEquals(buildMap(1 to "A", 2 to "B", 3 to "C"), map.range(0, false, 4, false))
        assertEquals(buildMap(1 to "A", 2 to "B", 3 to "C"), map.range(1, true, 3, true))
        assertEquals(buildMap(2 to "B", 3 to "C"), map.range(1, false, 3, true))
        assertEquals(buildMap(1 to "A", 2 to "B"), map.range(1, true, 3, false))
        assertEquals(buildMap(2 to "B"), map.range(1, false, 3, false))
        assertEquals(buildMap(2 to "B"), map.range(2, true, 2, true))
        assertEquals(buildMap(1 to "A"), map.range(1, true, 1, true))
        assertEquals(buildMap<Int, String>(), map.range(4, true, 1, true))
    }

    test fun from() {
        val map = buildMap(1 to "A",  2 to "B", 3 to "C")
        assertEquals(buildMap(1 to "A",  2 to "B", 3 to "C"), map.from(0, false))
        assertEquals(buildMap(1 to "A",  2 to "B", 3 to "C"), map.from(1, true))
        assertEquals(buildMap(2 to "B", 3 to "C"), map.from(1, false))
        assertEquals(buildMap(2 to "B", 3 to "C"), map.from(2, true))
        assertEquals(buildMap(3 to "C"), map.from(3, true))
        assertEquals(buildMap<Int, String>(), map.from(3, false))
    }

    test fun until() {
        val map = buildMap(1 to "A",  2 to "B", 3 to "C")
        assertEquals(buildMap<Int, String>(), map.to(0, false))
        assertEquals(buildMap(1 to "A"), map.to(1, true))
        assertEquals(buildMap(1 to "A"), map.to(2, false))
        assertEquals(buildMap(1 to "A", 2 to "B"), map.to(2, true))
        assertEquals(buildMap(1 to "A", 2 to "B"), map.to(3, false))
        assertEquals(buildMap(1 to "A", 2 to "B", 3 to "C"), map.to(3, true))
        assertEquals(buildMap(1 to "A", 2 to "B", 3 to "C"), map.to(4, false))
    }

    test fun firstSortedMap() {
        val map = buildMap(1 to "A", 3 to "C", 2 to "B", 5 to "E", 4 to "D")
        assertEquals(Pair(1, "A"), map.first())
    }

    test fun lastSortedMap() {
        val map = buildMap(1 to "A", 3 to "C", 2 to "B", 5 to "E", 4 to "D")
        assertEquals(Pair(5, "E"), map.last())
    }

    test fun remainsSorted() {
//        arrayListOf("").filter {  }
    }

    test fun sortedWithCustomComparator() {

    }

}
