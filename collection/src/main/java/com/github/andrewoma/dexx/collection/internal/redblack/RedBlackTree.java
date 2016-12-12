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

import com.github.andrewoma.dexx.collection.Function;
import com.github.andrewoma.dexx.collection.KeyFunction;
import com.github.andrewoma.dexx.collection.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


public class RedBlackTree<K, V> {
    private final TreeFactory factory;
    private final Comparator<? super K> ordering;
    private final KeyFunction<K, V> kf;

    @SuppressWarnings("unchecked")
    private static final Comparator DEFAULT_COMPARATOR = new Comparator() {
        public int compare(@NotNull Object o1, @NotNull Object o2) {
            return ((Comparable) o1).compareTo(o2);
        }
    };

    public RedBlackTree() {
        this(new DefaultTreeFactory(), null, null);
    }

    @SuppressWarnings("unchecked")
    public RedBlackTree(TreeFactory factory, Comparator<? super K> ordering, KeyFunction<K, V> keyFunction) {
        this.factory = factory;
        this.kf = keyFunction;

        if (ordering == null) {
            ordering = DEFAULT_COMPARATOR;
        }

        this.ordering = ordering;
    }

    public KeyFunction<K, V> getKeyFunction() {
        return kf;
    }

    public Comparator<? super K> getOrdering() {
        return ordering == DEFAULT_COMPARATOR ? null : ordering;
    }

    public boolean isEmpty(Tree<K, V> tree) {
        return tree == null;
    }

    public boolean contains(Tree<K, V> tree, K x) {
        return lookup(tree, x) != null;
    }

    public V get(Tree<K, V> tree, K x) {
        Tree<K, V> lookup = lookup(tree, x);
        return lookup == null ? null : lookup.getValue();
    }

    // TODO: Remove recursion @tailrec
    public Tree<K, V> lookup(Tree<K, V> tree, K x) {
        if (tree == null)
            return null;

        int cmp = ordering.compare(x, tree.getKey(kf));

        if (cmp < 0) return lookup(tree.getLeft(), x);
        else if (cmp > 0) return lookup(tree.getRight(), x);
        else return tree;
    }

    public static int count(Tree<?, ?> tree) {
        return tree == null ? 0 : tree.count();
    }

    public Tree<K, V> update(Tree<K, V> tree, K k, V v, boolean overwrite) {
        return blacken(upd(tree, k, v, overwrite));
    }

    public Tree<K, V> delete(Tree<K, V> tree, K k) {
        return blacken(del(tree, k));
    }

    public Tree<K, V> range(Tree<K, V> tree, K from, boolean fromInclusive, K until, boolean untilInclusive) {
        return blacken(doRange(tree, from, fromInclusive, until, untilInclusive));
    }

    public Tree<K, V> from(Tree<K, V> tree, K from, boolean inclusive) {
        return blacken(doFrom(tree, from, inclusive));
    }

    public Tree<K, V> until(Tree<K, V> tree, K key, boolean inclusive) {
        return blacken(doUntil(tree, key, inclusive));
    }

    public Tree<K, V> drop(Tree<K, V> tree, int n) {
        return blacken(doDrop(tree, n));
    }

    public Tree<K, V> take(Tree<K, V> tree, int n) {
        return blacken(doTake(tree, n));
    }

    public Tree<K, V> slice(Tree<K, V> tree, int from, int until) {
        return blacken(doSlice(tree, from, until));
    }

    public Tree<K, V> smallest(Tree<K, V> tree) {
        if (tree == null) throw new NoSuchElementException("empty map");
        Tree<K, V> result = tree;
        while (result.getLeft() != null) result = result.getLeft();
        return result;
    }

    public Tree<K, V> greatest(Tree<K, V> tree) {
        if (tree == null) throw new NoSuchElementException("empty map");
        Tree<K, V> result = tree;
        while (result.getRight() != null) result = result.getRight();
        return result;
    }

    public <U> void forEach(Tree<K, V> tree, Function<Pair<K, V>, U> f) {
        if (tree != null) {
            if (tree.getLeft() != null) {
                forEach(tree.getLeft(), f);
            }
            f.invoke(new Pair<K, V>(tree.getKey(kf), tree.getValue()));
            if (tree.getRight() != null) {
                forEach(tree.getRight(), f);
            }
        }
    }

