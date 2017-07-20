import java.util.*;
/**
 * Created by Georgi Valkov
 *            Vojtech Martinek
 *            Dedric Sundby
 *            on 7/4/2017.
 *
 * Concepts of Programming Lang XLS Group 94 Summer Semester 2017
 * Professor: Jose Garrido
 */


public class Parser {
    /* Data fields */
    private SyntaxToken current;
    private LinkedList<SyntaxToken> tokenList;
    private LinkedList<SyntaxToken> parsedTokenList = new LinkedList<>();
    private int errors;
    /* Constructor */
    public Parser(List l) {
        this.tokenList = new LinkedList<>(l);
        getToken();
    }
    // Returns whether or not a parsing was successful
    private boolean wasSuccessful() {
        return errors == 0;
    }
    // Returns a number of errors found
    public int errorCount() {
        return errors;
    }
    // Function that starts the parsing process
    public void parse() {
        errors = 0;
        start();
    }
    // Returns parsed token list
    // Must be called after parse()
    public LinkedList<SyntaxToken> getParsedTokenList() {
        return parsedTokenList;
    }
    // Gets next token from the list
    private void getToken() {
        if (!tokenList.isEmpty()) {
            current = tokenList.pop();
            parsedTokenList.addLast(current);
        } else if (current == null) {
            error("Reached EOF while parsing.");
            System.exit(0);
        } else {
            current = null;
        }
    }
    // Returns true if if the type of the current token
    // is the same as the given as a parameter token type
    private boolean check(TokenConst tType) {
        return current.type == tType;
    }

    //Looks for certain tokens to move forward, (optional found)
    private boolean found(TokenConst tType) {
        if (check(tType)) {
            getToken();
            return true;
        }
        return false;
    }

    //Generic error catching for required tokens (required "found")
    private boolean expect(TokenConst tType) {
        if (found(tType)) {
            return true;
        } else {
            error("Unexpected Token");
            return false;
        }
    }

    private void error(String message) {
        String err = "Error on Line " + current.lineNum + " at token ";
        if (current instanceof ValueToken) {
            err += "'" + ((ValueToken) current).value + "'";
        } else {
            err += "'" + current.type.getLexeme() + " (" + current.type.getDesc() + ")'";
        }
        err += ": " + message;
        System.out.println(err);

        errors++;
        getToken();
    }
    
    //////////////////////// Recursive Descent Parsers ////////////////////
    
