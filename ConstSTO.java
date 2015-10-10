//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------


import java.math.BigDecimal;

class ConstSTO extends STO
{
    //----------------------------------------------------------------
    //	Constants have a value, so you should store them here.
    //	Note: We suggest using Java's BigDecimal class, which can hold
    //	floats and ints. You can then do .floatValue() or 
    //	.intValue() to get the corresponding value based on the
    //	type. Booleans/Ptrs can easily be handled by ints.
    //	Feel free to change this if you don't like it!
    //----------------------------------------------------------------
    private BigDecimal		m_value;

	private Type m_returnType;
	private String m_strName;
	private Type m_type;
	private boolean m_isAddressable;
	private boolean m_isModifiable;

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public ConstSTO(String strName)
	{
		super(strName);
		m_value = null; // fix this
		m_strName = strName;
		m_isAddressable = true;				// double value are constands non mod. L value
		m_isModifiable = false;
		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}

	public ConstSTO(String strName, Type typ)
	{
		super(strName, typ);
		m_strName = strName;
		m_type = typ;
		m_value = null; // fix this
		m_isAddressable = true;				// double value are constands non mod. L value
		m_isModifiable = false;
		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}

	public ConstSTO(String strName, Type typ, int val)
	{
		super(strName, typ);
		m_strName = strName;
		m_type = typ;
		m_value = new BigDecimal(val);
		m_isAddressable = true;				// double value are constands non mod. L value
		m_isModifiable = false;
		// You may want to change the isModifiable and isAddressable
		// fields as necessary
	}

	public ConstSTO(String strName, Type typ, double val)
	{
		super(strName, typ);
		m_strName = strName;
		m_type = typ;
		m_value = new BigDecimal(val);
		m_isAddressable = true;				// double value are constands non mod. L value
		m_isModifiable = false;
		// You may want to change the isModifiable and isAddressable
		// fields as necessary
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

	public boolean isVar() { return false; }
	public boolean isExpr() { return false; }
	public boolean isFunc() { return false; }
	public boolean isStructdef() { return false; }
	public boolean isError() { return false; }
}
