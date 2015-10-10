/**
 * Created by jinyongsuk on 10/8/15.
 */
public class MulOp extends ArithmeticOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public MulOp(String strName )
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
        //System.out.println(a);System.out.println(b);System.out.println(getName());
        //System.out.println(checkOperands(a,b, getName()));
        return checkOperands(a,b, getName());
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    private void setName(String str)
    {
        m_OpName = str;
    }
}