    //        def foreach[K, V, U](tree: Tree[K, V], f: ((K, V)) => U): Unit = if (tree ne null) {
//        if (tree.left ne null) foreach(tree.left, f)
//        f((tree.key, tree.value))
//        if (tree.right ne null) foreach(tree.right, f)
//        }


//        def foreachKey[K, U](tree: Tree[K, _], f: K => U): Unit = if (tree ne null) {
//        if (tree.left ne null) foreachKey(tree.left, f)
//        f(tree.key)
//        if (tree.right ne null) foreachKey(tree.right, f)
//        }

    public Iterator<Pair<K, V>> iterator(Tree<K, V> tree) {
        return new EntriesIterator<K, V>(tree);
    }

    public Iterator<K> keysIterator(Tree<K, V> tree) {
        return new KeysIterator<K, V>(tree, kf);
    }

    public Iterator<V> valuesIterator(Tree<K, V> tree) {
        return new ValuesIterator<K, V>(tree);
    }

    private boolean isRedTree(Tree<?, ?> tree) {
        return tree != null && tree.isRed();
    }

    private boolean isBlackTree(Tree<?, ?> tree) {
        return tree != null && tree.isBlack();
    }

    private Tree<K, V> blacken(Tree<K, V> t) {
        return (t == null) ? null : t.black();
    }

    private Tree<K, V> mkTree(boolean isBlack, K k, V v, Tree<K, V> l, Tree<K, V> r) {
        return isBlack ? factory.black(k, v, l, r) : factory.red(k, v, l, r);
    }

    private Tree<K, V> balanceLeft(boolean isBlack, K z, V zv, Tree<K, V> l, Tree<K, V> d) {
        if (isRedTree(l) && isRedTree(l.getLeft()))
            return factory.red(l.getKey(kf), l.getValue(), factory.black(l.getLeft().getKey(kf), l.getLeft().getValue(), l.getLeft().getLeft(), l.getLeft().getRight()), factory.black(z, zv, l.getRight(), d));
        else if (isRedTree(l) && isRedTree(l.getRight()))
            return factory.red(l.getRight().getKey(kf), l.getRight().getValue(), factory.black(l.getKey(kf), l.getValue(), l.getLeft(), l.getRight().getLeft()), factory.black(z, zv, l.getRight().getRight(), d));
        else
            return mkTree(isBlack, z, zv, l, d);
    }

    private Tree<K, V> balanceRight(boolean isBlack, K x, V xv, Tree<K, V> a, Tree<K, V> r) {
        if (isRedTree(r) && isRedTree(r.getLeft()))
            return factory.red(r.getLeft().getKey(kf), r.getLeft().getValue(), factory.black(x, xv, a, r.getLeft().getLeft()), factory.black(r.getKey(kf), r.getValue(), r.getLeft().getRight(), r.getRight()));
        else if (isRedTree(r) && isRedTree(r.getRight()))
            return factory.red(r.getKey(kf), r.getValue(), factory.black(x, xv, a, r.getLeft()), factory.black(r.getRight().getKey(kf), r.getRight().getValue(), r.getRight().getLeft(), r.getRight().getRight()));
        else
            return mkTree(isBlack, x, xv, a, r);
    }

    private Tree<K, V> upd(Tree<K, V> tree, K k, V v, boolean overwrite) {
        if (tree == null)
            return factory.red(k, v, null, null);

        int cmp = ordering.compare(k, tree.getKey(kf));

        if (cmp < 0)
            return balanceLeft(isBlackTree(tree), tree.getKey(kf), tree.getValue(), upd(tree.getLeft(), k, v, overwrite), tree.getRight());

        if (cmp > 0)
            return balanceRight(isBlackTree(tree), tree.getKey(kf), tree.getValue(), tree.getLeft(), upd(tree.getRight(), k, v, overwrite));

        if (overwrite || !k.equals(tree.getKey(kf))) // Hmmm ... how can these not be equal
            return mkTree(isBlackTree(tree), k, v, tree.getLeft(), tree.getRight());

        return tree;
    }

