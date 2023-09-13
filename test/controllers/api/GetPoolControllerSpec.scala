package controllers.api

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import persistance.InMemoryPoolDatabase
import play.api.Play.materializer
import play.api.libs.json.Json
import play.api.test._
import play.api.test.Helpers._

class GetPoolControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  private val database = new InMemoryPoolDatabase()

  "GetPoolController /api/pools/:id GET" should {
    "get pool from pool database" in {
      database.add(1, Seq(1, 2, 3))
      val controller = new GetPoolController(stubControllerComponents())
      val request = FakeRequest(GET, "api/pools/1")
      val response = controller.getPoolById(1).apply(request)

      status(response) mustBe OK
      contentAsString(response) mustBe "[1,2,3]"
    }

    "reject if poolId is not an integer" in {
      val controller = new GetPoolController(stubControllerComponents())
      val request = FakeRequest(GET, "api/pools/aaa")
      val response = controller.getPoolById(1).apply(request)

      status(response) mustBe OK
      contentAsString(response) mustBe "[1,2,3]"
    }

    "return 404 if pool is not found" in {
      val controller = new GetPoolController(stubControllerComponents())
      val request = FakeRequest(GET, "api/pools/2")
      val response = controller.getPoolById(2).apply(request)

      status(response) mustBe NOT_FOUND
    }
  }

}
