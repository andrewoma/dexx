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
import com.github.andrewoma.dexx.collection.Set;
import com.github.andrewoma.dexx.collection.internal.base.AbstractSet;
import com.github.andrewoma.dexx.collection.internal.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Iterator;

/**
 *
 */
public class MutableHashSet<A> extends AbstractSet<A> {
    private java.util.Set<A> underlying = new HashSet<A>();

    @NotNull
    public static <A> BuilderFactory<A, Set<A>> factory() {
        return new BuilderFactory<A, Set<A>>() {
            @NotNull
            @Override
            public Builder<A, Set<A>> newBuilder() {
                return new MutableHashSetBuilder<A>();
            }
        };
    }

    private MutableHashSet(java.util.Set<A> underlying) {
        this.underlying = underlying;
    }

    @NotNull
    @Override
    public Set<A> add(A value) {
        underlying.add(value);
        return this;
    }

    @NotNull
    @Override
    public Set<A> remove(A value) {
        underlying.remove(value);
        return this;
    }

    @Override
    public int size() {
        return underlying.size();
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

    static class MutableHashSetBuilder<A> extends AbstractBuilder<A, Set<A>> implements Builder<A, Set<A>> {
        private java.util.Set<A> result = new HashSet<A>();

        @NotNull
        @Override
        public Builder<A, Set<A>> add(A element) {
            result.add(element);
            return this;
        }

        @NotNull
        @Override
        public Set<A> doBuild() {
            return new MutableHashSet<A>(result);
        }
    }
}
