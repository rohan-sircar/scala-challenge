package wow.doge.app

import wow.doge.app.refinements._
import io.circe.generic.semiauto._
import java.time.LocalDateTime
// import eu.timepit.refined.auto._
import eu.timepit.refined.types.string.NonEmptyFiniteString
import eu.timepit.refined.types.numeric.PosInt
import cats.syntax.all._
import cats.data.NonEmptyList
import monix.bio.IO
import monix.bio.UIO

final case class RawNewDomainProduct(
    name: String,
    vendorName: String,
    price: Int,
    expirationDate: Option[LocalDateTime]
) {
  // defer because expirationDate verification has a side effect
  val refine = UIO(
    (
      NonEmptyFiniteString[10]
        .from(name)
        .map(ProductName.apply)
        .leftMap(_ => "Product name shoud be at most 10 characters long")
        .leftMap(NonEmptyList.one),
      NonEmptyFiniteString[10]
        .from(vendorName)
        .map(VendorName.apply)
        .leftMap(_ => "Vendor name shoud be at most 10 characters long")
        .leftMap(NonEmptyList.one),
      PosInt
        .from(price)
        .map(ProductPrice.apply)
        .leftMap(_ => "Product price should be a positive number")
        .leftMap(NonEmptyList.one),
      expirationDate
        .map(e => ExpirationDate.impure(e).leftMap(NonEmptyList.one))
        .sequence
    ).parMapN(NewDomainProduct.apply)
  )
}
object RawNewDomainProduct {
  implicit val codec = deriveCodec[RawNewDomainProduct]
}

final case class NewDomainProduct(
    name: ProductName,
    vendorName: VendorName,
    price: ProductPrice,
    expirationDate: Option[ExpirationDate]
)
object NewDomainProduct {
  implicit val codec = deriveCodec[NewDomainProduct]
}

final case class DomainProduct(
    id: ProductId,
    name: ProductName,
    vendorName: VendorName,
    price: ProductPrice,
    expirationDate: Option[ExpirationDate]
)
object DomainProduct {
  implicit val codec = deriveCodec[DomainProduct]
}
