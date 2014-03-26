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

/**
 * Set defines the interface for a unique set of values as defined by {@link java.lang.Object#equals(Object)}.
 */
public interface Set<E> extends Iterable<E> {
    /**
     * Returns a set that adds the specified value if it doesn't already exist in this set.
     */
    @NotNull
    Set<E> add(E value);

    /**
     * Removes the specified value from the set if it exists.
     */
    @NotNull
    Set<E> remove(E value);

    /**
     * Returns true if the value exists in this set.
     */
    boolean contains(E value);

    /**
     * Returns an immutable view of this set as an instance of {@code java.util.Set}.
     */
    @NotNull
    java.util.Set<E> asSet();
}
