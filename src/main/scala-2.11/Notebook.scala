import  io.getquill._
import cats.data.Xor
import doobie.imports._, scalaz.effect.IO
import scalaz._, Scalaz._
import scalaz.concurrent.Task
import io.circe.Decoder.Result
import io.circe._, io.circe.generic.auto._, io.circe.jawn._, io.circe.syntax._
import scala.sys.process._
//import AST._
import PrettyPrinter.{print => printAst}



object Notebook {
  val db = io.getquill.source.mirror.mirrorSource

  val xa = DriverManagerTransactor[IO](
    "org.postgresql.Driver", "jdbc:postgresql:test", "postgres", "postgres"
  )

  def main(args: Array[String]) {
    case class Person(id: Int, name: String, age: Int)
    case class Contact(personId: Int, phone: String)

    val q1 = quote {
      query[Person]
    }


    val q: Quoted[EntityQuery[EntityQuery[(String, String)]]] = quote {
      for {
        p <- query[Person]
      } yield {
        for {
          c <- query[Contact]
        } yield (p.name, c.phone)
      }
    }

//    val ast = Comp(ListT(ScalarT(StringT)), Lit(ScalarT(StringT), ScalarV(StringV("Hello World"))), BindQ("x", Var(ListT(TupleT(List(ScalarT(IntT), ScalarT(IntT)))), "t")))
 //   val past = printAst(ast)

//    val q3 = "(let t = table(foo, [x::Int, y::Int], [[x]])::[(Int,Int)] in " ++ past ++ ")::[String]"
    val q5 = "(let t = table(person, [id::Int, name::String, age::Int], [[id]])::[(Int,String,Int)] in ([(([ (1337::Int) | xy <- (t::[(Int,String,Int)]) ]::[Int]),(42::Int))::([Int],Int) | xy <- (t::[(Int,String,Int)]) ]::[([Int],Int)]))::[([Int],Int)]"
    val q4 = "(let t = table(person, [id::Int, name::String, age::Int], [[id]])::[(Int,String,Int)] in ([(([ (((xy::(Int,String,Int)).3::Int) + ((xy::(Int,String,Int)).1::Int))::Int | xy <- (t::[(Int,String,Int)]) ]::[Int]),((xy::(Int,String,Int)).1::Int))::([Int],Int) | xy <- (t::[(Int,String,Int)]) ]::[([Int],Int)]))::[([Int],Int)]"
    val res = (s"echo $q4" #| "src/main/resources/flatsqlcomp").!!
    val d = jawn.parse(res)
    print(d)
    val c = d.getOrElse(Json.empty).cursor



    def getQuery(x: Cursor): Result[String] = {
      val n1 = for {
        m1 <- x.downField("contents")
        m2 <- m1.downArray
        m3 <- m2.first
      } yield m3.focus

      n1.getOrElse(Json.empty).as[String]
    }

    val y = getQuery(c) match { case Xor.Left(e) => ""; case Xor.Right(s) => s}
    val qu3 : String = y.map({ case '\n' => ' '; case c => c})

    val c2 = (for {
        m1 <- c.downField("contents")
        m2 <- m1.downArray
        m3 <- m2.last
    } yield m3).getOrElse(Json.empty.cursor)

    val qu4 = (getQuery(c2) match { case Xor.Left(e) => ""; case Xor.Right(s) => s}).map({ case '\n' => ' '; case c => c})

    def downOneLevel(c: Cursor) : Option[Cursor] = for {
      m1 <- c.downField("contents")
      m2 <- m1.downArray
      m3 <- m2.last
    } yield m3



    println("\n=========\n")
   println(qu4)
    println(qu3)

    //val proc = HC.process[(Int)](qu3, ().list[PreparedStatementIO])

   //def find(n: String) = sqlqu3).query[(Int)].list

   //print(proc.transact(xa).unsafePerformIO)

    val program3 =
      for {
        a <- sql"SELECT a0.id AS k1, a0.id AS o1 FROM person AS a0 ORDER BY o1 ASC;".query[(Int,Int)].list
        b <- sql"select random()".query[Double].unique
      } yield (a, b)
    val program4 =
      for {
        a <- sql"SELECT a0.id AS k1, a0.id AS o1, a0.id AS i1 FROM person AS a0 ORDER BY o1 ASC;".query[(Int,Int,Int)].list
        b <- sql"SELECT a1.id AS r1, a1.id AS o1, a2.id AS o2,       (a2.age + a2.id) AS i1 FROM person AS a1,     person AS a2 ORDER BY r1 ASC, o1 ASC, o2 ASC;".query[(Int,Int,Int,Int)].list
      } yield (a, b.groupBy(_._3))

    def createTuple[A,B] = ???

    def find(n: String): ConnectionIO[List[(Int,Int)]] =
      sql"SELECT a0.id AS k1, a0.id AS o1 FROM person AS a0 ORDER BY o1 ASC;".query[(Int,Int)].list

    print(program4.transact(xa).unsafePerformIO)

  }
}
