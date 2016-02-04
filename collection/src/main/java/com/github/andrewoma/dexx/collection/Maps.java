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

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * {@code Maps} is the preferred method of constructing instances of {@code Map}.
 * <p/>
 * <p>{@link com.github.andrewoma.dexx.collection.HashMap} is currently constructed in
 * all cases, however this may change in the future.
 * <p/>
 * <p>{@code Maps} is preferred for construction as:
 * <ul>
 * <li>It works better in languages that support type inference
 * <li>It allows future optimisations (e.g. small maps may be dedicated classes which are then upgraded to {@code HashMaps})
 * </ul>
 */
public class Maps {
    private Maps() {
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> of() {
        return construct();
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> of(K k, V v) {
        return construct(p(k, v));
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        return construct(p(k1, v1), p(k2, v2));
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return construct(p(k1, v1), p(k2, v2), p(k3, v3));
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return construct(p(k1, v1), p(k2, v2), p(k3, v3), p(k4, v4));
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return construct(p(k1, v1), p(k2, v2), p(k3, v3), p(k4, v4), p(k5, v5));
    }

    private static <K, V> Pair<K, V> p(K k, V v) {
        return new Pair<K, V>(k, v);
    }

    private static <K, V> Map<K, V> construct(Pair<K, V>... pairs) {
        Map<K, V> map = HashMap.empty();
        for (Pair<K, V> pair : pairs) {
            map = map.put(pair.component1(), pair.component2());
        }
        return map;
    }

    @NotNull
    public static <K, V> Map<K, V> copyOf(java.lang.Iterable<Pair<K, V>> iterable) {
        HashMap<K, V> result = HashMap.empty();
        for (Pair<K, V> pair : iterable) {
            result = result.put(pair.component1(), pair.component2());
        }

        return result;
    }

    @NotNull
    public static <K, V> Map<K, V> copyOf(Iterator<Pair<K, V>> iterator) {
        HashMap<K, V> result = HashMap.empty();
        while (iterator.hasNext()) {
            Pair<K, V> pair = iterator.next();
            result = result.put(pair.component1(), pair.component2());
        }

        return result;
    }

    @NotNull
    public static <K, V> Map<K, V> copyOf(Pair<K, V>[] pairs) {
        HashMap<K, V> result = HashMap.empty();
        for (Pair<K, V> pair : pairs) {
            result = result.put(pair.component1(), pair.component2());
        }
        return result;
    }

    @NotNull
    public static <K, V> Map<K, V> copyOfTraversable(Traversable<Pair<K, V>> traversable) {
        @SuppressWarnings("unchecked")
        final HashMap<K, V>[] result = new HashMap[1];
        result[0] = HashMap.empty();
        traversable.forEach(new Function<Pair<K, V>, Object>() {
            @Override
            public Object invoke(Pair<K, V> pair) {
                result[0] = result[0].put(pair.component1(), pair.component2());
                return null;
            }
        });

        return result[0];
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <K, V> BuilderFactory<Pair<K, V>, Map<K, V>> factory() {
        return (BuilderFactory) HashMap.<K, V>factory();
    }

    @NotNull
    public static <K, V> Builder<Pair<K, V>, Map<K, V>> builder() {
        return Maps.<K, V>factory().newBuilder();
    }
}
