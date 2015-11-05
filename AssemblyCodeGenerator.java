
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



    public static final String OFFSET = " = -(92 + %d) & -8";
    public static final String SP = "%sp";
    public static final String FP = "%fp";

    //synthetic instructions
    private static final String SET_OP = "set";
    private static final String SAVE_OP = "save";
    private static final String RET_OP = "ret\n";
    private static final String RESTORE_OP = "restore\n";
    private static final String NOP_OP = "nop\n";

    private static final String CMP_OP = "cmp";
    private static final String JMP_OP = "jmp";
    private static final String CALL_OP = "call";
    private static final String TST_OP = "tst";
    private static final String NOT_OP = "not";
    private static final String NEG_OP = "neg";
    private static final String INC_OP = "inc";
    private static final String DEC_OP = "dec";
    private static final String MOV_OP = "mov";

    private static final String ST_OP = "st";
    private static final String LD_OP = "ld";

    private static final String ADD_OP = "add";


    //section
    private static final String SECTION = ".section" + SEPARATOR+"\"%s\"\n";
    private static final String TEXT_SEC = ".text";
    private static final String DATA_SEC = ".data";
    private static final String BSS_SEC = ".bss";
    private static final String GLOBAL = ".global \t%s\n";
    private static final String ALIGN = ".align  \t%s\n";
    private static final String WORD = ".word   \t%s\n";
    private static final String SKIP = ".skip   \t%s\n";
    private static final String FLOAT = ".single \t0r%s\n";

    private static final String VARCOLON = "%s:\n"+SEPARATOR;
    //private static final String ASCIZ = ".asciz";

    private static final String SAVE_MAIN = "SAVE.main";
    private static final String SAVE_FUNC = "SAVE.%s";
    private static final String FINI_FUNC = "%s.%s.fini";

    private static final String NL = "\n";
    private static final String O0 = "%o0";
    private static final String O1 = "%o1";



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

    //simple function decl
    //TODO: does not take care of parameters yet
    public void writeFunctionDecl_1(STO sto){
        infunc = sto.getName();
        String name = sto.getName();
        increaseIndent();
        writeAssembly(SECTION, TEXT_SEC);
        writeAssembly(GLOBAL, name);
        decreaseIndent();
        writeAssembly(VARCOLON, name);
        writeAssembly(NL);

        if(name.equals("main")){
            increaseIndent();
            writeAssembly(TWO_PARAM, SET_OP, SAVE_MAIN, "%g1");
            writeAssembly(THREE_PARAM, SAVE_OP, SP, "%g1", SP);
            decreaseIndent();
            writeAssembly(NL);
        }
    }

    //TODO: take care of when init not there too
    public void writeLocalVariable(STO sto, STO init){
        String sectioncheck;
        String val = ""; //later used for init if init is null to handle null pointer

        increaseIndent();
        if((init != null)){     //check if init is not null store the value


            val = stoValue(init); // stoVal gets teh value of sto.


            decreaseOffset();
            //create space
            writeAssembly(TWO_PARAM, SET_OP, iString(offset), O1);
            writeAssembly(THREE_PARAM, ADD_OP, FP, O1, O1);
            //set value
            writeAssembly(TWO_PARAM, SET_OP, val, O0);
            writeAssembly(TWO_PARAM, ST_OP, O1, "["+O1+"]");
            decreaseIndent();
            writeAssembly(NL);
        }
        else{
            //here nothing done yet
            sectioncheck = TEXT_SEC;
        }
    }
    //writes assembly for variables that is glocal or static
    public void writeGlobalStaticVariable(STO sto, STO init){
        increaseIndent();
        String sectioncheck;
        //Type sType = sto.getType();

        Type stotype = sto.getType();
        int size = stotype.getSize();
        String val = "";


        if((init == null)){
            sectioncheck = BSS_SEC;
        }
        else{
            sectioncheck = DATA_SEC;

            val = stoValue(init);   // stoValue gets the value of the sto

            //any global variable not initialized when declared is set to value 0
            if(!init.isConst() && sto.isGlobal()){
                size = 0;
                val = iString(size);
            }

        }


        writeAssembly(SECTION, sectioncheck);
        writeAssembly(ALIGN, iString(stotype.getSize()));

        String name = sto.getName();
        if (!sto.isStatic()) { // global
            writeAssembly(GLOBAL, name);

        }
        //take care of the name change if static is inside function
        if(sto.isStatic()){
            if(infunc != null){
                name = infunc + "." + sto.getName();
            }
            //if sto is static and is not global get the size of the type and write as word even tho it is bss
            if(!sto.isGlobal()){
                val = iString(stotype.getSize());
            }
        }

        decreaseIndent();
        writeAssembly(VARCOLON, name);
        if((sectioncheck == BSS_SEC) && sto.isGlobal()){
            writeAssembly(SKIP, iString(size));
        } else {
            if(init.getType().isFloat()){
                writeAssembly(FLOAT,val);
            }
            else {
                writeAssembly(WORD, val);
            }
        }

        increaseIndent();
        writeAssembly(NL);
        writeAssembly(SECTION, TEXT_SEC);
        writeAssembly(ALIGN, iString(stotype.getSize()));
        writeAssembly(NL);
        decreaseIndent();

    }





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