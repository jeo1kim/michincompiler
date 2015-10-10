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


        if (a.getType().isInt() && b.getType().isInt()) {
            //System.out.println(a.getName() + b.getName() + " has typ: " + a.getType().toString());
            if (a.isConst() && b.isConst())
            {
                return new ConstSTO( a.getName()+b.getName() , b.getType());
            }
            return new ExprSTO(a.getName() + b.getName(), a.getType());
        } else {
            //if it's not both integer then return error STO
            // should increment m_nNumErrors++; in MyParser
            if (a.getType().isInt())
                return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, b.getType().getName(),"%",a.getType().getName()));
            else
                return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, a.getType().getName(),"%",b.getType().getName()));
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
