
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;


public class AssemblyCodeGenerator {

    // 1
    private int indent_level = 0;

    // 2
    private static final String ERROR_IO_CLOSE =
            "Unable to close fileWriter";
    private static final String ERROR_IO_CONSTRUCT =
            "Unable to construct FileWriter for file %s";
    private static final String ERROR_IO_WRITE =
            "Unable to write to fileWriter";

    // 3
    private FileWriter fileWriter;

    // 4
    private static final String FILE_HEADER =
            "/*\n" +
                    " * Generated %s\n" +
                    " */\n\n";

    // 5
    private static final String SEPARATOR = "\t";

    private int offset = 0; //to check what offset of that value is
    private String infunc = null; //used as a flag to check if it is inside function or not i belive there will be a better way tho
    // 6
    //parameters
    private static final String ONE_PARAM = "%s" + SEPARATOR + "%s\n";
    private static final String TWO_PARAM = "%s" + SEPARATOR + "%s, %s\n";
    private static final String THREE_PARAM = "%s" + SEPARATOR + "%s, %s, %s\n";

    private static final String NOPARAM_3 = "%s\n%s\n%s\n";


    public static final String OFFSET_TOTAL = " = -(92 + %s) & -8";
    public static final String SP = "%sp";
    public static final String FP = "%fp";

    //synthetic instructions
    private static final String SET_OP = "set    \t";
    private static final String SAVE_OP = "save\t";
    private static final String RET_OP = "ret\n";
    private static final String RESTORE_OP = "restore\n";
    private static final String NOP_OP = "nop\n";

    private static final String CMP_OP = "cmp   \t";
    private static final String FCMP_OP = "fcmps \t";
    private static final String JMP_OP = "jmp   \t";
    private static final String CALL_OP = "call   \t";
    private static final String TST_OP = "tst   \t";
    private static final String NOT_OP = "not   \t";
    private static final String NEG_OP = "neg   \t";
    private static final String INC_OP = "inc   \t";
    private static final String DEC_OP = "dec   \t";
    private static final String MOV_OP = "mov   \t";
    private static final String FMOV_OP = "fmovs\t";

    private static final String BA_OP = "ba    \t";
    private static final String BE_OP = "be    \t";
    private static final String BG_OP = "bg    \t";
    private static final String BL_OP = "bl    \t";
    private static final String BLE_OP = "ble    \t";
    private static final String BGE_OP = "bge   \t";
    private static final String BNE_OP = "bne   \t";

    private static final String FBLE_OP = "fble  \t";
    private static final String FBGE_OP = "fbge   \t";
    private static final String FBG_OP = "fbg    \t";
    private static final String FBL_OP = "fbl    \t";
    private static final String FBE_OP = "fbe    \t";
    private static final String FBNE_OP = "fbne   \t";


    private static final String ST_OP = "st     \t";
    private static final String LD_OP = "ld     \t";


    //arithmetic 
    private static final String ADD_OP = "add    \t";
    private static final String SUB_OP = "sub    \t";
    private static final String MUL_OP = ".mul";
    private static final String DIV_OP = ".div";
    private static final String MOD_OP = ".rem";

    private static final String BW_AND_OP = "and    \t";
    private static final String BW_OR_OP = "or    \t";
    private static final String XOR_OP = "xor    \t";

    private static final String FADD_OP = "fadds  \t";
    private static final String FSUB_OP = "fsubs  \t";
    private static final String FMUL_OP = "fmuls  \t";
    private static final String FDIV_OP = "fdivs  \t";


    //section
    private static final String SECTION = ".section" + SEPARATOR + "\"%s\"\n";

    private static final String INIT_SEC = ".init";
    private static final String TEXT_SEC = ".text";
    private static final String DATA_SEC = ".data";
    private static final String BSS_SEC = ".bss";
    private static final String GLOBAL = ".global \t%s\n";
    private static final String ALIGN = ".align  \t%s\n";
    private static final String WORD = ".word   \t%s\n";
    private static final String SKIP = ".skip   \t%s\n";
    private static final String FLOAT = ".single \t0r%s\n";
    private static final String RODATA_SEC = ".rodata";
    private static final String VARCOLON = "%s:\n";

    //private static final String ASCIZ = ".asciz";
    private static final String FLOAT_COUNTER = ".$$.float.%s:\n";
    private static final String CMP_COUNTER = ".$$.cmp.%s";

    private static final String BASIC_FIN_NL = ".$$.%s.%s:\n";
    private static final String BASIC_FIN = ".$$.%s.%s";
    //global auto
    private static final String GL_AUTO_INIT = ".$.init.%s";
    private static final String GL_AUTO_FINI = ".$.init.%s.fini";

    //private static final String SAVE_MAIN = "SAVE.%s.void";
    private static final String SAVE_FUNC = "SAVE.%s%s";
    private static final String FINI_FUNC = "%s%s.fini";

    private static final String NL = "\n";
    /*
     * LOCAL REGISTERS
     */
    private static final String LO = "%l0";
    private static final String L1 = "%l1";
    private static final String L2 = "%l2";
    private static final String L3 = "%l3";
    private static final String L4 = "%l4";
    private static final String L5 = "%l5";
    private static final String L6 = "%l6";

    /*
     * OUTPUT REGISTERS
     */
    private static final String O0 = "%o0";
    private static final String O1 = "%o1";
    private static final String O2 = "%o2";
    private static final String O3 = "%o3";
    private static final String O4 = "%o4";
    private static final String O5 = "%o5";
    private static final String O6 = "%o6";
    private static final String O7 = "%o7";

    /*
     * INPUT REGISTERS
     */
    private static final String I0 = "%i0";
    private static final String I1 = "%i1";
    private static final String I2 = "%i2";
    private static final String I3 = "%i3";
    private static final String I4 = "%i4";
    private static final String I5 = "%i5";
    private static final String I6 = "%i6";
    private static final String I7 = "%i7";

    /*
     * GLOBAL REGISTERS
     */
    private static final String G0 = "%g0";
    private static final String G1 = "%g1";
    private static final String G2 = "%g2";
    private static final String G3 = "%g3";
    private static final String G4 = "%g4";
    private static final String G5 = "%g5";
    private static final String G6 = "%g6";
    private static final String G7 = "%g7";


    // for float
    private static final String f0 = "%f0";
    private static final String F1 = "%f1";
    private static final String F2 = "%f2";
    private static final String F3 = "%f3";
    private static final String F4 = "%f4";
    private static final String F5 = "%f5";


    private static final String L7 = "%l7";
    private static final String FITOS = "fitos  \t";

    // printing
    private static final String ASCIZ = ".asciz \t";
    private static final String intFmt = ".$$.intFmt";
    private static final String strFmt = ".$$.strFmt";
    private static final String strTF = ".$$.strTF";
    private static final String strEndl = ".$$.strEndl";
    private static final String strArrBound = ".$$.strArrBound";
    private static final String strNullPtr = ".$$.strNullPtr";
    private static final String arrCheckCall = ".$$.arrCheck";
    private static final String ptrCheckCall = ".$$.ptrCheck";

    private static final String printBool = ".$$.printBool:\n";
    private static final String printBool2 = ".$$.printBool2:\n";
    private static final String arrCheck = ".$$.arrCheck:\n";
    private static final String arrCheck2 = ".$$.arrCheck2:\n";
    private static final String ptrCheck = ".$$.ptrCheck:\n";
    private static final String ptrCheck2 = ".$$.ptrCheck2:\n";

    private static final String d = "\"%d\"";
    private static final String nl = "\"\\n\"";
    private static final String s = "\"%s\"";
    private static final String tf = "\"false\\0\\0\\0true\"";
    private static final String arrbound = "\"Index value of %d is outside legal range [0,%d).\\n\"";
    private static final String nullptr = "\"Attempt to dereference NULL pointer.\\n\"";


    private static final String var_comment = "! %s = %s\n";
    private static final String var_comment_op = "! %s, %s\n";
    private int floatcounter = 0; //count number of float
    private int cmpcounter = 0;  //count cmp
    private int ifcounter = 0;  //count how many if is called
    private int strFmtCnt = 0;  
    private int andorskipcnt = 0;  //count how many and & or are there
    private int loopcounter = 0; //count how many loops are there 
    private int arraycounter = 0; //count number of arrays
    private boolean func = false;
    private boolean arithmetic = false;

