import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Georgi Valkov
 *            Vojtech Martinek
 *            Dedric Sundby
 *            on 6/19/2017.
 *
 * Concepts of Programming Lang XLS Group 94 Summer Semester 2017
 * Professor: Jose Garrido
 */

public class Main {
    
    /*  Entry point of the Scanner*/
    public static void main(String[] args) throws IOException{
        
        // Creating an input stream from a source code file
        InputStream f = Main.class.getResourceAsStream("test.scl");
        // Creating a new Scanner using the input stream
        Scanner sc = new Scanner(f);
        // Scan the source code to find all possible tokens
        List<SyntaxToken> m = sc.getTokenList();
//        for(SyntaxToken t: m) {
//            System.out.print(t.toString());
//        }
        // Creating a parser using the list returned from the Scanner
        Parser parser = new Parser(m);
        // Parse the list with tokens
        parser.parse();
        LinkedList<SyntaxToken> list = parser.getParsedTokenList();
        
        // Print relevant information whether parsing was successful or not
//        if(parser.wasSuccessful())
//            System.out.println("Parsing successful!");
//        else
//            System.out.println("Parsing failed, " + parser.errorCount() + " errors detected.");
        
        
        Interpreter interpreter = new Interpreter(m);
        interpreter.run();
    }
}
