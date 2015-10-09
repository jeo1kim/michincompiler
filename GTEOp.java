import jdk.nashorn.internal.codegen.types.BooleanType;

/**
 * Created by jinyongsuk on 10/8/15.
 */
public class GTEOp extends ComparisonOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public GTEOp(String strName )
    {
        super(strName);
        //setSize(size);
    }
    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public String getName()
    {
        return m_OpName;
    }

    //T_LT, T_LTE, T_GT, T_GTE operands tryp must be numeric
    //with returning Bollean
    STO checkOperands(STO a, STO b) {
        Type aType = a.getType();
        Type bType = b.getType();

        if ((aType.isNumeric()) || (bType.isNumeric())) {

            return new ExprSTO(a.getName() + " >= " + b.getName(), new BoolType("newBool" ,1));

        } else {
            //if it's not both integer then return error STO
            STO err = (!(aType.isNumeric())) ? b : a;
            // should increment m_nNumErrors++; in MyParser
            return new ErrorSTO(err.getName());
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
