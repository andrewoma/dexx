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
**    / __/ __// _ | / /  / _ |    (c) 2003-2013, LAMP/EPFL             **
**  __\ \/ /__/ __ |/ /__/ __ |    http://scala-lang.org/               **
** /____/\___/_/ |_/____/_/ | |                                         **
**                          |/                                          **
\*                                                                      */

package com.github.andrewoma.dexx.collection;

import com.github.andrewoma.dexx.collection.internal.base.AbstractIndexedList;
import com.github.andrewoma.dexx.collection.internal.builder.AbstractBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Vector is a general-purpose, immutable data structure.
 * <p/>
 * <p>It provides random access and updates in effectively constant time, as well as very fast append and prepend.
 * <p/>
 * <p>It is backed by a little endian bit-mapped vector trie with a branching factor of 32.  Locality is very good, but not
 * contiguous, which is good for very large sequences.
 * <p/>
 * <p>See Scala's <a href="http://www.scala-lang.org/docu/files/collections-api/collections_15.html">documentation</a>
 * for more information on the implementation.
 */
public class Vector<E> extends AbstractIndexedList<E> {
    private static final Vector EMPTY = new Vector(0, 0, 0);
    protected final VectorPointer<E> pointer = new VectorPointer<E>();

    @NotNull
    public static <E> BuilderFactory<E, Vector<E>> factory() {
        return new BuilderFactory<E, Vector<E>>() {
            @NotNull
            @Override
            public Builder<E, Vector<E>> newBuilder() {
                return new VectorBuilder<E>();
            }
        };
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <E> Vector<E> empty() {
        return EMPTY;
    }

    private final int startIndex;
    private final int endIndex;
    private final int focus;

    Vector(int startIndex, int endIndex, int focus) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.focus = focus;
    }

    private boolean dirty = false;

    @Override
    public int size() {
        return endIndex - startIndex;
    }

    private void initIterator(VectorIterator<E> s) {
        s.initFrom(pointer);
        if (dirty) s.stabilize(focus);
        if (s.depth > 1) s.gotoPos(startIndex, startIndex ^ focus);
    }

    @NotNull
    @Override
    public Iterator<E> iterator() {
        VectorIterator<E> s = new VectorIterator<E>(startIndex, endIndex);
        initIterator(s);
        return s;
    }

// TODO: check performance of foreach/map etc. should override or not?
// Ideally, clients will inline calls to map all the way down, including the iterator/builder methods.
// In principle, escape analysis could even remove the iterator/builder allocations and do it
// with local variables exclusively. But we're not quite there yet ...

    @Override
    public E get(int index) {
        int idx = checkRangeConvert(index);
        return pointer.getElem(idx, idx ^ focus);
    }

    private int checkRangeConvert(int index) {
        int idx = index + startIndex;
        if (0 <= index && idx < endIndex)
            return idx;
        else
            throw new IndexOutOfBoundsException(String.valueOf(index));
    }

    @NotNull
    @Override
    public Vector<E> take(int n) {
        if (n <= 0)
            return Vector.empty();
        else if (startIndex + n < endIndex)
            return dropBack0(startIndex + n);
        else
            return this;
    }

