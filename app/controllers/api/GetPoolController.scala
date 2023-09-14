package controllers.api

import core.{ApachePercentileCalculator, SimplePercentileCalculator}
import models.{PoolPercentileQuery, PoolPercentileQueryResult}
import persistance.SingletonInMemoryPoolDatabase
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.Inject

class GetPoolController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  private val database = SingletonInMemoryPoolDatabase()

  def getPoolById(id: Long): Action[AnyContent] = Action {
    val poolValues = database.get(id)
    poolValues match {
      case Some(values) => Ok(Json.toJson(values))
      case None => NotFound
    }
  }
}
