/**
 * Created by jinyongsuk on 10/8/15.
 */
abstract class BooleanOp extends BinaryOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public BooleanOp(String strName )
    {
        super(strName);
        //setSize(size);
    }

    STO checkOperands(STO a, STO b, String opName) {
        Type aType = a.getType();
        Type bType = b.getType();


        if( !(aType instanceof BoolType) || !(bType instanceof BoolType) )
        {
            // should increment m_nNumErrors++; in MyParser
            if ( aType instanceof NumericType)
                return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, b.getType().getName(),opName,a.getType().getName()));
            else
                return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, a.getType().getName(), opName, b.getType().getName()));
        }
        else if ((aType instanceof BoolType) && (bType instanceof BoolType)) {
            //errro
            if (a.isConst() && b.isConst())
            {
                return new ConstSTO( a.getName()+ getName() + b.getName() , b.getType());
            }
            return new ExprSTO(a.getName() + getName()+ b.getName(), a.getType());
        } else {
            //if it's not both integer then return error STO
            STO err = (!(aType instanceof BoolType)) ? b : a;
            // should increment m_nNumErrors++; in MyParser
            return new ErrorSTO(err.getName());
        }
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public String getName()
    {
        return m_OpName;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    private void setName(String str)
    {
        m_OpName = str;
    }
}
