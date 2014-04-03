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

import org.junit.Test as test
import java.math.BigDecimal
import com.github.andrewoma.dexx.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNull

class VectorTest() : AbstractListTest() {

    override fun <T> factory(): BuilderFactory<T, out List<T>> {
        return Vector.factory()
    }

    fun size(level: Int) = BigDecimal(2).pow(level * 5).intValueExact()

    fun sizes(): Collection<Int> {
        val max = if (TestMode.current() == TestMode.QUICK) 3 else 5
        val sizes = (1..max).mapTo(arrayListOf<Int>()) { size(it) }
        sizes.add(size(max) + 2)
        return sizes
    }

    test(expected = javaClass<IndexOutOfBoundsException>()) fun getOutOfBounds() {
        sequence(10)[11]
    }

    test fun take() {
        assertSequence(sequence(10).take(5), 0, 5)
    }

    test fun takeNone() {
        val vector = sequence(10)
        assertSequence(vector.take(0), 0, 0)
        assertSequence(vector.take(-1), 0, 0)
    }

    test fun takeAll() {
        assertSequence(sequence(10).take(100), 0, 10)
    }

    test fun drop() {
        assertSequence(sequence(10).drop(5), 5, 5)
    }

    test fun dropNone() {
        val vector = sequence(10)
        assertSequence(vector.drop(0), 0, 10)
        assertSequence(vector.drop(-1), 0, 10)
    }

    test fun dropAll() {
        assertSequence(sequence(10).drop(100), 0, 0)
    }

    test fun first() {
        assertEquals(0, sequence(10).first())
        assertNull(sequence(0).first())
    }

    test fun last() {
        assertEquals(9, sequence(10).last())
        assertNull(sequence(0).last())
    }

    fun assertSequence(vector: Vector<Int>, from: Int, length: Int) {
        var from_ = from
        assertEquals(length, vector.size())
        for (integer in vector)
        {
            assertEquals(from_++, integer)
        }
    }

    test fun isEmpty() {
        val vector = Vector.empty<String>()
        assertTrue(vector.isEmpty())
        assertFalse(vector.append("A").isEmpty())
    }

    test fun addGet() {
        for (size in sizes()) {
            var vector = Vector.empty<Int>()
            for (i in 0..size - 1) {
                vector = vector.append(i)
            }
            assertEquals(size, vector.size())

            var i: Int = 0
            for (integer in vector)
            {
                assertEquals(i++, integer)
            }
        }
    }

    test fun addPrependGet() {
        for (size in sizes()) {
            var vector = Vector.empty<Int>()
            val half: Int = size / 2
            for (i in 0..half - 1) {
                vector = vector.append(half + i)
                vector = vector.prepend(half - i - 1)
            }
            assertEquals(size, vector.size())

            for (i in 0..size - 1) {
                assertEquals(i, vector[i])
            }

            var i: Int = 0
            for (integer in vector)
            {
                assertEquals(i++, integer)
            }
        }
    }

    test fun split() {
        for (size in sizes()) {
            var vector = sequence(size)
            val half = size / 2

            val (top, bottom) = vector.splitAt(half)

            assertEquals(half, top?.size())
            assertEquals(half, bottom?.size())

            var i: Int = 0
            for (integer in top!!)
            {
                assertEquals(i++, integer)
            }

            for (integer in bottom!!)
            {
                assertEquals(i++, integer)
            }
        }
    }

    test fun prependGet() {
        for (size in sizes()) {
            var vector = Vector.empty<Int>()
            var i = size - 1
            while (i >= 0) {
                vector = vector.prepend(i--)
            }

            assertEquals(size, vector.size())

            i = 0
            for (integer in vector)
            {
                assertEquals(i++, integer)
            }
        }
    }

    test fun buildIterate() {
        for (size in sizes()) {
            var vector = sequence(size)
            var i: Int = 0
            for (integer in vector)
            {
                assertEquals(i++, integer)
            }
        }
    }

    test fun update() {
        for (size in sizes()) {
            var vector = sequence(size)
            for (i in 0..size - 1) {
                vector = vector.set(i, i + 1)
            }
            for (i in 0..size - 1) {
                assertEquals(i + 1, vector[i])
            }
        }
    }

    fun sequence(size: Int): Vector<Int> {
        val builder = Vector.factory<Int>().newBuilder()
        for (i in 0..size - 1) {
            builder.add(i)
        }
        return builder.build()
    }
}