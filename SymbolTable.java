//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------

import java.util.*;

class SymbolTable
{
	private Stack<Scope> m_stkScopes;
	private int m_nLevel;
	private Scope m_scopeGlobal;
	private FuncSTO m_func = null;
	private HashMap<String, FuncSTO> hMap_OverloadedF;

	private String ScopeName;


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public SymbolTable()
	{
		m_nLevel = 0;
		m_stkScopes = new Stack<Scope>();
		m_scopeGlobal = null;
		hMap_OverloadedF = new HashMap<String, FuncSTO>();
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void insert(STO sto)
	{
		Scope scope = m_stkScopes.peek();
		scope.InsertLocal(sto);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public STO accessGlobal(String strName)
	{

		//System.out.println("From global scope " + " " + strName);
		return m_scopeGlobal.access(strName);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public STO accessLocal(String strName)
	{
		Scope scope = m_stkScopes.peek();
		return scope.accessLocal(strName);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public STO access(String strName)
	{
		Stack stk = new Stack();
		Scope scope;
		STO stoReturn = null;

		for (Enumeration<Scope> e = m_stkScopes.elements(); e.hasMoreElements();)
		{

			scope = e.nextElement();
			if ((stoReturn = scope.access(strName)) != null)
				stk.push(stoReturn);  //add all elements to stack.
				//return stoReturn;

		}

		while (!stk.empty())  // while stack has elements, pop the top LIFO
			return (STO) stk.pop();


		return null;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void openScope()
	{
		Scope scope = new Scope();
		// The first scope created will be the global scope.
		if (m_scopeGlobal == null)
			m_scopeGlobal = scope;

		m_stkScopes.push(scope);
		m_nLevel++;
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void closeScope()
	{
		m_stkScopes.pop();
		m_nLevel--;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public int getLevel()
	{
		return m_nLevel;
	}


	//----------------------------------------------------------------
	//	This is the function currently being parsed.
	//----------------------------------------------------------------
	public FuncSTO getFunc() { return m_func; }
	public void setFunc(FuncSTO sto) { m_func = sto; }

	public void insertOverloadedFunc(String key, FuncSTO fn)
	{
		hMap_OverloadedF.put(key, fn);
	}

	public FuncSTO getOverLoadedFuncs(String name_Type)
	{
		return hMap_OverloadedF.get(name_Type);
	}

	public boolean isInHMap (String key)
	{
		return hMap_OverloadedF.containsKey(key);
	}
}
