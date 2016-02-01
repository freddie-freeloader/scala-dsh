import cats.Show
import cats.syntax.show._

object Expr {
  sealed trait Expr {
    override def toString = this match {
      case Prototype(s) => s
    }
  }
  case class Prototype(s: String) extends Expr
}
