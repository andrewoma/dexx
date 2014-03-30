#### What are Dexx Collections?

Dexx Collections are a port of Scala's immutable, persistent collection classes to pure Java.

_Persistent_ in the context of functional data structures means the data structure preserves the previous version of itself when modified. This means any reference to a collection is effectively immutable. However, modifications can be made by returning a new version of the data structure, leaving the original structure unchanged.

Here's an example using Dexx's Sets (examples are in [Kotlin](http://kotlin.jetbrains.org/) for conciseness, but the collections are pure Java):
```kotlin
val set1 = Sets.of(1, 2, 3)
val set2 = set1.add(4)
val set3 = set1.remove(1)
println(set1) // Prints Set(1, 2, 3)
println(set2) // Prints Set(1, 2, 3, 4)
println(set3) // Prints Set(2, 3)
```

From the above example we can see that although we've made modifications to `set1` to create `set2` and `set3`, the contents of `set1` remain unchanged.

#### Why port?

Scala's collections can be directly used from Java, but the resulting code is far from [idiomatic](http://stackoverflow.com/questions/6578615/how-to-use-scala-collection-immutable-list-in-a-java-code). Scala's standard library is also large and binary incompatible between versions.

Secondly, a pure Java implementation of functional persistent collections is usable from not only Java, but other JVM languages that interoperate with Java such as Kotlin, Ceylon or GWT. In fact, the collections have been specifically designed for use with Kotlin.

#### Overview

The diagram below shows Dexx's class hierarchy (interfaces are in blue and concrete implementations are in green).

![Dexx Collections Overview](/docs/dexxcollections.png "Dexx Collections Overview")

Note that the interfaces such as `Map`, `Set` and `List` are *not* related to the `java.util` equivalents as persistent collections require all modification methods such as `add` and `remove` to return a new collection instance.

#### Status
* Scala's HashSet, TreeSet, HashMap, TreeMap and Vector have been ported
* Helper classes for construction and adapters to `java.util` collections are complete
* Test coverage is fairly comprehensive: 94% line and 89% branch at present

#### Dependencies
* There are no runtime dependencies
* [JetBrain's annotations](https://www.jetbrains.com/idea/documentation/howto.html) (`@NotNull` and `@Nullable`) are used in the source to support Kotlin's nullable types, but they are [not required at runtime](http://stackoverflow.com/questions/3567413/why-doesnt-a-missing-annotation-cause-a-classnotfoundexception-at-runtime).
* The tests are written in Kotlin, but again this is not a runtime dependency

#### Roadmap
* Publishing of 0.1 to Maven Central is in progress
* Complete basic benchmarks for existing implementations (HashSet and TreeSet are done).
* Port Scala's ArrayList as an alternative IndexedList implementation
* Port Scala's List/ListBuffer as a LinkedList/Builder implementation

#### License
This project is licensed under a MIT license. Portions ported from Scala are Scala's 3-clause BSD license.

#### Usage

##### Adding to your project
Version 0.1 has been released. You can use it via the following dependency:
```xml
<dependency>
    <groupId>com.github.andrewoma.dexx</groupId>
    <artifactId>dexx-collections</artifactId>
    <version>0.1</version>
</dependency>
```
This release should be in Maven Central shortly. Until then, it is available via Sonatype's staging repository:
```xml
<repository>
    <id>sonatype-staging</id>
    <name>Sonatype Staging</name>
    <url>https://oss.sonatype.org/content/groups/staging</url>
</repository>
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
val set = SortedSets.of(1, 2, 3, 4, 5, 6).stream()
        .filter { it % 2 == 0 }
        .map { "$it is even" }
        .take(2)
        .toPersistentSortedSet()

assertEquals(SortedSets.of("2 is even", "4 is even"), set)
```
The example above uses Kotlins in-built extension function that converts any `Iterable` into a `Stream`.
It also uses the following extension functions to add `Stream<T>.toPersistentSortedSet()` to cleanly convert the stream
back into a Dexx Collection.

```kotlin
fun <T, R> Stream<T>.build(builder: Builder<T, R>): R {
    this.forEach { builder.add(it) }
    return builder.build()
}

fun <T> Stream<T>.toPersistentSet(): Set<T> = build(Sets.builder<T>())
```

#### Performance

Benchmarking is still a work in progress (all the warnings about JVM benchmarks apply). The results so far
running on OS X 10.6.8 with JDK 1.6.0_65 are:
* HashSet is around 3 times slower than `java.util.HashSet` for add, remove and contains.
* TreeSet is around 3 times slower than `java.util.TreeSet` for add and remove, but on a par for contains.

See the section below for how to run the benchmarks for yourself. The output from my development machine
is [here](/docs/benchmarks.txt).

My conclusions so far are that the collections perform adequately to be used as a drop-in replacement
for the majority of use cases. While slower, _slow_ is generally referring to millions of operations per second.

#### Development
* Dexx is currently built with maven. Use `mvn install` to build and install into your local repository.
* By default, a quick version of tests are run. Getting better test coverage of `Vectors` requires large
  collections. To run tests with complete coverage use: `mvn -Ddexx.test.mode=COMPLETE -P cobertura clean cobertura:cobertura`
* To run the benchmarks, use `mvn -Ddexx.test.mode=BENCHMARK test` (results are lines starting with BENCHMARK).
