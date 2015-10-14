//---------------------------------------------------------------------
// CSE 131 Reduced-C Compiler Project
// Copyright (C) 2008-2015 Garo Bournoutian and Rick Ord
// University of California, San Diego
//---------------------------------------------------------------------


import java_cup.runtime.*;
import sun.tools.jstat.Identifier;
import java.util.Iterator;
import java.util.Map;
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
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
		}

		VarSTO sto = new VarSTO(id);
		m_symtab.insert(sto);
	}
	void DoVarDeclwType(String id, Type typ)
	{
		if (m_symtab.accessLocal(id) != null)
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
		}
		VarSTO sto = new VarSTO(id, typ);
		m_symtab.insert(sto);
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
	void DoConstDecl(String id)
	{
		if (m_symtab.accessLocal(id) != null)
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
		}

		ConstSTO sto = new ConstSTO(id, null, 0);   // fix me
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
		if (m_symtab.accessLocal(id) != null)
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
		}

		FuncSTO sto = new FuncSTO(id);

		m_symtab.insert(sto);

		m_symtab.openScope();
		m_symtab.setFunc(sto);
		sto.setLevel(m_symtab.getLevel());
	}

	void DoFuncDecl_1_param(String id, Type ret)
	{

		if (m_symtab.accessLocal(id) != null)
		{
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.redeclared_id, id));
		}

		FuncSTO sto = new FuncSTO(id, ret);
		m_symtab.insert(sto);

		System.out.println();

		m_symtab.openScope();
		m_symtab.setFunc(sto);
		sto.setLevel(m_symtab.getLevel());

	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void DoFuncDecl_2()
	{
		DoNoRerutn();
		m_symtab.closeScope();
		m_symtab.setFunc(null);
	}

	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	void DoFormalParams(Vector<STO> params)
	{
		//System.out.print(params.get(0));
		if (m_symtab.getFunc() == null)
		{
			m_nNumErrors++;
			m_errors.print ("internal: DoFormalParams says no proc!");
		}

		FuncSTO func = m_symtab.getFunc();
		if( params != null) {
			func.setParamVec(params);
			func.setParamCount(params.size()); // set the
		}
		func.setParamCount(0);
			// insert parameters here
	}

	//----------------------------------------------------------------
	//
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
		if( !stoDes.getType().isAssignableTo(expr.getType())){
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.error3b_Assign, getName(expr), getName(stoDes)));
		}
		//error3b_Assign ="Value of type %T not assignable to variable of type %T.";

		return stoDes;
	}


	//----------------------------------------------------------------
	//
	//----------------------------------------------------------------
	STO DoFuncCall(STO sto, Vector<STO> paramTyp)
	{
		// func holds expected param
		STO func =  m_symtab.access(sto.getName());
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
		else if (((FuncSTO) sto).getParamCount() != ((FuncSTO)func).getParamCount()){
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.error5n_Call,sto.getName(), func.getName()));
			return new ErrorSTO(sto.getName());
		}
		// paramType has arguments
		Iterator<STO> it1;
		Iterator<STO> it2;
		Vector<STO> paramList = ((FuncSTO) func).getParamVec();

		it1 = paramTyp.iterator();
		it2 = paramList.iterator();
		for( it1 = paramTyp.iterator(), it2 = paramList.iterator(); it1.hasNext() && it2.hasNext();){  //VarSTO params : paramTyp && (VarSTO argTyp : ((FuncSTO) func).setParamVec();)){
			STO arg =  it1.next();
			STO param =  it2.next();

			if (!param.isRef() && !arg.getType().isAssignableTo(param.getType())  ){
				m_nNumErrors++;
				m_errors.print(Formatter.toString(ErrorMsg.error5a_Call, getName(arg), getName(param), getName(param)));
			}
			if(param.isRef() && arg.getType().isEquivalentTo(param.getType())){
				m_nNumErrors++;
				m_errors.print(Formatter.toString(ErrorMsg.error5r_Call, getName(arg), getName(param), getName(param)));
			}
			if(param.isRef() && !arg.isModLValue()){
				m_nNumErrors++;
				m_errors.print(Formatter.toString(ErrorMsg.error5c_Call, getName(param), getName(param)));
			}
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


		if (o.checkOperands(a, b).isError())
		{
			m_nNumErrors++;
			m_errors.print(o.checkOperands(a, b).getName());
			return new ErrorSTO(o.checkOperands(a, b).getName());
		}
		return o.checkOperands(a, b);
	}


	STO DoUnaryOp(STO a, Operator o) {
		STO result = o.checkOperands(a);
		if (result.isError()) {
			m_nNumErrors++;
			m_errors.print(result.getName());
			return new ErrorSTO(result.getName());
		}

		return result;
	}

	STO DoVoidReturn()
	{
		FuncSTO result;

		//get current funcSTO
		result =  m_symtab.getFunc();

		//if this return Key is in top level
		if(result.getLevel() == m_symtab.getLevel())
		{
			result.setReturn_top_level(true);
		}


		if(result.getReturnType() instanceof VoidType)
			return null;
		else
		{
			m_nNumErrors++;
			m_errors.print(ErrorMsg.error6a_Return_expr);
			return new ErrorSTO(result.getName());
		}
	}

	STO DoExprReturn(STO a)
	{
		FuncSTO result;
		result = m_symtab.getFunc();
		Type resultType = result.getReturnType();
		Type aType = a.getType();

		//if this return Key is in top level
		if(result.getLevel() == m_symtab.getLevel())
		{
			result.setReturn_top_level(true);
		}

		//type check pass by value
		if (!result.isRetByRef())
		{
			if(resultType != aType)
			{
				//if type is different but is assignable ex) int to float
				if (aType.isAssignableTo(resultType))
				{
					return a;
				}
				else {

					//error6a_Return_type =
					//"Type.Type of return expression (%T), not assignment compatible with function's return type (%T).";
					m_nNumErrors++;
					m_errors.print(Formatter.toString(
							ErrorMsg.error6a_Return_type,
							result.getReturnType().getName(),
							a.getType().getName()));

					return new ErrorSTO(a.getName());
				}
			}
		}
		else
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


			if (resultType != aType) {
				//error6b_Return_equiv =
				//"Type.Type of return expression (%T) is not equivalent to the function's return type (%T).";
				m_nNumErrors++;
				m_errors.print(Formatter.toString(
						ErrorMsg.error6b_Return_equiv,
						a.getType().getName(),
						result.getReturnType().getName()));
				return new ErrorSTO(a.getName());
			}
			else
			{
				return a;
			}

		}
		return a;
	}


	STO DoNoRerutn() {

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

	STO DoExitExpr(STO a)
	{

		Type aType = a.getType();
		if (!(aType.isAssignableTo(new intType("intType", 4))))
		{
			//error7_Exit  =
			//"Exit expression (type %T) is not assignable to int.";
			m_nNumErrors++;
			m_errors.print(Formatter.toString(ErrorMsg.error7_Exit, aType.getName()));
		}

		//if assignable to int then return expr
		return a;
	}

	// Helper Function
	String getName(Type typ){
		return typ.getName();
	}
	String getName(STO sto){
		return sto.getType().getName();
	}
}
