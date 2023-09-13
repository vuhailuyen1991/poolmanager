package core

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class DefaultPercentileCalculatorSpec extends AnyFunSpec with Matchers {
  val percentileCalculator = new DefaultPercentileCalculator {
    var hasCalculatedPercentile = false

    override protected def calculatePercentile(percentile: Float, data: Seq[Long]): Long = {
      hasCalculatedPercentile = true
      1
    }
  }

  describe("DefaultPercentileCalculatorTest") {
    it("should throw an error if percentile is > 100") {
      val sample = Seq(7, 34, 39, 18, 16, 17, 21).map(_.toLong)
      val exception = intercept[IllegalArgumentException](percentileCalculator.calculate(110, sample))
      exception.getMessage shouldBe "requirement failed"
      percentileCalculator.hasCalculatedPercentile shouldBe false
    }

    it("should throw an error if percentile is < 0") {
      val sample = Seq(7, 34, 39, 18, 16, 17, 21).map(_.toLong)
      val exception = intercept[IllegalArgumentException](percentileCalculator.calculate(-1, sample))
      exception.getMessage shouldBe "requirement failed"
      percentileCalculator.hasCalculatedPercentile shouldBe false
    }

    it("should throw an error if sample is empty") {
      val sample = Seq.empty
      val exception = intercept[IllegalArgumentException](percentileCalculator.calculate(12, sample))
      exception.getMessage shouldBe "requirement failed"
      percentileCalculator.hasCalculatedPercentile shouldBe false
    }

    it("should call the calculatePercentile method") {
      val sample = Seq(1, 2, 3).map(_.toLong)
      percentileCalculator.calculate(2, sample)
      percentileCalculator.hasCalculatedPercentile shouldBe true
    }
  }
}
