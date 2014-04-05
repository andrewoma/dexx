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
import com.github.andrewoma.dexx.collection.SortedSet;
import com.github.andrewoma.dexx.collection.internal.base.AbstractSortedSet;
import com.github.andrewoma.dexx.collection.internal.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

/**
 *
 */
public class MutableTreeSet<A> extends AbstractSortedSet<A> {
    private java.util.SortedSet<A> underlying = new TreeSet<A>();

    @NotNull
    public static <A> BuilderFactory<A, SortedSet<A>> factory(final Comparator<? super A> comparator) {
        return new BuilderFactory<A, SortedSet<A>>() {
            @NotNull
            @Override
            public Builder<A, SortedSet<A>> newBuilder() {
                return new MutableTreeSetBuilder<A>(comparator);
            }
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Comparator<? super A> comparator() {
        return underlying.comparator();
    }

    private MutableTreeSet(java.util.SortedSet<A> underlying) {
        this.underlying = underlying;
    }

    @Nullable
    @Override
    public A first() {
        try {
            return underlying.first();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Nullable
    @Override
    public A last() {
        try {
            return underlying.last();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @NotNull
    @Override
    public SortedSet<A> add(A value) {
        underlying.add(value);
        return this;
    }

    @NotNull
    @Override
    public SortedSet<A> remove(A value) {
        underlying.remove(value);
        return this;
    }

    @Override
    public boolean contains(A value) {
        return underlying.contains(value);
    }

    @NotNull
    @Override
    public Iterator<A> iterator() {
        return underlying.iterator();
    }

    @NotNull
    @Override
    public SortedSet<A> drop(int number) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public SortedSet<A> take(int number) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return underlying.size();
    }

    static class MutableTreeSetBuilder<A> extends AbstractBuilder<A, SortedSet<A>> implements Builder<A, SortedSet<A>> {
        private java.util.SortedSet<A> result;

        MutableTreeSetBuilder(Comparator<? super A> comparator) {
            result = new java.util.TreeSet<A>(comparator);
        }

        @NotNull
        @Override
        public Builder<A, SortedSet<A>> add(A element) {
            result.add(element);
            return this;
        }

        @NotNull
        @Override
        public SortedSet<A> doBuild() {
            return new MutableTreeSet<A>(result);
        }
    }

    @NotNull
    @Override
    public SortedSet<A> range(@NotNull A from, boolean fromInclusive, @NotNull A to, boolean toInclusive) {
        throw new UnsupportedOperationException();
    }
}
