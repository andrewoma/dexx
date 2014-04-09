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

package com.github.andrewoma.dexx.collection.internal.adapter;

import com.github.andrewoma.dexx.collection.Pair;
import com.github.andrewoma.dexx.collection.SortedMap;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 *
 */
public class SortedMapAdapter<K, V> extends MapAdapter<K, V> implements java.util.SortedMap<K, V> {
    private SortedMap<K, V> map;

    public SortedMapAdapter(SortedMap<K, V> map) {
        super(map);
        this.map = map;
    }

    @SuppressWarnings("NullableProblems") // JetBrains annotation doesn't match the spec
    @Override
    public Comparator<? super K> comparator() {
        return map.comparator();
    }

    @NotNull
    @Override
    public java.util.SortedMap<K, V> subMap(@NotNull K fromKey, @NotNull K toKey) {
        return new SortedMapAdapter<K, V>(map.range(fromKey, true, toKey, false));
    }

    @NotNull
    @Override
    public java.util.SortedMap<K, V> headMap(@NotNull K toKey) {
        return new SortedMapAdapter<K, V>(map.to(toKey, false));
    }

    @NotNull
    @Override
    public java.util.SortedMap<K, V> tailMap(@NotNull K fromKey) {
        return new SortedMapAdapter<K, V>(map.from(fromKey, true));
    }

    @Override
    public K firstKey() {
        Pair<K, V> first = map.first();
        if (first == null) throw new NoSuchElementException("Empty map");
        return first.component1();
    }

    @Override
    public K lastKey() {
        Pair<K, V> last = map.last();
        if (last == null) throw new NoSuchElementException("Empty map");
        return last.component1();
    }
}
