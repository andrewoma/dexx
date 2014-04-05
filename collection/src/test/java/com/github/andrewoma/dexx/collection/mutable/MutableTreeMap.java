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

package com.github.andrewoma.dexx.collection.mutable;

import com.github.andrewoma.dexx.collection.Builder;
import com.github.andrewoma.dexx.collection.BuilderFactory;
import com.github.andrewoma.dexx.collection.Map;
import com.github.andrewoma.dexx.collection.Pair;
import com.github.andrewoma.dexx.collection.SortedMap;
import com.github.andrewoma.dexx.collection.internal.base.AbstractSortedMap;
import com.github.andrewoma.dexx.collection.internal.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeMap;

/**
 *
 */
public class MutableTreeMap<K, V> extends AbstractSortedMap<K, V> {
    private java.util.SortedMap<K, V> underlying = new TreeMap<K, V>();

    @NotNull
    public static <K, V> BuilderFactory<Pair<K, V>, Map<K, V>> factory() {
        return new BuilderFactory<Pair<K, V>, Map<K, V>>() {
            @NotNull
            @Override
            public Builder<Pair<K, V>, Map<K, V>> newBuilder() {
                return new MutableTreeMapBuilder<K, V>();
            }
        };
    }

    private MutableTreeMap(java.util.SortedMap<K, V> underlying) {
        this.underlying = underlying;
    }

    @NotNull
    @Override
    public SortedMap<K, V> put(@NotNull K key, V value) {
        underlying.put(key, value);
        return this;
    }

    @Nullable
    @Override
    public V get(@NotNull K key) {
        return underlying.get(key);
    }

    @NotNull
    @Override
    public SortedMap<K, V> remove(@NotNull K key) {
        underlying.remove(key);
        return this;
    }

    @Override
    public boolean containsKey(@NotNull K key) {
        return underlying.containsKey(key);
    }

    @Override
    public int size() {
        return underlying.size();
    }

    @NotNull
    @Override
    public SortedMap<K, V> range(@NotNull K from, boolean fromInclusive, @NotNull K to, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<? super K> comparator() {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public Pair<K, V> first() {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public Pair<K, V> last() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public SortedMap<K, V> drop(int number) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public SortedMap<K, V> take(int number) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Iterator<Pair<K, V>> iterator() {
        throw new UnsupportedOperationException();
    }

    static class MutableTreeMapBuilder<K, V> extends AbstractBuilder<Pair<K, V>, Map<K, V>> {
        private TreeMap<K, V> map = new TreeMap<K, V>();

        @NotNull
        @Override
        public Builder<Pair<K, V>, Map<K, V>> add(Pair<K, V> element) {
            map.put(element.component1(), element.component2());
            return this;
        }

        @NotNull
        @Override
        public SortedMap<K, V> doBuild() {
            return new MutableTreeMap<K, V>(map);
        }
    }
}
