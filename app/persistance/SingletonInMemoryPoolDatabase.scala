package persistance

import scala.collection.mutable
import java.util.concurrent.ConcurrentHashMap
import scala.jdk.CollectionConverters.ConcurrentMapHasAsScala

class SingletonInMemoryPoolDatabase extends PoolDatabase {
  override def get(id: Long): Option[Seq[Long]] =
    Pools.map.get(id).map(_.toSeq)

  override def add(id: Long, elements: Seq[Long]): Unit =
    if (Pools.map.contains(id))
      Pools.map(id) = Pools.map(id).appendedAll(elements)
    else
      Pools.map += (id -> mutable.Seq.empty[Long].appendedAll(elements))


  override def truncate(): Unit = Pools.map.clear()

  override def size(): Long = Pools.map.size
}

object SingletonInMemoryPoolDatabase {
  def apply(): SingletonInMemoryPoolDatabase = new SingletonInMemoryPoolDatabase()
}

private object Pools {
  var map = new ConcurrentHashMap[Long, mutable.Seq[Long]]().asScala
}