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

abstract class AbstractListTest() : AbstractIterableTest() {

    private fun build<T>(vararg ts: T) = build_(*ts) as List<T>

    fun <T> List<T>.klist() : java.util.ArrayList<T> {
        val klist = arrayListOf<T>()
        for (i in this) {
            klist.add(i)
        }
        return klist
    }

    test fun insertOrderPreserved() {
        assertEquals(listOf(1, 3, 2), build(1, 3, 2).klist())
    }

    test fun duplicatesPreserved() {
        assertEquals(listOf(1, 2, 2, 1), build(1, 2, 2, 1).klist())
    }

    test fun indexOf() {
        val list = build(1, 2, 2, 1)
        assertEquals(1, list.indexOf(2))
        assertEquals(-1, list.indexOf(3))
    }

    test fun tail() {
        assertEquals(build(2, 3), build(1, 2, 3).tail());
        assertEquals(build(2), build(1, 2).tail());
        assertEquals(build<Int>(), build(1).tail());
        assertEquals(build<Int>(), build<Int>().tail());
    }

    test fun prepend() {
        assertEquals(build(1), build<Int>().prepend(1));
        assertEquals(build(2, 1), build(1).prepend(2));
        assertEquals(build(3, 2, 1), build(2, 1).prepend(3));
    }

    test fun range() {
        assertEquals(build(1, 2, 3, 4), build(1, 2, 3, 4).range(0, true, 3, true));
        assertEquals(build(2, 3), build(1, 2, 3, 4).range(0, false, 3, false));
        assertEquals(build(2, 3), build(1, 2, 3, 4).range(1, true, 2, true ));
        assertEquals(build(2), build(1, 2, 3, 4).range(1, true, 2, false ));
        assertEquals(build(3), build(1, 2, 3, 4).range(1, false, 2, true));
        assertEquals(build<Int>(), build(1, 2, 3, 4).range(1, false, 2, false));
    }

    test fun lastIndexOf() {
        val list = build(1, 2, 2, 1)
        assertEquals(2, list.lastIndexOf(2))
        assertEquals(-1, list.lastIndexOf(3))
    }

    test fun indexOfWithNulls() {
        val list = build(1, null, null, 1)
        assertEquals(1, list.indexOf(null))
        assertEquals(-1, list.indexOf(3))
    }

    test fun lastIndexOfWithNulls() {
        val list = build(1, null, null, 1)
        assertEquals(2, list.lastIndexOf(null))
        assertEquals(-1, list.lastIndexOf(3))
    }

    test fun equalsUsingNulls() {
        assertEquals(build(1, null, 2, null), build(1, null, 2, null))
        assertEquals(build(1, 2), build(1, 2))
        assertEquals(build(null, 2), build(null, 2))
        assertEquals(build(1, null), build(1, null))
        assertFalse(build(1).equals(build(null)))
        assertFalse(build(null).equals(build(1)))
        assertFalse(build(1, 2, 3).equals(build(null, null, null)))
    }

    test fun hashCodeUsingNulls() {
        assertEquals(build(1, null, 2, null).hashCode(), build(1, null, 2, null).hashCode())
    }

    test fun notEqualsWithDifferentLengths() {
        assertFalse(build(1, 1, 1).equals(build(1, 1)))
        assertFalse(build(1, 1).equals(build(1, 1, 1)))
    }
}
