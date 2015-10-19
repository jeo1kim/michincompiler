import java.math.BigDecimal;

/**
 * Created by jinyongsuk on 10/8/15.
 */
public class DivideOp extends ArithmeticOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public DivideOp(String strName )
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

    STO checkOperands(STO a, STO b)
    {
        STO result = checkOperands(a, b, getName());
        ConstSTO stoB = (ConstSTO) b;
        Type btype = stoB.getType();

        //System.out.println(stoB+stoB.getName()+ stoB.getFloatValue() + btype);
        if(b.isConst()){
            if((btype.isInt() && stoB.getIntValue() == 0) || (btype.isFloat() && stoB.getFloatValue() == 0.0) ){
                return new ErrorSTO(ErrorMsg.error8_Arithmetic);
            }


        }
        result.setValue(a.getValue());
        return result;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    private void setName(String str)
    {
        m_OpName = str;
    }
}
