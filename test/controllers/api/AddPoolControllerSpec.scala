package controllers.api

import com.typesafe.config.{ConfigFactory, ConfigValueFactory}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import persistance.InMemoryPoolDatabase
import play.api.Configuration
import play.api.Play.materializer
import play.api.libs.json.{JsArray, Json}
import play.api.test._
import play.api.test.Helpers._


class AddPoolControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  private val database = new InMemoryPoolDatabase()

  "AddPoolController /api/pools POST" should {
    "add new pool to pool database" in {
      val config = new Configuration(simpleConfig)
      val controller = new AddPoolController(config, stubControllerComponents())
      val body = Json.obj(("poolId", 12), ("poolValues", Seq(1, 2, 3)))
      val request = FakeRequest(POST, "/api/pools").withJsonBody(body)

      val response = controller.addPool().apply(request)
      status(response) mustBe CREATED
      database.get(12) mustBe Some(Seq(1, 2, 3))
    }

    "update pool in pool database" in {
      val config = new Configuration(simpleConfig)
      val controller = new AddPoolController(config, stubControllerComponents())
      val body = Json.obj(("poolId", 13), ("poolValues", Seq(1, 2, 3)))
      val request = FakeRequest(POST, "/api/pools").withJsonBody(body)

      val response = controller.addPool().apply(request)
      status(response) mustBe CREATED
      database.get(13) mustBe Some(Seq(1, 2, 3))

      val body2 = Json.obj(("poolId", 13), ("poolValues", Seq(4, 5, 6)))
      val request2 = FakeRequest(POST, "/api/pools").withJsonBody(body2)

      val response2 = controller.addPool().apply(request2)
      status(response2) mustBe NO_CONTENT
      database.get(13) mustBe Some(Seq(1, 2, 3, 4, 5, 6))
    }

    "reject if request is malformed" in {
      val config = new Configuration(simpleConfig)
      val controller = new AddPoolController(config, stubControllerComponents())
      val body = Json.obj(("poolId", "malformed"), ("poolValues", Seq(1, 2, 3)))
      val request = FakeRequest(POST, "/api/pools").withJsonBody(body)

      val response = controller.addPool().apply(request)
      status(response) mustBe BAD_REQUEST
    }

    "reject if poolValues size is greater than maximum values allowed" in {
      val config = new Configuration(simpleConfig)
      val controller = new AddPoolController(config, stubControllerComponents())
      val body = Json.obj(("poolId", 12), ("poolValues", Seq(1, 2, 3, 4, 5)))
      val request = FakeRequest(POST, "/api/pools").withJsonBody(body)

      val response = controller.addPool().apply(request)
      status(response) mustBe BAD_REQUEST
    }

    "reject if poolValues is empty" in {
      val config = new Configuration(simpleConfig)
      val controller = new AddPoolController(config, stubControllerComponents())
      val body = Json.obj(("poolId", 12), ("poolValues", JsArray()))
      val request = FakeRequest(POST, "/api/pools").withJsonBody(body)

      val response = controller.addPool().apply(request)
      status(response) mustBe BAD_REQUEST
    }
  }


  private val simpleConfig = ConfigFactory.load()
    .withValue("maxNumberOfPoolValues", ConfigValueFactory.fromAnyRef(3))
}
