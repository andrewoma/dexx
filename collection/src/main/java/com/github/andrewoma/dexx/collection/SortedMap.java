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
 * SortedMap defines the interface for maps that are sorted by their key.
 */
public interface SortedMap<K, V> extends Map<K, V> {
    @NotNull
    SortedMap<K, V> put(@NotNull K key, V value);

    @NotNull
    SortedMap<K, V> remove(@NotNull K key);

    /**
     * Returns the bottom of the map starting from the key specified.
     *
     * @param inclusive if true, the key will be included in the result, otherwise it will be excluded
     */
    @NotNull
    SortedMap<K, V> from(@NotNull K key, boolean inclusive);

    /**
     * Returns the top of the map up until the key specified.
     *
     * @param inclusive if true, the key will be included in the result, otherwise it will be excluded
     */
    @NotNull
    SortedMap<K, V> to(@NotNull K key, boolean inclusive);

    /**
     * Returns a subset of the map between the {@code from} and {@code to} keys specified.
     *
     * @param fromInclusive if true, the key will be included in the result, otherwise it will be excluded
     * @param toInclusive   if true, the key will be included in the result, otherwise it will be excluded
     */
    @NotNull
    SortedMap<K, V> range(@NotNull K from, boolean fromInclusive, @NotNull K to, boolean toInclusive);

    /**
     * Returns the comparator associated with this map, or {@code null} if the default ordering is used.
     */
    Comparator<? super K> comparator();

    /**
     * Returns the first entry in the map or {@code null} if the map is empty.
     */
    @Nullable
    Pair<K, V> first();

    /**
     * Returns the last entry in the map or {@code null} if the map is empty.
     */
    @Nullable
    Pair<K, V> last();

    /**
     * Returns a map containing all elements in this map, excluding the first {@code number} of elements.
     */
    @NotNull
    SortedMap<K, V> drop(int number);

    /**
     * Returns a list containing the first {@code number} of elements from this list.
     */
    @NotNull
    SortedMap<K, V> take(int number);

    /**
     * Returns an immutable view of this map as an instance of {@code java.util.SortedMap}.
     */
    @NotNull
    java.util.SortedMap<K, V> asSortedMap();
}
