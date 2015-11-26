//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------


import java.math.BigDecimal;
import java.util.Vector;

abstract class STO
{
	public int ifcounter;
	public int loopcounter;
	private String m_strName;
	private Type m_type;
	private boolean m_isAddressable;
	private boolean m_isModifiable;
	private boolean m_ref  = false;
	private BigDecimal m_value;
	private int m_paramCount;

	private Vector<STO> paramSTO;
	private boolean m_return_top_level = false;
	private boolean m_isGlobal = false;
	private boolean m_isLocal = false;

	private boolean m_static;
	private boolean m_auto = false;
	private String m_prepost = "";

	private int sparcOffset=0;
	private String sparcBase= "";

	private boolean m_isArray = false;
	private boolean m_paramCalled = false;

	private STO m_array;
	private boolean inStruct = false;

	private boolean m_pointer = false;
	private boolean m_isparam = false;

	public boolean isStructVar(){
		return inStruct;
	}
	public void setStructVar(){
		inStruct = true;
	}

	public void setSparcBase(String base){
		sparcBase = base;
	}
	public String getSparcBase(){
		return sparcBase;
	}

	public void setAuto(){
		m_auto = true;
	}
	public boolean getAuto(){
		return m_auto;
	}

	public void setPrePost(String prepost){
		m_prepost = prepost;
	}
	public String getPrePost(){
		return m_prepost;
	}

	public void setSparcOffset(int offset){
		sparcOffset = offset;
	}
	public int getSparcOffset(){
		return sparcOffset;
	}

	public void setStatic(boolean stat){ m_static = stat;}
	public boolean isStatic(){ return m_static; }

	private boolean structFuck = false;

	public boolean isLocal(){
		return m_isGlobal;
	}
	public void setLocal(){
		m_isGlobal = true;
	}	public boolean isGlobal(){
	return m_isGlobal;
}
	public void setGlobal(){
		m_isGlobal = true;
	}

	public void setStructFunc(boolean func){
		structFuck = func;
	}
	public boolean isitStructFuck(){
		return structFuck;
	}

	public void setisArray(){
		m_isArray = true;
	}
	public boolean getisArray(){ return m_isArray; }

	public void setParamCalled(){
		m_paramCalled = true;
	}
	public boolean getParamCalled(){ return m_paramCalled; }

	public void setisPointer(){
		m_pointer = true;
	}

	public boolean getisPointer(){ return m_pointer; }
	
	public void setisParam(){
		m_isparam = true;
	}
	public boolean getisParam(){ return m_isparam; }

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public STO(String strName)
	{
		this(strName, null);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public STO(String strName, Type typ)
	{
		setName(strName);
		setType(typ);
		setIsAddressable(false);
		setIsModifiable(false);
	}

	private Scope s_scope;

	public void setMyStruct(Scope stru){ s_scope = stru;}
	public Scope getMyStruct(){
		return s_scope;
	}




	public void setParamVec(Vector<STO> paraList){
		paramSTO = paraList;
	}
	public Vector<STO> getParamVec(){
		return paramSTO;
	}
	public int getParamCount(){ return m_paramCount; }

	public void setParamCount(int count){
		m_paramCount = count;
	}

	public void setRef(boolean ref){ m_ref = ref; }
	public boolean isRef(){ return m_ref;}
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public String getName()
	{
		return m_strName;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void setName(String str)
	{
		m_strName = str;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public Type getType()
	{
		return m_type;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void setType(Type type)
	{
		m_type = type;
	}

	//----------------------------------------------------------------
	// Addressable refers to if the object has an address. Variables
	// and declared constants have an address, whereas results from 
	// expression like (x + y) and literal constants like 77 do not 
	// have an address.
	//----------------------------------------------------------------
	public boolean getIsAddressable()
	{
		return m_isAddressable;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void setIsAddressable(boolean addressable)
	{
		m_isAddressable = addressable;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean getIsModifiable()
	{
		return m_isModifiable;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void setIsModifiable(boolean modifiable)
	{
		m_isModifiable = modifiable;
	}


	//----------------------------------------------------------------
	// A modifiable L-value is an object that is both addressable and
	// modifiable. Objects like constants are not modifiable, so they 
	// are not modifiable L-values.
	//----------------------------------------------------------------
	public boolean isModLValue()
	{
		return getIsModifiable() && getIsAddressable();
	}

	// return true if both are false.
	public boolean isRValue() { return !(getIsAddressable() || getIsModifiable()); }

	public void markRVal(){
		setIsModifiable(false); setIsAddressable(false);
	}
	public void markModVal(){
		setIsModifiable(true); setIsAddressable(true);
	}
	public void markModLVal(){
		setIsModifiable(false); setIsAddressable(true);
	}


	public boolean isAssignableTo(Type t) { return false; }
	public boolean isEquivalentTo(Type t) { return false; }
	//----------------------------------------------------------------
	//	It will be helpful to ask a STO what specific STO it is.
	//	The Java operator instanceof will do this, but these methods 
	//	will allow more flexibility (ErrorSTO is an example of the
	//	flexibility needed).
	//----------------------------------------------------------------
	public boolean isVar() { return false; }
	public boolean isConst() { return false; }
	public boolean isExpr() { return false; }
	public boolean isFunc() { return false; }
	public boolean isStructdef() { return false; }
	public boolean isError() { return false; }

	String getName(Type typ){
		return typ.getName();
	}
	String getName(STO sto){
		return sto.getType().getName();
	}


	public void setValue(BigDecimal value){
		m_value = value;
	}

	public BigDecimal getValue()
	{
		return m_value;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public int getIntValue()
	{
		return m_value.intValue();
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public float getFloatValue()
	{
		return m_value.floatValue();
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean getBoolValue()
	{
		return !BigDecimal.ZERO.equals(m_value);
	}
}
