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
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import org.junit.Test as test
import java.util.NoSuchElementException

abstract class AbstractSortedSetTest() : AbstractSetTest() {

    private fun build<T>(vararg ts: T) = build_(*ts) as SortedSet<T>

    test fun range() {
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

    test fun from() {
        val set = build(1,  2, 3)
        assertEquals(build(1,  2, 3), set.from(0, false))
        assertEquals(build(1,  2, 3), set.from(1, true))
        assertEquals(build(2, 3), set.from(1, false))
        assertEquals(build(2, 3), set.from(2, true))
        assertEquals(build(3), set.from(3, true))
        assertEquals(build<Int>(), set.from(3, false))
    }

    test fun until() {
        val set = build(1,  2, 3)
        assertEquals(build<Int>(), set.to(0, false))
        assertEquals(build(1), set.to(1, true))
        assertEquals(build(1), set.to(2, false))
        assertEquals(build(1, 2), set.to(2, true))
        assertEquals(build(1, 2), set.to(3, false))
        assertEquals(build(1, 2, 3), set.to(3, true))
        assertEquals(build(1, 2, 3), set.to(4, false))
    }

    test fun firstSortedSet() {
        val set = build(1, 3, 2, 5, 4)
        assertEquals(1, set.first())
    }

    test fun lastSortedSet() {
        val set = build(1, 3, 2, 5, 4)
        assertEquals(5, set.last())
    }

    test fun asSortedSet() {
        assertEquals(setOf(1, 2, 3), build(1, 2, 3).asSortedSet())
    }
}
