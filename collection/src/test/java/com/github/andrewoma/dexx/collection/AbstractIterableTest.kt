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
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class AbstractIterableTest() : AbstractTraversableTest() {

    override abstract fun <T> factory(): BuilderFactory<T, out Iterable<T>>

    private fun <T> build(vararg ts: T) = build_(*ts) as Iterable<T>

    @Test fun iterateEmpty() {
        assertFalse(build<Int>().iterator().hasNext())
    }

    @Test(expected = NoSuchElementException::class) fun iterateEmptyNext() {
        build<Int>().iterator().next()
    }

    @Test fun iterateSingle() {
        val i = build(1).iterator()
        assertTrue(i.hasNext())
        assertEquals(1, i.next())
        assertFalse(i.hasNext())
    }

    @Test(expected = NoSuchElementException::class) fun iterateSingleNext() {
        val i = build(1).iterator()
        assertTrue(i.hasNext())
        assertEquals(1, i.next())
        assertFalse(i.hasNext())
        i.next()
    }

    @Test(expected = UnsupportedOperationException::class) fun iteratorRemove() {
        val iterable = build(1)
        if (isMutable(iterable)) throw UnsupportedOperationException()
        val i = iterable.iterator()
        i.next()
        i.remove()
    }

    @Test(expected = UnsupportedOperationException::class) fun iteratorRemove2() {
        val iterable = build(1, 2)
        if (isMutable(iterable)) throw UnsupportedOperationException()
        val i = iterable.iterator()
        i.next()
        i.next()
        i.remove()
    }

    @Test fun iterator() {
        assertEquals(setOf(1, 2, 3, 4), build(1, 2, 3, 4).iterator().asSequence().toSet())
    }

    @Test fun iteratorWithCollisions() {
        val keys = setOf(CollidingKey(1, 1), CollidingKey(1, 2), CollidingKey(2, 3), CollidingKey(2, 4))
        assertEquals(keys, build(*keys.toTypedArray()).iterator().asSequence().toSet())
    }

    @Test fun iterableLarge() {
        var builder = factory<Int>().newBuilder()
        val expected = hashSetOf<Int>()
        for (i in 1..maxSize) {
            builder = builder.add(i)
            expected.add(i)
        }
        val actual = hashSetOf<Int>()
        builder.build().forEach { actual.add(it) }
        assertEquals(expected, actual)
    }
}