    private Tree<K, V> updNth(Tree<K, V> tree, int idx, K k, V v, boolean overwrite) {
        if (tree == null) {
            return mkTree(false, k, v, null, null);
        } else {
            int rank = count(tree.getLeft()) + 1;
            if (idx < rank) {
                return balanceLeft(isBlackTree(tree), tree.getKey(kf), tree.getValue(), updNth(tree.getLeft(), idx, k, v, overwrite), tree.getRight());
            } else if (idx > rank) {
                return balanceRight(isBlackTree(tree), tree.getKey(kf), tree.getValue(), tree.getLeft(), updNth(tree.getRight(), idx - rank, k, v, overwrite));
            } else if (overwrite) {
                return mkTree(isBlackTree(tree), k, v, tree.getLeft(), tree.getRight());
            }
            return tree;
        }
    }

    /* Based on Stefan Kahrs' Haskell version of Okasaki's Red&Black Trees
    * http://www.cse.unsw.edu.au/~dons/data/RedBlackTree.html */
    private Tree<K, V> del(Tree<K, V> tree, K k) {
        if (tree == null)
            return null;

        int cmp = ordering.compare(k, tree.getKey(kf));
        if (cmp < 0) return delLeft(tree, k);
        else if (cmp > 0) return delRight(tree, k);
        else return append(tree.getLeft(), tree.getRight());
    }

    private Tree<K, V> balance(K x, V xv, Tree<K, V> tl, Tree<K, V> tr) {
        if (isRedTree(tl)) {
            if (isRedTree(tr)) {
                return factory.red(x, xv, tl.black(), tr.black());
            } else if (isRedTree(tl.getLeft())) {
                return factory.red(tl.getKey(kf), tl.getValue(), tl.getLeft().black(), factory.black(x, xv, tl.getRight(), tr));
            } else if (isRedTree(tl.getRight())) {
                return factory.red(tl.getRight().getKey(kf), tl.getRight().getValue(), factory.black(tl.getKey(kf), tl.getValue(), tl.getLeft(), tl.getRight().getLeft()), factory.black(x, xv, tl.getRight().getRight(), tr));
            } else {
                return factory.black(x, xv, tl, tr);
            }
        } else if (isRedTree(tr)) {
            if (isRedTree(tr.getRight())) {
                return factory.red(tr.getKey(kf), tr.getValue(), factory.black(x, xv, tl, tr.getLeft()), tr.getRight().black());
            } else if (isRedTree(tr.getLeft())) {
                return factory.red(tr.getLeft().getKey(kf), tr.getLeft().getValue(), factory.black(x, xv, tl, tr.getLeft().getLeft()), factory.black(tr.getKey(kf), tr.getValue(), tr.getLeft().getRight(), tr.getRight()));
            } else {
                return factory.black(x, xv, tl, tr);
            }
        } else {
            return factory.black(x, xv, tl, tr);
        }
    }

    private Tree<K, V> subl(Tree<K, V> t) {
        if (isBlackTree(t))
            return t.red();

        throw new RuntimeException("Defect: invariance violation; expected black, got " + t);
    }

    private Tree<K, V> balLeft(K x, V xv, Tree<K, V> tl, Tree<K, V> tr) {
        if (isRedTree(tl)) {
            return factory.red(x, xv, tl.black(), tr);
        } else if (isBlackTree(tr)) {
            return balance(x, xv, tl, tr.red());
        } else if (isRedTree(tr) && isBlackTree(tr.getLeft())) {
            return factory.red(tr.getLeft().getKey(kf), tr.getLeft().getValue(), factory.black(x, xv, tl, tr.getLeft().getLeft()), balance(tr.getKey(kf), tr.getValue(), tr.getLeft().getRight(), subl(tr.getRight())));
        } else {
            throw new RuntimeException("Defect: invariance violation");
        }
    }

    private Tree<K, V> balRight(K x, V xv, Tree<K, V> tl, Tree<K, V> tr) {
        if (isRedTree(tr)) {
            return factory.red(x, xv, tl, tr.black());
        } else if (isBlackTree(tl)) {
            return balance(x, xv, tl.red(), tr);
        } else if (isRedTree(tl) && isBlackTree(tl.getRight())) {
            return factory.red(tl.getRight().getKey(kf), tl.getRight().getValue(), balance(tl.getKey(kf), tl.getValue(), subl(tl.getLeft()), tl.getRight().getLeft()), factory.black(x, xv, tl.getRight().getRight(), tr));
        } else {
            throw new RuntimeException("Defect: invariance violation");
        }
    }

