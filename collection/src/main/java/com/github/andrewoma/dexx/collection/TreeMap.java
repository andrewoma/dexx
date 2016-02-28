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

import com.github.andrewoma.dexx.collection.internal.base.AbstractIterable;
import com.github.andrewoma.dexx.collection.internal.base.AbstractSortedMap;
import com.github.andrewoma.dexx.collection.internal.builder.AbstractSelfBuilder;
import com.github.andrewoma.dexx.collection.internal.redblack.DefaultTreeFactory;
import com.github.andrewoma.dexx.collection.internal.redblack.DerivedKeyFactory;
import com.github.andrewoma.dexx.collection.internal.redblack.RedBlackTree;
import com.github.andrewoma.dexx.collection.internal.redblack.Tree;
import com.github.andrewoma.dexx.collection.internal.redblack.TreeFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;

/**
 * {@code TreeMap} is an implementation of {@code SortedMap} based on a
 * <a href="http://en.wikipedia.org/wiki/Red%E2%80%93black_tree">red-black tree</a>.
 * <p/>
 * <p>{@code TreeMaps} can be constructed with a {@link com.github.andrewoma.dexx.collection.KeyFunction}
 * to provide modest memory saving per node. See {@link com.github.andrewoma.dexx.collection.DerivedKeyHashMap}
 * for an example of using a key function.
 */
public class TreeMap<K, V> extends AbstractSortedMap<K, V> {
    private final Tree<K, V> tree;
    private final RedBlackTree<K, V> redBlackTree;

    public TreeMap() {
        tree = null;
        redBlackTree = new RedBlackTree<K, V>();
    }

    @NotNull
    public static <K, V> BuilderFactory<Pair<K, V>, TreeMap<K, V>> factory(final Comparator<? super K> ordering, final KeyFunction<K, V> keyFunction) {
        return new BuilderFactory<Pair<K, V>, TreeMap<K, V>>() {
            @NotNull
            @Override
            public Builder<Pair<K, V>, TreeMap<K, V>> newBuilder() {
                return new AbstractSelfBuilder<Pair<K, V>, TreeMap<K, V>>(new TreeMap<K, V>(ordering, keyFunction)) {
                    @NotNull
                    @Override
                    public Builder<Pair<K, V>, TreeMap<K, V>> add(Pair<K, V> element) {
                        result = result.put(element.component1(), element.component2());
                        return this;
                    }
                };
            }
        };
    }

    @Override
    public Comparator<? super K> comparator() {
        return redBlackTree.getOrdering();
    }

    public TreeMap(Comparator<? super K> ordering, KeyFunction<K, V> keyFunction) {
        TreeFactory factory = keyFunction == null ? new DefaultTreeFactory() : new DerivedKeyFactory();
        tree = null;
        redBlackTree = new RedBlackTree<K, V>(factory, ordering, keyFunction);
    }

    private TreeMap(Tree<K, V> tree, RedBlackTree<K, V> redBlackTree) {
        this.tree = tree;
        this.redBlackTree = redBlackTree;
    }

    @Override
    public boolean containsKey(@NotNull K key) {
        return redBlackTree.contains(tree, key);
    }

    @NotNull
    @Override
    public TreeMap<K, V> put(@NotNull K key, V value) {
        return new TreeMap<K, V>(redBlackTree.update(tree, key, value, true), redBlackTree);
    }

    @Override
    public V get(@NotNull K key) {
        return redBlackTree.get(tree, key);
    }

    @Override
    public int size() {
        return RedBlackTree.count(tree);
    }

    @Override
    public boolean isEmpty() {
        return redBlackTree.isEmpty(tree);
    }

    @NotNull
    @Override
    public TreeMap<K, V> remove(@NotNull K key) {
        return new TreeMap<K, V>(redBlackTree.delete(tree, key), redBlackTree);
    }

    @NotNull
    @Override
    public Iterator<Pair<K, V>> iterator() {
        return redBlackTree.iterator(tree);
    }

    public <U> void forEach(@NotNull Function<Pair<K, V>, U> f) {
        redBlackTree.forEach(tree, f);
    }

    @Nullable
    @Override
    public Pair<K, V> first() {
        return tree != null ? toPair(redBlackTree.smallest(tree)) : null;
    }

    private Pair<K, V> toPair(Tree<K, V> tree) {
        return new Pair<K, V>(tree.getKey(redBlackTree.getKeyFunction()), tree.getValue());
    }

    @Nullable
    @Override
    public Pair<K, V> last() {
        return tree != null ? toPair(redBlackTree.greatest(tree)) : null;
    }

    @NotNull
    @Override
    public SortedMap<K, V> drop(int number) {
        return new TreeMap<K, V>(redBlackTree.drop(tree, number), redBlackTree);
    }

    @NotNull
    @Override
    public SortedMap<K, V> take(int number) {
        return new TreeMap<K, V>(redBlackTree.take(tree, number), redBlackTree);
    }

    @NotNull
    @Override
    public SortedMap<K, V> from(@NotNull K key, boolean inclusive) {
        return new TreeMap<K, V>(redBlackTree.from(tree, key, inclusive), redBlackTree);
    }

    @NotNull
    @Override
    public SortedMap<K, V> to(@NotNull K key, boolean inclusive) {
        return new TreeMap<K, V>(redBlackTree.until(tree, key, inclusive), redBlackTree);
    }

    @NotNull
    @Override
    public SortedMap<K, V> range(@NotNull K from, boolean fromInclusive, @NotNull K to, boolean toInclusive) {
        return new TreeMap<K, V>(redBlackTree.range(tree, from, fromInclusive, to, toInclusive), redBlackTree);
    }

    @NotNull
    @Override
    public Iterable<K> keys() {
        return new AbstractIterable<K>() {
            @NotNull
            @Override
            public Iterator<K> iterator() {
                return redBlackTree.keysIterator(tree);
            }
        };
    }

    @NotNull
    @Override
    public Iterable<V> values() {
        return new AbstractIterable<V>() {
            @NotNull
            @Override
            public Iterator<V> iterator() {
                return redBlackTree.valuesIterator(tree);
            }
        };
    }

    @NotNull
    protected TreeMap<K, V> slice(int from, int until) {
        return new TreeMap<K, V>(redBlackTree.slice(tree, from, until), redBlackTree);
    }
}

