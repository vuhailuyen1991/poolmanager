package core

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class SimplePercentileCalculatorSpec extends AnyFunSpec with Matchers {
  val percentileCalculator = new SimplePercentileCalculator()
  describe ("SmallDatasetPercentileCalculatorTest") {
    it ("should calculate correct value given a percentile") {
      val sample = Seq(7, 34, 39, 18, 16, 17, 21, 36, 17, 2, 4, 39, 4, 19, 2, 12, 35, 13, 40, 37).map(_.toLong)
      val value = percentileCalculator.calculate(11, sample)
      value shouldBe 4
    }
  }
}
