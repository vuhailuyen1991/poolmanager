package core

import org.apache.commons.math3.stat.StatUtils


class ApachePercentileCalculator extends DefaultPercentileCalculator {

  override protected def calculatePercentile(percentile: Float, data: Seq[Long]): Long = {
    val value = StatUtils.percentile(data.toArray.map(_.toDouble), percentile)
    value.toLong
  }
}
