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

/**
 *
 */
class TreeMapTest : AbstractSortedMapTest() {

    override fun <K, V> mapFactory(comparator: Comparator<in K>?): BuilderFactory<Pair<K, V>, out SortedMap<K, V>> {
        return TreeMap.factory<K, V>(comparator, null)
    }

    @Test fun construct() {
        assertEquals(mapFactory<Int, Int>().newBuilder().add(Pair(1, 2)).build(), TreeMap<Int, Int>().put(1, 2))
    }

    @Test fun sliceFromStart() {
        val max = 100
        val map = sequence(max) as TreeMap
        for (i in 0..max - 1) {
            val from = i
            val to = map.size()
            assertEquals(map.drop(from).take(to - from), map.slice(from, to))
        }
    }

    @Test fun sliceFromEnd() {
        val max = 100
        val map = sequence(max) as TreeMap
        for (i in 0..max - 1) {
            val from = 0
            val to = map.size() - i
            assertEquals(map.drop(from).take(to - from), map.slice(from, to))
        }
    }

    @Test fun sliceFromStartAndEnd() {
        val max = 100
        val map = sequence(max) as TreeMap
        for (i in 0..max / 2) {
            val from = i
            val to = map.size() - i
            assertEquals(map.drop(from).take(to - from), map.slice(from, to))
        }
    }
}
