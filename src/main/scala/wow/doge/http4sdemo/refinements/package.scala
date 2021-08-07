package wow.doge.app

import io.estatico.newtype.macros.newtype
import io.estatico.newtype.ops._
import eu.timepit.refined.types.string
import eu.timepit.refined.types.numeric.PosInt
import java.time.LocalDateTime
import jp.ne.opt.chronoscala.NamespacedImports._
import monix.bio.UIO
import monix.bio.IO
import io.circe.refined._
import io.circe.Encoder
import io.circe.Decoder
import cats.kernel.Eq
import eu.timepit.refined.cats._

package object refinements {
  @newtype final case class ProductId(inner: PosInt)
  object ProductId {
    implicit val encoder: Encoder[ProductId] = Encoder[PosInt].coerce
    implicit val decoder: Decoder[ProductId] = Decoder[PosInt].coerce

    implicit class ProductIdOps(private val v: ProductId) extends AnyVal {
      def :+(that: ProductId) = ProductId(
        PosInt.unsafeFrom(v.inner.value + that.inner.value)
      )
    }
  }
  @newtype final case class ProductName(inner: string.NonEmptyFiniteString[10])
  object ProductName {
    implicit val encoder: Encoder[ProductName] =
      Encoder[string.NonEmptyFiniteString[10]].coerce
    implicit val decoder: Decoder[ProductName] =
      Decoder[string.NonEmptyFiniteString[10]].coerce
  }
  @newtype final case class VendorName(inner: string.NonEmptyFiniteString[10])
  object VendorName {
    implicit val encoder: Encoder[VendorName] =
      Encoder[string.NonEmptyFiniteString[10]].coerce
    implicit val decoder: Decoder[VendorName] =
      Decoder[string.NonEmptyFiniteString[10]].coerce
    implicit val eq: Eq[VendorName] = Eq[string.NonEmptyFiniteString[10]].coerce
  }
  @newtype final case class ProductPrice(inner: PosInt)
  object ProductPrice {
    implicit val encoder: Encoder[ProductPrice] = Encoder[PosInt].coerce
    implicit val decoder: Decoder[ProductPrice] = Decoder[PosInt].coerce
  }

}

package refinements {

  final case class ExpirationDate private (inner: LocalDateTime)
  object ExpirationDate {
    def apply(v: LocalDateTime): IO[String, ExpirationDate] = for {
      now <- UIO(LocalDateTime.now())
      res <-
        if (v > now) IO.pure(new ExpirationDate(v))
        else IO.raiseError("Expiration date has to be in the future")
    } yield res

    def impure(v: LocalDateTime) = for {
      now <- Right(LocalDateTime.now())
      res <-
        if (v > now) Right(new ExpirationDate(v))
        else Left("Expiration date has to be in the future")
    } yield res

    implicit val encoder =
      Encoder[LocalDateTime].contramap[ExpirationDate](_.inner)

    implicit val decoder = Decoder[LocalDateTime].emap(impure)
  }
}
