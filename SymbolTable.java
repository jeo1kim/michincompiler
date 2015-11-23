//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------

import java.util.*;

class SymbolTable {

    private Stack<Scope> m_stkScopes;
    private int m_nLevel;
    private Scope m_scopeGlobal;
    private FuncSTO m_func = null;
    private StructdefSTO m_struct = null;

    private Stack<String> m_stkLooop = new Stack<String>();

    private String m_scopeName = null;



    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public SymbolTable() {
        m_nLevel = 0;
        m_stkScopes = new Stack<Scope>();
        m_scopeGlobal = null;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public void insert(STO sto) {
        if (sto.getType().isStruct()) {
            Scope scope = m_stkScopes.peek();
            if(scope == m_scopeGlobal)
            {
                sto.setGlobal();
                sto.setSparcBase("%g0");

            }
            m_scopeGlobal.InsertLocal(sto);
        }
        else {
            Scope scope = m_stkScopes.peek();
            if(scope == m_scopeGlobal)
            {
                sto.setGlobal();

            }

            scope.InsertLocal(sto);
        }
    }
    public boolean isGlobalScope(){
        if(m_stkScopes.peek() == m_scopeGlobal)
        {
            return true;
        }
        return false;
    }
    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public STO accessGlobal(String strName) {

        //System.out.println("From global scope " + " " + strName);
        return m_scopeGlobal.access(strName);
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public STO accessLocal(String strName) {
        Scope scope = m_stkScopes.peek();
        return scope.accessLocal(strName);
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public STO access(String strName) {
        Stack stk = new Stack();
        Scope scope;
        STO stoReturn = null;

        for (Enumeration<Scope> e = m_stkScopes.elements(); e.hasMoreElements(); ) {

            scope = e.nextElement();
            if ((stoReturn = scope.access(strName)) != null)
                stk.push(stoReturn);  //add all elements to stack.
            //return stoReturn;
        }

        while (!stk.empty()) {  // while stack has elements, pop the top LIFO
            return (STO) stk.pop();
        }
        return null;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public void openScope() {
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
    public void closeScope() {
        m_stkScopes.pop();
        m_nLevel--;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public int getLevel() {
        return m_nLevel;
    }


    //----------------------------------------------------------------
    //	This is the function currently being parsed.
    //----------------------------------------------------------------

    public FuncSTO getFunc() {
        return m_func;
    }

    public void setFunc(FuncSTO sto) {
        m_func = sto;
    }

    public StructdefSTO getStruct() {
        return m_struct;
    }
    public void setStruct(StructdefSTO struct) {
        m_struct = struct;
    }

    public void pushLoop(String name) {
        m_stkLooop.push(name);
    }
    public void popLoop() {
        m_stkLooop.pop();
    }
    public int getLoopSize() {
        return m_stkLooop.size();
    }

    // get the scope for structs
    public Scope getScope() {
        return m_stkScopes.peek();
    }
    public void setScope(String name) {
        m_scopeName = name;
    }
}
