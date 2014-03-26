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

package com.github.andrewoma.dexx.collection;

/**
 * {@code Pair} is a generic container for two components of specified types.
 */
public class Pair<C1, C2> {
    private final C1 component1;
    private final C2 component2;

    public Pair(C1 component1, C2 component2) {
        this.component1 = component1;
        this.component2 = component2;
    }

    public C1 component1() {
        return component1;
    }

    public C2 component2() {
        return component2;
    }

    @Override
    public String toString() {
        return "(" + component1 + ", " + component2 + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair pair = (Pair) o;
        return !(component1 != null ? !component1.equals(pair.component1) : pair.component1 != null)
                && !(component2 != null ? !component2.equals(pair.component2) : pair.component2 != null);
    }

    @Override
    public int hashCode() {
        int result = component1 != null ? component1.hashCode() : 0;
        result = 31 * result + (component2 != null ? component2.hashCode() : 0);
        return result;
    }
}
