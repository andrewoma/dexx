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
import kotlin.test.assertEquals
import kotlin.test.assertNull

abstract class AbstractSortedSetTest() : AbstractSetTest() {

    private fun <T> build(vararg ts: T) = build_(*ts) as SortedSet<T>

    @Test fun range() {
        val set = build(1, 2, 3)
        assertEquals(build(1, 2, 3), set.range(0, false, 4, false))
        assertEquals(build(1, 2, 3), set.range(1, true, 3, true))
        assertEquals(build(2, 3), set.range(1, false, 3, true))
        assertEquals(build(1, 2), set.range(1, true, 3, false))
        assertEquals(build(2), set.range(1, false, 3, false))
        assertEquals(build(2), set.range(2, true, 2, true))
        assertEquals(build(1), set.range(1, true, 1, true))
        assertEquals(build<Int>(), set.range(4, true, 1, true))
    }

    @Test fun from() {
        val set = build(1, 2, 3)
        assertEquals(build(1, 2, 3), set.from(0, false))
        assertEquals(build(1, 2, 3), set.from(1, true))
        assertEquals(build(2, 3), set.from(1, false))
        assertEquals(build(2, 3), set.from(2, true))
        assertEquals(build(3), set.from(3, true))
        assertEquals(build<Int>(), set.from(3, false))
    }

    @Test fun until() {
        val set = build(1, 2, 3)
        assertEquals(build<Int>(), set.to(0, false))
        assertEquals(build(1), set.to(1, true))
        assertEquals(build(1), set.to(2, false))
        assertEquals(build(1, 2), set.to(2, true))
        assertEquals(build(1, 2), set.to(3, false))
        assertEquals(build(1, 2, 3), set.to(3, true))
        assertEquals(build(1, 2, 3), set.to(4, false))
    }

    @Test fun firstSortedSet() {
        val set = build(1, 3, 2, 5, 4)
        assertEquals(1, set.first())
    }

    @Test fun lastSortedSet() {
        val set = build(1, 3, 2, 5, 4)
        assertEquals(5, set.last())
    }

    @Test fun firstEmpty() {
        assertNull(build<Int>().first())
    }

    @Test fun lastEmpty() {
        assertNull(build<Int>().last())
    }

    @Test fun asSortedSet() {
        assertEquals(setOf(1, 2, 3), build(1, 2, 3).asSortedSet())
    }

    @Test fun take() {
        assertSequence(sequence(10).take(5), 0, 5)
    }

    @Test fun takeNone() {
        val set = sequence(10)
        assertSequence(set.take(0), 0, 0)
        assertSequence(set.take(-1), 0, 0)
    }

    @Test fun takeLoop() {
        val max = 100
        val set = sequence(max)
        for (i in 1..max) {
            assertSequence(set.take(i), 0, i)
        }
    }

    @Test fun dropLoop() {
        val max = 100
        val set = sequence(max)
        for (i in 1..max) {
            assertSequence(set.drop(i), i, max - i)
        }
    }

    @Test fun takeAll() {
        assertSequence(sequence(10).take(100), 0, 10)
    }

    @Test fun drop() {
        assertSequence(sequence(10).drop(5), 5, 5)
    }

    @Test fun dropNone() {
        val set = sequence(10)
        assertSequence(set.drop(0), 0, 10)
        assertSequence(set.drop(-1), 0, 10)
    }

    @Test fun dropAll() {
        assertSequence(sequence(10).drop(100), 0, 0)
    }

    fun sequence(size: Int): SortedSet<Int> {
        var set = TreeSet.empty<Int>()
        for (i in 0..size - 1) {
            set = set.add(i)
        }
        return set
    }

    fun assertSequence(set: SortedSet<Int>, from: Int, length: Int) {
        var from_ = from
        assertEquals(length, set.size())
        for (integer in set) {
            assertEquals(from_++, integer)
        }
    }
}
