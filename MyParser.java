//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------


import java_cup.runtime.*;

import javax.swing.text.Style;
import java.util.Iterator;
import java.util.Map;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import java.lang.*;


class MyParser extends parser {
    private Lexer m_lexer;
    private ErrorPrinter m_errors;
    private boolean m_debugMode;
    private int m_nNumErrors;
    private String m_strLastLexeme;
    private boolean m_bSyntaxError = true;
    private int m_nSavedLineNum;

    private SymbolTable m_symtab;

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public MyParser(Lexer lexer, ErrorPrinter errors, boolean debugMode) {
        m_lexer = lexer;
        m_symtab = new SymbolTable();
        m_errors = errors;
        m_debugMode = debugMode;
        m_nNumErrors = 0;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public boolean Ok() {
        return m_nNumErrors == 0;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public Symbol scan() {
        Token t = m_lexer.GetToken();

        //	We'll save the last token read for error messages.
        //	Sometimes, the token is lost reading for the next
        //	token which can be null.
        m_strLastLexeme = t.GetLexeme();

        switch (t.GetCode()) {
            case sym.T_ID:
            case sym.T_ID_U:
            case sym.T_STR_LITERAL:
            case sym.T_FLOAT_LITERAL:
            case sym.T_INT_LITERAL:
                return new Symbol(t.GetCode(), t.GetLexeme());
            default:
                return new Symbol(t.GetCode());
        }
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public void syntax_error(Symbol s) {
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public void report_fatal_error(Symbol s) {
        m_nNumErrors++;
        if (m_bSyntaxError) {
            m_nNumErrors++;

            //	It is possible that the error was detected
            //	at the end of a line - in which case, s will
            //	be null.  Instead, we saved the last token
            //	read in to give a more meaningful error
            //	message.
            m_errors.print(Formatter.toString(ErrorMsg.syntax_error, m_strLastLexeme));
        }
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public void unrecovered_syntax_error(Symbol s) {
        report_fatal_error(s);
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public void DisableSyntaxError() {
        m_bSyntaxError = false;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public void EnableSyntaxError() {
        m_bSyntaxError = true;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public String GetFile() {
        return m_lexer.getEPFilename();
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public int GetLineNum() {
        return m_lexer.getLineNumber();
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public void SaveLineNum() {
        m_nSavedLineNum = m_lexer.getLineNumber();
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    public int GetSavedLineNum() {
        return m_nSavedLineNum;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoProgramStart() {
        // Opens the global scope.

        m_symtab.openScope();
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoProgramEnd() {
        m_symtab.closeScope();
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoVarDecl(String id) {
        if (m_symtab.accessLocal(id) != null)
            if (m_symtab.accessLocal(id) != null) {
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
            }

        VarSTO sto = new VarSTO(id);
        m_symtab.insert(sto);
    }

    void DoVarDeclwType(String id, Type typ, boolean stat, Vector<STO> array, STO init) {
        if (init != null && init.isError()) {
            m_nNumErrors++;
            return;    // might wanan change with !init.isError()
        }
        if (m_symtab.accessLocal(id) != null) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
            return;
        }


        VarSTO sto = new VarSTO(id, typ);
        if (stat) {
            sto.setStatic(stat); // set Variable static
        }
        if (array.size() == 0 && (init != null)) { // indicates that this var is not an array and init exp exist
            // do the type check with init if it exist
            if (!init.getType().isAssignableTo(sto.getType())) {
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.error8_Assign, getTypeName(init), getTypeName(typ)));
                return;
            } else { // exp is assignable to this varSto type. so
                sto.setValue(init.getValue()); // set the value
                m_symtab.insert(sto);
                return;
            }
        }
        //case where var is an array
//		else if(array.size()>0){
//			System.out.print(array.get(0));
//			sto.setType(new ArrayType("array",array.size())); // double check array size
//			m_symtab.insert(sto);
//			return;
//		}
        else {
            m_symtab.insert(sto);
            return;
        }
    }

    void DoVarDeclwAuto(String id, STO expr, boolean stat) {
        if (expr.isError()) {
            return;
        }
        if (m_symtab.accessLocal(id) != null) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
        }

        if (!expr.isConst()) {
            VarSTO sto = new VarSTO(id, expr.getType());
            if (stat) {
                sto.setStatic(stat); // set variable static
            }
            m_symtab.insert(sto);

        } else {
            ConstSTO sto = new ConstSTO(id, expr.getType());
            if (stat) {
                sto.setStatic(stat); // set variable static
            }
            m_symtab.insert(sto);
        }

        //m_symtab.insert(sto);
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoExternDecl(String id) {
        if (m_symtab.accessLocal(id) != null) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
        }

        VarSTO sto = new VarSTO(id);
        m_symtab.insert(sto);
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoConstDecl(String id, Type typ, STO exp, boolean stat) {

        if (exp != null && exp.isError()) {
            m_nNumErrors++;
            return;    // might wanan change with !init.isError()
        }
        if (m_symtab.accessLocal(id) != null) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
        }

        ConstSTO sto = new ConstSTO(id, typ);   // fix me
        if( !exp.isConst()){
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error8_CompileTime, id));
            return;
        }
        if (stat) {
            sto.setStatic(stat); // set Variable static
        }
        if (!exp.getType().isAssignableTo(typ)) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error8_Assign, getTypeName(exp), getTypeName(typ)));
            return;
        } else { // exp is assignable to this varSto type. so
            if (exp.getValue() == null) {
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.error8_CompileTime, id));
                return;
            }
            sto.setValue(exp.getValue());
            sto.markModLVal();
            m_symtab.insert(sto);
            return;
        }


    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoConstDeclwAuto(String id, STO exp, boolean stat) {
        if (exp != null && exp.isError()) {
            m_nNumErrors++;
            return;    // might wanan change with !init.isError()
        }
        if (m_symtab.accessLocal(id) != null) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
        }

        ConstSTO sto = new ConstSTO(id, exp.getType());   // fix me
        if (stat) {
            sto.setStatic(stat); // set Variable static
        }
        if (exp.getValue() == null) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error8_CompileTime, id));
            return;
        }
        sto.setValue(exp.getValue());
        sto.markModLVal();
        m_symtab.insert(sto);
        return;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoStructdefDecl(String id) {
        if (m_symtab.accessLocal(id) != null) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
        }

        StructdefSTO sto = new StructdefSTO(id);
        m_symtab.insert(sto);
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoFuncDecl_1(String id) {
        boolean isThereOverloadedFunction = false;
        STO a = m_symtab.accessLocal(id);

        if (a != null && !(a.isFunc())) //if found STO is not function
        {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
            return;
            //error9_Decl
            // "Duplicate declaration of overloaded function %S.";
        }

        FuncSTO sto = new FuncSTO(id);


        m_symtab.insert(sto);
        //m_symtab.insertOverloadedFunc(id, sto); //all funcSTO goes into HashMap
        m_symtab.openScope();
        m_symtab.setFunc(sto);
        //sto.setLevel(m_symtab.getLevel());
    }

    //func decl
    void DoFuncDecl_1_param(String id, Type ret, Vector<STO> params)
    {
        boolean isThereOverloadedFunction = false;
        STO a = m_symtab.accessLocal(id);
        if (a != null && !(a.isFunc())) //if found STO is not function
        {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
        }
        else if (a != null && a.isFunc()) {
            if (DoOverloadedFuncCheck(id, params) == null) {
                isThereOverloadedFunction = true;
            }
        }


        FuncSTO sto = new FuncSTO(id, ret);


        m_symtab.insert(sto);
        //m_symtab.insertOverloadedFunc(hKey, sto); //all funcSTO goes into HashMap
        m_symtab.openScope();
        m_symtab.setFunc(sto);

    }

    // funcdef
    void DoFuncDecl_1_param(String id, Type ret, boolean ref, Vector<STO> params) {
        boolean isThereOverloadedFunction = false;

        STO a = m_symtab.accessLocal(id);

        if (a != null && !(a.isFunc())) //if found STO is not function
        {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
            return;
        }


//        else if (a != null && a.isFunc()) {
//            if (DoOverloadedFuncCheck(id, params) == null) {
//                isThereOverloadedFunction = true;
//            }
//        }

        FuncSTO sto = new FuncSTO(id, ret, params, ref); //
        //sto.setRef(ref);


        m_symtab.insert(sto);
        //m_symtab.insertOverloadedFunc(hKey, sto); //all funcSTO goes into HashMap
        m_symtab.openScope();
        m_symtab.setFunc(sto);
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoFuncDecl_2() {

        m_symtab.closeScope();
        m_symtab.setFunc(null);
    }


    //----------------------------------------------------------------
    // Method for Function parameter checking
    //----------------------------------------------------------------
    void DoFormalParams(Vector<STO> params) {
        FuncSTO func = m_symtab.getFunc();
        //System.out.print(params.get(0));
        if (func == null) {
            m_nNumErrors++;
            m_errors.print("internal: DoFormalParams says no proc!");
        }


        for (STO param : params) {
            m_symtab.insert(param);
        }
        func.setParamVec(params);
        func.setParamCount(params.size()); // set the

        String key = makeHKey(func.getName(), params);
        if(func.getOverloaded(key) == null){
            //throw error
            func.addOverload(key, func);

        }
        else{
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error9_Decl, func.getName()));
            return;
        }

    }

