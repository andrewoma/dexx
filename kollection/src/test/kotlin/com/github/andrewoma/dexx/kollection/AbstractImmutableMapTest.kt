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

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

abstract class AbstractImmutableMapTest {

    abstract fun <K : Comparable<K>, V> map(elements: List<Pair<K, V>>): ImmutableMap<K, V>

    fun pairs(vararg keys: Int) = keys.map { it to ('a' + it - 1).toString() }

    fun map(vararg keys: Int): ImmutableMap<Int, String> = map(pairs(*keys))

    @Test fun `should support immutable semantics`() {
        val map1 = map(1, 2, 3)
        val map2 = map1 + (4 to "d")
        val map3 = map1 - 1
        val map4 = map1.put(4, "d")

        assertThat(map1).isEqualTo(map(1, 2, 3))
        assertThat(map2).isEqualTo(map(1, 2, 3, 4))
        assertThat(map3).isEqualTo(map(2, 3))
        assertThat(map4).isEqualTo(map(1, 2, 3, 4))
    }

    @Test fun `should support equals and hashCode`() {
        assertThat(map<Int, String>(listOf())).isEqualTo(map<Int, String>(listOf()))
        assertThat(map(1)).isEqualTo(map(1))
        assertThat(map(1, 2, 3)).isEqualTo(map(1, 2, 3))
        assertThat(map(1, 2, 3)).isNotEqualTo(map(2, 3, 4))
        assertThat(map(listOf(1 to null))).isNotEqualTo(map(listOf(1 to "a")))

        assertThat(map<Int, String>(listOf()).hashCode()).isEqualTo(map<Int, String>(listOf()).hashCode())
        assertThat(map(1).hashCode()).isEqualTo(map(1).hashCode())
        assertThat(map(1, 2, 3).hashCode()).isEqualTo(map(1, 2, 3).hashCode())
        assertThat(map(1, 2, 3).hashCode()).isNotEqualTo(map(2, 3, 4).hashCode())
    }

    @Test fun `should equal kotlin maps`() {
        assertThat(map(1, 2, 3)).isEqualTo(pairs(1, 2, 3).toMap())
        assertThat(pairs(1, 2, 3).toMap()).isEqualTo(map(1, 2, 3))
    }

    @Test fun `should convert to and from kotlin collections`() {
        assertThat(pairs(1, 2, 3).toImmutableMap()).isEqualTo(map(1, 2, 3))
        assertThat(LinkedHashMap(map(1, 2, 3))).isEqualTo(pairs(1, 2, 3).toMap())
    }

    @Test fun `should support bulk operations`() {
        assertThat(map(1, 2, 3) + map(4, 5)).isEqualTo(map(1, 2, 3, 4, 5))
        assertThat(map(1, 2, 3) - setOf(1, 2)).isEqualTo(map(3))
    }

    @Test fun `should support contains`() {
        assertThat(map(1, 2, 3).containsKey(1)).isTrue()
        assertThat(map(1, 2, 3).containsKey(4)).isFalse()

        assertThat(map(1, 2, 3).containsValue("c")).isTrue()
        assertThat(map(1, 2, 3).containsValue("d")).isFalse()
    }

    @Test fun `should support size`() {
        assertThat(map<Int, String>(listOf()).size).isEqualTo(0)
        assertThat(map<Int, String>(listOf()).isEmpty()).isTrue()

        assertThat(map(1).size).isEqualTo(1)
        assertThat(map(1).isEmpty()).isFalse()

        assertThat(map(1, 2, 3).size).isEqualTo(3)
        assertThat(map(1, 2, 3).isEmpty()).isFalse()
    }

    @Test fun `should iterate values`() {
        assertThat(map(1, 2, 3).values.toSet()).isEqualTo(setOf("a", "b", "c"))
    }

    @Test fun `should iterate keys`() {
        assertThat(map(1, 2, 3).keys).isEqualTo(setOf(1, 2, 3))
    }

    @Test fun `should iterate entries`() {
        assertThat(map(1, 2, 3).entries.map { it.key to it.value }.toSet()).isEqualTo(pairs(1, 2, 3).toSet())
    }

    @Test fun `should support variance in value`() {
        val map: ImmutableMap<Int, String> = map(1, 2, 3)
        val mapAny: ImmutableMap<Int, Any> = map
        assertThat(mapAny).isEqualTo(map)
    }
}
