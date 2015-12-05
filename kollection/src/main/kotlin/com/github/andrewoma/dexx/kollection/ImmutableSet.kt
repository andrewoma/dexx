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

interface ImmutableSet<E : Any> : Set<E> {

    operator fun plus(value: E): ImmutableSet<E>

    operator fun plus(values: Iterable<E>): ImmutableSet<E>

    operator fun minus(value: E): ImmutableSet<E>

    operator fun minus(values: Iterable<E>): ImmutableSet<E>
}

internal class ImmutableHashSet<E : Any>(val underlying: HashSet<E> = HashSet.empty()) : ImmutableSet<E> {

    override val size: Int
        get() = underlying.size()

    override fun isEmpty() = underlying.isEmpty

    override fun contains(element: E) = underlying.contains(element)

    override fun iterator(): Iterator<E> = underlying.iterator()

    override fun containsAll(elements: Collection<E>) = underlying.asSet().containsAll(elements)

    override fun plus(value: E) = ImmutableHashSet(underlying.add(value))

    override fun plus(values: Iterable<E>) = ImmutableHashSet(values.fold(underlying) { r, e -> r.add(e) })

    override fun minus(value: E) = ImmutableHashSet(underlying.remove(value))

    override fun minus(values: Iterable<E>): ImmutableSet<E> = ImmutableHashSet(values.fold(underlying) { r, e -> r.remove(e) })

    override fun toString() = this.joinToString(", ", "ImmutableSet(", ")")

    override fun equals(other: Any?) = this === other || (other is Set<*> && this.size == other.size && this.containsAll(other))

    override fun hashCode(): Int = underlying.hashCode()
}

fun <E : Any> immutableSetOf(vararg elements: E): ImmutableSet<E> =
        ImmutableHashSet(elements.fold(HashSet.empty<E>()) { r, e -> r.add(e) })

fun <E : Any> Iterable<E>.toImmutableSet() = immutableSetOf<E>() + this


