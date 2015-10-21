import shapeless._

import derivation.generic.doubleConversion._
import derivation.ShowReprDefns
object doubleConversionTest extends App with ShowReprDefns {


  case class AnswerData(length: Int, numWords: Int, isDeleted: Boolean, c: Char)
  object AnswerData {
    implicit val dcChar: DoubleConverter[Char] =
      new DoubleConverter[Char] {
        def toDouble(x: Char): Vector[Double] =
          Vector(1.0)
      }
  }
  case class AuthorData(age: Int, numAnswers: Int, deletionRatio: Double)

  case class Social(numFriends: Int)

  case class AnswerFeature(answerData: AnswerData, authorData: AuthorData)

  val testAnswer = AnswerData(123, 12, false, 'c')
  val testAuthor = AuthorData(30, 5, 0.5)
  val testFeature = AnswerFeature(testAnswer, testAuthor)
  import AnswerData._
  println(testFeature.toDouble)
}
