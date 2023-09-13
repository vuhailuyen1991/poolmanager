package persistance

import scala.collection.mutable

class InMemoryPoolDatabase extends PoolDatabase {
  override def get(id: Long): Option[Seq[Long]] =
    Pools.map.get(id).map(_.toSeq)

  override def add(id: Long, elements: Seq[Long]): Unit = synchronized {
    if (Pools.map.contains(id))
      Pools.map(id) = Pools.map(id).appendedAll(elements)
    else
      Pools.map += (id -> mutable.Seq.empty[Long].appendedAll(elements))
  }

  override def truncate(): Unit = Pools.map.clear()

  override def size(): Long = Pools.map.size
}

private object Pools {
  val map = new mutable.HashMap[Long, mutable.Seq[Long]]()
}