/**
 * Created by jinyongsuk on 10/2/15.
 */
public class BoolType extends BasicType {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_typeName;
    private int m_size;

    public BoolType(){
        super();
        setName("bool");
        setSize(4);
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public BoolType(String strName, int size)
    {
        super(strName, size);
        setName(strName);
        setSize(size);
    }
    public boolean isAssignableTo(Type t) { return t.isBool(); }
    public boolean isEquivalentTo(Type t) { return t.isBool(); }

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
    private void setName(String str)
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
    private void setSize(int size)
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
    public boolean  isInt()	    { return false; }
    public boolean	isFloat() 	{ return false; }	// added checks for All types
    public boolean	isBool()	{ return true; }
    public boolean 	isArray()	{ return false;	}
    public boolean 	isStruct()	{ return false;	}
    public boolean 	isNullPointer() { return false; }
    public boolean	isPointer()	{ return false; }

    public boolean isVoid() 	{ return false; } // where is this used?
    public boolean isBasic()	{ return true; }
    public boolean isComposite(){ return false; }

}