    // Parses primary rule
    void primary() {
        if (found(TokenConst.LPAREN)) {
            arithmeticExp();
            expect(TokenConst.RPAREN);
        } else if (found(TokenConst.SUB)) {
            primary();
        } else if (found(TokenConst.INTLIT) || found(TokenConst.STRLIT) || found(TokenConst.ID)) {
        } else {
            error("Invalid primary.");
        }
    }
    // Parses mulexp reule
    void mulExp() {
        primary();
        if (found(TokenConst.MUL) || found(TokenConst.DIV)) {
            mulExp();
        }
    }
    // Parses arithmetic_exp rule
    private void arithmeticExp() {
        mulExp();
        if (found(TokenConst.ADD) || found(TokenConst.SUB)) {
            arithmeticExp();
        }
    }
    // Prints out an error if we relative operator not found
    private void relativeOp() {
        if (!(found(TokenConst.EQ) || found(TokenConst.NEQ) || found(TokenConst.LT) || found(TokenConst.LTE) || found(TokenConst.GT) || found(TokenConst.GTE))) {
            error("Missing valid logical operator");
        }
    }
    // Parses boolean expression rule
    private void booleanExp() {
        arithmeticExp();
        relativeOp();
        arithmeticExp();
    }
    // Parses args rule
    private void args() {
        if (!(found(TokenConst.ID) || found(TokenConst.STRLIT) || found(TokenConst.INTLIT))) {
            error("Invalid argument.");
        }
    }
    //not looped infinitely ( the grammar rules for the non-terminal would intersect if we code it in the order )
    private void argsList() {
        args();
        if (found(TokenConst.COMA)) {
            argsList();
        }
    }
    // Parses f_body rule
    private void fBody() {
        expect(TokenConst.BEGIN);
        statementList(TokenConst.ENDFUN);
    }
    //keep looking for statements in the statements list until we finfd the correct stop token
    private void statementList(TokenConst stopToken) {
        while (!found(stopToken) && !tokenList.isEmpty()) {
            statement();
        }
    }
    // Parses statement rule
    private void statement() {
        if (found(TokenConst.IF)) {
            ifStatement();
        } else if (found(TokenConst.WHILE)) {
            whileStatement();
        } else if (found(TokenConst.SET)) {
            assignStatement();
        } else if (found(TokenConst.REP)) {
            repeatStatement();
        } else if (found(TokenConst.DIS)) {
            printStatement();
        } else {
            error("Invalid start of statement.");
        }
    }
    // Parses if_statement rule
    private void ifStatement() {
        booleanExp();
        expect(TokenConst.THEN);
        statementList(TokenConst.ELSE);
        statementList(TokenConst.EIF);
    }
    // Parses while_statement rule
    private void whileStatement() {
        booleanExp();
        expect(TokenConst.DO);
        statementList(TokenConst.EWHILE);
    }
    // Parses assignment_statement rule
    private void assignStatement() {
        expect(TokenConst.ID);
        expect(TokenConst.ASSIGN);
        arithmeticExp();
    }
    // Parses repeat_statement rule
    private void repeatStatement() {
        statementList(TokenConst.UNTIL);
        booleanExp();
        expect(TokenConst.EREP);
    }
    // parses print_statement rule
    private void printStatement() {
        argsList();
    }
    // Parses start rule
    private void start() {
        symbols();
        forwardRefs();
        specifications();
        globals();
        implement();
    }
    // Parses symbols rule
    private void symbols() {
        if (found(TokenConst.SYM)) {
            symbolDef();
        }
    }
    // Parses symbol_def rule
    private void symbolDef() {
        expect(TokenConst.ID);
    }
    // Parses forward_refs
    private void forwardRefs() {
        if (found(TokenConst.FORW)) {
            frefs();
        }
    }
    // Parses frefs rule
    private void frefs() {
        found(TokenConst.REF);
        forwardList();
    }
    // Parses forward_list rule
    private void forwardList() {
        forwards();
    }
    // Parses forwards rule
    private void forwards() {
        funcMain();
        decParams();
    }
    // parses func_main rule
    private void funcMain() {
        if (found(TokenConst.FUN) && found(TokenConst.ID)) {
            operType();
        } else if (found(TokenConst.MAIN)) {
        }
    }
    // Parses dec_parameters rule
    private void decParams() {
    }
    // Parses oper_type rule
    private void operType() {
        expect(TokenConst.RTRN);
        chkPtr();
        chkArr();
        returnType();
    }
    // Parses chk_pointer rule
    private void chkPtr() {
        if (found(TokenConst.POINT)) {
        }
    }
    // Parses chk_array rule
    private void chkArr() {
        if (found(TokenConst.ARRAY)) {
            arrayDimList();
        }
    }
    // Parses ret_type rule
    private void returnType() {
        if (found(TokenConst.TYPE)) {
            typeName();
        } else if (found(TokenConst.STRUCT)) {
            expect(TokenConst.ID);
        } else {
            error("Invalid return type.");
        }
    }
    // Parses array_dim_list rule
    private void arrayDimList() {
        expect(TokenConst.LBRACK);
        do {
            arrayIndex();
            expect(TokenConst.RBRACK);
        } while (found(TokenConst.LBRACK));
    }
    // Parses type_name rule
    private void typeName() {
        if (!(found(TokenConst.MVOID) || found(TokenConst.INTEGER) || found(TokenConst.SHORT))) {
            error("Invalid type name.");
        }
    }
    // Parses array_index rule (without ICON)
    private void arrayIndex() {
        expect(TokenConst.ID);
    }
    // Parses specifications rule
    private void specifications() {
    }
    // Parses globals rule
    private void globals() {
        if (found(TokenConst.GLOB)) {
            declarations();
        }
    }
    // Parses implement rule
    private void implement() {
        if (!expect(TokenConst.IMPL)) {
            error("IMPLEMENTATIONS is required.");
        }
        functList();
    }
    // Parses declarations rule
    private void declarations() {
        expect(TokenConst.DECL);

    }
    // Parses funct_list rule
    private void functList() {
        while (!tokenList.isEmpty()) {
            functDef();
        }
    }
    // Parses funct_def rule
    private void functDef() {
        expect(TokenConst.FUN);
        mainHead();
        parameters();
        fBody();
    }
    // Parses main_head rule
    private void mainHead() {
        if (!(found(TokenConst.MAIN) || found(TokenConst.ID))) {
            error("Invalid method name/identifier.");
        }
    }
    // Parses parameters rule
    private void parameters() {
        if (found(TokenConst.PARAM)) {
            paramList();
        }
    }
    // Parses param_list rule
    private void paramList() {
        do {
            paramDef();
        } while (found(TokenConst.COMA));
    }
    // Parses param_def rule
    private void paramDef() {
        expect(TokenConst.ID);
        chkConst();
        chkPtr();
        chkArr();
        expect(TokenConst.TYPE);
        typeName();
    }
    // Parses chk_const rule
    private void chkConst() {
        found(TokenConst.CONST);
    }

}
