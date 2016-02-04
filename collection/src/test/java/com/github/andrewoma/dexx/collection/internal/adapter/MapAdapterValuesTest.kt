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
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import com.github.andrewoma.dexx.collection.Pair as DPair

/**
 * TODO ... should values implement equals() and hashCode()? Java's doesn't ...
 */
open class MapAdapterValuesTest {
    open fun values(vararg values: Int): MutableCollection<Int> {
        val builder = HashMap.factory<Int, Int>().newBuilder()
        for (value in values) {
            builder.add(DPair(value, value))
        }
        return builder.build().asMap().values
    }

    @Test fun size() {
        assertEquals(0, values().size)
        assertEquals(2, values(1, 2).size)
    }

    @Test fun empty() {
        assertTrue(values().isEmpty())
        assertFalse(values(1, 2).isEmpty())
    }

    @Test fun contains() {
        val set = values(1, 2, 3)
        assertTrue(set.contains(1))
        assertFalse(set.contains(4))
    }

    @Test fun iterator() {
        assertEquals(values(1, 2, 3).iterator().asSequence().toSet(), setOf(1, 2, 3))
    }

    @Test fun toArray() {
        val ints = values(1, 2).toTypedArray()
        assertEquals(arrayListOf(*ints), listOf(1, 2))
    }

    @Test fun containsAll() {
        assertTrue(values(1, 2, 3).containsAll(listOf(1, 2, 3)))
        assertTrue(values(1, 2, 3).containsAll(listOf(1, 2)))
        assertTrue(values(1, 2, 3).containsAll(listOf(1)))
        assertTrue(values(1, 2, 3).containsAll(listOf()))
        assertFalse(values(1, 2, 3).containsAll(listOf(4)))
        assertFalse(values(1, 2, 3).containsAll(listOf(1, 2, 3, 4)))
    }

    @Test(expected = UnsupportedOperationException::class) fun add() {
        values().add(1)
    }

    @Test(expected = UnsupportedOperationException::class) fun remove() {
        values().remove(1)
    }

    @Test(expected = UnsupportedOperationException::class) fun addAll() {
        values().addAll(listOf(1))
    }

    @Test(expected = UnsupportedOperationException::class) fun retainAll() {
        values().retainAll(listOf(1))
    }

    @Test(expected = UnsupportedOperationException::class) fun removeAll() {
        values().removeAll(listOf(1))
    }

    @Test(expected = UnsupportedOperationException::class) fun clear() {
        values().clear()
    }
}