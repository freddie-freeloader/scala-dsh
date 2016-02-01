import AST._

object PrettyPrinter {

  def print(e: Expr) : String = e match {
      case AppE1(t,f,e1) => "(" + print(f) <> print(e1) + ")::" + print(t)
      case AppE2(t,f,e1,e2) => "(" + print(f) <> print(e1) <> print(e2) + ")::" + print(t)
      case BinOp(t,f,e1,e2) => "(" + print(e1) <> print(f) <> print(e2) + ")::" + print(t)
      case UnOp(t,f,e1) => "(" + print(f) <> print(e1) + ")"
      case If(t,e1,e2,e3) => "(if" <> print(e1) <> "then" <> print (e2) <> "else" <> print(e3) + ")::" + print(t)
      case Lit(t,v) => "(" + print(v) + "::" <> print(t) + ")"
      case Var(t,id) => "(" + print(id) + "::" + print(t) + ")"
      case Comp(t,c,q) => "[" <> print(c)  <> "|" <> print(q) <> "]::" + print(t)
      case MkTuple(t,xs) => "((" + print(xs) + ")::" + print(t) + ")"
      case Let(t,id,e1,e2) => "((let" <> print(id) <> "=" <> print(e1) <> "in" <> print(e2) + ")::" + print(t) + ")"
    }

  def print(xs: List[Expr]) : String = xs.map(print).intersperse(", ").foldRight("")(_++_)

  def print(t: TypeC) : String = t match {
    case ListT(t1) => "[" + print(t1) + "]"
    case TupleT(ts) => "(" + ts.map(print).intersperse(", ").foldRight("")(_++_) + ")"
    case ScalarT(tS) => tS match {
      case IntT => "Int"
      case BoolT => "Bool"
      case DoubleT => "Double"
      case StringT => "String"
      case UnitT => "()"
      case DateT => "Date"
    }
  }

  def print(f: Prim2) : String = f match {
    case Append => "append"
    case Zip => "zip"
  }
  def print(f: Prim1) : String = f match {
    case Length => "length"
    case Concat => "concat"
    case Null => "null"
    case Sum => "sum"
    case Avg => "avg"
    case Minimum => "minimum"
    case Maximum => "maximum"
    case Reverse => "reverse"
    case And => "and"
    case Or => "Or"
    case Nub => "nub"
    case Number => "number"
    case Sort => "Sort"
    case Group => "Group"
    case Only => "only"
    case Guard => "guard"
    case TupElem(i) => "????????????????TupElemIsPrinted in a special way???" //TODO
  }

  def print(q: Qual) : String = q match {
    case GuardQ(e) => print(e)
    case BindQ(i,e) => print(i) <> "<-" <> print(e)
  }

  def print(id: Identifier) : String = id

  def print(v: Val) : String = v match {
    case ListV(ys) => "[" + ys.map(print).intersperse(", ") + "]"
    case TupleV(ys) => "(" + ys.map(print).intersperse(", ") + ")"
    case ScalarV(y) => y match {
      case IntV(x) => x.toString
      case StringV(x) => "\"" + x + "\""
      case BoolV(x) => x.toString
      case DoubleV(x) => x.toString
      case DateV(x) => x.toString
      case UnitV => "()"
    }
  }

  def print(f: ScalarUnOp) : String = f match {
    case SUBoolOp(g) => g match {case Not => "not"}
    case SUCastOp(g) => g match {case CastDecimal => "decimal"; case CastDouble => "Double"}
    case SUTextOp(g) => g match {case SubString(i,s) => "subString_" + i.toString + "," + s}
    case SUDateOp(g) => g match {case DateDay => "dateDay"; case DateMonth => "dateMonth"; case DateYear => "dateYear"}
    case SUNumOp(g) => g match {
      case Sin => "sin"
      case Cos => "cos"
      case Tan => "tan"
      case Sqrt => "sqrt"
      case Exp => "exp"
      case Log => "log"
      case ASin => "asin"
      case ACos => "acos"
      case ATan => "atan"
    }
  }
  def print(f: ScalarBinOp) : String = f match {
    case SBBoolOp(g) => g match {case Conj => "&&"; case Disj => "||"} // Todo Parser

    case SBNumOp(g) => g match {
      case Add => "+"
      case Sub => "-"
      case Mul => "*"
      case Div => "/"
      case Mod => "%"
    }
    case SBRelOp(g) => g match {
      case Eq => "=="
      case Gt => ">"
      case GtE => ">="
      case Lt => "<"
      case LtE => "<="
      case NEq => "!="
    }
  }

  implicit class StringFunc(self: String) {
    def <>(other: String) = self + " " + other
  }

  implicit class MissingListFunc[A](xs: List[A]) {
    def intersperse(a: A): List[A] = xs match {
      case Nil => Nil
      case (x :: Nil) => List(x)
      case (y :: ys) => y :: a :: ys.intersperse(a)
    }
  }
}
