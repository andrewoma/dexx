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
import org.junit.Test as test
import kotlin.test.fail
import org.junit.Assume
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import java.util.Comparator

abstract class AbstractTraversableTest() {
    protected abstract fun factory<T>() : BuilderFactory<T, out Traversable<T>>
    open val maxSize = 10000

    fun isMutable(travserable: Traversable<*>) = travserable.getClass().getSimpleName().contains("Mutable")

    protected open fun build_<T>(vararg ts : T) : Traversable<T> {
        val builder = factory<T>().newBuilder()
        for (t : T in ts) builder.add(t)
        return builder.result()
    }

    private fun build<T>(vararg ts: T) = build_(*ts)

    test fun builder() {
        val actual = factory<Int>().newBuilder().add(1).addAll(2, 3, 4).addAll(build(5, 6, 7)).result()
        assertEquals(build(1, 2, 3, 4, 5, 6, 7), actual)
    }

    test fun forEach() {
        val actual = hashSetOf<Int>()
        build(1, 2, 3).forEach { actual.add(it!!) }
        assertEquals(setOf(1, 2, 3), actual)
    }

    test fun emptyForEach() {
        build<Int>().forEach { fail("Shouldn't be called") }
    }

    test fun singleForEach() {
        val actual = hashSetOf<Int>()
        build<Int>(1).forEach { actual.add(it!!) }
        assertEquals(setOf(1), actual)
    }

    test fun forEachLarge() {
        var builder = factory<Int>().newBuilder()
        val expected = hashSetOf<Int>()
        for (i in 1..maxSize) {
            builder = builder.add(i)
            expected.add(i)
        }
        val actual = hashSetOf<Int>()
        builder.result().forEach { actual.add(it!!) }
        assertEquals(expected, actual)
    }

    test fun toSet() {
        assertEquals(build(1, 1, 2, 2).toSet(), HashSet.factory<Int>().newBuilder().addAll(1, 2).result())
    }

    // Converts to a list so the order of traversal is known (may not be the order of insertion for Sets et al)
    fun <T> toList(traversable: Traversable<T>) : jet.List<T> {
        val ordered = arrayListOf<T>()
        traversable.forEach { ordered.add(it!!) }
        return ordered
    }

    test fun makeString() {
        val traversable = build(1, 2, 3)
        assertEquals(toList(traversable).makeString(","), traversable.makeString(","))
    }

    test fun makeStringFull() {
        val traversable = build(1, 2, 3)
        assertEquals("prefix" + toList(traversable).makeString(",") + "postfix", traversable.makeString(",", "prefix", "postfix", -1, ""))
    }

    test fun makeStringTruncated() {
        val traversable = build(1, 2, 3)
        val list = toList(traversable)
        assertEquals("prefix" + list[0] + "," + list[1] + ",...postfix", traversable.makeString(",", "prefix", "postfix", 2, "..."))
    }

    test fun toSortedSet() {
        assertEquals(TreeSet.factory<Int>(null).newBuilder().addAll(1, 2, 3).result(), build(3, 1, 2).toSortedSet(null))
    }

    test fun toSortedSetWithComparator() {
        object comparator : Comparator<Int> {
            [suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")]
            override fun compare(o1: Int, o2: Int): Int {
                return o1.compareTo(o2) * -1
            }
        }

        assertEquals(TreeSet.factory<Int>(comparator).newBuilder().addAll(1, 2, 3).result(), build(3, 1, 2).toSortedSet(comparator))
    }

    test fun toIndexedList() {
        val traversable = build(1, 2, 3)
        assertEquals(Vector.factory<Int>().newBuilder().addAll(toList(traversable)).result(), traversable.toIndexedList());
    }

    test fun toString() {
        val traversable = build(1, 2, 3)
        assertEquals(traversable.getClass().getSimpleName() + "(" + toList(traversable).makeString(", ") + ")", traversable.toString())
    }

    test fun clearBuilder() {
        assertEquals(build(3, 4, 5), factory<Int>().newBuilder().addAll(1, 2, 3).clear().addAll(3, 4, 5).result())
    }
}