    //----------------------------------------------------------------
    // Opens the Scope, global, function, brackets.
    //----------------------------------------------------------------
    void DoBlockOpen() {
        // Open a scope.
        m_symtab.openScope();
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    void DoBlockClose() {
        m_symtab.closeScope();
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    STO DoAssignExpr(STO stoDes, STO expr) {
        if (expr.isError()) {
            m_nNumErrors++;
            return expr;
        }
        if (stoDes.isError()) {
            m_nNumErrors++;
            return stoDes;
        }
        if (!stoDes.isModLValue()) {
            if (expr instanceof ErrorSTO) {
                return new ErrorSTO(ErrorMsg.error3a_Assign);
            }
            m_nNumErrors++;
            //      "Left-hand operand is not assignable (not a modifiable L-value).";
            m_errors.print(ErrorMsg.error3a_Assign);
//			STO result = new ExprSTO(stoDes.getName()+expr.getName(), expr.getType());
//			result.markRVal();
//			return result;
            // Good place to do the assign checks
            return new ErrorSTO(ErrorMsg.error3a_Assign);
        }
        if (!expr.getType().isAssignableTo(stoDes.getType())) {

            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error3b_Assign, getTypeName(expr), getTypeName(stoDes)));
            return new ErrorSTO(ErrorMsg.error3a_Assign); // do we need this?
        }
        //error3b_Assign ="Value of type %T not assignable to variable of type %T.";

        return stoDes;
    }


    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    STO DoFuncCall(STO sto, Vector<STO> argTyp) {
        // recursive case? calling main inside main;
        FuncSTO recurFunc = m_symtab.getFunc();


        // func holds expected param


        STO func = m_symtab.access(sto.getName());
        Vector<STO> paramList = func.getParamVec(); //<--- error case if undeclared function-call then
        //		then throw nullpointerException

        String hKey = makeHKey(sto.getName(), argTyp); //u can use it for anyother func call

        FuncSTO funcSTO = func.isFunc() ? (FuncSTO) func : null;

        //if func is OverLoaded Function then check 9b
        if (funcSTO != null && funcSTO.isOverloaded()) {
            return (DoOverloadedFuncCall(funcSTO, hKey));
        }
        //if func is not Overloaded then check 5
        else {

            if (!sto.isFunc()) {
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.not_function, sto.getName()));
                return new ErrorSTO(sto.getName());
            } else if (!func.isFunc() || func == null) {
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.not_function, func.getName()));
                return new ErrorSTO(sto.getName());
            } else if (argTyp.size() != func.getParamCount()) {
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.error5n_Call, argTyp.size(), func.getParamCount()));
                return new ErrorSTO(sto.getName());
            }


            //chech 0 parm
            // paramType has arguments
            Iterator<STO> it1;
            Iterator<STO> it2;
            Boolean flag = false;
            for (it1 = argTyp.iterator(), it2 = paramList.iterator(); it1.hasNext() && it2.hasNext(); ) {  //VarSTO params : paramTyp && (VarSTO argTyp : ((FuncSTO) func).setParamVec();)){


                STO arg = it1.next();
                STO param = it2.next();

                if (arg.isError()) {
                    return arg;
                }
                if (!param.isRef() && !arg.getType().isAssignableTo(param.getType())) {
                    m_nNumErrors++;

                    m_errors.print(Formatter.toString(ErrorMsg.error5a_Call, getTypeName(arg), param.getName(), getTypeName(param)));
                    flag = true;

                }
                if (param.isRef()) {
                    if (!arg.getType().isEquivalentTo(param.getType())) {
                        m_nNumErrors++;
                        m_errors.print(Formatter.toString(ErrorMsg.error5r_Call, getTypeName(arg), param.getName(), getTypeName(param)));
                        flag = true;

                    }//return new ErrorSTO(sto.getName());
                    else if (!arg.isModLValue()) {
                        m_nNumErrors++;
                        m_errors.print(Formatter.toString(ErrorMsg.error5c_Call, param.getName(), getTypeName(param)));
                        flag = true;
                        //return new ErrorSTO(sto.getName());
                    }
                }
            }
            if (flag) {
                return new ErrorSTO(sto.getName());
            }
