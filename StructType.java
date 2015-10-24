/**
 * Created by jinyongsuk on 10/8/15.
 */
public class StructType extends CompositeType {
    // Name of the Type (e.g., int, bool, some structdef, etc.)
    private String m_typeName;
    private int m_size = 0;

    private Scope m_structScope;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public StructType(String strName, int size)
    {
        super(strName, size);
        setName(strName);
        setSize(size);
    }

    public void insert(STO var){
        m_structScope.InsertLocal(var);
    }

    public void setStructSize(){
        for(STO var : m_structScope.getScopelist()){
            if(var.getType().isBasic()){
                m_size += 4;
            }
            else if(var.getType().isComposite()){
                m_size += var.getType().getSize();
            }
        }
    }


    public boolean isAssignableTo(Type t) { return this.getName().replace("~","").equals(t.getName().replace("~", "")); }
    public boolean isEquivalentTo(Type t) { return this.getName().replace("~","").equals(t.getName().replace("~","")); }


    public void setScope(Scope scope){
        m_structScope = scope;
    }
    public Scope getScope(){
        return m_structScope;
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
    public boolean 	isStruct()	{ return true;	}
    public boolean 	isNullPointer() { return false; }
    public boolean	isPointer()	{ return false; }

    public boolean isVoid() 	{ return false; } // where is this used?
    public boolean isBasic()	{ return false; }
    public boolean isComposite(){ return true; }
    public boolean isNumeric()	{ return false; }
}
