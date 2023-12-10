#### What are Dexx Collections?

Dexx Collections are a port of Scala's immutable, persistent collection classes to pure Java.

_Persistent_ in the context of functional data structures means the data structure preserves the previous version of itself when modified. This means any reference to a collection is effectively immutable. However, modifications can be made by returning a new version of the data structure, leaving the original structure unchanged.

Here's an example using Dexx's Sets (examples are in [Kotlin](https://kotlinlang.org/) for conciseness, but the collections are pure Java):
```kotlin
val set1 = Sets.of(1, 2, 3)
val set2 = set1.add(4)
val set3 = set1.remove(1)
println(set1) // Prints Set(1, 2, 3)
println(set2) // Prints Set(1, 2, 3, 4)
println(set3) // Prints Set(2, 3)
```

From the above example we can see that although we've made modifications to `set1` to create `set2` and `set3`, the contents of `set1` remain unchanged.

Note: There's now first class support for Kotlin - see the [kollection module README](kollection/README.md) for more information.

#### Why port?

Scala's collections can be directly used from Java, but the resulting code is far from [idiomatic](http://stackoverflow.com/questions/6578615/how-to-use-scala-collection-immutable-list-in-a-java-code). Scala's standard library is also large and binary incompatible between versions.

Secondly, a pure Java implementation of functional persistent collections is usable from not only Java, but other JVM languages that interoperate with Java such as Kotlin, Ceylon or GWT. In fact, the collections have been specifically designed for use with Kotlin.

#### Overview

The diagram below shows Dexx's class hierarchy (interfaces are in blue and concrete implementations are in green).

![Dexx Collections Overview](/docs/dexxcollections.png "Dexx Collections Overview")

Note that the interfaces such as `Map`, `Set` and `List` are *not* related to the `java.util` equivalents as persistent collections require all modification methods such as `add` and `remove` to return a new collection instance.

#### Status
* This is a fork of [andrewoma/dexx](https://github.com/andrewoma/dexx) for maintenance reasons
* All collections have been implemented
* HashSet, TreeSet, HashMap, TreeMap and Vector are ports from Scala
* ConsList and ArrayList have been written from scratch.
* Helper classes for construction and adapters to `java.util` collections are available
* Test coverage is fairly comprehensive: 95% line and 90% branch at present

#### Dependencies
* There are no runtime dependencies
* [JetBrain's annotations](https://www.jetbrains.com/idea/documentation/howto.html) (`@NotNull` and `@Nullable`) are used in the source to support Kotlin's nullable types, but they are [not required at runtime](http://stackoverflow.com/questions/3567413/why-doesnt-a-missing-annotation-cause-a-classnotfoundexception-at-runtime).
* The tests are written in Kotlin, but again this is not a runtime dependency

#### Roadmap
* Explore annotating methods that return a new collection with [@CheckReturnValue](https://code.google.com/p/error-prone/wiki/CheckReturnValue)
  to allow static verification of collection usage.
* Active development is essentially complete. Further work is expected to be bug fixes and refinements.

#### Release Notes
* 0.7:
  * Fixes #11 - a balancing error in red black trees
* 0.6:
  * Added OSGI metadata (thanks ajs6f)
  * Make internal fields `final` (thanks mkull)
  * Performance improvement to `first` and `last` of `TreeMap` & `TreeSet` (thanks mkull)
* 0.5:
  * Updated to 1.0.0
  * Added `toImmutableMap()` conversions from existing `Maps`
* 0.4:
  * Updated to 1.0.0-rc-1036
  * Removed accidental assertJ compile dependency in kollection (thanks @brianegan)
* 0.3.1:
  * Added a native Kotlin api in the kollection module
  * Converted the build to gradle from maven
  * Renamed `dexx-collections` artifact to `collection`
* 0.2:
  * Add `LinkedLists` support with `ConsList` as the default implementation
  * Add `ArrayList` as an alternative `IndexedList` implementation
  * Formalise the `Builder` contract and enforce at runtime
* 0.1:
  * Includes ports of Scala's `HashSet`, `TreeSet`, `HashMap`, `TreeMap` and `Vector`

#### License
This project is licensed under a MIT license. Portions ported from Scala are Scala's 3-clause BSD license.

#### Usage

##### Adding to your project
Version 0.7 has been released and is available in Maven Central [here](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.andrewoma.dexx%22). You can use it via the following gradle dependency:

```groovy
'com.github.andrewoma.dexx:collection:0.7' // For Java
'com.github.andrewoma.dexx:kollection:0.7' // For Kotlin
```

##### Constructing collections

Each of the leaf interfaces (`Set`, `SortedSet`, `Map`, `SortedMap`, `IndexedList` and `LinkedList`) have
associated companion classes with static methods for construction.

The companion class uses the plural form of the interface. e.g. `Set` has a companion class of `Sets`.

To build a collection from a fixed number of elements, use the overloaded `of()` methods. e.g.
```kotlin
val set = Sets.of(1, 2, 3)
```
To build a collection from a `java.util` collection, use the `copyOf()` methods. e.g.
```kotlin
val set = Sets.copyOf(javaCollection)
```
`Builders` should be used when incrementally constructing a collection. This allows for more efficient structures
to be used internally during construction. In the case of `LinkedList`, using a builder is important as `LinkedList` does not support appending without copying the entire collection.
```kotlin
val builder = Sets.builder<Int>()
for (i in 1..100) {
    builder.add(i)
}
val set = builder.build()
```
##### Viewing as `java.util` collections

Unfortunately, the `java.util` collection interfaces are not compatible with persistent collections as
modifications such as `add()` must return a new collection instance, leaving the original untouched.

However, all collections can be viewed as an immutable form of their `java.util` equivalent by using the
the `as...()` methods.
```kotlin
val javaSet = Sets.of(1, 2, 3).asSet() // Now a java.util.Set
```
##### Where are `filter()`, `map()` and friends?

Such transformations are deliberately <b>not</b> supported:

* In JDK versions < 1.8, using a functional style is ugly and not recommended.
  See [Excessive use of Guava's functional programming idioms can lead to verbose, confusing, unreadable, and inefficient code](http://code.google.com/p/guava-libraries/wiki/FunctionalExplained).

* In Kotlin and JDK 1.8, the platform provides transformations that can be used on the collections for free.
  Adding another set of transformations directly to the collections (with subtly different semantics) seems harmful.

Here's an example of using lazy evaluation in a functional style with Kotlin:

```kotlin
val set = SortedSets.of(1, 2, 3, 4, 5, 6).asSequence()
        .filter { it % 2 == 0 }
        .map { "$it is even" }
        .take(2)
        .toImmutableSet()

assertEquals(SortedSets.of("2 is even", "4 is even"), set)
```
The example above uses Kotlins in-built extension function that converts any `Iterable` into a `Sequence`.
It also uses the following extension functions to add `Sequence<T>.toImmutableSet()` to cleanly convert the sequence
back into a Dexx Collection.

```kotlin
fun <T, R> Sequence<T>.build(builder: Builder<T, R>): R {
    this.forEach { builder.add(it) }
    return builder.build()
}

fun <T> Sequence<T>.toImmutableSet(): SortedSet<T> = build(SortedSets.builder<T>())
```

#### Performance

Benchmarking is still a work in progress (all the warnings about JVM benchmarks apply). The results so far
running on Mac OS X 10.11.1 x86_64 with JDK 1.8.0_65 (Oracle Corporation 25.65-b01) are [here](/docs/benchmarks.txt).

My conclusions so far are that the collections perform adequately to be used as a drop-in replacement
for the majority of use cases. While slower, _slow_ is generally referring to millions of operations per second.

In general, mutating methods incur a overhead of 2-5 times that of `java.util` equivalents and
reading operations are 1-1.5 time slower.

@ptitjes Has done some more rigorous benchmarks here: https://github.com/ptitjes/benchmark-immutables/blob/master/results/2016-10-02-23:56:36.pdf

#### Development
* Dexx is built with gradle. Use `./gradlew install` to build and install into your local repository.
* To run the benchmarks, use `./gradlew check -PdexxTestMode=BENCHMARK --info | grep '^    BENCHMARK:'`.
* To generate coverage reports, use:
```
./gradlew :collection:clean :collection:check :collection:jacocoTestReport
 open collection/build/jacocoHtml/index.html
```
* By default, a quick version of tests are run. Getting better test coverage of `Vectors` requires large
  collections. To run tests with complete coverage use: `./gradlew -PdexxTestMode=COMPLETE :collection:clean :collection:check :collection:jacocoTestReport`

#### Method counts

For android developers, here are method counts:
* 'com.github.andrewoma.dexx:collection:0.6' = 1036 methods
* 'com.github.andrewoma.dexx:kollection:0.6' = 1213 methods

[![Build Status](https://travis-ci.org/andrewoma/dexx.svg?branch=master)](https://travis-ci.org/andrewoma/dexx)
