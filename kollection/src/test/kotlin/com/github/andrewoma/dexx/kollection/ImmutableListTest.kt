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

class ImmutableListTest {

    fun <E> list(vararg elements: E): ImmutableList<E> = immutableListOf(*elements)

    @Test fun `should support immutable semantics`() {
        val list1 = list(1, 2, 3)
        val list2 = list1 + 4
        val list3 = list1 - 1

        assertThat(list1).isEqualTo(list(1, 2, 3))
        assertThat(list2).isEqualTo(list(1, 2, 3, 4))
        assertThat(list3).isEqualTo(list(2, 3))
    }

    @Test fun `should support equals and hashCode`() {
        assertThat(list<Int>()).isEqualTo(list<Int>())
        assertThat(list(1)).isEqualTo(list(1))
        assertThat(list(1, 2, 3)).isEqualTo(list(1, 2, 3))
        assertThat(list(1, 2, 3)).isNotEqualTo(list(2, 3, 4))

        assertThat(list<Int>().hashCode()).isEqualTo(list<Int>().hashCode())
        assertThat(list(1).hashCode()).isEqualTo(list(1).hashCode())
        assertThat(list(1, 2, 3).hashCode()).isEqualTo(list(1, 2, 3).hashCode())
        assertThat(list(1, 2, 3).hashCode()).isNotEqualTo(list(2, 3, 4).hashCode())
    }

    @Test fun `should equal kotlin lists`() {
        assertThat(list(1, 2, 3)).isEqualTo(listOf(1, 2, 3))
        assertThat(listOf(1, 2, 3)).isEqualTo(list(1, 2, 3))
    }

    @Test fun `should convert to and from kotlin collections`() {
        assertThat(arrayListOf(1, 2, 3).toImmutableList()).isEqualTo(list(1, 2, 3))
        assertThat(arrayListOf(1, 2, 3).asSequence().toImmutableList()).isEqualTo(list(1, 2, 3))
        assertThat(list(1, 2, 3).toList()).isEqualTo(listOf(1, 2, 3))
    }

    @Test fun `should support bulk operations`() {
        assertThat(list(1, 2, 3) + list(4, 5)).isEqualTo(list(1, 2, 3, 4, 5))
        assertThat(list(1, 2, 3) - list(1, 2)).isEqualTo(list(3))
    }

    @Test fun `should support contains`() {
        assertThat(list(1, 2, 3).contains(1)).isTrue()
        assertThat(list(1, 2, 3).contains(4)).isFalse()

        assertThat(list(1, 2, 3).containsAll(listOf(1, 2, 3))).isTrue()
        assertThat(list(1, 2, 3).containsAll(listOf(1, 2))).isTrue()
        assertThat(list(1, 2, 3).containsAll(listOf(1, 2, 3, 4))).isFalse()
    }

    @Test fun `should support size`() {
        assertThat(list<Int>().size).isEqualTo(0)
        assertThat(list<Int>().isEmpty()).isTrue()

        assertThat(list(1).size).isEqualTo(1)
        assertThat(list(1).isEmpty()).isFalse()

        assertThat(list(1, 2, 3).size).isEqualTo(3)
        assertThat(list(1, 2, 3).isEmpty()).isFalse()
    }

    @Test fun `should iterate elements`() {
        assertThat(list(1, 2, 3).iterator().asSequence().toList())
                .isEqualTo(listOf(1, 2, 3))
    }

    @Test fun `should be produce a readable toString`() {
        assertThat(list(1, 2, 3).toString()).isEqualTo("ImmutableList(1, 2, 3)")
    }

    @Test fun `should get by index`() {
        val list = list(1, 2, 3)
        assertThat(list[0]).isEqualTo(1)
        assertThat(list[1]).isEqualTo(2)
        assertThat(list[2]).isEqualTo(3)
    }

    @Test fun `should find index of element`() {
        val list = list(1, 2, 3)
        assertThat(list.indexOf(2)).isEqualTo(1)
    }

