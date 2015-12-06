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

    abstract fun <E : Comparable<E>> iSetOf(vararg elements: E): ImmutableSet<E>

    @Test fun `should support immutable semantics`() {
        val set1 = iSetOf(1, 2, 3)
        val set2 = set1 + 4
        val set3 = set1 - 1

        assertThat(set1).isEqualTo(iSetOf(1, 2, 3))
        assertThat(set2).isEqualTo(iSetOf(1, 2, 3, 4))
        assertThat(set3).isEqualTo(iSetOf(2, 3))
    }

    @Test fun `should support equals and hashCode`() {
        assertThat(iSetOf<Int>()).isEqualTo(iSetOf<Int>())
        assertThat(iSetOf(1)).isEqualTo(iSetOf(1))
        assertThat(iSetOf(1, 2, 3)).isEqualTo(iSetOf(1, 2, 3))

        assertThat(iSetOf<Int>().hashCode()).isEqualTo(iSetOf<Int>().hashCode())
        assertThat(iSetOf(1).hashCode()).isEqualTo(iSetOf(1).hashCode())
        assertThat(iSetOf(1, 2, 3).hashCode()).isEqualTo(iSetOf(1, 2, 3).hashCode())
    }

    @Test fun `should equal kotlin sets`() {
        assertThat(iSetOf(1, 2, 3)).isEqualTo(hashSetOf(1, 2, 3))
        assertThat(hashSetOf(1, 2, 3)).isEqualTo(iSetOf(1, 2, 3))
    }

    @Test fun `should convert to and from kotlin collections`() {
        assertThat(arrayListOf(1, 2, 3).toImmutableSet()).isEqualTo(iSetOf(1, 2, 3))
        assertThat(iSetOf(1, 2, 3).toSet()).isEqualTo(setOf(1, 2, 3))
    }

    @Test fun `should support bulk operations`() {
        assertThat(iSetOf(1, 2, 3) + iSetOf(4, 5)).isEqualTo(iSetOf(1, 2, 3, 4, 5))
        assertThat(iSetOf(1, 2, 3) - iSetOf(1, 2)).isEqualTo(iSetOf(3))
    }

    @Test fun `should support contains`() {
        assertThat(iSetOf(1, 2, 3).contains(1)).isTrue()
        assertThat(iSetOf(1, 2, 3).contains(4)).isFalse()

        assertThat(iSetOf(1, 2, 3).containsAll(setOf(1, 2, 3))).isTrue()
        assertThat(iSetOf(1, 2, 3).containsAll(setOf(1, 2))).isTrue()
        assertThat(iSetOf(1, 2, 3).containsAll(setOf(1, 2, 3, 4))).isFalse()
    }

    @Test fun `should support size`() {
        assertThat(iSetOf<Int>().size).isEqualTo(0)
        assertThat(iSetOf<Int>().isEmpty()).isTrue()

        assertThat(iSetOf(1).size).isEqualTo(1)
        assertThat(iSetOf(1).isEmpty()).isFalse()

        assertThat(iSetOf(1, 2, 3).size).isEqualTo(3)
        assertThat(iSetOf(1, 2, 3).isEmpty()).isFalse()
    }

    @Test fun `should iterate elements`() {
        assertThat(iSetOf(1, 2, 3).iterator().asSequence().toSet())
                .isEqualTo(setOf(1, 2, 3))
    }
}