    @NotNull
    @Override
    public Vector<E> drop(int n) {
        if (n <= 0)
            return this;
        else if (startIndex + n < endIndex)
            return dropFront0(startIndex + n);
        else
            return Vector.empty();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Nullable
    @Override
    public E first() {
        if (isEmpty()) return null;
        return get(0);
    }

    @NotNull
    @Override
    public Vector<E> tail() {
        if (isEmpty()) return this;
        return drop(1);
    }

    @Nullable
    @Override
    public E last() {
        if (isEmpty()) return null;
        return get(size() - 1);
    }

    @NotNull
    @Override
    public Vector<E> range(int from, boolean fromInclusive, int to, boolean toInclusive) {
        return slice(from + (fromInclusive ? 0 : 1), to + (toInclusive ? 1 : 0));
    }

    @NotNull
    private Vector<E> slice(int from, int until) {
        return take(until).drop(from);
    }

    @NotNull
    protected Pair<Vector<E>, Vector<E>> splitAt(int n) {
        return new Pair<Vector<E>, Vector<E>>(take(n), drop(n));
    }

    @NotNull
    @Override
    public Vector<E> set(int index, E elem) {
        int idx = checkRangeConvert(index);
        Vector<E> s = new Vector<E>(startIndex, endIndex, idx);
        s.pointer.initFrom(pointer);
        s.dirty = dirty;
        s.gotoPosWritable(focus, idx, focus ^ idx);  // if dirty commit changes; go to new pos and prepare for writing
        s.pointer.display0[idx & 0x1f] = elem;
        return s;
    }

    private void gotoPosWritable(int oldIndex, int newIndex, int xor) {
        if (dirty) {
            pointer.gotoPosWritable1(oldIndex, newIndex, xor);
        } else {
            pointer.gotoPosWritable0(newIndex);
            dirty = true;
        }
    }

    private void gotoFreshPosWritable(int oldIndex, int newIndex, int xor) {
        if (dirty) {
            pointer.gotoFreshPosWritable1(oldIndex, newIndex, xor);
        } else {
            pointer.gotoFreshPosWritable0(oldIndex, newIndex, xor);
            dirty = true;
        }
    }

    @NotNull
    @Override
    public Vector<E> prepend(E value) {
        if (endIndex != startIndex) {
            int blockIndex = (startIndex - 1) & ~31;
            int lo = (startIndex - 1) & 31;

            if (startIndex != blockIndex + 32) {
                Vector<E> s = new Vector<E>(startIndex - 1, endIndex, blockIndex);
                s.pointer.initFrom(pointer);
                s.dirty = dirty;
                s.gotoPosWritable(focus, blockIndex, focus ^ blockIndex);
                s.pointer.display0[lo] = value;
                return s;
            } else {

                int freeSpace = ((1 << 5 * (pointer.depth)) - endIndex); // free space at the right given the current tree-structure depth
                int shift = freeSpace & ~((1 << 5 * (pointer.depth - 1)) - 1); // number of elements by which we'll shift right (only move at top level)
                int shiftBlocks = freeSpace >>> 5 * (pointer.depth - 1); // number of top-level blocks

                if (shift != 0) {
                    // case A: we can shift right on the top level

                    if (pointer.depth > 1) {
                        int newBlockIndex = blockIndex + shift;
                        int newFocus = focus + shift;
                        Vector<E> s = new Vector<E>(startIndex - 1 + shift, endIndex + shift, newBlockIndex);
                        s.pointer.initFrom(pointer);
                        s.dirty = dirty;
                        s.shiftTopLevel(0, shiftBlocks); // shift right by n blocks
                        s.gotoFreshPosWritable(newFocus, newBlockIndex, newFocus ^ newBlockIndex); // maybe create pos; prepare for writing
                        s.pointer.display0[lo] = value;
                        return s;
                    } else {
                        int newBlockIndex = blockIndex + 32;
                        int newFocus = focus;

                        Vector<E> s = new Vector<E>(startIndex - 1 + shift, endIndex + shift, newBlockIndex);
                        s.pointer.initFrom(pointer);
                        s.dirty = dirty;
                        s.shiftTopLevel(0, shiftBlocks); // shift right by n elements
                        s.gotoPosWritable(newFocus, newBlockIndex, newFocus ^ newBlockIndex); // prepare for writing
                        s.pointer.display0[shift - 1] = value;
                        return s;
                    }
                } else if (blockIndex < 0) {
                    // case B: we need to move the whole structure
                    int move = (1 << 5 * (pointer.depth + 1)) - (1 << 5 * (pointer.depth));

                    int newBlockIndex = blockIndex + move;
                    int newFocus = focus + move;

                    Vector<E> s = new Vector<E>(startIndex - 1 + move, endIndex + move, newBlockIndex);
                    s.pointer.initFrom(pointer);
                    s.dirty = dirty;
                    s.gotoFreshPosWritable(newFocus, newBlockIndex, newFocus ^ newBlockIndex); // could optimize: we know it will create a whole branch
                    s.pointer.display0[lo] = value;
                    return s;
                } else {
                    int newFocus = focus;

                    Vector<E> s = new Vector<E>(startIndex - 1, endIndex, blockIndex);
                    s.pointer.initFrom(pointer);
                    s.dirty = dirty;
                    s.gotoFreshPosWritable(newFocus, blockIndex, newFocus ^ blockIndex);
                    s.pointer.display0[lo] = value;
                    return s;
                }

            }
        } else {
            // empty vector, just insert single element at the back
            Object[] elems = new Object[32];
            elems[31] = value;
            Vector<E> s = new Vector<E>(31, 32, 0);
            s.pointer.depth = 1;
            s.pointer.display0 = elems;
            return s;
        }
    }

    @NotNull
    @Override
    public Vector<E> append(E value) {
        if (endIndex != startIndex) {
            int blockIndex = endIndex & ~31;
            int lo = endIndex & 31;

            if (endIndex != blockIndex) {
                Vector<E> s = new Vector<E>(startIndex, endIndex + 1, blockIndex);
                s.pointer.initFrom(pointer);
                s.dirty = dirty;
                s.gotoPosWritable(focus, blockIndex, focus ^ blockIndex);
                s.pointer.display0[lo] = value;
                return s;
            } else {
                int shift = startIndex & ~((1 << 5 * (pointer.depth - 1)) - 1);
                int shiftBlocks = startIndex >>> 5 * (pointer.depth - 1);

                if (shift != 0) {
                    if (pointer.depth > 1) {
                        int newBlockIndex = blockIndex - shift;
                        int newFocus = focus - shift;
                        Vector<E> s = new Vector<E>(startIndex - shift, endIndex + 1 - shift, newBlockIndex);
                        s.pointer.initFrom(pointer);
                        s.dirty = dirty;
                        s.shiftTopLevel(shiftBlocks, 0); // shift left by n blocks
                        s.gotoFreshPosWritable(newFocus, newBlockIndex, newFocus ^ newBlockIndex);
                        s.pointer.display0[lo] = value;
                        return s;
                    } else {
                        int newBlockIndex = blockIndex - 32;
                        int newFocus = focus;

                        Vector<E> s = new Vector<E>(startIndex - shift, endIndex + 1 - shift, newBlockIndex);
                        s.pointer.initFrom(pointer);
                        s.dirty = dirty;
                        s.shiftTopLevel(shiftBlocks, 0); // shift right by n elements
                        s.gotoPosWritable(newFocus, newBlockIndex, newFocus ^ newBlockIndex);
                        s.pointer.display0[32 - shift] = value;
                        return s;
                    }
                } else {
                    int newFocus = focus;

                    Vector<E> s = new Vector<E>(startIndex, endIndex + 1, blockIndex);
                    s.pointer.initFrom(pointer);
                    s.dirty = dirty;
                    s.gotoFreshPosWritable(newFocus, blockIndex, newFocus ^ blockIndex);
                    s.pointer.display0[lo] = value;
                    //assert(s.depth == depth+1) might or might not create new level!
                    return s;
                }
            }
        } else {
            Object[] elems = new Object[32];
            elems[0] = value;
            Vector<E> s = new Vector<E>(0, 1, 0);
            s.pointer.depth = 1;
            s.pointer.display0 = elems;
            return s;
        }
    }

    private void shiftTopLevel(int oldLeft, int newLeft) {
        switch (pointer.depth - 1) {
            case 0:
                pointer.display0 = pointer.copyRange(pointer.display0, oldLeft, newLeft);
                break;
            case 1:
                pointer.display1 = pointer.copyRange(pointer.display1, oldLeft, newLeft);
                break;
            case 2:
                pointer.display2 = pointer.copyRange(pointer.display2, oldLeft, newLeft);
                break;
            case 3:
                pointer.display3 = pointer.copyRange(pointer.display3, oldLeft, newLeft);
                break;
            case 4:
                pointer.display4 = pointer.copyRange(pointer.display4, oldLeft, newLeft);
                break;
            case 5:
                pointer.display5 = pointer.copyRange(pointer.display5, oldLeft, newLeft);
                break;
            default:
        }
    }

    private void zeroLeft(Object[] array, int index) {
        int i = 0;
        while (i < index) {
            array[i] = null;
            i += 1;
        }
    }

    private void zeroRight(Object[] array, int index) {
        int i = index;
        while (i < array.length) {
            array[i] = null;
            i += 1;
        }
    }

    private Object[] copyLeft(Object[] array, int right) {
        Object[] a2 = new Object[array.length];
        System.arraycopy(array, 0, a2, 0, right);
        return a2;
    }

    private Object[] copyRight(Object[] array, int left) {
        Object[] a2 = new Object[array.length];
        System.arraycopy(array, left, a2, left, a2.length - left);
        return a2;
    }

    private void preClean(int depth) {
        pointer.depth = depth;

        switch (depth - 1) {
            case 0:
                pointer.display1 = null;
                pointer.display2 = null;
                pointer.display3 = null;
                pointer.display4 = null;
                pointer.display5 = null;
                break;
            case 1:
                pointer.display2 = null;
                pointer.display3 = null;
                pointer.display4 = null;
                pointer.display5 = null;
                break;
            case 2:
                pointer.display3 = null;
                pointer.display4 = null;
                pointer.display5 = null;
                break;
            case 3:
                pointer.display4 = null;
                pointer.display5 = null;
                break;
            case 4:
                pointer.display5 = null;
                break;
            default:
        }
    }

    // requires structure is at index cutIndex and writable at level 0
    private void cleanLeftEdge(int cutIndex) {
        if (cutIndex < (1 << 5)) {
            zeroLeft(pointer.display0, cutIndex);
        } else if (cutIndex < (1 << 10)) {
            zeroLeft(pointer.display0, cutIndex & 0x1f);
            pointer.display1 = copyRight(pointer.display1, (cutIndex >>> 5));
        } else if (cutIndex < (1 << 15)) {
            zeroLeft(pointer.display0, cutIndex & 0x1f);
            pointer.display1 = copyRight(pointer.display1, (cutIndex >>> 5) & 0x1f);
            pointer.display2 = copyRight(pointer.display2, (cutIndex >>> 10));
        } else if (cutIndex < (1 << 20)) {
            zeroLeft(pointer.display0, cutIndex & 0x1f);
            pointer.display1 = copyRight(pointer.display1, (cutIndex >>> 5) & 0x1f);
            pointer.display2 = copyRight(pointer.display2, (cutIndex >>> 10) & 0x1f);
            pointer.display3 = copyRight(pointer.display3, (cutIndex >>> 15));
        } else if (cutIndex < (1 << 25)) {
            zeroLeft(pointer.display0, cutIndex & 0x1f);
            pointer.display1 = copyRight(pointer.display1, (cutIndex >>> 5) & 0x1f);
            pointer.display2 = copyRight(pointer.display2, (cutIndex >>> 10) & 0x1f);
            pointer.display3 = copyRight(pointer.display3, (cutIndex >>> 15) & 0x1f);
            pointer.display4 = copyRight(pointer.display4, (cutIndex >>> 20));
        } else if (cutIndex < (1 << 30)) {
            zeroLeft(pointer.display0, cutIndex & 0x1f);
            pointer.display1 = copyRight(pointer.display1, (cutIndex >>> 5) & 0x1f);
            pointer.display2 = copyRight(pointer.display2, (cutIndex >>> 10) & 0x1f);
            pointer.display3 = copyRight(pointer.display3, (cutIndex >>> 15) & 0x1f);
            pointer.display4 = copyRight(pointer.display4, (cutIndex >>> 20) & 0x1f);
            pointer.display5 = copyRight(pointer.display5, (cutIndex >>> 25));
        } else {
            throw new IllegalArgumentException();
        }
    }

    // requires structure is writable and at index cutIndex
    private void cleanRightEdge(int cutIndex) {
        // we're actually sitting one block left if cutIndex lies on a block boundary
        // this means that we'll end up erasing the whole block!!

        if (cutIndex <= (1 << 5)) {
            zeroRight(pointer.display0, cutIndex);
        } else if (cutIndex <= (1 << 10)) {
            zeroRight(pointer.display0, ((cutIndex - 1) & 0x1f) + 1);
            pointer.display1 = copyLeft(pointer.display1, (cutIndex >>> 5));
        } else if (cutIndex <= (1 << 15)) {
            zeroRight(pointer.display0, ((cutIndex - 1) & 0x1f) + 1);
            pointer.display1 = copyLeft(pointer.display1, (((cutIndex - 1) >>> 5) & 0x1f) + 1);
            pointer.display2 = copyLeft(pointer.display2, (cutIndex >>> 10));
        } else if (cutIndex <= (1 << 20)) {
            zeroRight(pointer.display0, ((cutIndex - 1) & 0x1f) + 1);
            pointer.display1 = copyLeft(pointer.display1, (((cutIndex - 1) >>> 5) & 0x1f) + 1);
            pointer.display2 = copyLeft(pointer.display2, (((cutIndex - 1) >>> 10) & 0x1f) + 1);
            pointer.display3 = copyLeft(pointer.display3, (cutIndex >>> 15));
        } else if (cutIndex <= (1 << 25)) {
            zeroRight(pointer.display0, ((cutIndex - 1) & 0x1f) + 1);
            pointer.display1 = copyLeft(pointer.display1, (((cutIndex - 1) >>> 5) & 0x1f) + 1);
            pointer.display2 = copyLeft(pointer.display2, (((cutIndex - 1) >>> 10) & 0x1f) + 1);
            pointer.display3 = copyLeft(pointer.display3, (((cutIndex - 1) >>> 15) & 0x1f) + 1);
            pointer.display4 = copyLeft(pointer.display4, (cutIndex >>> 20));
        } else if (cutIndex <= (1 << 30)) {
            zeroRight(pointer.display0, ((cutIndex - 1) & 0x1f) + 1);
            pointer.display1 = copyLeft(pointer.display1, (((cutIndex - 1) >>> 5) & 0x1f) + 1);
            pointer.display2 = copyLeft(pointer.display2, (((cutIndex - 1) >>> 10) & 0x1f) + 1);
            pointer.display3 = copyLeft(pointer.display3, (((cutIndex - 1) >>> 15) & 0x1f) + 1);
            pointer.display4 = copyLeft(pointer.display4, (((cutIndex - 1) >>> 20) & 0x1f) + 1);
            pointer.display5 = copyLeft(pointer.display5, (cutIndex >>> 25));
        } else {
            throw new IllegalArgumentException();
        }
    }

    private int requiredDepth(int xor) {
        if (xor < (1 << 5)) return 1;
        else if (xor < (1 << 10)) return 2;
        else if (xor < (1 << 15)) return 3;
        else if (xor < (1 << 20)) return 4;
        else if (xor < (1 << 25)) return 5;
        else if (xor < (1 << 30)) return 6;
        else throw new IllegalArgumentException();
    }

    private Vector<E> dropFront0(int cutIndex) {
        int blockIndex = cutIndex & ~31;

        int xor = cutIndex ^ (endIndex - 1);
        int d = requiredDepth(xor);
        int shift = (cutIndex & ~((1 << (5 * d)) - 1));

        // need to init with full display iff going to cutIndex requires swapping block at level >= d
        Vector<E> s = new Vector<E>(cutIndex - shift, endIndex - shift, blockIndex - shift);
        s.pointer.initFrom(pointer);
        s.dirty = dirty;
        s.gotoPosWritable(focus, blockIndex, focus ^ blockIndex);
        s.preClean(d);
        s.cleanLeftEdge(cutIndex - shift);
        return s;
    }

    private Vector<E> dropBack0(int cutIndex) {
        int blockIndex = (cutIndex - 1) & ~31;

        int xor = startIndex ^ (cutIndex - 1);
        int d = requiredDepth(xor);
        int shift = (startIndex & ~((1 << (5 * d)) - 1));

        Vector<E> s = new Vector<E>(startIndex - shift, cutIndex - shift, blockIndex - shift);
        s.pointer.initFrom(pointer);
        s.dirty = dirty;
        s.gotoPosWritable(focus, blockIndex, focus ^ blockIndex);
        s.preClean(d);
        s.cleanRightEdge(cutIndex - shift);
        return s;
    }
}

class VectorIterator<E> extends VectorPointer<E> implements Iterator<E> {
    private int blockIndex;
    private int lo;
    private final int endIndex;
    private int endLo;
    private boolean _hasNext;

