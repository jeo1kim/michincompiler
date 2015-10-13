//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------


import java.math.BigDecimal;


// VarSTO ARE Modifiable L-value!!!!
class VarSTO extends STO
{
	private String m_strName;
	private Type m_type;
	private boolean m_isAddressable;
	private boolean m_isModifiable;
	private boolean m_ref  = false;
	private BigDecimal m_value;

	private STO m_array;

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public VarSTO(String strName)
	{
		super(strName);
		setName(strName);
		setIsAddressable(true);
		setIsModifiable(true);
		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}

	public VarSTO(String strName, Type typ)
	{
		super(strName, typ);
		initSTO(strName, typ);
		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}

	public VarSTO(String strName, Type typ, boolean ref)//, STO array)
	{
		super(strName, typ);
		initSTO(strName, typ);
		setRef(ref);
		//m_array = array;
		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}

	public VarSTO(String strName, Type typ, int val)
	{
		super(strName, typ);
		initSTO(strName, typ);
		m_value = new BigDecimal(val);
		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}

	public VarSTO(String strName, Type typ, double val)
	{
		super(strName, typ);
		initSTO(strName, typ);
		m_value = new BigDecimal(val);

		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}

	public void initSTO(String strName, Type typ){
		setName(strName);
		setType(typ);
		setIsAddressable(true);
		setIsModifiable(true);
	}

	public void setRef(boolean ref){ m_ref = ref; }
	public boolean isRef(){ return m_ref;}

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
	public boolean isRValue() { return !(getIsAddressable() || getIsModifiable()); } // double check this,

	public String getName()
	{
		return m_strName;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	private void setName(String str)
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
	private void setType(Type type)
	{
		m_type = type;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean isConst()
	{
		return true;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
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

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean isVar() 
	{
		return true;
	}
}
