package wow.doge.app

import scala.concurrent.duration.MILLISECONDS

import cats.effect.ExitCode
import cats.effect.Resource
import cats.syntax.all._
import monix.bio.BIOApp
import monix.bio.IO
import monix.bio.Task
import monix.bio.UIO
import monix.execution.Scheduler
import io.odin.consoleLogger

object Main extends BIOApp {
  val schedulers = Schedulers.default

  override protected def scheduler: Scheduler = schedulers.async.value

  val program = for {
    startTime <- Resource.eval(IO.clock.realTime(MILLISECONDS))
    _ <- Resource.eval(Task(println("""
    |        .__     __    __            _____                      .___                     
    |        |  |___/  |__/  |_______   /  |  |  ______           __| _/____   _____   ____  
    |        |  |  \   __\   __\____ \ /   |  |_/  ___/  ______  / __ |/ __ \ /     \ /  _ \ 
    |        |   Y  \  |  |  | |  |_> >    ^   /\___ \  /_____/ / /_/ \  ___//  Y Y  (  <_> )
    |        |___||_|__|  |__| |   __/\____   |/______|         \_____|\____/\__|_|_/ \____/ 
    |                          |__|        |__|                                        
     """.stripMargin)))
    // logger = consoleLogger[Task]()
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
