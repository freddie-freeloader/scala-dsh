import java.util.Date


/*
 * Defines Expression AST for CL
 */

object AST {

  sealed trait Expr

  // TODO case class Table(t: TypeC, name: String, L.BaseTableSchema) extends Expr
  case class AppE1(t: TypeC, f: Prim1, e: Expr) extends Expr
  case class AppE2(t: TypeC, f: Prim2, e1: Expr, e2: Expr) extends Expr
  case class BinOp(t: TypeC, f: ScalarBinOp, e1: Expr, e2: Expr) extends Expr
  case class UnOp(t: TypeC, f: ScalarUnOp, e: Expr) extends Expr
  case class If(t: TypeC, e1: Expr, e2: Expr, e3: Expr) extends Expr
  case class Lit(t: TypeC, v: Val) extends Expr
  case class Var(t: TypeC, n: Identifier) extends Expr
  case class Comp(t: TypeC, e: Expr, q: Qual) extends Expr //Todo Multiple Q
  case class MkTuple(t: TypeC, es: List[Expr]) extends Expr
  case class Let(t: TypeC, n: Identifier, e1: Expr, e2: Expr) extends Expr


  type Identifier = String

  sealed trait Val

    case class ListV(vs: List[Val]) extends Val

    case class TupleV(vs: List[Val]) extends Val

    case class ScalarV(v: ScalarVal) extends Val



  sealed trait ScalarVal

    case class IntV(v: Int) extends ScalarVal

    case class BoolV(v: Boolean) extends ScalarVal

    case class StringV(v: String) extends ScalarVal

    case class DoubleV(v: Double) extends ScalarVal

    case class DateV(v: Date) extends ScalarVal

    case object UnitV extends ScalarVal


  sealed trait Qual

  case class BindQ(id: Identifier,e: Expr) extends Qual
  case class GuardQ(e: Expr) extends Qual


  sealed trait ScalarUnOp

    case class SUNumOp(f: UnNumOp) extends ScalarUnOp

    case class SUBoolOp(f: UnBoolOp) extends ScalarUnOp

    case class SUCastOp(f: UnCastOp) extends ScalarUnOp

    case class SUTextOp(f: UnTextOp) extends ScalarUnOp

    case class SUDateOp(f: UnDateOp) extends ScalarUnOp



  sealed trait UnNumOp

    case object Sin extends UnNumOp

    case object Cos extends UnNumOp

    case object Tan extends UnNumOp

    case object ASin extends UnNumOp

    case object ACos extends UnNumOp

    case object ATan extends UnNumOp

    case object Sqrt extends UnNumOp

    case object Exp extends UnNumOp

    case object Log extends UnNumOp



  sealed trait UnBoolOp

    case object Not extends UnBoolOp



  sealed trait UnCastOp

    case object CastDouble extends UnCastOp

    case object CastDecimal extends UnCastOp



  sealed trait UnTextOp

    case class SubString(i: Int, j: Int) extends UnTextOp



  sealed trait UnDateOp

    case object DateDay extends UnDateOp

    case object DateMonth extends UnDateOp

    case object DateYear extends UnDateOp




  sealed trait ScalarBinOp

    case class SBNumOp(f: BinNumOp) extends ScalarBinOp

    case class SBRelOp(f: BinRelOp) extends ScalarBinOp

    case class SBBoolOp(f: BinBoolOp) extends ScalarBinOp

    case class SBStringOp(f: BinStringOp) extends ScalarBinOp

    case class SBDateOp(f: BinDateOp) extends ScalarBinOp



  sealed trait BinDateOp

    case object AddDays extends BinDateOp

    case object SubDays extends BinDateOp

    case object DiffDays extends BinDateOp



  sealed trait BinStringOp

    case object Like extends BinStringOp



  sealed trait BinBoolOp

    case object Conj extends BinBoolOp

    case object Disj extends BinBoolOp



  sealed trait BinRelOp

    case object Eq extends BinRelOp

    case object Gt extends BinRelOp

    case object GtE extends BinRelOp

    case object Lt extends BinRelOp

    case object LtE extends BinRelOp

    case object NEq extends BinRelOp



  sealed trait BinNumOp

    case object Add extends BinNumOp

    case object Sub extends BinNumOp

    case object Div extends BinNumOp

    case object Mul extends BinNumOp

    case object Mod extends BinNumOp




  sealed trait Prim1

    case object Length extends Prim1

    case object Concat extends Prim1

    case object Null extends Prim1

    case object Sum extends Prim1

    case object Avg extends Prim1

    case object Minimum extends Prim1

    case object Maximum extends Prim1

    case object Reverse extends Prim1

    case object And extends Prim1

    case object Or extends Prim1

    case object Nub extends Prim1

    case object Number extends Prim1

    case object Sort extends Prim1

    case object Group extends Prim1

  case object Only extends Prim1

    case object Guard extends Prim1

    case class TupElem(tupleIndex: Int) extends Prim1




  sealed trait Prim2

    case object Append extends  Prim2

    case object Zip extends Prim2




  sealed trait TypeC

    case class ListT(t: TypeC)
      extends TypeC

    case class TupleT(ts: List[TypeC])
      extends TypeC

    case class ScalarT(tScalar: ScalarType)
      extends TypeC



  sealed trait ScalarType

    case object IntT
      extends ScalarType

    case object BoolT
      extends ScalarType

    case object DoubleT
      extends ScalarType

    case object StringT
      extends ScalarType

    case object UnitT
      extends ScalarType

    case object DateT
      extends ScalarType




}
