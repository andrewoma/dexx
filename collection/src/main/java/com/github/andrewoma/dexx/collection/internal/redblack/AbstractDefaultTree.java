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

abstract class AbstractDefaultTree<K, V> extends AbstractTree<K, V> {
    private final K key;

    protected AbstractDefaultTree(K key, V value, Tree<K, V> left, Tree<K, V> right) {
        super(left, right, value);
        this.key = key;
    }

    @Override
    public K getKey(KeyFunction<K, V> keyFunction) {
        return key;
    }
}

final class DefaultRedTree<K, V> extends AbstractDefaultTree<K, V> implements RedTree<K, V> {
    public DefaultRedTree(K key, V value, Tree<K, V> left, Tree<K, V> right) {
        super(key, value, left, right);
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
        return new DefaultBlackTree<K, V>(getKey(null), getValue(), getLeft(), getRight());
    }

    public Tree<K, V> red() {
        return this;
    }

    @Override
    public String toString() {
        return "RedTree(" + getKey(null) + ", " + getValue() + ", " + getLeft() + ", " + getRight() + ")";
    }
}

final class DefaultBlackTree<K, V> extends AbstractDefaultTree<K, V> implements BlackTree<K, V> {
    public DefaultBlackTree(K key, V value, Tree<K, V> left, Tree<K, V> right) {
        super(key, value, left, right);
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
        return new DefaultRedTree<K, V>(getKey(null), getValue(), getLeft(), getRight());
    }

    @Override
    public String toString() {
        return "BlackTree(" + getKey(null) + ", " + getValue() + ", " + getLeft() + ", " + getRight() + ")";
    }
}

