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

import com.github.andrewoma.dexx.collection.Builder
import com.github.andrewoma.dexx.collection.Sets
import com.github.andrewoma.dexx.collection.SortedSet
import com.github.andrewoma.dexx.collection.SortedSets
import com.github.andrewoma.dexx.collection.Maps

import org.junit.Test as test
import kotlin.test.assertEquals

class BlogTest {
//    fun <T, R> Stream<T>.build(builder: Builder<T, R>): R {
//        this.forEach { builder.add(it) }
//        return builder.build()
//    }
//
//    fun <T> Stream<T>.toPersistentSet(): com.github.andrewoma.dexx.collection.Set<T>
//            = build(Sets.builder<T>())
//
//    fun <T> Stream<T>.toPersistentSortedSet(): SortedSet<T>
//            = build(SortedSets.builder<T>())
//
//    fun <K, V> Stream<com.github.andrewoma.dexx.collection.Pair<K, V>>.toPersistentMap(): com.github.andrewoma.dexx.collection.Map<K, V>
//            = build(Maps.builder<K, V>())
//
    test fun lazyEvaluation() {
//        val set = SortedSets.of(1, 2, 3, 4, 5, 6).stream()
//                .filter { it % 2 == 0 }
//                .map { "$it is even" }
//                .take(2)
//                .toPersistentSortedSet()
//
//        assertEquals(SortedSets.of("2 is even", "4 is even"), set)
    }
}