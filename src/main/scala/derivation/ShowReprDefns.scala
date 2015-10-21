package derivation

import shapeless._

/** from miles sabin */

trait ShowReprDefns {
  def showRepr[T](t: T)(implicit st: ShowRepr[T]): String = st(t)

  trait ShowRepr[T] {
    def apply(t: T): String
  }

  object ShowRepr extends ShowRepr0 {
    implicit val hnilShowRepr: ShowRepr[HNil] =
      new ShowRepr[HNil] {
        def apply(t: HNil): String = "HNil"
      }

    implicit def hconsShowRepr[H, T <: HList]
    (implicit
     sh: Lazy[ShowRepr[H]],
     st: Lazy[ShowRepr[T]]
      ): ShowRepr[H :: T] =
      new ShowRepr[H :: T] {
        def apply(t: H :: T): String = sh.value(t.head)+" :: "+st.value(t.tail)
      }

    implicit val cnilShowRepr: ShowRepr[CNil] =
      new ShowRepr[CNil] {
        def apply(t: CNil): String = "CNil"
      }

    implicit def cconsShowRepr[H, T <: Coproduct]
    (implicit
     sh: Lazy[ShowRepr[H]],
     st: Lazy[ShowRepr[T]]
      ): ShowRepr[H :+: T] =
      new ShowRepr[H :+: T] {
        def apply(t: H :+: T): String =
          t match {
            case Inl(l) => "Inl("+sh.value(l)+")"
            case Inr(r) => "Inr("+st.value(r)+")"
          }
      }

    implicit def genShowRepr[T, R]
    (implicit
     gen: Generic.Aux[T, R],
     sr: Lazy[ShowRepr[R]]
      ): ShowRepr[T] =
      new ShowRepr[T] {
        def apply(t: T): String = sr.value(gen.to(t))
      }
  }

  trait ShowRepr0 {
    implicit def default[T]: ShowRepr[T] =
      new ShowRepr[T] {
        def apply(t: T): String = t.toString
      }
  }
}