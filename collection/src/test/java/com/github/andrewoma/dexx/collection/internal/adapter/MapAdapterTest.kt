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

import org.junit.Test as test
import com.github.andrewoma.dexx.collection.TreeSet
import com.github.andrewoma.dexx.collection.Sets
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import com.github.andrewoma.dexx.collection.HashMap
import com.github.andrewoma.dexx.collection.Pair as DPair
import kotlin.test.assertNull


public open class MapAdapterTest {
    open fun map(vararg values: Pair<Int, Int>) : MutableMap<Int, Int> {
        val builder = HashMap.factory<Int, Int>().newBuilder()
        for (pair in values) {
            builder.add(DPair(pair.first, pair.second))
        }
        return builder.result().asMap()
    }

    test fun equals() {
        assertEquals(map(1 to 1, 2 to 2), mapOf(1 to 1, 2 to 2))
    }

    test fun testHashCode() {
        assertEquals(map(1 to 1, 2 to 2).hashCode(), map(1 to 1, 2 to 2).hashCode())
        assertEquals(map(1 to 1, 2 to 2).hashCode(), mapOf(1 to 1, 2 to 2).hashCode()) // Same hashCode algorithm as java.util
    }

    test fun size() {
        assertEquals(0, map().size())
        assertEquals(2, map(1 to 1, 2 to 2).size())
    }

    test fun empty() {
        assertTrue(map().isEmpty())
        assertFalse(map(1 to 1).isEmpty())
    }

    test fun containsKey() {
        val map = map(1 to 10, 2 to 20, 3 to 30)
        assertTrue(map.containsKey(1))
        assertFalse(map.containsKey(4))
        assertFalse(map.containsKey(10))
        assertFalse(map.containsKey("foo"))
    }

    test fun get() {
        val map = map(1 to 10, 2 to 20, 3 to 30)
        assertEquals(10, map.get(1))
        assertNull(map.get(4))
        assertNull(map.get(10))
    }

    test fun containsValue() {
        val map = map(1 to 10, 2 to 20, 3 to 30)
        assertTrue(map.containsValue(10))
        assertFalse(map.containsValue(40))
        assertFalse(map.containsValue(1))
        assertFalse(map.containsValue("foo"))
    }

    test(expected = javaClass<UnsupportedOperationException>()) fun put() {
        map().put(1, 1)
    }

    test(expected = javaClass<UnsupportedOperationException>()) fun remove() {
        map().remove(1)
    }

    test(expected = javaClass<UnsupportedOperationException>()) fun putAll() {
        map().putAll(map(1 to 1))
    }

    test(expected = javaClass<UnsupportedOperationException>()) fun clear() {
        map().clear()
    }
}