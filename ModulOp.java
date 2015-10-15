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
        setName(strName);
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

        System.out.println("modulo"+a+a.getType().getName());
        if(!(aType.isInt()))
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, aType.getName(), m_OpName , "int"));
        }
        else if(!(bType.isInt()))
        {
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, bType.getName(), m_OpName, "int"));
        }

        else if (aType instanceof intType && bType instanceof intType) {

            if (a.isConst() && b.isConst())
            {

                return new ConstSTO( a.getName() + b.getName() , b.getType());
            }

            return new ExprSTO(a.getName()+ b.getName(), a.getType());
        }
        else{
            STO c = !(aType instanceof intType) ? b : a;
            return new ErrorSTO(Formatter.toString(ErrorMsg.error1w_Expr, c.getType().getName(), m_OpName, "int"));
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
