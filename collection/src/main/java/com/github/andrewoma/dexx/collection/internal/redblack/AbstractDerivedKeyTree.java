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

package com.github.andrewoma.dexx.collection.internal.redblack;

import com.github.andrewoma.dexx.collection.KeyFunction;

public abstract class AbstractDerivedKeyTree<K, V> extends AbstractTree<K, V> {

    protected AbstractDerivedKeyTree(Tree<K, V> left, Tree<K, V> right, V value) {
        super(left, right, value);
    }

    public int count() {
        return 1 + RedBlackTree.count(getLeft()) + RedBlackTree.count(getRight());
    }

    @Override
    public K getKey(KeyFunction<K, V> keyFunction) {
        return keyFunction.key(getValue());
    }
}

class DerivedKeyRedTree<K, V> extends AbstractDerivedKeyTree<K, V> implements RedTree<K, V> {
    DerivedKeyRedTree(Tree<K, V> left, Tree<K, V> right, V value) {
        super(left, right, value);
    }

    @Override
    public boolean isBlack() {
        return false;
    }

    @Override
    public boolean isRed() {
        return true;
    }

    public Tree<K, V> black() {
        return new DerivedKeyBlackTree<K, V>(getLeft(), getRight(), getValue());
    }

    public Tree<K, V> red() {
        return this;
    }
}

class DerivedKeyBlackTree<K, V> extends AbstractDerivedKeyTree<K, V> implements BlackTree<K, V> {

    DerivedKeyBlackTree(Tree<K, V> left, Tree<K, V> right, V value) {
        super(left, right, value);
    }

    @Override
    public boolean isBlack() {
        return true;
    }

    @Override
    public boolean isRed() {
        return false;
    }

    public Tree<K, V> black() {
        return this;
    }

    public Tree<K, V> red() {
        return new DerivedKeyRedTree<K, V>(getLeft(), getRight(), getValue());
    }
}