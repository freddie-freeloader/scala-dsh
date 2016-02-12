import Expr._
import cats.data.Xor
import io.circe._, io.circe.Json
import io.circe.Decoder.Result
import io.getquill.ast.Ast
import scala.sys.process._
import doobie.imports._, scalaz.effect.IO
import scalaz._, Scalaz._

/**
  * Collection of functions for Prototype
  */
object Driver {

  def toCL(ast: Ast) : Expr = ast match {
    case _ => Static("(let t = table(person, [id::Int, name::String, age::Int], [[id]])::[(Int,String,Int)] in ([(([ (((xz::(Int,String,Int)).3::Int) + ((xy::(Int,String,Int)).1::Int))::Int | xz <- (t::[(Int,String,Int)]) ]::[Int]),((xy::(Int,String,Int)).1::Int))::([Int],Int) | xy <- (t::[(Int,String,Int)]) ]::[([Int],Int)]))::[([Int],Int)]")
  }

  def toJson(e: Expr) : Json = {
    val exprString : String = e.toString
    val raw : String = (s"echo $exprString" #| "src/main/resources/flatsqlcomp").!!

    jawn.parse(raw).getOrElse(Json.empty)
  }

  def compQuery(json: Json, xa: Transactor[IO]) = {
    def c = json.cursor
    def q1 = getString(getQuery(c))
    def c2 = for {
      c1 <- downOneLevel(c)
      c2 <- c1.downField("contents")
      c3 <- c2.downArray
      c4 <- c3.first
    } yield c4
    def q2 = getString(getQuery(c2.getOrElse(Json.empty.cursor)))

    println("Resulting queries:")
    println(q1)
    println(q2)

    val fQ = for {
      a <- HC.process[(Int,Int,Int)](q1, ().point[PreparedStatementIO]).list
      b <- HC.process[(Int,Int,Int)](q2, ().point[PreparedStatementIO]).list
    } yield (a,b)

    val res = fQ.transact(xa).unsafePerformIO

    res match { case (a,b) => a.map((x : (Int,Int,Int)) => (b.groupBy(_._3).getOrElse(x._1,Nil),x._1))   }
  }


  def getString(res: Result[String]) : String = res match {
    case Xor.Left(e) => ""
    case Xor.Right(s : String) => s.map({ case '\n' => ' '; case c => c })
  }

  def downOneLevel(c: Cursor) : Option[Cursor] = for {
        m1 <- c.downField("contents")
        m2 <- m1.downArray
        m3 <- m2.last
    } yield m3

  def getQuery(x: Cursor): Result[String] = {
    val n1 = for {
      m1 <- x.downField("contents")
      m2 <- m1.downArray
      m3 <- m2.first
    } yield m3.focus



    n1.getOrElse(Json.empty).as[String]
  }


}
