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
import com.github.andrewoma.dexx.collection.IndexedList;
import com.github.andrewoma.dexx.collection.List;
import com.github.andrewoma.dexx.collection.internal.base.AbstractIndexedList;
import com.github.andrewoma.dexx.collection.internal.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 */
public class MutableArrayList<E> extends AbstractIndexedList<E> {
    private java.util.List<E> underlying = new ArrayList<E>();

    @NotNull
    public static <A> BuilderFactory<A, IndexedList<A>> factory() {
        return new BuilderFactory<A, IndexedList<A>>() {
            @NotNull
            @Override
            public Builder<A, IndexedList<A>> newBuilder() {
                return new MutableArrayListBuilder<A>();
            }
        };
    }

    private MutableArrayList(java.util.List<E> underlying) {
        this.underlying = underlying;
    }

    @NotNull
    @Override
    public IndexedList<E> set(int i, E elem) {
        underlying.set(i, elem);
        return this;
    }

    @NotNull
    @Override
    public IndexedList<E> append(E elem) {
        underlying.add(elem);
        return this;
    }

    @NotNull
    @Override
    public IndexedList<E> prepend(E elem) {
        underlying.add(0, elem);
        return this;
    }

    @NotNull
    @Override
    public IndexedList<E> drop(int number) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public IndexedList<E> take(int number) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public IndexedList<E> range(int from, boolean fromInclusive, int to, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }

    @Override
    public E get(int i) {
        return underlying.get(i);
    }

    @Nullable
    @Override
    public E first() {
        throw new UnsupportedOperationException();
    }

    @Nullable
    @Override
    public E last() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public List<E> tail() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return underlying.iterator();
    }

    static class MutableArrayListBuilder<A> extends AbstractBuilder<A, IndexedList<A>> implements Builder<A, IndexedList<A>> {
        private java.util.List<A> result = new ArrayList<A>();

        @NotNull
        @Override
        public Builder<A, IndexedList<A>> add(A element) {
            result.add(element);
            return this;
        }

        @NotNull
        @Override
        public IndexedList<A> doBuild() {
            return new MutableArrayList<A>(result);
        }
    }

    @Override
    public int size() {
        return underlying.size();
    }
}
