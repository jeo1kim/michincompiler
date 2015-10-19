import java.math.BigDecimal;

/**
 * Created by jinyongsuk on 10/8/15.
 */
abstract class BitwiseOp extends BinaryOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public BitwiseOp(String strName )
    {
        super(strName);
        setName(strName);
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

        if( !(aType instanceof intType))
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, aType.getName(),opName, "int"));
        }
        if(!(bType instanceof intType))
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, bType.getName(), opName, "int"));
        }

        else if ((aType instanceof intType) && (bType instanceof intType)) {
            //errro
            if (a.isConst() && b.isConst())
            {
                int val = calculate(a.getIntValue(), b.getIntValue(), opName);
                ConstSTO ret =  new ConstSTO( Integer.toString(val) , b.getType(), val);
                ret.markRVal();
                return ret;
            }
            ExprSTO ret = new ExprSTO(a.getName() + getName()+ b.getName(), a.getType());
            ret.markRVal();
            return ret;
        } else {
            //if it's not both integer then return error STO
            STO err = (!(aType instanceof intType)) ? b : a;
            // should increment m_nNumErrors++; in MyParser
            return new ErrorSTO(err.getName());
        }
    }

    public int calculate (int a, int b, String opname){

        int result= 999;

        switch (opname) {
            case "|": result = a|b;
                break;
            case "&": result =  a&b;
                break;
            case "^": result = a^b;
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
