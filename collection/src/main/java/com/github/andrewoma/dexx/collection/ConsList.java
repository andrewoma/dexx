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

import com.github.andrewoma.dexx.collection.internal.base.AbstractLinkedList;
import com.github.andrewoma.dexx.collection.internal.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * {@code ConsList} is a functional {@link com.github.andrewoma.dexx.collection.LinkedList} implementation
 * that constructs a list by prepending an element to another list.
 * <p/>
 * <p><b>WARNING:</b> Appending to a {@code ConsList} results in copying the entire list - always
 * use a {@link com.github.andrewoma.dexx.collection.Builder} when appending. Likewise,
 * operations like {@link #set(int, Object)} will result in copying portions of the list.
 * <p/>
 * <p>If there is any doubt as to the access patterns for using a {@code List}, use a {@link com.github.andrewoma.dexx.collection.Vector}
 * instead.
 */
public abstract class ConsList<E> extends AbstractLinkedList<E> {
    private static final ConsList<Object> EMPTY = new Nil<Object>();

    @NotNull
    public static <E> BuilderFactory<E, ConsList<E>> factory() {
        return new BuilderFactory<E, ConsList<E>>() {
            @NotNull
            @Override
            public Builder<E, ConsList<E>> newBuilder() {
                return new AbstractBuilder<E, ConsList<E>>() {
                    private Vector<E> buffer = Vector.empty();

                    @NotNull
                    @Override
                    public Builder<E, ConsList<E>> add(E element) {
                        buffer = buffer.append(element);
                        return this;
                    }

                    @NotNull
                    @Override
                    public ConsList<E> doBuild() {
                        ConsList<E> result = ConsList.empty();
                        for (int i = buffer.size() - 1; i >= 0; i--) {
                            result = result.prepend(buffer.get(i));
                        }
                        return result;
                    }
                };
            }
        };
    }

    @SuppressWarnings("unchecked")
    public static <E> ConsList<E> empty() {
        return (ConsList<E>) EMPTY;
    }

    @NotNull
    @Override
    public ConsList<E> prepend(E elem) {
        return new Cons<E>(elem, this);
    }

    @NotNull
    @Override
    public abstract ConsList<E> append(E elem);

    @NotNull
    @Override
    public Iterator<E> iterator() {
        return new ConsListIterator<E>(this);
    }

    @NotNull
    @Override
    public abstract ConsList<E> range(int from, boolean fromInclusive, int to, boolean toInclusive);

    @NotNull
    @Override
    public abstract ConsList<E> tail();

    @NotNull
    @Override
    public abstract ConsList<E> take(int number);

    @NotNull
    @Override
    public abstract ConsList<E> drop(int number);

    @NotNull
    @Override
    public abstract ConsList<E> set(int i, E elem);
}

class ConsListIterator<E> implements Iterator<E> {
    ConsList<E> current;

    ConsListIterator(ConsList<E> current) {
        this.current = current;
    }

    @Override
    public boolean hasNext() {
        return !(current instanceof Nil);
    }

    @Override
    public E next() {
        if (current instanceof Nil) {
            throw new NoSuchElementException("Empty list");
        }
        Cons<E> cons = (Cons<E>) current;
        current = cons.tail();
        return cons.first();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

/**
 * Nil is the empty list
 */
class Nil<E> extends ConsList<E> {
    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    @Nullable
    public E first() {
        return null;
    }

    @Override
    @NotNull
    public ConsList<E> tail() {
        return this; // Scala throws UnsupportedOperationException. Should this?
    }

    @NotNull
    @Override
    public ConsList<E> set(int i, E elem) {
        throw new IndexOutOfBoundsException();
    }

    @NotNull
    @Override
    public ConsList<E> append(E elem) {
        return new Cons<E>(elem, this);
    }

    @NotNull
    @Override
    public ConsList<E> drop(int number) {
        return this;
    }

    @NotNull
    @Override
    public ConsList<E> take(int number) {
        return this;
    }

    @NotNull
    @Override
    public ConsList<E> range(int from, boolean fromInclusive, int to, boolean toInclusive) {
        return this;
    }

    @Override
    public E get(int i) {
        throw new IndexOutOfBoundsException(String.valueOf(i));
    }

    @Nullable
    @Override
    public E last() {
        return null;
    }
}

/**
 * Cons constructs a new list by prepending a new element to an existing list
 */
class Cons<E> extends ConsList<E> {
    private E head;
    private ConsList<E> tail;


    Cons(E head, ConsList<E> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    @Nullable
    public E first() {
        return head;
    }

    @Override
    @NotNull
    public ConsList<E> tail() {
        return tail;
    }

    @NotNull
    @Override
    public ConsList<E> set(int i, E elem) {
        // Copy everything up to the index we need to set
        java.util.List<E> before = new ArrayList<E>(i);
        ConsListIterator<E> iterator = (ConsListIterator<E>) iterator();
        for (int count = 0; count < i && iterator.hasNext(); count++) {
            before.add(iterator.next());
        }

        if (!iterator.hasNext()) {
            throw new IndexOutOfBoundsException(String.valueOf(i));
        }

        iterator.next(); // Discard the current value
        ConsList<E> result = iterator.current; // Share the unmodified tail

        result = result.prepend(elem); // Set the new value

        // Add all the elements before the one we set back
        for (int index = before.size() - 1; index >= 0; index--) {
            result = result.prepend(before.get(index));
        }

        return result;
    }

    @NotNull
    @Override
    public ConsList<E> append(E elem) {
        java.util.List<E> current = new ArrayList<E>();
        for (E e : this) {
            current.add(e);
        }

        ConsList<E> result = empty();
        result = result.append(elem);

        for (int i = current.size() - 1; i >= 0; i--) {
            result = result.prepend(current.get(i));
        }

        return result;
    }

    @NotNull
    @Override
    public ConsList<E> drop(int number) {
        ConsListIterator<E> iterator = (ConsListIterator<E>) iterator();
        for (int count = 0; count < number && iterator.hasNext(); count++) {
            iterator.next();
        }

        return iterator.current;
    }

    @NotNull
    @Override
    public ConsList<E> take(int number) {
        java.util.List<E> top = new ArrayList<E>(Math.max(0, number));
        ConsListIterator<E> iterator = (ConsListIterator<E>) iterator();
        for (int count = 0; count < number && iterator.hasNext(); count++) {
            top.add(iterator.next());
        }

        ConsList<E> result = empty();
        for (int i = top.size() - 1; i >= 0; i--) {
            result = result.prepend(top.get(i));
        }

        return result;
    }

    @NotNull
    @Override
    public ConsList<E> range(int from, boolean fromInclusive, int to, boolean toInclusive) {
        from = fromInclusive ? from : from + 1;
        to = toInclusive ? to + 1 : to;

        return this.drop(from).take(to - from);
    }

    @Override
    public E get(int i) {
        int count = 0;
        for (E e : this) {
            if (i == count++) {
                return e;
            }
        }

        throw new IndexOutOfBoundsException(String.valueOf(i));
    }

    @Nullable
    @Override
    public E last() {
        E last = null;
        for (E e : this) {
            last = e;
        }
        return last;
    }
}