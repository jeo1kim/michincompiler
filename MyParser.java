//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------


import com.sun.tools.internal.jxc.ap.Const;
import java_cup.runtime.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;
import java.lang.*;


class MyParser extends parser
{
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
	public MyParser(Lexer lexer, ErrorPrinter errors, boolean debugMode)
	{
		m_lexer = lexer;
		m_symtab = new SymbolTable();
		m_errors = errors;
		m_debugMode = debugMode;
		m_nNumErrors = 0;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public boolean Ok()
	{
		return m_nNumErrors == 0;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public Symbol scan()
	{
		Token t = m_lexer.GetToken();

		//	We'll save the last token read for error messages.
		//	Sometimes, the token is lost reading for the next
		//	token which can be null.
		m_strLastLexeme = t.GetLexeme();

		switch (t.GetCode())
		{
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
	public void syntax_error(Symbol s)
	{
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void report_fatal_error(Symbol s)
	{
		m_nNumErrors++;
		if (m_bSyntaxError)
		{
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
	public void unrecovered_syntax_error(Symbol s)
	{
		report_fatal_error(s);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void DisableSyntaxError()
	{
		m_bSyntaxError = false;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void EnableSyntaxError()
	{
		m_bSyntaxError = true;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public String GetFile()
	{
		return m_lexer.getEPFilename();
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public int GetLineNum()
	{
		return m_lexer.getLineNumber();
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public void SaveLineNum()
	{
		m_nSavedLineNum = m_lexer.getLineNumber();
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	public int GetSavedLineNum()
	{
		return m_nSavedLineNum;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void DoProgramStart()
	{
		// Opens the global scope.

		m_symtab.openScope();
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void DoProgramEnd()
	{
		m_symtab.closeScope();
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void DoVarDecl(String id)
	{
		if (m_symtab.accessLocal(id) != null)
		if (m_symtab.accessLocal(id) != null)
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
		}

		VarSTO sto = new VarSTO(id);
		m_symtab.insert(sto);
	}
	void DoVarDeclwType(String id, Type typ, boolean stat, Vector<STO> array, STO init)
	{
//		if (init.isError()){
//			return;    // might wanan change with !init.isError()
//		}
		if (m_symtab.accessLocal(id) != null)
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
		}
		VarSTO sto = new VarSTO(id, typ);
		if(stat){
			sto.setStatic(stat); // set Variable static
		}
		if (array.size() == 0 && (init != null)){ // indicates that this var is not an array and init exp exist
			// do the type check with init if it exist
			if( !init.getType().isAssignableTo(sto.getType()) && !init.isError()){
				m_nNumErrors++;
				m_errors.print(Formatter.toString(ErrorMsg.error3b_Assign, getName(init), getName(sto)));
				return;
			}
			else{ // exp is assignable to this varSto type. so
				m_symtab.insert(sto);
				return;
			}
		}
		//case where var is an array
		else if(array.size()>0){
			System.out.print(array.get(0));
			sto.setType(new ArrayType("array",array.size())); // double check array size
			m_symtab.insert(sto);
			return;
		}
		else{
			m_symtab.insert(sto);

		}
	}

	void DoVarDeclwAuto(String id, STO expr, boolean stat)
	{
		if (expr.isError()){
			return;
		}
		if (m_symtab.accessLocal(id) != null)
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
		}

		if (!expr.isConst()){
			VarSTO sto = new VarSTO(id, expr.getType());
			if(stat){
				sto.setStatic(stat); // set variable static
			}
			m_symtab.insert(sto);

		}
		else{
			ConstSTO sto = new ConstSTO(id, expr.getType());
			if(stat){
				sto.setStatic(stat); // set variable static
			}
			m_symtab.insert(sto);
		}

		//m_symtab.insert(sto);
	}
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void DoExternDecl(String id)
	{
		if (m_symtab.accessLocal(id) != null)
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
		}

		VarSTO sto = new VarSTO(id);
		m_symtab.insert(sto);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void DoConstDecl(String id, Type typ, STO exp, boolean stat)
	{
		if (exp.isError()){
			return;
		}
		if (m_symtab.accessLocal(id) != null)
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
		}

		ConstSTO sto = new ConstSTO(id, typ, 0);   // fix me
		sto.markModLVal();
		m_symtab.insert(sto);
	}
	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void DoConstDeclwAuto(String id, STO exp, boolean stat)
	{
		if (exp.isError()){
			return;
		}
		if (m_symtab.accessLocal(id) != null)
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
		}

		ConstSTO sto = new ConstSTO(id, exp.getType(), 0);   // fix me
		sto.markModLVal();
		m_symtab.insert(sto);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void DoStructdefDecl(String id)
	{
		if (m_symtab.accessLocal(id) != null)
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
		}
		
		StructdefSTO sto = new StructdefSTO(id);
		m_symtab.insert(sto);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void DoFuncDecl_1(String id)
	{
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

		m_symtab.openScope();
		m_symtab.setFunc(sto);
		sto.setLevel(m_symtab.getLevel());
	}

	void DoFuncDecl_1_param(String id, Type ret)

	{

		STO a = m_symtab.accessLocal(id);
		if (a != null && !(a.isFunc())) //if found STO is not function
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
		}

		FuncSTO sto = new FuncSTO(id, ret);
		m_symtab.insert(sto);


		m_symtab.openScope();
		m_symtab.setFunc(sto);
		sto.setLevel(m_symtab.getLevel());

	}
	void DoFuncDecl_1_param(String id, Type ret, boolean ref)
	{
		STO a = m_symtab.accessLocal(id);
		if (a != null && !(a.isFunc())) //if found STO is not function
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
		}

		FuncSTO sto = new FuncSTO(id, ret);
		sto.setRef(ref);
		m_symtab.insert(sto);

		m_symtab.openScope();
		m_symtab.setFunc(sto);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void DoFuncDecl_2()
	{
		//System.out.println("DoFuncDecl2"+m_symtab.getFunc().getName());
		m_symtab.closeScope();
		m_symtab.setFunc(null);
	}


	//----------------------------------------------------------------
	// Method for Function parameter checking
	//----------------------------------------------------------------
	void DoFormalParams(Vector<STO> params)
	{
		FuncSTO func = m_symtab.getFunc();
		//System.out.print(params.get(0));
		if (func == null)
		{
			m_nNumErrors++;
			m_errors.print ("internal: DoFormalParams says no proc!");
		}

		for(STO param : params){
			m_symtab.insert(param);
		}
		func.setParamVec(params);
		func.setParamCount(params.size()); // set the

			// insert parameters here
	}

	//----------------------------------------------------------------
	// Opens the Scope, global, function, brackets.
	//----------------------------------------------------------------
	void DoBlockOpen()
	{
		// Open a scope.
		m_symtab.openScope();
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void DoBlockClose()
	{
		m_symtab.closeScope();
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	STO DoAssignExpr(STO stoDes, STO expr)
	{

		if (!stoDes.isModLValue())
		{
			if ( expr instanceof ErrorSTO){
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
		if( !expr.getType().isAssignableTo(stoDes.getType()) && !expr.isError()){
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.error3b_Assign, getName(expr), getName(stoDes)));
			return new ErrorSTO(ErrorMsg.error3a_Assign); // do we need this?
		}
		//error3b_Assign ="Value of type %T not assignable to variable of type %T.";

		return stoDes;
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	STO DoFuncCall(STO sto, Vector<STO> argTyp)
	{
		// func holds expected param
		STO func =  m_symtab.access(sto.getName());
		Vector<STO> paramList = func.getParamVec(); //<--- error case if undeclared function-call then
													//		then throw nullpointerException



		if (!sto.isFunc())
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.not_function, sto.getName()));
			return new ErrorSTO(sto.getName());
		}
		else if(!func.isFunc() || func == null)
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.not_function, func.getName()));
			return new ErrorSTO(sto.getName());
		}
		else if ( argTyp.size() != func.getParamCount()){
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.error5n_Call,sto.getName(), func.getName()));
			return new ErrorSTO(sto.getName());
		}


		//chech 0 parm
		// paramType has arguments
		Iterator<STO> it1;
		Iterator<STO> it2;

		Boolean flag = false;
		for( it1 = argTyp.iterator(), it2 = paramList.iterator(); it1.hasNext() && it2.hasNext();){  //VarSTO params : paramTyp && (VarSTO argTyp : ((FuncSTO) func).setParamVec();)){
			STO arg =  it1.next();
			STO param =  it2.next();

			if (!param.isRef() && !arg.getType().isAssignableTo(param.getType())  ){
				m_nNumErrors++;
				m_errors.print(Formatter.toString(ErrorMsg.error5a_Call, getName(arg), getName(param), getName(param)));
				flag = true;
			}
			if(param.isRef() && arg.getType().isEquivalentTo(param.getType())){
				m_nNumErrors++;
				m_errors.print(Formatter.toString(ErrorMsg.error5r_Call, getName(arg), getName(param), getName(param)));
				flag = true;
			}
			if(param.isRef() && !arg.isModLValue()){
				m_nNumErrors++;
				m_errors.print(Formatter.toString(ErrorMsg.error5c_Call, getName(param), getName(param)));
				flag = true;
			}
		}
		if(flag){
			return new ErrorSTO(sto.getName());
		}

		// check if func sto was called by ref and assign R val or mod l val
		if( func.isRef()) {
			sto.markModLVal();
			return sto;
		}else if(!func.isRef())
		{
			sto.markRVal();
			return sto;
		}
		return sto;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	STO DoDesignator2_Dot(STO sto, String strID)
	{
		// Good place to do the struct checks

		return sto;
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	STO DoDesignator2_Array(STO sto)
	{
		// Good place to do the array checks

		return sto;
	}

	//----------------------------------------------------------------
	//	CASE: when there is no :: for assigning value to left
	//----------------------------------------------------------------
	STO DoDesignator3_ID(String strID)
	{
		STO sto;
		//check variable name in local scope
		if ((sto = m_symtab.accessLocal(strID)) == null)
		{
			//if there is not variable name in local scope
			//	then check the same name in global scope thus if u find
			//	then return the global scope
			if ((sto = m_symtab.accessGlobal(strID)) == null) {
				m_nNumErrors++;
				m_errors.print(Formatter.toString(ErrorMsg.undeclared_id, strID));
				sto = new ErrorSTO(strID);
			}

		}
		return sto;
	}

	//----------------------------------------------------------------
	// CASE: when there is :: for accessing global scope
	//----------------------------------------------------------------
	Type DoStructType_ID(String strID)
	{
		STO sto;

		if ((sto = m_symtab.access(strID)) == null)
		{
			m_nNumErrors++;
		 	m_errors.print(Formatter.toString(ErrorMsg.undeclared_id, strID));
			return new ErrorType();
		}

		if (!sto.isStructdef())
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.not_type, sto.getName()));
			return new ErrorType();
		}

		return sto.getType();
	}

	STO DoConditionCheck(STO condition)
	{
		Type conType = condition.getType();
		if(condition.isError()){
			return condition;
		}
		if (!conType.isBool()) {
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.error4_Test, conType.getName()));
			return new ErrorSTO(condition.getName());
		}
		return condition;
	}

	STO CheckGlobalColonColon(String strID)
	{
		STO sto;
		if ((sto = m_symtab.accessGlobal(strID)) == null)
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.error0g_Scope, strID));
			return new ErrorSTO(strID);
		}
		return sto;
	}



