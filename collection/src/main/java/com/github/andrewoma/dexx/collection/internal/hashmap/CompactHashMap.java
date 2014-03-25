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

package com.github.andrewoma.dexx.collection.internal.hashmap;

import com.github.andrewoma.dexx.collection.Function;
import com.github.andrewoma.dexx.collection.KeyFunction;
import com.github.andrewoma.dexx.collection.Pair;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 *
 */
public class CompactHashMap<K, V> {
    protected static final CompactHashMap EMPTY = new CompactHashMap();

    public Iterator<Pair<K, V>> iterator(KeyFunction<K, V> kf) {
        return Collections.<Pair<K, V>>emptyList().iterator();
    }

    public int size() {
        return 0;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> CompactHashMap<K, V> empty() {
        return EMPTY;
    }

    public <U> void forEach(Function<Pair<K, V>, U> f, KeyFunction<K, V> keyFunction) {
    }

    public V get(K key, KeyFunction<K, V> keyFunction) {
        return get0(key, computeHash(key), 0, keyFunction);
    }

    public CompactHashMap<K, V> put(K key, V value, KeyFunction<K, V> keyFunction) {
        assert key.equals(keyFunction.key(value));
        return updated0(key, computeHash(key), 0, value, null, keyFunction);
    }

    public CompactHashMap<K, V> remove(K key, KeyFunction<K, V> keyFunction) {
        return removed0(key, computeHash(key), 0, keyFunction);
    }

    protected int elemHashCode(K key) {
        return key.hashCode();
    }

    protected final int improve(int hashCode) {
        int h = hashCode + ~(hashCode << 9);
        h = h ^ (h >>> 14);
        h = h + (h << 4);
        return h ^ (h >>> 10);
    }

    protected int computeHash(K key) {
        return improve(elemHashCode(key));
    }

    protected V get0(K key, int hash, int level, KeyFunction<K, V> keyFunction) {
        return null;
    }

    protected CompactHashMap<K, V> updated0(K key, int hash, int level, V value, Pair<K, V> kv, KeyFunction<K, V> keyFunction) {
        return new HashMap1<K, V>(key, hash, value, kv);
    }

    protected CompactHashMap<K, V> removed0(K key, int hash, int level, KeyFunction<K, V> keyFunction) {
        return this;
    }

    // utility method to create a HashTrieMap from two leaf CompactHashMaps (HashMap1 or CompactHashMapCollision1) with non-colliding hash code)
    protected static <K, V> HashTrieMap<K, V> makeHashTrieMap(int hash0, CompactHashMap<K, V> elem0, int hash1, CompactHashMap<K, V> elem1, int level, int size) {
        int index0 = (hash0 >>> level) & 0x1f;
        int index1 = (hash1 >>> level) & 0x1f;
        if (index0 != index1) {
            int bitmap = (1 << index0) | (1 << index1);
            @SuppressWarnings("unchecked")
            Object[] elems = new Object[2];
            if (index0 < index1) {
                elems[0] = unwrap(elem0);
                elems[1] = unwrap(elem1);
            } else {
                elems[0] = unwrap(elem1);
                elems[1] = unwrap(elem0);
            }
            return new HashTrieMap<K, V>(bitmap, elems, size);
        } else {
            @SuppressWarnings("unchecked")
            Object[] elems = new Object[1];
            int bitmap = (1 << index0);
            elems[0] = makeHashTrieMap(hash0, elem0, hash1, elem1, level + 5, size);
            return new HashTrieMap<K, V>(bitmap, elems, size);
        }
    }

    protected static <K, V> Object unwrap(CompactHashMap<K, V> hashMap) {
        if (hashMap instanceof HashMap1) {
            return ((HashMap1<K, V>) hashMap).value;
        } else {
            return hashMap;
        }
    }

    @SuppressWarnings("unchecked")
    protected CompactHashMap<K, V> wrap(Object object, KeyFunction<K, V> keyFunction) {
        if (object instanceof CompactHashMap) {
            return ((CompactHashMap<K, V>) object);
        } else {
            // Instead of storing HashMap1s, recreate on the fly ...
            V value = (V) object;
            K key = keyFunction.key(value);
            return new HashMap1<K, V>(key, computeHash(key), value, null);
        }
    }
}

class HashMap1<K, V> extends CompactHashMap<K, V> {
    protected final K key;
    protected final int hash;
    protected final V value;
    protected Pair<K, V> kv;

