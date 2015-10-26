/**
 * Created by jinyongsuk on 10/8/15.
 */
public class PointerType extends CompositeType {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_typeName;
    private int m_size = 4;

    private Type nextType;

    public Type getNextType(){ return nextType; }
    public void setNextType(Type type){ nextType = type; }

    public Type getBaseType(){
        if(!this.getNextType().isPointer()){
            return this.getNextType();
        }
        return this;
    }



    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public PointerType(String strName, int size)
    {
        super(strName, 4);
        setName(strName);
        setSize(size);
    }
    public boolean isEquivalentTo(Type t) {
        if(t.isNullPointer()){
            return true;
        }
        else if (t.isPointer()){
            return this.getNextType().isEquivalentTo(t.getNextType());
        }
        return false;
    }


    public boolean isAssignableTo(Type t) {
        if(t.isPointer()){
            return this.isEquivalentTo(t);
        }
        else{
            return false;
        }
    }
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
    public boolean  isInt()	    { return false; }
    public boolean	isFloat() 	{ return false; }	// added checks for All types
    public boolean	isBool()	{ return false; }
    public boolean 	isArray()	{ return false;	}
    public boolean 	isStruct()	{ return false;	}
    public boolean 	isNullPointer() { return false; }
    public boolean	isPointer()	{ return true; }

    public boolean isVoid() 	{ return false; } // where is this used?
    public boolean isBasic()	{ return false; }
    public boolean isComposite(){ return true; }
    public boolean isNumeric()	{ return false; }
}