	STO DoBinaryExpr(STO a, Operator o, STO b)
	{
		if(b.isError()){
			return b;
		}
		if(a.isError()){
			return a;
		}
		STO result = o.checkOperands(a, b);
		if (result.isError())
		{
			m_nNumErrors++;
			m_errors.print(result.getName());
			return new ErrorSTO(result.getName());
		}
		return result;
	}


	STO DoIncDecOp(STO a, Operator o) {
		STO result = o.checkOperands(a);

		if(a.isError()){
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
		STO result = o.checkOperands(a);
		if(a.isError()){
			return a;
		}
		if (result.isError()) {
			m_nNumErrors++;
			m_errors.print(result.getName());
			return new ErrorSTO(result.getName());
		}

		return result;
	}

	// this function is called on void return type functions
	STO DoVoidReturn()
	{
		FuncSTO result = m_symtab.getFunc();

		if(!(result.getType().isVoid())) {
			m_nNumErrors++;
			m_errors.print(ErrorMsg.error6a_Return_expr);
			return new ErrorSTO(result.getName());
		}
		else {
			m_symtab.setFunc(null);
			return new ExprSTO(result.getName()); //
		}
	}

	STO DoExprReturn(STO a)
	{
		FuncSTO result = m_symtab.getFunc();
		Type resultType = result.getType();
		Type exprType = a.getType();

		//if this return Key is in top level

		if( a.isError()){
			return a;
		}
		//type check pass by value
		if (!result.isRetByRef())
		{
			if(resultType != exprType)
			{
				//if type is different but is assignable ex) int to float
				if (!exprType.isAssignableTo(resultType))
				{
					//error6a_Return_type =
					//"Type.Type of return expression (%T), not assignment compatible with function's return type (%T).";
					m_nNumErrors++;
					m_errors.print(Formatter.toString(ErrorMsg.error6a_Return_type,
							getName(resultType), getName(a)));

					return new ErrorSTO(a.getName());
				}
				else {
					//System.out.println("clearing func");
					//m_symtab.setFunc(null);
					return new ExprSTO(result.getName());
				}


			}
		}
		else if(result.isRetByRef()) // sane check
		//pass by reference
		//the type of the return expression is not equivalent to the return type of the function
		{
			//Expression is not ModLValue
			if (!(a.isModLValue()))
			{
				//error6b_Return_modlval =
				//		"Return expression is not a modifiable L-value for function that returns by reference.";
				m_nNumErrors++;
				m_errors.print(ErrorMsg.error6b_Return_modlval);
				return new ErrorSTO(a.getName());
			}


			if (!resultType.isEquivalentTo(exprType)){  //resultType != exprType) {
				//error6b_Return_equiv =
				//"Type.Type of return expression (%T) is not equivalent to the function's return type (%T).";
				m_nNumErrors++;
				m_errors.print(Formatter.toString(ErrorMsg.error6b_Return_equiv,
						getName(a), getName(result)));

				return new ErrorSTO(a.getName());
			}
			else
			{
				//System.out.println("clearing func2");
				//m_symtab.setFunc(null);
				return new ExprSTO(result.getName());
			}

		}
		System.out.println("In DoExpReturn this should never reach");
		m_symtab.setFunc(null);
		return new ExprSTO(result.getName());
	}


	STO DoNoReturn(Vector<STO> ret) {
		FuncSTO result = m_symtab.getFunc();
		Type resultType = result.getReturnType();
		//if there is no ReturnType in Top-level

		// ret should be null if return is empty.
		if (!resultType.isVoid() && result.getLevel() == 0 && ret == null)
		{
			//check for return
			m_nNumErrors++;
			m_errors.print(ErrorMsg.error6c_Return_missing);
			return new ErrorSTO(result.getName());
		}
		else {
			//if there is return stmt and correspond to retunType
			return new ExprSTO(result.getName());
		}
	}


	/*
	* STO DoNoRerutn() {

		FuncSTO result = m_symtab.getFunc();
		Type resultType = result.getReturnType();

		//if there is no ReturnType in Top-level
		if(!(result.getReturn_top_level()))
		{
			if (resultType instanceof VoidType )
			{

				return null;
			}
			m_nNumErrors++;
			m_errors.print(ErrorMsg.error6c_Return_missing);
			return new ErrorSTO(result.getName());
		}
		else{ //if there is return stmt and correspond to retunType
			return null;
		}


	}
	* */

	STO DoExitExpr(STO a)
	{
		if(a.isError()){
			return a;
		}
		Type aType = a.getType();
		if (!(aType.isAssignableTo(new intType("int", 4))))
		{
			//error7_Exit  =
			//"Exit expression (type %T) is not assignable to int.";
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.error7_Exit, aType.getName()));
		}

		//if assignable to int then return expr
		// double check what to return when you exit
		return new ExprSTO(a.getName());
	}

	STO DoOverloadedFuncCheck(String id, Vector<STO> param)
	{
		STO a = m_symtab.access(id);
		Vector<STO> aParam = a.getParamVec();


		if (a != null) {
			if (aParam.size() == param.size()) {
				for (int i = 0; i < aParam.size(); i++)
				{
					if ( aParam.get(i).getType() != param.get(i).getType())
					{
						m_nNumErrors++;
						m_errors.print(Formatter.toString(ErrorMsg.error9_Decl, id));
						ErrorSTO err = new ErrorSTO(id);
					}
				}

			}
		}
		return (new FuncSTO(id));
	}

	// Helper Function
	String getName(Type typ){
		return typ.getName();
	}
	String getName(STO sto){
		return sto.getType().getName();
	}
}
