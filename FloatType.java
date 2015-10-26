/**
 * Created by jinyongsuk on 10/2/15.
 */
public class FloatType extends NumericType {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_typeName;
    private int m_size;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------

    public FloatType(){
        super();
        setName("float");
        setSize(4);
    }
    public FloatType(String strName, int size)
    {
        super(strName, size);
        setName(strName);
        setSize(4);
    }

    public boolean isAssignableTo(Type t) { return t.isFloat(); }
    public boolean isEquivalentTo(Type t) { return t.isFloat(); }
    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public String getName()
    {
        return m_typeName;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public void setName(String str)
    {
        m_typeName = str;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public int getSize()
    {
        return m_size;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public void setSize(int size)
    {
        m_size = size;
    }

    //----------------------------------------------------------------
    //	It will be helpful to ask a Type what specific Type it is.
    //	The Java operator instanceof will do this, but you may
    //	also want to implement methods like isNumeric(), isInt(),
    //	etc. Below is an example of isInt(). Feel free to
    //	change this around.
    //----------------------------------------------------------------
    public boolean  isError()   { return false; }
    public boolean  isInt()	    { return false; }// added checks for All types
    public boolean	isBool()	{ return false; }
    public boolean 	isArray()	{ return false;	}
    public boolean 	isStruct()	{ return false;	}
    public boolean 	isNullPointer() { return false; }
    public boolean	isPointer()	{ return false; }
    public boolean isVoid() 	{ return false; } // where is this used?
    public boolean isComposite(){ return false; }

    public boolean isNumeric()  { return true; }
    public boolean	isFloat() 	{ return true; }
    public boolean isBasic()	{ return true; }

}
