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


import com.github.andrewoma.dexx.collection.Function;
import com.github.andrewoma.dexx.collection.Map;
import com.github.andrewoma.dexx.collection.Pair;
import com.github.andrewoma.dexx.collection.internal.adapter.MapAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMap<K, V> extends AbstractIterable<Pair<K, V>> implements Map<K, V> {
    @NotNull
    @Override
    public com.github.andrewoma.dexx.collection.Iterable<K> keys() {
        return new MappedIterable<K, Pair<K, V>>(this, new Function<Pair<K, V>, K>() {
            @Override
            public K invoke(Pair<K, V> pair) {
                return pair.component1();
            }
        });
    }

    @NotNull
    @Override
    public com.github.andrewoma.dexx.collection.Iterable<V> values() {
        return new MappedIterable<V, Pair<K, V>>(this, new Function<Pair<K, V>, V>() {
            @Override
            public V invoke(Pair<K, V> pair) {
                return pair.component2();
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Map))
            return false;

        Map<K, V> m = (Map<K, V>) o;
        if (m.size() != size())
            return false;

        try {
            for (Pair<K, V> pair : this) {
                K key = pair.component1();
                V value = pair.component2();
                if (value == null) {
                    if (m.get(key) != null || !m.containsKey(key))
                        return false;
                } else {
                    if (!value.equals(m.get(key)))
                        return false;
                }
            }
        } catch (ClassCastException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for (Pair<K, V> pair : this) {
            h += 31 * pair.hashCode();
        }
        return h;
    }

    @NotNull
    @Override
    public java.util.Map<K, V> asMap() {
        return new MapAdapter<K, V>(this);
    }
}
