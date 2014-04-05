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

package com.github.andrewoma.dexx.collection.internal.builder;

import com.github.andrewoma.dexx.collection.Builder;
import com.github.andrewoma.dexx.collection.Function;
import com.github.andrewoma.dexx.collection.Traversable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 *
 */
public abstract class AbstractBuilder<E, R> implements Builder<E, R> {
    private boolean built = false;

    @NotNull
    @Override
    public Builder<E, R> addAll(@NotNull Traversable<E> elements) {
        elements.forEach(new Function<E, Object>() {
            @Override
            public Object invoke(E element) {
                add(element);
                return null;
            }
        });
        return this;
    }

    @NotNull
    @Override
    public Builder<E, R> addAll(@NotNull Iterable<E> elements) {
        for (E element : elements) {
            add(element);
        }
        return this;
    }

    @NotNull
    @Override
    public Builder<E, R> addAll(@NotNull Iterator<E> iterator) {
        while (iterator.hasNext()) {
            add(iterator.next());
        }
        return this;
    }

    @NotNull
    @Override
    public Builder<E, R> addAll(E e1, E e2, E... es) {
        add(e1);
        add(e2);
        for (E e : es) {
            add(e);
        }
        return this;
    }

    @NotNull
    @Override
    final public R build() {
        if (built) throw new IllegalStateException("Builders do not support multiple calls to build()");
        built = true;
        return doBuild();
    }

    @NotNull
    public abstract R doBuild();
}
