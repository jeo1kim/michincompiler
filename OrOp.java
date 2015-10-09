/**
 * Created by jinyongsuk on 10/8/15.
 */
public class OrOp extends BooleanOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public OrOp(String strName )
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

    //T_AND, T_OR, and T_NOT must be both bool type
    //and returning bool type
    STO checkOperands(STO a, STO b) {
        Type aType = a.getType();
        Type bType = b.getType();

        if ((aType.isBool()) && (bType.isBool())) {

            return new ExprSTO(a.getName() + " && " + b.getName(), new BoolType("newBool" ,1));

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
