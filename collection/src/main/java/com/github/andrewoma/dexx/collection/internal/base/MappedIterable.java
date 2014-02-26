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

package com.github.andrewoma.dexx.collection.internal.base;

import com.github.andrewoma.dexx.collection.Function;
import com.github.andrewoma.dexx.collection.Iterable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class MappedIterable<T, F> extends AbstractIterable<T> {
    private final Function<F, T> mapFunction;
    private final com.github.andrewoma.dexx.collection.Iterable<F> from;

    public MappedIterable(Iterable<F> from, Function<F, T> mapFunction) {
        this.mapFunction = mapFunction;
        this.from = from;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        final Iterator<F> iterator = from.iterator();
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public T next() {
                return mapFunction.invoke(iterator.next());
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }
}



