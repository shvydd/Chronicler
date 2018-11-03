package nodomain.shvydkoy.chronicler.api.webfeed;

final public class ParsingFailedException extends Exception
{
    final private static String exceptionName = "ParsingFailedException: ";
    final private String message;

    public ParsingFailedException(String message)
    {
        this.message = message;
    }

    @Override
    public String toString()
    {
        return exceptionName + message;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    public String getExceptionName()
    {
        return exceptionName;
    }


}


