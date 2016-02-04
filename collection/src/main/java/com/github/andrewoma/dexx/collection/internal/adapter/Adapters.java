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

import com.github.andrewoma.dexx.collection.Function;
import com.github.andrewoma.dexx.collection.Traversable;
import com.github.andrewoma.dexx.collection.internal.base.Break;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 *
 */
public class Adapters {
    public static <E> boolean containsAll(@NotNull Traversable<E> traversable, @NotNull Collection<?> c) {
        for (Object e : c) {
            if (!contains(traversable, e)) {
                return false;
            }
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    public static <E> boolean contains(@NotNull Traversable<E> traversable, final Object o) {
        try {
            traversable.forEach(new Function<E, Object>() {
                @Override
                public Object invoke(E e) {
                    if (e.equals(o)) {
                        throw Break.instance;
                    }
                    return null;
                }
            });
            return false;
        } catch (Break e) {
            return true;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T, E> T[] toArray(@NotNull Traversable<E> traversable, T[] a) {
        // Estimate size of array; be prepared to see more or fewer elements
        int size = traversable.size();
        final T[] result = a.length >= size ? a : (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        final int[] count = new int[]{0};

        traversable.forEach(new Function<E, Object>() {
            @Override
            public Object invoke(E e) {
                result[count[0]] = (T) e;
                count[0] = count[0] + 1;
                return null;
            }
        });

        return result;
    }
}