    VectorIterator(int _startIndex, int _endIndex) {
        blockIndex = _startIndex & ~31;
        lo = _startIndex & 31;
        endIndex = _endIndex;
        endLo = Math.min(endIndex - blockIndex, 32);
        _hasNext = blockIndex + lo < endIndex;
    }

    @Override
    public boolean hasNext() {
        return _hasNext;
    }

    @Override
    public E next() {
        if (!_hasNext) throw new NoSuchElementException("reached iterator end");

        @SuppressWarnings("unchecked")
        E res = (E) display0[lo];
        lo += 1;

        if (lo == endLo) {
            if (blockIndex + lo < endIndex) {
                int newBlockIndex = blockIndex + 32;
                gotoNextBlockStart(newBlockIndex, blockIndex ^ newBlockIndex);

                blockIndex = newBlockIndex;
                endLo = Math.min(endIndex - blockIndex, 32);
                lo = 0;
            } else {
                _hasNext = false;
            }
        }

        return res;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}


class VectorPointer<E> {
    int depth = 0;
    Object[] display0 = null;
    Object[] display1 = null;
    Object[] display2 = null;
    Object[] display3 = null;
    Object[] display4 = null;
    Object[] display5 = null;

    // used
    public void initFrom(VectorPointer<E> that) {
        initFrom(that, that.depth);
    }