    private int ploopcounter = 0; //for break and continue;

    public AssemblyCodeGenerator(String fileToWrite) {
        try {
            fileWriter = new FileWriter(fileToWrite);

            // 7
            writeAssembly(FILE_HEADER, (new Date()).toString());
        } catch (IOException e) {
            System.err.printf(ERROR_IO_CONSTRUCT, fileToWrite);
            e.printStackTrace();
            System.exit(1);
        }
    }

    //takes care of the offset inc and dec
    public int getOffset() {
        return this.offset;
    }

    public int increaseOffset() {
        return this.offset += 4;
    }

    public int decreaseOffset() {
        return this.offset -= 4;
    }

    //check if the variable is static or global
    public void writeVariable(STO sto, STO init) {
        if (sto.isStatic() || sto.isGlobal()) {
            this.writeGlobalStaticVariable(sto, init);
        } else {
            writeLocalVariable(sto, init);
        }
    }

    public void writeDot(STO stru, STO sto){
        int off = -sto.getSparcOffset()-4;
        String offset = stru.isGlobal() ? stru.getName() :iString(stru.getSparcOffset());
        String base = stru.isGlobal() ? "%g0" : "%fp";

        indent_level =2;
        writeAssembly("! "+stru.getName()+"."+sto.getName()+"\n");
        writeAssembly(TWO_PARAM, SET_OP, offset, O0);
        writeAssembly(THREE_PARAM, ADD_OP, base, O0, O0);
        writeAssembly(TWO_PARAM, SET_OP, iString(off), O1);
        writeAssembly(THREE_PARAM, ADD_OP, G0, O1, O1);
        writeAssembly(THREE_PARAM, ADD_OP, O0, O1, O0);
        decreaseOffset();
        writeAssembly(TWO_PARAM, SET_OP, iString(getOffset()), O1);
        writeAssembly(THREE_PARAM, ADD_OP, FP, O1, O1);
        writeAssembly(TWO_PARAM, ST_OP, O0,"["+O1+"]" );
        newline();

        //sto.setSparcOffset(getOffset());
    }

    public void writeStructDecl(STO struc){
        if(struc.isGlobal()){
            indent_level=1;
            sectionAlign(BSS_SEC, iString(4));
            writeAssembly(GLOBAL, struc.getName());
            decreaseIndent();
            writeAssembly(struc.getName()+":\n");
            increaseIndent();
            writeAssembly(SKIP, iString(struc.getType().getSize()));
            sectionAlign(TEXT_SEC, iString(4));

            writeGlobalAuto(struc, struc);
            sectionAlign(TEXT_SEC, iString(4));
            return;
        }

        writeBasicStruct(struc);
    }

    public void writeBasicStruct(STO struc){
        int size = -struc.getType().getSize();
        String name = struc.getType().getName();
        String base = struc.isGlobal() ?  "%g0": "%fp";
        String off = struc.isGlobal() ? struc.getName() : iString(offset);

        funcIndent();
        struc.setSparcOffset(size + getOffset());
        offset = offset + size;
        writeAssembly("! STRUCTS\n");
        setadd(struc, 0);
        name = name+"."+name+".void";
        writeAssembly(ONE_PARAM, CALL_OP, name);
        writeAssembly(NOP_OP);
        writeStructCtor();

        writeAssembly(TWO_PARAM,SET_OP, ctor, O0);
        writeAssembly(TWO_PARAM, SET_OP, off, O1);
        writeAssembly(THREE_PARAM, ADD_OP, base, O1, O1);
        writeAssembly(TWO_PARAM, ST_OP, O1, "["+O0+"]");
        newline();
    }

    public int ctorcounter = 1;
    String ctor = "";
    public void writeStructCtor(){
        sectionAlign(BSS_SEC, iString(4));
        decreaseIndent();
        ctor = ".$$.ctorDtor." + iString(ctorcounter++);
        writeAssembly(ctor+":\n");
        increaseIndent();
        writeAssembly(SKIP, iString(4));

        sectionAlign(TEXT_SEC, iString(4));
        newline();
    }

    public void writeDtor(STO sto,){

    }

    public boolean structCtor = false;
    public boolean struct = false;
    public void writeStruct(STO sto){
        String name = sto.getName();
        String param = paramtypelist(sto);
        String funcvoid = name+"."+name;
        struct = true;

        if(structCtor == true){
            funcvoid = name+".$"+name;
        }
        structCtor = !structCtor;
        indent_level = 0;

        writeAssembly(VARCOLON, funcvoid+param);

        increaseIndent();
        writeAssembly(TWO_PARAM, SET_OP, String.format(SAVE_FUNC, funcvoid, param), "%g1");
        writeAssembly(THREE_PARAM, SAVE_OP, SP, "%g1", SP);
        writeAssembly(NL);

        increaseIndent();

        writeAssembly(TWO_PARAM, ST_OP, I0, "["+FP +"+68"+"]");
        newline();
        decreaseIndent();
        indent_level = 0;
    }

    public void writeFuncCall(STO newex, STO oldfunc){
        funcIndent();
        writeAssembly("! "+ newex.getName()+"(...)\n");
        String param = paramtypelist(oldfunc);
        call(newex.getName() + param);
        if(!((FuncSTO)oldfunc).getReturnType().isVoid()){
            decreaseOffset();
            newex.setSparcOffset(offset);
            if(newex.getType().isFloat()){
                setaddst(f0, iString(offset));
            }else{
                setaddst(O0, iString(offset));
            }
        }
        funcDedent();
    }

    public void writeFuncCallParam(STO sto, int count){
        newline();
        funcIndent();
        writeAssembly("! "+ sto.getName()+"\n");
        if(sto.isRef()){
            setadd(sto, count);
        }
        else if(sto.isConst()){
            if(sto.getType().isFloat()){
                writeConstFloat(sto);
                writeAssembly(TWO_PARAM, LD_OP, "["+ L7+"]", "%f"+iString(count));
            }
            writeAssembly(TWO_PARAM, SET_OP, iString(sto.getIntValue()), "%o"+iString(count));
        }
        else {
            setaddld("%o"+iString(count), iString(sto.getSparcOffset()));
        }
        funcDedent();
    }

    public String makeStructName(STO sto){
        String name = sto.getName();
        return name + "." + name;
    }

    public void writeCloseFunc(STO sto) {
        increaseIndent();
        String param = paramtypelist(sto);
        String name = sto.getName();
        int posoffset = Math.abs(getOffset());

        if(struct == true){
            if(structCtor == false) {
                name = name + ".$" + name;
            }
            else{
                name = name + "." + name;
            }
            posoffset = 0;
            struct = false;
        }


        call(String.format(FINI_FUNC, name, param));
        retRest();



        writeAssembly("%s %s\n", String.format(SAVE_FUNC, name, param), String.format(OFFSET_TOTAL, iString(posoffset)));

        decreaseIndent();
        indent_level = 0;
        writeAssembly(NL);
        writeAssembly(FINI_FUNC, name, param);
        writeAssembly(":\n");
        increaseIndent();
        int add = -92 + getOffset();
        writeAssembly(THREE_PARAM, SAVE_OP, SP, "-96", SP);
        retRest();

        func = false;
        newline();
    }

    //simple function decl
    //TODO: does not take care of parameters yet
    public void writeFunctionDecl_1(STO sto) {
        indent_level=0;
        offset = 0;
        func = true;
        infunc = sto.getName();
        String name = sto.getName();
        increaseIndent();
        //writeAssembly(SECTION, TEXT_SEC);
        writeAssembly(GLOBAL, name);
        decreaseIndent();
        writeAssembly(VARCOLON, name);

        //void if there is no parameter 
        String param = paramtypelist(sto);

        String funcvoid = name + param;
        writeAssembly(VARCOLON, funcvoid);

        increaseIndent();
        writeAssembly(TWO_PARAM, SET_OP, String.format(SAVE_FUNC, name, param), "%g1");
        writeAssembly(THREE_PARAM, SAVE_OP, SP, "%g1", SP);
        writeAssembly(NL);

        //void if there is no parameter
        if(sto.getParamVec() != null){
            Vector<STO> paramlist = sto.getParamVec();
            int count = 0;
            int valplace = 68;
            for(STO i : paramlist){
                if(i.getType().isFloat()){
                    writeAssembly(TWO_PARAM, ST_OP, "%f"+iString(count), "["+FP+"+"+iString(valplace)+"]");
                }
                else{
                    writeAssembly(TWO_PARAM, ST_OP, "%i"+iString(count), "["+FP+"+"+iString(valplace)+"]");

                }
                i.setSparcOffset(valplace);
                count++;
                valplace += 4;
            }
        }
        decreaseIndent();
        writeAssembly(NL);


        decreaseIndent();
        //decreaseIndent();
    }

