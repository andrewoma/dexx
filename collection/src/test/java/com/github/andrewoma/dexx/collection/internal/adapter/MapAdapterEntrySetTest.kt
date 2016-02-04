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

import com.github.andrewoma.dexx.collection.HashMap
import com.github.andrewoma.dexx.collection.Maps
import com.google.common.collect.ImmutableMap
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.github.andrewoma.dexx.collection.Pair as DPair

/**
 *
 */
open class MapAdapterEntrySetTest {
    open fun map(vararg values: Pair<Int, Int>): MutableSet<MutableMap.MutableEntry<Int, Int>> {
        val builder = HashMap.factory<Int, Int>().newBuilder()
        for (pair in values) {
            builder.add(DPair(pair.first, pair.second))
        }
        return builder.build().asMap().entries
    }

    @Test fun equals() {
        assertEquals(map(1 to 1, 2 to 2), mapOf(1 to 1, 2 to 2).entries)
        assertEquals(mapOf(1 to 1, 2 to 2).entries, map(1 to 1, 2 to 2))
    }

    @Test fun testHashCode() {
        assertEquals(map(1 to 1, 2 to 2), map(1 to 1, 2 to 2))
    }

    @Test fun size() {
        assertEquals(0, map().size)
        assertEquals(2, map(1 to 1, 2 to 2).size)
    }

    @Test fun empty() {
        assertTrue(map().isEmpty())
        assertFalse(map(1 to 1, 2 to 2).isEmpty())
    }

    @Test(expected = UnsupportedOperationException::class) fun setValue() {
        map(1 to 1).iterator().next().setValue(2)
    }

    @Test fun entriesEqual() {
        val e1 = map(1 to 1)
        val e2 = map(1 to 1)
        val e3 = map(1 to 2)
        assertEquals(e1.iterator().next(), e2.iterator().next())
        assertEquals(e2.iterator().next(), e1.iterator().next())
        assertFalse(e1.iterator().next() == e3.iterator().next())
        assertFalse(e1.iterator().next().equals(""))
    }

    @Test fun entriesWithNullsEqual() {
        val e1 = Maps.of(1, null).asMap().entries
        val e2 = Maps.of(1, null).asMap().entries
        assertEquals(e1.iterator().next(), e2.iterator().next())
        assertEquals(e2.iterator().next(), e1.iterator().next())
        assertFalse(e1.iterator().next().equals(""))
    }

    @Test fun entryToString() {
        assertEquals("1=2", map(1 to 2).iterator().next().toString())
    }

    @Test fun entriesHashcodesEqual() {
        val e1 = map(1 to 1)
        val e2 = map(1 to 1)
        val e3 = map(1 to 2)
        assertEquals(e1.iterator().next().hashCode(), e2.iterator().next().hashCode())
        assertFalse(e1.iterator().next().hashCode() == e3.iterator().next().hashCode())
    }

    @Test fun contains() {
        val set = map(1 to 1, 2 to 2, 3 to 3)
        assertTrue(set.contains(e(1 to 1)))
        assertFalse(set.contains(e(4  to 4)))
    }

    @Test fun toArray() {
        val ints = map(1 to 1, 2 to 2).toTypedArray()
        assertEquals(ints.map { it.key }.sortedBy { it }, listOf(1, 2))
    }

    @Test fun iterator() {
        val ints = mapOf(1 to 1, 2 to 2).iterator()
        assertEquals(ints.asSequence().toList().map { it.key }.sortedBy { it }, listOf(1, 2))
    }

    @Test fun containsAll() {
        assertTrue(map(1 to 1, 2 to 2, 3 to 3).containsAll(listOf(e(1 to 1), e(2 to 2), e(3 to 3))))
        assertTrue(map(1 to 1, 2 to 2, 3 to 3).containsAll(listOf(e(1 to 1), e(2 to 2))))
        assertTrue(map(1 to 1, 2 to 2, 3 to 3).containsAll(listOf(e(1 to 1))))
        assertTrue(map(1 to 1, 2 to 2, 3 to 3).containsAll(listOf()))
        assertFalse(map(1 to 1, 2 to 2, 3 to 3).containsAll(listOf(e(4 to 4))))
        assertFalse(map(1 to 1, 2 to 2, 3 to 3).containsAll(listOf(e(1 to 1), e(2 to 2), e(3 to 3), e(4 to 4))))
    }

    fun e(pair: Pair<Int, Int>): MutableMap.MutableEntry<Int, Int> {
        return ImmutableMap.of(pair.first, pair.second)!!.entries.iterator().next()
    }

    @Test(expected = UnsupportedOperationException::class) fun add() {
        map().add(e(1 to 1))
    }

    @Test(expected = UnsupportedOperationException::class) fun remove() {
        map().remove(e(1 to 1))
    }

    @Test(expected = UnsupportedOperationException::class) fun addAll() {
        map().addAll(listOf(e(1 to 1)))
    }

    @Test(expected = UnsupportedOperationException::class) fun retainAll() {
        map().retainAll(listOf(e(1 to 1)))
    }

    @Test(expected = UnsupportedOperationException::class) fun removeAll() {
        map().removeAll(listOf(e(1 to 1)))
    }

    @Test(expected = UnsupportedOperationException::class) fun clear() {
        map().clear()
    }
}