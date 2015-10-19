/**
 * Created by jinyongsuk on 10/8/15.
 */
public class NotEqualOp extends ComparisonOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public NotEqualOp(String strName )
    {
        super(strName);
        setName(strName);
        //setSize(size);
    }

    STO checkOperands(STO a, STO b) {
        Type aType = a.getType();
        Type bType = b.getType();

        if(!(aType instanceof NumericType) && !(aType instanceof BoolType))
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1b_Expr, aType.getName(), m_OpName, bType.getName()));
        }
        if(!(bType instanceof NumericType) && !(bType instanceof BoolType))
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1b_Expr, aType.getName(), m_OpName, bType.getName()));
        }

        else if (((aType instanceof NumericType) && (bType instanceof NumericType))
                ||( aType instanceof BoolType && bType instanceof BoolType)) {
            //errro
            if (a.isConst() && b.isConst())
            {
                int val = a.getValue().compareTo(b.getValue());
                return new ConstSTO( a.getName()+ getName() + b.getName() , new BoolType(), val);
            }
            return new ExprSTO(a.getName() + getName()+ b.getName(), new BoolType());
        } else {
            //if it's not both integer then return error STO
            STO err = (!(aType instanceof BoolType) && !(aType instanceof NumericType)) ? b : a;
            // should increment m_nNumErrors++; in MyParser
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1b_Expr, err.getType().getName(), m_OpName, bType.getName()));
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