    public void writeAssigntmentSto(STO sto) {

        if (sto.isGlobal() || sto.isStatic()) {
            writeAssembly(TWO_PARAM, SET_OP, sto.getName(), O1);
            writeAssembly(THREE_PARAM, ADD_OP, G0, O1, O1);
        }
        else if(sto.isStructVar()){
            writeAssembly(TWO_PARAM, SET_OP, iString(getOffset()), O1);
            writeAssembly(THREE_PARAM, ADD_OP, FP, O1, O1);
            writeAssembly(TWO_PARAM, LD_OP, "["+O1+"]" ,O1);
        }
        else {
            writeAssembly(TWO_PARAM, SET_OP, iString(sto.getSparcOffset()+offset), O1);
            writeAssembly(THREE_PARAM, ADD_OP, FP, O1, O1);
        }
    }

    public void writeAssignExprVariable(STO stoDes, STO expr) {
        int desoffset;
        String val = "";
        increaseIndent();
        floatreg = 0;

        if (func) {
            indent_level = 2;
        }
 
        String sName = stoDes.getName();
        String iName = expr.getName();
        //create space

        writeAssembly(String.format(var_comment, sName, iName));
        //writeAssembly("current offset in Assign: %s\n", iString(getOffset()));

        writeAssembly("! %s\n", iName);
        if (expr.isConst() && !(stoDes.getisArray())) {
            writeAssigntmentSto(stoDes);

            if (expr.getType().isFloat()) {  // if its not auto and float
                writeConstFloat(expr);
                writeAssembly(TWO_PARAM, ST_OP, f0, "[" + O1 + "]");
                decreaseIndent();
                writeAssembly(NL);
            } else {
                writeInit(stoDes, expr);
            }
        } else if ((desoffset = stoDes.getSparcOffset()) != 0) {

            val = stoValue(expr); // stoVal gets teh value of sto.
            writeAssembly(TWO_PARAM, SET_OP, iString(desoffset), O1);
            writeAssembly(THREE_PARAM, ADD_OP, FP, O1, O1);
            if(stoDes.getisArray()){
                writeAssembly(TWO_PARAM, LD_OP, "["+O1+"]", O1);
            }
            //xwriteAssembly("////////////////////\n");
            writeInit(stoDes, expr);

        } else {
            writeAssigntmentSto(stoDes);
            //set value
            writeInit(stoDes, expr);
        }
        decreaseIndent();
    }

    public int floatreg;

    //for binary expr
    public void writeBinaryExpr(STO a, Operator o, STO b, STO result) {
        boolean iffloat = false;
        //arithmetic = true;
        floatreg = 0;
        increaseIndent();
        writeAssembly(NL);
        funcIndent();


        writeAssembly("! (" + a.getName() + ")" + checkmod(o.getName()) + "(" + b.getName() + ")");

        newline();


        if(!func){
            return;
        }
    
        decreaseOffset();
        result.setSparcOffset(getOffset());
        //writeAssembly("current offset: %s\n", iString(getOffset()));

        //if it is boolean expr 
        if (a.getType().isBool() && b.getType().isBool()) {
            andorskipcnt++;
            if(o.getName() == "&&" || o.getName() == "||"){
                if(!b.isFunc()){
                    if (a.isConst()) {
                        writeAssembly(TWO_PARAM, SET_OP, iString(a.getIntValue()), O0);
                    } else {
                        writeCallStored(a, 0);
                    }
                    cmpbenop("andorSkip");
                    newline();
                }

                if (b.isConst()) {
                    writeAssembly(TWO_PARAM, SET_OP, iString(b.getIntValue()), O0);
                } else {
                    writeCallStored(b, 0);
                }
                cmpbenop("andorSkip");

                writeAssembly(ONE_PARAM, BA_OP, String.format(BASIC_FIN, "andorEnd", iString(andorskipcnt)));
                String tempstore = O0;
                int templast = 0;
                if (a.isConst() && b.isConst()) {
                    tempstore = G0;
                }

                if (o.getName() == "&&") {
                    writeAssembly(TWO_PARAM, MOV_OP, iString(1), tempstore);
                    decreaseIndent();
                    writeAssembly(BASIC_FIN_NL, "andorSkip", iString(andorskipcnt));
                    increaseIndent();
                    writeAssembly(TWO_PARAM, MOV_OP, iString(0), tempstore);
                    decreaseIndent();
                    writeAssembly(BASIC_FIN_NL, "andorEnd", iString(andorskipcnt));
                    increaseIndent();
                } else {
                    writeAssembly(TWO_PARAM, MOV_OP, iString(0), tempstore);
                    decreaseIndent();
                    writeAssembly(BASIC_FIN_NL, "andorSkip", iString(andorskipcnt));
                    increaseIndent();
                    templast = 1;
                    writeAssembly(TWO_PARAM, MOV_OP, iString(1), tempstore);
                    decreaseIndent();
                    writeAssembly(BASIC_FIN_NL, "andorEnd", iString(andorskipcnt));
                    increaseIndent();
                }

                if(a.isConst() && b.isConst()){
                    increaseOffset();
                    result.setSparcOffset(getOffset());
                }

                //it does not print out setaddst if both value is const
                if(!result.isConst()){
                    setaddst(O0, iString(result.getSparcOffset()));
                }
                newline();
                return;
            }else{
                //for cases such as == and !=
                if (a.isConst()) {
                    writeAssembly(TWO_PARAM, SET_OP, iString(a.getIntValue()), O0);
                } else {
                    writeCallStored(a, 0);
                }
                newline();

                if (b.isConst()) {
                    writeAssembly(TWO_PARAM, SET_OP, iString(b.getIntValue()), O1);
                } else {
                    writeCallStored(b, 1);
                }
            }
        } else {
            if(a.isConst() && b.isConst()){
                //increase offset again because it is not stored inside register
                increaseOffset();
                result.setSparcOffset(getOffset());
                return;
            }
            if (a.getType().isInt() && b.getType().isFloat()) { //if a is int and b is float

                writeCallStored(a, 0);
                convertToFloatBinary(a, 0);

                if (b.isConst()) {
                    writeConstFloat(b);
                }

                if (!b.isConst()) {
                    writeCallStored(b, 1);
                }

                iffloat = true;

            } else if (a.getType().isFloat() && b.getType().isInt()) {

                if (!a.isConst()) {
                    writeCallStored(a, 0);
                }
                writeCallStored(b, 1);

                if (a.isConst()) {
                    writeConstFloat(a);
                }
                convertToFloatBinary(b, 1);

                iffloat = true;

            } else if (a.getType().isFloat() && b.getType().isFloat()) { //if both is float

                if (a.isConst() && !b.isConst()) {
                    writeConstFloat(a);
                    writeCallStored(b, 1);
                }
                if (!a.isConst() && b.isConst()) {
                    writeCallStored(a, 0);
                    floatreg++;
                    writeConstFloat(b);
                } else if (!a.isConst() && !b.isConst()) {
                    writeCallStored(a, 0);
                    writeCallStored(b, 1);

                }
                iffloat = true;
            } else if(a.isConst() && b.isConst()){
                //it is for when a and b is const and o is comparison like > < 
                increaseOffset();
                result.setSparcOffset(getOffset());
            }
            else {
                writeCallStored(a, 0);
                writeCallStored(b, 1);
            }


        }            

        //System.err.println(a.getValue());
        //System.err.println(b.getValue());

        String register = iffloat ? f0 : O0;
        increaseIndent();
        writeInstructionCase(o, iffloat, register, iString(result.getSparcOffset()));
        
        
        funcDedent();
        decreaseIndent();
    }

