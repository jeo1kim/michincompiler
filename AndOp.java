/**
 * Created by jinyongsuk on 10/8/15.
 */
public class AndOp extends BooleanOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public AndOp(String strName )
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

    //T_AND, T_OR, and T_NOT must be both bool type
    //and returning bool type
    STO checkOperands(STO a, STO b) {
        return checkOperands(a, b, getName());
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    private void setName(String str)
    {
        m_OpName = str;
    }
}
