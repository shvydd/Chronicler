package nodomain.shvydkoy.chronicler.api.webfeed.parsingException;

final public class FailedParsingException extends Exception
{
    final private static String exceptionName = "FailedParsingException: ";
    final private String message;

    FailedParsingException(String message)
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


