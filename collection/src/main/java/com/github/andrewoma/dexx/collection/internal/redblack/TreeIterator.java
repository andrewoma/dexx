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

/*                     __                                               *\
**     ________ ___   / /  ___     Scala API                            **
**    / __/ __// _ | / /  / _ |    (c) 2005-2013, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package com.github.andrewoma.dexx.collection.internal.redblack;

import com.github.andrewoma.dexx.collection.KeyFunction;
import com.github.andrewoma.dexx.collection.Pair;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 */
abstract class TreeIterator<K, V, R> implements Iterator<R> {
    private Tree<K, V>[] path;
    private int index = 0;
    private Tree<K, V> next;

    @SuppressWarnings("unchecked")
    protected TreeIterator(Tree<K, V> tree) {
        if (tree != null) {
            /*
            * According to "Ralf Hinze. Constructing red-black trees" [http://www.cs.ox.ac.uk/ralf.hinze/publications/#P5]
            * the maximum height of a red-black tree is 2*log_2(n + 2) - 2.
            *
            * According to {@see Integer#numberOfLeadingZeros} ceil(log_2(n)) = (32 - Integer.numberOfLeadingZeros(n - 1))
            *
            * We also don't store the deepest nodes in the path so the maximum path length is further reduced by one.
            */

            int maximumHeight = 2 * (32 - Integer.numberOfLeadingZeros(tree.count() + 2 - 1)) - 2 - 1;
            path = new Tree[maximumHeight];
        }
        next = findNext(tree);
    }

    protected abstract R nextResult(Tree<K, V> tree);

    public boolean hasNext() {
        return next != null;
    }

    public R next() {
        if (next == null)
            throw new NoSuchElementException("next on empty iterator");

        Tree<K, V> tree = next;
        next = findNext(tree.getRight());
        return nextResult(tree);
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    //@tailrec
    private Tree<K, V> findNext(Tree<K, V> tree) {
        if (tree == null) {
            return popPath();
        } else if (tree.getLeft() == null) {
            return tree;
        } else {
            pushPath(tree);
            return findNext(tree.getLeft());
        }
    }

    @SuppressWarnings("unchecked")
    private void pushPath(Tree<K, V> tree) {
        try {
            path[index] = tree;
            index += 1;
        } catch (ArrayIndexOutOfBoundsException e) {
            /*
            * Either the tree became unbalanced or we calculated the maximum height incorrectly.
            * To avoid crashing the iterator we expand the path array. Obviously this should never
            * happen...
            *
            * An exception handler is used instead of an if-condition to optimize the normal path.
            * This makes a large difference in iteration speed!
            */
            e.printStackTrace();
            assert (index >= path.length);
            Tree<K, V>[] temp = new Tree[path.length + 1];
            System.arraycopy(path, 0, temp, 0, path.length);
            path = temp;
            pushPath(tree);
        }
    }

    private Tree<K, V> popPath() {
        if (index == 0)
            return null;

        index -= 1;
        return path[index];
    }
}

class EntriesIterator<K, V> extends TreeIterator<K, V, Pair<K, V>> {
    public EntriesIterator(Tree<K, V> tree) {
        super(tree);
    }

    @Override
    protected Pair<K, V> nextResult(Tree<K, V> tree) {
        return new Pair<K, V>(tree.getKey(null), tree.getValue());
    }
}

class KeysIterator<K, V> extends TreeIterator<K, V, K> {
    private final KeyFunction<K, V> kf;

    KeysIterator(Tree<K, V> tree, KeyFunction<K, V> kf) {
        super(tree);
        this.kf = kf;
    }

    @Override
    protected K nextResult(Tree<K, V> tree) {
        return tree.getKey(kf);
    }
}

class ValuesIterator<K, V> extends TreeIterator<K, V, V> {
    ValuesIterator(Tree<K, V> tree) {
        super(tree);
    }

    @Override
    protected V nextResult(Tree<K, V> tree) {
        return tree.getValue();
    }
}

