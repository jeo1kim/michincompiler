import java.math.BigDecimal;

/**
 * Created by jinyongsuk on 10/8/15.
 */
abstract class BooleanOp extends BinaryOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public BooleanOp(String strName )
    {
        super(strName);
        //setSize(size);
    }

    STO checkOperands(STO a, STO b, String opName) {
        Type aType = a.getType();
        Type bType = b.getType();

        if (b.isError()){
            return b;
        }if (a.isError()){
            return a;
        }

        if(!(aType instanceof BoolType))
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, a.getType().getName(),opName, "bool"));
        }
        if(!(bType instanceof BoolType))
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, b.getType().getName(), opName, "bool"));
        }

        else if ((aType instanceof BoolType) && (bType instanceof BoolType)) {
            //errro
            if (a.isConst() && b.isConst())
            {
                boolean result = calculate(a.getBoolValue(), b.getBoolValue(), opName);
                int val = 0;
                if(result){
                    val = 1;
                }
                return new ConstSTO( a.getName()+ getName() + b.getName() , b.getType(), val);
            }
            return new ExprSTO(a.getName() + getName()+ b.getName(), a.getType());
        } else {
            //if it's not both integer then return error STO
            STO err = (!(aType instanceof BoolType)) ? b : a;
            // should increment m_nNumErrors++; in MyParser
            return new ErrorSTO(err.getName());
        }
    }

    public boolean calculate (boolean a, boolean b, String opname){

        boolean result = false;

        switch (opname) {
            case "||": result = a||b;
                break;
            case "&&": result = a&&b;
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
