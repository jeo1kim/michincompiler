/**
 * Created by jinyongsuk on 10/8/15.
 */
public class DecOp extends UnaryOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public DecOp(String strName )
    {
        super(strName);
        setName(strName);
        //setSize(size);
    }
    STO checkOperands(STO a){
        return checkOperands(a, getName());
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
