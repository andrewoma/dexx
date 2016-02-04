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

import java.util.Comparator;

/**
 * {@code Traversable} is the root of the collection hierarchy.
 * <p/>
 * <p>{@code Traversable} allows collections to be defined by a {@link #forEach(Function)} method and without an
 * iterator. {@link #forEach(Function)} can be a lot simpler to implement for tree-like structure and also offers
 * a more direct method of traversal for such structures.
 */
public interface Traversable<E> {
    /**
     * All collection methods can be built upon this {@code forEach} definition.
     */
    @SuppressWarnings("NullableProblems")
    <U> void forEach(@NotNull Function<E, U> f);

    /**
     * Returns the size of the collection.
     * <p/>
     * <p>Warning: infinite collections are possible, as are collections that require traversal to calculate the
     * size.
     */
    int size();

    /**
     * Returns true if this collection is empty.
     */
    boolean isEmpty();

    /**
     * Returns this collection converted to a string by joining elements together with the specified {@code separator}.
     */
    @NotNull
    String makeString(@NotNull String separator);

    /**
     * Returns this collection converted to a string.
     *
     * @param separator Specifies the joining character
     * @param prefix    Specifies a prefix to the string
     * @param postfix   Species a postfix to the string
     * @param limit     Specifies the maximum number of elements to join. If the limit is exceeded, additional elements are ignored.
     * @param truncated If the limit is reached, the {@code truncated} value will be appended to indicate the limit was reached.
     */
    @NotNull
    String makeString(@NotNull String separator, @NotNull String prefix, @NotNull String postfix, int limit, @NotNull String truncated);

    /**
     * Converts this collection to another collection using a builder.
     */
    @NotNull
    <R extends Traversable<E>> R to(@NotNull Builder<E, R> builder);

    /**
     * Converts this collection to a set.
     */
    @NotNull
    Set<E> toSet();

    /**
     * Converts this collection to a sorted set.
     */
    @NotNull
    SortedSet<E> toSortedSet(Comparator<? super E> comparator);

    /**
     * Converts this collection to an indexed list.
     */
    @NotNull
    IndexedList<E> toIndexedList();

    /**
     * Converts this collection to an array of objects.
     */
    @NotNull
    Object[] toArray();

    /**
     * Converts this collection to an array of objects of the correct type.
     */
    @NotNull
    E[] toArray(E[] array);
}
