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

package com.github.andrewoma.dexx.collection.performance;

import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.github.andrewoma.dexx.TestMode;
import com.github.andrewoma.dexx.collection.BuilderFactory;
import com.github.andrewoma.dexx.collection.HashSet;
import com.github.andrewoma.dexx.collection.Set;
import com.github.andrewoma.dexx.collection.TreeSet;
import com.github.andrewoma.dexx.collection.mutable.MutableHashSet;
import com.github.andrewoma.dexx.collection.mutable.MutableTreeSet;
import org.jetbrains.annotations.NotNull;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class SetPerformanceTest {
    private static int[] rnd;
    private static java.util.Map<String, java.util.Map<Class, Set<Integer>>> setResults = new HashMap<String, Map<Class, Set<Integer>>>();
    private static java.util.Map<String, java.util.Map<Class, Integer>> intResults = new HashMap<String, Map<Class, Integer>>();

    private static Set<Integer> dexxHashSet = HashSet.<Integer>factory().newBuilder().build();
    private static Set<Integer> javaHashSet = MutableHashSet.<Integer>factory().newBuilder().build();
    private static Set<Integer> dexxTreeSet = TreeSet.<Integer>factory(null).newBuilder().build();
    private static Set<Integer> javaTreeSet = MutableTreeSet.<Integer>factory(null).newBuilder().build();

    private Comparator<Integer> ordering = new Comparator<Integer>() {
        @Override
        public int compare(@NotNull Integer o1, @NotNull Integer o2) {
            return o1.compareTo(o2);
        }
    };

    @Rule
    public org.junit.rules.TestRule benchmarkRun = new BenchmarkRule();

    public boolean disabled() {
        return !TestMode.isEnabled(TestMode.BENCHMARK);
    }

    @BeforeClass
    public static void prepare() {
        int count = 100000;
        rnd = new int[count];

        final Random random = new Random();
        for (int i = 0; i < count; i++) {
            int value = Math.abs(random.nextInt());
            rnd[i] = value;

            // Build some default collections
            dexxHashSet = dexxHashSet.add(value);
            javaHashSet = javaHashSet.add(value);
            dexxTreeSet = dexxTreeSet.add(value);
            javaTreeSet = javaTreeSet.add(value);
        }
    }

    @AfterClass
    public static void compare() {
        for (Map.Entry<String, Map<Class, Set<Integer>>> entry : setResults.entrySet()) {
            System.out.println("Verifying " + entry.getKey());
            Iterator<Set<Integer>> sets = entry.getValue().values().iterator();
            Set<Integer> first = sets.next();
            System.out.println("    Comparing " + first.getClass().getSimpleName());
            while (sets.hasNext()) {
                Set<Integer> next = sets.next();
                System.out.print("    to " + next.getClass().getSimpleName() + " ... ");
                assertEquals(first, next);
                System.out.println("OK");
            }
        }

        for (Map.Entry<String, Map<Class, Integer>> entry : intResults.entrySet()) {
            System.out.print("Verifying " + entry.getKey() + " ... ");
            Iterator<Integer> ints = entry.getValue().values().iterator();
            Integer first = ints.next();
            while (ints.hasNext()) {
                Integer next = ints.next();
                assertEquals(first, next);
            }
            System.out.println("OK");
        }
    }

    @Test
    public void addRandomJavaHashSet() {
        addRandom(MutableHashSet.<Integer>factory());
    }

    @Test
    public void addRandomDexxHashSet() {
        addRandom(HashSet.<Integer>factory());
    }

    @Test
    public void addRandomDexxTreeSet() {
        //noinspection RedundantTypeArguments
        addRandom(TreeSet.<Integer>factory(ordering));
    }

    @Test
    public void addRandomJavaTreeSet() {
        //noinspection RedundantTypeArguments
        addRandom(MutableTreeSet.<Integer>factory(ordering));
    }

    @Test
    public void addRandomThenRemoveJavaHashSet() {
        addRandomThenRemove(MutableHashSet.<Integer>factory());
    }

    private void addRandomThenRemove(BuilderFactory<Integer, ? extends Set<Integer>> factory) {
        if (disabled()) return;

        Set<Integer> result = factory.newBuilder().build();
        for (int i : rnd) {
            result = result.add(i);
        }
        for (int i : rnd) {
            result = result.remove(i);
        }
        assertEquals(0, result.size());
        getIntResults("addRandomThenRemove").put(result.getClass(), result.size());
    }

    @Test
    public void addRandomThenRemoveDexxHashSet() {
        if (disabled()) return;
        addRandomThenRemove(HashSet.<Integer>factory());
    }

    @Test
    public void addRandomThenRemoveDexxTreeSet() {
        if (disabled()) return;
        //noinspection RedundantTypeArguments
        addRandomThenRemove(TreeSet.<Integer>factory(ordering));
    }

    @Test
    public void addRandomThenRemoveJavaTreeSet() {
        if (disabled()) return;
        //noinspection RedundantTypeArguments
        addRandomThenRemove(MutableTreeSet.<Integer>factory(ordering));
    }

    @Test
    public void containsRandomJavaHashSet() {
        containsRandom(javaHashSet);
    }

    private void containsRandom(Set<Integer> set) {
        if (disabled()) return;

        int result = 0;
        for (int i : rnd) {
            if (set.contains(i)) result += i;
        }

        getIntResults("containsRandom").put(set.getClass(), result);
    }

    @Test
    public void containsRandomDexxHashSet() {
        containsRandom(dexxHashSet);
    }

    @Test
    public void containsRandomDexxTreeSet() {
        containsRandom(dexxTreeSet);
    }

    @Test
    public void containsRandomJavaTreeSet() {
        containsRandom(javaTreeSet);
    }

    private void addRandom(BuilderFactory<Integer, ? extends Set<Integer>> factory) {
        if (disabled()) return;

        Set<Integer> result = factory.newBuilder().build();
        for (int i : rnd) {
            result = result.add(i);
        }
        getSetResults("addRandom").put(result.getClass(), result); // Only store one result per class for verification
    }

    private Map<Class, Set<Integer>> getSetResults(String category) {
        Map<Class, Set<Integer>> sets = setResults.get(category);
        if (sets == null) {
            sets = new HashMap<Class, Set<Integer>>();
            setResults.put(category, sets);
        }

        return sets;
    }

    private Map<Class, Integer> getIntResults(String category) {
        Map<Class, Integer> ints = intResults.get(category);
        if (ints == null) {
            ints = new HashMap<Class, Integer>();
            intResults.put(category, ints);
        }

        return ints;
    }
}
