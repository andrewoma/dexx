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

class ImmutableMapTest : AbstractImmutableMapTest() {

    override fun <K : Comparable<K>, V> map(elements: List<Pair<K, V>>) = elements.toImmutableMap()

    @Test fun `should be produce a readable toString`() {
        assertThat(map(pairs(1, 2, 3)).toString())
                .isEqualTo("ImmutableMap(1=a, 2=b, 3=c)")
    }

    @Test fun `should allow construction from sequences`() {
        assertThat(pairs(1, 2, 3).asSequence().toImmutableMap())
                .isEqualTo(map(pairs(1, 2, 3)))
    }

    @Test fun `should allow construction from maps`() {
        assertThat(pairs(1, 2, 3).toMap().toImmutableMap())
                .isEqualTo(map(pairs(1, 2, 3)))
    }
}
