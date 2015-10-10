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
        // operands must be numeric and resulting type must be bool
        if( (a.getType().isNumeric()) && (b.getType().isNumeric()))
        {
            //System.out.println(a.getName()+b.getName() +" has typ: "+ a.getType().toString());
            return new ExprSTO(a.getName()+b.getName(), new BoolType());
        }
        else
        {
            STO err = (!(a.getType().isNumeric())) ? a : b;
            // should increment m_nNumErrors++; in MyParser
            if (a.getType().isNumeric())
                return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, b.getType().getName(),opName,a.getType().getName()));
            else
                return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, a.getType().getName(),opName,b.getType().getName()));
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
