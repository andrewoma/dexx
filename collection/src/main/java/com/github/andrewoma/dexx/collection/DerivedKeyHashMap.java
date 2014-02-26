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

import java.util.Iterator;

public class DerivedKeyHashMap<K, V> extends AbstractMap<K, V> {
    private final KeyFunction<K, V> keyFunction;
    private CompactHashMap<K, V> compactHashMap = CompactHashMap.empty();

    @NotNull
    public static <K, V> BuilderFactory<Pair<K, V>, DerivedKeyHashMap<K, V>> factory(final KeyFunction<K, V> keyFunction) {
        return new BuilderFactory<Pair<K, V>, DerivedKeyHashMap<K, V>>() {
            @NotNull
            @Override
            public Builder<Pair<K, V>, DerivedKeyHashMap<K, V>> newBuilder() {
                return new AbstractSelfBuilder<Pair<K, V>, DerivedKeyHashMap<K, V>>(new DerivedKeyHashMap<K, V>(keyFunction)) {
                    @NotNull
                    @Override
                    public Builder<Pair<K, V>, DerivedKeyHashMap<K, V>> add(Pair<K, V> element) {
                        result = result.put(element.component1(), element.component2());
                        return this;
                    }
                };
            }
        };
    }

    public DerivedKeyHashMap(@NotNull KeyFunction<K, V> keyFunction) {
        this.keyFunction = keyFunction;
    }

    private DerivedKeyHashMap(KeyFunction<K, V> keyFunction, CompactHashMap<K, V> compactHashMap) {
        this.keyFunction = keyFunction;
        this.compactHashMap = compactHashMap;
    }

    @Override
    public boolean containsKey(@NotNull K key) {
        return get(key) != null;
    }

    @NotNull
    public DerivedKeyHashMap<K, V> put(@NotNull K key, V value) {
        return new DerivedKeyHashMap<K, V>(keyFunction, compactHashMap.put(key, value, keyFunction));
    }

    public V get(@NotNull K key) {
        return compactHashMap.get(key, keyFunction);
    }

    @NotNull
    public DerivedKeyHashMap<K, V> remove(@NotNull K key) {
        return new DerivedKeyHashMap<K, V>(keyFunction, compactHashMap.remove(key, keyFunction));
    }

    public int size() {
        return compactHashMap.size();
    }

    public <U> void forEach(@NotNull final Function<Pair<K, V>, U> f) {
        compactHashMap.forEach(new Function<Pair<K, V>, Object>() {
            @Override
            public Object invoke(Pair<K, V> parameter) {
                f.invoke(parameter);
                return null;
            }
        }, keyFunction);
    }

    @NotNull
    public Iterator<Pair<K, V>> iterator() {
        return compactHashMap.iterator(keyFunction);
    }
}
