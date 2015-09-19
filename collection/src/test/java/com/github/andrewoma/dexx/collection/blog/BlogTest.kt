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

package com.github.andrewoma.dexx.collection.blog

import com.github.andrewoma.dexx.collection.*
import org.junit.Test
import kotlin.test.assertEquals

class BlogTest {
    fun <T, R> Sequence<T>.build(builder: Builder<T, R>): R {
        for (e in this) {
            builder.add(e)
        }
        return builder.build()
    }

    fun <T> Sequence<T>.toPersistentSet(): com.github.andrewoma.dexx.collection.Set<T>
            = build(Sets.builder<T>())

    fun <T> Sequence<T>.toPersistentSortedSet(): SortedSet<T>
            = build(SortedSets.builder<T>())

    fun <K, V> Sequence<com.github.andrewoma.dexx.collection.Pair<K, V>>.toPersistentMap(): com.github.andrewoma.dexx.collection.Map<K, V>
            = build(Maps.builder<K, V>())

    @Test fun lazyEvaluation() {
        val set = SortedSets.of(1, 2, 3, 4, 5, 6).asSequence()
                .filter { it % 2 == 0 }
                .map { "$it is even" }
                .take(2)
                .toPersistentSortedSet()

        assertEquals(SortedSets.of("2 is even", "4 is even"), set)
    }

    @Test fun setExample() {
        val set1 = Sets.of(1, 2, 3)
        val set2 = set1.add(4)
        val set3 = set1.remove(1)
        println(set1) // Prints Set(1, 2, 3)
        println(set2) // Prints Set(1, 2, 3, 4)
        println(set3) // Prints Set(2, 3)
    }

    @Test fun usageFragment1() {
        val set = Sets.of(1, 2, 3)
        println(set)
    }

    @Test fun usageFragment2() {
        val javaSet: kotlin.Set<Int> = setOf<Int>()
        val set = Sets.copyOf(javaSet)
        println(set)
    }

    @Test fun usageFragment3() {
        val builder = Sets.builder<Int>()
        for (i in 1..100) {
            builder.add(i)
        }
        val set = builder.build()
        println(set)
    }
}