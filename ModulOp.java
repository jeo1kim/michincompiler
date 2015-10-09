/**
 * Created by jinyongsuk on 10/8/15.
 */
public class ModulOp extends ArithmeticOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public ModulOp(String strName )
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

    //both operands must be integer otherwise return error
    STO checkOperands(STO a, STO b) {
        Type aType = a.getType();
        Type bType = b.getType();

        if (!(aType.isNumeric()) || !(bType.isNumeric())) {
            STO err = (!(aType.isNumeric())) ? b : a;
            // should increment m_nNumErrors++; in MyParser
            return new ErrorSTO(err.getName());
        } else if (aType.isInt() && bType.isInt()) {
            System.out.println(a.getName() + "" + b.getName() + " has typ: " + a.getType().toString());
            return new ExprSTO(a.getName() + "" + b.getName(), a.getType());
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
