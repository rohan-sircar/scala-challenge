package wow.doge.app
import cats.data.NonEmptyList
import io.circe.Codec

sealed trait AppError extends Exception {
  def errors: NonEmptyList[String]
}
object AppError {
  import io.circe.generic.extras.Configuration

  implicit val genDevConfig: Configuration =
    Configuration.default.withDiscriminator("kind")

  implicit val codec: Codec[AppError] = {
    import io.circe.generic.extras.semiauto._
    deriveConfiguredCodec[AppError]
  }

  final case class BadInput(errors: NonEmptyList[String]) extends AppError

}
