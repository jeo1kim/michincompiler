
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;


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
    private static final String JMP_OP = "jmp   \t";
    private static final String CALL_OP = "call   \t";
    private static final String TST_OP = "tst   \t";
    private static final String NOT_OP = "not   \t";
    private static final String NEG_OP = "neg   \t";
    private static final String INC_OP = "inc   \t";
    private static final String DEC_OP = "dec   \t";
    private static final String MOV_OP = "mov   \t";
    private static final String BE_OP = "be    \t";
    private static final String BL_OP = "bl    \t";
    private static final String BGE_OP = "bge   \t";
    private static final String BNE_OP = "bne   \t";


    private static final String ST_OP = "st     \t";
    private static final String LD_OP = "ld     \t";

    //arithmetic 
    private static final String ADD_OP = "add     \t";
    private static final String SUB_OP = "sub     \t";
    private static final String MUL_OP = "mul     \t";
    private static final String DIV_OP = "div     \t";
    private static final String MOD_OP = "mod     \t";

    private static final String FADD_OP = "fadds     \t";
    private static final String FSUB_OP = "fsubs     \t";
    private static final String FMUL_OP = "fmuls     \t";
    private static final String FDIV_OP = "fdivs     \t";



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

    //global auto
    private static final String GL_AUTO_INIT = ".$.init.%s:";
    private static final String GL_AUTO_FINI = ".$.init.%s.fini:";

    //private static final String SAVE_MAIN = "SAVE.%s.void";
    private static final String SAVE_FUNC = "SAVE.%s.void";
    private static final String FINI_FUNC = "%s.%s.fini";

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

    private static final String L7 = "%l7";
    private static final String FITOS = "fitos  \t";

    // printing
    private static final String ASCIZ = ".asciz \t";
    private static final String intFmt = ".$$.intFmt:\n";
    private static final String strFmt = ".$$.strFmt:\n";
    private static final String strTF = ".$$.strTF:\n";
    private static final String strEndl = ".$$.strEndl:\n";
    private static final String strArrBound = ".$$.strArrBound:\n";
    private static final String strNullPtr = ".$$.strNullPtr:\n";

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
    private int floatcounter = 0;
    private boolean func = false;
    private boolean arithmetic = false;


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


    public void writeCloseFunc(STO sto) {
        increaseIndent();
        writeAssembly(ONE_PARAM, CALL_OP, String.format(FINI_FUNC, sto.getName(), "void"));
        writeAssembly(NOP_OP);
        writeAssembly(RET_OP);
        writeAssembly(RESTORE_OP);

        int posoffset = Math.abs(getOffset());
        writeAssembly("%s %s\n", String.format(SAVE_FUNC, sto.getName()), String.format(OFFSET_TOTAL, iString(posoffset)));

        decreaseIndent();
        indent_level=0;
        writeAssembly(NL);
        writeAssembly(FINI_FUNC, sto.getName(), "void");
        writeAssembly(":\n");
        increaseIndent();
        int add = -92 + getOffset();
        writeAssembly(THREE_PARAM, SAVE_OP, SP, iString(add), SP);
        writeAssembly(RET_OP);
        writeAssembly(RESTORE_OP);

        func = false;
    }

    //simple function decl
    //TODO: does not take care of parameters yet
    public void writeFunctionDecl_1(STO sto) {
        offset = 0;
        func = true;
        infunc = sto.getName();
        String name = sto.getName();
        increaseIndent();
        //writeAssembly(SECTION, TEXT_SEC);
        writeAssembly(GLOBAL, name);
        decreaseIndent();
        writeAssembly(VARCOLON, name);
        String funcvoid = name + ".void";
        writeAssembly(VARCOLON, funcvoid);

        increaseIndent();
        writeAssembly(TWO_PARAM, SET_OP, String.format(SAVE_FUNC, name), "%g1");
        writeAssembly(THREE_PARAM, SAVE_OP, SP, "%g1", SP);
        decreaseIndent();
        writeAssembly(NL);

        decreaseIndent();
        //decreaseIndent();
    }

    public void writeAssignExprVariable(STO stoDes, STO expr) {
        int desoffset;
        String val = "";
        increaseIndent();
        if (func) {
            indent_level = 2;
        }
        if ((desoffset = stoDes.getSparcOffset()) != 0) {
            String sName = stoDes.getName();
            String iName = expr.getName();
            writeAssembly(String.format(var_comment, sName, iName));
            val = stoValue(expr); // stoVal gets teh value of sto.
            writeAssembly(TWO_PARAM, SET_OP, iString(desoffset), O1);
            writeAssembly(THREE_PARAM, ADD_OP, FP, O1, O1);
            writeInit(stoDes, expr);

        }
        decreaseIndent();
    }

    public void writeBinaryExpr(STO a, Operator o, STO b, STO result){
        boolean iffloat = false;
        //arithmetic = true;

        increaseIndent();

        writeCallStored(a, 0);
        decreaseOffset();
        result.setSparcOffset(getOffset());
        if(a.getType().isInt() && b.getType().isFloat()){
            convertToFloatBinary(a, b, 0);
        }

        writeCallStored(b, 1);
        if(a.getType().isFloat() && b.getType().isInt()){
            convertToFloatBinary(a, b, 1);
        }

        if(a.getType().isFloat() || b.getType().isFloat()){
            iffloat = true;
        }
        writeInstructionCase(o, iffloat);
        String register = iffloat ? f0 : O0;

        //int binaryoffset = 
        writeAssembly(TWO_PARAM, SET_OP, iString(result.getSparcOffset()), O1);
        writeAssembly(THREE_PARAM, ADD_OP, FP, O1, O1);
        writeAssembly(TWO_PARAM, ST_OP, register, "[" + O1 + "]");

        decreaseIndent();
    }

    public void writeInstructionCase(Operator o, boolean iffloat){
        String opname = o.getName();
        
        if(!iffloat){
            switch(opname){
                case "+": writeAssembly(THREE_PARAM, ADD_OP, O0, O1, O0);
                    break;
                case "-": writeAssembly(THREE_PARAM, SUB_OP, O0, O1, O0);
                    break;
                case "*": writeAssembly(THREE_PARAM, MUL_OP, O0, O1, O0);
                    break;
                case "/": writeAssembly(THREE_PARAM, DIV_OP, O0, O1, O0);
                    break;
                case "%": writeAssembly(THREE_PARAM, MOD_OP, O0, O1, O0);
                    break;
            }
        }
        else{
            switch(opname){
                case "+": writeAssembly(THREE_PARAM, FADD_OP, f0, F1, f0);
                    break;
                case "-": writeAssembly(THREE_PARAM, FSUB_OP, f0, F1, f0);
                    break;
                case "*": writeAssembly(THREE_PARAM, FMUL_OP, f0, F1, f0);
                    break;                
            }
        }
    }

    public void writeCallStored(STO init, int x){
        String val = stoValue(init); // stoVal gets teh value of sto.
        String register = init.getType().isFloat() ? "isfl" : "isint"; // check for float f0 or o0
        String global = init.getSparcBase() == "%g0" ? init.getName() : iString(init.getSparcOffset());
        String globalreg = init.getSparcBase() == "%g0" ? G0 : FP;

        if(x == 0){
            if(register.equals("isfl")){
                register = f0;
            }else {
                register = O0;
            }
        }
        if( x == 1 ){
            if(register.equals("isfl")){
                register = F1;
            }else {
                register = O1;
            }
        }

        writeAssembly(TWO_PARAM, SET_OP, global, L7);
        writeAssembly(THREE_PARAM, ADD_OP, globalreg, L7, L7);
        writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", register);
    }

    //used when initializing values e.g x = 2
    public void writeInit(STO sto, STO init) {
        String val = stoValue(init); // stoVal gets teh value of sto.
        String register = init.getType().isFloat() ? f0 : O0; // check for float f0 or o0
        String global = init.getSparcBase() == "%g0" ? init.getName() : iString(init.getSparcOffset());
        String globalreg = init.getSparcBase() == "%g0" ? G0 : FP;

        if (init.isConst()) {
            writeAssembly(TWO_PARAM, SET_OP, val, O0);
            if (sto.getType().isFloat()) {
                convertToFloat(sto ,init);
                writeAssembly(TWO_PARAM, ST_OP, f0, "[" + O1 + "]");
                //decreaseIndent();
                writeAssembly(NL);
                return;
            }
        } else {

            writeAssembly(TWO_PARAM, SET_OP, global, L7);
            writeAssembly(THREE_PARAM, ADD_OP, globalreg, L7, L7);
            writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", register);
            if (sto.getType().isFloat() && init.getType().isInt()) {
                convertToFloat(sto, init);
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
    public void convertToFloatBinary(STO sto, STO init, int x) {
        String global = init.getSparcBase() == "%g0" ? init.getName() : iString(init.getSparcOffset());
        String globalreg = init.getSparcBase() == "%g0" ? G0 : FP;
        String register = init.getType().isFloat() ? f0 : O0; // check for float f0 or o0

        if(x == 0){
            if(register.equals("isfl")){
                register = f0;
            }else {
                register = O0;
            }
        }
        if( x == 1 ){
            if(register.equals("isfl")){
                register = F1;
            }else {
                register = O1;
            }
        }

        //int newoffset =  sto.isGlobal() ? -4:offset;
        decreaseOffset();
        //int newoffset = getOffset() - 8; 
        //sto.setSparcOffset(newoffset);
        //offset = newoffset;
        writeAssembly(TWO_PARAM, SET_OP, iString(getOffset()), L7);
        writeAssembly(THREE_PARAM, ADD_OP, FP, L7, L7);
        writeAssembly(TWO_PARAM, ST_OP, O0, "[" + f0 + "]");
        writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", f0);
        writeAssembly(TWO_PARAM, FITOS, f0, f0);

        //writeAssembly(TWO_PARAM, ST_OP, f0, "[" + O1 + "]");

    }

    //convert int to float when needed
    public void convertToFloat(STO sto, STO init) {
        String global = init.getSparcBase() == "%g0" ? init.getName() : iString(init.getSparcOffset());
        String globalreg = init.getSparcBase() == "%g0" ? G0 : FP;
        String register = init.getType().isFloat() ? f0 : O0; // check for float f0 or o0

        int newoffset =  sto.isGlobal() ? -4:offset;
        decreaseOffset();
        writeAssembly(TWO_PARAM, SET_OP, iString(newoffset), L7);
        writeAssembly(THREE_PARAM, ADD_OP, FP, L7, L7);
        writeAssembly(TWO_PARAM, ST_OP, O0, "[" + f0 + "]");
        writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", f0);
        writeAssembly(TWO_PARAM, FITOS, f0, f0);

        //writeAssembly(TWO_PARAM, ST_OP, f0, "[" + O1 + "]");

    }

    public void writeConstFloat(STO init) {

        writeAssembly(NL);
        writeAssembly(SECTION, RODATA_SEC);
        writeAssembly(ALIGN, iString(init.getType().getSize()));
        //decreaseIndent();
        int templvl = indent_level;
        indent_level = 1;
        writeAssembly(FLOAT_COUNTER, iString(++floatcounter));
        //increaseIndent();
        indent_level = templvl;
        writeAssembly(FLOAT, stoValue(init));
        writeAssembly(NL);

        writeAssembly(SECTION, TEXT_SEC);
        writeAssembly(ALIGN, iString(init.getType().getSize()));
        writeAssembly(TWO_PARAM, SET_OP, ".$$.float." + iString(floatcounter), L7);

        writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", f0);
    }


    public void writeReturn(STO init) {
        increaseIndent();
        String val = stoValue(init); // stoVal gets teh value of sto.
        String register = init.getType().isFloat() ? f0 : I0; // check for float f0 or o0
        String global = init.getSparcBase() == "%g0" ? init.getName() : iString(init.getSparcOffset());
        String globalreg = init.getSparcBase() == "%g0" ? G0 : FP;

        writeAssembly(NL);
        writeAssembly("! return "+val+";\n");
        if (init.isConst()) {
            if (init.getType().isFloat()) {
                writeConstFloat(init);
            } else {
                writeAssembly(TWO_PARAM, SET_OP, val, I0);
            }
        } else {

            writeAssembly(TWO_PARAM, SET_OP, global, L7);
            writeAssembly(THREE_PARAM, ADD_OP, FP, L7, L7);
            writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", register);
        }
        decreaseIndent();
    }

    //TODO: take care of when init not there too
    public void writeLocalVariable(STO sto, STO init) {
        increaseIndent();
        increaseIndent();
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
            writeAssembly(TWO_PARAM, SET_OP, iString(offset), O1);
            writeAssembly(THREE_PARAM, ADD_OP, FP, O1, O1);

            if (sto.getAuto()) { // if its auto

                writeInit(sto, init);

            } else if (init.getType().isFloat() && !sto.getAuto()) {  // if its not auto and float

                writeConstFloat(init);
                writeAssembly(TWO_PARAM, ST_OP, f0, "[" + O1 + "]");

                decreaseIndent();
                writeAssembly(NL);
            } else {
                //set value
                writeInit(sto, init);
            }
        }
        decreaseIndent();
        decreaseIndent();
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

        if ((init == null) || (auto = sto.getAuto())) {
            sectioncheck = BSS_SEC;
        }
        else {
            sectioncheck = sto.getAuto() ? BSS_SEC : DATA_SEC;

            val = stoValue(init);   // stoValue gets the value of the sto
            //any global variable not initialized when declared is set to value 0
            if ( sto.isGlobal()) {
                if( init.isStatic()){
                    sectioncheck = BSS_SEC;
                }
                else if(!init.isConst()) {
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
        }
        else {
            if (init != null && init.getType().isFloat()) {
                writeAssembly(FLOAT, stoValue(init));
            } else if (init != null) {
                writeAssembly(WORD, stoValue(init));
            }
        }

        if (auto) {       // if its auto do auto and return
            sectionAlign(TEXT_SEC, iString(stotype.getSize()));
            writeGlobalAuto(sto, init);
        }
        else {
            if (init != null && sto.isGlobal()) {
                if (sto.getType().isFloat() && init.getType().isInt()) {
                    sectionAlign(TEXT_SEC, iString(stotype.getSize()));
                    writeGlobalAuto(sto, init);

                } else if (init.isStatic()) {
                    sectionAlign(TEXT_SEC, iString(stotype.getSize()));
                    writeGlobalAuto(sto, init);

                }

            }
        }
        sectionAlign(TEXT_SEC, iString(stotype.getSize())); // SECTION and ALIGN
        decreaseIndent();
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
        writeAssembly(GL_AUTO_INIT, sName);
        newline();
        increaseIndent();
        writeAssembly(TWO_PARAM, SET_OP, save, G1);
        writeAssembly(THREE_PARAM, SAVE_OP, SP, G1, SP);

        writeAssembly(NL);
        increaseIndent();
        writeAssembly(String.format(var_comment, sName, iName));
        writeAssembly(TWO_PARAM, SET_OP, sName, O1);
        writeAssembly(THREE_PARAM, ADD_OP, G0, O1, O1);
        writeInit(sto, init); // do the init registers
        writeAssembly(NL);

        writeAssembly(String.format(endFunc_cmt, sName));
        call(sName + ".fini");
        retRest();

        writeAssembly(save + String.format(OFFSET_TOTAL, "0"));
        writeAssembly(NL);
        writeAssembly(NL);

        decreaseIndent();
        writeAssembly(GL_AUTO_FINI, sName);
        newline();
        increaseIndent();
        writeAssembly(THREE_PARAM, SAVE_OP, SP, "-96", SP); // might need to change offset
        retRest();

        sectionAlign(INIT_SEC, iString(sto.getType().getSize()));
        call(sName);
    }

    public void call(String name) {
        writeAssembly(ONE_PARAM, CALL_OP, String.format(GL_AUTO_INIT, name));
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


    public void fmtHeader(){
        increaseIndent();
        sectionAlign(RODATA_SEC, "4");
        fmt(intFmt, d);
        fmt(strFmt, s);
        fmt(strTF, tf);
        fmt(strEndl, nl);
        fmt(strArrBound, arrbound);
        fmt(strNullPtr, nullptr );
        sectionAlign(TEXT_SEC, "4");

        decreaseIndent();

        writeAssembly( ".$$.printBool:\n");
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

        writeAssembly( ".$$.ptrCheck:\n");
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

    public void fmt(String fmt, String typ){
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