    private Tree<K, V> delLeft(Tree<K, V> tree, K k) {
        return isBlackTree(tree.getLeft()) ? balLeft(tree.getKey(kf), tree.getValue(), del(tree.getLeft(), k), tree.getRight()) : factory.red(tree.getKey(kf), tree.getValue(), del(tree.getLeft(), k), tree.getRight());
    }

    private Tree<K, V> delRight(Tree<K, V> tree, K k) {
        return isBlackTree(tree.getRight()) ? balRight(tree.getKey(kf), tree.getValue(), tree.getLeft(), del(tree.getRight(), k)) : factory.red(tree.getKey(kf), tree.getValue(), tree.getLeft(), del(tree.getRight(), k));
    }

    public Tree<K, V> append(Tree<K, V> tl, Tree<K, V> tr) {
        if (tl == null) {
            return tr;
        } else if (tr == null) {
            return tl;
        } else if (isRedTree(tl) && isRedTree(tr)) {
            Tree<K, V> bc = append(tl.getRight(), tr.getLeft());
            if (isRedTree(bc)) {
                return factory.red(bc.getKey(kf), bc.getValue(), factory.red(tl.getKey(kf), tl.getValue(), tl.getLeft(), bc.getLeft()), factory.red(tr.getKey(kf), tr.getValue(), bc.getRight(), tr.getRight()));
            } else {
                return factory.red(tl.getKey(kf), tl.getValue(), tl.getLeft(), factory.red(tr.getKey(kf), tr.getValue(), bc, tr.getRight()));
            }
        } else if (isBlackTree(tl) && isBlackTree(tr)) {
            Tree<K, V> bc = append(tl.getRight(), tr.getLeft());
            if (isRedTree(bc)) {
                return factory.red(bc.getKey(kf), bc.getValue(), factory.black(tl.getKey(kf), tl.getValue(), tl.getLeft(), bc.getLeft()), factory.black(tr.getKey(kf), tr.getValue(), bc.getRight(), tr.getRight()));
            } else {
                return balLeft(tl.getKey(kf), tl.getValue(), tl.getLeft(), factory.black(tr.getKey(kf), tr.getValue(), bc, tr.getRight()));
            }
        } else if (isRedTree(tr)) {
            return factory.red(tr.getKey(kf), tr.getValue(), append(tl, tr.getLeft()), tr.getRight());
        } else if (isRedTree(tl)) {
            return factory.red(tl.getKey(kf), tl.getValue(), tl.getLeft(), append(tl.getRight(), tr));
        } else {
            throw new RuntimeException("unmatched tree on append: " + tl + ", " + tr);
        }
    }

    private Tree<K, V> doFrom(Tree<K, V> tree, K from, boolean inclusive) {
        if (tree == null) return null;
        if (inclusive) {
            if (ordering.compare(tree.getKey(kf), from) < 0) return doFrom(tree.getRight(), from, true);
        } else {
            if (ordering.compare(tree.getKey(kf), from) <= 0) return doFrom(tree.getRight(), from, false);
        }

        Tree<K, V> newLeft = doFrom(tree.getLeft(), from, inclusive);
        if (newLeft != null && newLeft.equals(tree.getLeft())) return tree;
        else if (newLeft == null) return upd(tree.getRight(), tree.getKey(kf), tree.getValue(), false);
        else return rebalance(tree, newLeft, tree.getRight());
    }

    private Tree<K, V> doUntil(Tree<K, V> tree, K until, boolean inclusive) {
        if (tree == null) return null;
        if (inclusive) {
            if (ordering.compare(until, tree.getKey(kf)) < 0) return doUntil(tree.getLeft(), until, true);
        } else {
            if (ordering.compare(until, tree.getKey(kf)) <= 0) return doUntil(tree.getLeft(), until, false);
        }

        Tree<K, V> newRight = doUntil(tree.getRight(), until, inclusive);
        if (newRight != null && newRight.equals(tree.getRight())) return tree;
        else if (newRight == null) return upd(tree.getLeft(), tree.getKey(kf), tree.getValue(), false);
        else return rebalance(tree, tree.getLeft(), newRight);
    }

