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


public open class SetAdapterTest {
    open fun set(vararg values: Int) = Sets.copyOf(values.toList()).asSet()

    test fun equals() {
        assertEquals(set(1, 2, 3), setOf(1, 2, 3))
        assertEquals(setOf(1, 2, 3), set(1, 2, 3))
    }

    test fun testHashCode() {
        assertEquals(set(1, 2, 3), set(1, 2, 3))
    }

    test fun size() {
        assertEquals(0, set().size())
        assertEquals(3, set(1, 2, 3).size())
    }

    test fun empty() {
        assertTrue(set().isEmpty())
        assertFalse(set(1, 2, 3).isEmpty())
    }

    test fun contains() {
        val set = set(1, 2, 3)
        assertTrue(set.contains(1))
        assertFalse(set.contains(4))
        assertFalse(set.contains("foo"))
    }

    test fun toArray() {
        val ints = set(1, 2, 3).toTypedArray()
        assertEquals(ints.sortBy { it }, listOf(1, 2, 3))
    }

    test fun iterator() {
        val ints = set(1, 2, 3).iterator()
        assertEquals(ints.asSequence().toList().sortBy { it }, listOf(1, 2, 3))
    }

    test fun containsAll() {
        assertTrue(set(1, 2, 3).containsAll(listOf(1, 2, 3)))
        assertTrue(set(1, 2, 3).containsAll(listOf(1, 2)))
        assertTrue(set(1, 2, 3).containsAll(listOf(1)))
        assertTrue(set(1, 2, 3).containsAll(listOf()))
        assertFalse(set(1, 2, 3).containsAll(listOf(4)))
        assertFalse(set(1, 2, 3).containsAll(listOf(1, 2, 3, 4)))
    }

    test(expected = UnsupportedOperationException::class) fun add() {
        set().add(1)
    }

    test(expected = UnsupportedOperationException::class) fun remove() {
        set().remove(1)
    }

    test(expected = UnsupportedOperationException::class) fun addAll() {
        set().addAll(listOf(1))
    }

    test(expected = UnsupportedOperationException::class) fun retainAll() {
        set().retainAll(listOf(1))
    }

    test(expected = UnsupportedOperationException::class) fun removeAll() {
        set().removeAll(listOf(1))
    }

    test(expected = UnsupportedOperationException::class) fun clear() {
        set().clear()
    }
}