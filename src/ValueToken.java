/**
 * Created by Georgi Valkov
 *            Vojtech Martinek
 *            Dedric Sundby
 *            on 6/19/2017.
 *
 * Concepts of Programming Lang XLS Group 94 Summer Semester 2017
 * Professor: Jose Garrido
 */

public class ValueToken<T> extends SyntaxToken
{
	T value;

	public ValueToken(TokenConst type, int lineNum, T value)
	{
		super(type,lineNum);
		this.value = value;
	}
	
	public String getLexeme() {
		return this.value.toString();
	}
	
}