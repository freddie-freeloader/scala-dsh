import Driver._
import io.getquill._
import doobie.imports._, scalaz.effect.IO


object Prototype {
  def main(args: Array[String]) : Unit = {

    case class Person(id: Int, name: String, age: Int)

    val xa: Transactor[IO] = DriverManagerTransactor[IO](
      "org.postgresql.Driver", "jdbc:postgresql:test", "postgres", "postgres"
    )

    val q = quote { 42 }

    // Note: toCl is not implemented yet and ignores ast!
    print(compQuery(toJson(toCL(q.ast)),xa))
  }
}
