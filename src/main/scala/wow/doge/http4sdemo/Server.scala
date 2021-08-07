package wow.doge.app

import org.http4s.ember.server.EmberServerBuilder
import monix.bio.Task
import org.http4s.implicits._

final class Server(schedulers: Schedulers, appRoutes: AppRoutes) {
  val resource = for {
    httpRoutes <- appRoutes.routes
    server <- EmberServerBuilder
      .default[Task]
      .withHost("0.0.0.0")
      .withPort(8081)
      .withHttpApp(httpRoutes.orNotFound)
      .withBlocker(schedulers.io.blocker)
      .build
  } yield server
}
