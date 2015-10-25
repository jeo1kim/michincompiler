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
        if (a.isError()){
            return a;
        }
        //System.out.println(a.getType().isNumeric());
        Type aType = a.getType();
        //System.out.println(a);System.out.println(aType);
        if(!a.isModLValue()){
            //      "Operand to %O is not a modifiable L-value.";
            return new ErrorSTO(Formatter.toString(ErrorMsg.error2_Lval, opName));
        }
        else if( !(aType.isNumeric() || aType.isPointer()))
        {
            // "Incompatible type %T to operator %O, equivalent to int, float, or pointer expected.";
            return new ErrorSTO(Formatter.toString(ErrorMsg.error2_Type, aType.getName(),opName));
        }
        else
        {
            if (a.isConst()){
                int val = calculate(a.getIntValue(), opName);
                ConstSTO result = new ConstSTO( a.getName() , a.getType(), val); // do i make a new STO or return the old one.
                result.markRVal();
                return result;
            }
            else{
                ExprSTO result = new ExprSTO( a.getName() , a.getType()); // do i make a new STO or return the old one.
                result.markRVal();
                return result;
            }
        }
    }
    public int calculate (int a, String opname){

        int result= a;

        switch (opname) {
            case "++": result = a++;
                break;
            case "--": result = a--;
                break;

        }

        return result;
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
