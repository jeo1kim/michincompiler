/**
 * Created by jinyongsuk on 10/8/15.
 */
public class LTEOp extends ComparisonOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public LTEOp(String strName )
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
        return super.checkOperands(a,b);
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    private void setName(String str)
    {
        m_OpName = str;
    }
}
