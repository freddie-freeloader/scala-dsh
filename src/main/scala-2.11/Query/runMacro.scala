package Query

import AST._
import scala.reflect.macros.whitebox.Context


object runMacro {

  /**
  def runimpl[A](c: Context)(q: Query[A]) : Option[A] /* TODO Should be typed*/ = {
    import c.universe._
    q"$q" match {
        // TODO Figure out, how to do proper recursion with macros
      case q"Bind($e,$f)" => Some(":\"(")
    }
  }
    */

  implicit class StringFunc(self: String) {
    def <>(other: String) = self + " " + other
  }
}
