package nodomain.shvydkoy.chronicler.api.webfeed.parsingException;

final public class NotifyingParsingException extends Exception
{
    final private static String exceptionName = "NotifyingParsingException: ";
    final private String message;

    public NotifyingParsingException(String message)
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
