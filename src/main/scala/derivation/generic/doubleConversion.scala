package derivation.generic

import shapeless._

object doubleConversion {

  trait DoubleConverter[T] {
    def toDouble(x: T): Vector[Double]
  }

  object DoubleConverter {
    implicit val dcBoolean: DoubleConverter[Boolean] =
      new DoubleConverter[Boolean] {
        def toDouble(x: Boolean): Vector[Double] =
          if (x) Vector(1.0) else Vector(0.0)
      }

    implicit val dcInt: DoubleConverter[Int] =
      new DoubleConverter[Int] {
        def toDouble(x: Int): Vector[Double] = Vector(x.toDouble)
      }

    implicit val dcDouble: DoubleConverter[Double] =
      new DoubleConverter[Double] {
        def toDouble(x: Double): Vector[Double] = Vector(x)
      }

    implicit def dcGeneric[T, R]
    (implicit
     gen: Generic.Aux[T, R],
     dcRepr: Lazy[DoubleConverter[R]]
      ): DoubleConverter[T] =
      new DoubleConverter[T] {
        def toDouble(x: T): Vector[Double] =
          dcRepr.value.toDouble(gen.to(x))
      }

    implicit val dcHNil: DoubleConverter[HNil] = new DoubleConverter[HNil] {
      def toDouble(x: HNil): Vector[Double] = Vector()
    }

    implicit def dcHCons[H, T <: HList]
    (implicit
      dcH: Lazy[DoubleConverter[H]],
      dcT: Lazy[DoubleConverter[T]]
      ): DoubleConverter[H :: T] = new DoubleConverter[H :: T] {
      def toDouble(x: H :: T): Vector[Double] = dcH.value.toDouble(x.head) ++ dcT.value.toDouble(x.tail)
    }
  }

  implicit class DoubleOps[T](x: T)(implicit dcT: DoubleConverter[T]) {
    def toDouble: Vector[Double] = dcT.toDouble(x)
  }

}
