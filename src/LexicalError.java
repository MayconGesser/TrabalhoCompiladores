public class LexicalError extends AnalysisError
{		
    public LexicalError(String msg, int position)
	{
        super(msg, position);
    }

    public LexicalError(String msg)
    {
        super(msg);
    }
    
    public LexicalError(String msg, char character, int position) {
    	super(msg,position);
    }
}