//			if (sto == recurFunc) {   // check recursion
//				return sto;
//			}
            ExprSTO ret = new ExprSTO(sto.getName(), sto.getType());
            // check if func sto was called by ref and assign R val or mod l val
            if (func.isRef()) {
                ret.markModLVal();
                ret.setRef(true);
                return ret;
            } else if (!func.isRef()) {
                ret.markRVal();
                return ret;
            }
            System.out.println("here");
            return sto;
        }
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    STO DoDesignator2_Dot(STO sto, String strID) {
        // Good place to do the struct checks

        return sto;
    }

    //----------------------------------------------------------------
    //
    //----------------------------------------------------------------
    STO DoDesignator2_Array(STO sto) {
        // Good place to do the array checks

        return sto;
    }

    //----------------------------------------------------------------
    //	CASE: when there is no :: for assigning value to left
    //----------------------------------------------------------------
    STO DoDesignator3_ID(String strID) {
        STO sto;
        //check variable name in local scope
        if ((sto = m_symtab.accessLocal(strID)) == null) {
            //if there is not variable name in local scope
            //	then check the same name in global scope thus if u find
            //	then return the global scope

            if ((sto = m_symtab.accessGlobal(strID)) == null) {
                if ((sto = m_symtab.access(strID)) == null) {
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(ErrorMsg.undeclared_id, strID));
                    sto = new ErrorSTO(strID);
                }
            }

        }
        return sto;
    }

    //----------------------------------------------------------------
    // CASE: when there is :: for accessing global scope
    //----------------------------------------------------------------
    Type DoStructType_ID(String strID) {
        STO sto;

        if ((sto = m_symtab.access(strID)) == null) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.undeclared_id, strID));
            return new ErrorType();
        }

        if (!sto.isStructdef()) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.not_type, sto.getName()));
            return new ErrorType();
        }

        return sto.getType();
    }

    STO DoConditionCheck(STO condition) {
        Type conType = condition.getType();
        if (condition.isError()) {
            return condition;
        }
        if (!conType.isBool()) {

            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error4_Test, conType.getName()));
            return new ErrorSTO(condition.getName());
        }
        return condition;
    }

    STO CheckGlobalColonColon(String strID) {
        STO sto;
        if ((sto = m_symtab.accessGlobal(strID)) == null) {
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error0g_Scope, strID));
            return new ErrorSTO(strID);
        }
        return sto;
    }


    STO DoBinaryExpr(STO a, Operator o, STO b) {
        if (b.isError()) {
            return b;
        }
        if (a.isError()) {
            return a;
        }
        STO result = o.checkOperands(a, b);
        if (result.isError()) {
            m_nNumErrors++;
            m_errors.print(result.getName());
            return new ErrorSTO(result.getName());
        }
        return result;
    }


    STO DoIncDecOp(STO a, Operator o) {
        STO result = o.checkOperands(a);

        if (a.isError()) {
            return a;
        }
        if (result.isError()) {
            m_nNumErrors++;
            m_errors.print(result.getName());
            return new ErrorSTO(result.getName());
        }

        return result;
    }

    STO DoUnaryOp(STO a, Operator o) {

        if (a.isError()) {
            return new ErrorSTO("DoUnaryOp" + a.getName() + o.getName());
        }
        STO result = o.checkOperands(a);
        if (result.isError()) {
            m_nNumErrors++;
            m_errors.print(result.getName());
            return new ErrorSTO("DoUnaryOp" + a.getName() + o.getName());
        }

        return result;
    }

    // this function is called on void return type functions
    STO DoVoidReturn() {
        FuncSTO result = m_symtab.getFunc();

        if (!(result.getType().isVoid())) {
            m_nNumErrors++;
            m_errors.print(ErrorMsg.error6a_Return_expr);
            return new ErrorSTO(result.getName());
        } else {
            //m_symtab.setFunc(null);
            return new ExprSTO(result.getName()); //
        }
    }

    STO DoExprReturn(STO a) {
        if (a.isError()) {
            return a;
        }
        FuncSTO result = m_symtab.getFunc();
//        if (a == result) {
//
//            return a;
//        }
        Type resultType = result.getType();
        Type exprType = a.getType();

        //if this return Key is in top level


        //type check pass by value
        if (!result.isRef()) {
            //if type is different but is assignable ex) int to float
            if (!exprType.isAssignableTo(resultType)) {
                //error6a_Return_type =
                //"Type.Type of return expression (%T), not assignment compatible with function's return type (%T).";
                m_nNumErrors++;
                m_errors.print(Formatter.toString(ErrorMsg.error6a_Return_type,
                        getTypeName(a), getTypeName(resultType)));

                return new ErrorSTO(a.getName());
            } else {
                //m_symtab.setFunc(null);
                return new ExprSTO(result.getName());
            }


        } else if (result.isRef()) // sane check
        //pass by reference
        //the type of the return expression is not equivalent to the return type of the function
        {


            if (!resultType.isEquivalentTo(exprType)) {  //resultType != exprType) {
                //error6b_Return_equiv =
                //"Type.Type of return expression (%T) is not equivalent to the function's return type (%T).";
                if (a.isRef()) {
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(ErrorMsg.error6b_Return_equiv,
                            getTypeName(a) + "*", getTypeName(result)));
                } else {
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(ErrorMsg.error6b_Return_equiv,
                            getTypeName(a), getTypeName(result)));
                }
                return new ErrorSTO("DoExprReturn" + a.getName());
            }
            else if(!(a.isModLValue())) {
                //error6b_Return_modlval =
                //		"Return expression is not a modifiable L-value for function that returns by reference.";
                m_nNumErrors++;
                m_errors.print(ErrorMsg.error6b_Return_modlval);
                return new ErrorSTO(a.getName());
            }
            else {
                //System.out.println("clearing func2");
                //m_symtab.setFunc(null);
                ExprSTO ret = new ExprSTO("result of " + result.getName());
                ret.setRef(true);
                return ret;
            }


        }
        System.out.println("In DoExpReturn this should never reach");
        m_symtab.setFunc(null);
        return new ExprSTO(result.getName());
    }


