/**
 * Created by jinyongsuk on 10/8/15.
 */
public class ModulOp extends ArithmeticOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public ModulOp(String strName )
    {
        super(strName);
        setName(strName);
        //setSize(size);
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public String getName()
    {
        return m_OpName;
    }

    //both operands must be integer otherwise return error
    STO checkOperands(STO a, STO b) {


        Type aType = a.getType();
        Type bType = b.getType();

        if (aType instanceof intType && bType instanceof intType) {

            if (a.isConst() && b.isConst())
            {
                return new ConstSTO( a.getName() + b.getName() , b.getType());
            }

            return new ExprSTO(a.getName()+ b.getName(), a.getType());
        }

        else {
            //if it's not both integer then return error STO
            // should increment m_nNumErrors++; in MyParser

            //"Incompatible type %T to binary operator %O, equivalent to %T expected.";
            if (aType instanceof intType)
                return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, b.getType().getName(), m_OpName ,a.getType().getName()));
            else
                return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, a.getType().getName(), m_OpName ,b.getType().getName()));
        }
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    private void setName(String str)
    {
        m_OpName = str;
    }
}
