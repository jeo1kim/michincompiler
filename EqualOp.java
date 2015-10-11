import com.sun.org.apache.xpath.internal.operations.Bool;

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

        if (((aType instanceof NumericType) && (bType instanceof NumericType))
                || (aType instanceof BoolType && bType instanceof BoolType))
        {
            //System.out.println("Inside Equal Op");
            return new ExprSTO(a.getName() + b.getName(), new BoolType("bool", 4));
        } else if (a.getType().isBool() && b.getType().isBool()){
            return new ExprSTO(a.getName() + b.getName(), new BoolType("bool", 4));
        }
        else
        {
            //if it's not both integer then return error STO
            //STO err = (!(a.getType().isNumeric())) ? a : b;
            // should increment m_nNumErrors++; in MyParser
            //"Incompatible types to operator: %T %O %T;\n  both must be numeric, or both equivalent to bool.";

            if( aType instanceof NumericType && !(bType instanceof NumericType))
            {
                return new ErrorSTO(Formatter.toString(ErrorMsg.error1b_Expr, a.getType().getName(), m_OpName, b.getType().getName()));
            }

            else if (!(aType instanceof  NumericType) && bType instanceof NumericType)
            {
                return new ErrorSTO(Formatter.toString(ErrorMsg.error1b_Expr, b.getType().getName(), m_OpName, a.getType().getName()));
            }
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1b_Expr, b.getType().getName(),m_OpName,a.getType().getName()));
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
