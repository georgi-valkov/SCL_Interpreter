import java.util.LinkedList;
import java.util.List;
import java.io.IOException;
import java.util.stream.Stream;

/**
 * Created by Georgi on 7/4/2017.
 */
public class Parser {
    // Declaring variables
    LinkedList<SyntaxToken> tokenList;
    LinkedList<SyntaxToken> list = new LinkedList<>();
    private String file;
    
   public Parser(String file) {
       this.file = file;
   }
   
   public void populateList() throws IOException{
    
       Scanner sc = new Scanner("test.scl");
       // Converting to linked list
       tokenList = new LinkedList<>(sc.getTokenList());
       
       while (!tokenList.isEmpty()) {
           parseStart();
//            if (tokenList.getFirst().type.getLexeme().equals("function")) {
//
//            }
//            else if (tokenList.getFirst().type.getLexeme().equals("function")) {
//
//            }
//            else if (tokenList.getFirst().type.getLexeme().equals("function")) {
//
//            }
//            else if (tokenList.getFirst().type.getLexeme().equals("function")) {
//
//            }
//            else if (tokenList.getFirst().type.getLexeme().equals("function")) {
//
//            }
//            else if (tokenList.getFirst().type.getLexeme().equals("function")) {
//
//            }
        
       }
      
       
       
       
   }
   public void parseStart() {
       parseSymbols();
       parseForwardRefs();
       parseGlobals();
       parseImplement();
   }
   
   public Boolean parseSymbols() {
       
       if (parseSymbolDef()) {
           return true;
       }
       else {
           return false;
       }
   }
   public Boolean parseForwardRefs() {
       if (tokenList.getFirst().getLexeme().equals("forward")) {
           System.out.println("Found: " + tokenList.getFirst().getLexeme());
           list.addLast(tokenList.getFirst());
           tokenList.removeFirst();
           if (parseFrefs()) {
               return true;
           }
           else {
               throw new IllegalArgumentException("Syntax Error on line " + tokenList.getFirst().getLineNum());
           }
       }
       return true;
   }
   public Boolean parseFrefs() {
       if (tokenList.getFirst().getLexeme().equals("references")) {
           System.out.println("Found: " + tokenList.getFirst().getLexeme());
           list.addLast(tokenList.getFirst());
           tokenList.removeFirst();
           if (parseForwardList()) {
               return true;
           }
           else {
               throw new IllegalArgumentException("Syntax Error on line " + tokenList.getFirst().getLineNum());
           }
       }
       else if (tokenList.getFirst().getLexeme().equals("declarations")) {
           System.out.println("Found: " + tokenList.getFirst().getLexeme());
           list.addLast(tokenList.getFirst());
           tokenList.removeFirst();
           if (parseForwardList()) {
               return true;
           }
           else {
               throw new IllegalArgumentException("Syntax Error on line " + tokenList.getFirst().getLineNum());
           }
       }
       else {
           return false;
       }
   }
   
   public Boolean parseForwardList() {
       if (parseForwards()) {
           return true;
       }
       else if (parseForwardList() && parseForwardList()) {
           return true;
       }
       else {
           throw new IllegalArgumentException("Syntax Error on line " + tokenList.getFirst().getLineNum());
       }
   }
   
   public Boolean parseForwards() {
        return true;
   }
   
   public Boolean parseGlobals() {
       return true;
   }
   public Boolean parseImplement() {
       return true;
   }
   
   public Boolean parseSymbolDef() {
       if (tokenList.getFirst().getLexeme().equals("symbol")) {
           System.out.println("Found: " + tokenList.getFirst().getLexeme());
           list.addLast(tokenList.getFirst());
           tokenList.removeFirst();
           if (parseIdentifier()) {
               return true;
           }
           else {
               throw new IllegalArgumentException("Syntax Error on line " + tokenList.getFirst().getLineNum());
           }
       }
       else {
           return false;
       }
       
    }
   
    public Boolean parseIdentifier() {
       
       if (tokenList.getFirst().getLexeme().equals("identifier")) {
           list.addLast(tokenList.getFirst());
           tokenList.removeFirst();
           return true;
       }
       else {
           return false;
       }
    }
   
   private Boolean parseFunctionList() {
       if (parseFunctionDefinition() || (parseFunctionList() && parseFunctionDefinition())) {
           return true;
       }
       else {
           return false;
       }
   }
   private Boolean parseFunctionDefinition() {
       if (parseFunctionBody()) {
           return true;
       }
       else {
           return false;
       }
   }
   private Boolean parseFunctionBody() {
        if (tokenList.getFirst().type.getLexeme().equals("function")) {
            list.add(tokenList.getFirst());
            tokenList.removeFirst();
            return true;
        }
        else if (!parseMainHead() || !parseParameters()){
            return false;
        }
        else {
            parseMainHead();
            parseParameters();
            return true;
        }
   }
   

   private Boolean parseMainHead() {
       if (tokenList.getFirst().type.getLexeme().equals("main")) {
           list.add(tokenList.getFirst());
           tokenList.removeFirst();
           return true;
       }
       else if (tokenList.getFirst().type.getLexeme().equals("identifier")) {
           list.add(tokenList.getFirst());
           tokenList.removeFirst();
           return true;
       }
       else {
           return false;
       }
   }
   
    private Boolean parseParameters() {
       
        return true;
    }
    
    // Check if two adjacent tokens are on the same line
//    private Boolean newLine() {
//       if (tokenList.getFirst().getLineNum() < tokenList.get(1).getLineNum()) {
//           return true;
//       }
//       else {
//           return false;
//       }
//    }
}
