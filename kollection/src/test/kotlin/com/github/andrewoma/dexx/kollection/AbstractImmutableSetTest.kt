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

abstract class AbstractImmutableSetTest {

    abstract fun <E : Comparable<E>> set(vararg elements: E): ImmutableSet<E>

    @Test fun `should support immutable semantics`() {
        val set1 = set(1, 2, 3)
        val set2 = set1 + 4
        val set3 = set1 - 1

        assertThat(set1).isEqualTo(set(1, 2, 3))
        assertThat(set2).isEqualTo(set(1, 2, 3, 4))
        assertThat(set3).isEqualTo(set(2, 3))
    }

    @Test fun `should support equals and hashCode`() {
        assertThat(set<Int>()).isEqualTo(set<Int>())
        assertThat(set(1)).isEqualTo(set(1))
        assertThat(set(1, 2, 3)).isEqualTo(set(1, 2, 3))
        assertThat(set(1, 2, 3)).isNotEqualTo(set(2, 3, 4))

        assertThat(set<Int>().hashCode()).isEqualTo(set<Int>().hashCode())
        assertThat(set(1).hashCode()).isEqualTo(set(1).hashCode())
        assertThat(set(1, 2, 3).hashCode()).isEqualTo(set(1, 2, 3).hashCode())
        assertThat(set(1, 2, 3).hashCode()).isNotEqualTo(set(2, 3, 4).hashCode())
    }

    @Test fun `should equal kotlin sets`() {
        assertThat(set(1, 2, 3)).isEqualTo(hashSetOf(1, 2, 3))
        assertThat(hashSetOf(1, 2, 3)).isEqualTo(set(1, 2, 3))
    }

    @Test fun `should convert to and from kotlin collections`() {
        assertThat(arrayListOf(1, 2, 3).toImmutableSet()).isEqualTo(set(1, 2, 3))
        assertThat(set(1, 2, 3).toSet()).isEqualTo(setOf(1, 2, 3))
    }

    @Test fun `should support bulk operations`() {
        assertThat(set(1, 2, 3) + set(4, 5)).isEqualTo(set(1, 2, 3, 4, 5))
        assertThat(set(1, 2, 3) - set(1, 2)).isEqualTo(set(3))
    }

    @Test fun `should support contains`() {
        assertThat(set(1, 2, 3).contains(1)).isTrue()
        assertThat(set(1, 2, 3).contains(4)).isFalse()

        assertThat(set(1, 2, 3).containsAll(setOf(1, 2, 3))).isTrue()
        assertThat(set(1, 2, 3).containsAll(setOf(1, 2))).isTrue()
        assertThat(set(1, 2, 3).containsAll(setOf(1, 2, 3, 4))).isFalse()
    }

    @Test fun `should support size`() {
        assertThat(set<Int>().size).isEqualTo(0)
        assertThat(set<Int>().isEmpty()).isTrue()

        assertThat(set(1).size).isEqualTo(1)
        assertThat(set(1).isEmpty()).isFalse()

        assertThat(set(1, 2, 3).size).isEqualTo(3)
        assertThat(set(1, 2, 3).isEmpty()).isFalse()
    }

    @Test fun `should iterate elements`() {
        assertThat(set(1, 2, 3).iterator().asSequence().toSet()).isEqualTo(setOf(1, 2, 3))
    }

    @Test fun `should support variance`() {
        val set: ImmutableSet<Int> = set(1, 2, 3)
        val setAny: ImmutableSet<Any> = set
        assertThat(setAny).isEqualTo(set)
    }
}