    public void cmpbenop(String word) {
        writeAssembly(TWO_PARAM, CMP_OP, O0, G0);
        writeAssembly(ONE_PARAM, BE_OP, String.format(BASIC_FIN, word, iString(andorskipcnt)));
        writeAssembly(NOP_OP);
    }

    //for not expr
    public void writeNotExpr(STO a, Operator o, STO result) {
        writeAssembly(var_comment_op, o.getName(), a.getName());

        increaseIndent();
        funcIndent();
        andorskipcnt++;
        if (!a.isConst()) {
            writeCallStored(a, 0);
            writeAssembly(THREE_PARAM, XOR_OP, O0, "1", O0);

            decreaseOffset();
            result.setSparcOffset(getOffset());
        } 
            /*setaddst(O0, iString(result.getSparcOffset()));
            newline();

            setaddld(O0, iString(result.getSparcOffset()));*/

        /*writeAssembly(TWO_PARAM, CMP_OP, O0, G0);
        writeAssembly(ONE_PARAM, BE_OP, String.format(BASIC_FIN, "else", iString(andorskipcnt)));
        writeAssembly(NOP_OP);*/
    }

    //for incdecrxpr
    public void writeIncDecExpr(STO a, Operator o, STO result) {
        boolean iffloat = false;
        //arithmetic = true;
        writeAssembly(var_comment_op, o.getName(), a.getName());

        increaseIndent();
        writeAssembly(NL);
        funcIndent();

        writeCallStored(a, 0);
        decreaseOffset();
        result.setSparcOffset(getOffset());

        if (a.getType().isFloat()) {
            writeIncDecFloat(1);
            iffloat = true;
        } else {
            writeAssembly(TWO_PARAM, SET_OP, iString(1), O1);
        }
        String register = iffloat ? f0 : O0;
        if(a.getPrePost() == "pre"){
            if(register.equals(f0)){
                register = F2;
            }
            else {
                register = O2;
            }
        }

        increaseIndent();
        writeInstructionCase(o, iffloat, register, iString(result.getSparcOffset()));

        funcIndent();
        setAddStore(a);
        funcDedent();

        funcDedent();
        decreaseIndent();
    }

    //used to convert on to float during incdec for float
    public void writeIncDecFloat(int init) {
        String name = FLOAT_COUNTER;

        int counter = ++floatcounter;


        writeAssembly(NL);
        sectionAlign(RODATA_SEC, "4");

        indent_level = 1;
        writeAssembly(name, iString(counter));
        indent_level = 2;


        writeAssembly(FLOAT, "1.0");
        writeAssembly(NL);
        sectionAlign(TEXT_SEC, "4");
        writeAssembly(TWO_PARAM, SET_OP, ".$$.float." + iString(floatcounter), L7);
        writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", F1);
    }

    public void writeInstructionCase(Operator o, boolean iffloat, String register, String resoffset) {
        String opname = o.getName();

        if (!iffloat) {
            switch (opname) {
                case "+":

                    writeArithmetic(ADD_OP, register, resoffset, O0, O1, O0);
                    break;
                case "-":
                    writeArithmetic(SUB_OP, register, resoffset, O0, O1, O0);
                    break;
                case "*":
                    funcIndent();
                    call(MUL_OP);
                    writeAssembly(TWO_PARAM, MOV_OP, O0, O0);
                    setaddst(register, resoffset);
                    funcDedent();
                    //writeArithmetic(MUL_OP, register, resoffset, O0, O1, O0);
                    break;
                case "/":
                    funcIndent();
                    call(DIV_OP);
                    writeAssembly(TWO_PARAM, MOV_OP, O0, O0);
                    setaddst(register, resoffset);
                    funcDedent();
                    //writeArithmetic(DIV_OP, register, resoffset, O0, O1, O0);
                    break;
                case "%":
                    funcIndent();
                    call(MOD_OP);
                    writeAssembly(TWO_PARAM, MOV_OP, O0, O0);
                    setaddst(register, resoffset);
                    funcDedent();
                    break;
                case "^":
                    writeArithmetic(XOR_OP, register, resoffset, O0, O1, O0);
                    break;
                case "|":
                    writeArithmetic(BW_OR_OP, register, resoffset, O0, O1, O0);
                    break;
                case "&":
                    writeArithmetic(BW_AND_OP, register, resoffset, O0, O1, O0);
                    break;
                case "++":
                    writeArithmetic(ADD_OP, register, resoffset, O0, O1, O2);
                    break;
                case "--":
                    writeArithmetic(SUB_OP, register, resoffset, O0, O1, O2);
                    break;
                case ">":
                    writeComparison(BLE_OP, register, resoffset, O0, O1);
                    break;
                case ">=":
                    writeComparison(BL_OP, register, resoffset, O0, O1);
                    break;
                case "<":
                    writeComparison(BGE_OP, register, resoffset, O0, O1);
                    break;
                case "<=":
                    writeComparison(BG_OP, register, resoffset, O0, O1);
                    break;
                case "==":
                    writeComparison(BNE_OP, register, resoffset, O0, O1);
                    break;
                case "!=":
                    writeComparison(BE_OP, register, resoffset, O0, O1);
                    break;
            }
        } else {
            switch (opname) {
                case "+":
                    writeArithmetic(FADD_OP, register, resoffset, f0, F1, f0);
                    break;
                case "-":
                    writeArithmetic(FSUB_OP, register, resoffset, f0, F1, f0);
                    break;
                case "*":
                    writeArithmetic(FMUL_OP, register, resoffset, f0, F1, f0);
                    break;
                case "/":
                    writeArithmetic(FDIV_OP, register, resoffset, f0, F1, f0);
                    break;
                case "++":
                    writeArithmetic(FADD_OP, register, resoffset, f0, F1, F2);
                    break;
                case "--":
                    writeArithmetic(FSUB_OP, register, resoffset, f0, F1, F2);
                    break;
                case ">":
                    writeComparison(FBLE_OP, register, resoffset, f0, F1);
                    break;
                case ">=":
                    writeComparison(FBL_OP, register, resoffset, f0, F1);
                    break;
                case "<":
                    writeComparison(FBGE_OP, register, resoffset, f0, F1);
                    break;
                case "<=":
                    writeComparison(FBG_OP, register, resoffset, f0, F1);
                    break;
                case "==":
                    writeComparison(FBNE_OP, register, resoffset, f0, F1);
                    break;
                case "!=":
                    writeComparison(FBE_OP, register, resoffset, f0, F1);
                    break;
            }
        }
    }

    public void writeArithmetic(String opname, String register, String resoffset, String reg1, String reg2, String reg3) {
        funcIndent();
        writeAssembly(THREE_PARAM, opname, reg1, reg2, reg3);

        setaddst(register, resoffset);
        funcDedent();
    }

    public void writeComparison(String opname, String register, String resoffset, String reg1, String reg2) {
        funcIndent();
        newline();
        cmpcounter++;
        andorskipcnt++;
        if (reg1 == f0) {
            writeAssembly(TWO_PARAM, FCMP_OP, reg1, reg2);
            writeAssembly(NOP_OP);
        } else {
            writeAssembly(TWO_PARAM, CMP_OP, reg1, reg2);
        }
        //writeAssembly("///////////////////");
        writeAssembly(ONE_PARAM, opname, String.format(CMP_COUNTER, iString(cmpcounter)));

        writeAssembly(TWO_PARAM, MOV_OP, G0, O0);
        writeAssembly(ONE_PARAM, INC_OP, O0);

        decreaseIndent();
        writeAssembly(VARCOLON, String.format(CMP_COUNTER, iString(cmpcounter)));

        increaseIndent();

        setaddst(O0, resoffset);
        newline();
        funcDedent();

    }

    public void writeLoopStart(String loopname){
        loopcounter++;
        funcIndent();
        decreaseIndent();
        writeAssembly(BASIC_FIN_NL, "loopCheck", iString(loopcounter));

        funcDedent();
    }

