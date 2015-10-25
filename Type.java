// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------

//---------------------------------------------------------------------
// This is the top of the Type hierarchy. You most likely will need to
// create sub-classes (since this one is abstract) that handle specific
// types, such as IntType, FloatType, ArrayType, etc.
//---------------------------------------------------------------------

abstract class Type
{
	// Name of the Type (e.g., int, bool, some structdef, etc.)
	private String m_typeName;
	private int m_size;

	private Type nextType;
	private Scope m_structScope;

	public Type(){}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public Type(String strName, int size)
	{
		setName(strName);
		setSize(size);
	}

	private boolean m_ptr = false;

	public void setPtr(boolean ptr){
		m_ptr= true;
	}
	public boolean isPtr(){
		return m_ptr;
	}

	public void setStructSize(){
		return;
	}
	public void setScope(Scope scope){
		m_structScope = scope;
	}
	public Scope getScope(){
		return m_structScope;
	}


	public String getbaseName(){
		return m_basetype;
	}
	private String m_basetype;
	public void setBaseName(String base){
		m_basetype = base;
	}
	public void insert(STO var){
		m_structScope.InsertLocal(var);
	}

	public Type getNextType(){ return nextType; }
	public void setNextType(Type type){ nextType = type; }

	public boolean isAssignableTo(Type t) { return false; }
	public boolean isEquivalentTo(Type t) { return false; }

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
	public boolean	isPointer()	{ return false; }

	public boolean isVoid() 	{ return false; } // where is this used?
	public boolean isBasic()	{ return false; }
	public boolean isComposite(){ return false; }
	public boolean isNumeric()	{ return false; }
}