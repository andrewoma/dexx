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

import com.github.andrewoma.dexx.collection.internal.builder.AbstractBuilder
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

abstract class AbstractMapTest(val supportsNullValues: Boolean = true) : AbstractIterableTest() {
    abstract fun <K, V> mapFactory(comparator: Comparator<in K>? = null): BuilderFactory<Pair<K, V>, out Map<K, V>>

    protected fun <K, V> buildMap_(vararg entries: kotlin.Pair<K, V>): Map<K, V> {
        val builder = mapFactory<K, V>().newBuilder()
        for (e in entries) builder.add(Pair(e.first, e.second))
        return builder.build()
    }

    private fun <K, V> buildMap(vararg entries: kotlin.Pair<K, V>): Map<K, V> = buildMap_(*entries)

    override fun <T> factory(): BuilderFactory<T, out Iterable<T>> {
        return WrappedBuilderFactory<T>(mapFactory())
    }

    class WrappedBuilderFactory<T>(val f: BuilderFactory<Pair<T, T>, out Map<T, T>>) : BuilderFactory<T, Iterable<T>> {
        override fun newBuilder(): Builder<T, Iterable<T>> {
            return WrappedBuilder(f.newBuilder(), this)
        }
    }

    class WrappedBuilder<T>(val builder: Builder<Pair<T, T>, out Map<T, T>>, val factory: BuilderFactory<T, Iterable<T>>) : AbstractBuilder<T, Iterable<T>>() {
        @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE", "BASE_WITH_NULLABLE_UPPER_BOUND")
        override fun add(p0: T?): Builder<T, Iterable<T>> {
            builder.add(Pair(p0!!, p0))
            return this
        }

        override fun doBuild(): Iterable<T> {
            val iterable = WrappedMapIterable(builder.build())
            return iterable
        }
    }

    @Test fun putGetRemove() {
        var map = buildMap(1 to "A")
        assertEquals("A", map[1])

        map = map.remove(1)
        assertTrue(map.isEmpty)
    }

    @Test fun removeFromEmpty() {
        val map = buildMap<Int, Int>()
        assertTrue(map.remove(1).isEmpty)
    }

    @Test fun removeNotExists() {
        var map = buildMap(1 to "A")
        assertEquals("A", map.get(1))

        map = map.remove(2)
        assertEquals(buildMap(1 to "A"), map)
    }

    @Test fun removeNotExists2() {
        val map = buildMap(1 to "A", 2 to "B")
        assertEquals(buildMap(1 to "A", 2 to "B"), map.remove(3))
    }

    @Test fun removeSingle() {
        val map = buildMap(1 to "A")
        assertTrue(map.remove(1).isEmpty)
    }

    @Test fun removeUpdateSingle() {
        var map = buildMap(1 to "A")
        map = map.put(1, "A")
        assertEquals(buildMap(1 to "A"), map)
    }

    @Test fun getFromEmpty() {
        val map = buildMap<Int, Int>()
        assertNull(map.get(1))
    }

    @Test fun forEachWithPairs() {
        val pairs = setOf(1 to "A", 2 to "B", 3 to "C", 4 to "D")
        val map = buildMap(*pairs.toTypedArray())
        val actual = hashSetOf<kotlin.Pair<Int, String>>()
        val f = Function<Pair<Int, String>, Unit> { parameter ->
            actual.add(kotlin.Pair(parameter!!.component1()!!, parameter.component2()!!))
            actual.add(kotlin.Pair(parameter.component1()!!, parameter.component2()!!))
        }

        map.forEach(f)
        assertEquals(pairs, actual)
    }

    @Test fun iteratorWithPairs() {
        val pairs = setOf(1 to "A", 2 to "B", 3 to "C", 4 to "D")
        val map = buildMap(*pairs.toTypedArray())
        val actual = hashSetOf<kotlin.Pair<Int, String>>()
        map.forEach { actual.add(kotlin.Pair(it.component1()!!, it.component2()!!)) }
        assertEquals(pairs, actual)
    }

    @Test fun keys() {
        val pairs = setOf(1 to "A", 2 to "B", 3 to "C", 4 to "D")
        val map = buildMap(*pairs.toTypedArray())
        val actual = hashSetOf<Int>()
        map.keys().forEach { actual.add(it) }
        assertEquals(pairs.map { it.first }.toSet(), actual)
    }

    @Test fun values() {
        val pairs = setOf(1 to "A", 2 to "B", 3 to "C", 4 to "D")
        val map = buildMap(*pairs.toTypedArray())
        val actual = hashSetOf<String>()
        map.values().forEach { actual.add(it) }
        assertEquals(pairs.map { it.second }.toSet(), actual)
    }

    @Test fun forEachWithCollisions() {
        val pairs = setOf(CollidingKey(1, 1) to "A", CollidingKey(1, 2) to "B", CollidingKey(2, 3) to "C", CollidingKey(2, 4) to "D")
        val map = buildMap(*pairs.toTypedArray())
        val actual = hashSetOf<kotlin.Pair<CollidingKey, String>>()
        map.forEach { actual.add(kotlin.Pair(it.component1()!!, it.component2()!!)) }
        assertEquals(pairs, actual)
    }

    @Test fun iteratorWithCollisionsWithPairs() {
        val pairs = setOf(CollidingKey(1, 1) to "A", CollidingKey(1, 2) to "B", CollidingKey(2, 3) to "C", CollidingKey(2, 4) to "D")
        val map = buildMap(*pairs.toTypedArray())
        val actual = hashSetOf<kotlin.Pair<CollidingKey, String>>()
        map.forEach { actual.add(kotlin.Pair(it.component1()!!, it.component2()!!)) }
        assertEquals(pairs, actual)
    }

