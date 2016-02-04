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

import com.github.andrewoma.dexx.collection.HashSet
import com.github.andrewoma.dexx.collection.TreeSet
import java.util.*
import kotlin.comparisons.compareValuesBy
import com.github.andrewoma.dexx.collection.Set as DSet
import com.github.andrewoma.dexx.collection.SortedSet as DSortedSet

interface ImmutableSet<out E : Any> : Set<E> {

    operator fun plus(value: @UnsafeVariance E): ImmutableSet<E>

    operator fun plus(values: Iterable<@UnsafeVariance E>): ImmutableSet<E>

    operator fun minus(value: @UnsafeVariance E): ImmutableSet<E>

    operator fun minus(values: Iterable<@UnsafeVariance E>): ImmutableSet<E>
}

internal class SetAdapter<E : Any>(val underlying: DSet<E>) : ImmutableSet<E> {

    override val size: Int
        get() = underlying.size()

    override fun isEmpty() = underlying.isEmpty

    override fun contains(element: E) = underlying.contains(element)

    override fun iterator(): Iterator<E> = underlying.iterator()

    override fun containsAll(elements: Collection<E>) = underlying.asSet().containsAll(elements)

    override fun plus(value: E) = SetAdapter(underlying.add(value))

    override fun plus(values: Iterable<E>) = SetAdapter(values.fold(underlying) { r, e -> r.add(e) })

    override fun minus(value: E) = SetAdapter(underlying.remove(value))

    override fun minus(values: Iterable<E>): ImmutableSet<E> = SetAdapter(values.fold(underlying) { r, e -> r.remove(e) })

    override fun toString(): String {
        val prefix = "${if (underlying is DSortedSet<*>) "ImmutableSortedSet" else "ImmutableSet"}("
        return this.joinToString(", ", prefix, ")")
    }

    override fun equals(other: Any?) = this === other || (other is Set<*> && this.size == other.size && this.containsAll(other))

    override fun hashCode(): Int = underlying.hashCode()
}

// Construction
fun <E : Any> immutableSetOf(vararg elements: E): ImmutableSet<E>
        = SetAdapter(elements.fold(HashSet.empty<E>()) { r, e -> r.add(e) })

fun <E : Comparable<E>> immutableSortedSetOf(vararg elements: E): ImmutableSet<E>
        = SetAdapter(elements.fold(TreeSet.empty<E>()) { r, e -> r.add(e) })

fun <E : Any> immutableCustomSortedSetOf(selector: (E) -> Comparable<*>?, vararg elements: E): ImmutableSet<E> {
    val ordering = Comparator<E> { e1, e2 -> compareValuesBy(e1, e2, selector) }
    return SetAdapter(elements.fold(TreeSet(ordering)) { r, e -> r.add(e) })
}

// Conversion from Iterables
fun <E : Any> Iterable<E>.toImmutableSet(): ImmutableSet<E>
        = immutableSetOf<E>() + this

fun <E : Comparable<E>> Iterable<E>.toImmutableSortedSet(): ImmutableSet<E>
        = immutableSortedSetOf<E>() + this

fun <E : Any> Iterable<E>.toImmutableSortedSet(selector: (E) -> Comparable<*>?): ImmutableSet<E>
        = immutableCustomSortedSetOf(selector) + this

// Conversion from Sequences
fun <E : Any> Sequence<E>.toImmutableSet(): ImmutableSet<E>
        = immutableSetOf<E>() + this.asIterable()

fun <E : Comparable<E>> Sequence<E>.toImmutableSortedSet(): ImmutableSet<E>
        = immutableSortedSetOf<E>() + this.asIterable()

fun <E : Any> Sequence<E>.toImmutableSortedSet(selector: (E) -> Comparable<*>?): ImmutableSet<E>
        = immutableCustomSortedSetOf(selector) + this.asIterable()

