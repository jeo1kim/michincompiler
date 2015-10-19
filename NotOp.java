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
    public NotOp(String strName) {
        super(strName);
        setName(strName);
        //setSize(size);
    }

    STO checkOperands(STO a) {
        if (a.isError()) {
            return a;
        }
        //System.out.println(a.getType().isNumeric());
        Type aType = a.getType();

        if (!aType.isBool()) {
            // "Incompatible type %T to operator %O, equivalent to int, float, or pointer expected.";
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1u_Expr, aType.getName(), getName(), "bool"));
        }
        else {
//            return a; // if its a bool type just return
            if (a.getBoolValue() == true) {
                return new ConstSTO(a.getName(), a.getType(), 0);
            } else if (a.getBoolValue() == false) {
                return new ConstSTO(a.getName(), a.getType(), 1);

            }

        }
        System.out.println("NotOp should this print?");
        return a;
    }

        //----------------------------------------------------------------
        //
        //----------------------------------------------------------------

    public String getName() {
        return m_OpName;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    private void setName(String str) {
        m_OpName = str;
    }
}
