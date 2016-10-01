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

class ArrayListsTest {
    private fun <T> build(vararg ts: T): ArrayList<T> {
        val builder = ArrayList.factory<T>().newBuilder()
        for (t in ts) {
            builder.add(t)
        }
        return builder.build()
    }

    @Test fun of() {
        assertEquals(build<Int>(), ArrayLists.of<Int>())
        assertEquals(build(1), ArrayLists.of(1))
        assertEquals(build(1, 2), ArrayLists.of(1, 2))
        assertEquals(build(1, 2, 3), ArrayLists.of(1, 2, 3))
        assertEquals(build(1, 2, 3, 4), ArrayLists.of(1, 2, 3, 4))
        assertEquals(build(1, 2, 3, 4, 5), ArrayLists.of(1, 2, 3, 4, 5))
        assertEquals(build(1, 2, 3, 4, 5, 6), ArrayLists.of(1, 2, 3, 4, 5, 6))
        assertEquals(build(1, 2, 3, 4, 5, 6, 7), ArrayLists.of(1, 2, 3, 4, 5, 6, 7))
        assertEquals(build(1, 2, 3, 4, 5, 6, 7, 8), ArrayLists.of(1, 2, 3, 4, 5, 6, 7, 8))
        assertEquals(build(1, 2, 3, 4, 5, 6, 7, 8, 9), ArrayLists.of(1, 2, 3, 4, 5, 6, 7, 8, 9))
        assertEquals(build(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), ArrayLists.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        assertEquals(build(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), ArrayLists.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11))
        assertEquals(build(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), ArrayLists.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))
    }

    @Test fun copyOfCollection() {
        assertEquals(build(1, 2, 3), ArrayLists.copyOf(java.util.Arrays.asList(1, 2, 3)))
    }

    @Test fun copyOfIterator() {
        assertEquals(build(1, 2, 3), ArrayLists.copyOf(Vector.empty<Int>().append(1).append(2).append(3).iterator()))
    }

    @Test fun copyOfArray() {
        assertEquals(build(1, 2, 3), ArrayLists.copyOf(java.util.Arrays.asList(1, 2, 3).toTypedArray()))
    }

    @Test fun copyOfTraversable() {
        assertEquals(build(1, 2, 3), ArrayLists.copyOfTraversable(Vector.empty<Int>().append(1).append(2).append(3)))
    }

    @Test fun factory() {
        assertEquals(build(1, 2, 3), ArrayLists.factory<Int>().newBuilder().addAll(1, 2, 3).build())
    }

    @Test fun builder() {
        assertEquals(build(1, 2, 3), ArrayLists.builder<Int>().addAll(1, 2, 3).build())
    }
}