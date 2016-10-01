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

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * {@code ArrayLists} is the preferred method of constructing instances of {@code ArrayList}.
 * <p/>
 * <p>{@link com.github.andrewoma.dexx.collection.ArrayList} is currently constructed in
 * all cases, however this may change in the future.
 * <p/>
 * <p>{@code ArrayLists} is preferred for construction as:
 * <ul>
 * <li>It works better in languages that support type inference
 * </ul>
 */
public class ArrayLists {
	private ArrayLists() {
	}

	@NotNull
	@SuppressWarnings("unchecked")
	public static <E> IndexedList<E> of(E... elements) {
		return copyOf(elements);
	}

	@NotNull
	public static <E> IndexedList<E> copyOf(java.lang.Iterable<E> iterable) {
		Builder<E, ArrayList<E>> builder = ArrayList.<E>factory().newBuilder();
		for (E e : iterable) {
			builder.add(e);
		}
		return builder.build();
	}

	@NotNull
	public static <E> IndexedList<E> copyOf(Iterator<E> iterator) {
		Builder<E, ArrayList<E>> builder = ArrayList.<E>factory().newBuilder();
		while (iterator.hasNext()) {
			builder.add(iterator.next());
		}
		return builder.build();
	}

	@NotNull
	public static <E> IndexedList<E> copyOf(E[] es) {
		Object[] copy = new Object[es.length];
		System.arraycopy(es, 0, copy, 0, es.length);

		return new ArrayList<E>(copy);
	}

	@NotNull
	public static <E> IndexedList<E> copyOfTraversable(Traversable<E> traversable) {
		@SuppressWarnings("unchecked") final
		Builder<E, ArrayList<E>> builder = ArrayList.<E>factory().newBuilder();
		traversable.forEach(new Function<E, Object>() {
			@Override
			public Object invoke(E e) {
				builder.add(e);
				return null;
			}
		});

		return builder.build();
	}

	@NotNull
	@SuppressWarnings("unchecked")
	public static <E> BuilderFactory<E, IndexedList<E>> factory() {
		return (BuilderFactory) ArrayList.<E>factory();
	}

	@NotNull
	public static <E> Builder<E, IndexedList<E>> builder() {
		return ArrayLists.<E>factory().newBuilder();
	}
}
