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

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ImmutableSortedSetTest : AbstractImmutableSetTest() {

    val reverse: (Int) -> Int = { e -> e * -1 }

    override fun <E : Comparable<E>> set(vararg elements: E) = immutableSortedSetOf(*elements)

    @Test fun `should order elements`() {
        assertThat(immutableSortedSetOf(3, 1, 4, 5, 6, 2).toList())
                .isEqualTo(listOf(1, 2, 3, 4, 5, 6))
        assertThat(immutableCustomSortedSetOf(reverse, 3, 1, 4, 5, 6, 2).toList())
                .isEqualTo(listOf(6, 5, 4, 3, 2, 1))
    }

    @Test fun `should convert from Kotlin collections`() {
        assertThat(listOf(3, 1, 4, 5, 6, 2).toImmutableSortedSet().toList())
                .isEqualTo(listOf(1, 2, 3, 4, 5, 6))
        assertThat(listOf(3, 1, 4, 5, 6, 2).toImmutableSortedSet(reverse).toList())
                .isEqualTo(listOf(6, 5, 4, 3, 2, 1))
    }

    @Test fun `should be produce a readable toString`() {
        Assertions.assertThat(set(1, 2, 3).toString()).isEqualTo("ImmutableSortedSet(1, 2, 3)")
    }

    @Test fun `should allow construction from sequences`() {
        assertThat(listOf(1, 2, 3).asSequence().toImmutableSortedSet()).isEqualTo(immutableSortedSetOf(1, 2, 3))
        assertThat(listOf(1, 2, 3).asSequence().toImmutableSortedSet(reverse)).isEqualTo(immutableCustomSortedSetOf(reverse, 1, 2, 3))
    }
}

