import java.math.BigDecimal;

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

        if (b.isError()){
            return b;
        }
        if (a.isError()){
            return a;
        }


        if(!(aType instanceof NumericType))
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr, aType.getName(),opName));
        }
        if(!(bType instanceof NumericType))
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr, bType.getName(), opName));
        }


        // operands must be numeric and resulting type must be bool
        if( (aType instanceof NumericType) && (bType instanceof NumericType))
        {
            if (a.isConst() && b.isConst())
            {
                int result = calculate(a.getValue(), b.getValue(),opName);
                ConstSTO ret = new ConstSTO( a.getName()+ getName() + b.getName() , new BoolType(), result);
                ret.markRVal();
                return ret;
            }
            ExprSTO ret = new ExprSTO(a.getName() + getName()+ b.getName(), new BoolType());
            return ret;
        }
        else {
            STO err = (!(aType instanceof NumericType)) ? a : b;
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1n_Expr, err.getType().getName(), opName));
        }

    }

    public int calculate (BigDecimal a, BigDecimal b, String opname){

        int result = 9999999;

        switch (opname) {
            case "<": result = a.compareTo(b);
                break;
            case "<=": result =  a.compareTo(b);
                break;
            case ">": result = a.compareTo(b);
                break;
            case ">=": result = a.compareTo(b);
                break;
        }

        return result;
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
