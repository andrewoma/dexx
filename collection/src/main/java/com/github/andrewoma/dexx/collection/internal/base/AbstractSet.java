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

import com.github.andrewoma.dexx.collection.Set;
import com.github.andrewoma.dexx.collection.internal.adapter.SetAdapater;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public abstract class AbstractSet<E> extends AbstractIterable<E> implements Set<E> {
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Set))
            return false;

        final Set other = (Set) o;
        if (other.size() != size())
            return false;

        for (E a : this) {
            try {
                if (!other.contains(a)) {
                    return false;
                }
            } catch (NullPointerException e) {
                return false;
            } catch (ClassCastException e) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (E e : this) {
            hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
        }

        return hashCode;
    }

    @Override
    @NotNull
    public java.util.Set<E> asSet() {
        return new SetAdapater<E>(this);
    }
}
