/*
 * Copyright (c) 2015 Andrew O'Malley
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

package com.github.andrewoma.dexx.kollection

import com.github.andrewoma.dexx.collection.HashMap
import com.github.andrewoma.dexx.collection.TreeMap
import java.util.*
import kotlin.comparisons.compareValuesBy
import com.github.andrewoma.dexx.collection.Map as DMap
import com.github.andrewoma.dexx.collection.SortedMap as DSortedMap

interface ImmutableMap<K : Any, out V> : Map<K, V> {

    fun put(key: K, value: @UnsafeVariance V): ImmutableMap<K, V>

    operator fun plus(entry: Pair<K, @UnsafeVariance V>): ImmutableMap<K, V>

    operator fun plus(entries: Iterable<Pair<K, @UnsafeVariance V>>): ImmutableMap<K, V>

    operator fun minus(key: K): ImmutableMap<K, V>

    operator fun minus(keys: Iterable<K>): ImmutableMap<K, V>
}

internal class MapAdapter<K : Any, V>(val underlying: DMap<K, V>) : ImmutableMap<K, V> {
    override val size: Int
        get() = underlying.size()

    override fun isEmpty() = underlying.isEmpty

    override fun containsKey(key: K) = underlying.containsKey(key)

    override fun containsValue(value: V) = underlying.find { it.component2() == value } != null

    override fun get(key: K) = underlying[key]

    override fun put(key: K, value: V) = MapAdapter(underlying.put(key, value))

    override val keys: Set<K>
        get() = underlying.asMap().keys

    override val values: Collection<V>
        get() = underlying.asMap().values

    override val entries: Set<Map.Entry<K, V>>
        get() = underlying.asMap().entries

    override fun plus(entry: Pair<K, V>): ImmutableMap<K, V> = MapAdapter(underlying.put(entry.first, entry.second))

    override fun minus(key: K) = MapAdapter(underlying.remove(key))

    override fun plus(entries: Iterable<Pair<K, V>>)
            = MapAdapter(entries.fold(underlying) { r, e -> r.put(e.first, e.second) })

    override fun minus(keys: Iterable<K>)
            = MapAdapter(keys.fold(underlying) { r, k -> r.remove(k) })

    override fun toString(): String {
        val prefix = "${if (underlying is DSortedMap<*, *>) "ImmutableSortedMap" else "ImmutableMap"}("
        return this.entries.joinToString(", ", prefix, ")")
    }

    override fun equals(other: Any?) = this === other || (other is Map<*, *> && this.size == other.size && contentsEquals(other))

    private fun contentsEquals(m: Map<*, *>): Boolean {
        for ((key, value) in this) {
            if (value == null) {
                if (m[key] != null || !m.containsKey(key)) return false
            } else {
                if (value != m[key]) return false
            }
        }
        return true
    }

    override fun hashCode(): Int = underlying.hashCode()
}

// Construction
fun <K : Any, V> immutableMapOf(vararg elements: Pair<K, V>): ImmutableMap<K, V>
        = MapAdapter(elements.fold(HashMap.empty<K, V>()) { r, e -> r.put(e.first, e.second) })

fun <K : Comparable<K>, V> immutableSortedMapOf(vararg elements: Pair<K, V>): ImmutableMap<K, V>
        = MapAdapter(elements.fold(TreeMap<K, V>()) { r, e -> r.put(e.first, e.second) })

fun <K : Any, V> immutableCustomSortedMapOf(selector: (K) -> Comparable<*>?, vararg elements: Pair<K, V>): ImmutableMap<K, V> {
    val ordering = Comparator<K> { e1, e2 -> compareValuesBy(e1, e2, selector) }
    return MapAdapter(elements.fold(TreeMap(ordering, null)) { r, e -> r.put(e.first, e.second) })
}

// Conversion from Iterables
fun <K : Any, V> Iterable<Pair<K, V>>.toImmutableMap(): ImmutableMap<K, V>
        = immutableMapOf<K, V>() + this

fun <K : Comparable<K>, V> Iterable<Pair<K, V>>.toImmutableSortedMap(): ImmutableMap<K, V>
        = immutableSortedMapOf<K, V>() + this

fun <K : Any, V> Iterable<Pair<K, V>>.toImmutableSortedMap(selector: (K) -> Comparable<*>?): ImmutableMap<K, V>
        = immutableCustomSortedMapOf<K, V>(selector) + this

// Conversion from Sequences
fun <K : Any, V> Sequence<Pair<K, V>>.toImmutableMap(): ImmutableMap<K, V>
        = immutableMapOf<K, V>() + this.asIterable()

fun <K : Comparable<K>, V> Sequence<Pair<K, V>>.toImmutableSortedMap(): ImmutableMap<K, V>
        = immutableSortedMapOf<K, V>() + this.asIterable()

fun <K : Any, V> Sequence<Pair<K, V>>.toImmutableSortedMap(selector: (K) -> Comparable<*>?): ImmutableMap<K, V>
        = immutableCustomSortedMapOf<K, V>(selector) + this.asIterable()

// Conversion from other Maps
fun <K : Any, V> Map<K, V>.toImmutableMap(): ImmutableMap<K, V>
        = convert(this, immutableMapOf<K, V>())

fun <K : Comparable<K>, V> Map<K, V>.toImmutableSortedMap(): ImmutableMap<K, V>
        = convert(this, immutableSortedMapOf<K, V>())

fun <K : Any, V> Map<K, V>.toImmutableSortedMap(selector: (K) -> Comparable<*>?): ImmutableMap<K, V>
        = convert(this, immutableCustomSortedMapOf<K, V>(selector))

private fun <K : Any, V> convert(from: Map<K, V>, to: ImmutableMap<K, V>)
        = from.entries.fold(to) { m, e -> m.put(e.key, e.value) }
