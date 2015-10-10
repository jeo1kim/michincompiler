/**
 * Created by jinyongsuk on 10/8/15.
 */
public class EqualOp extends ComparisonOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public EqualOp(String strName )
    {
        super(strName);
        setName(strName);
        //setSize(size);
    }

    STO checkOperands(STO a, STO b) {
        Type aType = a.getType();
        Type bType = b.getType();

        if (((aType.isNumeric()) && (bType.isNumeric()))
                || (aType.isBool() && bType.isBool()))
        {
            //System.out.println("Inside Equal Op");
            return new ExprSTO(a.getName() + b.getName(), new BoolType("bool", 4));
        } else if (a.getType().isBool() && b.getType().isBool()){
            return new ExprSTO(a.getName() + b.getName(), new BoolType("bool", 4));
        }
        else
        {
            //if it's not both integer then return error STO
            STO err = (!(a.getType().isNumeric())) ? b : a;
            // should increment m_nNumErrors++; in MyParser
            if (a.getType().isNumeric())
                return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, b.getType().getName(),"==",a.getType().getName()));
            else
                return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, a.getType().getName(),"==",b.getType().getName()));
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