    public void writeWhileCase(STO sto){
        sto.loopcounter = loopcounter;
        ploopcounter = sto.loopcounter;
        funcIndent();
        writeAssembly("! Check Loop Condition\n");
        if(sto.getType().isBool()){
            if(sto.isConst()){
                writeAssembly(TWO_PARAM, SET_OP, iString(sto.getIntValue()), O0);
            }
            else{
                newline();
                //setaddld(O0, iString(sto.getSparcOffset()));
                writeCallStored(sto, 0);
            }
        }else{
            setaddld(O0, sto.getName()); // might need to fix //not sure its for which case?
        }

        writeAssembly(TWO_PARAM, CMP_OP, O0, G0);  // might need to fix
        writeAssembly(ONE_PARAM, "be  \t", String.format(BASIC_FIN, "loopEnd", iString(sto.loopcounter)));
        writeAssembly(NOP_OP);
        newline();
        funcDedent();
    }

    public void writeWhileClose(STO sto){
        newline();
        funcIndent();
        increaseIndent();
        writeAssembly(ONE_PARAM, BA_OP, String.format(BASIC_FIN, "loopCheck", iString(sto.loopcounter)));
        writeAssembly(NOP_OP);
        newline();
        funcDedent();
        increaseIndent();
        writeAssembly("\t! else\n");
        writeAssembly(BASIC_FIN_NL, "loopEnd", iString(sto.loopcounter));
        newline();

        ploopcounter--;
        decreaseIndent();
        newline();
    }

    public void writeIfCase(STO sto) {
        ifcounter++;
        sto.ifcounter = ifcounter;
        funcIndent();
        writeAssembly("! if condition\n");
        // might need to fix
        // fix for bool: need to print offset if it is bool comparison
        if(sto.getType().isBool()){
            if(sto.isConst()){
                writeAssembly(TWO_PARAM, SET_OP, iString(sto.getIntValue()), O0);
            }
            else{
                newline();
                //setaddld(O0, iString(sto.getSparcOffset()));
                writeCallStored(sto, 0);
            }
        }else{
            setaddld(O0, sto.getName()); // might need to fix //not sure its for which case?
        }

        writeAssembly(TWO_PARAM, CMP_OP, O0, G0);  // might need to fix
        writeAssembly(ONE_PARAM, "be  \t", String.format(BASIC_FIN, "else", iString(ifcounter)));
        writeAssembly(NOP_OP);
        newline();
        funcDedent();
    }

    public void writeBeforeElse(STO sto){
        newline();
        funcIndent();
        increaseIndent();
        writeAssembly(ONE_PARAM, BA_OP, String.format(BASIC_FIN, "endif", iString(sto.ifcounter)));
        writeAssembly(NOP_OP);
        newline();
        funcDedent();
        increaseIndent();
        writeAssembly("\t! else\n");
        writeAssembly(BASIC_FIN_NL, "else", iString(sto.ifcounter));
        newline();

        
        decreaseIndent();
        newline();
    }
    public void writeIfClose(STO sto) {
        newline();
        funcIndent();
        decreaseIndent();
        writeAssembly("\t! endif\n");
        writeAssembly(BASIC_FIN_NL, "endif", iString(sto.ifcounter));
        funcDedent();
        increaseIndent();
        newline();

    }


    //used in cout
    public void setAddLoad(STO init) {
        String global = init.getSparcBase() == "%g0" ? init.getName() : iString(init.getSparcOffset());
        String globalreg = init.getSparcBase() == "%g0" ? G0 : FP;
        String register = init.getType().isFloat() ? f0 : O1; // check for float f0 or o0
        String off = init.isGlobal() ? init.getName() : iString(init.getSparcOffset());


        String load = init.isStructdef() ? O1 : L7;
        if (init.getType().isBool()) {
            register = O0;
        }
        if (init.isStructVar()){
            off = iString(getOffset());
        }

        writeAssembly(TWO_PARAM, SET_OP, off, load);
        writeAssembly(THREE_PARAM, ADD_OP, globalreg, load, load);
        if(init.isStructVar()){
            writeAssembly(TWO_PARAM, LD_OP, "[" + load + "]", load);

        }
        writeAssembly(TWO_PARAM, LD_OP, "[" + load + "]", register);
    }

    public void setaddld(String register, String resoffset) {
        writeAssembly(TWO_PARAM, SET_OP, resoffset, L7);
        writeAssembly(THREE_PARAM, ADD_OP, FP, L7, L7);
        writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", register);
    }

    public void setAddStore(STO init) {
    String global = init.getSparcBase() == "%g0" ? init.getName() : iString(init.getSparcOffset());
    String globalreg = init.getSparcBase() == "%g0" ? G0 : FP;
    String register = init.getType().isFloat() ? F2 : O2; // check for float f0 or o0
    if (init.getType().isBool()) {
        register = O0;
    }
    String off = init.isGlobal() ? init.getName() : iString(init.getSparcOffset());

    writeAssembly(TWO_PARAM, SET_OP, off, O1);
    writeAssembly(THREE_PARAM, ADD_OP, globalreg, O1, O1);
    writeAssembly(TWO_PARAM, ST_OP, register, "[" + O1 + "]");
    }

    public void setaddst(String register, String resoffset) {
        writeAssembly(TWO_PARAM, SET_OP, resoffset, O1);
        writeAssembly(THREE_PARAM, ADD_OP, FP, O1, O1);
        writeAssembly(TWO_PARAM, ST_OP, register, "[" + O1 + "]");
    }

    //simple function used in designator_dot 3 funccall when made
    public void setadd(STO sto, int count){
        String off = sto.isGlobal() ? sto.getName() : iString(sto.getSparcOffset());
        String globalreg = sto.getSparcBase() == "%g0" ? G0 : FP;
        String valplace =  "%o"+iString(count);


        writeAssembly(TWO_PARAM, SET_OP, off, valplace);
        writeAssembly(THREE_PARAM, ADD_OP, globalreg, valplace, valplace);
        newline();
    }

    //setaddload
    public void writeCallStored(STO init, int x) {
        String val = stoValue(init); // stoVal gets teh value of sto.
        String register = init.getType().isFloat() ? "isfl" : "isint"; // check for float f0 or o0
        String global = init.getSparcBase() == "%g0" ? init.getName() : iString(init.getSparcOffset());
        String globalreg = init.getSparcBase() == "%g0" ? G0 : FP;

        if (x == 0) {
            if (register.equals("isfl")) {
                register = f0;
            } else {
                register = O0;
            }
        }
        if (x == 1) {
            if (register.equals("isfl")) {
                register = F1;
            } else {
                register = O1;
            }
        }
        if (init.isConst()) {
            if (init.isGlobal() || init.isStatic()) {
                writeAssembly(TWO_PARAM, SET_OP, global, L7);
                writeAssembly(THREE_PARAM, ADD_OP, globalreg, L7, L7);
                writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", register);
            } else {
                writeAssembly(TWO_PARAM, SET_OP, val, register);
            }
        }else if(init.getisArray()){
            writeAssembly(TWO_PARAM, SET_OP, global, L7);
            writeAssembly(THREE_PARAM, ADD_OP, globalreg, L7, L7);
            writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", L7);
            writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", register);
        } 
        else {
            writeAssembly(TWO_PARAM, SET_OP, global, L7);
            writeAssembly(THREE_PARAM, ADD_OP, globalreg, L7, L7);
            writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", register);
        }
    }

    //used when initializing values e.g x = 2
    public void writeInit(STO sto, STO init) {
        String val = stoValue(init); // stoVal gets teh value of sto.
        String register = init.getType().isFloat() ? f0 : O0; // check for float f0 or o0
        String global = init.getSparcBase() == "%g0" ? init.getName() : iString(init.getSparcOffset());
        String globalreg = init.getSparcBase() == "%g0" ? G0 : FP;

        if (init.isConst()) {
            if (sto.getType().isFloat() && !(sto.getisArray())) {
                writeAssembly(TWO_PARAM, SET_OP, val, O0);
                convertToFloat(sto, init, O0);
                writeAssembly(TWO_PARAM, ST_OP, f0, "[" + O1 + "]");
                //decreaseIndent();
                writeAssembly(NL);
                return;
            }
            if(sto.getisArray() && init.getType().isFloat()){
                writeConstFloat(init);
                //writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", f0);
                //writeAssembly(TWO_PARAM, ST_OP, f0, "[" + O1 + "]");
            }else if(sto.getisArray() && init.getType().isInt() && sto.getType().isFloat()){
                convertToFloat(sto, init, O0);
                register = f0;
            }else{
                //its here because it is not needed for array 
                writeAssembly(TWO_PARAM, SET_OP, val, O0);
            }
        } else {
            writeAssembly(TWO_PARAM, SET_OP, global, L7);
            writeAssembly(THREE_PARAM, ADD_OP, globalreg, L7, L7);
            writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", register);
            if (sto.getType().isFloat() && init.getType().isInt()) {
                convertToFloat(sto, init, O0);
                writeAssembly(TWO_PARAM, ST_OP, f0, "[" + O1 + "]");
                //decreaseIndent();
                writeAssembly(NL);
                return;
            }
        }
        writeAssembly(TWO_PARAM, ST_OP, register, "[" + O1 + "]");
        decreaseIndent();
        writeAssembly(NL);
    }


