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
import kotlin.test.assertEquals

class LinkedListsTest {
    private fun build<T>(vararg ts: T) : LinkedList<T> {
        val builder = ConsList.factory<T>().newBuilder()
        for (t in ts) { builder.add(t) }
        return builder.build()
    }

    test fun of() {
        assertEquals(build<Int>(), LinkedLists.of<Int>())
        assertEquals(build(1), LinkedLists.of(1))
        assertEquals(build(1, 2), LinkedLists.of(1, 2))
        assertEquals(build(1, 2, 3), LinkedLists.of(1, 2, 3))
        assertEquals(build(1, 2, 3, 4), LinkedLists.of(1, 2, 3, 4))
        assertEquals(build(1, 2, 3, 4, 5), LinkedLists.of(1, 2, 3, 4, 5))
        assertEquals(build(1, 2, 3, 4, 5, 6), LinkedLists.of(1, 2, 3, 4, 5, 6))
        assertEquals(build(1, 2, 3, 4, 5, 6, 7), LinkedLists.of(1, 2, 3, 4, 5, 6, 7))
        assertEquals(build(1, 2, 3, 4, 5, 6, 7, 8), LinkedLists.of(1, 2, 3, 4, 5, 6, 7, 8))
        assertEquals(build(1, 2, 3, 4, 5, 6, 7, 8, 9), LinkedLists.of(1, 2, 3, 4, 5, 6, 7, 8, 9))
        assertEquals(build(1, 2, 3, 4, 5, 6, 7, 8, 9, 10), LinkedLists.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10))
        assertEquals(build(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11), LinkedLists.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11))
        assertEquals(build(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), LinkedLists.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12))
    }

    test fun copyOfCollection() {
        assertEquals(build(1, 2, 3), LinkedLists.copyOf(java.util.Arrays.asList(1, 2, 3)))
    }

    test fun copyOfIterator() {
        assertEquals(build(1, 2, 3), LinkedLists.copyOf(Vector.empty<Int>().append(1).append(2).append(3).iterator()))
    }

    test fun copyOfArray() {
        assertEquals(build(1, 2, 3), LinkedLists.copyOf(java.util.Arrays.asList(1, 2, 3).copyToArray()))
    }

    test fun copyOfTraversable() {
        assertEquals(build(1, 2, 3), LinkedLists.copyOfTraversable(Vector.empty<Int>().append(1).append(2).append(3)))
    }

    test fun factory() {
        assertEquals(build(1, 2, 3), LinkedLists.factory<Int>().newBuilder().addAll(1, 2, 3).build())
    }

    test fun builder() {
        assertEquals(build(1, 2, 3), LinkedLists.builder<Int>().addAll(1, 2, 3).build())
    }
}