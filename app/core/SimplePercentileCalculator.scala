package core


class SimplePercentileCalculator extends DefaultPercentileCalculator {

  override protected def calculatePercentile(percentile: Float, data: Seq[Long]): Long = {
    val sortedData = data.sorted
    val index = math.ceil((data.length - 1) * (percentile / 100.0)).toInt
    sortedData(index)
  }
}