    //convert int to float when needed
    //made a function similar to convertofloat but used in binary.
    public void convertToFloatBinary(STO init, int x) {
        String global = init.getSparcBase() == "%g0" ? init.getName() : iString(init.getSparcOffset());
        String globalreg = init.getSparcBase() == "%g0" ? G0 : FP;
        String register = ""; // check for float f0 or o0
        String order = "";
        if (x == 0) {
            register = f0;
            order = O0;
        }
        if (x == 1) {
            register = F1;
            floatreg = 1;
            order = O1;
        }

        decreaseOffset();
        writeAssembly(TWO_PARAM, SET_OP, iString(getOffset()), L7);
        writeAssembly(THREE_PARAM, ADD_OP, FP, L7, L7);
        writeAssembly(TWO_PARAM, ST_OP, order, "[" + L7 + "]");
        writefloatreg(floatreg++);
        writeAssembly(TWO_PARAM, FITOS, register, register);

    }


    //convert int to float when needed
    public void convertToFloat(STO sto, STO init, String storeval) {
        String global = init.getSparcBase() == "%g0" ? init.getName() : iString(init.getSparcOffset());
        String globalreg = init.getSparcBase() == "%g0" ? G0 : FP;
        String register = init.getType().isFloat() ? f0 : O0; // check for float f0 or o0

        int newoffset = sto.isGlobal() ? -4 : offset;
        decreaseOffset();
        writeAssembly(TWO_PARAM, SET_OP, iString(getOffset()), L7);
        writeAssembly(THREE_PARAM, ADD_OP, FP, L7, L7);
        writeAssembly(TWO_PARAM, ST_OP, storeval, "[" + L7 + "]");
        writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", f0);
        writeAssembly(TWO_PARAM, FITOS, f0, f0);

        //writeAssembly(TWO_PARAM, ST_OP, f0, "[" + O1 + "]");
    }

    public void writeConstFloat(STO init) {
        String name = FLOAT_COUNTER;
        int counter = ++floatcounter;
        boolean str = false;
        String size = init.getType() == null ? "4" : iString(init.getType().getSize());
        if (init.getType() == null) {
            funcIndent();
            name = ".$$.str." + iString(++strFmtCnt) + ":\n";
            counter = strFmtCnt;
            str = true;
        }

        sectionAlign(RODATA_SEC, size);

        indent_level = 1;
        writeAssembly(name, iString(counter));
        indent_level = 2;

        if (str) {
            writeAssembly(ONE_PARAM, ASCIZ, "\"" + init.getName() + "\"");
            writeAssembly(NL);
            sectionAlign(TEXT_SEC, "4");
            writeAssembly(NL);
            return;
        }
        writeAssembly(FLOAT, stoValue(init));
        sectionAlign(TEXT_SEC, size);
        writeAssembly(TWO_PARAM, SET_OP, ".$$.float." + iString(floatcounter), L7);
        writefloatreg(floatreg++);
    }

    public void writefloatreg(int regnum) {

        String reg = "";
        switch (iString(regnum)) {

            case "0":
                reg = f0;
                break;
            case "1":
                reg = F1;
                break;
            case "2":
                reg = F2;
                break;
            case "3":
                reg = F3;
        }

        writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", reg);
    }

    public void writeReturn(STO init, STO func) {
        String val = stoValue(init); // stoVal gets teh value of sto.
        String register = init.getType().isFloat() ? L7 : I0; // check for float f0 or o0
        String global = init.getSparcBase() == "%g0" ? init.getName() : iString(init.getSparcOffset());
        String globalreg = init.getSparcBase() == "%g0" ? G0 : FP;

        funcIndent();
        writeAssembly(NL);
        writeAssembly("! return " + val + ";\n");
        if (init.isConst()) {
            if (init.getType().isFloat()) {
                writeConstFloat(init);
            } else {
                writeAssembly(TWO_PARAM, SET_OP, val, register);
            }
        } else {
            writeAssembly(TWO_PARAM, SET_OP, global, L7);
            writeAssembly(THREE_PARAM, ADD_OP, FP, L7, L7);
            writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", register);
        }
        //its used when init is constant int and return type is float 
        if(((FuncSTO)func).getReturnType().isFloat()){
            if(init.isConst()){
                convertToFloat(func, init, I0);
            }else{
                writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", f0);
            }

        }
        String param = paramtypelist(func);
        call(String.format(FINI_FUNC, func.getName(), param));
        retRest();
        funcDedent();
        newline();
    }

    public void writeArrayDeclLocal(STO stoDes, STO expr, STO sto){

        int size = sto.getType().getSize();
        int tempsize = size*4;
        funcIndent();
        writeAssembly("! %s[%s]\n", stoDes.getName(), expr.getName());
        if(expr.isConst()){
            //System.out.print("int inside val: "+iString(expr.getIntValue()));
            writeAssembly(TWO_PARAM, SET_OP, iString(expr.getIntValue()), O0);
        }else {
            //writeAssembly(TWO_PARAM, SET_OP, iString(expr.getIntValue()), O0);
            writeCallStored(expr, 0);
        }
        writeAssembly(TWO_PARAM, SET_OP, iString(size), O1);
        call(arrCheckCall);
        writeAssembly(TWO_PARAM, SET_OP, "4", O1);
        call(MUL_OP);
        writeAssembly(TWO_PARAM, MOV_OP, O0, O1);

        writeAssembly(TWO_PARAM, SET_OP, iString(sto.getSparcOffset()), O0);
        writeAssembly(THREE_PARAM, ADD_OP, FP, O0, O0);
        call(ptrCheckCall);
        writeAssembly(THREE_PARAM, ADD_OP, O0, O1, O0);
        decreaseOffset();
        stoDes.setSparcOffset(getOffset());
        setaddst(O0, iString(stoDes.getSparcOffset()));
        newline();
        funcDedent();
    
        
    }
    public void writeArrayDeclGlobal(STO sto, Vector<STO> array, Type temp){
        int size = temp.getSize();
        int tempsize = size*4;
        if (sto.isStatic() || sto.isGlobal()) {
            increaseIndent();
            sectionAlign(BSS_SEC, "4");
            writeAssembly(GLOBAL, sto.getName());
            decreaseIndent();
            writeAssembly(VARCOLON, sto.getName());
            increaseIndent();
   
            writeAssembly(SKIP, iString(tempsize));
            sectionAlign(TEXT_SEC, "4");
            decreaseIndent();

        }
        this.offset -= tempsize;
        sto.setSparcOffset(getOffset());
    }

    //TODO: take care of when init not there too
    public void writeLocalVariable(STO sto, STO init) {
        funcIndent();
        String sectioncheck;
        String register = "";
        String val = ""; //later used for init if init is null to handle null pointer
        sto.setSparcBase("%fp");
        decreaseOffset();
        sto.setSparcOffset(getOffset());


        if (func) {
            indent_level = 2;
        }

        if ((init != null)) {     //check if init is not null store the value

            val = stoValue(init); // stoVal gets tehq value of sto.
            String sName = sto.getName();
            String iName = init.getName();
            //create space
            writeAssembly(String.format(var_comment, sName, iName));
//            writeAssembly(TWO_PARAM, SET_OP, iString(offset), O1);
//            writeAssembly(THREE_PARAM, ADD_OP, FP, O1, O1);
            writeAssigntmentSto(sto);

            if (init.isConst()) {
                if (init.getType().isFloat()) {  // if its not auto and float
                    floatreg = 0;
                    writeConstFloat(init);
                    writeAssembly(TWO_PARAM, ST_OP, f0, "[" + O1 + "]");
                    decreaseIndent();
                    writeAssembly(NL);
                } else {
                    writeInit(sto, init);
                }
            } else {
                //set value
                writeInit(sto, init);
            }
        }
        funcDedent();
    }