    HashMap1(K key, int hash, V value, Pair<K, V> kv) {
        this.key = key;
        this.hash = hash;
        this.value = value;
        this.kv = kv;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    protected V get0(K key, int hash, int level, KeyFunction<K, V> keyFunction) {
        return hash == this.hash && key.equals(this.key) ? value : null;
    }

    @Override
    protected CompactHashMap<K, V> updated0(K key, int hash, int level, V value, Pair<K, V> kv, KeyFunction<K, V> keyFunction) {
        if (hash == this.hash && key.equals(this.key)) {
            return new HashMap1<K, V>(key, hash, value, kv);
        } else {
            if (hash != this.hash) {
                // they have different hashes, but may collide at this level - find a level at which they don't
                HashMap1<K, V> that = new HashMap1<K, V>(key, hash, value, kv);
                return makeHashTrieMap(this.hash, this, hash, that, level, 2);
            } else {
                // 32-bit hash collision (rare, but not impossible)
                return new HashMapCollision1<K, V>(hash, ListMap.<K, V>empty().put(this.key, this.value).put(key, value));
            }
        }
    }

    @Override
    protected CompactHashMap<K, V> removed0(K key, int hash, int level, KeyFunction<K, V> keyFunction) {
        return hash == this.hash && key.equals(this.key) ? CompactHashMap.<K, V>empty() : this;
    }

    @Override
    public <U> void forEach(Function<Pair<K, V>, U> f, KeyFunction<K, V> keyFunction) {
        f.invoke(new Pair<K, V>(key, value));
    }

    private Pair<K, V> ensureKv() {
        if (kv == null) {
            kv = new Pair<K, V>(key, value);
        }
        return kv;
    }

    @Override
    public Iterator<Pair<K, V>> iterator(KeyFunction<K, V> kf) {
        return Collections.singleton(ensureKv()).iterator();
    }
}

class HashMapCollision1<K, V> extends CompactHashMap<K, V> {
    private final int hash;
    private final ListMap<K, V> kvs;

    HashMapCollision1(int hash, ListMap<K, V> kvs) {
        this.hash = hash;
        this.kvs = kvs;
    }

    @Override
    public int size() {
        return kvs.size();
    }

    @Override
    protected V get0(K key, int hash, int level, KeyFunction<K, V> keyFunction) {
        return hash == this.hash ? kvs.get(key) : null;
    }

    @Override
    protected CompactHashMap<K, V> updated0(K key, int hash, int level, V value, Pair<K, V> kv, KeyFunction<K, V> keyFunction) {
        if (hash == this.hash) {
            return new HashMapCollision1<K, V>(hash, kvs.put(key, value));
        } else {
            HashMap1<K, V> that = new HashMap1<K, V>(key, hash, value, kv);
            return makeHashTrieMap(this.hash, this, hash, that, level, size() + 1);
        }
    }

    @Override
    protected CompactHashMap<K, V> removed0(K key, int hash, int level, KeyFunction<K, V> keyFunction) {
        if (hash == this.hash) {
            ListMap<K, V> kvs1 = kvs.remove(key);
            if (kvs1.isEmpty()) {
                return CompactHashMap.empty();
            } else if (kvs1.tail().isEmpty()) {
                Pair<K, V> kv = kvs1.iterator().next();
                return new HashMap1<K, V>(kv.component1(), hash, kv.component2(), kv);
            } else
                return new HashMapCollision1<K, V>(hash, kvs1);
        } else {
            return this;
        }
    }

    @Override
    public <U> void forEach(Function<Pair<K, V>, U> f, KeyFunction<K, V> keyFunction) {
        for (Pair<K, V> kv : kvs) {
            f.invoke(kv);
        }
    }

    @Override
    public Iterator<Pair<K, V>> iterator(KeyFunction<K, V> kf) {
        return kvs.iterator();
    }
}

class HashTrieMap<K, V> extends CompactHashMap<K, V> {
    private final int bitmap;
    private final Object[] elems;
    private final int size;

    HashTrieMap(int bitmap, Object[] elems, int size) {
        this.bitmap = bitmap;
        this.elems = elems;
        this.size = size;
    }

    @Override
    public int size() {
        return size;
    }

    Object[] getElems() {
        return elems;
    }

    CompactHashMap<K, V> getElem(int index, KeyFunction<K, V> keyFunction) {
        return wrap(elems[index], keyFunction);
    }

    @Override
    protected V get0(K key, int hash, int level, KeyFunction<K, V> keyFunction) {
        int index = (hash >>> level) & 0x1f;
        int mask = (1 << index);
        if (bitmap == -1) {
            return getElem(index & 0x1f, keyFunction).get0(key, hash, level + 5, keyFunction);
        } else if ((bitmap & mask) != 0) {
            int offset = Integer.bitCount(bitmap & (mask - 1));
            return getElem(offset, keyFunction).get0(key, hash, level + 5, keyFunction);
        } else {
            return null;
        }
    }

