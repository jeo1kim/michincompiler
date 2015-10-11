/**
 * Created by jinyongsuk on 10/8/15.
 */
public abstract class ArithmeticOp extends BinaryOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public ArithmeticOp(String strName )
    {
        super(strName);
        setName(strName);
        //setSize(size);
    }

    STO checkOperands(STO a, STO b, String opName)
    {
        //System.out.println(a.getType().isNumeric());
        Type aType = a.getType();
        Type bType = b.getType();

        if( !(aType instanceof NumericType) || !(bType instanceof NumericType) )
        {
            // should increment m_nNumErrors++; in MyParser
            if ( aType instanceof NumericType)
                return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr, b.getType().getName(),opName));
            else
                return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr, a.getType().getName(), opName));
        }

        else if (a.isConst() && b.isConst())
        {
            return new ConstSTO( a.getName()+b.getName() , b.getType());
        }

        else {
            STO c = !(aType instanceof intType) ? b : a;
            return new ExprSTO(a.getName()+b.getName(), c.getType());
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
