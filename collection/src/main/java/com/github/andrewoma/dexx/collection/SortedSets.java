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

import java.util.Comparator;
import java.util.Iterator;

/**
 * {@code SortedSets} is the preferred method of constructing instances of {@code SortedSet}.
 * <p/>
 * <p>{@link com.github.andrewoma.dexx.collection.TreeSet} is currently constructed in
 * all cases, however this may change in the future.
 * <p/>
 * <p>{@code SortedSets} is preferred for construction as:
 * <ul>
 * <li>It works better in languages that support type inference
 * <li>It allows future optimisations (e.g. small sets may be dedicated classes which are then upgraded to {@code TreeSets})
 * </ul>
 */
public class SortedSets {
    private SortedSets() {
    }

    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of() {
        return TreeSet.empty();
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(E e) {
        return construct(null, e);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(E e1, E e2) {
        return construct(null, e1, e2);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(E e1, E e2, E e3) {
        return construct(null, e1, e2, e3);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(E e1, E e2, E e3, E e4) {
        return construct(null, e1, e2, e3, e4);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(E e1, E e2, E e3, E e4, E e5) {
        return construct(null, e1, e2, e3, e4, e5);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6) {
        return construct(null, e1, e2, e3, e4, e5, e6);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
        return construct(null, e1, e2, e3, e4, e5, e6, e7);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
        return construct(null, e1, e2, e3, e4, e5, e6, e7, e8);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
        return construct(null, e1, e2, e3, e4, e5, e6, e7, e8, e9);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
        return construct(null, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E... others) {
        SortedSet<E> set = construct(null, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10);
        for (E e : others) {
            set = set.add(e);
        }
        return set;
    }

    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(Comparator<? super E> comparator) {
        return new TreeSet<E>(comparator);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(Comparator<? super E> comparator, E e) {
        return construct(comparator, e);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(Comparator<? super E> comparator, E e1, E e2) {
        return construct(comparator, e1, e2);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(Comparator<? super E> comparator, E e1, E e2, E e3) {
        return construct(comparator, e1, e2, e3);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(Comparator<? super E> comparator, E e1, E e2, E e3, E e4) {
        return construct(comparator, e1, e2, e3, e4);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(Comparator<? super E> comparator, E e1, E e2, E e3, E e4, E e5) {
        return construct(comparator, e1, e2, e3, e4, e5);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(Comparator<? super E> comparator, E e1, E e2, E e3, E e4, E e5, E e6) {
        return construct(comparator, e1, e2, e3, e4, e5, e6);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(Comparator<? super E> comparator, E e1, E e2, E e3, E e4, E e5, E e6, E e7) {
        return construct(comparator, e1, e2, e3, e4, e5, e6, e7);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(Comparator<? super E> comparator, E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8) {
        return construct(comparator, e1, e2, e3, e4, e5, e6, e7, e8);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(Comparator<? super E> comparator, E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9) {
        return construct(comparator, e1, e2, e3, e4, e5, e6, e7, e8, e9);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(Comparator<? super E> comparator, E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10) {
        return construct(comparator, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends Comparable<? super E>> SortedSet<E> of(Comparator<? super E> comparator, E e1, E e2, E e3, E e4, E e5, E e6, E e7, E e8, E e9, E e10, E... others) {
        SortedSet<E> set = construct(comparator, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10);
        for (E e : others) {
            set = set.add(e);
        }
        return set;
    }

    private static <E> SortedSet<E> construct(Comparator<? super E> comparator, E... es) {
        SortedSet<E> set = new TreeSet<E>(comparator);
        for (E e : es) {
            set = set.add(e);
        }
        return set;
    }

    @NotNull
    public static <E> SortedSet<E> copyOf(java.lang.Iterable<E> iterable) {
        return copyOf(null, iterable);
    }

    @NotNull
    public static <E> SortedSet<E> copyOf(Iterator<E> iterator) {
        return copyOf(null, iterator);
    }

    @NotNull
    public static <E> SortedSet<E> copyOf(E[] es) {
        return copyOf(null, es);
    }

    @NotNull
    public static <E> SortedSet<E> copyOf(Comparator<? super E> comparator, E[] es) {
        return construct(comparator, es);
    }

    @NotNull
    public static <E> SortedSet<E> copyOf(Comparator<? super E> comparator, java.lang.Iterable<E> iterable) {
        SortedSet<E> set = new TreeSet<E>(comparator);
        for (E e : iterable) {
            set = set.add(e);
        }
        return set;
    }

    @NotNull
    public static <E> SortedSet<E> copyOf(Comparator<? super E> comparator, Iterator<E> iterator) {
        SortedSet<E> set = new TreeSet<E>(comparator);
        while (iterator.hasNext()) {
            set = set.add(iterator.next());
        }
        return set;
    }

    @NotNull
    public static <E> SortedSet<E> copyOf(Comparator<? super E> comparator, Traversable<E> traversable) {
        @SuppressWarnings("unchecked")
        final SortedSet<E>[] set = new TreeSet[1];
        set[0] = new TreeSet<E>(comparator);
        traversable.forEach(new Function<E, Object>() {
            @Override
            public Object invoke(E e) {
                set[0] = set[0].add(e);
                return null;
            }
        });

        return set[0];
    }

    @NotNull
    public static <E> SortedSet<E> copyOfTraversable(Traversable<E> traversable) {
        return copyOfTraversable(null, traversable);
    }

    @NotNull
    public static <E> SortedSet<E> copyOfTraversable(Comparator<? super E> comparator, Traversable<E> traversable) {
        return copyOf(comparator, traversable);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> BuilderFactory<E, SortedSet<E>> factory() {
        return factory(null);
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> BuilderFactory<E, SortedSet<E>> factory(Comparator<? super E> comparator) {
        return (BuilderFactory) TreeSet.factory(comparator);
    }

    @NotNull
    public static <E> Builder<E, SortedSet<E>> builder() {
        return SortedSets.<E>factory().newBuilder();
    }

    @NotNull
    public static <E> Builder<E, SortedSet<E>> builder(Comparator<? super E> comparator) {
        //noinspection RedundantTypeArguments
        return SortedSets.<E>factory(comparator).newBuilder();
    }
}
