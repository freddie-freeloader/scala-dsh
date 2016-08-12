package Query

import AST._
import scala.reflect.macros.whitebox.Context


object runMacro {

  def runimpl[T](c: Context)(q: c.Expr[Query[T]]) : c.Expr[String] /* TODO Should be typed*/ = {
    import c.universe._
    reify(showRaw(q))
    /*
    q"$q" match {
        // TODO Figure out, how to do proper recursion with macros
      case q"Bind($e,$f)" => Some(":\"(")
    }
    */
  }

  implicit class StringFunc(self: String) {
    def <>(other: String) = self + " " + other
  }
}
