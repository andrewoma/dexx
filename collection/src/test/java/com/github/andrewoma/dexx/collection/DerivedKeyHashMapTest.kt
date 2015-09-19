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

package com.github.andrewoma.dexx.collection

import com.github.andrewoma.dexx.collection.internal.base.AbstractMap
import com.github.andrewoma.dexx.collection.internal.builder.AbstractSelfBuilder
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

/**
 *
 */
class DerivedKeyHashMapTest : AbstractMapTest(supportsNullValues = false) {
    fun <K, V> empty() = DerivedKeyHashMap.factory<K, Pair<K, V>> { it.component1()!! }.newBuilder().build()

    @Suppress("BASE_WITH_NULLABLE_UPPER_BOUND", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    inner class WrappedDerivedKeyHashMap<K, V>(val underlying: DerivedKeyHashMap<K, Pair<K, V?>> = empty()) : AbstractMap<K, V>() {

        override fun put(key: K, value: V?): Map<K, V> {
            return WrappedDerivedKeyHashMap(underlying.put(key, Pair(key, value)))
        }

        override fun get(key: K): V? {
            return underlying.get(key)?.component2()
        }

        override fun remove(key: K): Map<K, V> {
            return WrappedDerivedKeyHashMap(underlying.remove(key))
        }

        override fun iterator(): MutableIterator<Pair<K, V?>> {
            val i = underlying.iterator()

            return object : MutableIterator<Pair<K, V?>> {
                override fun remove() = i.remove()
                override fun next() = i.next().component2()
                override fun hasNext() = i.hasNext()
            }
        }

        override fun <U> forEach(f: Function<Pair<K, V?>, U>) {
            underlying.forEach { f.invoke(it.component2()) }
        }

        override fun containsKey(key: K): Boolean {
            return underlying.containsKey(key)
        }
    }

    override fun <K, V> mapFactory(comparator: Comparator<in K>?): BuilderFactory<Pair<K, V>, out Map<K, V>> {
        return object : BuilderFactory<Pair<K, V>, Map<K, V>> {
            override fun newBuilder(): Builder<Pair<K, V>, Map<K, V>> {
                return object : AbstractSelfBuilder<Pair<K, V>, Map<K, V>>(WrappedDerivedKeyHashMap<K, V>(empty())) {
                    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
                    override fun add(element: Pair<K, V>?): Builder<Pair<K, V>, Map<K, V>> {
                        result = result!!.put(element!!.component1()!!, element.component2()!!)
                        return this
                    }
                }
            }
        }
    }

    @Test fun explicitForEach() {
        var map = DerivedKeyHashMap(keyFunction)
        map = map.put("1", ClassWithKey("1", "A"))
        map = map.put("2", ClassWithKey("2", "B"))

        val actual = java.util.HashSet<String>()
        val f = object : Function<Pair<String, DerivedKeyHashMapTest.ClassWithKey>, Unit> {
            @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
            override fun invoke(parameter: Pair<String, DerivedKeyHashMapTest.ClassWithKey>?) {
                actual.add(parameter!!.component1()!!)
            }
        }
        map.forEach(f)
        assertEquals(setOf("1", "2"), actual)
    }

    @Test fun directBuilding() {
        val builder = DerivedKeyHashMap.factory(keyFunction).newBuilder()
        val map = builder.add(Pair("1", ClassWithKey("1", "A"))).build()

        assertEquals(1, map.size())
        assertEquals(ClassWithKey("1", "A"), map["1"])
    }

    @Test fun withCustomClass() {
        var map = DerivedKeyHashMap.factory(keyFunction).newBuilder().build()
        map = map.put("1", ClassWithKey("1", "A"))
        map = map.put("2", ClassWithKey("2", "B"))
        map = map.put("3", ClassWithKey("3", "C"))
        assertEquals(3, map.size())
        assertEquals(ClassWithKey("1", "A"), map["1"])
        assertEquals(ClassWithKey("2", "B"), map["2"])
        assertEquals(ClassWithKey("3", "C"), map["3"])
    }

    @Test fun withIdentityKeyFunction() {
        var map = DerivedKeyHashMap.factory<ClassForIdentityKey, ClassForIdentityKey>(IdentityKeyFunction()).newBuilder().build()
        val o1 = ClassForIdentityKey("1", "2")
        map = map.put(o1, o1)
        val o2 = ClassForIdentityKey("1", "3")
        map = map.put(o2, o2)
        assertEquals(1, map.size())
        assertEquals(Pair(o2, o2), map.iterator().next())
    }

    data class ClassWithKey(val key: String, val value: String)

    val keyFunction = object : KeyFunction<String, ClassWithKey> {
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
        override fun key(value: ClassWithKey): String {
            return value.key
        }
    }

    class ClassForIdentityKey(val key: String, val value: String) {
        override fun equals(other: Any?): Boolean = other is ClassForIdentityKey && other.key == key
        override fun hashCode(): Int = key.hashCode()
    }
}
