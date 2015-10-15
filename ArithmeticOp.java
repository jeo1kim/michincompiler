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

        if( !(aType instanceof NumericType) && !a.isError())
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr, aType.getName(),opName));
        }
        if(!(bType instanceof NumericType) && !b.isError())
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr, bType.getName(), opName));
        }

        else if (a.isConst() && b.isConst())
        {
            // need to check float <=> int
            return new ConstSTO( a.getName()+b.getName() , b.getType());
        }

        else{
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
