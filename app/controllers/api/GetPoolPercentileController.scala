package controllers.api

import core.{ApachePercentileCalculator, SimplePercentileCalculator}
import models.{PoolPercentileQuery, PoolPercentileQueryResult}
import persistance.SingletonInMemoryPoolDatabase
import play.api.libs.json.{Json, OFormat}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.Inject

class GetPoolPercentileController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {
  private val database = SingletonInMemoryPoolDatabase()
  private val simplePercentileCalculator = new SimplePercentileCalculator()
  private val apachePercentileCalculator = new ApachePercentileCalculator()

  implicit val percentileQuery: OFormat[PoolPercentileQuery] = Json.format[PoolPercentileQuery]
  implicit val percentileQueryResult: OFormat[PoolPercentileQueryResult] = Json.format[PoolPercentileQueryResult]

  def getPoolPercentileValue(): Action[AnyContent] = Action { implicit request =>
    val content = request.body
    val jsonObject = content.asJson
    val maybeQuery: Option[PoolPercentileQuery] = jsonObject.flatMap(Json.fromJson[PoolPercentileQuery](_).asOpt)

    maybeQuery match {
      case Some(query) =>
        if (query.percentile > 100 || query.percentile < 0)
          BadRequest
        else {
          val maybePool = database.get(query.poolId)
          maybePool match {
            case Some(pool) =>
              val value = retrievePercentileValue(query.percentile, pool)
              Ok(Json.toJson(PoolPercentileQueryResult(pool.size, value)))
            case None => NotFound
          }
        }
      case None => BadRequest
    }
  }

  private def retrievePercentileValue(percentile: Float, data: Seq[Long]): Long =
    if (data.size < 100)
      simplePercentileCalculator.calculate(percentile, data)
    else
      apachePercentileCalculator.calculate(percentile, data)
}