    public void initFrom(VectorPointer<E> that, int depth) {
        this.depth = depth;

        switch (depth - 1) {
            case -1:
                break;
            case 0:
                display0 = that.display0;
                break;
            case 1:
                display1 = that.display1;
                display0 = that.display0;
                break;
            case 2:
                display2 = that.display2;
                display1 = that.display1;
                display0 = that.display0;
                break;
            case 3:
                display3 = that.display3;
                display2 = that.display2;
                display1 = that.display1;
                display0 = that.display0;
                break;
            case 4:
                display4 = that.display4;
                display3 = that.display3;
                display2 = that.display2;
                display1 = that.display1;
                display0 = that.display0;
                break;
            case 5:
                display5 = that.display5;
                display4 = that.display4;
                display3 = that.display3;
                display2 = that.display2;
                display1 = that.display1;
                display0 = that.display0;
                break;
            default:
        }
    }


    // requires structure is at pos oldIndex = xor ^ index
    @SuppressWarnings("unchecked")
    public E getElem(int index, int xor) {
        if (xor < (1 << 5)) { // level = 0
            return (E) display0[index & 31];
        } else if (xor < (1 << 10)) { // level = 1
            return (E) ((Object[]) display1[(index >> 5) & 31])[index & 31];
        } else if (xor < (1 << 15)) { // level = 2
            return (E) ((Object[]) ((Object[]) display2[(index >> 10) & 31])[(index >> 5) & 31])[index & 31];
        } else if (xor < (1 << 20)) { // level = 3
            return (E) ((Object[]) ((Object[]) ((Object[]) display3[(index >> 15) & 31])[(index >> 10) & 31])[(index >> 5) & 31])[index & 31];
        } else if (xor < (1 << 25)) { // level = 4
            return (E) ((Object[]) ((Object[]) ((Object[]) ((Object[]) display4[(index >> 20) & 31])[(index >> 15) & 31])[(index >> 10) & 31])[(index >> 5) & 31])[index & 31];
        } else if (xor < (1 << 30)) { // level = 5
            return (E) ((Object[]) ((Object[]) ((Object[]) ((Object[]) ((Object[]) display5[(index >> 25) & 31])[(index >> 20) & 31])[(index >> 15) & 31])[(index >> 10) & 31])[(index >> 5) & 31])[index & 31];
        } else { // level = 6
            throw new IllegalArgumentException();
        }
    }

