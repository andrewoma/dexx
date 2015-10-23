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

class MapsTest {
    private fun <K, V> build(vararg ts: kotlin.Pair<K, V>): Map<K, V> {
        val builder = HashMap.factory<K, V>().newBuilder()
        for (t in ts) {
            builder.add(Pair(t.first, t.second))
        }
        return builder.build()
    }

    @Test fun of() {
        assertEquals(build<Int, Int>(), Maps.of<Int, Int>())
        assertEquals(build(1 to "a"), Maps.of(1, "a"))
        assertEquals(build(1 to "a", 2 to "b"), Maps.of(1, "a", 2, "b"))
        assertEquals(build(1 to "a", 2 to "b", 3 to "c"), Maps.of(1, "a", 2, "b", 3, "c"))
        assertEquals(build(1 to "a", 2 to "b", 3 to "c", 4 to "d"), Maps.of(1, "a", 2, "b", 3, "c", 4, "d"))
        assertEquals(build(1 to "a", 2 to "b", 3 to "c", 4 to "d", 5 to "e"), Maps.of(1, "a", 2, "b", 3, "c", 4, "d", 5, "e"))
    }

    @Test fun copyOfCollection() {
        assertEquals(build(1 to "a", 2 to "b"), Maps.copyOf(arrayListOf(Pair(1, "a"), Pair(2, "b"))))
    }

    @Test fun copyOfIterator() {
        assertEquals(build(1 to "a", 2 to "b"), Maps.copyOf(arrayListOf(Pair(1, "a"), Pair(2, "b")).iterator()))
    }

    @Test fun copyOfArray() {
        assertEquals(build(1 to "a", 2 to "b"), Maps.copyOf(arrayListOf(Pair(1, "a"), Pair(2, "b")).toTypedArray()))
    }

    @Test fun copyOfTraversable() {
        assertEquals(build(1 to "a", 2 to "b"), Maps.copyOfTraversable(Vector.empty<Pair<Int, String>>().append(Pair(1, "a")).append(Pair(2, "b"))))
    }

    @Test fun factory() {
        assertEquals(build(1 to "a", 2 to "b"), Maps.factory<Int, String>().newBuilder().addAll(Pair(1, "a"), Pair(2, "b")).build())
    }

    @Test fun builder() {
        assertEquals(build(1 to "a", 2 to "b"), Maps.builder<Int, String>().addAll(Pair(1, "a"), Pair(2, "b")).build())
    }
}