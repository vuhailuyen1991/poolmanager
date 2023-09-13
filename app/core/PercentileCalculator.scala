package core

trait PercentileCalculator {
  def calculate(percentile: Float, data: Seq[Long]): Long
}
