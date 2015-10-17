/**
 * Created by jinyongsuk on 10/8/15.
 */
public class MinusOp extends ArithmeticOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public MinusOp(String strName )
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