    @Test fun putRemoveCollisions() {
        var map = buildMap(CollidingKey(1, 1) to "A", CollidingKey(1, 2) to "B", CollidingKey(2, 3) to "C", CollidingKey(2, 4) to "D")
        map = map.remove(CollidingKey(1, 1)).remove(CollidingKey(1, 2)).remove(CollidingKey(2, 3)).remove(CollidingKey(2, 4))
        assertTrue(map.isEmpty)
    }

    @Test fun putGetRemoveMultiple() {
        var map = buildMap(1 to "A", 2 to "B", 3 to "C", 4 to "D")
        map = map.remove(1).remove(2).remove(3).remove(4)
        assertTrue(map.isEmpty)
    }

    @Test fun immutablity() {
        val map1 = buildMap(1 to "A")

        val map2 = map1.put(2, "B")
        val map3 = map2.put(3, "C")
        val map4 = map3.remove(1)

        assertEquals(buildMap(1 to "A"), map1)
        assertEquals(buildMap(1 to "A", 2 to "B"), map2)
        assertEquals(buildMap(1 to "A", 2 to "B", 3 to "C"), map3)
        assertEquals(buildMap(2 to "B", 3 to "C"), map4)
    }

    @Test fun updateSameKey() {
        var map = buildMap(1 to "A")
        map = map.put(1, "B")
        assertEquals(buildMap(1 to "B"), map)
    }

    @Test fun updateSameKeyWithCollision() {
        var map = buildMap(CollidingKey(1, 1) to "A")
        map = map.put(CollidingKey(1, 1), "B")
        assertEquals(buildMap(CollidingKey(1, 1) to "B"), map)
    }

    @Test fun collisions() {
        val map = buildMap(CollidingKey(1, 1) to "A", CollidingKey(1, 2) to "B", CollidingKey(1, 3) to "C", CollidingKey(1, 4) to "D")
        assertEquals("A", map[CollidingKey(1, 1)])
        assertEquals("B", map[CollidingKey(1, 2)])
        assertEquals("C", map[CollidingKey(1, 3)])
        assertEquals("D", map[CollidingKey(1, 4)])
    }

    @Test fun collisionsUpdated() {
        var map = buildMap(CollidingKey(1, 1) to "A", CollidingKey(1, 2) to "B")
        map = map.put(CollidingKey(1, 1), "C")
        map = map.put(CollidingKey(1, 2), "D")
        assertEquals("C", map[CollidingKey(1, 1)])
        assertEquals("D", map[CollidingKey(1, 2)])
    }

    @Test fun collisionsUpdateWithSame() {
        var map = buildMap(CollidingKey(1, 1) to "A")
        map = map.put(CollidingKey(1, 1), "A")
        assertEquals("A", map[CollidingKey(1, 1)])
    }

    @Test fun collisionsRemoved() {
        var map = buildMap(CollidingKey(1, 1) to "A", CollidingKey(1, 2) to "B", CollidingKey(1, 3) to "C", CollidingKey(1, 4) to "D")
        assertEquals(4, map.size())

        map = map.remove(CollidingKey(1, 1)).remove(CollidingKey(1, 2)).remove(CollidingKey(1, 3)).remove(CollidingKey(1, 4))
        assertTrue(map.isEmpty)
    }

    @Test fun equal() {
        assertEquals(buildMap(1 to 2, 3 to 4), buildMap(1 to 2, 3 to 4))
    }

    @Test fun testHashCode() {
        assertEquals(buildMap(1 to 2, 3 to 4).hashCode(), buildMap(1 to 2, 3 to 4).hashCode())
    }

    @Test fun containsKey() {
        val map = buildMap(1 to 2, 3 to 4)
        assertTrue(map.containsKey(1))
        assertTrue(map.containsKey(3))
        assertFalse(map.containsKey(2))
        assertFalse(map.containsKey(4))
    }

    @Test fun emptyGet() {
        val map = buildMap<Int, Int>()
        assertNull(map.get(1))
    }

    @Test fun emptyRemove() {
        val map = buildMap<Int, Int>()
        assertTrue(map.remove(1).isEmpty)
    }

    @Test fun nullValue() {
        val map = buildMap<Int?, Int?>()
        assertNull(map.put(1, null).get(1))
    }

    @Test fun containsKeyNullValue() {
        val map = buildMap<Int?, Int?>()
        assertTrue(map.put(1, null).containsKey(1))
    }

    @Test fun equalsNonMap() {
        assertFalse(buildMap(1 to 2).equals(""))
    }

    @Test fun equalsWithNullValues() {
        if (!supportsNullValues) return
        assertEquals(buildMap(1 to null), buildMap(1 to null))
    }

    open @Test fun putGetRemoveRandom() {
        putGetRemoveRandom(10000)
    }

    fun putGetRemoveRandom(size: Int) {
        val numbers = randomNumbers(size)
        var map = buildMap<Int, Int>()
        for (i in numbers) {
            map = map.put(i, i)
        }
        // Do it again
        for (i in numbers) {
            map = map.put(i, i)
        }
        // Do it again with new value
        for (i in numbers) {
            map = map.put(i, i + 1)
        }
        assertEquals(numbers.size, map.size())

        for (i in numbers) {
            assertEquals(i + 1, map.get(i))
        }

        for (i in numbers) {
            map = map.remove(i)
        }
        assertTrue(map.isEmpty)

        // Do it again
        for (i in numbers) {
            map = map.remove(i)
        }
        assertTrue(map.isEmpty)
    }

    fun randomNumbers(size: Int): LinkedHashSet<Int> {
        val random = Random()
        val generated = LinkedHashSet<Int>()
        while (generated.size < size) {
            generated.add(random.nextInt())
        }
        return generated
    }
}
