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