    // go to specific position
    // requires structure is at pos oldIndex = xor ^ index,
    // ensures structure is at pos index
    public void gotoPos(int index, int xor) {
        //noinspection StatementWithEmptyBody
        if (xor < (1 << 5)) { // level = 0 (could maybe removed)
        } else if (xor < (1 << 10)) { // level = 1
            display0 = (Object[]) display1[(index >> 5) & 31];
        } else if (xor < (1 << 15)) { // level = 2
            display1 = (Object[]) display2[(index >> 10) & 31];
            display0 = (Object[]) display1[(index >> 5) & 31];
        } else if (xor < (1 << 20)) { // level = 3
            display2 = (Object[]) display3[(index >> 15) & 31];
            display1 = (Object[]) display2[(index >> 10) & 31];
            display0 = (Object[]) display1[(index >> 5) & 31];
        } else if (xor < (1 << 25)) { // level = 4
            display3 = (Object[]) display4[(index >> 20) & 31];
            display2 = (Object[]) display3[(index >> 15) & 31];
            display1 = (Object[]) display2[(index >> 10) & 31];
            display0 = (Object[]) display1[(index >> 5) & 31];
        } else if (xor < (1 << 30)) { // level = 5
            display4 = (Object[]) display5[(index >> 25) & 31];
            display3 = (Object[]) display4[(index >> 20) & 31];
            display2 = (Object[]) display3[(index >> 15) & 31];
            display1 = (Object[]) display2[(index >> 10) & 31];
            display0 = (Object[]) display1[(index >> 5) & 31];
        } else { // level = 6
            throw new IllegalArgumentException();
        }
    }


