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
 * {@code Sets} is the preferred method of constructing instances of {@code Set}.
 * <p/>
 * <p>{@link com.github.andrewoma.dexx.collection.HashSet} is currently constructed in
 * all cases, however this may change in the future.
 * <p/>
 * <p>{@code Sets} is preferred for construction as:
 * <ul>
 * <li>It works better in languages that support type inference
 * <li>It allows future optimisations (e.g. small sets be dedicated classes which are then upgraded to {@code HashSets})
 * </ul>
 */
public class Sets {
    private Sets() {
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> Set<E> of() {
        return construct();
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> Set<E> of(E t) {
        return construct(t);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> Set<E> of(E e1, E e2) {
        return construct(e1, e2);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> Set<E> of(E e1, E e2, E e3) {
        return construct(e1, e2, e3);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> Set<E> of(E e1, E e2, E e3, E e4) {
        return construct(e1, e2, e3, e4);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5) {
        return construct(e1, e2, e3, e4, e5);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
        return construct(e1, e2, e3, e4, e5, e6);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
        return construct(e1, e2, e3, e4, e5, e6, e7);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
        return construct(e1, e2, e3, e4, e5, e6, e7, e8);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
        return construct(e1, e2, e3, e4, e5, e6, e7, e8, e9);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
        return construct(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> Set<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E... others) {
        Set<E> set = construct(e1, e2, e3, e4, e5, e6, e7, e8, e9, e10);
        for (E e : others) {
            set = set.add(e);
        }
        return set;
    }

    private static <E> Set<E> construct(E... es) {
        Set<E> set = HashSet.empty();
        for (E e : es) {
            set = set.add(e);
        }
        return set;
    }

    @NotNull
    public static <E> Set<E> copyOf(java.lang.Iterable<E> iterable) {
        HashSet<E> result = HashSet.empty();
        for (E e : iterable) {
            result = result.add(e);
        }
        return result;
    }

    @NotNull
    public static <E> Set<E> copyOf(Iterator<E> iterator) {
        HashSet<E> result = HashSet.empty();
        while (iterator.hasNext()) {
            result = result.add(iterator.next());
        }
        return result;
    }

    @NotNull
    public static <E> Set<E> copyOf(E[] es) {
        HashSet<E> result = HashSet.empty();
        for (E e : es) {
            result = result.add(e);
        }
        return result;
    }

    @NotNull
    public static <E> Set<E> copyOfTraversable(Traversable<E> traversable) {
        @SuppressWarnings("unchecked")
        final HashSet<E>[] result = new HashSet[1];
        result[0] = HashSet.empty();
        traversable.forEach(new Function<E, Object>() {
            @Override
            public Object invoke(E e) {
                result[0] = result[0].add(e);
                return null;
            }
        });

        return result[0];
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> BuilderFactory<E, Set<E>> factory() {
        return (BuilderFactory) HashSet.<E>factory();
    }

    @NotNull
    public static <E> Builder<E, Set<E>> builder() {
        return Sets.<E>factory().newBuilder();
    }
}
