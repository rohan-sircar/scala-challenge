package wow.doge.app.services

import wow.doge.app.NewDomainProduct
import monix.bio.UIO
import monix.bio.IO
import cats.effect.concurrent.Ref
import wow.doge.app.refinements.ProductId
import monix.bio.Task
import wow.doge.app.DomainProduct
import eu.timepit.refined.auto._
import wow.doge.app.refinements.VendorName

trait ProductService {
  def createProduct(newProduct: NewDomainProduct): UIO[ProductId]
  def getProductById(id: ProductId): UIO[Option[DomainProduct]]
  def getProductsGroupedByVendorName: UIO[Map[VendorName, List[DomainProduct]]]
  def getProducts: UIO[List[DomainProduct]]
}

final class InMemoryProductService(
    idCounter: Ref[Task, ProductId],
    products: Ref[Task, Map[ProductId, DomainProduct]]
) extends ProductService {

  def createProduct(newProduct: NewDomainProduct): UIO[ProductId] =
    for {
      currId <- idCounter.get.hideErrors
      //can be done automatically using a macro library like chimney
      p = DomainProduct(
        currId,
        newProduct.name,
        newProduct.vendorName,
        newProduct.price,
        newProduct.expirationDate
      )
      _ <- products.update(_ + (currId -> p)).hideErrors
      _ <- idCounter.update(_ :+ ProductId(1)).hideErrors
    } yield currId

  def getProducts = products.get.map(_.values.toList).hideErrors

  def getProductById(id: ProductId): UIO[Option[DomainProduct]] =
    products.get.hideErrors.map(_.get(id))

  def getProductsGroupedByVendorName
      : UIO[Map[VendorName, List[DomainProduct]]] =
    products.get.hideErrors.map(_.values.toList.groupBy(_.vendorName))
}

object InMemoryProductService {
  def apply() = for {
    idCounterRef <- Ref[Task].of(ProductId(1)).hideErrors
    productsRef <- Ref[Task].of(Map.empty[ProductId, DomainProduct]).hideErrors
  } yield new InMemoryProductService(idCounterRef, productsRef)
}
