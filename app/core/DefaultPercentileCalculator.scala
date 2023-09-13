package core

abstract class DefaultPercentileCalculator extends PercentileCalculator {
  override def calculate(percentile: Float, data: Seq[Long]): Long = {
    validateInputs(percentile, data)
    calculatePercentile(percentile, data)
  }

  protected def calculatePercentile(percentile: Float, data: Seq[Long]): Long

  private def validateInputs(percentile: Float, data: Seq[Long]): Unit = {
    require(data.nonEmpty)
    require(0 <= percentile && percentile <= 100)
  }
}
