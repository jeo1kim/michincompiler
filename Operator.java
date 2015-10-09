/**
 * Created by jinyongsuk on 10/8/15.
 */
abstract class Operator {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_OpName;
    //private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public Operator(String strName )
    {
        setName(strName);
        //setSize(size);
    }

    STO checkOperands(STO a, STO b)
    {
        Type aType = a.getType();
        Type bType = b.getType();

        if( !(aType.isNumeric()) || !(bType.isNumeric()))
        {
            STO err = (!(aType.isNumeric())) ? b : a;
            // should increment m_nNumErrors++; in MyParser
            return new ErrorSTO(err.getName());
        }
        else if ( aType.isInt() && bType.isInt()){

            System.out.println(a.getName()+""+b.getName() +" has typ: "+ a.getType().toString());
            return new ExprSTO(a.getName()+""+b.getName(), a.getType());
        }
        else{
            STO c = !(aType.isInt()) ? b : a;
            System.out.println(a.getName()+""+b.getName());
            return new ExprSTO(a.getName()+""+b.getName(), c.getType());
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

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    //public int getSize()
    //{
    //    return m_size;
    //}

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    //private void setSize(int size)
    //{
    //    m_size = size;
    //}

    //----------------------------------------------------------------
    //	It will be helpful to ask a Type what specific Type it is.
    //	The Java operator instanceof will do this, but you may
    //	also want to implement methods like isNumeric(), isInt(),
    //	etc. Below is an example of isInt(). Feel free to
    //	change this around.
    //----------------------------------------------------------------

    /*
    public boolean  isError()   { return false; }
    public boolean  isInt()	    { return false; }
    public boolean	isFloat() 	{ return false; }	// added checks for All types
    public boolean	isBool()	{ return false; }
    public boolean 	isArray()	{ return false;	}
    public boolean 	isStruct()	{ return false;	}
    public boolean 	isNullPointer() { return false; }
    public boolean	isPointer()	{ return false; }

    public boolean isVoid() 	{ return false; } // where is this used?
    public boolean isBasic()	{ return false; }
    public boolean isComposite(){ return false; }
    public boolean isNumeric()	{ return false; }
    */
}
