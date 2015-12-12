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

import com.github.andrewoma.dexx.collection.Vector

interface ImmutableList<out E> : List<E> {

    operator fun plus(value: @UnsafeVariance E): ImmutableList<E>

    operator fun plus(values: Iterable<@UnsafeVariance E>): ImmutableList<E>

    fun drop(n: Int): ImmutableList<E>

    fun dropLast(n: Int): ImmutableList<E>

    fun take(n: Int): ImmutableList<E>

    fun takeLast(n: Int): ImmutableList<E>

    override fun subList(fromIndex: Int, toIndex: Int): ImmutableList<E>
}

internal class ListAdapter<E>(val underlying: Vector<E>) : ImmutableList<E> {

    override val size: Int
        get() = underlying.size()

    override fun isEmpty() = underlying.isEmpty

    override fun contains(element: E) = underlying.contains(element)

    override fun iterator(): Iterator<E> = underlying.iterator()

    override fun containsAll(elements: Collection<E>) = underlying.asList().containsAll(elements)

    override fun get(index: Int) = underlying.get(index)

    override fun indexOf(element: E) = underlying.indexOf(element)

    override fun lastIndexOf(element: E) = underlying.lastIndexOf(element)

    override fun listIterator() = underlying.asList().listIterator()

    override fun listIterator(index: Int) = underlying.asList().listIterator(index)

    override fun plus(value: E) = ListAdapter(underlying.append(value))

    override fun plus(values: Iterable<E>) = ListAdapter(values.fold(underlying) { r, e -> r.append(e) })

    override fun drop(n: Int) = ListAdapter(underlying.drop(n))

    override fun dropLast(n: Int) = ListAdapter(underlying.take((size - n).coerceAtLeast(0)))

    override fun take(n: Int) = ListAdapter(underlying.take(n))

    override fun takeLast(n: Int): ImmutableList<E> =
            ListAdapter(underlying.range((size - n).coerceAtLeast(0), true, size, false))

    override fun subList(fromIndex: Int, toIndex: Int) = ListAdapter(underlying.range(fromIndex, true, toIndex, false))

    override fun toString() = this.joinToString(", ", "ImmutableList(", ")")

    override fun equals(other: Any?) = this === other || (other is List<*> && contentsEquals(other))

    private fun contentsEquals(other: List<*>): Boolean {
        val iterator = iterator()
        val otherIterator = other.iterator()

        while (iterator.hasNext() && otherIterator.hasNext()) {
            val elem = iterator.next()
            val otherElem = otherIterator.next()
            if (!(if (elem == null) otherElem == null else elem == otherElem))
                return false
        }

        return !(iterator.hasNext() || otherIterator.hasNext())
    }

    override fun hashCode(): Int = underlying.hashCode()
}

// Construction
fun <E> immutableListOf(vararg elements: E): ImmutableList<E>
        = ListAdapter(elements.fold(Vector.empty<E>()) { r, e -> r.append(e) })

// Conversion from Iterables
fun <E> Iterable<E>.toImmutableList(): ImmutableList<E>
        = immutableListOf<E>() + this

// Conversion from Sequences
fun <E> Sequence<E>.toImmutableList(): ImmutableList<E>
        = immutableListOf<E>() + this.asIterable()

