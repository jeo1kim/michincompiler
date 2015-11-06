
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

    private static final String CMP_OP = "cmp";
    private static final String JMP_OP = "jmp";
    private static final String CALL_OP = "call"+SEPARATOR;
    private static final String TST_OP = "tst";
    private static final String NOT_OP = "not";
    private static final String NEG_OP = "neg";
    private static final String INC_OP = "inc";
    private static final String DEC_OP = "dec";
    private static final String MOV_OP = "mov";

    private static final String ST_OP = "st     \t";
    private static final String LD_OP = "ld     \t";

    private static final String ADD_OP = "add    \t";


    //section
    private static final String SECTION = ".section" + SEPARATOR+"\"%s\"\n";

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
    private static final String O0 = "%o0";
    private static final String O1 = "%o1";

    //global register
    private static final String G0 = "%g0";
    private static final String G1 = "%g1";
    private static final String G2 = "%g2";
    private static final String G3 = "%g3";



    // for float
    private static final String f0 = "%f0";
    private static final String L7 = "%l7";

    private static final String var_comment = "! %s = %s\n";



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
    public int getOffset(){
        return this.offset;
    }
    public int increaseOffset(){
        return this.offset += 4;
    }
    public int decreaseOffset(){
        return this.offset -= 4;
    }

    //check if the variable is static or global
    public void writeVariable(STO sto, STO init){
        if(sto.isStatic() || sto.isGlobal()){
            this.writeGlobalStaticVariable(sto, init);
        }
        else {
            writeLocalVariable(sto, init);
        }
    }


    public void writeCloseFunc(STO sto){
        increaseIndent();
        writeAssembly(ONE_PARAM, CALL_OP, String.format(FINI_FUNC, sto.getName(), "void"));
        writeAssembly(NOP_OP);
        writeAssembly(RET_OP);
        writeAssembly(RESTORE_OP);

        int posoffset = Math.abs(getOffset());
        writeAssembly("%s %s\n", String.format(SAVE_FUNC, sto.getName()), String.format(OFFSET_TOTAL, iString(posoffset)));
        
        decreaseIndent();

        writeAssembly(NL);
        writeAssembly(FINI_FUNC, sto.getName(), "void");
        writeAssembly(":\n");
        increaseIndent();
        int add = -92 + getOffset();
        writeAssembly(THREE_PARAM, SAVE_OP, SP, iString(add), SP);
        writeAssembly(RET_OP);
        writeAssembly(RESTORE_OP);
    }
    //simple function decl
    //TODO: does not take care of parameters yet
    public void writeFunctionDecl_1(STO sto){
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

    public void writeAssignExprVariable(STO stoDes, STO expr){
        int desoffset;
        String val = "";
        increaseIndent();
        if((desoffset = stoDes.getSparcOffset()) != 0){
            

            val = stoValue(expr); // stoVal gets teh value of sto.
            //create space
            writeAssembly(TWO_PARAM, SET_OP, iString(desoffset), O1);
            writeAssembly(THREE_PARAM, ADD_OP, FP, O1, O1);
            //set value
            writeAssembly(TWO_PARAM, SET_OP, val, O0);
            writeAssembly(TWO_PARAM, ST_OP, O0, "["+O1+"]");
            decreaseIndent();
            writeAssembly(NL);
            
        }
        decreaseIndent();
    }
    //TODO: take care of when init not there too
    public void writeLocalVariable(STO sto, STO init){
        increaseIndent();
        increaseIndent();
        String sectioncheck;
        String register = "";
        String val = ""; //later used for init if init is null to handle null pointer


        int floatcounter=0;
        int templvl=0;

        decreaseOffset();


        if((init != null)){     //check if init is not null store the value

            increaseIndent();
            val = stoValue(init); // stoVal gets teh value of sto.
            String sName = sto.getName();
            String iName = init.getName();
            //create space
            writeAssembly(String.format(var_comment, sName,iName));
            writeAssembly(TWO_PARAM, SET_OP, iString(offset), O1);
            writeAssembly(THREE_PARAM, ADD_OP, FP, O1, O1);

            if(sto.getAuto()){ // if its auto

                register = init.getType().isFloat() ? f0 : O0; // check for float f0 or o0
                if(sto.isConst()){
                    writeAssembly(TWO_PARAM, SET_OP, stoValue(init), O0);

                }
                else {
                    writeAssembly(TWO_PARAM, SET_OP, iString(init.getSparcOffset()), L7);
                    writeAssembly(THREE_PARAM, ADD_OP, FP, L7, L7);
                    writeAssembly(TWO_PARAM, LD_OP, "[" + L7 + "]", register);
                }
                writeAssembly(TWO_PARAM, ST_OP, register, "["+O1+"]");
                decreaseIndent();
                writeAssembly(NL);


            }
            else if(init.getType().isFloat() && !sto.getAuto()){  // if its not auto and float
                writeAssembly(NL);
                writeAssembly(SECTION,RODATA_SEC);
                writeAssembly(ALIGN, iString(init.getType().getSize()));
                //decreaseIndent();
                templvl = indent_level;
                indent_level = 1;
                writeAssembly(FLOAT_COUNTER, iString(++floatcounter));
                //increaseIndent();
                indent_level = templvl;
                writeAssembly(FLOAT, stoValue(init));
                writeAssembly(NL);

                writeAssembly(SECTION, TEXT_SEC);
                writeAssembly(ALIGN, iString(init.getType().getSize()));
                writeAssembly(TWO_PARAM, SET_OP, ".$$.float."+iString(floatcounter), L7  );

                writeAssembly(TWO_PARAM, LD_OP, "["+L7+"]", f0);
                writeAssembly(TWO_PARAM, ST_OP, f0, "["+O1+"]");

                decreaseIndent();
                writeAssembly(NL);


            }
            else{
            //set value
                
                writeAssembly(TWO_PARAM, SET_OP, val, O0);
                writeAssembly(TWO_PARAM, ST_OP, O0, "["+O1+"]");
                decreaseIndent();
                writeAssembly(NL);
            }
            decreaseIndent();
        }
        else{

            //here nothing done yet
            sto.setSparcOffset(getOffset());
            decreaseIndent();
        }
        decreaseIndent();
    }
    //writes assembly for variables that is glocal or static
    public void writeGlobalStaticVariable(STO sto, STO init){
        increaseIndent();
        String sectioncheck;
        //Type sType = sto.getType();

        Type stotype = sto.getType();
        int size = stotype.getSize();
        String val = "";
        boolean auto = false;


        if((init == null) || (auto = sto.getAuto())){
            sectioncheck = BSS_SEC;
        }
        else{

            sectioncheck = sto.getAuto() ? BSS_SEC : DATA_SEC;

            val = stoValue(init);   // stoValue gets the value of the sto

            //any global variable not initialized when declared is set to value 0
            if(!init.isConst() && sto.isGlobal()){
                size = 0;
                val = iString(size);
            }

        }

        writeAssembly(NL);
        writeAssembly(SECTION, sectioncheck);
        writeAssembly(ALIGN, iString(stotype.getSize()));



        String name = sto.getName();


        if (!sto.isStatic()) { // global
            writeAssembly(GLOBAL, name);
        }
        //take care of the name change if static is inside function
        if(sto.isStatic()){
            if(infunc != null){
                name = infunc + ".void." + sto.getName();
            }
            //if sto is static and is not global get the size of the type and write as word even tho it is bss
            if(!sto.isGlobal()){
                val = iString(stotype.getSize());
            }
        }

        decreaseIndent();
        writeAssembly(VARCOLON, name);
        increaseIndent();

        if((sectioncheck == BSS_SEC)){
            writeAssembly(SKIP, iString(size));
        }
        else {
            if(init !=null && init.getType().isFloat()){
                writeAssembly(FLOAT,stoValue(init));
            }
            else if (init !=null) {
                writeAssembly(WORD, stoValue(init));
            }
        }

        if(auto){       // if its auto do auto and return

            sectionAlign(TEXT_SEC,iString(stotype.getSize()) );
            writeGlobalAuto(sto, init);
        }

        sectionAlign(TEXT_SEC,iString(stotype.getSize()) ); // SECTION and ALIGN
        //writeAssembly(SECTION, TEXT_SEC);
        //writeAssembly(ALIGN, iString(stotype.getSize()));

        //writeAssembly(NL);
        decreaseIndent();

    }

    private static final String endFunc_cmt = "! End of function .$.init.%s\n";
    public void writeGlobalAuto(STO sto, STO init){
        String sName = sto.getName();
        String iName = init.getName();
        String save = "SAVE..$.init."+sName;
        String register = "";

        decreaseIndent();
        writeAssembly(GL_AUTO_INIT, sName);
        newline();
        increaseIndent();
        writeAssembly(TWO_PARAM, SET_OP, save , G1);
        writeAssembly(THREE_PARAM, SAVE_OP, SP, G1, SP);

        writeAssembly(NL);
        increaseIndent();
        writeAssembly(String.format(var_comment, sName,iName));
        writeAssembly(TWO_PARAM, SET_OP, sName, O1);
        writeAssembly(THREE_PARAM, ADD_OP, G0, O1, O1);

        writeAssembly(TWO_PARAM, SET_OP, iName, L7);
        writeAssembly(THREE_PARAM, ADD_OP, G0, L7, L7);

        register = init.getType().isFloat() ? f0 : O0; // check for float f0 or o0
        writeAssembly(TWO_PARAM, LD_OP, "["+L7+"]", register);
        writeAssembly(TWO_PARAM, ST_OP, register, "["+O1+"]");

        decreaseIndent();
        writeAssembly(NL);
        writeAssembly(String.format(endFunc_cmt, sName));
        call(sName+".fini");
        //writeAssembly(ONE_PARAM, CALL_OP, String.format(GL_AUTO_FINI,sName) );
        retRest();
        writeAssembly(save+String.format(OFFSET_TOTAL,"0"));
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

    public void call(String name){
        writeAssembly(ONE_PARAM, CALL_OP, String.format(GL_AUTO_INIT, name));
        writeAssembly(NOP_OP);
    }
    public void sectionAlign(String section, String align){
        writeAssembly(NL);
        writeAssembly(SECTION, section );
        writeAssembly(ALIGN, align);
    }

    public void newline(){
        writeAssembly(NL);
    }


    public void retRest(){
        writeAssembly(RET_OP);
        writeAssembly(RESTORE_OP);
    }

    //get int value and form it as a string
    public String iString(int val){
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
    public void writeAssembly(String template, String ... params) {
        StringBuilder asStmt = new StringBuilder();

        // 10
        for (int i=0; i < indent_level; i++) {
            asStmt.append(SEPARATOR);
        }

        // 11
        asStmt.append(String.format(template, (Object[])params));

        try {

            fileWriter.write(asStmt.toString());
        } catch (IOException e) {
            System.err.println(ERROR_IO_WRITE);
            e.printStackTrace();
        }
    }

    //get the value of the sto when sto is constant
    public String stoValue(STO sto){

        String ret = "";
        if(sto.isConst()) {
            Type stype = sto.getType();
            if (stype.isInt()) {
                ret =  Integer.toString(sto.getIntValue());
            }

            if (stype.isFloat()) {
                ret =  Float.toString(sto.getFloatValue());
            }

            if (stype.isBool()) {
                ret = Integer.toString((sto.getBoolValue()) ? 1 : 0);
            }
            return ret;
        }
        else{
            return "0";
        }
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
