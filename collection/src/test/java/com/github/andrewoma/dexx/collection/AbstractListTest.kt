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
import kotlin.test.assertFalse
import kotlin.test.assertNull

abstract class AbstractListTest() : AbstractIterableTest() {
    override abstract fun <T> factory(): BuilderFactory<T, out List<T>>

    private fun <T> build(vararg ts: T) = build_(*ts) as List<T>

    fun <T> List<T>.klist(): java.util.ArrayList<T> {
        val klist = arrayListOf<T>()
        for (i in this) {
            klist.add(i)
        }
        return klist
    }

    @Test fun insertOrderPreserved() {
        assertEquals(listOf(1, 3, 2), build(1, 3, 2).klist())
    }

    @Test fun duplicatesPreserved() {
        assertEquals(listOf(1, 2, 2, 1), build(1, 2, 2, 1).klist())
    }

    @Test fun indexOf() {
        val list = build(1, 2, 2, 1)
        assertEquals(1, list.indexOf(2))
        assertEquals(-1, list.indexOf(3))
    }

    @Test fun tail() {
        assertEquals(build(2, 3), build(1, 2, 3).tail());
        assertEquals(build(2), build(1, 2).tail());
        assertEquals(build<Int>(), build(1).tail());
        assertEquals(build<Int>(), build<Int>().tail());
    }

    @Test fun prepend() {
        assertEquals(build(1), build<Int>().prepend(1));
        assertEquals(build(2, 1), build(1).prepend(2));
        assertEquals(build(3, 2, 1), build(2, 1).prepend(3));
    }

    @Test fun append() {
        assertEquals(build(1), build<Int>().append(1));
        assertEquals(build(1, 2), build(1).append(2));
        assertEquals(build(1, 2, 3), build(1, 2).append(3));
    }

    @Test open fun range() {
        assertEquals(build(1, 2, 3, 4), build(1, 2, 3, 4).range(0, true, 3, true));
        assertEquals(build(2, 3), build(1, 2, 3, 4).range(0, false, 3, false));
        assertEquals(build(2, 3), build(1, 2, 3, 4).range(1, true, 2, true));
        assertEquals(build(2), build(1, 2, 3, 4).range(1, true, 2, false));
        assertEquals(build(3), build(1, 2, 3, 4).range(1, false, 2, true));
        assertEquals(build<Int>(), build(1, 2, 3, 4).range(1, false, 2, false));
    }

    @Test fun lastIndexOf() {
        val list = build(1, 2, 2, 1)
        assertEquals(2, list.lastIndexOf(2))
        assertEquals(-1, list.lastIndexOf(3))
    }

    @Test fun indexOfWithNulls() {
        val list = build(1, null, null, 1)
        assertEquals(1, list.indexOf(null))
        assertEquals(-1, list.indexOf(3))
    }

    @Test fun lastIndexOfWithNulls() {
        val list = build(1, null, null, 1)
        assertEquals(2, list.lastIndexOf(null))
        assertEquals(-1, list.lastIndexOf(3))
    }

    @Test fun equalsUsingNulls() {
        assertEquals(build(1, null, 2, null), build(1, null, 2, null))
        assertEquals(build(1, 2), build(1, 2))
        assertEquals(build(null, 2), build(null, 2))
        assertEquals(build(1, null), build(1, null))
        assertFalse(build(1).equals(build(null)))
        assertFalse(build(null).equals(build(1)))
        assertFalse(build(1, 2, 3).equals(build(null, null, null)))
    }

    @Test fun hashCodeUsingNulls() {
        assertEquals(build(1, null, 2, null).hashCode(), build(1, null, 2, null).hashCode())
    }

    @Test fun notEqualsWithDifferentLengths() {
        assertFalse(build(1, 1, 1).equals(build(1, 1)))
        assertFalse(build(1, 1).equals(build(1, 1, 1)))
    }

    @Test(expected = IndexOutOfBoundsException::class) fun getOutOfBounds() {
        sequence(10)[11]
    }

    @Test(expected = IndexOutOfBoundsException::class) fun setOutOfBounds() {
        sequence(10).set(11, 100)
    }

    @Test fun get() {
        val list = sequence(10)
        for (i in 0..list.size() - 1) {
            assertEquals(i, list[i])
        }
        for (i in (0..list.size() - 1).reversed()) {
            assertEquals(i, list[i])
        }
    }

    @Test fun set() {
        var list = sequence(10)
        for (i in 0..list.size() - 1) {
            list = list.set(i, i + 10);
        }

        for (i in 0..list.size() - 1) {
            assertEquals(i + 10, list[i])
        }
    }

    @Test fun setSameValueTwice() {
        var list = build(1, 2, 3)
        list = list.set(0, 3)
        list = list.set(0, 3)
        assertEquals(listOf(3, 2, 3), list.klist())
    }

    @Test(expected = IndexOutOfBoundsException::class) fun emptyGet() {
        build<Int>()[0]
    }

    @Test(expected = IndexOutOfBoundsException::class) fun emptySet() {
        build<Int>().set(0, 100)
    }

    @Test fun emptyTake() {
        assertEquals(build<Int>(), build<Int>().take(1))
    }

    @Test fun emptyDrop() {
        assertEquals(build<Int>(), build<Int>().drop(1))
    }

    @Test fun emptyRange() {
        assertEquals(build<Int>(), build<Int>().range(0, true, 1, true))
    }

    @Test fun take() {
        assertSequence(sequence(10).take(5), 0, 5)
    }

    @Test fun takeNone() {
        val list = sequence(10)
        assertSequence(list.take(0), 0, 0)
        assertSequence(list.take(-1), 0, 0)
    }

    @Test fun takeAll() {
        assertSequence(sequence(10).take(100), 0, 10)
    }

    @Test fun drop() {
        assertSequence(sequence(10).drop(5), 5, 5)
    }

    @Test fun dropNone() {
        val list = sequence(10)
        assertSequence(list.drop(0), 0, 10)
        assertSequence(list.drop(-1), 0, 10)
    }

    @Test fun dropAll() {
        assertSequence(sequence(10).drop(100), 0, 0)
    }

    @Test fun first() {
        assertEquals(0, sequence(10).first())
        assertNull(sequence(0).first())
    }

    @Test fun last() {
        assertEquals(9, sequence(10).last())
        assertNull(sequence(0).last())
    }

    fun assertSequence(list: List<Int>, from: Int, length: Int) {
        var from_ = from
        assertEquals(length, list.size())
        for (integer in list) {
            assertEquals(from_++, integer)
        }
    }

    fun sequence(size: Int): List<Int> {
        val builder = factory<Int>().newBuilder()
        for (i in 0..size - 1) {
            builder.add(i)
        }
        return builder.build()
    }
}
