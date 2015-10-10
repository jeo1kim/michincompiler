/**
 * Created by jinyongsuk on 10/8/15.
 */
public class IncOp extends UnaryOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public IncOp(String strName )
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

    STO checkOperands(STO a) {
        Type aType = a.getType();


        if (aType.isInt() || aType.isFloat()) {

            return new ExprSTO(a.getName(), aType);

        } else {
            //if it's not both integer then return error STO

            // should increment m_nNumErrors++; in MyParser
            return new ErrorSTO(a.getName());
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
