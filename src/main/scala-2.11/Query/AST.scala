package Query
import cats._
import cats.syntax.monadFilter._
import cats.syntax.functor._
import cats.syntax.flatMap._

object AST {

  sealed trait Query[+T]

  case class Scalar[T](x: T) extends Query[T]
  case class FlatMap[S,T](f: S => T, e: Query[S]) extends Query[T]
  case class Bind[S,T](e: Query[S], f: S => Query[T]) extends Query[T]
  case object Null extends Query[Nothing]

  implicit def queryMonadFilter : MonadFilter[Query] =
    new MonadFilter[Query] {

      override def empty[A]: Query[A] = Null

      def pure[A](a: A) : Query[A] = Scalar(a)

      def flatMap[A,B](fa: Query[A])(f: A => Query[B]) : Query[B] = Bind(fa, f)
    }

  def run[A](q: Query[A]) : Option[A] = q match {
    case Scalar(a) => Some(a)
    case FlatMap(f,e) => run(e).flatMap(x =>Some(f(x)))
    case Bind(e,f) => for {
      x0 <- run(e)
      x1 <- run(f(x0))
    } yield x1
    case Null => None
  }

  val q = for {
    x <- Scalar(38) : Query[Int] if x > 0
    y <- Scalar(4) : Query[Int]
  } yield x + y
}
