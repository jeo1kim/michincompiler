
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

    // 6
    //parameters
    private static final String ONE_PARAM = "%s" + SEPARATOR + "%s\n";
    private static final String TWO_PARAM = "%s" + SEPARATOR + "%s, %s\n";
    private static final String THREE_PARAM = "%s" + SEPARATOR + "%s, %s, %s\n";



    public static final String OFFSET = " = -(92 + %d) & -8";
    public static final String SP_REG = "%sp";
    public static final String FP_REG = "%fp";

    //synthetic instructions
    private static final String SET_OP = "set";
    private static final String SAVE_OP = "save";
    private static final String RET_OP = "ret";
    private static final String RESTORE_OP = "restore";

    private static final String CMP_OP = "cmp";
    private static final String JMP_OP = "jmp";
    private static final String CALL_OP = "call";
    private static final String TST_OP = "tst";
    private static final String NOT_OP = "not";
    private static final String NEG_OP = "neg";
    private static final String INC_OP = "inc";
    private static final String DEC_OP = "dec";
    private static final String MOV_OP = "mov";

    //section
    private static final String SECTION = ".section \"%s\"\n";
    private static final String TEXT_SEC = ".text";
    private static final String DATA_SEC = ".data";
    private static final String BSS_SEC = ".bss";
    private static final String GLOBAL = ".global %s\n";
    private static final String ALIGN = ".align %s\n";
    private static final String WORD = ".word %s\n";
    private static final String SKIP = ".skip %s\n";

    private static final String VARCOLON = "%s:"+SEPARATOR;
    //private static final String ASCIZ = ".asciz";

    private static final String SAVE_FUNC = "SAVE.%s.%s";
    private static final String FINI_FUNC = "%s.%s.fini";



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

    public void writeVariable(STO sto, STO init){

        if(sto.isStatic() || sto.isGlobal()){
            this.writeGlobalStaticVariable(sto, init);
        }

    }

    public void writeGlobalStaticVariable(STO sto, STO init){
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
            if(init.getType().isInt()) {
                int constval = init.getIntValue();
                val = iString(constval);
            }
            if(init.getType().isFloat()){
                float constval = init.getFloatValue();
                val = Float.toString(constval);
            }
            if(init.getType().isBool()){
                if(init.getValue().equals(BigDecimal.ZERO)){
                    val = "0";
                }else {
                    val = "1";
                }
            }
            if(!init.isConst()){
                size = 0;
                val = iString(size);
            }
        }

        //indent
        increaseIndent();
        writeAssembly(SECTION, sectioncheck);
        writeAssembly(ALIGN, iString(stotype.getSize()));

        String name = sto.getName();
        if (!sto.isStatic()) { // global
            writeAssembly(GLOBAL, name);
            decreaseIndent();
            writeAssembly(VARCOLON, name);
            writeAssembly(WORD, val);
        } else {
            decreaseIndent();
            writeAssembly(VARCOLON, name);
            if (init != null) {
                writeAssembly(WORD, val);
            } else {
                writeAssembly(SKIP, val);
            }
        }
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