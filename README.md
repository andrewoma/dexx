#### What are dexx collections?

Dexx collections are a port of Scala's immutable, persistent collection classes to pure Java.

_Persistent_ in the context of functional data structures means the data structure preserves the previous version of itself when modified. This means any reference to a collection is effectively immutable. However, modifications can be made by returning a new version of the data structure, leaving the original structure unchanged.

Here's an example using dexx's Sets (examples are in [Kotlin](http://kotlin.jetbrains.org/) for conciseness, but the collections are pure Java):
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

The diagram below shows dexx's class hierarchy (interfaces are in blue and concrete implementations are in green).

![dexx collections overview](/docs/dexxcollections.png "dexx collections overview")

Note that the interfaces such as `Map`, `Set` and `List` are *not* related to the `java.util` equivalents as persistent collections require all modification methods such as `add` and `remove` to return a new collection instance.

#### Status
* Scala's HashSet, TreeSet, HashMap, TreeMap & Vector have been ported
* Helper classes for construction and adapters to java.util equivalents are largely complete

#### Dependencies
* There are no runtime dependencies
* [JetBrain's annotations](https://www.jetbrains.com/idea/documentation/howto.html) (`@NotNull` and `@Nullable`) are used in the source to support Kotlin's nullable types, but they are [not required at runtime](http://stackoverflow.com/questions/3567413/why-doesnt-a-missing-annotation-cause-a-classnotfoundexception-at-runtime).

#### Roadmap
* Complete Javadocs
* Update README with basic usage
* Benchmark existing implementations
* Publish 0.1 to Maven Central via Sonatype
* Port Scala's ArrayList as an alternative IndexedList implementation
* Port Scala's List/ListBuffer as a LinkedList/Builder implementation
