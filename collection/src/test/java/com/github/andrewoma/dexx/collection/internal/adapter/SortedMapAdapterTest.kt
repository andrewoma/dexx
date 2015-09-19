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

package com.github.andrewoma.dexx.collection.internal.adapter

import com.github.andrewoma.dexx.collection.TreeMap
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import com.github.andrewoma.dexx.collection.Pair as DPair


public open class SortedMapAdapterTest : MapAdapterTest() {
    override fun map(vararg values: Pair<Int, Int>): SortedMap<Int, Int> {
        val builder = TreeMap.factory<Int, Int>(null, null).newBuilder()
        for (pair in values) {
            builder.add(DPair(pair.first, pair.second))
        }
        return builder.build().asSortedMap()
    }

    fun jmap(vararg values: Pair<Int, Int>): SortedMap<Int, Int> {
        val map = java.util.TreeMap<Int, Int>()
        for (pair in values) {
            map.put(pair.first, pair.second)
        }
        return map
    }

    @Test fun firstKey() {
        assertEquals(1, map(3 to 3, 1 to 1, 4 to 4, 3 to 3).firstKey())
    }

    @Test fun lastKey() {
        assertEquals(4, map(3 to 3, 1 to 1, 4 to 4, 3 to 3).lastKey())
    }

    @Test(expected = NoSuchElementException::class) fun firstKeyEmpty() {
        map().firstKey()
    }

    @Test(expected = NoSuchElementException::class) fun lastKeyEmpty() {
        map().lastKey()
    }

    @Test fun headMap() {
        assertEquals(map(1 to 1, 2 to 2, 3 to 3), map(1 to 1, 2 to 2, 3 to 3).headMap(4))
        assertEquals(map(1 to 1, 2 to 2), map(1 to 1, 2 to 2, 3 to 3).headMap(3))
        assertEquals(map(1 to 1), map(1 to 1, 2 to 2, 3 to 3).headMap(2))
        assertEquals(map(), map(1 to 1, 2 to 2, 3 to 3).headMap(1))
        assertEquals(map(), map().headMap(1))

        // Check the java.util behaviour is the same
        assertEquals(map(1 to 1, 2 to 2, 3 to 3), jmap(1 to 1, 2 to 2, 3 to 3).headMap(4))
        assertEquals(map(1 to 1, 2 to 2), jmap(1 to 1, 2 to 2, 3 to 3).headMap(3))
        assertEquals(map(1 to 1), jmap(1 to 1, 2 to 2, 3 to 3).headMap(2))
        assertEquals(map(), jmap(1 to 1, 2 to 2, 3 to 3).headMap(1))
        assertEquals(map(), jmap().headMap(1))
    }

    @Test fun tailMap() {
        assertEquals(map(1 to 1, 2 to 2, 3 to 3), map(1 to 1, 2 to 2, 3 to 3).tailMap(0))
        assertEquals(map(1 to 1, 2 to 2, 3 to 3), map(1 to 1, 2 to 2, 3 to 3).tailMap(1))
        assertEquals(map(2 to 2, 3 to 3), map(1 to 1, 2 to 2, 3 to 3).tailMap(2))
        assertEquals(map(3 to 3), map(1 to 1, 2 to 2, 3 to 3).tailMap(3))
        assertEquals(map(), map(1 to 1, 2 to 2, 3 to 3).tailMap(4))
        assertEquals(map(), map().tailMap(1))

        // Check the java.util behaviour is the same
        assertEquals(map(1 to 1, 2 to 2, 3 to 3), jmap(1 to 1, 2 to 2, 3 to 3).tailMap(0))
        assertEquals(map(1 to 1, 2 to 2, 3 to 3), jmap(1 to 1, 2 to 2, 3 to 3).tailMap(1))
        assertEquals(map(2 to 2, 3 to 3), jmap(1 to 1, 2 to 2, 3 to 3).tailMap(2))
        assertEquals(map(3 to 3), jmap(1 to 1, 2 to 2, 3 to 3).tailMap(3))
        assertEquals(map(), jmap(1 to 1, 2 to 2, 3 to 3).tailMap(4))
        assertEquals(map(), jmap().tailMap(1))
    }

    @Test fun subMap() {
        assertEquals(map(1 to 1, 2 to 2, 3 to 3), map(1 to 1, 2 to 2, 3 to 3).subMap(0, 4))
        assertEquals(map(1 to 1, 2 to 2), map(1 to 1, 2 to 2, 3 to 3).subMap(1, 3))
        assertEquals(map(), map(1 to 1, 2 to 2, 3 to 3).subMap(2, 2))
        assertEquals(map(), map(1 to 1, 2 to 2, 3 to 3).subMap(2, 1))
        assertEquals(map(), map().subMap(1, 1))

        // Check the java.util behaviour is the same
        assertEquals(map(1 to 1, 2 to 2, 3 to 3), jmap(1 to 1, 2 to 2, 3 to 3).subMap(0, 4))
        assertEquals(map(1 to 1, 2 to 2), jmap(1 to 1, 2 to 2, 3 to 3).subMap(1, 3))
        assertEquals(map(), jmap(1 to 1, 2 to 2, 3 to 3).subMap(2, 2))
        // Note: j.u.TreeMap throws IllegalArgumentException with fromKey > toKey
        //   assertEquals(map(), jmap(1 to 1, 2 to 2, 3 to 3).subMap(2, 1))
        assertEquals(map(), jmap().subMap(1, 1))
    }

    @Test fun comparator() {
        // TODO ... Bug in JetBrains' annotations - it thinks SortedMap.comparator() can't return a null
        // assertNull(map().comparator())
    }

    @Test fun <K, V>  sortedWithCustomComparator() {
        val c = object : Comparator<Int> {
            @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
            override fun compare(o1: Int, o2: Int): Int {
                return o1.compareTo(o2) * -1
            }
        }
        val map = TreeMap<Int, Int>(c, null).put(2, 20).put(1, 10).put(3, 30).put(7, 70).put(4, 40).asSortedMap()
        val actual = map.map { it.component1() }

        assertEquals(listOf(7, 4, 3, 2, 1), actual)
        assertEquals(c, map.comparator())
    }
}