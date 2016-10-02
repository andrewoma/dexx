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

import com.github.andrewoma.dexx.collection.internal.base.AbstractIndexedList;
import com.github.andrewoma.dexx.collection.internal.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@code ArrayList} is an {@code IndexedList} implementation backed by an array.
 * <p/>
 * <p><b>WARNING:</b> All modifications copy the entire backing array. {@code ArrayLists} should only be
 * used where modifications are infrequent and access times are critical. ArrayList is also compact
 * in memory usage, so may be appropriate for small lists. If there is any doubt regarding access patterns
 * for a {@code List} then use a {@link com.github.andrewoma.dexx.collection.Vector} instead.
 */
public class ArrayList<E> extends AbstractIndexedList<E> {
    private static final ArrayList<Object> EMPTY = new ArrayList<Object>();

    private final Object[] elements;

    @SuppressWarnings("unchecked")
    public static <E> ArrayList<E> empty() {
        return (ArrayList<E>) EMPTY;
    }

    @NotNull
    public static <E> BuilderFactory<E, ArrayList<E>> factory() {
        return new BuilderFactory<E, ArrayList<E>>() {
            @NotNull
            @Override
            public Builder<E, ArrayList<E>> newBuilder() {
                return new AbstractBuilder<E, ArrayList<E>>() {
                    private java.util.List<E> buffer = new java.util.ArrayList<E>();

                    @NotNull
                    @Override
                    public Builder<E, ArrayList<E>> add(E element) {
                        buffer.add(element);
                        return this;
                    }

                    @NotNull
                    @Override
                    public ArrayList<E> doBuild() {
                        return new ArrayList<E>(buffer.toArray(new Object[buffer.size()]));
                    }
                };
            }
        };
    }

    public ArrayList() {
        this(new Object[0]);
    }

    ArrayList(Object[] elements) {
        this.elements = elements;
    }

    @NotNull
    @Override
    public ArrayList<E> set(int i, E elem) {
        Object old = elements[i];
        if (old == elem) return this;
        int len = elements.length;
        Object[] newElements = Arrays.copyOf(elements, len);
        newElements[i] = elem;
        return new ArrayList<E>(newElements);
    }

    @NotNull
    @Override
    public ArrayList<E> append(E elem) {
        int len = elements.length;
        Object[] newElements = Arrays.copyOf(elements, len + 1);
        newElements[len] = elem;
        return new ArrayList<E>(newElements);
    }

    @NotNull
    @Override
    public ArrayList<E> prepend(E elem) {
        int len = elements.length;
        Object[] newElements = new Object[len + 1];
        System.arraycopy(elements, 0, newElements, 1, len);
        newElements[0] = elem;
        return new ArrayList<E>(newElements);
    }

    @NotNull
    @Override
    public ArrayList<E> drop(int number) {
        number = Math.max(number, 0);
        if (number >= elements.length) return empty();

        int len = elements.length - number;
        Object[] newElements = new Object[len];
        System.arraycopy(elements, number, newElements, 0, len);
        return new ArrayList<E>(newElements);
    }

    @NotNull
    @Override
    public ArrayList<E> take(int number) {
        number = Math.min(number, elements.length);
        number = Math.max(number, 0);

        if (number == 0) return empty();

        Object[] newElements = Arrays.copyOf(elements, number);
        return new ArrayList<E>(newElements);
    }

    @NotNull
    @Override
    public ArrayList<E> range(int from, boolean fromInclusive, int to, boolean toInclusive) {
        if (isEmpty()) return this;

        from = fromInclusive ? from : from + 1;
        from = Math.max(from, 0);
        from = Math.min(from, elements.length - 1);

        to = toInclusive ? to : to - 1;
        to = Math.max(to, 0);
        to = Math.min(to, elements.length - 1);

        // From and to are now an inclusive range within bounds
        if (to < from) return empty();

        int len = to - from + 1;
        Object[] newElements = Arrays.copyOf(elements, len);
        System.arraycopy(elements, from, newElements, 0, len);
        return new ArrayList<E>(newElements);
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int i) {
        return (E) elements[i];
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public E first() {
        return elements.length == 0 ? null : (E) elements[0];
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public E last() {
        return elements.length == 0 ? null : (E) elements[elements.length - 1];
    }

    @NotNull
    @Override
    public List<E> tail() {
        if (isEmpty()) return this;
        if (size() == 1) return empty();

        int len = elements.length - 1;
        Object[] newElements = new Object[len];
        System.arraycopy(elements, 1, newElements, 0, len);
        return new ArrayList<E>(newElements);
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int current = -1;

            @Override
            public boolean hasNext() {
                return current != ArrayList.this.elements.length - 1;
            }

            @Override
            public E next() {
                if (current == ArrayList.this.elements.length - 1) {
                    throw new NoSuchElementException();
                }
                current++;
                return (E) ArrayList.this.elements[current];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public int size() {
        return elements.length;
    }

    @Override
    public boolean isEmpty() {
        return elements.length == 0;
    }
}
