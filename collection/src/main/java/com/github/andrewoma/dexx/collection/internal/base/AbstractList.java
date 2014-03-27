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


import com.github.andrewoma.dexx.collection.List;
import com.github.andrewoma.dexx.collection.internal.adapter.ListAdapater;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public abstract class AbstractList<E> extends AbstractIterable<E> implements List<E> {

    @Override
    public int indexOf(E elem) {
        int index = 0;
        for (E e : this) {
            if (elem == null ? e == null : elem.equals(e)) {
                return index;
            }
            index++;
        }

        return -1;
    }

    @Override
    public int lastIndexOf(E elem) {
        int index = 0;
        int lastIndex = -1;
        for (E e : this) {
            if (elem == null ? e == null : elem.equals(e)) {
                lastIndex = index;
            }
            index++;
        }

        return lastIndex;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;

        if (!(other instanceof List))
            return false;

        Iterator<E> iterator = iterator();
        Iterator otherIterator = ((List) other).iterator();

        while (iterator.hasNext() && otherIterator.hasNext()) {
            E elem = iterator.next();
            Object otherElem = otherIterator.next();
            if (!(elem == null ? otherElem == null : elem.equals(otherElem)))
                return false;
        }

        return !(iterator.hasNext() || otherIterator.hasNext());
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        for (E elem : this) {
            hashCode = 31 * hashCode + (elem == null ? 0 : elem.hashCode());
        }

        return hashCode;
    }

    @NotNull
    @Override
    public java.util.List<E> asList() {
        return new ListAdapater<E>(this);
    }
}
