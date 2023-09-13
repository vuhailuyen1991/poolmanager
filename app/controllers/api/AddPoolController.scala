package controllers.api

import models.NewPool
import persistance.InMemoryPoolDatabase
import play.api.Configuration
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import javax.inject.Inject

class AddPoolController @Inject() (config: Configuration, c: ControllerComponents) extends AbstractController(c) {

  implicit val newPoolJson: OFormat[NewPool] = Json.format[NewPool]
  private val database = new InMemoryPoolDatabase()
  private val maxNumberOfPoolValues = config.get[Int]("maxNumberOfPoolValues")

  def addPool(): Action[AnyContent] = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson
    val maybePool: Option[NewPool] = jsonObject.flatMap(Json.fromJson[NewPool](_).asOpt)

    maybePool match {
      case Some(pool) =>
        if (pool.poolValues.size > maxNumberOfPoolValues)
          BadRequest
        else if (pool.poolValues.isEmpty)
          BadRequest
        else {
          if (database.get(pool.poolId).isEmpty) {
            database.add(pool.poolId, pool.poolValues)
            Created
          } else {
            database.add(pool.poolId, pool.poolValues)
            NoContent
          }

        }
      case None =>
        BadRequest
    }
  }

}
