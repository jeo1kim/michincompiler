/**
 * Created by jinyongsuk on 10/8/15.
 */
abstract class BitwiseOp extends BinaryOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public BitwiseOp(String strName )
    {
        super(strName);
        setName(strName);
        //setSize(size);
    }

    STO checkOperands(STO a, STO b, String opName) {
        Type aType = a.getType();
        Type bType = b.getType();


        if( !(aType instanceof intType))
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, aType.getName(),opName, "int"));
        }
        if(!(bType instanceof intType))
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, bType.getName(), opName, "int"));
        }

        else if ((aType instanceof intType) && (bType instanceof intType)) {
            //errro
            if (a.isConst() && b.isConst())
            {
                return new ConstSTO( a.getName()+ getName() + b.getName() , b.getType());
            }
            return new ExprSTO(a.getName() + getName()+ b.getName(), a.getType());
        } else {
            //if it's not both integer then return error STO
            STO err = (!(aType instanceof intType)) ? b : a;
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
