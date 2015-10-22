//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------

//---------------------------------------------------------------------
// For structdefs
//---------------------------------------------------------------------



class StructdefSTO extends STO
{

	private String m_strName;
	private Type m_type;
	private boolean m_isAddressable;
	private boolean m_isModifiable;
	private boolean m_ref  = false;

	private boolean m_static;
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public StructdefSTO(String strName)
	{
		super(strName);
	}

	public StructdefSTO(String strName, Type typ)
	{
		super(strName, typ);
	}


	public void setStatic(boolean stat){ m_static = stat;}
	public boolean isStatic(){ return m_static; }
	public void setRef(boolean ref){ m_ref = ref; }
	public boolean isRef(){ return m_ref;}

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
	public boolean isStructdef()
	{
		return true;
	}
}
