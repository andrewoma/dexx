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

import com.github.andrewoma.dexx.collection.Builder;
import com.github.andrewoma.dexx.collection.Function;
import com.github.andrewoma.dexx.collection.HashSet;
import com.github.andrewoma.dexx.collection.IndexedList;
import com.github.andrewoma.dexx.collection.Set;
import com.github.andrewoma.dexx.collection.SortedSet;
import com.github.andrewoma.dexx.collection.Traversable;
import com.github.andrewoma.dexx.collection.TreeSet;
import com.github.andrewoma.dexx.collection.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public abstract class AbstractTraversable<E> implements Traversable<E> {
    @Override
    public int size() {
        final int[] result = {0};
        forEach(new Function<E, Object>() {
            @Override
            public Object invoke(E parameter) {
                return result[0]++;
            }
        });
        return result[0];
    }

    @NotNull
    public String makeString(@NotNull String separator) {
        return makeString(separator, "", "", -1, "");
    }

    @NotNull
    public String makeString(@NotNull final String separator, @NotNull String prefix, @NotNull String postfix, final int limit, @NotNull String truncated) {
        final StringBuilder buffer = new StringBuilder(prefix);
        final int[] count = {0};

        try {
            forEach(new Function<E, Object>() {
                @Override
                public Object invoke(E element) {
                    int current = count[0] + 1;
                    count[0] = current;

                    if (current > 1) buffer.append(separator);

                    if (limit < 0 || current <= limit) {
                        buffer.append(element == null ? "null" : element.toString());
                    } else throw Break.instance;

                    return null;
                }
            });
        } catch (Break e) {
            // Fall through
        }

        if (limit >= 0 && count[0] > limit) buffer.append(truncated);
        buffer.append(postfix);

        return buffer.toString();
    }

    @Override
    public String toString() {
        return makeString(", ", getClass().getSimpleName() + "(", ")", 100, "...");
    }


    @NotNull
    @Override
    public <R extends Traversable<E>> R to(@NotNull Builder<E, R> builder) {
        return builder.addAll(this).build();
    }

    @NotNull
    @Override
    public Set<E> toSet() {
        return to(HashSet.<E>factory().newBuilder());
    }

    @NotNull
    @Override
    public SortedSet<E> toSortedSet(Comparator<? super E> comparator) {
        //noinspection RedundantTypeArguments
        return to(TreeSet.<E>factory(comparator).newBuilder());
    }

    @NotNull
    @Override
    public IndexedList<E> toIndexedList() {
        return to(Vector.<E>factory().newBuilder());
    }

    @Override
    public boolean isEmpty() {
        try {
            forEach(new Function<E, Object>() {
                @Override
                public Object invoke(E parameter) {
                    throw Break.instance;
                }
            });
        } catch (Break e) {
            return false;
        }

        return true;
    }

    @NotNull
    @Override
    public Object[] toArray() {
        final Object[] result = new Object[size()];
        final int[] count = new int[]{0};
        forEach(new Function<E, Object>() {
            @Override
            public Object invoke(E parameter) {
                result[count[0]] = parameter;
                count[0] = count[0] + 1;
                return null;
            }
        });

        return result;
    }

    @NotNull
    @Override
    public E[] toArray(E[] array) {
        // Estimate size of array; be prepared to see more or fewer elements
        int size = size();
        @SuppressWarnings("unchecked")
        final E[] result = array.length >= size ? array : (E[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);

        final int[] count = new int[]{0};
        forEach(new Function<E, Object>() {
            @Override
            public Object invoke(E parameter) {
                result[count[0]] = parameter;
                count[0] = count[0] + 1;
                return null;
            }
        });

        return result;
    }
}
