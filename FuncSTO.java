//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------
import java.beans.VetoableChangeListener;
import java.util.HashMap;
import java.util.Vector;

import java.util.Vector;

class FuncSTO extends STO
{
	private String m_strName;
	private Type m_type;
	private boolean m_isAddressable;
	private boolean m_isModifiable;
	private boolean m_isRetByRef;
	private boolean m_ref;
	private int m_paramCount;
	private Vector<STO> paramSTO = new Vector<>();
	private int m_level;
	private boolean m_return_top_level = false;
	private boolean m_isOverloaded = false;

	public HashMap<String, FuncSTO> m_overLoadFuncName = new HashMap<>();


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------

	public FuncSTO(String strName)
	{
		super (strName);
		setType(new VoidType("void", 1));
		setName(strName);
		m_return_top_level = false;

		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}
	public FuncSTO(String strName, Type retType)
	{
		super (strName);
		setType(retType);
		setReturnType(retType);
		setName(strName);
		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}
	public FuncSTO(String strName, Type retType, Vector<STO> params)
	{
		super (strName);
		setName(strName);
		setType(retType);
		setParamVec(params);
		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}


	public FuncSTO(String strName, Type retType, Vector<STO> params, boolean ref)
	{
		super (strName);
		initSTO(strName,retType, params, ref);
		setParamVec(params);
		m_return_top_level = false;
		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}


	public void initSTO(String strName, Type retType, Vector<STO> params, boolean ref){
		setReturnType(retType);
		setType(retType);
		setName(strName);
		setRef(ref);
		setParamCount(params.size());
	}


	public void addOverload(String overloadName, FuncSTO func){
			m_overLoadFuncName.put(overloadName, func );
	}

	public FuncSTO getOverloaded(String key){
		return m_overLoadFuncName.get(key);
	}

	public void setParamVec(Vector<STO> paraList){
		paramSTO = paraList;
	}
	public Vector<STO> getParamVec(){
		return paramSTO;
	}
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
	//
	//----------------------------------------------------------------
	public int getLevel()
	{
		return m_level;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void setLevel(int level)
	{
		m_level = level;
	}

	public void setOverloaded(boolean ovr)
	{
		m_isOverloaded = ovr;
	}
	public boolean isOverloaded()
	{
		return m_isOverloaded;
	}

	public int getParamCount(){ return m_paramCount; }

	public void setParamCount(int count){
		m_paramCount = count;
	}

	public void setReturn_top_level(boolean top_level)
	{
		m_return_top_level = top_level;
	}

	public boolean getReturn_top_level()
	{
		return m_return_top_level;
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

	// return true only if both are false
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
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean isFunc()
	{
		return true;
		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}

	public void setRef(boolean ref){ m_ref = ref; }
	public boolean isRef(){ return m_ref;}

	//----------------------------------------------------------------
	// This is the return type of the function. This is different from 
	// the function's type (for function pointers - which we are not 
	// testing in this project).
	//----------------------------------------------------------------
	public void setReturnType(Type typ)
	{
		m_type = typ;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public Type getReturnType ()
	{
		return m_type;
	}
}