    private Tree<K, V> doRange(Tree<K, V> tree, K from, boolean fromInclusive, K until, boolean untilInclusive) {
        if (tree == null) return null;

        if (fromInclusive) {
            if (ordering.compare(tree.getKey(kf), from) < 0)
                return doRange(tree.getRight(), from, true, until, untilInclusive);
        } else {
            if (ordering.compare(tree.getKey(kf), from) <= 0)
                return doRange(tree.getRight(), from, false, until, untilInclusive);
        }

        if (untilInclusive) {
            if (ordering.compare(until, tree.getKey(kf)) < 0)
                return doRange(tree.getLeft(), from, fromInclusive, until, true);
        } else {
            if (ordering.compare(until, tree.getKey(kf)) <= 0)
                return doRange(tree.getLeft(), from, fromInclusive, until, false);
        }

        Tree<K, V> newLeft = doFrom(tree.getLeft(), from, fromInclusive);
        Tree<K, V> newRight = doUntil(tree.getRight(), until, untilInclusive);
        if ((newLeft == tree.getLeft()) && (newRight == tree.getRight())) return tree;
        else if (newLeft == null) return upd(newRight, tree.getKey(kf), tree.getValue(), false);
        else if (newRight == null) return upd(newLeft, tree.getKey(kf), tree.getValue(), false);
        else return rebalance(tree, newLeft, newRight);
    }

    private Tree<K, V> doDrop(Tree<K, V> tree, int n) {
        if (n <= 0) return tree;
        if (n >= count(tree)) return null;
        int count = count(tree.getLeft());
        if (n > count) return doDrop(tree.getRight(), n - count - 1);
        Tree<K, V> newLeft = doDrop(tree.getLeft(), n);
        if (newLeft == tree.getLeft()) return tree;
        else if (newLeft == null)
            return updNth(tree.getRight(), n - count - 1, tree.getKey(kf), tree.getValue(), false);
        else return rebalance(tree, newLeft, tree.getRight());
    }

    private Tree<K, V> doTake(Tree<K, V> tree, int n) {
        if (n <= 0) return null;
        if (n >= count(tree)) return tree;
        int count = count(tree.getLeft());
        if (n <= count) return doTake(tree.getLeft(), n);
        Tree<K, V> newRight = doTake(tree.getRight(), n - count - 1);
        if (newRight == tree.getRight()) return tree;
        else if (newRight == null) return updNth(tree.getLeft(), n, tree.getKey(kf), tree.getValue(), false);
        else return rebalance(tree, tree.getLeft(), newRight);
    }

    private Tree<K, V> doSlice(Tree<K, V> tree, int from, int until) {
        if (tree == null) return null;
        int count = count(tree.getLeft());
        if (from > count) return doSlice(tree.getRight(), from - count - 1, until - count - 1);
        if (until <= count) return doSlice(tree.getLeft(), from, until);
        Tree<K, V> newLeft = doDrop(tree.getLeft(), from);
        Tree<K, V> newRight = doTake(tree.getRight(), until - count - 1);
        if ((newLeft == tree.getLeft()) && (newRight == tree.getRight())) return tree;
        else if (newLeft == null) return updNth(newRight, from - count - 1, tree.getKey(kf), tree.getValue(), false);
        else if (newRight == null) return updNth(newLeft, until, tree.getKey(kf), tree.getValue(), false);
        else return rebalance(tree, newLeft, newRight);
    }

    // The zipper returned might have been traversed left-most (always the left child)
    // or right-most (always the right child). Left trees are traversed right-most,
    // and right trees are traversed leftmost.

    // Returns the zipper for the side with deepest black nodes depth, a flag
    // indicating whether the trees were unbalanced at all, and a flag indicating
    // whether the zipper was traversed left-most or right-most.

    // If the trees were balanced, returns an empty zipper

    @SuppressWarnings("unchecked")
    private Zipper<K, V> compareDepth(Tree<K, V> left, Tree<K, V> right) {
        return unzipBoth(left, right, (List<Tree<K, V>>) Collections.EMPTY_LIST, (List<Tree<K, V>>) Collections.EMPTY_LIST, 0);
    }

    // Once a side is found to be deeper, unzip it to the bottom
    private List<Tree<K, V>> unzip(List<Tree<K, V>> zipper, boolean leftMost) {
        Tree<K, V> next = leftMost ? zipper.get(0).getLeft() : zipper.get(0).getRight();
        if (next == null)
            return zipper;

        return unzip(cons(next, zipper), leftMost);
    }

