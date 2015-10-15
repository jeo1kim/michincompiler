/**
 * Created by jinyongsuk on 10/8/15.
 */
abstract class ComparisonOp extends BinaryOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public ComparisonOp(String strName )
    {
        super(strName);
        //setSize(size);
    }

    STO checkOperands(STO a, STO b, String opName)
    {
        Type aType = a.getType();
        Type bType = b.getType();

        if(!(aType instanceof NumericType) && !a.isError())
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr, aType.getName(),opName));
        }
        if(!(bType instanceof NumericType) && !b.isError())
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr, bType.getName(), opName));
        }


        // operands must be numeric and resulting type must be bool
        if( (aType instanceof NumericType) && (bType instanceof NumericType))
        {
            //System.out.println(a.getName()+b.getName() +" has typ: "+ a.getType().toString());
            return new ExprSTO(a.getName()+b.getName(), new BoolType());
        }
        else {
            STO err = (!(aType instanceof NumericType)) ? a : b;
            // should increment m_nNumErrors++; in MyParser

            /*
            public static final String error1n_Expr  =
                "Incompatible type %T to binary operator %O, numeric expected.";
            * */
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr, err.getType().getName(), opName));
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
