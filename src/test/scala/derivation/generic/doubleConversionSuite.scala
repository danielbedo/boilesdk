package derivation.generic

import derivation.generic.doubleConversion._
import shapeless.test.illTyped

import org.scalatest.FunSuite

class DoubleConversionSuite extends FunSuite {

  test("An empty Set should have size 0") {
    case class ValidClass(a: Int, b: Double, c: Boolean)
    val vc = ValidClass(1, 2.0, false)

    assert(vc.toDouble == Vector(1.0, 2.0, 0.0))
  }

  test("toDouble shouldn't exist on a class that contains at least one not convertable attribute") {
    case class NotValidClass(a: Int, b: Double, c: Boolean, d: Char)
    val nvc = NotValidClass(1, 2.0, false, 'd')

    illTyped.apply("nvc.toDouble")
  }

  test("toDouble should work if you manually implement the converter for the missing type") {
    object WillBeValid {
      implicit val dcChar: DoubleConverter[Char] =
        new DoubleConverter[Char] {
          def toDouble(x: Char): Vector[Double] =
            Vector(1.0)
        }
    }
    case class WillBeValid(a: Int, b: Double, c: Boolean, d: Char)
    import WillBeValid._

    val wbv = WillBeValid(1, 2.0, false, 'd')

    assert(wbv.toDouble == Vector(1.0, 2.0, 0.0, 1.0))
  }
}