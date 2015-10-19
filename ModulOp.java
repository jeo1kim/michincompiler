import java.math.BigDecimal;

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

        if (b.isError()){
            return b;
        }if (a.isError()){
            return a;
        }
        //System.out.println(a.getType().isNumeric());
        Type aType = a.getType();
        Type bType = b.getType();

        if(!(aType.isInt()))
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, aType.getName(), m_OpName , "int"));
        }
        else if(!(bType.isInt()))
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, bType.getName(), m_OpName, "int"));
        }

        else if (aType.isInt() && bType.isInt()) {
            if (a.isConst() && b.isConst()) {

                if ((b.getType().isInt() && b.getIntValue() == 0) || (b.getType().isFloat() && b.getFloatValue() == 0.0)) {
                    return new ErrorSTO(ErrorMsg.error8_Arithmetic);
                }
                BigDecimal result = a.getValue().remainder(b.getValue());

                ConstSTO ret = new ConstSTO(Integer.toString(result.intValue()), b.getType(), result.intValue());
                ret.markRVal();
                return ret;
            }
            ExprSTO ret = new ExprSTO(a.getName()+ b.getName(), a.getType());
            ret.markRVal();
            return ret;
        }
        else{
            STO c = !(aType instanceof intType) ? b : a;
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, c.getType().getName(), m_OpName, "int"));
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
