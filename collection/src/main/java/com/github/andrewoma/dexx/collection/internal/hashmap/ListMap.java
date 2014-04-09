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

/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2003-2013, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package com.github.andrewoma.dexx.collection.internal.hashmap;

import com.github.andrewoma.dexx.collection.Builder;
import com.github.andrewoma.dexx.collection.BuilderFactory;
import com.github.andrewoma.dexx.collection.Function;
import com.github.andrewoma.dexx.collection.Pair;
import com.github.andrewoma.dexx.collection.internal.base.AbstractMap;
import com.github.andrewoma.dexx.collection.internal.builder.AbstractSelfBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 */
public class ListMap<K, V> extends AbstractMap<K, V> {
    private static final ListMap<Object, Object> EMPTY = new ListMap<Object, Object>();

    @NotNull
    public static <K, V> BuilderFactory<Pair<K, V>, ListMap<K, V>> factory() {
        return new BuilderFactory<Pair<K, V>, ListMap<K, V>>() {
            @NotNull
            @Override
            public Builder<Pair<K, V>, ListMap<K, V>> newBuilder() {
                return new AbstractSelfBuilder<Pair<K, V>, ListMap<K, V>>(ListMap.<K, V>empty()) {
                    @NotNull
                    @Override
                    public Builder<Pair<K, V>, ListMap<K, V>> add(Pair<K, V> element) {
                        result = result.put(element.component1(), element.component2());
                        return this;
                    }
                };
            }
        };
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <K, V> ListMap<K, V> empty() {
        return (ListMap<K, V>) EMPTY;
    }

    public int size() {
        return 0;
    }

    public V get(@NotNull K key) {
        return null;
    }

    @NotNull
    public ListMap<K, V> put(@NotNull K key, V value) {
        return new Node<K, V>(key, value);
    }

    @NotNull
    public ListMap<K, V> remove(@NotNull K key) {
        return this;
    }

    public <U> void forEach(@NotNull Function<Pair<K, V>, U> f) {
        for (Pair<K, V> pair : this) {
            f.invoke(pair);
        }
    }

    @NotNull
    public Iterator<Pair<K, V>> iterator() {
        return new ListMapIterator<K, V>(ListMap.this);
    }

    protected K getKey() {
        throw new NoSuchElementException("empty map");
    }

    protected V getValue() {
        throw new NoSuchElementException("empty map");
    }

    public ListMap<K, V> tail() {
        throw new NoSuchElementException("empty map");
    }

    public boolean isEmpty() {
        return true;
    }

    protected Node<K, V> createNode(K key, V value) {
        return new Node<K, V>(key, value);
    }

    public boolean containsKey(@NotNull K key) {
        return false;
    }

    // Note: this is an inner class that builds a list by using the pointer to the outer class
    @SuppressWarnings("TypeParameterHidesVisibleType")
    class Node<K, V> extends ListMap<K, V> {
        private final K key;
        private final V value;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int size() {
            return size0(this, 0);
        }

        // TODO ... remove recursion
        private int size0(ListMap<K, V> cur, int acc) {
            return cur.isEmpty() ? acc : size0(cur.tail(), acc + 1);
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public V get(@NotNull K key) {
            ListMap<K, V> pairs = apply0(this, key);
            return pairs == null ? null : pairs.getValue();
        }

        private ListMap<K, V> apply0(ListMap<K, V> cur, K key) {
            return cur.isEmpty() ? null : (key.equals(cur.getKey()) ? cur : apply0(cur.tail(), key));
        }

        @Override
        @SuppressWarnings("unchecked")
        public ListMap<K, V> tail() {
            return (ListMap<K, V>) ListMap.this;
        }

        @NotNull
        @Override
        public ListMap<K, V> put(@NotNull K key, V value) {
            ListMap<K, V> m = containsKey(key) ? this.remove(key) : this;
            return m.createNode(key, value);
        }

        @NotNull
        @Override
        public ListMap<K, V> remove(@NotNull K key) {
            ListMap<K, V> acc = ListMap.empty();
            for (Pair<K, V> pair : this) {
                if (!key.equals(pair.component1())) {
                    acc = acc.put(pair.component1(), pair.component2());
                }
            }
            return acc;
        }

        @Override
        protected K getKey() {
            return key;
        }

        @Override
        protected V getValue() {
            return value;
        }

        @Override
        public boolean containsKey(@NotNull K key) {
            return apply0(this, key) != null;
        }
    }
}

/**
 * Note: this currently iterates in reverse order
 */
class ListMapIterator<K, V> implements Iterator<Pair<K, V>> {
    private ListMap<K, V> listMap;

    ListMapIterator(ListMap<K, V> listMap) {
        this.listMap = listMap;
    }

    public boolean hasNext() {
        return !listMap.isEmpty();
    }

    public Pair<K, V> next() {
        if (!hasNext()) {
            throw new NoSuchElementException("next on empty iterator");
        }

        Pair<K, V> result = new Pair<K, V>(listMap.getKey(), listMap.getValue());
        listMap = listMap.tail();

        return result;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}
