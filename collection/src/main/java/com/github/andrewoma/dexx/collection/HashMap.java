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

import com.github.andrewoma.dexx.collection.internal.base.AbstractMap;
import com.github.andrewoma.dexx.collection.internal.builder.AbstractSelfBuilder;
import com.github.andrewoma.dexx.collection.internal.hashmap.CompactHashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 * {@code HashMap} is an implementation of {@code Map} based on a hash trie.
 * <p/>
 * <p>The underlying implementation is a port of Scala's HashMap which is an implementation of a
 * <a href="http://en.wikipedia.org/wiki/Hash_array_mapped_trie">hash array mapped trie.</a>
 */
public class HashMap<K, V> extends AbstractMap<K, V> {
    private static final HashMap EMPTY = new HashMap();

    private static final KeyFunction keyFunction = new KeyFunction<Object, Pair>() {
        @NotNull
        public Object key(@NotNull Pair value) {
            return value.component1();
        }
    };

    @SuppressWarnings("unchecked")
    @NotNull
    public static <K, V> HashMap<K, V> empty() {
        return EMPTY;
    }

    @NotNull
    public static <K, V> BuilderFactory<Pair<K, V>, HashMap<K, V>> factory() {
        return new BuilderFactory<Pair<K, V>, HashMap<K, V>>() {
            @NotNull
            @Override
            public Builder<Pair<K, V>, HashMap<K, V>> newBuilder() {
                return new AbstractSelfBuilder<Pair<K, V>, HashMap<K, V>>(HashMap.<K, V>empty()) {
                    @NotNull
                    @Override
                    public Builder<Pair<K, V>, HashMap<K, V>> add(Pair<K, V> element) {
                        result = result.put(element.component1(), element.component2());
                        return this;
                    }
                };
            }
        };
    }

    private final CompactHashMap<K, Pair<K, V>> compactHashMap;

    @SuppressWarnings("unchecked")
    private KeyFunction<K, Pair<K, V>> keyFunction() {
        return keyFunction;
    }

    @Override
    public boolean containsKey(@NotNull K key) {
        return compactHashMap.get(key, keyFunction()) != null;
    }

    public HashMap() {
        this(CompactHashMap.<K, Pair<K, V>>empty());
    }

    private HashMap(CompactHashMap<K, Pair<K, V>> compactHashMap) {
        this.compactHashMap = compactHashMap;
    }

    @NotNull
    @Override
    public HashMap<K, V> put(@NotNull K key, V value) {
        return new HashMap<K, V>(compactHashMap.put(key, new Pair<K, V>(key, value), keyFunction()));
    }

    @Nullable
    @Override
    public V get(@NotNull K key) {
        Pair<K, V> pair = compactHashMap.get(key, keyFunction());
        return pair == null ? null : pair.component2();
    }

    @NotNull
    @Override
    public HashMap<K, V> remove(@NotNull K key) {
        return new HashMap<K, V>(compactHashMap.remove(key, keyFunction()));
    }

    @Override
    public int size() {
        return compactHashMap.size();
    }

    @Override
    public <U> void forEach(@NotNull final Function<Pair<K, V>, U> f) {
        compactHashMap.forEach(new Function<Pair<K, Pair<K, V>>, Object>() {
            @Override
            public Object invoke(Pair<K, Pair<K, V>> pair) {
                f.invoke(pair.component2());
                return null;
            }
        }, keyFunction());
    }

    @NotNull
    @Override
    public Iterator<Pair<K, V>> iterator() {
        return new Itr<K, V>(compactHashMap.iterator(keyFunction()));
    }

    static class Itr<K, V> implements Iterator<Pair<K, V>> {
        final Iterator<Pair<K, Pair<K, V>>> iterator;

        public Itr(Iterator<Pair<K, Pair<K, V>>> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Pair<K, V> next() {
            Pair<K, Pair<K, V>> next = iterator.next();
            return next.component2();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