    @Test fun `should find last index of element`() {
        val list = list(1, 2, 2)
        assertThat(list.lastIndexOf(2)).isEqualTo(2)
    }

    @Test fun `should drop elements`() {
        assertThat(list(1, 2, 3).drop(0)).isEqualTo(list(1, 2, 3))
        assertThat(list(1, 2, 3).drop(1)).isEqualTo(list(2, 3))
        assertThat(list(1, 2, 3).drop(2)).isEqualTo(list(3))
        assertThat(list(1, 2, 3).drop(3)).isEqualTo(list<Int>())

        assertThat(list(1, 2, 3).drop(2)).isEqualTo(listOf(1, 2, 3).drop(2))
    }

    @Test fun `should drop last elements`() {
        assertThat(list(1, 2, 3).dropLast(0)).isEqualTo(list(1, 2, 3))
        assertThat(list(1, 2, 3).dropLast(1)).isEqualTo(list(1, 2))
        assertThat(list(1, 2, 3).dropLast(2)).isEqualTo(list(1))
        assertThat(list(1, 2, 3).dropLast(3)).isEqualTo(list<Int>())

        assertThat(list(1, 2, 3).dropLast(2)).isEqualTo(listOf(1, 2, 3).dropLast(2))
    }

    @Test fun `should take elements`() {
        assertThat(list(1, 2, 3).take(0)).isEqualTo(list<Int>())
        assertThat(list(1, 2, 3).take(1)).isEqualTo(list(1))
        assertThat(list(1, 2, 3).take(2)).isEqualTo(list(1, 2))
        assertThat(list(1, 2, 3).take(3)).isEqualTo(list(1, 2, 3))

        assertThat(list(1, 2, 3).take(2)).isEqualTo(listOf(1, 2, 3).take(2))
    }

    @Test fun `should take last elements`() {
        assertThat(list(1, 2, 3).takeLast(0)).isEqualTo(list<Int>())
        assertThat(list(1, 2, 3).takeLast(1)).isEqualTo(list(3))
        assertThat(list(1, 2, 3).takeLast(2)).isEqualTo(list(2, 3))
        assertThat(list(1, 2, 3).takeLast(3)).isEqualTo(list(1, 2, 3))

        assertThat(list(1, 2, 3).takeLast(2)).isEqualTo(listOf(1, 2, 3).takeLast(2))
    }

    @Test fun `should create sub lists`() {
        assertThat(list(1, 2, 3).subList(0, 0)).isEqualTo(list<Int>())
        assertThat(list(1, 2, 3).subList(0, 1)).isEqualTo(list(1))
        assertThat(list(1, 2, 3).subList(0, 2)).isEqualTo(list(1, 2))
        assertThat(list(1, 2, 3).subList(0, 3)).isEqualTo(list(1, 2, 3))
        assertThat(list(1, 2, 3).subList(1, 1)).isEqualTo(list<Int>())
        assertThat(list(1, 2, 3).subList(1, 2)).isEqualTo(list(2))

        for ((from, to) in listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3, 1 to 1, 1 to 2)) {
            assertThat(list(1, 2, 3).subList(from, to)).isEqualTo(listOf(1, 2, 3).subList(from, to))
        }
    }

    @Test fun `should iterate with index`() {
        assertThat(listOf(1, 2).withIndex().toList()).isEqualTo(listOf(IndexedValue(0, 1), IndexedValue(1, 2)))
    }

    @Test fun `should iterate with list iterators`() {
        assertThat(listOf(1, 2, 3).listIterator().asSequence().toList()).isEqualTo(listOf(1, 2, 3))
        assertThat(listOf(1, 2, 3).listIterator(2).asSequence().toList()).isEqualTo(listOf(3))
    }

    @Test fun `should support variance`() {
        val list: ImmutableList<Int> = list(1, 2, 3)
        val listAny: ImmutableList<Any> = list
        assertThat(listAny).isEqualTo(list)
    }
}
