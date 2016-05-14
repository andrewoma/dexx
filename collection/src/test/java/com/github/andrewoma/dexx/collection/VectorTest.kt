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

import com.github.andrewoma.dexx.TestMode
import org.junit.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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

    @Test fun isEmpty() {
        val vector = Vector.empty<String>()
        assertTrue(vector.isEmpty)
        assertFalse(vector.append("A").isEmpty)
    }

    @Test fun addGet() {
        for (size in sizes()) {
            var vector = Vector.empty<Int>()
            for (i in 0..size - 1) {
                vector = vector.append(i)
            }
            assertEquals(size, vector.size())

            var i: Int = 0
            for (integer in vector) {
                assertEquals(i++, integer)
            }
        }
    }

    @Test fun addPrependGet() {
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
            for (integer in vector) {
                assertEquals(i++, integer)
            }
        }
    }

    @Test fun split() {
        for (size in sizes()) {
            val vector = sequence(size) as Vector<Int>
            val half = size / 2

            val (top, bottom) = vector.splitAt(half)

            assertEquals(half, top?.size())
            assertEquals(half, bottom?.size())

            var i: Int = 0
            for (integer in top!!) {
                assertEquals(i++, integer)
            }

            for (integer in bottom!!) {
                assertEquals(i++, integer)
            }
        }
    }

    @Test fun prependGet() {
        for (size in sizes()) {
            var vector = Vector.empty<Int>()
            var i = size - 1
            while (i >= 0) {
                vector = vector.prepend(i--)
            }

            assertEquals(size, vector.size())

            i = 0
            for (integer in vector) {
                assertEquals(i++, integer)
            }
        }
    }

    @Test fun buildIterate() {
        for (size in sizes()) {
            val vector = sequence(size)
            var i: Int = 0
            for (integer in vector) {
                assertEquals(i++, integer)
            }
        }
    }

    @Test fun update() {
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
}