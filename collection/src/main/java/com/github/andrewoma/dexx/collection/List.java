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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * {@code List} defines an sequence of elements where the order is preserved.
 * <p/>
 * <p>There are two sub-interfaces of {@code List} that define very different performance characteristics:
 * <ul>
 * <li>{@link IndexedList}: Guarantees fast random access to elements in the {@code List} via indexes.
 * <li>{@link LinkedList}: Guarantees fast {@link #prepend(Object)} and {@link #tail()} operations.
 * </ul>
 * <p/>
 * <p>The performance of other operations is unspecified - care must be taken to use specific implementations
 * using the appropriate access patterns.
 */
public interface List<E> extends Iterable<E> {
    /**
     * Returns the element at the specified index in this list (zero-based).
     *
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    E get(int i);

    /**
     * Returns a list with the element set to the value specified at the index (zero-based).
     *
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    @NotNull
    List<E> set(int i, E elem);

    /**
     * Returns a list with the specified element appended to the bottom of the list.
     */
    @NotNull
    List<E> append(E elem);

    /**
     * Returns a list with the specified element prepended to the top of the list.
     */
    @NotNull
    List<E> prepend(E elem);

    /**
     * Returns the index of the first occurrence of the specified element in the list or -1 if there are no occurrences.
     */
    int indexOf(E elem);

    /**
     * Returns the index of the last occurrence of the specified element in the list or -1 if there are no occurrences.
     */
    int lastIndexOf(E elem);

    /**
     * Returns first element in the list or {@code null} if the list is empty.
     */
    @Nullable
    E first();

    /**
     * Returns last element in the list or {@code null} if the list is empty.
     */
    @Nullable
    E last();

    /**
     * Returns a list containing all elements in the list, excluding the first element. An empty list is
     * returned if the list is empty.
     */
    @NotNull
    List<E> tail();

    /**
     * Returns a list containing all elements in this list, excluding the first {@code number} of elements.
     */
    @NotNull
    List<E> drop(int number);

    /**
     * Returns a list containing the first {@code number} of elements from this list.
     */
    @NotNull
    List<E> take(int number);

    /**
     * Returns a list containing a contiguous range of elements from this list.
     *
     * @param from          starting index for the range (zero-based)
     * @param fromInclusive if true, the element at the {@code from} index will be included
     * @param to            end index for the range (zero-based)
     * @param toInclusive   if true, the element at the {@code to} index will be included
     */
    @NotNull
    List<E> range(int from, boolean fromInclusive, int to, boolean toInclusive);

    /**
     * Returns an immutable view of this list as an instance of {@code java.util.List}.
     */
    @NotNull
    java.util.List<E> asList();
}
