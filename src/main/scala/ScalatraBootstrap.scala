import javax.servlet.ServletContext

import com.egen.pipelines.AppModule
import com.egen.pipelines.controller.{PipelineController, ResultLogsController}
import org.scalatra.LifeCycle
import scaldi.Injectable._

class ScalatraBootstrap extends LifeCycle {

  implicit val injector = new AppModule

    override def init(context : ServletContext): Unit = {
      context mount (inject[PipelineController], "/pipeline/*")
      context mount (inject[ResultLogsController], "/results/*")
    }
}
