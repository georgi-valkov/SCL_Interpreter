
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;


/**
 * Created by Georgi Valkov
 *            Vojtech Martinek
 *            Dedric Sundby
 *            on 7/21/2017.
 *
 * Concepts of Programming Lang XLS Group 94 Summer Semester 2017
 * Professor: Jose Garrido
 */

public class Interpreter {
    // Data fields
    private LinkedList<SyntaxToken> parsedTokenTable = new LinkedList<>();
    Parser parser;
    
    // Constructor
    public Interpreter(List<SyntaxToken> m) {
        parser = new Parser(m);
        parser.parse();
        parsedTokenTable = parser.getParsedTokenList();
    }
    // Builds up string with already translated to Java
    // source code, saves it in file
    // Compiles the file and executes it
    public void run()  throws IOException{
        String source ="";
        // Building source code string
        String imports = ("import java.*; \n");
        String mainMethod = ("public class Test {\n" +
            "public static void main (String[] args) { \n" +
            "%s \n" +
            "} \n" +
            "} \n");
        String mainBody = mapMainBody(parsedTokenTable);
        
        source = imports + String.format(mainMethod, mainBody);
        
        // Saving the source file
        File file = new File("Test.java");
        try {
            // Creating a buffer that will write to the the file
            BufferedWriter out = new BufferedWriter(new FileWriter(file.getName()));
            out.write(source);
            out.close();
        }
        catch (IOException e){
            System.out.println("Writing to the source file failed");
        }
        // Compiling the source file
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        compiler.run(null, null, null, file.getPath());
        
        // Executing the file
        Runtime.getRuntime().exec("cmd.exe /c start cmd /k java Test");
    }
    // Maps the content of main body to java code
    private String mapMainBody(LinkedList<SyntaxToken> tokens) {
        
        
        int maxLines = tokens.getLast().getLineNum();
        SyntaxToken current;
        
        String body ="";
        current = tokens.pop();
        for (int i = 1; i <= maxLines + 1; i++) {


            LinkedList<SyntaxToken> list = new LinkedList<>();
            // convert list of tokens to their equivalent
            // java statements
             do {
                list.addLast(current);
                if (!tokens.isEmpty()) {
                    current = tokens.pop();
                    if (current.getLexeme().equals("set")) {
                        int lineNum = current.getLineNum();
                        
                        while (lineNum == current.getLineNum()) {
                            
                            body += current.getLexeme() + " ";
                            
                            current = tokens.pop();
                        }
                        tokens.push(current);
                        body += ";\n";
                    }
                    else if (current.getLexeme().equals("if")) {
                        body += "if (";
                    }
                    else if (current.getLexeme().equals("display")) {
                        body += "\nSystem.out.print( " + tokens.pop().getLexeme() + ");\n";
                    }
                    else if (current.getLexeme().equals("then")) {
                        body += ") {";
                    }
                    else if (current.getLexeme().equals("else")) {
                        body += "}\n else {";
                    }
                    else if (current.getLexeme().equals("endif")) {
                        body += "}";
                    }
                    else if ((current.getLexeme().equals("function")) ||
                            (current.getLexeme().equals("main")) ||
                            (current.getLexeme().equals("begin"))||
                            (current.getLexeme().equals("endfun")) ) {
                        
                    }
                    else {
                        body += current.getLexeme() + " ";
                    }

                }
                
            } while ((current.getLineNum() == i) && (!tokens.isEmpty()));
    
        }
        // Because we have only int variables
        // replace the beginning of the assignment statement to int
        body = body.replaceAll("set", "int");
        // Clear else if statement from leftovers
        body = body.replaceAll("else .*if", "else if");
        return body;
        
    }
    
}
