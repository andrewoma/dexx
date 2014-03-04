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

import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNull
import com.github.andrewoma.dexx.collection.AbstractMapTest.WrappedBuilderFactory
import org.junit.Test as test
import com.github.andrewoma.dexx.collection.internal.builder.AbstractBuilder
import kotlin.test.assertFalse

abstract class AbstractMapTest(val supportsNullValues: Boolean = true) : AbstractIterableTest() {
    abstract fun <K, V> mapFactory(): BuilderFactory<Pair<K, V>, out Map<K, V>>

    protected fun <K, V> buildMap_(vararg entries: kotlin.Pair<K, V>): Map<K, V> {
        val builder = mapFactory<K, V>().newBuilder()
        for (e in entries) builder.add(Pair(e.first, e.second))
        return builder.result()
    }

    private fun <K, V> buildMap(vararg entries: kotlin.Pair<K, V>): Map<K, V> = buildMap_(*entries)

    override fun <T> factory(): BuilderFactory<T, out Iterable<T>> {
        return WrappedBuilderFactory(mapFactory())
    }

    class WrappedBuilderFactory<T>(val f: BuilderFactory<Pair<T, T>, out Map<T, T>>) : BuilderFactory<T, Iterable<T>> {
        override fun newBuilder(): Builder<T, Iterable<T>> {
            return WrappedBuilder(f.newBuilder(), this)
        }
    }

    class WrappedBuilder<T : Any>(val builder: Builder<Pair<T, T>, out Map<T, T>>, val factory: BuilderFactory<T, Iterable<T>>) : AbstractBuilder<T, Iterable<T>>() {
        [suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")]
        override fun add(p0: T?): Builder<T, Iterable<T>> {
            builder.add(Pair(p0, p0))
            return this
        }

        override fun clear(): Builder<T, Iterable<T>> {
            builder.clear()
            return this
        }

        override fun result(): Iterable<T> {
            val iterable = WrappedMapIterable(builder.result())
            return iterable
        }
    }

    test fun putGetRemove() {
        var map = buildMap(1 to "A")
        assertEquals("A", map[1])

        map = map.remove(1)
        assertTrue(map.isEmpty())
    }

    test fun removeFromEmpty() {
        val map = buildMap<Int, Int>()
        assertTrue(map.remove(1).isEmpty())
    }

    test fun removeNotExists() {
        var map = buildMap(1 to "A")
        assertEquals("A", map.get(1))

        map = map.remove(2)
        assertEquals(buildMap(1 to "A"), map)
    }

    test fun removeNotExists2() {
        val map = buildMap(1 to "A", 2 to "B")
        assertEquals(buildMap(1 to "A", 2 to "B"), map.remove(3))
    }

    test fun removeSingle() {
        var map = buildMap(1 to "A")
        assertTrue(map.remove(1).isEmpty())
    }

    test fun removeUpdateSingle() {
        var map = buildMap(1 to "A")
        map = map.put(1, "A")
        assertEquals(buildMap(1 to "A"), map)
    }

    test fun getFromEmpty() {
        val map = buildMap<Int, Int>()
        assertNull(map.get(1))
    }

