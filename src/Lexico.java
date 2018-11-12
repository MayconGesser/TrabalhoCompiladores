import java.util.HashMap;
import java.util.Set;

public class Lexico implements Constants
{
	private String sourceCode; 
	private int currentPosition; 
	private char currentChar;
	private final char space = ' ';
	private final HashMap<String,Integer> lexemesToIds = new HashMap<String, Integer>();
	//inicializador de instância
	{
		int offsetToId = 2; 
		for(int i = 0; i<lexemes.length; i++) {
			lexemesToIds.put(lexemes[i], i+offsetToId);
		}		
	}
	private final Set<String> possibleLexemes = lexemesToIds.keySet();
	
	public Lexico(String sourceCode) {
		this.sourceCode = sourceCode;
		currentPosition = 0;
	}
	
    public Token nextToken() throws LexicalError
    {
    	String potentialLexeme;
    	Token t = null;		//q lixo de código q esse lixo de linguagem obriga a escrever...
    	char[] sequenceBuffer = new char[10];
    	currentChar = sourceCode.charAt(currentPosition);
    	//todo Token, por mais valido q seja,
    	//eh separado por espaço
    	while(currentChar != space) {    		
    		sequenceBuffer[currentPosition] = currentChar;
    		currentPosition++;
    		currentChar = sourceCode.charAt(currentPosition);
    	}
    	potentialLexeme = new String(sequenceBuffer);
    	if(possibleLexemes.contains(potentialLexeme)) {
    		int tokenId = lexemesToIds.get(potentialLexeme);
    		t = new Token(tokenId,potentialLexeme,currentPosition);
    	}
    	else {
    		throw new LexicalError("Erro léxico na posição " + currentPosition, currentPosition);
    	}
    	return t;
    }
}
