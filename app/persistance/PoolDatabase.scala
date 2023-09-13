package persistance

trait PoolDatabase {
  def get(id: Long): Option[Seq[Long]]
  def add(id: Long, elements: Seq[Long]): Unit
  def truncate(): Unit
  def size(): Long
}