    // USED BY ITERATOR
    // xor: oldIndex ^ index
    public void gotoNextBlockStart(int index, int xor) { // goto block start pos
        if (xor < (1 << 10)) { // level = 1
            display0 = (Object[]) display1[(index >> 5) & 31];
        } else if (xor < (1 << 15)) { // level = 2
            display1 = (Object[]) display2[(index >> 10) & 31];
            display0 = (Object[]) display1[0];
        } else if (xor < (1 << 20)) { // level = 3
            display2 = (Object[]) display3[(index >> 15) & 31];
            display1 = (Object[]) display2[0];
            display0 = (Object[]) display1[0];
        } else if (xor < (1 << 25)) { // level = 4
            display3 = (Object[]) display4[(index >> 20) & 31];
            display2 = (Object[]) display3[0];
            display1 = (Object[]) display2[0];
            display0 = (Object[]) display1[0];
        } else if (xor < (1 << 30)) { // level = 5
            display4 = (Object[]) display5[(index >> 25) & 31];
            display3 = (Object[]) display4[0];
            display2 = (Object[]) display3[0];
            display1 = (Object[]) display2[0];
            display0 = (Object[]) display1[0];
        } else { // level = 6
            throw new IllegalArgumentException();
        }
    }

    // USED BY BUILDER
    // xor: oldIndex ^ index
    public void gotoNextBlockStartWritable(int index, int xor) { // goto block start pos
        if (xor < (1 << 10)) { // level = 1
            if (depth == 1) {
                display1 = new Object[32];
                display1[0] = display0;
                depth += 1;
            }
            display0 = new Object[32];
            display1[(index >> 5) & 31] = display0;
        } else if (xor < (1 << 15)) { // level = 2
            if (depth == 2) {
                display2 = new Object[32];
                display2[0] = display1;
                depth += 1;
            }
            display0 = new Object[32];
            display1 = new Object[32];
            display1[(index >> 5) & 31] = display0;
            display2[(index >> 10) & 31] = display1;
        } else if (xor < (1 << 20)) { // level = 3
            if (depth == 3) {
                display3 = new Object[32];
                display3[0] = display2;
                depth += 1;
            }
            display0 = new Object[32];
            display1 = new Object[32];
            display2 = new Object[32];
            display1[(index >> 5) & 31] = display0;
            display2[(index >> 10) & 31] = display1;
            display3[(index >> 15) & 31] = display2;
        } else if (xor < (1 << 25)) { // level = 4
            if (depth == 4) {
                display4 = new Object[32];
                display4[0] = display3;
                depth += 1;
            }
            display0 = new Object[32];
            display1 = new Object[32];
            display2 = new Object[32];
            display3 = new Object[32];
            display1[(index >> 5) & 31] = display0;
            display2[(index >> 10) & 31] = display1;
            display3[(index >> 15) & 31] = display2;
            display4[(index >> 20) & 31] = display3;
        } else if (xor < (1 << 30)) { // level = 5
            if (depth == 5) {
                display5 = new Object[32];
                display5[0] = display4;
                depth += 1;
            }
            display0 = new Object[32];
            display1 = new Object[32];
            display2 = new Object[32];
            display3 = new Object[32];
            display4 = new Object[32];
            display1[(index >> 5) & 31] = display0;
            display2[(index >> 10) & 31] = display1;
            display3[(index >> 15) & 31] = display2;
            display4[(index >> 20) & 31] = display3;
            display5[(index >> 25) & 31] = display4;
        } else { // level = 6
            throw new IllegalArgumentException();
        }
    }

    // STUFF BELOW USED BY APPEND / UPDATE
    public Object[] copyOf(Object[] a) {
        Object[] b = new Object[a.length];
        System.arraycopy(a, 0, b, 0, a.length);
        return b;
    }

    public Object[] nullSlotAndCopy(Object[] array, int index) {
        Object x = array[index];
        array[index] = null;
        return copyOf((Object[]) x);
    }

    // make sure there is no aliasing
    // requires structure is at pos index
    // ensures structure is clean and at pos index and writable at all levels except 0
    public void stabilize(int index) {
        switch (depth - 1) {
            case 5:
                display5 = copyOf(display5);
                display4 = copyOf(display4);
                display3 = copyOf(display3);
                display2 = copyOf(display2);
                display1 = copyOf(display1);
                display5[(index >> 25) & 31] = display4;
                display4[(index >> 20) & 31] = display3;
                display3[(index >> 15) & 31] = display2;
                display2[(index >> 10) & 31] = display1;
                display1[(index >> 5) & 31] = display0;
                break;
            case 4:
                display4 = copyOf(display4);
                display3 = copyOf(display3);
                display2 = copyOf(display2);
                display1 = copyOf(display1);
                display4[(index >> 20) & 31] = display3;
                display3[(index >> 15) & 31] = display2;
                display2[(index >> 10) & 31] = display1;
                display1[(index >> 5) & 31] = display0;
                break;
            case 3:
                display3 = copyOf(display3);
                display2 = copyOf(display2);
                display1 = copyOf(display1);
                display3[(index >> 15) & 31] = display2;
                display2[(index >> 10) & 31] = display1;
                display1[(index >> 5) & 31] = display0;
                break;
            case 2:
                display2 = copyOf(display2);
                display1 = copyOf(display1);
                display2[(index >> 10) & 31] = display1;
                display1[(index >> 5) & 31] = display0;
                break;
            case 1:
                display1 = copyOf(display1);
                display1[(index >> 5) & 31] = display0;
                break;
            case 0:
                break;
        }
    }


