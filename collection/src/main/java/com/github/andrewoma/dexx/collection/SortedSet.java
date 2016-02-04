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
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;

/**
 * {@code SortedSet} defines the interface for sets that are sorted.
 */
public interface SortedSet<E> extends Set<E> {
    /**
     * Returns the first element in the set or {@code null} of the set is empty.
     */
    @Nullable
    E first();

    /**
     * Returns the last element in the set or {@code null} of the set is empty.
     */
    @Nullable
    E last();

    /**
     * Returns a set containing all elements in this set, excluding the first {@code number} of elements.
     */
    @NotNull
    SortedSet<E> drop(int number);

    /**
     * Returns a set containing the first {@code number} of elements from this set.
     */
    @NotNull
    SortedSet<E> take(int number);

    @Override
    @NotNull
    SortedSet<E> add(E value);

    @Override
    @NotNull
    SortedSet<E> remove(E value);

    /**
     * Returns the comparator associated with this map, or {@code null} if the default ordering is used.
     */
    Comparator<? super E> comparator();

    /**
     * Returns the bottom of the set starting from the key specified.
     *
     * @param inclusive if true, the key will be included in the result, otherwise it will be excluded
     */
    @NotNull
    SortedSet<E> from(@NotNull E value, boolean inclusive);

    /**
     * Returns the top of the set up until the key specified.
     *
     * @param inclusive if true, the key will be included in the result, otherwise it will be excluded
     */
    @NotNull
    SortedSet<E> to(@NotNull E value, boolean inclusive);

    /**
     * Returns a subset of the set between the {@code from} and {@code to} keys specified.
     *
     * @param fromInclusive if true, the key will be included in the result, otherwise it will be excluded
     * @param toInclusive   if true, the key will be included in the result, otherwise it will be excluded
     */
    @NotNull
    SortedSet<E> range(@NotNull E from, boolean fromInclusive, @NotNull E to, boolean toInclusive);

    /**
     * Returns an immutable view of this set as an instance of {@code java.util.SortedSet}.
     */
    @NotNull
    java.util.SortedSet<E> asSortedSet();
}
