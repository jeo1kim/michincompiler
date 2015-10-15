/**
 * Created by jinyongsuk on 10/8/15.
 */
public class NotOp extends UnaryOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public NotOp(String strName )
    {
        super(strName);
        setName(strName);
        //setSize(size);
    }

    STO checkOperands(STO a)
    {
        //System.out.println(a.getType().isNumeric());
        Type aType = a.getType();

        if( !aType.isBool() && !a.isError() )
        {
            // "Incompatible type %T to operator %O, equivalent to int, float, or pointer expected.";
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1u_Expr, aType.getName(),getName(), "bool"));
        }
        else
        {
            return a; // if its a bool type just return
//            STO result = new ConstSTO( a.getName() , a.getType()); // do i make a new STO or return the old one.
//            if (a.isConst()){
//                result.markRVal();
//                return result;
//            }
//            else{
//                return result;
//            }
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
