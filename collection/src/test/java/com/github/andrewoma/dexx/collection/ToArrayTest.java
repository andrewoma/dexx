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

package com.github.andrewoma.dexx.collection;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;

/**
 * toArray() seems difficult to call via Kotlin, so fall back to Java
 */
public class ToArrayTest {
    private java.util.List<Iterable<Pair<Integer, Integer>>> collections = Lists.newArrayList();
    private Comparator<Pair<Integer, Integer>> comparator = new Comparator<Pair<Integer, Integer>>() {
        @Override
        public int compare(@NotNull Pair<Integer, Integer> o1, @NotNull Pair<Integer, Integer> o2) {
            return o1.component1().compareTo(o2.component1());
        }
    };

    @Before
    public void setUp() {
        // Build collections of each type containing 10 elements (pairs of ints)
        java.util.List<Builder<Pair<Integer, Integer>, ? extends Iterable<Pair<Integer, Integer>>>> builders = ImmutableList.of(
                Sets.<Pair<Integer, Integer>>builder(),
                SortedSets.builder(comparator),
                IndexedLists.<Pair<Integer, Integer>>builder(),
                Maps.<Integer, Integer>builder(),
                SortedMaps.<Integer, Integer>builder()
        );

        for (Builder<Pair<Integer, Integer>, ? extends Iterable<Pair<Integer, Integer>>> builder : builders) {
            for (int i = 1; i <= 10; i++) {
                builder.add(new Pair<Integer, Integer>(i, i));
            }

            collections.add(builder.build());
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testToArrayNoArgs() {
        for (Iterable<Pair<Integer, Integer>> collection : collections) {
            @SuppressWarnings("unchecked")
            Object[] objects = collection.toArray();
            Pair<Integer, Integer>[] pairs = (Pair<Integer, Integer>[]) new Pair[objects.length];
            int i = 0;
            for (Object object : objects) {
                pairs[i++] = (Pair<Integer, Integer>) object;
            }
            assertArraySequence(pairs, collection.size());
        }
    }

    private void assertArraySequence(Pair<Integer, Integer>[] objects, int size) {
        assertEquals(size, objects.length);
        ArrayList<Pair<Integer, Integer>> pairs = Lists.newArrayList(objects);
        Collections.sort(pairs, comparator);

        int i = 1;
        for (Pair<Integer, Integer> pair : pairs) {
            assertEquals(i, pair.component1().intValue());
            assertEquals(i, pair.component2().intValue());
            i++;
        }
    }

    @Test
    public void testToArrayWithEmptyArray() {
        for (Iterable<Pair<Integer, Integer>> collection : collections) {
            @SuppressWarnings("unchecked")
            Pair<Integer, Integer>[] objects = collection.toArray(new Pair[0]);
            assertArraySequence(objects, collection.size());
        }
    }

    @Test
    public void testToArrayWithCorrectlySizedArray() {
        for (Iterable<Pair<Integer, Integer>> collection : collections) {
            @SuppressWarnings("unchecked")
            Pair<Integer, Integer>[] objects = collection.toArray(new Pair[collection.size()]);
            assertArraySequence(objects, collection.size());
        }
    }
}
