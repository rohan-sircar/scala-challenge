package wow.doge.app
import mytapir._
import sttp.model.StatusCode
import wow.doge.app.refinements.ProductId
import wow.doge.app.refinements.VendorName

object BaseEndpoint {
  val value = endpoint
    .in("api")
    .errorOut(
      oneOf[AppError](
        statusMappingValueMatcher(
          StatusCode.BadRequest,
          jsonBody[AppError].description("bad request")
        ) {
          case e: AppError.BadInput => true
          case _                    => false
        },
        statusDefaultMapping(
          jsonBody[AppError]
            .description(
              "Internal Error case implying some error case was uncaught"
            )
        )
      )
    )
}

object ProductEndpoints {
  val createProduct =
    BaseEndpoint.value.post
      .in("products")
      .in(jsonBody[RawNewDomainProduct])
      .out(jsonBody[CreateProductResponse])

  val getProducts = BaseEndpoint.value.get
    .in("products")
    .in(query[Option[VendorName]]("vendor"))
    .out(jsonBody[List[DomainProduct]])

  val getProductById = BaseEndpoint.value.get
    .in("products")
    .in(path[ProductId])
    .out(jsonBody[Option[DomainProduct]])

  val getProductsGroupedByVendorName = BaseEndpoint.value.get
    .in("products")
    .in("grouped")
    .out(jsonBody[Map[String, List[DomainProduct]]])

}