    /// USED IN UPDATE AND APPEND BACK
    // prepare for writing at an existing position

    // requires structure is clean and at pos oldIndex = xor ^ newIndex,
    // ensures structure is dirty and at pos newIndex and writable at level 0
    public void gotoPosWritable0(int newIndex) {
        switch (depth - 1) {
            case 5:
                display5 = copyOf(display5);
                display4 = nullSlotAndCopy(display5, (newIndex >> 25) & 31);
                display3 = nullSlotAndCopy(display4, (newIndex >> 20) & 31);
                display2 = nullSlotAndCopy(display3, (newIndex >> 15) & 31);
                display1 = nullSlotAndCopy(display2, (newIndex >> 10) & 31);
                display0 = nullSlotAndCopy(display1, (newIndex >> 5) & 31);
                break;
            case 4:
                display4 = copyOf(display4);
                display3 = nullSlotAndCopy(display4, (newIndex >> 20) & 31);
                display2 = nullSlotAndCopy(display3, (newIndex >> 15) & 31);
                display1 = nullSlotAndCopy(display2, (newIndex >> 10) & 31);
                display0 = nullSlotAndCopy(display1, (newIndex >> 5) & 31);
                break;
            case 3:
                display3 = copyOf(display3);
                display2 = nullSlotAndCopy(display3, (newIndex >> 15) & 31);
                display1 = nullSlotAndCopy(display2, (newIndex >> 10) & 31);
                display0 = nullSlotAndCopy(display1, (newIndex >> 5) & 31);
                break;
            case 2:
                display2 = copyOf(display2);
                display1 = nullSlotAndCopy(display2, (newIndex >> 10) & 31);
                display0 = nullSlotAndCopy(display1, (newIndex >> 5) & 31);
                break;
            case 1:
                display1 = copyOf(display1);
                display0 = nullSlotAndCopy(display1, (newIndex >> 5) & 31);
                break;
            case 0:
                display0 = copyOf(display0);
                break;
            default:
        }
    }

    // requires structure is dirty and at pos oldIndex,
    // ensures structure is dirty and at pos newIndex and writable at level 0
    public void gotoPosWritable1(int oldIndex, int newIndex, int xor) {
        if (xor < (1 << 5)) { // level = 0
            display0 = copyOf(display0);
        } else if (xor < (1 << 10)) { // level = 1
            display1 = copyOf(display1);
            display1[(oldIndex >> 5) & 31] = display0;
            display0 = nullSlotAndCopy(display1, (newIndex >> 5) & 31);
        } else if (xor < (1 << 15)) { // level = 2
            display1 = copyOf(display1);
            display2 = copyOf(display2);
            display1[(oldIndex >> 5) & 31] = display0;
            display2[(oldIndex >> 10) & 31] = display1;
            display1 = nullSlotAndCopy(display2, (newIndex >> 10) & 31);
            display0 = nullSlotAndCopy(display1, (newIndex >> 5) & 31);
        } else if (xor < (1 << 20)) { // level = 3
            display1 = copyOf(display1);
            display2 = copyOf(display2);
            display3 = copyOf(display3);
            display1[(oldIndex >> 5) & 31] = display0;
            display2[(oldIndex >> 10) & 31] = display1;
            display3[(oldIndex >> 15) & 31] = display2;
            display2 = nullSlotAndCopy(display3, (newIndex >> 15) & 31);
            display1 = nullSlotAndCopy(display2, (newIndex >> 10) & 31);
            display0 = nullSlotAndCopy(display1, (newIndex >> 5) & 31);
        } else if (xor < (1 << 25)) { // level = 4
            display1 = copyOf(display1);
            display2 = copyOf(display2);
            display3 = copyOf(display3);
            display4 = copyOf(display4);
            display1[(oldIndex >> 5) & 31] = display0;
            display2[(oldIndex >> 10) & 31] = display1;
            display3[(oldIndex >> 15) & 31] = display2;
            display4[(oldIndex >> 20) & 31] = display3;
            display3 = nullSlotAndCopy(display4, (newIndex >> 20) & 31);
            display2 = nullSlotAndCopy(display3, (newIndex >> 15) & 31);
            display1 = nullSlotAndCopy(display2, (newIndex >> 10) & 31);
            display0 = nullSlotAndCopy(display1, (newIndex >> 5) & 31);
        } else if (xor < (1 << 30)) { // level = 5
            display1 = copyOf(display1);
            display2 = copyOf(display2);
            display3 = copyOf(display3);
            display4 = copyOf(display4);
            display5 = copyOf(display5);
            display1[(oldIndex >> 5) & 31] = display0;
            display2[(oldIndex >> 10) & 31] = display1;
            display3[(oldIndex >> 15) & 31] = display2;
            display4[(oldIndex >> 20) & 31] = display3;
            display5[(oldIndex >> 25) & 31] = display4;
            display4 = nullSlotAndCopy(display5, (newIndex >> 25) & 31);
            display3 = nullSlotAndCopy(display4, (newIndex >> 20) & 31);
            display2 = nullSlotAndCopy(display3, (newIndex >> 15) & 31);
            display1 = nullSlotAndCopy(display2, (newIndex >> 10) & 31);
            display0 = nullSlotAndCopy(display1, (newIndex >> 5) & 31);
        } else { // level = 6
            throw new IllegalArgumentException();
        }
    }

