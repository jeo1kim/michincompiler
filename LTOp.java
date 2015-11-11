/**
 * Created by jinyongsuk on 10/8/15.
 */
public class LTOp extends ComparisonOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public LTOp(String strName )
    {
        super(strName);
        setName(strName);
        //setSize(size);
    }

    private boolean comparsion = true;


    public boolean isComparison(){
        return comparsion;
    }

    public void setComparison(boolean comp){
        comparsion = comp;
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
    STO checkOperands(STO a, STO b)
    {
        return checkOperands(a,b, getName() );
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    private void setName(String str)
    {
        m_OpName = str;
    }
}
