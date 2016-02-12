/**
  * Just for prototyping
  */

object Expr {
  sealed trait Expr
  case class Static(s: String) extends Expr
}
