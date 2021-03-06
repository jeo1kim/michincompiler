
/**
 * Created by jinyongsuk on 10/8/15.
 */
public class EqualOp extends ComparisonOp {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public EqualOp(String strName) {
        super(strName);
        setName(strName);
        //setSize(size);
    }

    private boolean comparsion = true;


    public boolean isComparison(){
        return comparsion;
    }

    public void setComparison(boolean comp){
        comparsion = comp;
    }

    STO checkOperands(STO a, STO b) {
        Type aType = a.getType();
        Type bType = b.getType();


        if (aType.isPointer() || bType.isPointer()) {
            if (!aType.isEquivalentTo(bType)) {
                return new ErrorSTO(Formatter.toString(ErrorMsg.error17_Expr, getName(), aType.getName(), bType.getName()));
            }
            else if (!bType.isEquivalentTo(aType)) {
                return new ErrorSTO(Formatter.toString(ErrorMsg.error17_Expr, getName(), aType.getName(), bType.getName()));
            }
            return new ExprSTO(a.getName() + getName() + b.getName(), new BoolType());
        }
        else if (aType.isNullPointer() || bType.isNullPointer()) {
            if (aType.isNullPointer() && bType.isNullPointer()){
                return new ExprSTO(a.getName() + getName() + b.getName(), new BoolType());
            }
            else if (!aType.isEquivalentTo(bType)) {
                return new ErrorSTO(Formatter.toString(ErrorMsg.error17_Expr, getName(), aType.getName(), bType.getName()));
            }
            else if (!bType.isEquivalentTo(aType)) {
                return new ErrorSTO(Formatter.toString(ErrorMsg.error17_Expr, getName(), aType.getName(), bType.getName()));
            }
            return new ExprSTO(a.getName() + getName() + b.getName(), new BoolType());
        }
        else if (((aType.isNumeric()) && (bType.isNumeric())) || (aType.isBool() && bType.isBool())) {
            if (a.isConst() && b.isConst()) {
                int val = a.getValue().compareTo(b.getValue());
                if(val == 0){
                    val = 1;
                }else{
                    val = 0;
                }
                return new ConstSTO(a.getName() + getName() + b.getName(), new BoolType(), val);
            }
            return new ExprSTO(a.getName() + getName() + b.getName(), new BoolType());
        } else {
            //if it's not both integer then return error STO
            STO err = (!(aType.isBool()) && !(aType.isNumeric())) ? b : a;
            // should increment m_nNumErrors++; in MyParser
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1b_Expr, err.getType().getName(), getName(), bType.getName()));
        }
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
