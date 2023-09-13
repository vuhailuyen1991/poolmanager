package controllers.api

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import persistance.InMemoryPoolDatabase
import play.api.Play.materializer
import play.api.libs.json.Json
import play.api.test._
import play.api.test.Helpers._

class GetPoolPercentileControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  private val database = new InMemoryPoolDatabase()

  "GetPoolPercentileController /api/pools/percentile POST" should {
    "get pool percentile for a pool" in {
      database.add(1, Seq(1, 2, 3, 4, 5, 6))
      val controller = new GetPoolPercentileController(stubControllerComponents())
      val body = Json.obj(("poolId", 1), ("percentile", 50))
      val request = FakeRequest(POST, "api/pools/percentile").withJsonBody(body)
      val response = controller.getPoolPercentileValue().apply(request)

      status(response) mustBe OK
      contentAsString(response) mustBe """{"count":9,"value":3}"""
    }

    "return 404 if pool is not found" in {
      val controller = new GetPoolPercentileController(stubControllerComponents())
      val body = Json.obj(("poolId", 12), ("percentile", 50))
      val request = FakeRequest(POST, "api/pools/percentile").withJsonBody(body)
      val response = controller.getPoolPercentileValue().apply(request)

      status(response) mustBe NOT_FOUND
    }

    "reject if request is malformed" in {
      val controller = new GetPoolPercentileController(stubControllerComponents())
      val body = Json.obj(("poolId", "malformed"), ("percentile", 50))
      val request = FakeRequest(POST, "api/pools/percentile").withJsonBody(body)
      val response = controller.getPoolPercentileValue().apply(request)

      status(response) mustBe BAD_REQUEST
    }
  }
}
