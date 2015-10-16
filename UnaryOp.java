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
        setName(strName);
        //setSize(size);
    }

    STO checkOperands(STO a,  String opName)
    {
        //System.out.println(a.getType().isNumeric());
        Type aType = a.getType();

        if( !(aType instanceof NumericType) && !a.isError())
        {
            // "Incompatible type %T to operator %O, equivalent to int, float, or pointer expected.";
            return new ErrorSTO(Formatter.toString(ErrorMsg.error2_Type, aType.getName(),opName));
        }
        if(!a.isModLValue() && !a.isError()){
            //      "Operand to %O is not a modifiable L-value.";
            return new ErrorSTO(Formatter.toString(ErrorMsg.error2_Lval, opName));
        }
        else
        {
            STO result = new ConstSTO( a.getName() , a.getType()); // do i make a new STO or return the old one.
            if (a.isConst()){
                result.markRVal();
                return result;
            }
            else{
                return result;
            }
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
