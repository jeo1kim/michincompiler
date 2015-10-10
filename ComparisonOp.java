/**
 * Created by jinyongsuk on 10/8/15.
 */
abstract class ComparisonOp extends BinaryOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public ComparisonOp(String strName )
    {
        super(strName);
        //setSize(size);
    }

    STO checkOperands(STO a, STO b)
    {
        Type aType = a.getType();
        Type bType = b.getType();

        // operands must be numeric and resulting type must be bool
        if( (aType.isNumeric()) && (bType.isNumeric()))
        {
            System.out.println(a.getName()+b.getName() +" has typ: "+ a.getType().toString());
            return new ExprSTO(a.getName()+b.getName(), new BoolType());

        }
        else
        {
            STO err = (!(aType.isNumeric())) ? a : b;
            // should increment m_nNumErrors++; in MyParser
            return new ErrorSTO(err.getName());
        }

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