    @Override
    protected CompactHashMap<K, V> updated0(K key, int hash, int level, V value, Pair<K, V> kv, KeyFunction<K, V> keyFunction) {
        int index = (hash >>> level) & 0x1f;
        int mask = (1 << index);
        int offset = Integer.bitCount(bitmap & (mask - 1));
        if ((bitmap & mask) != 0) {
            CompactHashMap<K, V> sub = getElem(offset, keyFunction);
            CompactHashMap<K, V> subNew = sub.updated0(key, hash, level + 5, value, kv, keyFunction);
            if (subNew.equals(sub)) {
                return this;
            } else {
                @SuppressWarnings("unchecked")
                Object[] elemsNew = new Object[elems.length];
                System.arraycopy(elems, 0, elemsNew, 0, elems.length);
                elemsNew[offset] = unwrap(subNew);
                return new HashTrieMap<K, V>(bitmap, elemsNew, size + (subNew.size() - sub.size()));
            }
        } else {
            @SuppressWarnings("unchecked")
            Object[] elemsNew = new Object[elems.length + 1];
            System.arraycopy(elems, 0, elemsNew, 0, offset);
            elemsNew[offset] = value;
            System.arraycopy(elems, offset, elemsNew, offset + 1, elems.length - offset);
            return new HashTrieMap<K, V>(bitmap | mask, elemsNew, size + 1);
        }
    }

    @Override
    protected CompactHashMap<K, V> removed0(K key, int hash, int level, KeyFunction<K, V> keyFunction) {
        int index = (hash >>> level) & 0x1f;
        int mask = (1 << index);
        int offset = Integer.bitCount(bitmap & (mask - 1));
        if ((bitmap & mask) != 0) {
            CompactHashMap<K, V> sub = getElem(offset, keyFunction);

            CompactHashMap<K, V> subNew = sub.removed0(key, hash, level + 5, keyFunction);
            if (subNew.equals(sub)) {
                return this;
            } else if (subNew.size() == 0) {
                int bitmapNew = bitmap ^ mask;
                if (bitmapNew != 0) {
                    @SuppressWarnings("unchecked")
                    Object[] elemsNew = new Object[elems.length - 1];
                    System.arraycopy(elems, 0, elemsNew, 0, offset);
                    System.arraycopy(elems, offset + 1, elemsNew, offset, elems.length - offset - 1);
                    int sizeNew = size - sub.size();
                    if (elemsNew.length == 1 && !(elemsNew[0] instanceof HashTrieMap)) {
                        return wrap(elemsNew[0], keyFunction);
                    } else {
                        return new HashTrieMap<K, V>(bitmapNew, elemsNew, sizeNew);
                    }
                } else {
                    return CompactHashMap.empty();
                }
            } else if (elems.length == 1 && !(subNew instanceof HashTrieMap)) {
                return subNew;
            } else {
                @SuppressWarnings("unchecked")
                Object[] elemsNew = new Object[elems.length];
                System.arraycopy(elems, 0, elemsNew, 0, elems.length);
                elemsNew[offset] = unwrap(subNew);
                int sizeNew = size + (subNew.size() - sub.size());
                return new HashTrieMap<K, V>(bitmap, elemsNew, sizeNew);
            }
        } else {
            return this;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <U> void forEach(Function<Pair<K, V>, U> f, KeyFunction<K, V> keyFunction) {
        for (Object elem : elems) {
            if (elem instanceof CompactHashMap) {
                ((CompactHashMap<K, V>) elem).forEach(f, keyFunction);
            } else {
                V value = (V) elem;
                f.invoke(new Pair<K, V>(keyFunction.key(value), value));
            }
        }
    }

    @Override
    public Iterator<Pair<K, V>> iterator(KeyFunction<K, V> kf) {
        return new CompactHashMapIterator<K, V>(elems, kf);
    }
}

class CompactHashMapIterator<K, V> implements Iterator<Pair<K, V>> {
    private final KeyFunction<K, V> keyFunction;
    private final Stack<Pointer> stack = new Stack<Pointer>();

    private Iterator<Pair<K, V>> subIterator;
    private Pointer current;
    private Pair<K, V> next;

    CompactHashMapIterator(Object[] elems, KeyFunction<K, V> keyFunction) {
        current = new Pointer(elems, 0);
        this.keyFunction = keyFunction;
        computeNext();
    }

    @SuppressWarnings("unchecked")
    private void computeNext() {
        next = null;

        if (subIterator != null) {
            if (subIterator.hasNext()) {
                next = subIterator.next();
                return;
            } else {
                subIterator = null;
            }
        }

        while (next == null) {
            if (current.pos == current.objects.length) {
                // Exhausted current array, try the stack
                if (stack.isEmpty()) {
                    return;
                } else {
                    current = stack.pop();
                }
            } else {
                Object object = current.objects[current.pos++];
                if (object instanceof HashTrieMap) {
                    stack.push(current);
                    current = new Pointer(((HashTrieMap) object).getElems(), 0);
                } else if (object instanceof HashMapCollision1) {
                    subIterator = ((HashMapCollision1) object).iterator(keyFunction);
                    next = subIterator.next();
                } else {
                    V leaf = (V) object;
                    next = new Pair<K, V>(keyFunction.key(leaf), leaf);
                }
            }
        }
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public Pair<K, V> next() {
        if (next == null) {
            throw new NoSuchElementException();
        }

        Pair<K, V> result = next;
        computeNext();
        return result;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    static class Pointer {
        final Object[] objects;
        int pos;

        Pointer(Object[] objects, int pos) {
            this.objects = objects;
            this.pos = pos;
        }
    }
}

