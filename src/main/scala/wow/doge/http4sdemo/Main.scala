package wow.doge.app

import cats.effect.ExitCode
import cats.effect.Resource
import monix.bio.BIOApp
import monix.bio.Task
import monix.bio.UIO
import monix.execution.Scheduler

object Main extends BIOApp {
  val schedulers = Schedulers.default

  override protected def scheduler: Scheduler = schedulers.async.value

  val program = for {
    _ <- Resource.eval(Task.unit)
    appRoutes = new AppRoutes
    _ <- new Server(schedulers, appRoutes).resource
  } yield ()

  def run(args: List[String]) = {
    program
      .use(_ => Task.never)
      .onErrorHandleWith(ex => UIO(ex.printStackTrace()))
      .as(ExitCode.Success)
  }
}
