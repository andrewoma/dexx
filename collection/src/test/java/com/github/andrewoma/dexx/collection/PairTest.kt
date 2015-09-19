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

/**
 *
 */
class PairTest {
    @Test fun equals() {
        assertEquals(Pair("1", "A"), Pair("1", "A"))
        assertFalse(Pair("1", "A").equals(Pair("1", "B")))
        assertFalse(Pair("1", "A").equals(Pair("2", "A")))
        val pair = Pair("1", "A")
        assertEquals(pair, pair)
        assertFalse(pair.equals(""))
        assertEquals(Pair(null, null), Pair(null, null))
        assertEquals(Pair(1, null), Pair(1, null))
        assertEquals(Pair(null, 1), Pair(null, 1))
        assertFalse(Pair(null, 1).equals(null))
    }

    @Test fun testHashCode() {
        assertEquals(Pair("1", "A").hashCode(), Pair("1", "A").hashCode())
        assertEquals(Pair(null, null).hashCode(), Pair(null, null).hashCode())
        assertEquals(Pair(1, null).hashCode(), Pair(1, null).hashCode())
        assertEquals(Pair(null, 1).hashCode(), Pair(null, 1).hashCode())
        assertFalse(Pair("1", "A").hashCode().equals(Pair("1", "B").hashCode()))
        assertFalse(Pair("1", "A").hashCode().equals(Pair("2", "A").hashCode()))
    }

    @Test fun testToString() {
        assertEquals("(1, A)", Pair("1", "A").toString())
    }
}