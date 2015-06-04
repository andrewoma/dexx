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

abstract class AbstractSetTest() : AbstractIterableTest() {

    private fun build<T>(vararg ts: T) = build_(*ts) as Set<T>

    test fun add() {
        var set = build<Int>()
        assertTrue(set.isEmpty())
        assertEquals(0, set.size())
        
        set = set.add(1)
        assertTrue(set.contains(1))
        assertFalse(set.isEmpty())
        assertEquals(1, set.size())
    }
    
    test fun remove() {
        var set = build(1)
        assertFalse(set.isEmpty())
        assertEquals(1, set.size())
        
        set = set.remove(1)
        assertTrue(set.isEmpty())
        assertEquals(0, set.size())
    }

    test fun removeNotExists() {
        var set = build(1)
        assertTrue(set.contains(1))

        var set2 = set.remove(2)
        assertEquals(set, set2)
    }
    
    test fun trieRemoveNotExists() {
        var set = build(1, 2)
        set = set.remove(3)
        assertEquals(build(1, 2), set)
    }

    test fun removeFromEmpty() {
        assertTrue(build<Int>().remove(1).isEmpty())
    }

    test fun removeFromSize1() {
        assertTrue(build(1).remove(1).isEmpty())
    }

    test fun updateSize1() {
        assertEquals(build(1), build(1).add(1))
    }

    test fun containsFromEmpty() {
        assertFalse(build<Int>().contains(1))
    }

    test fun putGetRemoveMultiple() {
        var set = build(1, 2, 3, 4)
        assertTrue(set.contains(1))
        assertTrue(set.contains(2))
        assertTrue(set.contains(3))
        assertTrue(set.contains(4))
        set = set.remove(1).remove(2).remove(3).remove(4)
        assertTrue(set.isEmpty())
    }

    test fun immutablity() {
        val set1 = build(1)
        if (set1.javaClass.getSimpleName().contains("Mutable")) return // Shouldn't be immutable

        val set2 = set1.add(2)
        assertEquals(build(1), set1)
        assertEquals(build(1, 2), set2)
    }

    test fun largeDataSet() {
        var ints = build<Int>()

        val range = 1..maxSize
        for (i in range) ints = ints.add(i)
        for (i in range) assertTrue(ints.contains(i))
        for (i in range) ints = ints.remove(i)

        assertTrue(ints.isEmpty())
    }

    test fun collisions() {
        val keys = setOf(CollidingKey(1, 1), CollidingKey(1, 2), CollidingKey(2, 3), CollidingKey(2, 4))
        val set = build(*keys.toTypedArray())
        for (i in set) assertTrue(set.contains(i))
    }

    test fun collisionsRemoved() {
        var set = build(CollidingKey(1, 1), CollidingKey(1, 2), CollidingKey(1, 3), CollidingKey(2, 4))
        assertEquals(4, set.size())

        set = set.remove(CollidingKey(1, 1))
        assertEquals(3, set.size())

        set = set.remove(CollidingKey(1, 2))
        assertEquals(2, set.size())

        set = set.remove(CollidingKey(1, 3))
        assertEquals(1, set.size())

        assertEquals(build(CollidingKey(2, 4)), set)
    }

    test fun outOfOrderInsertion() {
        assertEquals(build(3, 4, 2, 1), build(1, 2, 3, 4))
    }

    test fun duplicatesIgnored() {
        assertEquals(build(1, 2, 2, 3, 3, 3, 4, 4, 4, 4), build(1, 2, 3, 4))
    }

    test fun asSet() {
        assertEquals(setOf(1, 2, 3), build(1, 2, 3).asSet())
    }

    // TODO ... can/should Sets support containing nulls
//    test fun hashCodesWithNullValues() {
//        assertEquals(build(1, 2, null).hashCode(), build(1, 2, null).hashCode())
//    }
//
//    test fun equalsWithNullValues() {
//        assertEquals(build(1, 2, null), build(1, 2, null))
//        assertFalse(build(null).equals(build("1", "2", "3")))
//    }
}