    // USED IN DROP
    public Object[] copyRange(Object[] array, int oldLeft, int newLeft) {
        Object[] elems = new Object[32];
        System.arraycopy(array, oldLeft, elems, newLeft, 32 - Math.max(newLeft, oldLeft));
        return elems;
    }

    // USED IN APPEND
    // create a new block at the bottom level (and possibly nodes on its path) and prepares for writing
    // requires structure is clean and at pos oldIndex,
    // ensures structure is dirty and at pos newIndex and writable at level 0
    public void gotoFreshPosWritable0(int oldIndex, int newIndex, int xor) { // goto block start pos
        //noinspection StatementWithEmptyBody
        if (xor < (1 << 5)) { // level = 0
        } else if (xor < (1 << 10)) { // level = 1
            if (depth == 1) {
                display1 = new Object[32];
                display1[(oldIndex >> 5) & 31] = display0;
                depth += 1;
            }
            display0 = new Object[32];
        } else if (xor < (1 << 15)) { // level = 2
            if (depth == 2) {
                display2 = new Object[32];
                display2[(oldIndex >> 10) & 31] = display1;
                depth += 1;
            }
            display1 = (Object[]) display2[(newIndex >> 10) & 31];
            if (display1 == null) display1 = new Object[32];
            display0 = new Object[32];
        } else if (xor < (1 << 20)) { // level = 3
            if (depth == 3) {
                display3 = new Object[32];
                display3[(oldIndex >> 15) & 31] = display2;
                display2 = new Object[32];
                display1 = new Object[32];
                depth += 1;
            }
            display2 = (Object[]) display3[(newIndex >> 15) & 31];
            if (display2 == null) display2 = new Object[32];
            display1 = (Object[]) display2[(newIndex >> 10) & 31];
            if (display1 == null) display1 = new Object[32];
            display0 = new Object[32];
        } else if (xor < (1 << 25)) { // level = 4
            if (depth == 4) {
                display4 = new Object[32];
                display4[(oldIndex >> 20) & 31] = display3;
                display3 = new Object[32];
                display2 = new Object[32];
                display1 = new Object[32];
                depth += 1;
            }
            display3 = (Object[]) display4[(newIndex >> 20) & 31];
            if (display3 == null) display3 = new Object[32];
            display2 = (Object[]) display3[(newIndex >> 15) & 31];
            if (display2 == null) display2 = new Object[32];
            display1 = (Object[]) display2[(newIndex >> 10) & 31];
            if (display1 == null) display1 = new Object[32];
            display0 = new Object[32];
        } else if (xor < (1 << 30)) { // level = 5
            if (depth == 5) {
                display5 = new Object[32];
                display5[(oldIndex >> 25) & 31] = display4;
                display4 = new Object[32];
                display3 = new Object[32];
                display2 = new Object[32];
                display1 = new Object[32];
                depth += 1;
            }
            display4 = (Object[]) display5[(newIndex >> 25) & 31];
            if (display4 == null) display4 = new Object[32];
            display3 = (Object[]) display4[(newIndex >> 20) & 31];
            if (display3 == null) display3 = new Object[32];
            display2 = (Object[]) display3[(newIndex >> 15) & 31];
            if (display2 == null) display2 = new Object[32];
            display1 = (Object[]) display2[(newIndex >> 10) & 31];
            if (display1 == null) display1 = new Object[32];
            display0 = new Object[32];
        } else { // level = 6
            throw new IllegalArgumentException();
        }
    }

    // requires structure is dirty and at pos oldIndex,
    // ensures structure is dirty and at pos newIndex and writable at level 0
    public void gotoFreshPosWritable1(int oldIndex, int newIndex, int xor) {
        stabilize(oldIndex);
        gotoFreshPosWritable0(oldIndex, newIndex, xor);
    }
}

class VectorBuilder<E> extends AbstractBuilder<E, Vector<E>> {
    private final VectorPointer<E> pointer = new VectorPointer<E>();

    int blockIndex = 0;
    int lo = 0;

    // possible alternative: start with display0 = null, blockIndex = -32, lo = 32
    // to avoid allocating initial array if the result will be empty anyways

    VectorBuilder() {
        pointer.display0 = new Object[32];
        pointer.depth = 1;
    }

    @NotNull
    @Override
    public VectorBuilder<E> add(E elem) {
        if (lo >= pointer.display0.length) {
            int newBlockIndex = blockIndex + 32;
            pointer.gotoNextBlockStartWritable(newBlockIndex, blockIndex ^ newBlockIndex);
            blockIndex = newBlockIndex;
            lo = 0;
        }
        pointer.display0[lo] = elem;
        lo += 1;
        return this;
    }

    @NotNull
    @Override
    public Vector<E> doBuild() {
        int size = blockIndex + lo;
        if (size == 0)
            return Vector.empty();
        Vector<E> s = new Vector<E>(0, size, 0); // should focus front or back?
        s.pointer.initFrom(pointer);
        if (pointer.depth > 1) s.pointer.gotoPos(0, size - 1); // we're currently focused to size - 1, not size!
        return s;
    }
}