//		STO DoNoReturn (Vector < STO > ret) {
//			FuncSTO result = m_symtab.getFunc();
//			Type resultType = result.getReturnType();
//			//if there is no ReturnType in Top-level
//
//			// ret should be null if return is empty.
//			if (!resultType.isVoid() && result.getLevel() == 0 && ret == null) {
//				//check for return
//				m_nNumErrors++;
//				m_errors.print(ErrorMsg.error6c_Return_missing);
//				return new ErrorSTO(result.getName());
//			} else {
//				//if there is return stmt and correspond to retunType
//				return new ExprSTO(result.getName());
//			}
//		}


    void DoNoReturn(Vector<String> stmts) {

        FuncSTO result = m_symtab.getFunc();
        Type resultType = result.getReturnType();

        //if there is no ReturnType in Top-level
        boolean flag = false;
        for (String ret : stmts) {
            if ((ret != null) && (ret == "return")) {
                flag = true;
            }
        }

        if (!resultType.isVoid() && !flag) {
            m_nNumErrors++;
            m_errors.print(ErrorMsg.error6c_Return_missing);
            return;
        } else {
            return;
        }
    }


    STO DoExitExpr(STO a) {
        if (a.isError()) {
            return a;
        }
        Type aType = a.getType();

        if (!(aType.isAssignableTo(new intType("int", 4)))) {
            //error7_Exit  =
            //"Exit expression (type %T) is not assignable to int.";

            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error7_Exit, aType.getName()));
            return new ErrorSTO(a.getName());
        }

        //if assignable to int then return expr
        // double check what to return when you exit
        return new ExprSTO(a.getName());
    }

    //check 9a
    STO DoOverloadedFuncCheck(String id, Vector<STO> param) {

        String hKey = makeHKey(id, param);
        //since already checked in DoFuncDecl_1_param
        FuncSTO a = (m_symtab.isInHMap(hKey)) ? (m_symtab.getOverLoadedFuncs(hKey))
                : (FuncSTO) m_symtab.access(id);

        Vector<STO> aParam = a.getParamVec();

        //if void call
        if (param == null) {
            param = new Vector<STO>();
        }


        //System.out.println("caught here");

        //if hashcode is same then exactly same function (even names too)
        //thus save time to check other details
        if (m_symtab.isInHMap(hKey)) {
            //error9_Decl  =
            //"Duplicate declaration of overloaded function %S.";
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error9_Decl, id));
            ErrorSTO err = new ErrorSTO(id);
            return err;
        }
        //most overloaded cases goes to this (even if same function
        //but param is different names then goes here
        else {
            //System.out.println("caught here != hash");
            if (aParam.size() == param.size()) {
                //System.out.println("caught here == param size");
                boolean same = true;
                for (int i = 0; i < param.size(); i++) {
                    if (aParam.get(i).getType().getName() != param.get(i).getType().getName()) {
                        same = false;
                    }
                }
                if (same) //have same param list re declared
                {
                    m_nNumErrors++;
                    m_errors.print(Formatter.toString(ErrorMsg.error9_Decl, id));
                    ErrorSTO err = new ErrorSTO(id);
                    return err;
                } else {
                    //System.out.println("caught here same = false");
                    a.setOverloaded(true);
                    return null; //if its overloaed function then return null
                }
            } else {
                //System.out.println("caught here != size");
                //if size is different then number of param is different thus
                //it's overloaded function of id
                a.setOverloaded(true);
                return null; //if its overloaed function then return null
            }
        }
    }

    //check 9b
    STO DoOverloadedFuncCall(FuncSTO func, String hKey) {
        if (m_symtab.isInHMap(hKey)) {
            return func;
        } else {
            //error9_Illegal  =
            //		"Illegal call to overloaded function %S.";
            m_nNumErrors++;
            m_errors.print(Formatter.toString(ErrorMsg.error9_Illegal, func.getName()));
            ErrorSTO err = new ErrorSTO(func.getName());
            return err;
        }
    }


    // Helper Function
    String getTypeName(Type typ) {
        return typ.getName();
    }

    String getTypeName(STO sto) {
        return sto.getType().getName();
    }

    //its for making HashMap Key
    String makeHKey(String id, Vector<STO> param) {
        String paramKey = id;

        //set up H_Map key
        if (param != null) {
            for (int i = 0; i < param.size(); i++) {
                paramKey += param.get(i).getType().getName()+".";
            }
        }
        return paramKey;
    }

    STO markAmpersand(STO expr) {
        if (expr.isError()) {
            return expr;
        }

        expr.setRef(true);
        return expr;
    }


}
