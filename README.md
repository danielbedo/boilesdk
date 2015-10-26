## boilesdk: boilerplate-free machine learning helper library in scala
Boilesdk aims to be a typeclass and dependent type based helper library that simplifies common 
tasks associated with machine learning problems using [shapeless](https://github.com/milessabin/shapeless).

### Current features
#### Converting arbitrary case classes to vectors
Most of the time in our applications we represent our data using model (case) classes. 
```scala
case class House(size: Double, numRooms: Int, price: Int)
```
Usually the first task - regardless of the machine learning algorithm we want to use - is to convert these classes to a vector of doubles.
```scala
case class House(size: Double, numRooms: Int, price: Int) {
    def toDouble(): Vector[Double] = Vector(size, numRooms.toDouble, price.toDouble) 
}
```
This is perfectly fine if you have one class and it's not really changing, but it get's tedious if you have 50 features encoded in nested case
classes that change everyday as you are experimenting with your features.
here comes shapeless:
 