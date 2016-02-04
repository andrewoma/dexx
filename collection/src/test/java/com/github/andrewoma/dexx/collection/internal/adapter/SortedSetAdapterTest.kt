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

import com.github.andrewoma.dexx.collection.SortedSets
import com.github.andrewoma.dexx.collection.TreeSet
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class SortedSetAdapterTest : SetAdapterTest() {

    override fun set(vararg values: Int) = SortedSets.copyOf(values.toList()).asSortedSet()

    fun jset(vararg values: Int) = values.toSortedSet()

    @Test fun first() {
        assertEquals(1, set(3, 1, 4, 3).first())
    }

    @Test fun last() {
        assertEquals(4, set(3, 1, 4, 3).last())
    }

    @Test(expected = NoSuchElementException::class) fun firstEmpty() {
        set().first()
    }

    @Test(expected = NoSuchElementException::class) fun lastEmpty() {
        set().last()
    }

    @Test fun headSet() {
        assertEquals(set(1, 2, 3), set(1, 2, 3).headSet(4))
        assertEquals(set(1, 2), set(1, 2, 3).headSet(3))
        assertEquals(set(1), set(1, 2, 3).headSet(2))
        assertEquals(set(), set(1, 2, 3).headSet(1))
        assertEquals(set(), set().headSet(1))

        // Check the java.util behaviour is the same
        assertEquals(set(1, 2, 3), jset(1, 2, 3).headSet(4))
        assertEquals(set(1, 2), jset(1, 2, 3).headSet(3))
        assertEquals(set(1), jset(1, 2, 3).headSet(2))
        assertEquals(set(), jset(1, 2, 3).headSet(1))
        assertEquals(set(), jset().headSet(1))
    }

    @Test fun tailSet() {
        assertEquals(set(1, 2, 3), set(1, 2, 3).tailSet(0))
        assertEquals(set(1, 2, 3), set(1, 2, 3).tailSet(1))
        assertEquals(set(2, 3), set(1, 2, 3).tailSet(2))
        assertEquals(set(3), set(1, 2, 3).tailSet(3))
        assertEquals(set(), set(1, 2, 3).tailSet(4))
        assertEquals(set(), set().tailSet(1))

        // Check the java.util behaviour is the same
        assertEquals(set(1, 2, 3), jset(1, 2, 3).tailSet(0))
        assertEquals(set(1, 2, 3), jset(1, 2, 3).tailSet(1))
        assertEquals(set(2, 3), jset(1, 2, 3).tailSet(2))
        assertEquals(set(3), jset(1, 2, 3).tailSet(3))
        assertEquals(set(), jset(1, 2, 3).tailSet(4))
        assertEquals(set(), jset().tailSet(1))
    }

    @Test fun subSet() {
        assertEquals(set(1, 2, 3), set(1, 2, 3).subSet(0, 4))
        assertEquals(set(1, 2), set(1, 2, 3).subSet(1, 3))
        assertEquals(set(), set(1, 2, 3).subSet(2, 2))
        assertEquals(set(), set(1, 2, 3).subSet(2, 1))
        assertEquals(set(), set().subSet(1, 1))

        // Check the java.util behaviour is the same
        assertEquals(set(1, 2, 3), jset(1, 2, 3).subSet(0, 4))
        assertEquals(set(1, 2), jset(1, 2, 3).subSet(1, 3))
        assertEquals(set(), jset(1, 2, 3).subSet(2, 2))
        // Note: j.u.TreeSet throws IllegalArgumentException with fromKey > toKey
        // assertEquals(set(), jset(1, 2, 3).subSet(2, 1))
        assertEquals(set(), jset().subSet(1, 1))
    }

    @Test fun comparator() {
        // TODO ... Bug in JetBrains' annotations - it thinks SortedMap.comparator() can't return a null
        // assertNull(set().comparator())
    }

    @Test fun sortedWithCustomComparator() {
        val c = Comparator<Int> { o1, o2 -> o1.compareTo(o2) * -1 }
        val set = TreeSet<Int>(c).add(2).add(1).add(3).add(7).add(4).asSortedSet()

        assertEquals(listOf(7, 4, 3, 2, 1), set.toList())
        assertEquals(c, set.comparator())
    }
}