    // TODO ... do something more efficient
    private <E> List<E> cons(E value, List<E> list) {
        List<E> result = new ArrayList<E>(list.size() + 1);
        result.add(value);
        result.addAll(list);
        return result;
    }

    // Unzip left tree on the rightmost side and right tree on the leftmost side until one is
    // found to be deeper, or the bottom is reached
    @SuppressWarnings("unchecked")
    private Zipper<K, V> unzipBoth(Tree<K, V> left, Tree<K, V> right, List<Tree<K, V>> leftZipper, List<Tree<K, V>> rightZipper, int smallerDepth) {
        if (isBlackTree(left) && isBlackTree(right)) {
            return unzipBoth(left.getRight(), right.getLeft(), cons(left, leftZipper), cons(right, rightZipper), smallerDepth + 1);
        } else if (isRedTree(left) && isRedTree(right)) {
            return unzipBoth(left.getRight(), right.getLeft(), cons(left, leftZipper), cons(right, rightZipper), smallerDepth);
        } else if (isRedTree(right)) {
            return unzipBoth(left, right.getLeft(), leftZipper, cons(right, rightZipper), smallerDepth);
        } else if (isRedTree(left)) {
            return unzipBoth(left.getRight(), right, cons(left, leftZipper), rightZipper, smallerDepth);
        } else if ((left == null) && (right == null)) {
            return new Zipper<K, V>((List<Tree<K, V>>) Collections.EMPTY_LIST, true, false, smallerDepth);
        } else if ((left == null) && isBlackTree(right)) {
            return new Zipper<K, V>(unzip(cons(right, rightZipper), true), false, true, smallerDepth);
        } else if (isBlackTree(left) && (right == null)) {
            return new Zipper<K, V>(unzip(cons(left, leftZipper), false), false, false, smallerDepth);
        } else {
            throw new RuntimeException("unmatched trees in unzip: " + left + ", " + right);
        }
    }

    // This is like drop(n-1), but only counting black nodes
    private List<Tree<K, V>> findDepth(List<Tree<K, V>> zipper, int depth) {
        if (zipper.isEmpty())
            throw new RuntimeException("Defect: unexpected empty zipper while computing range");

        if (isBlackTree(zipper.get(0))) {
            return depth <= 1 ? zipper : findDepth(zipper.subList(1, zipper.size()), depth - 1);
        } else {
            return findDepth(zipper.subList(1, zipper.size()), depth);
        }
    }

    private Tree<K, V> rebalance(Tree<K, V> tree, Tree<K, V> newLeft, Tree<K, V> newRight) {
        // Blackening the smaller tree avoids balancing problems on union;
        // this can't be done later, though, or it would change the result of compareDepth
        Tree<K, V> blkNewLeft = blacken(newLeft);
        Tree<K, V> blkNewRight = blacken(newRight);
        Zipper<K, V> zipper = compareDepth(blkNewLeft, blkNewRight);

        if (zipper.levelled) {
            return factory.black(tree.getKey(kf), tree.getValue(), blkNewLeft, blkNewRight);
        } else {
            List<Tree<K, V>> zipFrom = findDepth(zipper.zipper, zipper.smallerDepth);

            Tree<K, V> result = zipper.leftMost ?
                    factory.red(tree.getKey(kf), tree.getValue(), blkNewLeft, zipFrom.get(0)) :
                    factory.red(tree.getKey(kf), tree.getValue(), zipFrom.get(0), blkNewRight);

            for (Tree<K, V> node : zipFrom.subList(1, zipFrom.size())) {
                if (zipper.leftMost) {
                    result = balanceLeft(isBlackTree(node), node.getKey(kf), node.getValue(), result, node.getRight());
                } else {
                    result = balanceRight(isBlackTree(node), node.getKey(kf), node.getValue(), node.getLeft(), result);
                }
            }

            return result;
        }
    }
}

class Zipper<K, V> {
    final List<Tree<K, V>> zipper;
    final boolean levelled;
    final boolean leftMost;
    final int smallerDepth;

    Zipper(List<Tree<K, V>> zipper, boolean levelled, boolean leftMost, int smallerDepth) {
        this.zipper = zipper;
        this.levelled = levelled;
        this.leftMost = leftMost;
        this.smallerDepth = smallerDepth;
    }
}
