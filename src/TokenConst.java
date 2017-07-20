/**
 * Created by Georgi Valkov
 *            Vojtech Martinek
 *            Dedric Sundby
 *            on 6/19/2017.
 *
 * Concepts of Programming Lang XLS Group 94 Summer Semester 2017
 * Professor: Jose Garrido
 */

public enum TokenConst {
    // Operators
    ADD("ADD",1001,"+"),
    SUB("SUB",1002,"-"),
    MUL("MUL",1003,"*"),
    DIV("DIV",1004,"/"),
    LTE("LTE",1005,"<="),
    GTE("GTE",1006,">="),
    LT ("LT",1007,"<"),
    GT("GT",1008,">"),
    EQ("EQ",1009,"=="),
    NEQ("NEQ",1010,"~="),
    ASSIGN("ASSIGN",1011,"="),
    LPAREN("LPAREN",1012,"("),
    RPAREN("RPAREN",1013,")"),
    LBRACK("LBRACK",1014,"["),
    RBRACK("RBRACK",1015,"]"),
    MATPOW("MATHPOW",1016,"^"),
    COMA("COMA",1017,","),
    
    // Reserved Words
    IMP("IMP",2000,"import"),
    SPEC("SPEC",2001,"specifications"),
    SYM("SYM",2002,"symbol"),
    FORW("FORW",2003,"forward"),
    REF("REF",2004,"references"),
    FUN("FUN",2005,"function"),
    POINT("POINT",2006,"pointer"),
    ARRAY("ARRAY",2007,"array"),
    TYPE("TYPE",2008,"type"),
    STRUCT("STRUCT",2009,"struct"),
    INT("INT",2010,"integer"),
    ENUM("ENUM",2011,"enum"),
    GLOB("GLOB",2012,"global"),
    DECL("DECL",2013,"declarations"),
    IMPL("IMPL",20014,"implementations"),
    MAIN("MAIN",2015,"main"),
    PARAM("PARAM",2016,"parameters"),
    CONST("CONST",2017,"constant"),
    BEGIN("BEGIN",2018,"begin"),
    ENDFUN("ENDFUN",2019,"endfun"),
    IF("IF",2020,"if"),
    THEN("THEN",2021,"then"),
    ELSE("ELSE",2022,"else"),
    SET("SET",2023,"set"),
    EIF("EIF",2024,"endif"),
    REP("REP",2025,"repeat"),
    UNTIL("UNTIL",2026,"until"),
    EREP("EREP",2027,"endrepeat"),
    DIS("DIS",2028,"display"),
    WHILE("WHILE",2029,"while"),
    EWHILE("EWHILE",2030,"endwhile"),
    OF("OF",2032,"of"),
    DEF("DEF",2032,"define"),
    FOR("FOR",2033,"for"),
    TO("TO",2034,"to"),
    DO("DO",2035,"do"),
    IN("IN",2036,"input"),
    RTRN("RTRN",2037,"return"),
    MVOID("MVOID",2038,"mvoid"),
    INTEGER("INTEGER",2039,"integer"),
    SHORT("SHORT",2040,"short"),
    
    // Value type tokens
    INTLIT("INTLIT",3000,"int_literal"),
    STRLIT("STRLIT",3001,"string_literal"),
    
    ID("ID",3002,"identifier");

    // Data Field
    private String desc;
    private int tokenCode;
    private String lexeme;
    //private int lineNumber = 0;

    /* Constructors */
    
    private TokenConst(String desc, int code, String lex) {
        this.desc = desc;
        this.tokenCode = code;
        this.lexeme = lex;
        //this.lineNumber = lineNum;
    }

    // Getters and Setters
    public String getDesc() {
        return desc;
    }

    public int getTokenCode() {
        return tokenCode;
    }

    public String getLexeme() {
        return lexeme;
    }
    
    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

    public String toString()
    {
		return lexeme + " --> " + desc + ":" + tokenCode;
	}
}
