/**
 * Created by jinyongsuk on 10/8/15.
 */
abstract class UnaryOp extends Operator {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public UnaryOp(String strName )
    {
        super(strName);
        //setSize(size);
    }

    STO checkOperands(STO a){
        return a;
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
