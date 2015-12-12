#### Dexx Collections for Kotlin

The Kotlin module provides decorators for the Dexx's Java collections to improve the Kotlin API.

### Features
- Defines `ImmutableSet`, `ImmutableList` & `ImmutableMap` that extend Kotlin's `Set`, `List` & `Map` respectively.
- Defines functions for construction, such as `immutableSetOf`, `immutableListOf` & `immutableMapOf` (as well as sorted variants).
- Defines extension functions to convert from Kotlin's `Iterable` and `Sequence`.

Example:
```kotlin
import com.github.andrewoma.dexx.kollection.*

val set1 = immutableSetOf(1, 2, 3)
val set2 = set1 + 4
val set3 = set1 - 1

assertEquals(set1, immutableSetOf(1, 2, 3))
assertEquals(set2, immutableSetOf(1, 2, 3, 4))
assertEquals(set3, immutableSetOf(2, 3))
```

From the above example we can see that although we've made "modifications" to `set1` to create `set2` and `set3`, the contents of `set1` remain unchanged.

#### Constructing collections

Construction can be done by the `immutable*Of()` functions or by converting existing `Iterables`
 and `Sequences` via the `toImmutable*()` extension functions.
```kotlin
val list1 = immutableListOf(1, 2, 3)
val list2 = listOf(1, 2, 3).toImmutableList()
```

#### Constructing sorted collections

Sorted collections can be constructed using `Comparable` elements or by providing
 a `selector` function that provides a `Comparable` object.

```kotlin
val sorted = immutableSortedSetOf(3, 1, 2)
assertEquals(sorted.toList(), listOf(1, 2, 3))

data class Name(val first: String, val last: String)

// Sorts the set by first name
val byName = immutableCustomSortedSetOf({ it.first },
        Name("Kate", "Beckinsale"),
        Name("Simon", "Pegg"))
```

#### More information

See the [API source](src/main/kotlin/com/github/andrewoma/dexx/kollection) for the full API.

See the [tests](src/test/kotlin/com/github/andrewoma/dexx/kollection) for more examples of usage.

#### Roadmap

* Expose sorted interfaces for `Sets` and `Maps` to allow efficient creation of subsets without copying
* Explore adding methods such as `add` and `remove` in addition the current operators
