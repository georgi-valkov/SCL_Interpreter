/**
 * Created by Georgi Valkov
 *            Vojtech Martinek
 *            Dedric Sundby
 *            on 6/19/2017.
 *
 * Concepts of Programming Lang XLS Group 94 Summer Semester 2017
 * Professor: Jose Garrido
 */

public class SyntaxToken
{
	TokenConst type;
	int lineNum;

	public SyntaxToken(TokenConst type, int lineNum)
	{
		this.type = type;
		this.lineNum = lineNum;
	}

	public TokenConst getType()
	{
		return type;
	}
	
	public int getLineNum()
	{
		return lineNum;
	}
	
	public String toString()
	{
		return "Line " + lineNum + ": " + type.toString() + "\n";
	}
	
	public String getDescription() {
		return this.type.getDesc();
	}
	
	public String getLexeme() {
		return this.type.getLexeme();
	}
}