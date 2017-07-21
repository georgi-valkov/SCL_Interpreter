import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Georgi Valkov
 *            Vojtech Martinek
 *            Dedric Sundby
 *            on 6/19/2017.
 *
 * Concepts of Programming Lang XLS Group 94 Summer Semester 2017
 * Professor: Jose Garrido
 */

public class Scanner {
    /* Data fields */
    // Stream reader that will read
    // the file character by character
    private PushbackReader charReader;
    // An integer variable that will count
    // the line numbers
    private int lineCounter = 1;
    // An array list that will store all tokens
    private ArrayList<SyntaxToken> tokenList = new ArrayList<>();
    // Table that will store the reserved words and operators
    private final HashMap<String, TokenConst> LookTable = new HashMap<>();
    // Will store the next char
    private char nextChar;

    /* Constructor */
    public Scanner(InputStream file) throws IOException{
        // If string path does not lead to a file
        // prints out file not found
        try {
            charReader = new PushbackReader(new InputStreamReader(file));
            throw new FileNotFoundException();
        } catch (FileNotFoundException ex) {
        }
        // Populate Tables with reserved words and operators
        populateTable();
    }

    /*
    Precondition:
        A Scanner object
        A file that contains scl source code
    Post condition:
        Returns an array list with all tokens
     */
    public ArrayList<SyntaxToken> getTokenList() throws IOException{

        if(!tokenList.isEmpty())
        	return tokenList;

        String buffer = "";
        nextChar = (char)charReader.read();

        while(true){ // Loops while the end of file is reached
            incrementLineCounter();
            
            if(nextChar == (char)-1)
            {
                // End of the file
				break;
			}
			// Next char is letter
            else if (Character.isLetter(nextChar)) {
                while (true) {
                    
                    incrementLineCounter();
                    // Check if it's a reserve word
                    // When next character is whitespace
                    if ((Character.isWhitespace(nextChar)) || (nextChar == (char) -1)) {
                        if (LookTable.get(buffer) != null) {
                            tokenList.add(new SyntaxToken(LookTable.get(buffer), lineCounter));
                            buffer = "";
                            break;
                        }
                        else {
                            tokenList.add(new ValueToken<>(TokenConst.ID, lineCounter, buffer));
                            buffer = "";
                            break;
                        }
                    }
                    // When next character is one from the list
                    else if ("()[],^".indexOf(nextChar) != -1 ) {
                        if (LookTable.get(buffer) != null) {
                            tokenList.add(new SyntaxToken(LookTable.get(buffer), lineCounter));
                            buffer = "";
                            charReader.unread(nextChar);
                            break;
                        }
                        else {
                            tokenList.add(new ValueToken<>(TokenConst.ID, lineCounter, buffer));
                            buffer = "";
                            charReader.unread(nextChar);
                            break;
                        }
                        
                        
                    }
                    // Throws an exception if character is one of the list
                    else if ("@#$%^&<>".indexOf(nextChar) != -1){
                        throw (new IllegalArgumentException("Illegal character - Line " + lineCounter));
                    }
                    else if (nextChar == (char)-1) {
                        break;
                    }
                    else {
                        buffer += nextChar;
                        nextChar = (char) charReader.read();
                    }
                }
                
            }
            // Integer Literal
            else if (Character.isDigit(nextChar)) {
                
                while (true) {
                    incrementLineCounter();
                    // If next char is digit add it to the buffer
                    if (Character.isDigit(nextChar)) {
                        buffer += nextChar;
                        
                    }
                    // Illegal character is integer literal
                    else if (!("_#@&;:'!.,".indexOf(nextChar) == -1) || Character.isLetter(nextChar)) {
                        
                        throw(new IllegalArgumentException("Illegal character in integer literal - Line " + lineCounter));
                        
                    }
                    else {
                        // Add integer literal to the list
                        tokenList.add(new ValueToken<>(TokenConst.INTLIT, lineCounter, buffer));
                        // Reset Buffer
                        buffer = "";
                        charReader.unread(nextChar);
                        break;
                        
                    }
                    nextChar = (char) charReader.read();
                }
            }
            // String Literal
            else if (nextChar == '"') {
                while(true) {
                    incrementLineCounter();
                    buffer += nextChar;
                    nextChar = (char)charReader.read();
                    if (nextChar == '"') {
                        // Adding the last quotes
                        buffer += nextChar;
                        // Adding string literal token to the list
                        tokenList.add(new ValueToken<>(TokenConst.STRLIT, lineCounter, buffer));
                        // Reset Buffer
                        buffer = "";
                        break;
                    }
                }
            }
            // Handle division sign
            else if (nextChar == '/') {
                // There are three cases
                // One char ahead to be space, / and *
                char oneAhead = (char) charReader.read();
                // Handle division operator
                if (Character.isWhitespace(oneAhead) || (Character.isDigit(oneAhead))) {
                    charReader.unread(oneAhead);
                    tokenList.add(new SyntaxToken(TokenConst.DIV, lineCounter));
                }
                // Handle line comment
                else if (oneAhead == '/') {
                    charReader.unread(oneAhead);
                    while(true) {
                        incrementLineCounter();
                        buffer += nextChar;
                        if (nextChar == '\n') {
                            buffer = "";
                            break;
                        }
                        else {
                            nextChar = (char) charReader.read();
                        }
                    }
                } // End handle line comments
                // Handle block comment
                else if (oneAhead == '*') {
                    
                    charReader.unread(oneAhead);
                    while (true) {
                        incrementLineCounter();
                        buffer += nextChar;
                        // Loops until */ the end of the comment block is reached
                        if ((buffer.length() >= 2) && (buffer.substring(buffer.length() - 2).equals("*/"))) {
                            buffer = "";
                            break;
                            
                        }
                        // Throws an exception if there is comment block finish symbol (*/)
                        else if (nextChar == (char) -1) {
                            throw (new IllegalArgumentException("Not finished comment block!"));
                        }
                        else {
                            
                            nextChar = (char) charReader.read();
                        }
                    }
                } // End handle Block comments
                
            }
            // Handle operator tokens
            else if ("=<>(){}[]~+-*".indexOf(nextChar) != - 1){
                while (true) {
                    incrementLineCounter();
                    buffer += nextChar;
                    char oneAhead = (char) charReader.read();
                    if ((LookTable.get(buffer) != null) && (oneAhead != '=')) {
                        charReader.unread(oneAhead);
                        tokenList.add(new SyntaxToken(LookTable.get(buffer), lineCounter));
                        buffer = "";
                        break;
                    }
                    else {
                        charReader.unread(oneAhead);
                        nextChar = (char) charReader.read();
                    }
                    
                }
                
            }// End handle division sign
            nextChar = (char)charReader.read();
            
        }
        return tokenList;
    }
    
    // Increments line counter when next char is \n
   private void incrementLineCounter(){
        if (nextChar == '\n') {
            lineCounter++;
        }
   }
    /*
    Precondition:
        HashMap<String, TokenConst>
        TokenConst
    Post condition:
        Populates HashMap table with TokenConsts
     */
    private void populateTable(){

           for(TokenConst t : TokenConst.values())
           {
			   LookTable.put(t.getLexeme(), t);
		   }
    }


}
