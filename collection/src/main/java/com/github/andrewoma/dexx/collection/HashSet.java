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

import com.github.andrewoma.dexx.collection.internal.base.AbstractSet;
import com.github.andrewoma.dexx.collection.internal.base.Iterables;
import com.github.andrewoma.dexx.collection.internal.builder.AbstractSelfBuilder;
import com.github.andrewoma.dexx.collection.internal.hashmap.CompactHashMap;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * {@code HashSet} is an implementation of {@code Set} backed by a {@code HashMap}.
 */
public class HashSet<E> extends AbstractSet<E> {
    private static final HashSet EMPTY = new HashSet();

    private static final KeyFunction keyFunction = new KeyFunction<Object, Object>() {
        @NotNull
        public Object key(@NotNull Object value) {
            return value;
        }
    };

    @NotNull
    public static <E> BuilderFactory<E, HashSet<E>> factory() {
        return new BuilderFactory<E, HashSet<E>>() {
            @NotNull
            @Override
            public Builder<E, HashSet<E>> newBuilder() {
                return new AbstractSelfBuilder<E, HashSet<E>>(HashSet.<E>empty()) {
                    @NotNull
                    @Override
                    public Builder<E, HashSet<E>> add(E element) {
                        result = result.add(element);
                        return this;
                    }
                };
            }
        };
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E> HashSet<E> empty() {
        return EMPTY;
    }

    private final CompactHashMap<E, E> compactHashMap;

    private HashSet() {
        this(CompactHashMap.<E, E>empty());
    }

    private HashSet(CompactHashMap<E, E> compactHashMap) {
        this.compactHashMap = compactHashMap;
    }

    @SuppressWarnings("unchecked")
    private KeyFunction<E, E> keyFunction() {
        return keyFunction;
    }

    @NotNull
    @Override
    public HashSet<E> add(E value) {
        return new HashSet<E>(compactHashMap.put(value, value, keyFunction()));
    }

    @NotNull
    @Override
    public HashSet<E> remove(E value) {
        return new HashSet<E>(compactHashMap.remove(value, keyFunction()));
    }

    @Override
    public boolean contains(E value) {
        return compactHashMap.get(value, keyFunction()) != null;
    }

    @Override
    public int size() {
        return compactHashMap.size();
    }

    @Override
    public <U> void forEach(@NotNull Function<E, U> f) {
        Iterables.forEach(this, f);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        final Iterator<Pair<E, E>> iterator = compactHashMap.iterator(keyFunction());
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next().component1();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}