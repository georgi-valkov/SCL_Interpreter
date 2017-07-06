import java.io.IOException;
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
        
        Parser pr = new Parser("test.scl");
        pr.populateList();
        // Test
        Scanner sc = new Scanner("test.scl");
        List<SyntaxToken> m = sc.getTokenList();
        for(SyntaxToken t: m) {
            System.out.print(t.toString());
        }
    }
}
