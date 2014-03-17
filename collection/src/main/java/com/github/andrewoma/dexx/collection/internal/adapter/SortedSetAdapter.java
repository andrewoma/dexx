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

package com.github.andrewoma.dexx.collection.internal.adapter;

import com.github.andrewoma.dexx.collection.SortedSet;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 *
 */
public class SortedSetAdapter<E> extends SetAdapater<E> implements java.util.SortedSet<E> {
    private SortedSet<E> set;

    public SortedSetAdapter(SortedSet<E> set) {
        super(set);
        this.set = set;
    }

    @NotNull
    @Override
    public Comparator<? super E> comparator() {
        return set.comparator();
    }

    @NotNull
    @Override
    public java.util.SortedSet<E> subSet(E fromElement, E toElement) {
        return new SortedSetAdapter<E>(set.range(fromElement, true, toElement, false));
    }

    @NotNull
    @Override
    public java.util.SortedSet<E> headSet(E toElement) {
        return new SortedSetAdapter<E>(set.to(toElement, false));
    }

    @NotNull
    @Override
    public java.util.SortedSet<E> tailSet(E fromElement) {
        return new SortedSetAdapter<E>(set.from(fromElement, true));
    }

    @Override
    public E first() {
        if (set.isEmpty()) throw new NoSuchElementException("Empty set");
        return set.first();
    }

    @Override
    public E last() {
        if (set.isEmpty()) throw new NoSuchElementException("Empty set");
        return set.last();
    }
}
