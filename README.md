## boilesdk: boilerplate-free machine learning helper library in scala
Boilesdk aims to be a typeclass and dependent type based helper library that simplifies common 
tasks associated with machine learning problems using [shapeless](https://github.com/milessabin/shapeless).

### Current features
#### Converting arbitrary case classes to vector of Doubles
Most of the time in our applications we represent our data using model (case) classes.
Usually the first task - regardless of the machine learning algorithm we want to use - is to convert these classes to a vector of doubles.
```scala
case class House(size: Double, numRooms: Int, price: Int) {
    def toDouble(): Vector[Double] = Vector(size, numRooms.toDouble, price.toDouble) 
}
```
This is perfectly fine if you have only one class and it's not changing often, but it get's a tedious task to update it all the time
if you have 50 features encoded in nested case classes that change everyday as you are experimenting with your features.
You could inspect the class at runtime using reflection and add the method there, but then if you change the case class but forget to change the method that spits out the Vector representation
you might only find it out at runtime.
Let's solve these problems at compile time and use shapeless to generate this method for us.
All you need to do is define your case class and import doubleConversion as follows:
```scala
case class House(size: Double, numRooms: Int, price: Int, free: Boolean)
val house = House(51.5, 3, 122, false)
println(house.toDouble)
```
output:
```scala
Vector(51.5, 3.0, 122.0, 0.0)
```

For now there are conversions available for the following primitive types: Int, Double, Boolean.
If your class contains some other type then the method toDouble won't be available on it, altough you can easily
add an implicit converter for the missing type to the companion object which will make it available.
```scala
case class House(size: Double, numRooms: Int, price: Int, free: Boolean, city: String)

// String needs a converter as it's not provided by the library
object House {
  implicit val dcString: DoubleConverter[String] =
    // not a useful converter, any string just gets represented as 0.0
    new DoubleConverter[String] {
      def toDouble(x: String): Vector[Double] =
        Vector(0.0)
    }
}
val house = House(51.5, 3, 122, false, "Munich")
import House._  // don't forget to pull the new implicit into scope
println(house.toDouble)
```
output:
```scala
Vector(51.5, 3.0, 122.0, 0.0, 0.0)
```

Nested Case Classes also get flattened into the vector:
```scala
case class House(size: Double, numRooms: Int, wc: WindowCount)
case class WindowCount(num: Int)

val house = House(2.0, 3, WindowCount(5))
println(house.toDouble)
```
output:
```scala
Vector(2.0, 3.0, 5.0)
```