    test fun forEachWithPairs() {
        val pairs = setOf(1 to "A", 2 to "B", 3 to "C", 4 to "D")
        val map = buildMap(*pairs.copyToArray())
        val actual = hashSetOf<kotlin.Pair<Int, String>>()
        val f = object : Function<Pair<Int, String>, Unit> {
            [suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")]
            override fun invoke(parameter: Pair<Int, String>?) {
                actual.add(kotlin.Pair(parameter!!.component1()!!, parameter.component2()!!))
            }
        }

        map.forEach(f)
        assertEquals(pairs, actual)
    }

    test fun iteratorWithPairs() {
        val pairs = setOf(1 to "A", 2 to "B", 3 to "C", 4 to "D")
        val map = buildMap(*pairs.copyToArray())
        val actual = hashSetOf<kotlin.Pair<Int, String>>()
        map.iterator().forEach { actual.add(kotlin.Pair(it.component1()!!, it.component2()!!)) }
        assertEquals(pairs, actual)
    }

    test fun keys() {
        val pairs = setOf(1 to "A", 2 to "B", 3 to "C", 4 to "D")
        val map = buildMap(*pairs.copyToArray())
        val actual = hashSetOf<Int>()
        map.keys().forEach { actual.add(it) }
        assertEquals(pairs.map { it.first }.toSet(), actual)
    }

    test fun values() {
        val pairs = setOf(1 to "A", 2 to "B", 3 to "C", 4 to "D")
        val map = buildMap(*pairs.copyToArray())
        val actual = hashSetOf<String>()
        map.values().forEach { actual.add(it) }
        assertEquals(pairs.map { it.second }.toSet(), actual)
    }

    test fun forEachWithCollisions() {
        val pairs = setOf(CollidingKey(1, 1) to "A", CollidingKey(1, 2) to "B", CollidingKey(2, 3) to "C", CollidingKey(2, 4) to "D")
        val map = buildMap(*pairs.copyToArray())
        val actual = hashSetOf<kotlin.Pair<CollidingKey, String>>()
        map.forEach { actual.add(kotlin.Pair(it.component1()!!, it.component2()!!)) }
        assertEquals(pairs, actual)
    }

    test fun iteratorWithCollisionsWithPairs() {
        val pairs = setOf(CollidingKey(1, 1) to "A", CollidingKey(1, 2) to "B", CollidingKey(2, 3) to "C", CollidingKey(2, 4) to "D")
        val map = buildMap(*pairs.copyToArray())
        val actual = hashSetOf<kotlin.Pair<CollidingKey, String>>()
        map.iterator().forEach { actual.add(kotlin.Pair(it.component1()!!, it.component2()!!)) }
        assertEquals(pairs, actual)
    }

    test fun putRemoveCollisions() {
        var map = buildMap(CollidingKey(1, 1) to "A", CollidingKey(1, 2) to "B", CollidingKey(2, 3) to "C", CollidingKey(2, 4) to "D")
        map = map.remove(CollidingKey(1, 1)).remove(CollidingKey(1, 2)).remove(CollidingKey(2, 3)).remove(CollidingKey(2, 4))
        assertTrue(map.isEmpty())
    }

    test fun putGetRemoveMultiple() {
        var map = buildMap(1 to "A", 2 to "B", 3 to "C", 4 to "D")
        map = map.remove(1).remove(2).remove(3).remove(4)
        assertTrue(map.isEmpty())
    }

    test fun immutablity() {
        val map1 = buildMap(1 to "A")

        val map2 = map1.put(2, "B")
        val map3 = map2.put(3, "C")
        val map4 = map3.remove(1)

        assertEquals(buildMap(1 to "A"), map1)
        assertEquals(buildMap(1 to "A", 2 to "B"), map2)
        assertEquals(buildMap(1 to "A", 2 to "B", 3 to "C"), map3)
        assertEquals(buildMap(2 to "B", 3 to "C"), map4)
    }

    test fun updateSameKey() {
        var map = buildMap(1 to "A")
        map = map.put(1, "B")
        assertEquals(buildMap(1 to "B"), map)
    }

    test fun updateSameKeyWithCollision() {
        var map = buildMap(CollidingKey(1, 1) to "A")
        map = map.put(CollidingKey(1, 1), "B")
        assertEquals(buildMap(CollidingKey(1, 1) to "B"), map)
    }

    test fun collisions() {
        val map = buildMap(CollidingKey(1, 1) to "A", CollidingKey(1, 2) to "B", CollidingKey(1, 3) to "C", CollidingKey(1, 4) to "D")
        assertEquals("A", map[CollidingKey(1, 1)])
        assertEquals("B", map[CollidingKey(1, 2)])
        assertEquals("C", map[CollidingKey(1, 3)])
        assertEquals("D", map[CollidingKey(1, 4)])
    }

    test fun collisionsUpdated() {
        var map = buildMap(CollidingKey(1, 1) to "A", CollidingKey(1, 2) to "B")
        map = map.put(CollidingKey(1, 1), "C")
        map = map.put(CollidingKey(1, 2), "D")
        assertEquals("C", map[CollidingKey(1, 1)])
        assertEquals("D", map[CollidingKey(1, 2)])
    }

    test fun collisionsUpdateWithSame() {
        var map = buildMap(CollidingKey(1, 1) to "A")
        map = map.put(CollidingKey(1, 1), "A")
        assertEquals("A", map[CollidingKey(1, 1)])
    }

    test fun collisionsRemoved() {
        var map = buildMap(CollidingKey(1, 1) to "A", CollidingKey(1, 2) to "B", CollidingKey(1, 3) to "C", CollidingKey(1, 4) to "D")
        assertEquals(4, map.size())

        map = map.remove(CollidingKey(1, 1)).remove(CollidingKey(1, 2)).remove(CollidingKey(1, 3)).remove(CollidingKey(1, 4))
        assertTrue(map.isEmpty())
    }

    test fun equal() {
        assertEquals(buildMap(1 to 2, 3 to 4), buildMap(1 to 2, 3 to 4))
    }

    test fun hashCode() {
        assertEquals(buildMap(1 to 2, 3 to 4).hashCode(), buildMap(1 to 2, 3 to 4).hashCode())
    }

    test fun containsKey() {
        val map = buildMap(1 to 2, 3 to 4)
        assertTrue(map.containsKey(1))
        assertTrue(map.containsKey(3))
        assertFalse(map.containsKey(2))
        assertFalse(map.containsKey(4))
    }

    test fun emptyGet() {
        val map = buildMap<Int, Int>()
        assertNull(map.get(1))
    }

    test fun emptyRemove() {
        val map = buildMap<Int, Int>()
        assertTrue(map.remove(1).isEmpty())
    }

    test fun nullValue() {
        val map = buildMap<Int?, Int?>()
        assertNull(map.put(1, null).get(1))
    }

    test fun containsKeyNullValue() {
        val map = buildMap<Int?, Int?>()
        assertTrue(map.put(1, null).containsKey(1))
    }

    test fun equalsNonMap() {
        assertFalse(buildMap(1 to 2).equals(""))
    }

    test fun equalsWithNullValues() {
        if (!supportsNullValues) return
        assertEquals(buildMap(1 to null), buildMap(1 to null))
    }
}
