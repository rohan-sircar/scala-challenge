package wow.doge.app

import cats.effect.Resource
import monix.bio.Task
import org.http4s.HttpRoutes
import wow.doge.app.services.ProductService
import wow.doge.app.refinements.ProductId
import io.circe.generic.semiauto._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import monix.bio.IO
import cats.syntax.all._
import wow.doge.app.refinements.VendorName
import wow.doge.app.services.InMemoryProductService

final case class CreateProductResponse(productId: ProductId)
object CreateProductResponse {
  implicit val codec = deriveCodec[CreateProductResponse]
}

final class ProductRoutes(productService: ProductService)
    extends Http4sServerInterpreter {

  def createProduct(rawNewProduct: RawNewDomainProduct) = for {
    mbNewProduct <- rawNewProduct.refine
    productId <- mbNewProduct match {
      case Left(value)  => IO.raiseError(AppError.BadInput(value))
      case Right(value) => productService.createProduct(value)
    }
  } yield CreateProductResponse(productId)

  val createProductRoute: HttpRoutes[Task] =
    toRouteRecoverErrors(ProductEndpoints.createProduct) { rawNewProduct =>
      val task = createProduct(rawNewProduct)
      task
    }

  val getProductsRoute: HttpRoutes[Task] =
    toRouteRecoverErrors(ProductEndpoints.getProducts) { mbVendorName =>
      val task = mbVendorName match {
        case Some(value) =>
          productService.getProducts.map(_.filter(_.vendorName === value))
        case None => productService.getProducts
      }
      task
    }

  val getProductByIdRoute: HttpRoutes[Task] =
    toRouteRecoverErrors(ProductEndpoints.getProductById) { productId =>
      val task = productService.getProductById(productId)
      task
    }

  val getProductsGroupedByVendorName: HttpRoutes[Task] =
    toRouteRecoverErrors(ProductEndpoints.getProductsGroupedByVendorName) {
      vendorName =>
        val task = productService.getProductsGroupedByVendorName
          .map(_.map { case (k, v) => (k.inner.value -> v) })
        task
    }

  val routes =
    createProductRoute <+> getProductsRoute <+> getProductByIdRoute <+> getProductsGroupedByVendorName
}

final class AppRoutes {
  val routes = Resource.eval(for {
    productService <- InMemoryProductService()
    routes = new ProductRoutes(productService).routes
  } yield routes)
}
