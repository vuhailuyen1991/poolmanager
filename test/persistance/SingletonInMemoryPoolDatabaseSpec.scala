package persistance

import org.scalatest.BeforeAndAfterEach
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class SingletonInMemoryPoolDatabaseSpec extends AnyFunSpec with Matchers with BeforeAndAfterEach {
  private val database = new SingletonInMemoryPoolDatabase()

  override def afterEach() = {
    database.truncate()
    super.afterEach()
  }

  describe("InMemoryPoolDatabaseTest") {

    describe("Insert") {
      it ("should insert elements to pools") {
        database.get(1) shouldBe None
        database.add(1, Seq(1, 23, 4))
        database.get(1) shouldBe Some(Seq(1, 23, 4))
      }

      it ("should correctly add elements to an existing pool") {
        database.get(1) shouldBe None
        database.add(1, Seq(1, 23, 4))
        database.add(1, Seq(5, 4))
        database.get(1) shouldBe Some(Seq(1, 23, 4, 5, 4))
      }
    }

    describe("Get") {
      it ("should return None if poolId does not exist") {
        database.get(1) shouldBe None
        database.get(2) shouldBe None
      }

      it ("should correctly get pools by poolId") {
        database.add(1, Seq(1, 23, 4))
        database.get(1) shouldBe Some(Seq(1, 23, 4))
      }
    }

    describe("Truncate") {
      it ("should correctly truncate all pools") {
        database.add(1, Seq(1, 2, 4))
        database.add(2, Seq(1, 4, 5))
        database.size() shouldBe 2
        database.truncate()
        database.get(1) shouldBe None
        database.get(2) shouldBe None
        database.size() shouldBe 0
      }
    }
  }
}
