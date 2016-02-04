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

/**
 * Dexx collections are a port of Scala's immutable, persistent collection classes to pure Java.
 * <p/>
 * <p>Persistent in the context of functional data structures means the data structure preserves
 * the previous version of itself when modified. This means any reference to a collection is effectively immutable.
 * However, modifications can be made by returning a new version of the data structure, leaving the original
 * structure unchanged.
 * <p/>
 * <p>The following diagram shows the key interfaces (blue) and classes (green) in this package:
 * <p><img alt="Dexx collections overview" src="https://github.com/andrewoma/dexx/raw/master/docs/dexxcollections.png">
 * <p/>
 * <p><b>Usage Notes:</b>
 * <ul>
 * <li>Each of the collection types has an associated companion class (plural form) that is the
 * preferred method of construction. e.g. To create a {@link com.github.andrewoma.dexx.collection.Set},
 * use the {@link com.github.andrewoma.dexx.collection.Sets} class.
 * <p/>
 * <li>Many of the collections have the same name as their mutable {@code java.util} counterparts, however
 * they are not directly related. This is due to the interfaces being fundamentally incompatible as
 * operations the "modify" collections must return a new instance for persistent collections.
 * <p/>
 * <li>Collections can be viewed as their {@code java.util} counterpart by using {@code as...()} methods.
 * e.g. {@link com.github.andrewoma.dexx.collection.Set#asSet()}. Such views are immutable.
 * <p/>
 * <li>Collections can be constructed from {@code java.util} collections by using the {@code copyOf(...)}
 * methods in the companion classes. e.g. {@link com.github.andrewoma.dexx.collection.Sets#copyOf(java.lang.Iterable)}.
 * <p/>
 * <li>While operations on collections often return a new instance that reflects the modifications,
 * this is not a guarantee. e.g. Attempting to remove an element from a {@code Set} that does not exist
 * may return the same collection as no modifications were required.
 * </ul>
 * <p/>
 * <p>See the <a href="https://github.com/andrewoma/dexx">project site</a> for further examples and information.
 */
package com.github.andrewoma.dexx.collection;