    //writes assembly for variables that is glocal or static
    public void writeGlobalStaticVariable(STO sto, STO init) {
        increaseIndent();
        indent_level = 1;
        if (func) {
            indent_level = 2;
        }
        String sectioncheck;
        Type stotype = sto.getType();
        int size = stotype.getSize();
        String val = "";
        boolean auto = false;
        sto.setSparcBase("%g0");

        String name = sto.getName();
        if(sto.isStructdef()){

        }


        if ((init == null) || (auto = sto.getAuto())) {
            sectioncheck = BSS_SEC;
        } else {
            sectioncheck = sto.getAuto() ? BSS_SEC : DATA_SEC;

            val = stoValue(init);   // stoValue gets the value of the sto
            //any global variable not initialized when declared is set to value 0
            if (sto.isGlobal()) {
                if (init.isStatic()) {
                    sectioncheck = BSS_SEC;
                } else if (!init.isConst()) {
                    size = 0;
                    val = iString(size);
                }
            }
        }

        sectionAlign(sectioncheck, iString(stotype.getSize()));

        if (!sto.isStatic()) { // global
            writeAssembly(GLOBAL, name);
        }
        //take care of the name change if static is inside function
        if (sto.isStatic()) {
            if (infunc != null) {
                name = infunc + ".void." + sto.getName();
            }
            //if sto is static and is not global get the size of the type and write as word even tho it is bss
            if (!sto.isGlobal()) {
                val = iString(stotype.getSize());
            }
        }

        decreaseIndent();
        writeAssembly(VARCOLON, name);
        increaseIndent();

        if ((sectioncheck == BSS_SEC)) {
            writeAssembly(SKIP, iString(size));
        } else {
            if (init != null && (init.getType().isFloat() || sto.getType().isFloat())) {
                init.setType(new FloatType());
                writeAssembly(FLOAT, stoValue(init));
            } else if (init != null) {
                writeAssembly(WORD, stoValue(init));
            }
        }

        if (auto) {       // if its auto do auto and return
            sectionAlign(TEXT_SEC, iString(stotype.getSize()));
            writeGlobalAuto(sto, init);
        } else {
            if (init != null && sto.isGlobal()) {
                if (sto.getType().isFloat() && init.getType().isInt()) {
                    //sectionAlign(TEXT_SEC, iString(stotype.getSize()));
                    //writeGlobalAuto(sto, init);

                } else if (init.isStatic()) {
                    sectionAlign(TEXT_SEC, iString(stotype.getSize()));
                    writeGlobalAuto(sto, init);

                }
            }
        }
        sectionAlign(TEXT_SEC, iString(stotype.getSize())); // SECTION and ALIGN
        decreaseIndent();
    }

    public void writeMarkUnary(String unary, STO init, STO des){
        funcIndent();
        //if(!a.isConst()){
        //writeAssembly(var_comment, init.getName(), unary+des.getName());
        writeAssembly("! unary %s \n", unary+init.getName());
        decreaseOffset();
        des.setSparcOffset(getOffset());
        writeCallStored(init, 0);
        //writeAssembly("! unary type %s \n", init.getType().getName());
        if(unary == "-"){
            if(des.getType().isFloat()){
                writeAssembly(TWO_PARAM, "fnegs\t", f0, f0);
                setaddst(f0, iString(des.getSparcOffset()));

            }else {
                writeAssembly(TWO_PARAM, NEG_OP, O0, O0);
                setaddst(O0, iString(des.getSparcOffset()));
            }
        } else{
            if(des.getType().isFloat()){
                writeAssembly(TWO_PARAM, FMOV_OP, f0, f0);
                setaddst(f0, iString(des.getSparcOffset()));
            }
            else{
                writeAssembly(TWO_PARAM, MOV_OP, O0, O0);
                setaddst(O0, iString(des.getSparcOffset()));
            }

        }
        //}
        funcDedent();
    }
    public void writeBreakOrCon(String loopname, int size){
        funcIndent();
        if(loopname.equals("break")){
            writeAssembly(ONE_PARAM, BA_OP, String.format(BASIC_FIN, "loopEnd", iString(ploopcounter)));
        }else{
            writeAssembly(ONE_PARAM, BA_OP, String.format(BASIC_FIN, "loopCheck", iString(ploopcounter)));
        }
        writeAssembly(NOP_OP);
        //loopcounter--;
        funcDedent();
    }
    public String paramtypelist(STO sto){
        String param = "";
        //void if there is no parameter
        if(sto.getParamVec() == null){
            param = ".void";
        }
        else if(sto.getParamVec().size() != 0){
            Vector<STO> paramlist = sto.getParamVec();
            for(STO i : paramlist){
                param += ".";
                param += i.getType().getName();
            }       
        }
        else{
            param = ".void";
        }
        return param;
    }
    public void call(String name) {
        writeAssembly(ONE_PARAM, CALL_OP, name);
        writeAssembly(NOP_OP);
    }

    public void sectionAlign(String section, String align) {
        writeAssembly(NL);
        writeAssembly(SECTION, section);
        writeAssembly(ALIGN, align);
    }

    public void newline() {
        writeAssembly(NL);
    }


    public void retRest() {
        writeAssembly(RET_OP);
        writeAssembly(RESTORE_OP);
    }

    //get int value and form it as a string
    public String iString(int val) {
        return Integer.toString(val);
    }

    public void writeCout(STO sto) {

        Vector<String> st = prepSTO(sto);

        funcIndent();
        writeAssembly(NL);

        if (sto.getType() != null) {
            writeAssembly("! cout << " + sto.getName() + "\n");
            if (sto.isConst()) {
                if(sto.getType().isFloat()){
                    writeConstFloat(sto);
                }else {
                    writeAssembly(TWO_PARAM, SET_OP, stoValue(sto), O1);
                }
                callCout(sto);
                return;
            }
            if(sto.getisArray()){
                writeCallStored(sto, 0);
            }else{
                setAddLoad(sto);
            }
        } else if (sto.getType() == null) {
            writeConstFloat(sto);
            String mod = checkmod(sto.getName());
            writeAssembly("! cout << "+mod);
            newline();
            writeAssembly(TWO_PARAM, SET_OP, strFmt, O0);
            writeAssembly(TWO_PARAM, SET_OP, ".$$.str." + iString(strFmtCnt), O1);
            call("printf");
            return;
        }

        callCout(sto);

        funcDedent();
    }

    public String checkmod(String sto){
        if(sto.contains("%")){
            return sto.replace("%", "%%");
        }
        return sto;
    }
    public void callCout(STO sto) {


        String stype = sto.getType().getName();
        String ret = "";
        switch (stype) {
            case "int":
                ret = intFmt;
                break;
            case "bool":
                ret = printBool;
                break;
        }

        if (stype == "float") {
            call("printFloat");
            return;
        } else if (stype == "bool") {
            call(".$$.printBool");
        } else {
            writeAssembly(TWO_PARAM, SET_OP, ret, O0);
            call("printf");
            return;
        }
    }

    public void writeCin(STO sto){
        Type stype = sto.getType();
        String register = sto.getType().isFloat() ? f0 : O1; // check for float f0 or o0


        newline();
        writeAssembly("! cin >> "+sto.getName()+"\n");
        if(stype.isInt()){
            call("inputInt");
        }
        else if(stype.isFloat()){
            call("inputFloat");
        }
        setaddst(register, iString(sto.getSparcOffset()));

    }

    public int oreg;

    public void writeCoutClose() {
        oreg = 0;
        funcIndent();
        writeAssembly(NL);
        writeAssembly("! cout << endl\n");
        writeAssembly(TWO_PARAM, SET_OP, strEndl, O0);
        call("printf");
        funcDedent();
    }


