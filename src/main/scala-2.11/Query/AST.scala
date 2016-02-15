package Query
import cats._
import cats.syntax.monadFilter._
import cats.syntax.functor._
import cats.syntax.flatMap._
import runMacro._
import scala.language.experimental.macros
import scala.reflect.runtime.universe._


object AST {

  // TODO Type lambdas needed for *instance Monad (Query s)*
  sealed trait Query[+T]

  case class Scalar[T](x: T) extends Query[T]
  case class FlatMap[S,T](f: S => T, e: Query[T]) extends Query[T]
  case class Bind[S,T](e: Query[S], f: S => Query[T]) extends Query[T]
  case object Null extends Query[Nothing]

  trait withKey[K] {
    val key : K
  }

  case class Person(id : Int, name: String)

  case class Table[T](k: List[SQLType])(implicit tag: TypeTag[T]) extends Query[T] {
    val key = k
  }

  def getKey[T](t: Table[T]) : List[SQLType] = t.key


  implicit def queryMonadFilter : MonadFilter[Query] =
    new MonadFilter[Query] {

      override def empty[A]: Query[A] = Null

      def pure[A](a: A) : Query[A] = Scalar(a)

      def flatMap[A,B](fa: Query[A])(f: A => Query[B]) : Query[B] = Bind(fa, f)
    }

  /**
  def eval[A](q: Query[A]) : Option[A] = q match {
    case Scalar(a) => Some(a)
    case FlatMap(f,e) => eval(e).flatMap(x =>Some(f(x)))
    case Bind(e,f) => for {
      x0 <- eval(e)
      x1 <- eval(f(x0))
    } yield x1
    case Null => None
  }
    */

  case class CL[A](cl: String, q: Query[A])

  trait SQLType
  object IntS extends SQLType
  object StringS extends SQLType



  val q2 = for {
     p <- Table[Person](List(IntS,StringS)) : Query[Person]
     n <- Scalar(38) : Query[Int]
  } yield n + p.id

  val q = for {
    x <- Scalar(38) : Query[Int] if x > 0
    y <- Scalar(4) : Query[Int]
  } yield x + y


}
