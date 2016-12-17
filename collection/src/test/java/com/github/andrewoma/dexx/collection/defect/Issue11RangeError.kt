/*
 * Copyright (c) 2016 Andrew O'Malley
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

package com.github.andrewoma.dexx.collection.defect

import com.github.andrewoma.dexx.collection.TreeMap
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class Issue11RangeError {

    val random = Random()

    @Test fun shouldReturnValidRangeTo() {
        var map = TreeMap<Int, String>()
        map = map.put(0, "0")
        map = map.put(50, "50")
        assertEquals(map.to(101, false).values().toSortedSet(), sortedSetOf("0", "50"))
    }

    @Test fun shouldReturnValidRangeToForSequence() {
        var map = TreeMap<Int, Int>()

        for (i in 0..1000) {
            map = map.put(i, i)
            assertEquals(map.to(i + 1, false).values().toList(), (0..i).toList())
            assertEquals(map.to(i, true).values().toList(), (0..i).toList())
            assertEquals(map.to(i - 1, true).values().toList(), (0..i - 1).toList())
        }
    }

    @Test fun shouldReturnValidRangeToForRandom() {

        repeat(100) {
            var map = TreeMap<Int, Int>()
            val values = sortedSetOf<Int>()

            repeat(100) {
                val value = random.nextInt()
                map = map.put(value, value)
                values.add(value)

                val last = values.last()

                assertEquals(map.to(last + 1, false).values().toList(), values.toList())
                assertEquals(map.to(last, true).values().toList(), values.toList())
                assertEquals(map.to(last, false).values().toList(), values.subSet(values.first(), values.last()).toList())
            }
        }
    }
}