    public void writeExitExpr(STO init) {
        funcIndent();

        //it needs to go to set if const and if not to else but somehow isConst not work here
        //need to fix later 
        if(init.isConst()){
            writeAssembly(TWO_PARAM, SET_OP, init.getName(), O0);
        }else {
            setaddld(O0, iString(init.getSparcOffset()));
        }
        writeAssembly(ONE_PARAM, CALL_OP, "exit");
        writeAssembly(NOP_OP);
        newline();
        funcDedent();
    }


    public Vector<String> prepSTO(STO sto) {
        String name = sto.getName();
        String base = sto.getSparcBase();
        String off = iString(sto.getSparcOffset());

        Vector<String> ret = new Vector<>();
        ret.add(name);
        ret.add(base);
        ret.add(off);
        return ret;
    }


    private static final String endFunc_cmt = "! End of function .$.init.%s\n";

    public void writeGlobalAuto(STO sto, STO init) {
        String sName = sto.getName();
        String iName = init.getName();
        String save = "SAVE..$.init." + sName;
        String register = "";

        if (func) {
            indent_level = 2;
        }

        decreaseIndent();
        writeAssembly(GL_AUTO_INIT, sName + ":");
        newline();
        increaseIndent();
        writeAssembly(TWO_PARAM, SET_OP, save, G1);
        writeAssembly(THREE_PARAM, SAVE_OP, SP, G1, SP);

        writeAssembly(NL);
        increaseIndent();
        if(sto.isStructdef()){
            writeBasicStruct(sto);
            decreaseIndent();
        }
        else{
            writeAssembly(String.format(var_comment, sName, iName));
            writeAssembly(TWO_PARAM, SET_OP, sName, O1);
            writeAssembly(THREE_PARAM, ADD_OP, G0, O1, O1);
            writeInit(sto, init); // do the init registers
            writeAssembly(NL);
        }
        writeAssembly(String.format(endFunc_cmt, sName));
        call(String.format(GL_AUTO_INIT, sName + ".fini"));
        retRest();

        writeAssembly(save + String.format(OFFSET_TOTAL, "0"));
        writeAssembly(NL);
        writeAssembly(NL);

        decreaseIndent();
        writeAssembly(GL_AUTO_INIT, sName + ".fini:");
        newline();
        increaseIndent();
        writeAssembly(THREE_PARAM, SAVE_OP, SP, "-96", SP); // might need to change offset
        retRest();

        sectionAlign(INIT_SEC, iString(4));
        call(String.format(GL_AUTO_INIT, sName));
    }

    public void writeAuto(){

    }

    public void funcIndent() {
        if (func) {
            indent_level = 2;
        }
    }

    public void funcDedent() {
        if (func) {
            indent_level = 0;
        }
    }

    // 8
    public void decreaseIndent() {
        indent_level--;
    }

    public void dispose() {
        try {
            fileWriter.close();
        } catch (IOException e) {
            System.err.println(ERROR_IO_CLOSE);
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void increaseIndent() {
        indent_level++;
    }


    // 9
    public void writeAssembly(String template, String... params) {
        StringBuilder asStmt = new StringBuilder();

        // 10
        for (int i = 0; i < indent_level; i++) {
            asStmt.append(SEPARATOR);
        }

        // 11
        asStmt.append(String.format(template, (Object[]) params));

        try {

            fileWriter.write(asStmt.toString());
        } catch (IOException e) {
            System.err.println(ERROR_IO_WRITE);
            e.printStackTrace();
        }
    }

    //get the value of the sto when sto is constant
    public String stoValue(STO sto) {
        String ret = "";
        if (!sto.isConst()) {
            return "0";
        } else {
            Type stype = sto.getType();
            if (stype.isInt()) {
                ret = Integer.toString(sto.getIntValue());
            }
            if (stype.isFloat()) {
                ret = Float.toString(sto.getFloatValue());
            }

            if (stype.isBool()) {
                ret = Integer.toString((sto.getBoolValue()) ? 1 : 0);
            }
            return ret;
        }

    }


    public void fmtHeader() {
        increaseIndent();
        sectionAlign(RODATA_SEC, "4");
        fmt(intFmt + ":\n", d);
        fmt(strFmt + ":\n", s);
        fmt(strTF + ":\n", tf);
        fmt(strEndl + ":\n", nl);
        fmt(strArrBound + ":\n", arrbound);
        fmt(strNullPtr + ":\n", nullptr);
        sectionAlign(TEXT_SEC, "4");

        decreaseIndent();

        writeAssembly(".$$.printBool:\n");
        increaseIndent();
        writeAssembly(THREE_PARAM, SAVE_OP, SP, "-96", SP);
        writeAssembly(TWO_PARAM, SET_OP, ".$$.strTF", O0);
        writeAssembly(TWO_PARAM, CMP_OP, G0, I0);
        writeAssembly(ONE_PARAM, BE_OP, ".$$.printBool2");
        writeAssembly(NOP_OP);
        writeAssembly(THREE_PARAM, ADD_OP, O0, "8", O0);
        decreaseIndent();

        writeAssembly(".$$.printBool2:\n");
        increaseIndent();
        writeAssembly(ONE_PARAM, CALL_OP, "printf");
        writeAssembly(NOP_OP);
        retRest();
        writeAssembly(NL);
        decreaseIndent();

        writeAssembly(".$$.arrCheck:\n");
        increaseIndent();
        writeAssembly(THREE_PARAM, SAVE_OP, SP, "-96", SP);
        writeAssembly(TWO_PARAM, CMP_OP, I0, G0);
        writeAssembly(ONE_PARAM, BL_OP, ".$$.arrCheck2");
        writeAssembly(NOP_OP);
        writeAssembly(TWO_PARAM, CMP_OP, I0, I1);
        writeAssembly(ONE_PARAM, BGE_OP, ".$$.arrCheck2");
        writeAssembly(NOP_OP);
        retRest();
        writeAssembly(NL);
        decreaseIndent();

        writeAssembly(".$$.arrCheck2:\n");
        increaseIndent();
        writeAssembly(TWO_PARAM, SET_OP, ".$$.strArrBound", O0);
        writeAssembly(TWO_PARAM, MOV_OP, I0, O1);
        writeAssembly(ONE_PARAM, CALL_OP, "printf");
        writeAssembly(TWO_PARAM, MOV_OP, I1, O2);
        writeAssembly(ONE_PARAM, CALL_OP, "exit");
        writeAssembly(TWO_PARAM, MOV_OP, "1", O0);
        retRest();
        writeAssembly(NL);
        decreaseIndent();

        writeAssembly(".$$.ptrCheck:\n");
        increaseIndent();
        writeAssembly(THREE_PARAM, SAVE_OP, SP, "-96", SP);
        writeAssembly(TWO_PARAM, CMP_OP, I0, G0);
        writeAssembly(ONE_PARAM, BNE_OP, ".$$.ptrCheck2");
        writeAssembly(NOP_OP);
        writeAssembly(TWO_PARAM, SET_OP, ".$$.strNullPtr", O0);
        writeAssembly(ONE_PARAM, CALL_OP, "printf");
        writeAssembly(NOP_OP);
        writeAssembly(ONE_PARAM, CALL_OP, "exit");
        writeAssembly(TWO_PARAM, MOV_OP, "1", O0);
        decreaseIndent();

        writeAssembly(".$$.ptrCheck2:\n");
        increaseIndent();
        retRest();
        writeAssembly(NL);
        decreaseIndent();

    }

    public void fmt(String fmt, String typ) {
        decreaseIndent();
        writeAssembly(fmt);
        increaseIndent();
        writeAssembly(ONE_PARAM, ASCIZ, typ);

    }

    // 12
   /* public static void main(String args[]) {
        AssemblyCodeGenerator myAsWriter = new AssemblyCodeGenerator("rc.s");

        myAsWriter.increaseIndent();
        myAsWriter.writeAssembly(TWO_PARAM, SET_OP, String.valueOf(4095), "%l0");
        myAsWriter.increaseIndent();
        myAsWriter.writeAssembly(TWO_PARAM, SET_OP, String.valueOf(1024), "%l1");
        myAsWriter.decreaseIndent();

        myAsWriter.writeAssembly(TWO_PARAM, SET_OP, String.valueOf(512), "%l2");

        myAsWriter.decreaseIndent();
        myAsWriter.dispose();
    }*/
}
