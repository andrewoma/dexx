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

/**
 * {@code Map} defines the interface for maps that associate keys with values.
 */
@SuppressWarnings("NullableProblems")
public interface Map<K, V> extends Iterable<Pair<K, V>> {
    /**
     * Returns a map with the value specified associated to the key specified.
     * <p>If value already exists for the key, it will be replaced.
     */
    @NotNull
    Map<K, V> put(@NotNull K key, V value);

    /**
     * Returns the value associated with the key or {@code null} if the no value exists with the key specified.
     */
    @Nullable
    V get(@NotNull K key);

    /**
     * Returns a map with the value associated with the key removed if it exists.
     */
    @NotNull
    Map<K, V> remove(@NotNull K key);

    /**
     * Returns the keys for this map.
     */
    @NotNull
    Iterable<K> keys();

    /**
     * Returns the values for this map.
     */
    @NotNull
    Iterable<V> values();

    /**
     * Returns true if this map contains the specified key.
     */
    boolean containsKey(@NotNull K key);

    /**
     * Returns an immutable view of this map as an instance of {@link java.util.Map}.
     */
    @NotNull
    java.util.Map<K, V> asMap();
}
