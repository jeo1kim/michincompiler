//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------


import java.math.BigDecimal;

class ExprSTO extends STO
{
	private BigDecimal		m_value;
	private String m_strName;
	private Type m_type;
	private boolean m_isAddressable;
	private boolean m_isModifiable;
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public ExprSTO(String strName)
	{
		super(strName);
        // You may want to change the isModifiable and isAddressable
        // fields as necessary
		m_strName = strName;
		init();
	}

	public ExprSTO(String strName, Type typ)
	{
		super(strName, typ);
		m_strName = strName;
		m_type = typ;
		init();
        // You may want to change the isModifiable and isAddressable
        // fields as necessary
	}
	public void init(){
		setIsAddressable(true);
		setIsModifiable(true);
	}

	private int sparcOffset=0;

	private String sparcBase= "";

	public void setSparcBase(String base){
		sparcBase = base;
	}
	public String getSparcBase(){
		return sparcBase;
	}

	public void setSparcOffset(int offset){
		sparcOffset = offset;
	}
	public int getSparcOffset(){
		return sparcOffset;
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
	public void setValue(BigDecimal value){
		m_value = value;
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
	public boolean isExpr()
	{
		return true;
	}
}
