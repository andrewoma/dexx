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
import com.github.andrewoma.dexx.collection.IndexedLists


public open class ListAdapterTest {
    open fun list(vararg values: Int) = IndexedLists.copyOf(values.toList()).asList()

    test fun equals() {
        assertEquals(list(1, 2, 3), listOf(1, 2, 3))
        assertEquals(listOf(1, 2, 3), list(1, 2, 3))
    }

    test fun testHashCode() {
        assertEquals(list(1, 2, 3), list(1, 2, 3))
    }

    test fun size() {
        assertEquals(0, list().size())
        assertEquals(3, list(1, 2, 3).size())
    }

    test fun empty() {
        assertTrue(list().isEmpty())
        assertFalse(list(1, 2, 3).isEmpty())
    }

    test fun contains() {
        val list = list(1, 2, 3)
        assertTrue(list.contains(1))
        assertFalse(list.contains(4))
        assertFalse(list.contains("foo"))
    }

    test fun get() {
        val list = list(1, 2)
        assertEquals(1, list[0])
        assertEquals(2, list[1])
    }

    test fun toArray() {
        val ints = list(1, 2, 3).toTypedArray()
        assertEquals(ints.sortBy { it }, listOf(1, 2, 3))
    }

    test fun iterator() {
        val ints = list(1, 2, 3).iterator()
        assertEquals(ints.asSequence().toList().sortBy { it }, listOf(1, 2, 3))
    }

    test fun containsAll() {
        assertTrue(list(1, 2, 3).containsAll(listOf(1, 2, 3)))
        assertTrue(list(1, 2, 3).containsAll(listOf(1, 2)))
        assertTrue(list(1, 2, 3).containsAll(listOf(1)))
        assertTrue(list(1, 2, 3).containsAll(listOf()))
        assertFalse(list(1, 2, 3).containsAll(listOf(4)))
        assertFalse(list(1, 2, 3).containsAll(listOf(1, 2, 3, 4)))
    }

    test(expected = UnsupportedOperationException::class) fun add() {
        list().add(1)
    }

    test(expected = UnsupportedOperationException::class) fun remove() {
        list().remove(1)
    }

    test(expected = UnsupportedOperationException::class) fun removeObject() {
        list().remove("")
    }

    test(expected = UnsupportedOperationException::class) fun addAll() {
        list().addAll(listOf(1))
    }

    test(expected = UnsupportedOperationException::class) fun retainAll() {
        list().retainAll(listOf(1))
    }

    test(expected = UnsupportedOperationException::class) fun removeAll() {
        list().removeAll(listOf(1))
    }

    test(expected = UnsupportedOperationException::class) fun clear() {
        list().clear()
    }
}